package com.vlad.rain.ai;

import com.vlad.rain.ai.data.Characters;
import com.vlad.rain.ai.data.Moves;
import com.vlad.rain.entity.mob.Player;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Vlad on 06.03.2016.
 */
public class Node {

    int[] points;
    private Node parent;
    private Node[] children;
    private Score score;
    private Characters currentCharacter;
    private Moves currentMove;
    private static int maxDepth = 4;
    private int currentDepth;

    private static ArrayList<Player> characters;


    public Node(int[] points, Score score, Node parent, int currentDepth){

        this.currentDepth = currentDepth;

        this.points = points;
        this.score = score;
        this.parent = parent;

        currentCharacter = parent == null ? characters.get(0).getCharacter() : parent.nextCharacter();

        if(currentDepth < maxDepth) {
            generateChildren();
        }

    }

    public static void setCharacters(ArrayList<Player> chrt){
        characters = chrt;
    }

    private Characters nextCharacter() {
        switch(currentCharacter){
            case A:
                return Characters.B;
            case B:
                return Characters.C;
            case C:
                return Characters.D;
            default:
                return Characters.A;
        }
    }

    public void generateChildren(){

        if (this.children == null){

            Node left, right, up, down;

            int[] tempPoints = Arrays.copyOf(points, points.length);
            tempPoints[currentCharacter.ordinal()] += this.score.score(currentCharacter, new Moves[]{Moves.LEFT});
            Score scoreLeft = new Score(this.score.getLevel(), this.score.getValueMap(), score.getPlayerPositions());
            scoreLeft.move(currentCharacter, Moves.LEFT);
            left = new Node(tempPoints, scoreLeft, this, currentDepth + 1);

            tempPoints = Arrays.copyOf(points, points.length);
            tempPoints[currentCharacter.ordinal()] += this.score.score(currentCharacter, new Moves[]{Moves.RIGHT});
            Score scoreRight = new Score(this.score.getLevel(), this.score.getValueMap(), score.getPlayerPositions());
            scoreRight.move(currentCharacter, Moves.RIGHT);
            right = new Node(tempPoints, scoreRight, this, currentDepth + 1);

            tempPoints = Arrays.copyOf(points, points.length);
            tempPoints[currentCharacter.ordinal()] += this.score.score(currentCharacter, new Moves[]{Moves.UP});
            Score scoreUp = new Score(this.score.getLevel(), this.score.getValueMap(), score.getPlayerPositions());
            scoreUp.move(currentCharacter, Moves.UP);
            up = new Node(tempPoints, scoreUp, this, currentDepth + 1);

            tempPoints = Arrays.copyOf(points, points.length);
            tempPoints[currentCharacter.ordinal()] += this.score.score(currentCharacter, new Moves[]{Moves.DOWN});
            Score scoreDown = new Score(this.score.getLevel(), this.score.getValueMap(), score.getPlayerPositions());
            scoreDown.move(currentCharacter, Moves.DOWN);
            down = new Node(tempPoints, scoreDown, this, currentDepth + 1);

            this.children = new Node[]{left, right, up, down};

        }

    }

    public static void setMaxDepth(int depth){
        maxDepth = depth;
    }

    public Node parent(){
        return this.parent;
    }

    public void setParent(Node parent){
        this.parent = parent;
    }

    public Node[] children(){
        return this.children;
    }

    public Characters getCurrentCharacter(){
        return this.currentCharacter;
    }

    public void decreaseCurrentDepth(){
        currentDepth--;
        if (this.children == null) generateChildren();
        else {
            for (Node child : this.children) child.decreaseCurrentDepth();
        }
    }

}
