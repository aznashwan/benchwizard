package com.vlad.rain.graphics;

import com.vlad.rain.ai.data.Characters;

/**
 * Created by vlad on 4/1/16.
 */
public class PlayerGraphics {

    private SpriteSheet player;

    public PlayerGraphics(Characters c){

        String[] heroes = {"wizard", "warlock", "knight", "sir"};

        switch(c){
            case A:
                player = new SpriteSheet("/" + heroes[0] +".png", 256);
                break;
            case B:
                player = new SpriteSheet("/" + heroes[1] +".png", 256);
                break;
            case C:
                player = new SpriteSheet("/" + heroes[2] +".png", 256);
                break;
            case D:
                player = new SpriteSheet("/" + heroes[3] +".png", 256);
                break;
        }

    }

    public Sprite forward(){
        return new Sprite(16, 0, 0, player);
    }

    public Sprite forward1(){
        return new Sprite(16, 0, 1, player);
    }

    public Sprite forward2(){
        return new Sprite(16, 0, 2, player);
    }

    public Sprite back(){
        return new Sprite(16, 2, 0, player);
    }

    public Sprite back1(){
        return new Sprite(16, 2, 1, player);
    }

    public Sprite back2(){
        return new Sprite(16, 2, 2, player);
    }

    public Sprite right(){
        return new Sprite(16, 1, 0, player);
    }

    public Sprite right1(){
        return new Sprite(16, 1, 1, player);
    }

    public Sprite right2(){
        return new Sprite(16, 1, 2, player);
    }

    public Sprite left(){
        return new Sprite(16, 3, 0, player);
    }

    public Sprite left1(){
        return new Sprite(16, 3, 1, player);
    }

    public Sprite left2(){
        return new Sprite(16, 3, 2, player);
    }

}
