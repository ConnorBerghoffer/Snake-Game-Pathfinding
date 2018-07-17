package game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {

	//Window Size
	static final int WIDTH = 1920;
	static final int HEIGHT = 1080;

	//Colours
	private static final String PRINT_RED = "\u001B[31m";
	private static final String PRINT_GREEN = "\u001B[32m";
	private static final String PRINT_WHITE = "\u001B[37m";
	private static final String PRINT_BLUE = "\u001B[34m";

	//Other Variables
	private int level;
	private int score;
	private int dx, dy;
	private Graphics2D g2d;
	private boolean running;
	private long targetTime;
	private boolean gameOver;
	private Entity head, apple;
	private BufferedImage image;
	private final int SIZE = 15;
	private ArrayList<Entity> snake;
	private boolean triggered = false;
	private boolean up, right, down, left;
	private String title = (line + PRINT_WHITE + "Action: ");
	private static String line = System.getProperty("line.separator");

	//First Function
	@Override
	public void run() {
		if (running) return;
		init();
		long startTime;
		long elapsed;
		long wait;

		while (running) {
			startTime = System.nanoTime();

			update();
			requestRender();
			elapsed = System.nanoTime() - startTime;
			wait = targetTime - elapsed / 1000000;
			if (wait > 0) {
				try { Thread.sleep(wait);
				} catch (Exception e) { e.printStackTrace();
				}
			}
		}
	}

	//Initialize
	private void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g2d = image.createGraphics();
		running = true;
		setUpLevel();
		System.out.print(line + PRINT_GREEN + "Game Started. " + line + PRINT_RED + "Level: " + level + line + line + PRINT_WHITE + "Made By: " + PRINT_BLUE +" Connor Berghoffer." + line);
	}

	//Set Up Game
	private void setUpLevel() {
		gameOver = false;
		level = 1;
		snake = new ArrayList<>();
		head = new Entity(SIZE);
		snake.add(head);
		setFPS(100);
		head.setPosition(WIDTH / 2, HEIGHT / 2);

		for (int i = 1; i < 2; i++) {
			Entity e = new Entity(SIZE);
			e.setPosition(head.getX() + (i * SIZE), head.getY());
			snake.add(e);
		}

		apple = new Entity(SIZE);
		setApple();
		score = 0;
	}

	//Render Apple
	private void setApple() {
		int appleX = (int)(Math.random() * (WIDTH - SIZE));
		int appleY = (int)(Math.random() * (HEIGHT - SIZE));
		appleY = appleY - (appleY % SIZE);
		appleX = appleX - (appleX % SIZE);

		apple.setPosition(appleX, appleY);
	}

	// Render Snake
	private void render(Graphics2D g2d) {
		g2d.clearRect(0, 0, WIDTH, HEIGHT);
		g2d.setColor(Color.GREEN);
		for (Entity e : snake) { e.render(g2d); }

		g2d.setColor(Color.RED);
		apple.render(g2d);
		g2d.setColor(Color.GREEN);
		g2d.setFont(new Font ("Courier New", Font.BOLD, 17));
		g2d.drawString("Score: " + score, 10, 20);
		g2d.drawString("Level: " + level, 10, 40);
	}

	private void requestRender() {
		render(g2d);
		Graphics g = getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();

	}

	private void update() {
		if (up && dy == 0 ) { dy = - SIZE; dx = 0; }
		if (down && dy == 0 ) { dy = SIZE; dx = 0; }
		if (left && dx == 0 ) { dy = 0; dx = - SIZE; }
		if (right && dx == 0 && dy != 0) { dy = 0; dx = SIZE; }

		//Move Snake Using Arrows
		if (dx != 0 || dy != 0) {
			for (int i = snake.size() - 1; i > 0; i--) {
				snake.get(i).setPosition(snake.get(i - 1).getX(), snake.get(i - 1).getY());
			}
			head.move(dx,  dy);
		}

		//If Snake Eats Apple
		if (apple.isCollision(head)) {
			up = false; down = false; left = false; right = false;
			triggered = false;
			System.out.print(title + PRINT_GREEN + "Ate Apple.");
			score++;
			setApple();
			Entity e = new Entity(SIZE);
			e.setPosition(-100, -100);
			snake.add(e);

			//Level Up
			if(score % 10 == 0) {
				level++;
				System.out.print(title + PRINT_BLUE + "Level " + level);
				if (level > 10) level = 10;
				int fps = 5;
				fps = fps + 5;
				setFPS(fps);
			}
		}

		if (head.getX() < 0) head.setX(WIDTH);
		if (head.getY() < 0) head.setY(HEIGHT);
		if (head.getX() > WIDTH) head.setX(0);
		if (head.getY() > HEIGHT) head.setY(0);

		//Find And Follow The Apple
		//TODO: Add tail avoidance
		if (!triggered) {
			if (apple.getX() > head.getX()) {
				left = false;
				right = true;
			} else if (apple.getX() < head.getX()) {
				left = true;
				right = false;
			} else if (apple.getX() == head.getX()) {
				triggered = true;
				left = false;
				right = false;

				if (apple.getY() > head.getY()) {
					up = false;
					down = true;
				} else if (apple.getY() < head.getY()) {
					up = true;
					down = false;
				}
			}
		}

		//If Head Touches Tail
		for (Entity e : snake) {
			if (e.isCollision(head)) {
				gameOver = true;
				break;
			}
		}
		//Game Over
		if (gameOver) {
			System.out.print(title + PRINT_RED + "Game Over.");
			setFPS(0);
		}
	}

	GamePanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
	}

	public void addNotify() {
		super.addNotify();
		Thread thread = new Thread(this);
		thread.start();
	}

	private void setFPS(int fps) {
		targetTime = 10000 / fps;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();

		if (k == KeyEvent.VK_UP) up = true;
		if (k == KeyEvent.VK_DOWN) down = true;
		if (k == KeyEvent.VK_LEFT) left = true;
		if (k == KeyEvent.VK_RIGHT) right = true;
		if (k == KeyEvent.VK_SPACE) setUpLevel();
		if (k == KeyEvent.VK_ESCAPE) System.exit(1);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();

		if (k == KeyEvent.VK_UP) up = false;
		if (k == KeyEvent.VK_DOWN) down = false;
		if (k == KeyEvent.VK_LEFT) left = false;
		if (k == KeyEvent.VK_RIGHT) right = false;
	}
}
