package com.vlad.rain.ai;

import com.sun.tools.javac.util.Pair;
import com.vlad.rain.ai.data.Characters;
import com.vlad.rain.ai.data.Moves;
import com.vlad.rain.level.Level;
import com.vlad.rain.level.SpawnLevel;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Vlad on 06.03.2016.
 */
public class Score {

    private int[][] valueMap;
    private HashMap<Characters, Pair<Integer, Integer>> playerPositions;
    private Level level;

    public Score(Level level){

        this.playerPositions = new HashMap<>();
        this.level = level;

        generateValueMap();

    }

    // creates value map 2D array from the level layout
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

    // return the score after all moves starting from current valueMap of the @current player
    public int score(Characters current, Moves[] m){

        int[][] valueMapClone = Arrays.stream(valueMap)
                .map(int[]::clone)
                .toArray(int[][]::new);
        HashMap<Characters, Pair<Integer, Integer>> playerPositionsClone = new HashMap<>(this.playerPositions);

        int score = 0;

        for(int index = 0; index < m.length; index++){

            int     playerX = playerPositionsClone.get(current).fst,
                    playerY = playerPositionsClone.get(current).snd;

            switch(m[index]){
                case UP:
                    if(playerY != 0) playerY--;
                    break;
                case DOWN:
                    if(playerY != level.getHeight()) playerY++;
                    break;
                case LEFT:
                    if(playerX != 0) playerX--;
                    break;
                case RIGHT:
                    if(playerX != level.getHeight()) playerX++;
                    break;
            }

            switch(level.getLayout()[playerX + playerY * level.getWidth()]){
                case 0xFF7F3300:
                case 0xFF404040:
                    continue;
            }

            score += this.processMap(valueMapClone, playerPositionsClone, current, playerX, playerY);

        }

        return score;
    }

    public void addPlayer(Characters name, Pair<Integer, Integer> position){

        this.playerPositions.put(name, position);

    }

    // processes the given map and player player positions
    // with the result of the move of player @current to position @X, @Y
    private int processMap(int[][] map, HashMap<Characters,Pair<Integer, Integer>> playerPositions,
                           Characters current, int X, int Y){

        int score = map[X][Y];

        if(score > 0 && score <=5) map[X][Y]--;
        else if(score == 25) map[X][Y] = 4;

        playerPositions.replace(current, new Pair<>(X, Y));

        return score;

    }

    // update valueMap with move m of character c --- assumes move is legal
    public void move(Characters c, Moves m){

        int     X = playerPositions.get(c).fst,
                Y = playerPositions.get(c).snd;

        switch(m){
            case LEFT:
                playerPositions.replace(c,new Pair<>(X-1,Y));
                break;
            case RIGHT:
                playerPositions.replace(c,new Pair<>(X+1,Y));
                break;
            case UP:
                playerPositions.replace(c,new Pair<>(X,Y-1));
                break;
            case DOWN:
                playerPositions.replace(c,new Pair<>(X,Y+1));
                break;
        }

        X = playerPositions.get(c).fst;
        Y = playerPositions.get(c).snd;

        switch(valueMap[X][Y]){

            case 0:
                break;
            case 25:
                valueMap[X][Y] = 4;
                break;
            default:
                valueMap[X][Y]--;

        }

    }

    public static void main(String[] args){

        Level level = new SpawnLevel("/level.png");
        Score score = new Score(level);
        score.addPlayer(Characters.A, new Pair<>(2, 2));

        // expect 155
        System.out.println(score.score(Characters.A,
                new Moves[]{Moves.DOWN, Moves.DOWN, Moves.DOWN, Moves.RIGHT, Moves.RIGHT, Moves.UP, Moves.UP}));

    }

}
