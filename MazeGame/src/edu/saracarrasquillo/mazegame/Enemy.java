package edu.saracarrasquillo.mazegame;

//Sara Carrasquillo
//So I used the Breadth First Search algorithm for this project and made a maze timed game where the player gets chased by the enemy.

import java.util.*;

public class Enemy {
    private int x, y;
    private Map<String, MazeMap.MazeObject> grid;

    public Enemy(int startX, int startY, Map<String, MazeMap.MazeObject> grid) {
        this.x = startX;
        this.y = startY;
        this.grid = grid;
    }
    public void setPosition(int x, int y) {
        if (isValidPosition(x, y)) {
            this.x = x;
            this.y = y;
        }
    }

    // Use BFS to find the shortest path to the player and move one step towards the player
    public void moveTowards(int targetX, int targetY) {
        if (!isValidPosition(targetX, targetY)) return; 

        Queue<int[]> queue = new LinkedList<>();
        Map<String, int[]> parent = new HashMap<>();
        queue.add(new int[]{x, y});
        parent.put(x + ";" + y, null);

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int curX = current[0], curY = current[1];
            
            // Check if we've reached the target
            if (curX == targetX && curY == targetY) {
                moveToNextStep(curX, curY, parent);
                return;
            }

            // Explore neighbors
            int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
            for (int[] d : directions) {
                int nx = curX + d[0], ny = curY + d[1];
                if (isValidPosition(nx, ny) && !parent.containsKey(nx + ";" + ny)) {
                    parent.put(nx + ";" + ny, new int[]{curX, curY});
                    queue.add(new int[]{nx, ny});
                }
            }
        }
    }

    private void moveToNextStep(int targetX, int targetY, Map<String, int[]> parent) {
        // Trace back the path from the target to the start and move one step
        int[] step = parent.get(targetX + ";" + targetY);
        if (step == null) return;  // No path to follow if null

        // Move to the first step along the path
        this.x = step[0];
        this.y = step[1];
    }

    private boolean isValidPosition(int x, int y) {
        String key = x + ";" + y;
        MazeMap.MazeObject obj = grid.get(key);
        return obj != null && !(obj instanceof MazeMap.MazeWall);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
