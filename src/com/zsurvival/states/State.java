package com.zsurvival.states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 * Super class for all states
 * @author Raya and Daniel
 */
public interface State
{
	/**
	 * Updates the state
	 */
	public void update();

	/**
	 * Draws the states image
	 * @param g The graphics object
	 */
	public void draw(Graphics2D g);

	/**
	 * Handles key pressed events
	 * @param k The key code of the key being pressed
	 */
	public void keyPressed(int k);

	/**
	 * Handles key released events
	 * @param k The key code of the key being released
	 */
	public void keyReleased(int k);

	/**
	 * Handles key typed events
	 * @param k The key code of the key being typed
	 */
	public void keyTyped(KeyEvent k);

	/**
	 * Handles mouse clicked events
	 * @param x The x coordinate of the mouse when it was clicked
	 * @param y The y coordinate of the mouse when it was clicked
	 */
	public void mouseClicked(int x, int y);

	/**
	 * Handles mouse moved events
	 * @param x The x coordinate of the mouse
	 * @param y The y coordinate of the mouse
	 */
	public void mouseMoved(int x, int y);
}
