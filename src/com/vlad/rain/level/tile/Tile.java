package com.vlad.rain.level.tile;

import com.vlad.rain.graphics.Screen;
import com.vlad.rain.graphics.Sprite;

public class Tile {
	
	public int x, y;
	public Sprite sprite;
	
	public static Tile grass = new GrassTile(Sprite.grass);
	public static Tile flower = new FlowerTile(Sprite.flower);
	public static Tile pebble = new PebbleTile(Sprite.pebble);
	public static Tile wall = new RockTile(Sprite.rock);
	public static Tile wood = new WoodTile(Sprite.wood1);
	public static Tile wood_wall = new RockTile(Sprite.wood_wall);
	public static Tile brick = new RockTile(Sprite.brick);
	public static Tile water = new RockTile(Sprite.water);
	public static Tile boat1 = new GrassTile(Sprite.boat1);
	public static Tile boat2 = new GrassTile(Sprite.boat2);
	
	public static Tile voidTile = new VoidTile(Sprite.voidSprite);
	
	public Tile(Sprite sprite){
		this.sprite = sprite;
	}
	
	public void render(int x, int y, Screen screen) {	
	}
	
	public boolean solid() {
		return false;
	}
	

}
