package com.vlad.rain.entity.mob;

import com.sun.glass.ui.SystemClipboard;
import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;
import com.vlad.rain.graphics.Screen;
import com.vlad.rain.graphics.Sprite;
import com.vlad.rain.input.Key;

public class Player extends Mob{

	private Key input;
	private Sprite sprite;
	private int anim = 0;
	private boolean walking = false;
	private int movesMade = 0;

	// used as an indicator if in the one-cell moving process.
	private boolean moving;

	// 1 = right, 2 = down, 3 = left, 4 = up
	private int going = 0;

	// the move distace (1 box = 16x16)
	private static int moveDistance = 16;

	public Player(Key input) {
		this.input = input;
	}
	
	public Player(int x, int y, Key input) {
		this.x = x;
		this.y = y;
		this.input = input;
	}
	
	public int getXPosition(){
		return this.x;
	}
	
	public int getYPosition(){
		return this.y;
	}
	
	public void update() {
		int xa = 0, ya = 0;
		
		if (anim < 7500) anim++; 
			else anim = 0;

		if (!this.moving) { // guard against multiple captures
			// try capture input:
			if (input.up) this.going = 4;
			if (input.down) this.going = 2;
			if (input.left) this.going = 3;
			if (input.right) this.going = 1;

			// check if any input given:
			if (this.going != 0) {
				this.moving = true;
			}
		}

		if (this.moving) {
			// if in the process of moving; check if move completed:
			if (this.movesMade == moveDistance) {
				// if so, set state to not moving:
				this.going = 0;
				this.movesMade = 0;
				this.moving = false;
			} else {
				// move in the going direction:
				switch (this.going) {
					case 1:
						xa++;
						break;
					case 2:
						ya++;
						break;
					case 3:
						xa--;
						break;
					case 4:
						ya--;
						break;
					default:
						break;
				}
				this.movesMade = this.movesMade + 1;
			}
		}


		if (xa != 0 || ya != 0) {
			move(xa, ya);
			walking = true;
		}
		else walking = false;
	}
	
	public void render(Screen screen) {
		if(dir == 0) {
			sprite = Sprite.player_forward;
			if (walking)
				if (anim % 20 > 10)
					sprite = Sprite.player_forward1;
				else sprite = Sprite.player_forward2;
		}
		
		if(dir == 1) {
			sprite = Sprite.player_right;
			if (walking)
				if (anim % 20 > 10)
					sprite = Sprite.player_right1;
				else sprite = Sprite.player_right2;
		}
		
		if(dir == 2) {
			sprite = Sprite.player_back;
			if (walking)
				if (anim % 20 > 10)
					sprite = Sprite.player_back1;
				else sprite = Sprite.player_back2;
		}
		
		if(dir == 3) {
			sprite = Sprite.player_left;
			if (walking)
				if (anim % 20 > 10)
					sprite = Sprite.player_left1;
				else sprite = Sprite.player_left2;
		}
		
		screen.renderPlayer(x, y, sprite);
	}
	
	
}
