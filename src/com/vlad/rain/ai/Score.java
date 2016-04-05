package com.vlad.rain.ai;

import com.sun.tools.javac.util.Pair;
import com.vlad.rain.ai.data.Characters;
import com.vlad.rain.ai.data.Moves;
import com.vlad.rain.level.Level;
import com.vlad.rain.level.SpawnLevel;

import java.util.Arrays;
import java.util.HashMap;

/**
 * A {@link Score} object keeps track of scores.
 */
public class Score {

    // an internal 'snapshot' of the current state of the map:
    private int[][] valueMap;

    // a mapping between Characters and their respective x, y positions:
    private HashMap<Characters, Pair<Integer, Integer>> playerPositions;

    // the game {@link Level} we are working on.
    private Level level;

    public Score(Level level){

        this.playerPositions = new HashMap<>();
        this.level = level;

        generateValueMap();

    }

    public Score(Level level, int[][] valueMap, HashMap<Characters, Pair<Integer, Integer>> playerPositions){
        this.level = level;
        this.valueMap = valueMap;
        this.playerPositions = playerPositions;
    }

    /**
     * Generates the int[][] map the {@link Score} object uses to keep
     * track of the state of the game and later compute relevant move scores.
     */
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

    /**
     * returns the {@link int} score for the given {@link Character} after performing all
     * of the provided {@link Moves}.
     * @param current the current {@link Character} for whom the score should be computed.
     * @param m a list of {@link Moves}.
     * @return @{link int} representing the score for the given {@link Character}.
     */
    public int score(Characters current, Moves[] m){

//        System.out.println("Current: " + current + " moves " + m[0]);

        // this line of code is nice; I like this line of code:
        int[][] valueMapClone = Arrays.stream(valueMap)
                .map(int[]::clone)
                .toArray(int[][]::new);
        HashMap<Characters, Pair<Integer, Integer>> playerPositionsClone = new HashMap<>(this.playerPositions);

        int score = 0;

        // for each move; execute execute the appropriate action:
        for (Moves aM : m) {

            int playerX = playerPositionsClone.get(current).fst,
                    playerY = playerPositionsClone.get(current).snd;

            switch (aM) {
                case UP:
                    if (playerY != 0) playerY--;
                    break;
                case DOWN:
                    if (playerY != level.getHeight()) playerY++;
                    break;
                case LEFT:
                    if (playerX != 0) playerX--;
                    break;
                case RIGHT:
                    if (playerX != level.getHeight()) playerX++;
                    break;
            }

            switch (level.getLayout()[playerX + playerY * level.getWidth()]) {
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

    /**
     * Process the internal map representation {@link int[][]} according to the move of
     * the player position.
     * @param map the int[][] map
     * @param playerPositions the map of {@link Characters} to their respective xOy coordinates.
     * @param current the current {@link Characters}.
     * @param X the {@link int} X position of the {@link Characters}.
     * @param Y the {@link int} Y position of the {@link Characters}.
     * @return
     */
    private int processMap(int[][] map, HashMap<Characters,Pair<Integer, Integer>> playerPositions,
                           Characters current, int X, int Y){

        int score = map[X][Y];

        if(score > 0 && score <=5) map[X][Y]--;
        else if(score == 25) map[X][Y] = 4;

        playerPositions.replace(current, new Pair<>(X, Y));

        return score;

    }

    // update valueMap with move m of character c: assumes move is legal

    /**
     * Updates the mapping between the {@link Characters} and their positions.
     * <P>
     * NOTE: assumes the provided {@link Moves} has already been validated.
     * @param c the moving {@link Characters}.
     * @param m the {@link Moves} the {@link Characters} has made.
     */
    public void move(Characters c, Moves m){

        int     X = playerPositions.get(c).fst,
                Y = playerPositions.get(c).snd;

        // walls cannot be moved through
        switch (level.getLayout()[X + Y * level.getWidth()]) {
            case 0xFF7F3300:
            case 0xFF404040:
                return;
        }

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

    public Level getLevel(){
        return this.level;
    }

    public int[][] getValueMap(){
        return this.valueMap;
    }

    public HashMap<Characters, Pair<Integer, Integer>> getPlayerPositions(){
        return this.playerPositions;
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
