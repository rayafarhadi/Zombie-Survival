package com.zsurvival.objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.zsurvival.assets.CollisionMap;

/**
 * The super class for all objects
 * @author Raya and Daniel
 */
public abstract class MapObject
{
	// Position
	protected int x;
	protected int y;

	// Dimensions
	protected int width;
	protected int height;

	// Collision
	protected CollisionMap map;
	protected ObjectType collisionObject;

	// Type
	protected ObjectType type;

	// Image
	protected BufferedImage image;

	/**
	 * Constructor
	 * @param x The x coordinate of the top left corner
	 * @param y The y coordinate of the top left corner
	 * @param width The width of the object
	 * @param height The height of the object
	 * @param map The collision map
	 * @param type The object type
	 */
	public MapObject(int x, int y, int width, int height, CollisionMap map, ObjectType type)
	{
		this.x = x;
		this.y = y;

		this.width = width;
		this.height = height;

		this.map = map;

		this.type = type;

		collisionObject = ObjectType.EMPTY;
	}

	/**
	 * Returns the objects x coordinate
	 * @return The objects x coordinate
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Returns the objects y coordinate
	 * @return The objects y coordinate
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * Returns the objects hit box
	 * @return The objects hit box
	 */
	public Rectangle getHitBox()
	{
		return new Rectangle(x, y, width, height);
	}

	/**
	 * Checks for collision
	 */
	public void checkCollision()
	{
		collisionObject = map.checkCollision(this);
	}

	/**
	 * Updates the object
	 */
	public abstract void update();

	/**
	 * Draws the object
	 * @param g The graphics object
	 */
	public abstract void draw(Graphics2D g);
}
