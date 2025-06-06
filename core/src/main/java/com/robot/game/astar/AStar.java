package com.robot.game.astar;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.robot.game.Room;


import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class AStar {
    public Node[][] grid;
    private final int width;
    private final int height;
    int[][] map;

    public Room room;

    public AStar(Room room) {
        this.room = room;
        this.map = room.map;
        this.width = map[0].length;
        this.height = map.length;
        this.initializeGrid(room.map);

    }

    public void initializeGrid(int[][] map) {
        grid = new Node[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean walkable = map[y][x] == 0;
                grid[y][x] = new Node(x, y, walkable);
            }
        }
    }
    public Array<Vector2> findPath(Node start, Node target) {

        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Set<Node> closedSet = new HashSet<>();

        start.gCost = 0;
        start.hCost = heuristic(start, target);
        openSet.add(start);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.equals(target)) {
                return retracePath(start, current);
            }

            closedSet.add(current);

            for (Node neighbor : getNeighbors(current)) {
                if (!neighbor.walkable || closedSet.contains(neighbor)) {
                    continue;
                }

                int newMovementCost = current.gCost + 10;
                if (newMovementCost < neighbor.gCost || !openSet.contains(neighbor)) {
                    neighbor.gCost = newMovementCost;
                    neighbor.hCost = heuristic(neighbor, target);
                    neighbor.parent = current;

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }
        System.out.println("NO PATH");
        return Array.with();
    }

    private Array<Node> getNeighbors(Node node) {
        Array<Node> neighbors = new Array<>();
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        for (int[] dir : directions) {
            int checkX = node.x + dir[0];
            int checkY = node.y + dir[1];

            if (checkX >= 0 && checkX < width && checkY >= 0 && checkY < height) {
                Node neighbor = grid[checkY][checkX];
                if (neighbor.walkable) {
                    neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    }
    private Array<Vector2> retracePath(Node startNode, Node endNode) {
        Array<Vector2> path = new Array<>();
        Node currentNode = endNode;

        while (currentNode != startNode) {

            float worldX = currentNode.x  * Room.multiplier + room.x;
            float worldY = (Math.abs(currentNode.y - map.length) - 1) * Room.multiplier + room.y;
            path.add(new Vector2(worldX, worldY));
            currentNode = currentNode.parent;
        }
        path.reverse();
        return path;
    }


    private int heuristic(Node a, Node b) {
        return 10 * (Math.abs(a.x - b.x) + Math.abs(a.y - b.y));
    }


    public Node getPointOnGrid(int x, int y) {

        if(x >= map[0].length) x = map[0].length - 1;
        if(y >=  map.length) y =  map.length - 1;
        if(x < 0) x = 0;
        if(y < 0) y = 0;

        return grid[Math.abs(map.length) - y - 1][x];
    }
    public Node getPointByCords(Vector2 vector){
        int x = (int) ((int) Math.floor((vector.x)/(Room.multiplier )) - room.x/Room.multiplier);
        int y = (int) ((int) Math.floor((vector.y)/(Room.multiplier))  - room.y/Room.multiplier);
        return getPointOnGrid(x, y);
    }
}
