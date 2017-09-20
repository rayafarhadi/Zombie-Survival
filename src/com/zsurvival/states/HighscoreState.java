package com.zsurvival.states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.zsurvival.assets.Asset;

/**
 * The high score leader board (had to put high score files outside of the
 * resources folder because the jar files could not find them)
 * @author Raya and Daniel
 *
 */
public class HighscoreState implements State
{
	// Image
	private BufferedImage image;

	// File reader
	private BufferedReader reader;

	// High scores and names
	public static String[] highNames;
	public static int[] highScores;

	private boolean enterReleased;

	/**
	 * Constuctor
	 * @param image The menu image
	 */
	public HighscoreState()
	{
		image = Asset.highscoreMenu;
		enterReleased = false;
		highNames = new String[5];
		highScores = new int[5];
	}

	/**
	 * Updates the arrays with the info from the text files
	 */
	public void update()
	{
		try
		{
			reader = new BufferedReader(new FileReader("Highnames.txt"));
			for (int i = 0; i < 5; i++)
			{
				highNames[i] = reader.readLine();
			}

			reader = new BufferedReader(new FileReader("Highscores.txt"));
			for (int i = 0; i < 5; i++)
			{
				highScores[i] = Integer.parseInt(reader.readLine());
			}

			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Draws the menu
	 */
	public void draw(Graphics2D g)
	{
		g.setFont(Asset.highscoresFont);
		g.setColor(Asset.fontColour);
		g.drawImage(image, 0, 0, null);

		for (int i = 0; i < 5; i++)
		{
			g.drawString(highNames[i], 50, i * 100 + 200);
		}

		for (int i = 0; i < 5; i++)
		{
			g.drawString(String.format("%09d", highScores[i]), 550, i * 100 + 200);
		}
	}

	/**
	 * Handles key pressed events
	 */
	public void keyPressed(int k)
	{
		if ((k == KeyEvent.VK_ENTER || k == KeyEvent.VK_SPACE
				|| k == KeyEvent.VK_SLASH || k == KeyEvent.VK_BACK_SPACE)
				&& enterReleased)
		{
			enterReleased = false;
			GameStateManager.setCurrentState(GameStateManager.MAIN_MENU_STATE);
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
	}

	/**
	 * Unused
	 */
	public void keyTyped(KeyEvent key)
	{

	}

	/**
	 * Handles mouse clicked events
	 */
	public void mouseClicked(int x, int y)
	{
		if (!enterReleased)
		{
			enterReleased = true;
		}
		else
		{
			enterReleased = false;
			GameStateManager.setCurrentState(GameStateManager.INSTRUCTIONS_STATE);
		}
	}

	/**
	 * Unused
	 */
	public void mouseMoved(int x, int y)
	{

	}

}
