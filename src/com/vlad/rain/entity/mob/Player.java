package com.vlad.rain.entity.mob;

import com.vlad.rain.ai.data.Characters;
import com.vlad.rain.graphics.PlayerGraphics;
import com.vlad.rain.graphics.Screen;
import com.vlad.rain.graphics.Sprite;
import com.vlad.rain.input.Key;

public class Player extends Mob{

	public Key input;
	private Sprite sprite;
    private PlayerGraphics pg;
	private Characters character;

	private int anim = 0;
	private boolean walking = false;
	private int movesMade = 0;

	// used as an indicator if in the one-cell moving process.
	public boolean moving;

	// 1 = right, 2 = down, 3 = left, 4 = up
	private int going = 0;

    @SuppressWarnings("unused")
    public Player(Key input) {
		this.input = input;
	}
	
	public Player(int x, int y, Key input, Characters character) {
		this.x = x;
		this.y = y;
		this.input = input;
        this.character = character;
        pg = new PlayerGraphics(character);
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
            int moveDistance = 16;
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
			sprite = pg.forward();
			if (walking)
				if (anim % 20 > 10)
					sprite = pg.forward1();
				else sprite = pg.forward2();
		}
		
		if(dir == 1) {
			sprite = pg.right();
			if (walking)
				if (anim % 20 > 10)
					sprite = pg.right1();
				else sprite = pg.right2();
		}
		
		if(dir == 2) {
			sprite = pg.back();
			if (walking)
				if (anim % 20 > 10)
					sprite = pg.back1();
				else sprite = pg.back2();
		}
		
		if(dir == 3) {
			sprite = pg.left();
			if (walking)
				if (anim % 20 > 10)
					sprite = pg.left1();
				else sprite = pg.left2();
		}
		
		screen.renderPlayer(x, y, sprite);
	}

    public Characters getCharacter(){
        return this.character;
    }

	public String toString(){
        return this.character + ": " + this.x / 16 + ", " + this.y / 16;
    }
	
	
}
