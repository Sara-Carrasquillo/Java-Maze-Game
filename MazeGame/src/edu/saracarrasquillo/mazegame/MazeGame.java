package edu.saracarrasquillo.mazegame;

// Sara Carrasquillo
// So I used the Breadth First Search algorithm for this project and made a maze timed game where the player gets chased by the enemy.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MazeGame {
    private JFrame frame;
    private MazeMap mazeMap;
    private Player player;
    private Enemy enemy;
    private JButton startButton, resetButton;
    private JLabel timerLabel;
    private GameLogic gameLogic;
    private JPanel gamePanel;
    private Timer gameTimer;
    private int elapsedTime = 0; 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                MazeGame window = new MazeGame();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public MazeGame() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Maze Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(830, 880);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        startButton = new JButton("Start Game");
        startButton.addActionListener(e -> startGame());

        resetButton = new JButton("Reset Game");
        resetButton.addActionListener(e -> resetGame());

        timerLabel = new JLabel("Time: 00:00");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(timerLabel);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        initializeGameComponents();
        setupGamePanel(mainPanel);
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void initializeGameComponents() {
        mazeMap = new MazeMap();
        player = new Player(1, 2, mazeMap.getGrid());
        enemy = new Enemy(1, 0, mazeMap.getGrid());
        gameLogic = new GameLogic(mazeMap, player, enemy);
        setupGameTimer(); // Game timer
    }

    private void setupGamePanel(JPanel mainPanel) {
        gamePanel = new JPanel() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGame(g);
            }
        };
        gamePanel.setPreferredSize(new Dimension(800, 800));
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        gamePanel.requestFocusInWindow();
    }

    private void setupGameTimer() {
        gameTimer = new Timer(1000, e -> {
            elapsedTime++;
            timerLabel.setText("Time: " + formatTime(elapsedTime));
            if (elapsedTime >= 35) {
                synchronized (this) {
                    if (gameLogic.isGameRunning()) {
                        JOptionPane.showMessageDialog(frame, "Time's up! You lost.", "Game Over", JOptionPane.ERROR_MESSAGE);
                        gameLogic.stopGame();
                    }
                }
            }
        });
    }

    private String formatTime(int totalSecs) {
        int minutes = totalSecs / 60;
        int seconds = totalSecs % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void drawGame(Graphics g) {
        for (int y = 0; y < mazeMap.getHeight(); y++) {
            for (int x = 0; x < mazeMap.getWidth(); x++) {
                MazeMap.MazeObject obj = mazeMap.getGrid().get(x + ";" + y);
                g.setColor(obj instanceof MazeMap.MazeWall ? Color.DARK_GRAY : Color.LIGHT_GRAY);
                g.fillRect(x * 32, y * 32, 32, 32);
            }
        }
        g.setColor(Color.white);
        g.fillOval(player.getX() * 32, player.getY() * 32, 32, 32); // Player white dot
        g.setColor(Color.black);
        g.fillOval(enemy.getX() * 32, enemy.getY() * 32, 32, 32); // Enemy black dot
    }

    private void handleKeyPress(KeyEvent e) {
        if (gameLogic.isGameRunning()) {
            int key = e.getKeyCode();
            boolean moved = false;
            switch (key) {
                case KeyEvent.VK_UP:
                    moved = player.move('u');
                    break;
                case KeyEvent.VK_DOWN:
                    moved = player.move('d');
                    break;
                case KeyEvent.VK_LEFT:
                    moved = player.move('l');
                    break;
                case KeyEvent.VK_RIGHT:
                    moved = player.move('r');
                    break;
            }
            if (moved) {
                gamePanel.repaint();
                checkWinCondition();
                checkLoseCondition();
            }
        }
    }

    private void checkWinCondition() {
        if (player.getX() == 23 && player.getY() == 24) {
            JOptionPane.showMessageDialog(frame, "Congratulations! You won!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            stopGame();
        }
    }

    private void checkLoseCondition() {
        if (player.getX() == enemy.getX() && player.getY() == enemy.getY()) {
            JOptionPane.showMessageDialog(frame, "The enemy got you!", "Game Over", JOptionPane.ERROR_MESSAGE);
            stopGame();
        }
    }

    private void stopGame() {
        gameLogic.stopGame();  
        gamePanel.repaint();
        timerLabel.setText("Time: 00:00"); 
        gameTimer.stop(); 
    }

    private void startGame() {
    	gameLogic.resetGame();
        gameLogic.startGame();
        timerLabel.setText("Time: 00:00");
        gamePanel.requestFocusInWindow();
        gameTimer.start(); 
        elapsedTime = 0; 
    }

    private void resetGame() {
        gameLogic.resetGame();
        timerLabel.setText("Time: 00:00");
        gamePanel.repaint();
        gameTimer.stop(); 
        elapsedTime = 0; 
    }
}
