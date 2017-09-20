package com.zsurvival.main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.zsurvival.assets.Asset;
import com.zsurvival.states.CreditsState;
import com.zsurvival.states.GameOverState;
import com.zsurvival.states.GameState;
import com.zsurvival.states.GameStateManager;
import com.zsurvival.states.HighscoreState;
import com.zsurvival.states.MainMenuState;
import com.zsurvival.states.State;
import com.zsurvival.states.instructions.InstructionsState;
import com.zsurvival.states.instructions.MenuState;

/**
 * The main panel for the game. Runs the game loop and has all of the action
 * listeners
 * @author Raya and Daniel
 */
public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 1L;

	// Dimensions
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 750;

	// Game thread
	private Thread thread;
	private boolean running;
	private static final int FPS = 60;
	private double timePerUpdate = 1000000000 / FPS;

	// Graphics
	private BufferedImage image;
	private Graphics2D g;

	// Splash Screen
	public static SplashScreen splash;
	private BufferedImage backupSplash;

	// States
	public static State[] states;
	private State currentState;
	private final int NUM_STATES = 10;

	/**
	 * Constructor
	 */
	public GamePanel()
	{
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(false);
	}

	/**
	 * Runs the super classes addNotify method and starts the thread also runs
	 * the run method
	 */
	public void addNotify()
	{
		super.addNotify();
		if (thread == null)
		{
			thread = new Thread(this);
			thread.start();
		}
	}

	/**
	 * Displays the splash screen, initializes the graphics object, initializes
	 * the states and starts the game loop
	 */
	private void init()
	{
		// Display splash screen
		splash = SplashScreen.getSplashScreen();

		// Initialize graphics
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();

		// Back up code in case splash screen doesn't work (we need vm arguments
		// for the splash screen to work and they don't get passed into jar
		// files so we need a back up)
		if (splash == null)
		{
			try
			{
				backupSplash = ImageIO.read(getClass().getResourceAsStream("/SplashScreen.png"));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			Graphics g2 = getGraphics();
			g2.drawImage(backupSplash, 0, 0, WIDTH, HEIGHT, null);
			g2.dispose();

			g.clearRect(0, 0, WIDTH, HEIGHT);

		}

		// Load all of the assets
		long start = System.currentTimeMillis();
		new Asset();
		long finish = System.currentTimeMillis()-start;
		System.out.println(finish);

		// Initialize states
		states = new State[NUM_STATES];

		states[GameStateManager.MAIN_MENU_STATE] = new MainMenuState();
		states[GameStateManager.GAME_STATE] = new GameState();
		states[GameStateManager.INSTRUCTIONS_STATE] = new InstructionsState();
		states[GameStateManager.OBJECTIVE_STATE] = new MenuState(Asset.objectiveMenu);
		states[GameStateManager.CONTROLS_STATE] = new MenuState(Asset.controlsMenu);
		states[GameStateManager.HUD_STATE] = new MenuState(Asset.HUDMenu);
		states[GameStateManager.CRATES_STATE] = new MenuState(Asset.cratesMenu);
		states[GameStateManager.GAME_OVER_STATE] = new GameOverState();
		states[GameStateManager.CREDITS_STATE] = new CreditsState();
		states[GameStateManager.HIGHSCORE_STATE] = new HighscoreState();

		GameStateManager.setCurrentState(GameStateManager.MAIN_MENU_STATE);
		currentState = GameStateManager.getCurrentState();

		// Start the game loop
		running = true;

		// Close the splash screen and open the frame
		if (splash != null)
		{
			splash.close();
		}
	}

	/**
	 * Runs the game loop
	 */
	public void run()
	{
		init();

		double deltaTime = 0;
		long currentTime;
		long lastUpdateTime = System.nanoTime();

		// Game loop
		while (running)
		{
			currentTime = System.nanoTime();
			deltaTime += (currentTime - lastUpdateTime) / timePerUpdate;
			lastUpdateTime = currentTime;

			if (deltaTime >= 1)
			{
				update();
				draw();
				deltaTime--;
			}
		}
	}

	/**
	 * Runs the current state's update method
	 */
	private void update()
	{
		if (currentState != null)
		{
			currentState.update();
			if (currentState != GameStateManager.getCurrentState())
			{
				currentState = GameStateManager.getCurrentState();
			}
		}
	}

	/**
	 * Runs the current state's draw method and draws everything to the screen
	 */
	private void draw()
	{
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
		g2.dispose();

		g.clearRect(0, 0, WIDTH, HEIGHT);

		if (currentState != null)
		{
			currentState.draw(g);
		}
	}

	/**
	 * Runs the current state's key pressed method
	 * @param key The key being pressed
	 */
	public void keyPressed(KeyEvent key)
	{
		currentState.keyPressed(key.getKeyCode());
	}

	/**
	 * Runs the current state's key released method
	 * @param key The key being pressed
	 */
	public void keyReleased(KeyEvent key)
	{
		currentState.keyReleased(key.getKeyCode());
	}

	public void keyTyped(KeyEvent key)
	{
		currentState.keyTyped(key);
	}

	/**
	 * Runs the current state's mouse clicked method
	 * @param mouse The mouse clicked event
	 */
	public void mouseClicked(MouseEvent mouse)
	{
		try
		{
			currentState.mouseClicked(mouse.getX(), mouse.getY());
		}
		catch (NullPointerException e)
		{
			// Don't print the stack trace because it will print a lot of null
			// pointer errors at the beginning before the game panel initializes
		}
	}

	/**
	 * Runs the current state's mouse moved method
	 * @param mouse The mouse moved event
	 */
	public void mouseMoved(MouseEvent mouse)
	{
		try
		{
			currentState.mouseMoved(mouse.getX(), mouse.getY());
		}
		catch (NullPointerException e)
		{

		}
	}

	// ALL BELOW METHODS ARE UNUSED MOUSE LISTENER AND MOUSE MOTION LISTENER
	// METHODS

	public void mouseEntered(MouseEvent mouse)
	{

	}

	public void mouseExited(MouseEvent mouse)
	{

	}

	public void mousePressed(MouseEvent mouse)
	{

	}

	public void mouseReleased(MouseEvent mouse)
	{

	}

	public void mouseDragged(MouseEvent mouse)
	{

	}
}
