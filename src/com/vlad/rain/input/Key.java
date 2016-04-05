package com.vlad.rain.input;

import com.vlad.rain.ai.Droid;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Key implements KeyListener {
	
	private boolean[] keys = new boolean[120];
	public boolean up, down, left, right;

	protected Droid droid;

	public Key(Droid droid){
		this.droid = droid;
	}
	
	public void update(){
		up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
	}

	public void keyPressed(KeyEvent e) {
		droid.actualMove(e.getKeyCode());
		keys[e.getKeyCode()] = true;
	}

	
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	
	public void keyTyped(KeyEvent e) {
		
	}

}
