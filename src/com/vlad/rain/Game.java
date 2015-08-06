package com.vlad.rain;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.vlad.rain.entity.mob.Player;
import com.vlad.rain.graphics.Screen;
import com.vlad.rain.input.Key;
import com.vlad.rain.level.Level;
import com.vlad.rain.level.SpawnLevel;

@SuppressWarnings("unused")
public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private static int score = 0;
	private static int scoreCounter = 0;
	private static int scoreCounter_start = 0;
	
	public static int width = 300;
	public static int height = width / 16 * 9;
	public static int scale = 3;
	
	public static String title = "BenchWizard";
	
	private Thread thread;
	private JFrame frame;
	private Key key;
	private Level level;
	private boolean running = false;
	private Player player;
	
	private Graphics g;
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
		player = new Player(4,11,key);
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
				System.out.println(updates + " ups, " + frames + " fps");
				frame.setTitle(title + "    " + frames + " fps");
				
				scoreCounter_start++;
				if(scoreCounter_start > 5) {
					score += frames;
					scoreCounter++;
				}

				if(player.getXPosition() > 610 && player.getYPosition() > 616) {
					
					int finalFPS = score/scoreCounter;
					String message = "Score: " + finalFPS + ". ";
					if(finalFPS < 50) message += "How did you make this run on a toaster?";
					else if(finalFPS < 100) message += "2004 was a great year to buy a PC.";
					else if(finalFPS < 300) message += "Your netbook runs within normal parameters.";
					else if(finalFPS < 700) message += "You can probably run most indie games.";
					else if(finalFPS < 1000) message += "Not bad. You can run most games.";
					else if(finalFPS < 1800) message += "You've got a decent gaming rig.";
					else if(finalFPS < 2400) message += "Ultra settings all the way.";
					else if(finalFPS < 3000) message += "Your PC could feed a small african country for 3.78 weeks.";
					else message += "I hope your trip from Alpha Centauri was pleasant.";
					
					JOptionPane.showMessageDialog(null, message);

					System.exit(ABORT);
				}
				
				updates = 0;
				frames = 0;
			}
		}
		stop();

	}
	
	
	public void update(){
		key.update();
		player.update();
		
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
		
		g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(image,0,0,getWidth(),getHeight(),null);
		
		g.dispose();
		bs.show();
	}
	
	
	public static synchronized void playSound() {
		  new Thread(new Runnable() {
		    public void run() {
		      try {
		        Clip clip = AudioSystem.getClip();
		        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
		        Game.class.getResource("/sounds/background.wav"));
		        clip.open(inputStream);
		        clip.start(); 
		      } catch (Exception e) {
		        System.out.println(e.getMessage());
		      }
		    }
		  }).start();
	}

	
	public static void main(String[] args){

		Game game = new Game();

		game.frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		game.frame.setUndecorated(true);
		
		playSound();
		
		game.frame.setResizable(true);
		game.frame.setTitle(title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);

		game.start();
		
		String message = "Use WASD or the arrow keys to find the exit!";
		JOptionPane.showMessageDialog(null, message);
		
	}
	
}
