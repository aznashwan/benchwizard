package com.vlad.rain;

import com.vlad.rain.entity.mob.Player;
import com.vlad.rain.graphics.Screen;
import com.vlad.rain.input.DummyKey;
import com.vlad.rain.input.Key;
import com.vlad.rain.level.Level;
import com.vlad.rain.level.SpawnLevel;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	// used as the key for players whose turn it currently isn't:
	private static final DummyKey dummyInput = new DummyKey();
	
	public static int width = 25 * 16;
	public static int height = width / 16 * 9;
	public static int scale = 3;
	
	public static String title = "FoodMaze";

    public static int SCORE = 0;
	
	private Thread thread;
	private JFrame frame;
	private Key key;
	private Level level;
	private boolean running = false;

	// the total number of players:
	private int numPlayers;

	// index of the player whose turn it currently is:
	private int currentPlayerIndex;

	// list of all the players:
	private ArrayList<Player> players = new ArrayList<>();

	// indicated that the current player is moving.
	private boolean currentPlayerMoved;

    private Screen screen;
	
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	
	
	public Game(){

		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		
		screen =  new Screen(width, height);
		
		frame = new JFrame();
		
		key = new Key();
		level = new SpawnLevel("/level.png");

		// NOTE: proper initialisation of numPlayers and current player index is vital:
		this.numPlayers = 2;
		this.currentPlayerIndex = 0;
		this.currentPlayerMoved = false;

		// add one player:
		Player p = new Player(2*16, 2*16, key);
		p.init(level);
		players.add(p);

		p = new Player(33*16, 35*16, this.dummyInput);
		p.init(level);
		players.add(p);

		addKeyListener(key);

	}
	
	public synchronized void start(){

		running = true;
		thread = new Thread(this, "Display");
		thread.start();

	}
	
	public synchronized void stop(){

		running = false;
		try{
			thread.join();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}

	}
	
	public void run(){

		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;

		requestFocus();

        while(running){
			long now = System.nanoTime();
			delta = delta + (now - lastTime)/ns;
			lastTime = now;

			while(delta >= 1){
				update();
				render();
				delta--;
			}
		}
		stop();

	}

	public void update(){

		// iterate through all the players:
		for (int i = 0; i < this.players.size(); i++) {
			Player p = this.players.get(i);

			p.input.update();
			p.update();
			level.update(p);

			// check if dealing with the current player:
			if (i == this.currentPlayerIndex) {
				// if so, check if he hasn't moved yet; but is just starting (i.e. has received input):
				if (!this.currentPlayerMoved && p.moving) {
					this.currentPlayerMoved = true;
					// then, switch off the current player's input:
					p.input = this.dummyInput;
				} else if (this.currentPlayerMoved && !p.moving) { // else, when the player has moved and stopped his turn:
					// also, switch the input to the next player:
					this.currentPlayerIndex = (i + 1) % this.numPlayers;
					this.players.get(this.currentPlayerIndex).input = this.key;
					this.currentPlayerMoved = false;
				}
			}
		}

		frame.setTitle(title + "   " + SCORE);

	}
	
	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}

		// render/center on current player.
		Player currentPlayer = this.players.get(this.currentPlayerIndex);
		
		screen.clear();
		int xScroll = currentPlayer.x - screen.width / 2;
		int yScroll = currentPlayer.y - screen.height / 2;
		level.render(xScroll, yScroll, screen);

		for (Player p : players) {
			p.render(screen);
		}

		for (int i = 0; i < pixels.length; i++)
			pixels[i] = screen.pixels[i];

        Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(image,0,0,getWidth(),getHeight(),null);
		
		g.dispose();
		bs.show();

	}

	public static synchronized void playMusic() {
		  new Thread(() -> {
            try {
              Clip clip = AudioSystem.getClip();
              AudioInputStream inputStream = AudioSystem.getAudioInputStream(
              Game.class.getResource("/sounds/background.wav"));
              clip.open(inputStream);
              clip.start();
            } catch (Exception e) {
              System.out.println(e.getMessage());
            }
          }).start();

	}

	public static void main(String[] args){

		Game game = new Game();

		game.frame.setResizable(true);
		game.frame.setTitle(title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);

		game.start();

	}
	
}
