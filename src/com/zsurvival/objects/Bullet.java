package com.zsurvival.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.zsurvival.assets.Asset;
import com.zsurvival.assets.CollisionMap;

/**
 * The bullet object
 * @author Raya and Daniel
 */
public class Bullet extends MapObject
{
	// Bullets damage when it hits a zombie
	private int damage;

	// Image
	private BufferedImage image;

	// Collision
	private boolean collided;
	private int collisionUpdates;
	private boolean hittingZombie;

	// Dimensions
	private static final int BULLET_SIZE = 4;

	// Direction
	private Direction direction;

	/**
	 * Constructor
	 * @param x The x coordinate of the top left corner
	 * @param y The y coordinate of the top left corner
	 * @param direction The direction that the bullet is moving
	 * @param map the collision map
	 * @param damage The damage the bullet will deal if it hits a zombie
	 */
	public Bullet(int x, int y, Direction direction, CollisionMap map, int damage)
	{
		super(x, y, BULLET_SIZE, BULLET_SIZE, map, ObjectType.BULLET);

		this.damage = damage;

		collided = false;
		collisionUpdates = 0;
		collisionObject = ObjectType.EMPTY;
		hittingZombie = false;

		image = Asset.bullet;

		this.direction = direction;
	}

	/**
	 * Moves the bullet at 20 pixels per update in its current direction
	 */
	public void move()
	{
		if (direction == Direction.UP)
		{
			y -= 20;
		}
		else if (direction == Direction.UP_LEFT)
		{
			y -= 20;
			x -= 20;
		}
		else if (direction == Direction.UP_RIGHT)
		{
			y -= 20;
			x += 20;
		}
		else if (direction == Direction.DOWN)
		{
			y += 20;
		}
		else if (direction == Direction.DOWN_LEFT)
		{
			y += 20;
			x -= 20;
		}
		else if (direction == Direction.DOWN_RIGHT)
		{
			y += 20;
			x += 20;
		}
		else if (direction == Direction.LEFT)
		{
			x -= 20;
		}
		else if (direction == Direction.RIGHT)
		{
			x += 20;
		}
	}

	/**
	 * Returns the damage the bullet will deal if it hits a zombie
	 * @return The damage the bullet will deal if it hits a zombie
	 */
	public int getDamage()
	{
		return damage;
	}

	/**
	 * Set the damage that the bullet will deal if it hits a zombie
	 * @param damage The damage that the bullet will deal if it hits a zombie
	 */
	public void setDamage(int damage)
	{
		this.damage = damage;
	}

	/**
	 * Updates movement and collision
	 */
	public void update()
	{
		if (hittingZombie)
		{
			hittingZombie = false;
		}

		checkCollision();
		if (collisionObject == ObjectType.EMPTY)
		{
			move();
		}
		else
		{
			collisionUpdates++;
		}

		if (collisionUpdates >= 3)
		{
			collided = true;
		}

		if (collisionObject == ObjectType.ZOMBIE && collided)
		{
			hittingZombie = true;
		}
	}

	/**
	 * Returns whether or not the bullet has collided
	 * @return Whether or not the bullet has collided
	 */
	public boolean collided()
	{
		return collided;
	}

	/**
	 * Returns whether or not the bullet is hitting a zombie
	 * @return Whether or not the bullet is hitting a zombie
	 */
	public boolean isHittingZombie()
	{
		return hittingZombie;
	}

	/**
	 * Draws the bullet
	 */
	public void draw(Graphics2D g)
	{
		g.setColor(Color.WHITE);
		if (collisionObject == ObjectType.WALL || collisionObject == ObjectType.ZOMBIE)
		{
			g.drawImage(image, x, y, null);
		}
		else
		{
			g.drawRect(x, y, 1, 1);
		}
	}

}
