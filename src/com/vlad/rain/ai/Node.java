package com.vlad.rain.ai;

import com.vlad.rain.ai.data.Characters;
import com.vlad.rain.ai.data.Moves;

/**
 * Created by Vlad on 06.03.2016.
 */
public class Node {

    private Node parent;
    private Node[] children;
    private Score score;
    private Characters currentCharacter;
    private Moves currentMove;

    public Node(Score score, Node parent, Node ... children){
        this.score = score;
        this.parent = parent;
        this.children = children;
    }

    public Node parent(){
        return this.parent;
    }

    public Node[] children(){
        return this.children;
    }

}
