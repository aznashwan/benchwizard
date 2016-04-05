package com.vlad.rain.ai;

import com.vlad.rain.ai.data.Characters;
import com.vlad.rain.ai.data.Moves;
import com.vlad.rain.entity.mob.Player;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Node represents a {@link Node} in the current
 */
public class Node {

    // the maximum depth of the game {@link Tree}:
    private static int maxDepth = 4;

    // the current depth into the game {@link Tree}:
    private int currentDepth;

    // the list of all the {@link Player}s:
    private static ArrayList<Player> characters;

    // the list of points:
    int[] points;

    // the parent {@link Node}:
    private Node parent;

    // the children of any given node; either an empty list or a list
    // of exactly 4 nodes:
    private Node[] children;

    // the {@link Score} computing object:
    private Score score;

    // the current {@link Character} whose turn it is:
    private Characters currentCharacter;

    private Moves currentMove;


    /**
     * Simple constructor for {@link Node} objects.
     * @param points the list of points.
     * @param score the {@link Score} object.
     * @param parent the parent {@link Node} object.
     * @param currentDepth the depth of the {@link Node} in the game {@link Tree}.
     */
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

    /**
     * Returns the next {@link Character} after the current one.
     * @return {@link Character}.
     */
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

    // generates the children of the current Node

    /**
     * Generates all the children of the current {@link Node}.
     * <p>
     * A {@link Node}'s children consist of 4 {@link Node}s, each one representing (when
     * traversed to that particular point), the state of the game at that given {@link Node}.
     */
    public void generateChildren(){

        // if the children were not computed yet:
        if (this.children == null){

            // then, spawn the 4 child Nodes:
            Node left, right, up, down;

            // for the subnode representing a move to the left:
            // make a copy of the list of points, and add the score of the Node representing a
            // move to the LEFT:
            int[] tempPoints = Arrays.copyOf(points, points.length);
            tempPoints[currentCharacter.ordinal()] += this.score.score(currentCharacter, new Moves[]{Moves.LEFT});

            // make a new Score corresponding to the LEFT subnode:
            Score scoreLeft = new Score(this.score.getLevel(), this.score.getValueMap(), score.getPlayerPositions());
            scoreLeft.move(currentCharacter, Moves.LEFT);

            // finally, construct the new Node object:
            left = new Node(tempPoints, scoreLeft, this, currentDepth + 1);

            // same for the cases where we are going towards the RiGHT, UP or DOWN respectively...
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

            // finally, add the list of children Nodes:
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

    public void setChildren(Node[] children){
        this.children = children;
    }

    public Node[] children(){
        return this.children;
    }

    public Characters getCurrentCharacter(){
        return this.currentCharacter;
    }

    /**
     * Decreases the current depth for the Node (as in when the Tree is getting truncated),
     * and, if currently on a leaf node which has moved upwards, forces the generation
     * of all the children Nodes.
     */
    public void decreaseCurrentDepth(){
        currentDepth--;
        if (this.children == null) generateChildren();
        else {
            for (Node child : this.children) child.decreaseCurrentDepth();
        }
    }

}
