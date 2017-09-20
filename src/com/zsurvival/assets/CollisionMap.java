package com.zsurvival.assets;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import com.zsurvival.main.GamePanel;
import com.zsurvival.objects.Bullet;
import com.zsurvival.objects.Crate;
import com.zsurvival.objects.MapObject;
import com.zsurvival.objects.ObjectType;
import com.zsurvival.objects.entities.Entity;
import com.zsurvival.objects.entities.Player;
import com.zsurvival.objects.entities.Zombie;

/**
 * Holds and handles all object collision boxes
 * @author Raya and Daniel
 */
public class CollisionMap
{
	// Walls
	private ArrayList<Rectangle> walls;
	private Rectangle tempRect;

	// Zombies
	private ArrayList<Zombie> zombies;
	private ArrayList<Rectangle> zombieSpawns;

	// Players
	private ArrayList<Player> players;
	private int[] playerXs;
	private int[] playerYs;

	private int tempEntityNum;

	// Crates
	private ArrayList<Crate> crates;
	private int crateType;
	private Random randomCrate;
	private Point[] crateLocations;

	// Map image
	private BufferedImage image;

	public static final int PLAYER = 0;
	public static final int ZOMBIE = 1;

	/**
	 * Constructor
	 * @param image The maps image
	 * @param player1Position Player 1's starting spawn location
	 * @param player2Position Player 2's starting spawn location
	 * @param crateLocations The starting crate spawn locations
	 */
	public CollisionMap(BufferedImage image, Point player1Position, Point player2Position, Point[] crateLocations)
	{
		// Initialize array lists
		walls = new ArrayList<Rectangle>();
		zombieSpawns = new ArrayList<Rectangle>();
		players = new ArrayList<Player>();
		zombies = new ArrayList<Zombie>();
		crates = new ArrayList<Crate>();

		// Get the player starting positions
		playerXs = new int[2];
		playerXs[0] = (int) player1Position.getX();
		playerXs[1] = (int) player2Position.getX();

		playerYs = new int[2];
		playerYs[0] = (int) player1Position.getY();
		playerYs[1] = (int) player2Position.getY();

		// Set the image
		this.image = image;

		// Set the starting crate locations
		this.crateLocations = crateLocations;

		// Spawn the initial crate with random types (Either health or ammo)
		randomCrate = new Random();
		for (int i = 0; i < crateLocations.length; i++)
		{
			crateType = randomCrate.nextInt(2);
			crates.add(new Crate(crateType, crateLocations[i].x, crateLocations[i].y, this));
		}
	}

	/**
	 * Adds a wall's collision box to the collision map
	 * @param x X coordinate of the top left corner
	 * @param y Y coordinate of the top left corner
	 * @param width The wall's width
	 * @param height The wall's height
	 */
	public void addWall(int x, int y, int width, int height)
	{
		walls.add(new Rectangle(x, y, width, height));
	}

	/**
	 * Adds a zombie spawn location to the collision map
	 * @param x X coordinate of the top left corner
	 * @param y Y coordinate of the top left corner
	 * @param width Width of the spawn location
	 * @param height Height of the spawn location
	 */
	public void addZombieSpawn(int x, int y, int width, int height)
	{
		zombieSpawns.add(new Rectangle(x, y, width, height));
	}

	/**
	 * Adds a zombie's collision box to the collision map
	 * @param z The zombie to be added
	 */
	public void addZombie(Zombie z)
	{
		zombies.add(z);
	}

	/**
	 * Removes a zombie's collision box from the collision map
	 * @param z The zombie to be removed
	 */
	public void removeZombie(Zombie z)
	{
		zombies.remove(z);
	}

	/**
	 * Adds a player's collision box to the collision map
	 * @param p The player to be added
	 */
	public void addPlayer(Player p)
	{
		players.add(p);
	}

	/**
	 * Adds a crate's collision box to the collision map
	 * @param c The crate to be added
	 */
	public void addCrate(Crate c)
	{
		crates.add(c);
	}

	/**
	 * Removes a crate's collision box to the collision map
	 * @param c The crate to be removed
	 */
	public void removeCrate(Crate c)
	{
		crates.remove(c);
	}

	/**
	 * Checks collision between two objects
	 * @param o The object being checked
	 * @return The type of object it has collided with (returns empty if it is
	 *         not colliding)
	 */
	public ObjectType checkCollision(MapObject o)
	{
		tempRect = o.getHitBox();

		// Check bounds
		if (tempRect.getMinX() <= 0)
		{
			return ObjectType.WALL;
		}

		if (tempRect.getMaxX() >= GamePanel.WIDTH)
		{
			return ObjectType.WALL;
		}

		if (tempRect.getMinY() <= 0)
		{
			return ObjectType.WALL;
		}

		if (tempRect.getMaxY() >= GamePanel.HEIGHT)
		{
			return ObjectType.WALL;
		}

		// Check for walls
		for (int i = 0; i < walls.size(); i++)
		{
			if (tempRect.intersects(walls.get(i)))
			{
				return ObjectType.WALL;
			}
		}

		// Check for zombies
		for (int i = 0; i < zombies.size(); i++)
		{
			if (tempRect.intersects(zombies.get(i).getHitBox()))
			{
				if (!zombies.get(i).isDead() && !zombies.get(i).isDying())
				{
					return ObjectType.ZOMBIE;
				}
				else
				{
					return ObjectType.EMPTY;
				}
			}
		}

		return ObjectType.EMPTY;
	}

	/**
	 * Checks for collision between a crate and a player
	 * @param c The crate being checked
	 * @return The player that the crate is colliding with. (Returns null if the
	 *         crate is not colliding with a player)
	 */
	public Player checkCrateCollision(Crate c)
	{
		tempRect = c.getHitBox();

		// Check for players
		for (int i = 0; i < players.size(); i++)
		{
			if (tempRect.intersects(players.get(i).getHitBox()))
			{
				return players.get(i);
			}
		}

		return null;
	}

	/**
	 * Checks if there are any entities in the zombies spawn area and will not
	 * spawn if there is
	 * @param spawnBox The spawn area to be checked's collision box
	 * @return The object type in the spawn area (returns empty if there isn't
	 *         an entity in the spawn area)
	 */
	public ObjectType checkSpawnCollision(Rectangle spawnBox)
	{
		// Check for players
		for (int i = 0; i < players.size(); i++)
		{
			if (spawnBox.intersects(players.get(i).getHitBox()))
			{
				return ObjectType.PLAYER;
			}
		}

		// Check for zombies
		for (int i = 0; i < zombies.size(); i++)
		{
			if (spawnBox.intersects(zombies.get(i).getHitBox()))
			{
				if (!zombies.get(i).isDead())
				{
					return ObjectType.ZOMBIE;
				}
				else
				{
					return ObjectType.EMPTY;
				}
			}
		}

		return ObjectType.EMPTY;
	}

	/**
	 * Checks if the bullet collides with a zombie
	 * @param b The bullet being checked
	 * @return The zombie that the bullet collides with (returns null if the
	 *         bullet is not colliding or is colliding with something other than
	 *         a zombie)
	 */
	public Zombie checkBulletCollision(Bullet b)
	{
		// Check for zombies
		for (int i = 0; i < zombies.size(); i++)
		{
			if (zombies.get(i).getHitBox().intersects(b.getHitBox()))
			{
				if (!zombies.get(i).isDead() && !zombies.get(i).isDying())
				{
					return zombies.get(i);
				}
			}
		}

		return null;
	}

	/**
	 * Checks collision between entities (same for zombies and players)
	 * @param e The entity being checked
	 * @param entityType The type of entitiy (Player or zombie)
	 * @return The type of object it has collided with (returns empty if not
	 *         collidiing)
	 */
	public ObjectType checkEntityCollision(Entity e, int entityType)
	{
		tempRect = e.getHitBox();
		tempEntityNum = e.getNum();

		// Check bounds
		if (tempRect.getMinX() <= 0)
		{
			return ObjectType.WALL;
		}

		if (tempRect.getMaxX() >= GamePanel.WIDTH)
		{
			return ObjectType.WALL;
		}

		if (tempRect.getMinY() <= 0)
		{
			return ObjectType.WALL;
		}

		if (tempRect.getMaxY() >= GamePanel.HEIGHT)
		{
			return ObjectType.WALL;
		}

		// Check for walls
		for (int i = 0; i < walls.size(); i++)
		{
			if (tempRect.intersects(walls.get(i)))
			{
				return ObjectType.WALL;
			}
		}

		// Check for players
		for (int i = 0; i < players.size(); i++)
		{
			if (entityType == PLAYER && tempEntityNum == i)
			{
				continue;
			}
			if (tempRect.intersects(players.get(i).getHitBox()))
			{
				if (players.get(i).isKnifing())
				{
					return ObjectType.KNIFE;
				}
				else if (players.get(i).isDead())
				{
					return ObjectType.EMPTY;
				}

				return ObjectType.PLAYER;
			}
		}

		// Check for zombies
		for (int i = 0; i < zombies.size(); i++)
		{
			if (entityType == ZOMBIE && tempEntityNum == zombies.get(i).getNum())
			{
				continue;
			}
			if (tempRect.intersects(zombies.get(i).getHitBox()))
			{
				if (!zombies.get(i).isDead() && !zombies.get(i).isDying())
				{
					return ObjectType.ZOMBIE;
				}
				else
				{
					return ObjectType.EMPTY;
				}
			}
		}

		return ObjectType.EMPTY;
	}

	/**
	 * Checks if a zombie is colliding with a player
	 * @param z The zombie being checked
	 * @return The player being collided with (returns null if not colliding
	 *         with a player)
	 */
	public Player checkZombieCollision(Zombie z)
	{
		tempRect = z.getHitBox();

		// Check for players
		for (int i = 0; i < players.size(); i++)
		{
			if (tempRect.intersects(players.get(i).getHitBox()))
			{
				return players.get(i);
			}
		}

		return null;
	}

	/**
	 * Clears all of the collision boxes from the map other than the walls
	 */
	public void clear()
	{
		players.clear();
		zombies.clear();
		crates.clear();
		for (int i = 0; i < crateLocations.length; i++)
		{
			crateType = randomCrate.nextInt(2);
			crates.add(new Crate(crateType, crateLocations[i].x, crateLocations[i].y, this));
		}

	}

	/**
	 * Returns the maps image
	 * @return The maps image
	 */
	public BufferedImage getImage()
	{
		return image;
	}

	/**
	 * Returns the starting x coordinate of a player
	 * @param player The player's number (Player 1 or Player 2)
	 * @return The starting x coordinate of a player
	 */
	public int getStartingX(int player)
	{
		return playerXs[player];
	}

	/**
	 * Returns the starting y coordinate of a player
	 * @param player The player's number (Player 1 or Player 2)
	 * @return The starting y coordinate of a player
	 */
	public int getStartingY(int player)
	{
		return playerYs[player];
	}

	/**
	 * Returns the current x coordinate of a player
	 * @param player The player's number (Player 1 or Player 2)
	 * @return The current x coordinate of a player
	 */
	public int getPlayerX(int player)
	{
		return players.get(player).getX();
	}

	/**
	 * Returns the current y coordinate of a player
	 * @param player The player's number (Player 1 or Player 2)
	 * @return The current y coordinate of a player
	 */
	public int getPlayerY(int player)
	{
		return players.get(player).getY();
	}

	/**
	 * Returns the amount of zombie spawn locations
	 * @return The amount of zombie spawn locations
	 */
	public int getNumZombieSpawns()
	{
		return zombieSpawns.size();
	}

	/**
	 * Returns the collision box of the zombie spawn location
	 * @param spawn The zombie spawn location number
	 * @return The collision box of the zombie spawn location
	 */
	public Rectangle getSpawnBox(int spawn)
	{
		return zombieSpawns.get(spawn);
	}

	/**
	 * Returns the crates array list
	 * @return The crates array list
	 */
	public ArrayList<Crate> getCrates()
	{
		return crates;
	}

	/**
	 * Returns the crate locations
	 * @return The crate locations
	 */
	public Point[] getCrateLocations()
	{
		return crateLocations;
	}
}
