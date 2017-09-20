package com.zsurvival.states;

import com.zsurvival.main.GamePanel;

/**
 * Handles all of the states. Static class
 * @author Raya and Daniel
 */
public final class GameStateManager
{
	// State options
	private static State currentState;

	public static final int MAIN_MENU_STATE = 0;
	public static final int GAME_STATE = 1;
	public static final int INSTRUCTIONS_STATE = 2;
	public static final int OBJECTIVE_STATE = 3;
	public static final int CONTROLS_STATE = 4;
	public static final int HUD_STATE = 5;
	public static final int CRATES_STATE = 6;
	public static final int GAME_OVER_STATE = 7;
	public static final int CREDITS_STATE = 8;
	public static final int HIGHSCORE_STATE = 9;

	/**
	 * Private constructor
	 */
	private GameStateManager()
	{

	}

	/**
	 * Changes the current state
	 * @param state The state to be changed to
	 */
	public static void setCurrentState(int state)
	{
		currentState = GamePanel.states[state];
	}

	/**
	 * Returns the current state
	 * @return The current state
	 */
	public static State getCurrentState()
	{
		return currentState;
	}
}
