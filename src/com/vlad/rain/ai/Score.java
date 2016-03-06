package com.vlad.rain.ai;

import com.vlad.rain.ai.data.Characters;
import com.vlad.rain.ai.data.Moves;
import com.vlad.rain.level.Level;

/**
 * Created by Vlad on 06.03.2016.
 */
public class Score {
    private static int[][] valueMap;

    public Score(Level level){
        valueMap = this.generateValueMap(level);
    }

    // parse level file and generate value map
    private int[][] generateValueMap(Level level) {
        // TODO add stars to Level???
        return null;
    }

    // return the score after all moves starting from current valueMap
    public int[] score(Characters[] c, Moves[] m){
        return null;
    }

    // update valueMap with move m of character c
    public void move(Characters c, Moves m){

    }

}
