package com.vlad.rain.level;

import com.vlad.rain.graphics.Screen;
import com.vlad.rain.level.tile.Tile;

public class Level {
	
	protected int width, height;
	protected int[] tilesInt;
	protected int[] tiles;
	
	public Level(int width, int height){
		
		this.width = width;
		this.height = height;
		this.tilesInt = new int[width * height];
		generateLevel();
		
	}
	
	protected void generateLevel(){
		
	}
		
	
	public Level(String path){
		
		loadLevel(path);
		generateLevel();
		
	}
	
	protected void loadLevel(String path){
		
	}
	
	public void update(){
		
	}
	
	/*
	private void time(){
		
	}
	*/
	
	
	public void render(int xScroll, int yScroll, Screen screen){
		screen.setOffset(xScroll, yScroll);
		int x0 = xScroll >> 4;
		int x1 = (xScroll + screen.width + 16) >> 4;
		int y0 = yScroll >> 4;
		int y1 = (yScroll + screen.height + 16) >> 4;
		
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				getTile(x, y).render(x, y, screen);
			}
		}
			
	}
	
	// Grass:  0xFFB6FF00
	// Flower: 0xFFFFD800
	// Pebble: 0xFF808080
	// Wall:  0xFF404040
	// Wood:   0xFFCE5200
	// Brick:   0xFF7F3300
	// Boat1: 0xFFFF00E1
	// Boat2: 0xFF00FFF6
	public Tile getTile(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) return Tile.water;
		if (tiles[x + y * width] == 0xFFB6FF00) return Tile.grass;
		if (tiles[x + y * width] == 0xFFFFD800) return Tile.flower;
		if (tiles[x + y * width] == 0xFF808080) return Tile.pebble;
		if (tiles[x + y * width] == 0xFF404040) return Tile.wood_wall;
		if (tiles[x + y * width] == 0xFFCE5200) return Tile.wood;
		if (tiles[x + y * width] == 0xFF7F3300) return Tile.brick;
		if (tiles[x + y * width] == 0xFFFF00E1) return Tile.boat1;
		if (tiles[x + y * width] == 0xFF00FFF6) return Tile.boat2;
		return Tile.water;
	}

}
