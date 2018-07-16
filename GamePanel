package game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    
    private Graphics2D g2d;
    private BufferedImage image;

    private Thread thread;
    private boolean running;
    private long targetTime;
    private int level;
    private boolean gameOver;

	private final int SIZE = 15;
    private Entity head, apple;
    private ArrayList<Entity> snake;
    private int score;

    private int dx, dy;
    private boolean up, right, down, left, start;
    
    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
        addKeyListener(this);
    }
    
    public void addNotify() {
    	super.addNotify();
    	thread = new Thread(this);
    	thread.start();
    }
    
    private void setFPS(int fps) {
    	targetTime = 1000 / fps;
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
    	if (k == KeyEvent.VK_ENTER) start = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    	int k = e.getKeyCode();
    	
    	if (k == KeyEvent.VK_UP) up = false;
    	if (k == KeyEvent.VK_DOWN) down = false;
    	if (k == KeyEvent.VK_LEFT) left = false;
    	if (k == KeyEvent.VK_RIGHT) right = false;
    	if (k == KeyEvent.VK_ENTER) start = false;
    }

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
    			try { 
    				Thread.sleep(wait); 
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    	}
    }

	private void update() {
        if (gameOver) {
            //if (start) {
            gameOver = false;
                setUpLevel();
           // }
        }

		if (up && dy == 0 ) {
			dy = - SIZE;
			dx = 0;
		}
		if (down && dy == 0 ) {
			dy = SIZE;
			dx = 0;
		}
		if (left && dx == 0 ) {
			dy = 0;
			dx = - SIZE;
		}
		if (right && dx == 0 && dy != 0) {
			dy = 0;
			dx = SIZE;
		}
		
		if (dx != 0 || dy != 0) {
		
		for (int i = snake.size() - 1; i > 0; i--) {
			snake.get(i).setPosition(snake.get(i - 1).getX(), snake.get(i - 1).getY());
		}
		head.move(dx,  dy);
		}

		for (Entity e : snake) {
		    if (e.isCollision(head)) {
		        gameOver = true;
		        break;
            }
        }

		if (apple.isCollision(head)) {
			score++;
			setApple();

            Entity e = new Entity(SIZE);
            e.setPosition(-100, -100);
            snake.add(e);
            if(score % 10 == 0) {
                level++;
                if (level > 10) level = 10;
                setFPS(level * 5);
            }
		}

		if (head.getX() < 0) head.setX(WIDTH);
		if (head.getY() < 0) head.setY(HEIGHT);
		if (head.getX() > WIDTH) head.setX(0);
		if (head.getY() > HEIGHT) head.setY(0);
		
		
	}
	
	private void setUpLevel() {
        gameOver = false;
        level = 1;
		snake = new ArrayList<>();
		head = new Entity(SIZE);
		snake.add(head);
		
		head.setPosition(WIDTH / 2, HEIGHT / 2);

		for (int i = 1; i < 5; i++) {
			Entity e = new Entity(SIZE);
			e.setPosition(head.getX() + (i * SIZE), head.getY());
			snake.add(e);
		}

		apple = new Entity(SIZE);
		setApple();
		score = 0;
	}

	public void setApple() {
		int x = (int)(Math.random() * (WIDTH - SIZE));
		int y = (int)(Math.random() * (HEIGHT - SIZE));
		y = y - (y % SIZE);
		x = x - (x % SIZE);

		apple.setPosition(x, y);
	}

	private void requestRender() {
		render(g2d);
		Graphics g = getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		
	}
	
	public void render(Graphics2D g2d) {
		g2d.clearRect(0, 0, WIDTH, HEIGHT);
		g2d.setColor(Color.GREEN);
		for (Entity e : snake) {
			e.render(g2d);
		}

		g2d.setColor(Color.RED);
		apple.render(g2d);
		g2d.setColor(Color.GREEN);
		g2d.setFont(new Font ("Courier New", 1, 17));
		g2d.drawString("Score: " + score, 10, 20);
        g2d.drawString("Level: " + level, 10, 40);
        if (gameOver) {
            g2d.drawString("Game Over!", WIDTH / 2, HEIGHT / 2);
        }
        if (dx == 0 && dy == 0) {
            g2d.drawString("Ready!", WIDTH / 2, HEIGHT / 2);
        }

	}

	private void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g2d = image.createGraphics();
        running = true;
		setUpLevel();
        setFPS(10);
	}
}
