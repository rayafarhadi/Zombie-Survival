package com.zsurvival.assets;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;

import com.zsurvival.main.Game;

/**
 * Loads resources
 * @author Raya and Daniel
 */
public class Loader
{
	// graphics environment for loading font
	private GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

	/**
	 * Loads an image
	 * @param path The path to the image location
	 * @return The image
	 */
	public BufferedImage loadImage(String path)
	{
		try
		{
			MediaTracker tracker = new MediaTracker(Game.panel);
			DataInputStream datainputstream = new DataInputStream(getClass().getResourceAsStream(path));

			byte[] bytes = new byte[datainputstream.available()];

			datainputstream.readFully(bytes);
			datainputstream.close();

			Image image = Toolkit.getDefaultToolkit().createImage(bytes);

			tracker.addImage(image, 1);
			tracker.waitForID(1);
			tracker.removeImage(image);

			BufferedImage buffered = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

			buffered.getGraphics().drawImage(image, 0, 0, null);

			return buffered;
		}
		catch (IOException | InterruptedException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Separates sprites from a sprite sheet
	 * @param sheet The sprite sheet
	 * @param numSprites The number of sprites in the sprite sheet
	 * @return The separated sprites in an array
	 */
	public BufferedImage[] loadSheet(SpriteSheet sheet, int numSprites)
	{
		BufferedImage[] sprites = new BufferedImage[numSprites];
		int count = 0;

		for (int i = 0; i < sheet.getHeight(); i++)
		{
			for (int j = 0; j < sheet.getWidth(); j++)
			{
				if (count >= numSprites)
				{
					break;
				}

				sprites[count] = sheet.getSubImage(j * Asset.SPRITE_SIZE, i * Asset.SPRITE_SIZE, Asset.SPRITE_SIZE, Asset.SPRITE_SIZE);
				count++;
			}
		}

		return sprites;
	}

	/**
	 * Loads a font
	 * @param path The path to the font location
	 */
	public void loadFont(String path)
	{
		try
		{
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,
					getClass().getResourceAsStream(path)));
		}
		catch (FontFormatException | IOException e)
		{
			e.printStackTrace();
		}
	}
}
