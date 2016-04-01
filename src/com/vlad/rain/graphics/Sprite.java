package com.vlad.rain.graphics;

public class Sprite {
	
	public final int SIZE;
	private int x, y;
	public int[] pixels;
	private SpriteSheet sheet;
	
	public static Sprite brick = new Sprite(16, 6, 1, SpriteSheet.tiles);
	public static Sprite wood1 = new Sprite(16, 5, 0, SpriteSheet.tiles);
	public static Sprite wood_wall = new Sprite(16, 6, 0, SpriteSheet.tiles);
	public static Sprite star_big = new Sprite(16, 7, 0, SpriteSheet.tiles);
    public static Sprite star = new Sprite(16, 8, 0, SpriteSheet.tiles);
	
	public static Sprite voidSprite = new Sprite(16, 0xA4A4A4);
	
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
