package com.vlad.rain.graphics;

import java.util.Random;

//import com.vlad.rain.entity.mob.Player;
import com.vlad.rain.level.tile.Tile;

public class Screen {

	private final int MAP_SIZE = 64;
	//private final int MAP_SIZE_MASK = MAP_SIZE - 1;
	public int width, height;
	public int[] pixels;
	
	public int xOffset, yOffset;
	
	public int[] tiles = new int[MAP_SIZE*MAP_SIZE];
	
	private Random random =  new Random();
	
	public Screen(int width, int height){
		this.width = width;
		this.height = height;
		
		pixels = new int[(int) (width*height*1.1)];
		
		for (int i = 0; i < MAP_SIZE*MAP_SIZE; i++)
			tiles[i] = random.nextInt(0xffffff);
	}
	
	public void clear(){
		for (int i = 0; i < pixels.length; i++)
			pixels[i] = 0;
	}
	

	
	public void renderTile(int xp, int yp, Tile tile) {
		xp -= xOffset;
		yp -= yOffset;
		for (int y = 0; y < tile.sprite.SIZE; y++) {
			int ya = y + yp;
			for (int x = 0; x < tile.sprite.SIZE; x++) {
				int xa = x + xp;
				if (xa < -tile.sprite.SIZE || xa >= width || ya < 0 || ya >= width) break;
				if (xa < 0) xa = 0;
				pixels[xa + ya * width] = tile.sprite.pixels[x + y * tile.sprite.SIZE];
			}
		}
		
	}
	
	public void renderPlayer(int xp, int yp, Sprite sprite) {
		xp -= xOffset;
		yp -= yOffset;
		for (int y = 0; y < 16; y++) {
			int ya = y + yp;
			for (int x = 0; x < 16; x++) {
				int xa = x + xp;
				if (xa < -16 || xa >= width || ya < 0 || ya >= width) break;
				if (xa < 0) xa = 0;
				int col = sprite.pixels[x + y * 16];
				if (col != 0xffff00e5) pixels[xa + ya * width] = col;
			}
		}
	}
	
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	
}
