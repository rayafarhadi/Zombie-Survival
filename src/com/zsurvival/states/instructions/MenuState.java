package com.zsurvival.states.instructions;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.zsurvival.states.GameStateManager;
import com.zsurvival.states.State;

/**
 * Instruction sub menus
 * @author Raya and Daniel
 */
public class MenuState implements State
{
	// Image
	private BufferedImage image;

	private boolean enterReleased;

	/**
	 * Constuctor
	 * @param image The menu image
	 */
	public MenuState(BufferedImage image)
	{
		this.image = image;
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
		if ((k == KeyEvent.VK_ENTER || k == KeyEvent.VK_SPACE || k == KeyEvent.VK_SLASH || k == KeyEvent.VK_BACK_SPACE) && enterReleased)
		{
			enterReleased = false;
			GameStateManager.setCurrentState(GameStateManager.INSTRUCTIONS_STATE);
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
		else
		{
			enterReleased = false;
			GameStateManager.setCurrentState(GameStateManager.INSTRUCTIONS_STATE);
		}

	}

	/**
	 * unused
	 */
	public void mouseMoved(int x, int y)
	{

	}

}
