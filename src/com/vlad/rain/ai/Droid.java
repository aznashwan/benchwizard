package com.vlad.rain.ai;

import com.vlad.rain.ai.data.Difficulty;
import com.vlad.rain.ai.data.Moves;
import com.vlad.rain.entity.mob.Player;
import com.vlad.rain.level.Level;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by vlad on 4/4/16.
 * Will handle move commands for both AI and human player
 */
public class Droid {

    private Tree tree;

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

    public int bestMove(){
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
