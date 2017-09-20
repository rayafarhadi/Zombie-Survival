package com.zsurvival.states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;

import com.zsurvival.assets.Asset;
import com.zsurvival.main.GamePanel;
import com.zsurvival.objects.HUD;

/**
 * The game over menu
 * @author Raya and Daniel
 */
public class GameOverState implements State
{
	// Screens
	private int currentScreen;
	private final int GAME_OVER_SCREEN = 0;
	private final int NAME_SCREEN = 1;

	private String name;
	private boolean nameEntered;

	// Options
	private BufferedImage[] options;

	private int currentOption;
	private final int PLAY_AGAIN = 0;
	private final int CREDITS = 1;
	private final int BACK_TO_MAIN_MENU = 2;
	private final int NUM_OPTIONS = 3;

	// Wait time
	private int wait;
	private final int WAIT_TIME = 5;

	private boolean enterReleased;

	/**
	 * Constructor
	 */
	public GameOverState()
	{
		currentOption = 0;
		wait = 0;
		currentScreen = GAME_OVER_SCREEN;

		options = new BufferedImage[NUM_OPTIONS];

		options[PLAY_AGAIN] = Asset.gameOverPlayAgain;
		options[CREDITS] = Asset.gameOverCredits;
		options[BACK_TO_MAIN_MENU] = Asset.gameOverBackToMainMenu;

		enterReleased = false;
		name = "";

		nameEntered = false;
	}

	/**
	 * Selects the current option
	 */
	public void select()
	{
		// Starts a new game
		if (currentOption == PLAY_AGAIN)
		{
			nameEntered = false;
			GamePanel.states[GameStateManager.GAME_STATE] = new GameState();
			GameStateManager.setCurrentState(GameStateManager.GAME_STATE);
		}
		// Goes to the credits menu
		else if (currentOption == CREDITS)
		{
			GameStateManager.setCurrentState(GameStateManager.CREDITS_STATE);
		}
		// Goes to the main menu
		else if (currentOption == BACK_TO_MAIN_MENU)
		{
			nameEntered = false;
			GameStateManager.setCurrentState(GameStateManager.MAIN_MENU_STATE);
		}
	}

	/**
	 * unused
	 */
	public void update()
	{
		if (GameState.highscore && !nameEntered)
		{
			currentScreen = NAME_SCREEN;
			nameEntered = true;
		}
	}

	public void sortScores()
	{
		HighscoreState.highScores[4] = Integer.parseInt(HUD.scoreString);
		HighscoreState.highNames[4] = name;

		int first;
		int temp;
		String tempName;
		for (int i = HighscoreState.highScores.length - 1; i > 0; i--)
		{
			first = 0;
			for (int j = 1; j <= i; j++)
			{
				if (HighscoreState.highScores[j] < HighscoreState.highScores[first])
					first = j;
			}
			temp = HighscoreState.highScores[first];
			HighscoreState.highScores[first] = HighscoreState.highScores[i];
			HighscoreState.highScores[i] = temp;
			tempName = HighscoreState.highNames[first];
			HighscoreState.highNames[first] = HighscoreState.highNames[i];
			HighscoreState.highNames[i] = tempName;
		}
	}

	/**
	 * Draws the image for the current option and displays the final score
	 */
	public void draw(Graphics2D g)
	{
		if (currentScreen == GAME_OVER_SCREEN)
		{
			g.drawImage(options[currentOption], 0, 0, null);

			g.setFont(Asset.finalScoreFont);
			g.setColor(Asset.fontColour);
			g.drawString("Score: " + HUD.scoreString, 100, 250);
		}
		else
		{
			g.drawImage(Asset.congratsMenu, 0, 0, null);
			g.drawString(name, 300, 650);
		}
	}

	/**
	 * Handles key pressed events
	 */
	public void keyPressed(int k)
	{
		if (currentScreen == GAME_OVER_SCREEN)
		{
			if ((k == KeyEvent.VK_ENTER || k == KeyEvent.VK_SPACE || k == KeyEvent.VK_SLASH)
					&& enterReleased)
			{
				enterReleased = false;
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
	}

	/**
	 * Handles key released events
	 */
	public void keyReleased(int k)
	{
		if (k == KeyEvent.VK_ENTER || k == KeyEvent.VK_SPACE
				|| k == KeyEvent.VK_SLASH)
		{
			enterReleased = true;
		}
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
	 * Handles key typed events. Used for getting the players name if they make
	 * it into the high score leader board.
	 */
	public void keyTyped(KeyEvent k)
	{
		if (currentScreen == NAME_SCREEN && name.length() <= 15)
		{
			char key = k.getKeyChar();
			if (key == KeyEvent.VK_ENTER && name.trim().length() > 0)
			{
				sortScores();

				try
				{
					PrintWriter scoreWriter = new PrintWriter("Highscores.txt");
					PrintWriter nameWriter = new PrintWriter("Highnames.txt");

					for (int i = 0; i < HighscoreState.highScores.length; i++)
					{
						scoreWriter.println(HighscoreState.highScores[i]);
						nameWriter.println(HighscoreState.highNames[i]);
					}

					scoreWriter.close();
					nameWriter.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				currentScreen = GAME_OVER_SCREEN;
			}
			else if ((Character.isLetter(key) || Character.isDigit(key) || Character.isSpaceChar(key)) && name.length() < 15)
			{
				name += key;
			}
			else if (key == KeyEvent.VK_BACK_SPACE && name.length() > 0)
			{
				name = name.substring(0, name.length() - 1);
			}
		}
	}

	/**
	 * Handles mouse clicked events
	 */
	public void mouseClicked(int x, int y)
	{
		if (currentScreen == GAME_OVER_SCREEN)
		{
			GamePanel.states[GameStateManager.CREDITS_STATE].mouseClicked(0, 0);
			if (y > 400)
			{
				select();
			}
		}
	}

	/**
	 * Handles mouse moved events
	 */
	public void mouseMoved(int x, int y)
	{
		if (currentScreen == GAME_OVER_SCREEN)
		{
			if (y < 500)
			{
				currentOption = PLAY_AGAIN;
			}
			else if (y >= 500 && y < 600)
			{
				currentOption = CREDITS;
			}
			else
			{
				currentOption = BACK_TO_MAIN_MENU;
			}
		}
	}

}
