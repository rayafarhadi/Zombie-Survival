package com.zsurvival.assets;

import java.awt.image.BufferedImage;

/**
 * A sprite sheet
 * @author Raya and Daniel
 */
public class SpriteSheet
{
	private BufferedImage sheet;

	/**
	 * constructor
	 * @param sheet The sheet image
	 */
	public SpriteSheet(BufferedImage sheet)
	{
		this.sheet = sheet;
	}

	/**
	 * Returns a sub image of a sprite from the sprite sheet
	 * @param x The x coordinate of the top left corner
	 * @param y the y coordinate of the top left corner
	 * @param width The width of the sprite
	 * @param height The height of the sprite
	 * @return A sub image of a sprite from the sprite sheet
	 */
	public BufferedImage getSubImage(int x, int y, int width, int height)
	{
		return sheet.getSubimage(x, y, width, height);
	}

	/**
	 * Returns the width of the sprite sheet
	 * @return The width of the sprite sheet
	 */
	public int getWidth()
	{
		return sheet.getWidth() / Asset.SPRITE_SIZE;
	}

	/**
	 * Returns the height of the sprite sheet
	 * @return The height of the sprite sheet
	 */
	public int getHeight()
	{
		return sheet.getHeight() / Asset.SPRITE_SIZE;
	}
}
