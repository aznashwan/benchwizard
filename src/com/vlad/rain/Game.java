package com.vlad.rain;

import com.vlad.rain.entity.mob.Player;
import com.vlad.rain.graphics.Screen;
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

@SuppressWarnings("unused")
public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
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
	private Player player;

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
		player = new Player(2*16,2*16,key);
		player.init(level);
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
		int frames = 0;
		int updates = 0;
		
		requestFocus();
		
		while(running){
			long now = System.nanoTime();
			delta = delta + (now - lastTime)/ns;
			lastTime = now;
			
			while(delta >= 1){
				update();
				updates++;
				delta--;
			}
			
			render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000){
				timer = timer + 1000;
                System.out.println(frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();

	}
	
	
	public void update(){
		key.update();
		player.update();
		level.update(player);
        frame.setTitle(title + "   " + SCORE);
	}
	
	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		
		screen.clear();
		int xScroll = player.x - screen.width / 2;
		int yScroll = player.y - screen.height / 2;
		level.render(xScroll, yScroll, screen);
		player.render(screen);

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
