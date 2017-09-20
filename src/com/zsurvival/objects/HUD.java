package com.zsurvival.objects;

import java.awt.Color;
import java.awt.Graphics2D;

import com.zsurvival.assets.Asset;
import com.zsurvival.objects.entities.Player;

/**
 * Heads up display
 * @author Raya and Daniel
 */
public class HUD
{
	// Wave variables
	private int wave;
	private boolean displayWave;
	private double alpha;
	private boolean alphaDown;

	// Player variables
	private Player[] players;
	private Player tempPlayer;
	private String[] names;
	private String healthString;

	// Score
	private int score;
	public static String scoreString;

	// Power ups
	private boolean damageActive;
	private boolean pointsActive;

	/**
	 * Constructor
	 * @param players Array of players
	 */
	public HUD(Player[] players)
	{
		wave = 1;
		displayWave = true;
		alpha = 0;

		this.players = players;
		names = new String[players.length];

		for (int i = 0; i < players.length; i++)
		{
			names[i] = players[i].getName();
		}

		score = 0;
		scoreString = String.format("%09d", score);
	}

	/**
	 * Advances to the next wave
	 */
	public void nextWave()
	{
		wave++;
	}

	/**
	 * Sets whether or not the wave should be displayed (displays during the
	 * break between waves)
	 * @param b Whether or not the wave should be displayed
	 */
	public void displayWave(boolean b)
	{
		displayWave = b;
	}

	/**
	 * Returns whether or not the wave is being displayed
	 * @return Whether or not the wave is being displayed
	 */
	public boolean isDisplayingWave()
	{
		return displayWave;
	}

	/**
	 * Sets whether or not the alpha of the wave display font should be lowered
	 * @param b Whether or not the alpha of the wave display font should be
	 *            lowered
	 */
	public void setAlphaDown(boolean b)
	{
		alphaDown = b;
	}

	/**
	 * Returns whether or not the alpha of the wave display font is being
	 * lowered
	 * @return Whether or not the alpha of the wave display font is being
	 *         lowered
	 */
	public boolean isAlphaDown()
	{
		return alphaDown;
	}

	/**
	 * Increases the score and formats the string to have up to 9 leading zeroes
	 * @param score The amount the score should be increased by
	 */
	public void setScore(int score)
	{
		this.score = score;
		scoreString = String.format("%09d", this.score);
	}

	public void damageActive(boolean b)
	{
		damageActive = b;
	}

	public void pointsActive(boolean b)
	{
		pointsActive = b;
	}

	/**
	 * Draws the wave, the player healths, the score and the current player
	 * weapons and ammo. Also lowers the alpha of the display wave font.
	 * @param g
	 */
	public void draw(Graphics2D g)
	{
		// Makes the wave display fade in and fade out
		if (displayWave)
		{
			if (!alphaDown)
			{
				alpha++;
				if (alpha > 200)
				{
					alpha = 200;
				}
			}
			else if (alphaDown)
			{
				alpha--;
				if (alpha < 0)
				{
					alpha = 0;
				}
			}

			g.setColor(new Color(186, 0, 0, (int) alpha));
			g.setFont(Asset.newWaveFont);
			g.drawString("WAVE " + wave, 300, 400);
		}

		// Draws the score
		g.setColor(Asset.fontColour);

		g.setFont(Asset.scoreFont);
		g.drawString(scoreString, 690, 50);

		// Draws the wave
		g.drawString(wave + "", 20, 40);

		for (int i = 0; i < players.length; i++)
		{
			tempPlayer = players[i];

			g.setFont(Asset.healthFont);

			// Draws the rings under the players
			if (i == 0)
			{
				g.setColor(Asset.fontColour);
				g.fillOval(720, 80, 20, 20);
				g.fillOval(15, 675, 20, 20);
			}
			else if (i == 1)
			{
				g.setColor(Color.BLUE);
				g.fillOval(720, 130, 20, 20);
				g.fillOval(665, 675, 20, 20);
			}

			// Draws player health
			g.setColor(Asset.fontColour);
			healthString = String.format("%s %3d", names[i], tempPlayer.getHealth());
			g.drawString(healthString, 760, 100 + (50 * i));

			g.setFont(Asset.weaponFont);

			// Draws the current weapon and if it's a gun the amount of ammo it
			// has
			if (tempPlayer.getCurrentWeapon().getName().equals("Knife"))
			{
				g.drawString(tempPlayer.getCurrentWeapon().getName(), 50 + (i * 650), 700);
			}
			else
			{
				g.drawString(tempPlayer.getCurrentWeapon().getName() + ": " + tempPlayer.getCurrentWeapon().getAmmo(), 50 + (i * 650), 700);
			}

			// Displays the double damage icon if the power up is active
			if (damageActive)
			{
				g.drawImage(Asset.damageActive, 15, 80, null);
			}

			// Displays the double points icon if the power up is active
			if (pointsActive)
			{
				g.drawImage(Asset.pointsActive, 75, 80, null);
			}

		}

	}
}
