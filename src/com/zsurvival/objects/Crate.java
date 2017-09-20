package com.zsurvival.objects;

import java.awt.Graphics2D;
import java.util.Random;

import com.zsurvival.assets.Asset;
import com.zsurvival.assets.CollisionMap;
import com.zsurvival.objects.entities.Player;
import com.zsurvival.states.GameState;

/**
 * The crate object
 * @author Raya and Daniel
 */
public class Crate extends MapObject
{
	public boolean active;
	private Player tempPlayer;

	// Types of crate
	private int type;
	private Random randomCrate;

	private final int HEALTH = 0;
	private final int AMMO = 1;
	private final int POINTS = 2;
	private final int DAMAGE = 3;
	private final int NUKE = 4;

	private final int NUM_SPAWN_CRATES = 2;

	private GameState gameState;

	/**
	 * Constructor
	 * @param type The type of crate
	 * @param x The x coordinate of the top left corner
	 * @param y The y coordinate of the top left corner
	 * @param map The collision map
	 */
	public Crate(int type, int x, int y, CollisionMap map)
	{
		super(x, y, 50, 50, map, ObjectType.CRATE);
		active = true;
		this.type = type;
		randomCrate = new Random();
	}

	/**
	 * Randomizes the type of crate that spawns between waves (health or ammo)
	 */
	public void randomizeType()
	{
		type = randomCrate.nextInt(NUM_SPAWN_CRATES);
	}

	/**
	 * Activates the crates ability
	 * @param p The player that activated it
	 */
	public void activate(Player p)
	{
		if (active)
		{

			if (type == HEALTH)
			{
				tempPlayer.increaseHealth();
			}
			else if (type == AMMO)
			{
				tempPlayer.fillAmmo();
			}
			else if (type == POINTS)
			{
				gameState.doublePoints();
			}
			else if (type == DAMAGE)
			{
				gameState.doubleDamage();
			}
			else if (type == NUKE)
			{
				gameState.nuke();
			}
		}
	}

	/**
	 * Checks for collision with a player
	 */
	public void checkCollision()
	{
		tempPlayer = map.checkCrateCollision(this);
	}

	/**
	 * Allows each crate to have reference to the game state
	 * @param state The game state
	 */
	public void addGameState(GameState state)
	{
		this.gameState = state;
	}

	/**
	 * Updates player collision and whether or not the crate is still active
	 */
	public void update()
	{
		checkCollision();

		if (tempPlayer != null)
		{
			activate(tempPlayer);
			active = false;
		}
	}

	/**
	 * Draws the crate based on which type of crate it is
	 */
	public void draw(Graphics2D g)
	{
		if (active)
		{
			if (type == AMMO)
			{
				g.drawImage(Asset.ammoCrate, x, y, null);
			}
			else if (type == DAMAGE)
			{
				g.drawImage(Asset.damageCrate, x, y, null);
			}
			else if (type == POINTS)
			{
				g.drawImage(Asset.pointsCrate, x, y, null);
			}
			else if (type == HEALTH)
			{
				g.drawImage(Asset.healthCrate, x, y, null);
			}
			else if (type == NUKE)
			{
				g.drawImage(Asset.nukeCrate, x, y, null);
			}
		}
	}

}
