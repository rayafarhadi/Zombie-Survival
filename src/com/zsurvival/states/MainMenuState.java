package com.zsurvival.states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.zsurvival.assets.Asset;
import com.zsurvival.main.GamePanel;

/**
 * The main menu
 * @author Raya and Daniel
 */
public class MainMenuState implements State
{
	// Options
	private BufferedImage[] options;

	private int currentOption;

	private final int START = 0;
	private final int INSTRUCTIONS = 1;
	private final int HIGHSCORES = 2;
	private final int QUIT = 3;
	private final int NUM_OPTIONS = 4;

	// Wait time
	private int wait;
	private final int WAIT_TIME = 5;

	/**
	 * Constructor
	 */
	public MainMenuState()
	{
		options = new BufferedImage[NUM_OPTIONS];

		options[START] = Asset.mainStart;
		options[INSTRUCTIONS] = Asset.mainInstructions;
		options[HIGHSCORES] = Asset.mainHighscores;
		options[QUIT] = Asset.mainQuit;

		currentOption = 0;
		wait = 0;

		// Plays and loops the theme music
		Asset.backgroundMusic.play();
		Asset.backgroundMusic.loop();
	}

	/**
	 * Selects the current option
	 */
	public void select()
	{
		// Starts a new game
		if (currentOption == START)
		{
			GamePanel.states[GameStateManager.GAME_STATE] = new GameState();
			GameStateManager.setCurrentState(GameStateManager.GAME_STATE);
		}
		// Goes to the instructions menu
		else if (currentOption == INSTRUCTIONS)
		{
			GameStateManager.setCurrentState(GameStateManager.INSTRUCTIONS_STATE);
		}
		else if (currentOption == HIGHSCORES)
		{
			updateHighScores();
			GameStateManager.setCurrentState(GameStateManager.HIGHSCORE_STATE);
		}
		// Closes the game
		else if (currentOption == QUIT)
		{
			System.exit(0);
		}
	}

	/**
	 * Updates the high scores
	 */
	public void updateHighScores()
	{
		GamePanel.states[GameStateManager.HIGHSCORE_STATE].update();
	}

	/**
	 * unused
	 */
	public void update()
	{

	}

	/**
	 * Draws the image for the current option
	 */
	public void draw(Graphics2D g)
	{
		g.drawImage(options[currentOption], 0, 0, null);
	}

	/**
	 * Handles key pressed events
	 */
	public void keyPressed(int k)
	{
		if (k == KeyEvent.VK_ENTER || k == KeyEvent.VK_SPACE || k == KeyEvent.VK_SLASH)
		{
			select();
		}
		if (wait <= 1)
		{
			if (k == KeyEvent.VK_UP || k == KeyEvent.VK_W)
			{
				currentOption--;
				if (currentOption < 0)
				{
					currentOption = options.length - 1;
				}
				wait += WAIT_TIME;
			}
			if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S)
			{
				currentOption++;
				if (currentOption >= options.length)
				{
					currentOption = 0;
				}
				wait += WAIT_TIME;
			}
		}
		else
		{
			wait--;
		}
	}

	/**
	 * Handles key released events
	 */
	public void keyReleased(int k)
	{
		if (k == KeyEvent.VK_UP || k == KeyEvent.VK_W)
		{
			wait = 0;
		}
		if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S)
		{
			wait = 0;
		}
	}

	/**
	 * Unused
	 */
	public void keyTyped(KeyEvent k)
	{

	}

	/**
	 * Handles mouse clicked events
	 */
	public void mouseClicked(int x, int y)
	{
		select();
	}

	/**
	 * Handles mouse moved events
	 */
	public void mouseMoved(int x, int y)
	{
		if (y < 300)
		{
			currentOption = START;
		}
		else if (y >= 300 && y < 425)
		{
			currentOption = INSTRUCTIONS;
		}
		else if (y >= 425 && y < 550)
		{
			currentOption = HIGHSCORES;
		}
		else
		{
			currentOption = QUIT;
		}
	}

}
