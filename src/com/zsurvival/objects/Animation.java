package com.zsurvival.objects;

import java.awt.image.BufferedImage;

/**
 * Runs entity animations
 * @author Raya and Daniel
 */
public class Animation
{
	private int frameDelay;
	private int actionDelay;
	private int actionTime;
	private int currentFrame;
	private int updates;
	private boolean finished;
	private BufferedImage[] frames;

	/**
	 * Constructor
	 * @param frameDelay The number of updates between frames
	 * @param frames The number of frames per animation iteration
	 */
	public Animation(int frameDelay, BufferedImage[] frames)
	{
		this.frameDelay = frameDelay;
		actionDelay = 0;
		actionTime = 0;
		this.frames = frames;
		currentFrame = 0;
		updates = 0;
		finished = false;
	}

	/**
	 * Constructor
	 * @param frameDelay The number of updates between frames
	 * @param actionDelay the number of updates between animation iteration
	 * @param frames The number of frames per animation iteration
	 */
	public Animation(int frameDelay, int actionDelay, BufferedImage[] frames)
	{
		this.frameDelay = frameDelay;
		this.actionDelay = actionDelay;
		actionTime = 0;
		this.frames = frames;
		currentFrame = 0;
		updates = 0;
		finished = false;
	}

	/**
	 * Updates the animations current frame and delay
	 */
	public void update()
	{
		finished = false;
		actionTime++;
		if (actionTime > actionDelay)
		{
			updates++;
			finished = false;

			if (updates > frameDelay)
			{
				currentFrame++;
				if (currentFrame >= frames.length)
				{
					currentFrame = 0;
					finished = true;
					actionTime = 0;
				}
				updates = 0;
			}
		}
	}

	/**
	 * Returns the image from the current frame of the animation
	 * @return The image from the current frame of the animation
	 */
	public BufferedImage getImage()
	{
		return frames[currentFrame];
	}

	/**
	 * Returns whether or not the animation has finished
	 * @return Whether or not the animation has finished
	 */
	public boolean isFinished()
	{
		return finished;
	}

	/**
	 * Returns the current frame of the animation
	 * @return The current frame of the animation
	 */
	public int getFrame()
	{
		return currentFrame;
	}

	/**
	 * Returns whether or not the current animation is at the beginning of it's
	 * iteration
	 * @return Whether or not the current animation is at the beginning of it's
	 *         iteration
	 */
	public boolean active()
	{
		return updates == 0;
	}

	/**
	 * Sets the delay between animation iterations
	 * @param delay The delay between animation iterations
	 */
	public void setActionDelay(int delay)
	{
		actionDelay = delay;
	}

	/**
	 * Decreases the delay between frames (For speeding up weapon fire rates
	 * after action delay has reached zero)
	 */
	public void decreaseFrameDelay()
	{
		frameDelay--;
	}

	/**
	 * Returns the total delay between iterations ((delay between iterations *
	 * delay between frames * number of frames per iteration) or if the delay
	 * between iterations is zero then just (delay between frames * number of
	 * frames per iteration)
	 * @return The total delay between iterations
	 */
	public int getDelay()
	{
		if (actionDelay > 1)
		{
			return actionDelay * frameDelay * frames.length;
		}
		else
		{
			return frameDelay * frames.length;
		}
	}

}