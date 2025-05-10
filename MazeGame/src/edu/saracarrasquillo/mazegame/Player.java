package edu.saracarrasquillo.mazegame;

//Sara Carrasquillo
//So I used the Breadth First Search algorithm for this project and made a maze timed game where the player gets chased by the enemy.

import java.util.Map;

public class Player {
    private int x, y; // Player's current position in the maze
    private long lastMoveTime = 0;
    private Map<String, MazeMap.MazeObject> grid; // Reference to the maze grid

    public Player(int startX, int startY, Map<String, MazeMap.MazeObject> grid) {
        this.x = startX;
        this.y = startY;
        this.grid = grid;
    }

    public void setPosition(int x, int y) {
        // Update player's position if the new position is valid
        if (isValidPosition(x, y)) {
            this.x = x;
            this.y = y;
        }
    }

    // Check if the new position is a valid position that the player can move to
    private boolean isValidPosition(int x, int y) {
        String key = x + ";" + y;
        // Ensure the position is within the maze and not a wall
        MazeMap.MazeObject obj = grid.get(key);
        return obj != null && !(obj instanceof MazeMap.MazeWall); // Check if it's not a wall
    }

    // Player movement based on direction input
    public boolean move(char direction) {
    	if (System.currentTimeMillis() - lastMoveTime < 200) return false;
        int newX = x, newY = y;
        switch (direction) {
            case 'u': newY--; break; // Move up
            case 'd': newY++; break; // Move down
            case 'l': newX--; break; // Move left
            case 'r': newX++; break; // Move right
        }
        if (isValidPosition(newX, newY)) {
            x = newX;
            y = newY;
            return true;
        }
        return false;
    }

    // Access for player's current position
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

