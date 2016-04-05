package com.vlad.rain.input;

import com.vlad.rain.ai.Droid;

/**
 * Created by nashwan on 4/1/16.
 * A DummyKey is a Key which is never pressed.
 * It will be given to Players which are out of turn, and thus accept no input.
 */
public class DummyKey extends Key {

    public DummyKey(Droid droid){
        super(droid);
    }

    public void update() {
        this.up = false;
        this.down = false;
        this.left = false;
        this.right = false;
    }
}
