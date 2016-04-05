package com.vlad.rain.ai;

import com.vlad.rain.ai.data.Difficulty;
import com.vlad.rain.ai.data.Moves;
import com.vlad.rain.entity.mob.Player;
import com.vlad.rain.input.Key;
import com.vlad.rain.level.Level;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 * Will handle the move commands for both AI and human player
 */
public class Droid {

    // the game tree:
    private Tree tree;

    /**
     * The main constructor of a {@link Droid} oject, it takes a reference to the level
     * (i.e the map), a list with all the {@link Player}s, and the {@link Difficulty} the
     * {@link Player} should live up to.
     * <p>
     * Depending on the provided {@link Difficulty} level, the {@link Player} will either be
     * making erratic, random decisions(EASY); better ones calculated by moving 3 level deep in the
     * game{@link Tree}, or play HARD by looking 6 level deep in the game {@link Tree}.
     */
    public Droid(Level level, ArrayList<Player> players, Difficulty difficulty){
        switch(difficulty){
            case EASY: break;
            case MEDIUM:
                this.tree = new Tree(level, players, 3);
                break;
            case HARD:
                this.tree = new Tree(level, players, 6);
                break;
        }
    }

    /**
     * Retuns the {@link int} key event code representing the desired move.
     * This is later fed directly into the {@link Key} object of said player in order to
     * make the {@link Droid}s move as per their behaviour.
     * <p>
     * @return the {@link int} key code of the best move the player may take.
     * @see {@link int}
     */
    public int bestMove(){
        int[] keys = {KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN};

        // if EASY => random key presses
        if (tree == null) return keys[new Random().nextInt(3)];

        switch(tree.getBestMove(tree.getCurrentCharacter())){
            case LEFT:
                return KeyEvent.VK_LEFT;
            case RIGHT:
                return KeyEvent.VK_RIGHT;
            case UP:
                return KeyEvent.VK_UP;
            default:
                return KeyEvent.VK_DOWN;
        }
    }

    // updates the AI tree based on the received move KeyEvent

    /**
     * Mutates the game {@link Tree} following the move of a {@link Player}.
     * <p>
     * Following a given keycode representing the key of the last move of a player;
     * translates the key to a {@link Moves} and and updates the game {@link Tree} accordingly.
     * @param move the {@link int} move.
     */
    public void actualMove(int move){
        switch(move){
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                tree.actuallyMove(Moves.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                tree.actuallyMove(Moves.RIGHT);
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                tree.actuallyMove(Moves.UP);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                tree.actuallyMove(Moves.DOWN);
                break;
        }
    }

}
