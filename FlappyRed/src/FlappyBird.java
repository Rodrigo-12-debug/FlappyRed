
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    private int birdY, birdVelocity, score;
    private final int GRAVITY = 2, JUMP_STRENGTH = -20;
    private final int BIRD_X = 100, BIRD_SIZE = 30;
    private ArrayList<Rectangle> pipes;
    private Timer timer;
    private boolean gameOver;

    public FlappyBird() {
        JFrame frame = new JFrame("Flappy Bird en Java");
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.addKeyListener(this);
        frame.setVisible(true);

        resetGame();
    }

    private void resetGame() {
        birdY = 250;
        birdVelocity = 0;
        gameOver = false;
        score = 0;
        pipes = new ArrayList<>();
        addPipes();
        addPipes2();
        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(20, this);
        timer.start();
        repaint();
    }

    private void addPipes() {
        Random rand = new Random();
        int gap = 150;
        int pipeHeight = rand.nextInt(200) + 100;
        pipes.add(new Rectangle(400, 0, 50, pipeHeight)); // Tubo superior
        pipes.add(new Rectangle(400, pipeHeight + gap, 50, 600 - pipeHeight - gap)); // Tubo inferior
    }
    private void addPipes2() {
        Random rand = new Random();
        int gap = 150;
        int pipeHeight = rand.nextInt(200) + 100;
        pipes.add(new Rectangle(400, 0, 50, pipeHeight)); // Tubo superior
        pipes.add(new Rectangle(400, pipeHeight + gap, 50, 600 - pipeHeight - gap)); // Tubo inferior
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.YELLOW);
        g.fillOval(BIRD_X, birdY, BIRD_SIZE, BIRD_SIZE);

        g.setColor(Color.GREEN);
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Puntaje: " + score, 20, 20);

        if (gameOver) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("GAME OVER", 100, 300);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Presiona ESPACIO para reiniciar", 50, 350);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            birdVelocity += GRAVITY;
            birdY += birdVelocity;

            for (int i = 0; i < pipes.size(); i++) {
                Rectangle pipe = pipes.get(i);
                pipe.x -= 5;
                if (pipe.x + pipe.width < 0) {
                    pipes.remove(i);
                    i--;
                    score += 1; // Aumenta el puntaje cuando un tubo desaparece
                }
            }

            if (pipes.size() < 2) {
                addPipes();
            }

            checkCollision();
            repaint();
        }
    }

    private void checkCollision() {
        if (birdY < 0 || birdY > getHeight()) {
            gameOver = true;
            timer.stop();
        }
        for (Rectangle pipe : pipes) {
            if (new Rectangle(BIRD_X, birdY, BIRD_SIZE, BIRD_SIZE).intersects(pipe)) {
                gameOver = true;
                timer.stop();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (gameOver) {
                resetGame();
            } else {
                birdVelocity = JUMP_STRENGTH;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        new FlappyBird();
    }
}
