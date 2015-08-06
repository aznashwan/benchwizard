package com.vlad.rain.graphics;

public class Sprite {
	
	public final int SIZE;
	private int x, y;
	public int[] pixels;
	private SpriteSheet sheet;
	
	public static Sprite grass = new Sprite(16, 0, 0, SpriteSheet.tiles);
	public static Sprite flower = new Sprite(16, 1, 0, SpriteSheet.tiles);
	public static Sprite pebble = new Sprite(16, 2, 0, SpriteSheet.tiles);
	public static Sprite rock = new Sprite(16, 3, 0, SpriteSheet.tiles);
	public static Sprite brick = new Sprite(16, 6, 1, SpriteSheet.tiles);
	public static Sprite wood1 = new Sprite(16, 5, 0, SpriteSheet.tiles);
	public static Sprite wood2 = new Sprite(16, 5, 1, SpriteSheet.tiles);
	public static Sprite wood_wall = new Sprite(16, 6, 0, SpriteSheet.tiles);
	public static Sprite water = new Sprite(16, 4, 0, SpriteSheet.tiles);	

	public static Sprite boat1 = new Sprite(16, 1, 1, SpriteSheet.tiles);
	public static Sprite boat2 = new Sprite(16, 2, 1, SpriteSheet.tiles);
	
	public static Sprite voidSprite = new Sprite(16, 0x1B87E0);
	
	
	
	public static Sprite player_forward = new Sprite(16, 0, 0, SpriteSheet.player);
	public static Sprite player_back = new Sprite(16, 2, 0, SpriteSheet.player);
	public static Sprite player_left = new Sprite(16, 3, 0, SpriteSheet.player);
	public static Sprite player_right = new Sprite(16, 1, 0, SpriteSheet.player);
	public static Sprite player_forward1 = new Sprite(16, 0, 1, SpriteSheet.player);
	public static Sprite player_forward2 = new Sprite(16, 0, 2, SpriteSheet.player);
	public static Sprite player_back1 = new Sprite(16, 2, 1, SpriteSheet.player);
	public static Sprite player_back2 = new Sprite(16, 2, 2, SpriteSheet.player);
	public static Sprite player_left1 = new Sprite(16, 3, 1, SpriteSheet.player);
	public static Sprite player_left2 = new Sprite(16, 3, 2, SpriteSheet.player);
	public static Sprite player_right1 = new Sprite(16, 1, 1, SpriteSheet.player);
	public static Sprite player_right2 = new Sprite(16, 1, 2, SpriteSheet.player);
	
	public Sprite(int size, int x, int y, SpriteSheet sheet){
		SIZE = size;
		pixels =  new int[SIZE*SIZE];
		this.x = x * SIZE;
		this.y = y * SIZE;
		this.sheet = sheet;
		load();
	}
	
	public Sprite(int size, int color){
		SIZE = size;
		pixels = new int[SIZE*SIZE];
		setColor(color);
	}
	
	private void setColor(int color) {
		for (int i = 0; i < SIZE * SIZE; i++) {
			pixels[i] = color;
		}
	}
	
	private void load(){
		for (int y = 0; y < SIZE; y++)
			for (int x = 0; x < SIZE; x++)
				pixels[x + y * SIZE] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.SIZE];
	}

}
