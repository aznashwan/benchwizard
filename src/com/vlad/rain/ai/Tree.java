package com.vlad.rain.ai;

import com.sun.tools.javac.util.Pair;
import com.vlad.rain.ai.data.Characters;
import com.vlad.rain.ai.data.Moves;
import com.vlad.rain.entity.mob.Player;
import com.vlad.rain.level.Level;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by vlad on 4/4/16.
 */
public class Tree {

    private Node root;
    private int maxDepth;

    public Tree(Level level, ArrayList<Player> players, int maxDepth){

        this.maxDepth = maxDepth;
        Node.setCharacters(players);
        Node.setMaxDepth(maxDepth);

        Score score = new Score(level);
        for(Player p : players){
            score.addPlayer(p.getCharacter(), new Pair<>(p.getXPosition() / 16, p.getYPosition() / 16));
        }

        root = new Node(new int[players.size()], score, null, 0);

    }

    public Node getRoot(){
        return this.root;
    }

    // update the current tree with the received move
    public void actuallyMove(Moves move){
        switch(move){
            case LEFT:
                this.root = this.root.children()[0];
                break;
            case RIGHT:
                this.root = this.root.children()[1];
                break;
            case UP:
                this.root = this.root.children()[2];
                break;
            case DOWN:
                this.root = this.root.children()[3];
                break;
        }
        // no more references to useless part of the tree => will get GC
        this.root.setChildren(null);
        this.root.setParent(null);

        // decrease the current depth for all remaining nodes and generate next tree layer
        this.root.decreaseCurrentDepth();
    }

    // returns the best move the given player
    public Moves getBestMove(Characters character){

        int index = character.ordinal();
        ArrayList<Node> leafs = this.getLeafNodes(root);

        int max = 0;
        for(Node leaf : leafs){
//            System.out.println(leaf.points[0] + " " + leaf.points[1] + " " + leaf.points[2] + " " + leaf.points[3]);
            if(leaf.points[index] > max) max = leaf.points[index];
        }

        ArrayList<Node> maxValues = new ArrayList<>();

        for(Node leaf : leafs){
            if(leaf.points[index] == max) maxValues.add(leaf);
        }

        Node bestNode = this.bestAdversaryDeviation(this.minAdversaryScoreSum(character, maxValues));

        int bestNodeIndex = leafs.indexOf(bestNode);

        return this.getActualMove(bestNodeIndex);
    }

    // returns the move that resulted in the leaf node given as its index
    private Moves getActualMove(int bestLeafIndex){
        int current = (bestLeafIndex / 4);
        for(int i = this.maxDepth - 2; i > 0; i--){
            current /= 4;
        }
        switch (current){
            case 0: return Moves.LEFT;
            case 1: return Moves.RIGHT;
            case 2: return Moves.UP;
            default: return Moves.DOWN;
        }
    }

    // returns the Node which has the smallest deviation in the scores of players other than the main (current) player
    private Node bestAdversaryDeviation(ArrayList<Node> minAdversaryScoreSum){
        // TODO eventually
        return minAdversaryScoreSum.get(new Random().nextInt(minAdversaryScoreSum.size()));
    }

    // returns the list of Nodes which have the minimum total score for all players other than the main (current) player
    private ArrayList<Node> minAdversaryScoreSum(Characters main, ArrayList<Node> maxValues){

        ArrayList<Node> result = new ArrayList<>();

        int min = Integer.MAX_VALUE;

        for(Node node : maxValues){
            int computed = this.computeAdversaryScoreSum(main, node);
            if(computed < min) min = computed;
        }

        for(Node node : maxValues){
            if(this.computeAdversaryScoreSum(main, node) == min) result.add(node);
        }

        return result;

    }

    // computes the sum of the scores of all players other than the main (current) player
    private int computeAdversaryScoreSum(Characters main, Node node){
        int sum = 0;
        for(int i = 0; i < node.points.length; i++){
            if(i == main.ordinal()) continue;
            sum += node.points[i];
        }
        return sum;
    }

    // returns a list of all the leaf nodes starting with the given root
    private ArrayList<Node> getLeafNodes(Node node){
        ArrayList<Node> leafNodes = new ArrayList<>();
        if (node.children() == null) {
            leafNodes.add(node);
        } else {
            for (Node child : node.children()) {
                leafNodes.addAll(getLeafNodes(child));
            }
        }
        return leafNodes;
    }

    public Characters getCurrentCharacter(){
        return root.getCurrentCharacter();
    }

}
