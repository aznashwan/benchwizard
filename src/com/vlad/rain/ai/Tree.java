package com.vlad.rain.ai;

import com.sun.tools.javac.util.Pair;
import com.vlad.rain.ai.data.Characters;
import com.vlad.rain.ai.data.Moves;
import com.vlad.rain.entity.mob.Player;
import com.vlad.rain.level.Level;

import java.util.ArrayList;
import java.util.Random;

/**
 * The class representing the game {@link Tree}.
 * It represents nothing more than a pruned Alpha-Beta tree.
 */
public class Tree {

    // the root {@link Node} of the {@link Tree}.
    private Node root;
    private int maxDepth;

    /**
     * @param level the {@link Level} we are currently working on.
     * @param players the {@link java.util.List} of {@link Player}s.
     * @param maxDepth the {@link int} maximum depth of the {@link Tree}.
     */
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

    /**
     * Re-form the tree in accordance with the given {@link Moves}.
     * <P>
     * NOTE: The provided {@link Moves} object is not formally verified in any way.
     * @param move
     */
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
        // TODO: force gc collection:
        // no more references to useless part of the tree => will get GC
        this.root.setChildren(null);
        this.root.setParent(null);

        // decrease the current depth for all remaining nodes and generate next tree layer
        this.root.decreaseCurrentDepth();
    }

    // returns the best move the given player

    /**
     * Traverses the game {@link Tree} recursively until it eventually returns the move which
     * would lead to the best leaf {@link Node} path.
     * the best move possible for the given {@link Characters}.
     * @param character the {@link Characters} to compute the best move for.
     * @return the best {@link Moves}.
     */
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

    /**
     * Returns the move to be made, all things considered after getBestMove is called.
     * @param bestLeafIndex
     * @return
     */
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

    /**
     * Returns the Node which presents the most minimal standard deviation in the final
     * result for the rest of the players.
     * @param minAdversaryScoreSum the {@link Node}s which represent the minimum adversary score desired.
     * @return a {@link Node} for the way to go.
     */
    private Node bestAdversaryDeviation(ArrayList<Node> minAdversaryScoreSum){
        // TODO eventually
        return minAdversaryScoreSum.get(new Random().nextInt(minAdversaryScoreSum.size()));
    }

    /**
     * Returns the {@link java.util.List} of {@link Node}s which have the minimal total score for all other
     * players except the current (human-controlled) one.
     * @param main the main {@link Characters}.
     * @param maxValues the {@link java.util.List} of maximum-valued nodes.
     * @return
     */
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

    /**
     * Computes the sum of scores of all {@link Characters} other than the main {@link Characters}.
     * @param main the main {@link Characters}.
     * @param node the given {@link Node} we are currently on in the game {@link Tree}.
     * @return the {@link int} sum of the scores of all the other {@link Characters}.
     */
    private int computeAdversaryScoreSum(Characters main, Node node){
        int sum = 0;
        for(int i = 0; i < node.points.length; i++){
            if(i == main.ordinal()) continue;
            sum += node.points[i];
        }
        return sum;
    }

    // returns a list of all the leaf nodes starting with the given root

    /**
     * Traverses the {@link Tree} and returns a {@link java.util.List} of the leaf {@link Node}s.
     * @param node the {@link Node} from which to start.
     * @return the {@link ArrayList} of the terminal {@link Node}s down the subtree.
     */
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
