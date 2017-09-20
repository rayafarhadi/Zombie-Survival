package com.zsurvival.states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.zsurvival.assets.Asset;

/**
 * Credits menu
 * @author Raya and Daniel
 */
public class CreditsState implements State
{
	// Image
	private BufferedImage image;

	private boolean enterReleased;

	/**
	 * Constructor
	 */
	public CreditsState()
	{
		image = Asset.credits;
		enterReleased = false;
	}

	/**
	 * unused
	 */
	public void update()
	{

	}

	/**
	 * Draws the menu image
	 */
	public void draw(Graphics2D g)
	{
		g.drawImage(image, 0, 0, null);
	}

	/**
	 * Handles key pressed events
	 */
	public void keyPressed(int k)
	{
		if ((k == KeyEvent.VK_ENTER || k == KeyEvent.VK_SPACE || k == KeyEvent.VK_SLASH) && enterReleased)
		{
			enterReleased = false;
			GameStateManager.setCurrentState(GameStateManager.GAME_OVER_STATE);
		}
	}

	/**
	 * Handles key released events
	 */
	public void keyReleased(int k)
	{
		if (k == KeyEvent.VK_ENTER || k == KeyEvent.VK_SPACE || k == KeyEvent.VK_SLASH)
		{
			enterReleased = true;
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
		// If statement is for when the mouse is clicked in the instructions
		// state but enter is clicked in this state
		if (!enterReleased)
		{
			enterReleased = true;
		}

		enterReleased = false;
		GameStateManager.setCurrentState(GameStateManager.GAME_OVER_STATE);

	}

	/**
	 * unused
	 */
	public void mouseMoved(int x, int y)
	{

	}

}
