package com.zsurvival.objects.entities;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import com.zsurvival.assets.CollisionMap;
import com.zsurvival.objects.Animation;
import com.zsurvival.objects.MapObject;
import com.zsurvival.objects.ObjectType;

/**
 * Super class for all entities (Players and zombies)
 * @author Raya and Daniel
 */
public abstract class Entity extends MapObject
{
	// Entity number (for collision checking, to ensure it doesn't collide with
	// itself)
	protected int num;

	// Animation
	protected Animation animation;

	// Image variables
	protected AffineTransform reset;
	protected double rotation;
	protected boolean up;
	protected boolean down;
	protected boolean right;
	protected boolean left;
	protected boolean reflect;
	protected int staticPosition;
	protected int imageWidth;
	protected int imageHeight;

	// Rotations
	protected final double UP_ROTATION = Math.toRadians(-90);
	protected final double UP_RIGHT_ROTATION = Math.toRadians(-45);
	protected final double DOWN_ROTATION = Math.toRadians(90);
	protected final double DOWN_RIGHT_ROTATION = Math.toRadians(45);

	// Positions
	protected final int UP_POSITION = 1;
	protected final int DOWN_POSITION = 2;
	protected final int RIGHT_POSITION = 3;
	protected final int LEFT_POSITION = 4;

	/**
	 * Constructor
	 * @param num The entity number
	 * @param x The x coordinate of the top left corner
	 * @param y The y coordinate of the top left corner
	 * @param width The width of the entity's collision box
	 * @param height The height of the entity's collision box
	 * @param imageWidth The width of the entity's image
	 * @param imageHeight the height of the entity's image
	 * @param map The collision map
	 * @param type The object type
	 */
	public Entity(int num, int x, int y, int width, int height, int imageWidth, int imageHeight, CollisionMap map, ObjectType type)
	{
		super(x, y, width, height, map, type);

		reset = new AffineTransform();
		reset.rotate(0, 0, 0);
		reflect = false;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.num = num;
	}

	/**
	 * Rotate the image
	 */
	public void rotate()
	{
		rotation = 0;
		if (up)
		{
			if (right)
			{
				rotation = UP_RIGHT_ROTATION;
			}
			else if (left)
			{
				rotation = DOWN_RIGHT_ROTATION;
				reflect = true;
			}
			else
			{
				rotation = UP_ROTATION;
			}
		}
		else if (down)
		{
			if (right)
			{
				rotation = DOWN_RIGHT_ROTATION;
			}
			else if (left)
			{
				rotation = UP_RIGHT_ROTATION;
				reflect = true;
			}
			else
			{
				rotation = DOWN_ROTATION;
			}
		}
		else
		{
			if (left)
			{
				reflect = true;
			}
		}
	}

	/**
	 * Returns the entity's hit box
	 * @return The entity's hit box
	 */
	public Rectangle getHitBox()
	{
		return new Rectangle(x + (imageWidth / 4), y + (imageHeight / 4), width, height);
	}

	/**
	 * Returns the entity number
	 * @return The entity number
	 */
	public int getNum()
	{
		return num;
	}

}
