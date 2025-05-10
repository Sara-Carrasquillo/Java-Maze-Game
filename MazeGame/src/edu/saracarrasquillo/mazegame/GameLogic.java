package edu.saracarrasquillo.mazegame;

//Sara Carrasquillo
//So I used the Breadth First Search algorithm for this project and made a maze timed game where the player gets chased by the enemy.

import javax.swing.Timer;

public class GameLogic {
    private Player player;           
    private Enemy enemy;             
    private boolean gameRunning;     
    private Timer gameTimer;         
    private Timer enemyMoveTimer;    

    // Initialize the game logic 
    public GameLogic(MazeMap mazeMap, Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
        this.gameRunning = false;
        setupTimers();
    }

    // Timer for game and enemy movements
    private void setupTimers() {
        // Timer for slowing enemy movements
        enemyMoveTimer = new Timer(500, e -> {
            if (gameRunning) { // Only move enemy if the game is running
                enemy.moveTowards(player.getX(), player.getY());
                checkLoseCondition();
            }
        });
        enemyMoveTimer.setInitialDelay(1000); // Delay first movement to give player a brief head start
        enemyMoveTimer.stop(); // Ensure it doesn't start until the game does

        // Main game timer 
        gameTimer = new Timer(100, e -> updateGame());
        gameTimer.stop(); // Stop the timer until the game starts
    }

    // Start or resume the game
    public void startGame() {
        gameRunning = true; // Set the game as running
        enemyMoveTimer.start(); // Start the enemy movement timer
        gameTimer.start(); // Start the main game timer
    }

    // Method called periodically by the gameTimer to perform game updates
    private void updateGame() {
        if (!gameRunning) return; // Do nothing if the game is not running
        // Additional game logic can be placed here
    }

    // Check if the player has lost the game
    private void checkLoseCondition() {
        if (player.getX() == enemy.getX() && player.getY() == enemy.getY()) {
            stopGame(); // Stop the game if the player and enemy are at the same position
        }
    }

    // Stops the game and all related activities
    public void stopGame() {
        if (gameRunning) {
            gameRunning = false; // Set the game as not running
            gameTimer.stop(); // Stop the main game timer
            enemyMoveTimer.stop(); // Stop the enemy movement timer
        }
    }

    // Resets the game to its initial state
    public void resetGame() {
        player.setPosition(1, 2); // Reset player's position
        enemy.setPosition(1, 0); // Reset enemy's position
        stopGame(); // Ensure all timers are stopped and the game state is reset
    }

    // Return the current running state of the game
    public synchronized boolean isGameRunning() {
        return gameRunning;
    }
}
