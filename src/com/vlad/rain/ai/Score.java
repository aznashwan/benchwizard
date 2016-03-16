package com.vlad.rain.ai;

import com.vlad.rain.ai.data.Characters;
import com.vlad.rain.ai.data.Moves;
import com.vlad.rain.level.Level;
import com.vlad.rain.level.SpawnLevel;

import java.util.Arrays;

/**
 * Created by Vlad on 06.03.2016.
 */
public class Score {

    private static int[][] valueMap;
    private Level level;

    public Score(Level level){

        this.level = level;

        generateValueMap();

    }

    // parse level file and generate value map
    private void generateValueMap() {

        valueMap = new int[level.getWidth()][level.getHeight()];

        int[] layout = level.getLayout();

        for (int x = 0; x < level.getWidth(); x++){
            for (int y = 0; y < level.getHeight(); y++){
                int value = 0;
                switch(layout[x + y * level.getWidth()]) {
                    case 0xFFCE5200: // floor
                        value = 5;
                        break;
                    case 0xFFFFD200: // star
                        value = 25;
                        break;
                }

                valueMap[x][y] = value;
            }
        }

    }

    // return the score after all moves starting from current valueMap (ONE TURN -> one move / player)
    public int[] score(Moves[] m){

        int[][] valueMapClone = Arrays.stream(valueMap)
                .map(int[]::clone)
                .toArray(int[][]::new);

        int[] score = new int[m.length];

        for(int index = 0; index < m.length; index++){
            // TODO add players to valueMap????
        }

        return score;
    }

    // update valueMap with move m of character c
    public void move(Characters c, Moves m){

    }

    public static void main(String[] args){
        Level level = new SpawnLevel("/level.png");
        Score score = new Score(level);

        score.score(null);

    }

}
