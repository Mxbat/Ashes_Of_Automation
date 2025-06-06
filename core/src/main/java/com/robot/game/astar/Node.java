package com.robot.game.astar;

public class Node implements Comparable<Node> {
    public int x, y;
    public int gCost;
    public int hCost;
    public Node parent;
    public boolean walkable;

    public Node(int x, int y, boolean walkable) {
        this.x = x;
        this.y = y;
        this.walkable = walkable;
    }

    public int fCost() {
        return gCost + hCost;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.fCost(), other.fCost());
    }
}
