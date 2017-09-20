package com.zsurvival.states.instructions;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.zsurvival.assets.Asset;
import com.zsurvival.main.GamePanel;
import com.zsurvival.states.GameStateManager;
import com.zsurvival.states.State;

/**
 * Instructions menu
 * @author Raya and Daniel
 */
public class InstructionsState implements State
{
	// The menu options
	private BufferedImage[] options;
	private int currentOption;

	private final int OBJECTIVE = 0;
	private final int CONTROLS = 1;
	private final int HUD = 2;
	private final int CRATES = 3;
	private final int BACK_TO_MAIN_MENU = 4;
	private final int NUM_OPTIONS = 5;

	// Time to wait between key presses if the key is being held down so that
	// the transitions between options is smoother
	private int wait;
	private final int WAIT_TIME = 5;

	private boolean clicked;

	/**
	 * Constructor
	 */
	public InstructionsState()
	{
		currentOption = 0;
		clicked = false;
		wait = 0;

		options = new BufferedImage[NUM_OPTIONS];

		options[OBJECTIVE] = Asset.instructionsObjective;
		options[CONTROLS] = Asset.instructionsControls;
		options[HUD] = Asset.instructionsHUD;
		options[CRATES] = Asset.instructionsCrates;
		options[BACK_TO_MAIN_MENU] = Asset.instructionsBackToMainMenu;
	}

	/**
	 * Selects the current option
	 */
	public void select()
	{
		// Goes to the objective menu
		if (currentOption == OBJECTIVE)
		{
			if (clicked)
			{
				GamePanel.states[GameStateManager.OBJECTIVE_STATE].mouseClicked(0, 0);
				clicked = false;
			}
			GameStateManager.setCurrentState(GameStateManager.OBJECTIVE_STATE);
		}
		// Goes to the controls menu
		else if (currentOption == CONTROLS)
		{
			if (clicked)
			{
				GamePanel.states[GameStateManager.CONTROLS_STATE].mouseClicked(0, 0);
				clicked = false;
			}
			GameStateManager.setCurrentState(GameStateManager.CONTROLS_STATE);
		}
		// Goes to the HUD menu
		else if (currentOption == HUD)
		{
			if (clicked)
			{
				GamePanel.states[GameStateManager.HUD_STATE].mouseClicked(0, 0);
				clicked = false;
			}
			GameStateManager.setCurrentState(GameStateManager.HUD_STATE);
		}
		// Goes to the crates menu
		else if (currentOption == CRATES)
		{
			if (clicked)
			{
				GamePanel.states[GameStateManager.CRATES_STATE].mouseClicked(0, 0);
				clicked = false;
			}
			GameStateManager.setCurrentState(GameStateManager.CRATES_STATE);
		}
		// Goes to the main menu
		else if (currentOption == BACK_TO_MAIN_MENU)
		{
			if (clicked)
			{
				clicked = false;
			}
			GameStateManager.setCurrentState(GameStateManager.MAIN_MENU_STATE);
		}
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
		if (k == KeyEvent.VK_ENTER || k == KeyEvent.VK_SLASH || k == KeyEvent.VK_SPACE)
		{
			select();
		}
		if (k == KeyEvent.VK_BACK_SPACE)
		{
			currentOption = BACK_TO_MAIN_MENU;
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
		clicked = true;
		select();
	}

	/**
	 * Handles mouse movement events
	 */
	public void mouseMoved(int x, int y)
	{
		if (y < 275)
		{
			currentOption = OBJECTIVE;
		}
		else if (y >= 275 && y < 375)
		{
			currentOption = CONTROLS;
		}
		else if (y >= 375 && y < 500)
		{
			currentOption = HUD;
		}
		else if (y >= 500 && y < 600)
		{
			currentOption = CRATES;
		}
		else
		{
			currentOption = BACK_TO_MAIN_MENU;
		}
	}

}
