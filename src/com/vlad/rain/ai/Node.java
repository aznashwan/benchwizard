package com.vlad.rain.ai;

/**
 * Created by Vlad on 06.03.2016.
 */
public class Node {

    private Node parent;
    private Node[] children;
    private int[] score;

    public Node(Node parent, Node ... children){
        this.parent = parent;
        this.children = children;
    }

    public Node parent(){
        return this.parent;
    }

    public Node[] children(){
        return this.children;
    }

    public int[] score(){
        return this.score;
    }

    public void update(int ... score){
        this.score = score;
    }

}
