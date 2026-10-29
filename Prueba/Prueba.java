import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Prueba extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 900;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 18;
    private static final int PADDLE_HEIGHT = 120;
    private static final int BALL_SIZE = 16;
    private static final int PADDLE_SPEED = 11;

    private int player1Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int player2Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int player1X = 30;
    private int player2X = WIDTH - 30 - PADDLE_WIDTH;

    private int ballX = WIDTH / 2 - BALL_SIZE / 2;
    private int ballY = HEIGHT / 2 - BALL_SIZE / 2;
    private int ballVX = 6;
    private int ballVY = 5;

    private int score1 = 0;
    private int score2 = 0;

    private final boolean[] keys = new boolean[256];

    public Prueba() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        new Timer(16, this).start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    private void updateGame() {
        if (keys[KeyEvent.VK_A]) {
            player1Y -= PADDLE_SPEED;
        }
        if (keys[KeyEvent.VK_D]) {
            player1Y += PADDLE_SPEED;
        }
        if (keys[KeyEvent.VK_UP]) {
            player2Y -= PADDLE_SPEED;
        }
        if (keys[KeyEvent.VK_DOWN]) {
            player2Y += PADDLE_SPEED;
        }

        if (player1Y < 0) {
            player1Y = 0;
        }
        if (player1Y + PADDLE_HEIGHT > HEIGHT) {
            player1Y = HEIGHT - PADDLE_HEIGHT;
        }
        if (player2Y < 0) {
            player2Y = 0;
        }
        if (player2Y + PADDLE_HEIGHT > HEIGHT) {
            player2Y = HEIGHT - PADDLE_HEIGHT;
        }

        ballX += ballVX;
        ballY += ballVY;

        if (ballY <= 0 || ballY + BALL_SIZE >= HEIGHT) {
            ballVY *= -1;
            ballY = Math.max(0, Math.min(ballY, HEIGHT - BALL_SIZE));
        }

        Rectangle player1 = new Rectangle(player1X, player1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        Rectangle player2 = new Rectangle(player2X, player2Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        Rectangle ball = new Rectangle(ballX, ballY, BALL_SIZE, BALL_SIZE);

        if (ball.intersects(player1)) {
            ballX = player1X + PADDLE_WIDTH;
            ballVX = Math.abs(ballVX) + 1;
            int centroPelota = ballY + BALL_SIZE / 2;
            int centroBarra = player1Y + PADDLE_HEIGHT / 2;
            ballVY = (centroPelota - centroBarra) / 5;
        }

        if (ball.intersects(player2)) {
            ballX = player2X - BALL_SIZE;
            ballVX = -Math.abs(ballVX) - 1;
            int centroPelota = ballY + BALL_SIZE / 2;
            int centroBarra = player2Y + PADDLE_HEIGHT / 2;
            ballVY = (centroPelota - centroBarra) / 5;
        }

        if (ballX + BALL_SIZE < 0) {
            score2++;
            resetBall(1);
        } else if (ballX > WIDTH) {
            score1++;
            resetBall(-1);
        }
    }

    private void resetBall(int direction) {
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;
        ballVX = direction * 6;
        ballVY = (Math.random() > 0.5 ? 1 : -1) * 5;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        g2.setColor(new Color(255, 255, 255, 220));
        g2.fillRect(player1X, player1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        g2.fillRect(player2X, player2Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        g2.setColor(new Color(255, 255, 255, 255));
        g2.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        g2.setColor(new Color(255, 255, 255, 80));
        g2.fillOval(ballX - 3, ballY - 3, BALL_SIZE + 6, BALL_SIZE + 6);

        g2.setColor(new Color(255, 255, 255, 40));
        g2.fillOval(ballX - 8, ballY - 8, BALL_SIZE + 16, BALL_SIZE + 16);

        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.WHITE);
        for (int y = 0; y < HEIGHT; y += 30) {
            g2.drawLine(WIDTH / 2, y, WIDTH / 2, y + 15);
        }

        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString(String.valueOf(score1), WIDTH / 2 - 60, 40);
        g2.drawString(String.valueOf(score2), WIDTH / 2 + 30, 40);

        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("Jugador 1: A / D", 20, 25);
        g2.drawString("Jugador 2: ↑ / ↓", WIDTH - 180, 25);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("PONG 2 JUGADORES");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setContentPane(new Prueba());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
