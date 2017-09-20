package com.zsurvival.objects;

import com.zsurvival.objects.entities.Player;

/**
 * The weapon object
 * @author Raya and Daniel
 */
public class Weapon
{
	// Weapon Info
	private String name;
	private int damage;
	private double speed;
	private int ammo;
	private int maxAmmo;
	private boolean unlocked;
	private Player player;
	private int weaponNum;

	// Upgrades
	private int startPrice;
	private int upgradeIndex;

	/**
	 * Constructor
	 * @param name The name of the weapon
	 * @param damage The amount of damage the weapon deals
	 * @param speed The fire rate
	 * @param ammo The starting ammo value
	 * @param maxAmmo The starting max ammo value
	 */
	public Weapon(String name, int damage, double speed, int ammo, int maxAmmo, boolean unlocked, int startPrice, Player player)
	{
		this.name = name;
		this.damage = damage;
		this.speed = speed;
		this.ammo = ammo;
		this.maxAmmo = maxAmmo;
		this.unlocked = unlocked;
		this.startPrice = startPrice;
		this.player = player;

		if (name.equals("Knife"))
		{
			weaponNum = Player.KNIFE;
		}
		else if (name.equals("Pistol"))
		{
			weaponNum = Player.PISTOL;
		}
		else if (name.equals("Rifle"))
		{
			weaponNum = Player.RIFLE;
		}
		else if (name.equals("Shotgun"))
		{
			weaponNum = Player.SHOTGUN;
		}

		upgradeIndex = 1;

	}

	/**
	 * Lowers the number of ammo by one when the weapon is shot
	 */
	public void shoot()
	{
		ammo--;
		if (ammo < 0)
		{
			ammo = 0;
		}
	}

	/**
	 * Returns the amount of damage the weapon deals
	 * @return The amount of damage the weapon deals
	 */
	public int getDamage()
	{
		return damage;
	}

	/**
	 * Returns the weapons fire rate
	 * @return The weapons fire rate
	 */
	public double getSpeed()
	{
		return speed;
	}

	/**
	 * Sets the weapons speed stat
	 */
	public void setSpeed(double speed)
	{
		this.speed = speed;
	}

	/**
	 * Returns the current amount of ammo that the weapon has
	 * @return The current amount of ammo that the weapon has
	 */
	public int getAmmo()
	{
		return ammo;
	}

	/**
	 * Returns the max amount of ammo that the weapon can hold
	 * @return The max amount of ammo that the weapon can hold
	 */
	public int getMaxAmmo()
	{
		return maxAmmo;
	}

	/**
	 * Returns the name of the weapon
	 * @return The name of the weapon
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Returns the starting price if the weapon's not unlocked or the price for
	 * the next upgrade if it is
	 * @return The next price
	 */
	public int getNextPrice()
	{
		if (startPrice > 0)
		{
			if (unlocked)
			{
				return startPrice * upgradeIndex * 2;
			}
			else
			{
				return startPrice;
			}

		}
		else
		{
			return 300 * upgradeIndex * 2;
		}
	}

	/**
	 * Upgrades the weapon
	 */
	public void upgrade()
	{
		if (!isMaxed())
		{
			upgradeIndex++;
			if (weaponNum != Player.KNIFE)
			{
				if (upgradeIndex % 3 == 1)
				{
					increaseDamage();
				}
				else if (upgradeIndex % 3 == 2)
				{
					player.increaseWeaponSpeed(weaponNum, upgradeIndex);
				}
				else
				{
					doubleMaxAmmo();
				}
			}
			else
			{
				increaseDamage();
			}

			fillAmmo();
		}
	}

	/**
	 * Increases the weapons damage
	 */
	public void increaseDamage()
	{
		damage *= 1.2;
	}

	/**
	 * Fills all of the weapons ammo
	 */
	public void fillAmmo()
	{
		ammo = maxAmmo;
	}

	/**
	 * Increases the maximum amount of ammo that the weapon can hold by 50%
	 */
	public void doubleMaxAmmo()
	{
		maxAmmo *= 1.5;
	}

	/**
	 * Returns whether or not the weapon has been unlocked
	 * @return Whether or not the weapon has been unlocked
	 */
	public boolean isUnlocked()
	{
		return unlocked;
	}

	/**
	 * Returns if the weapon is fully upgraded
	 * @return if the weapon is fully upgraded
	 */
	public boolean isMaxed()
	{
		return upgradeIndex == 10;
	}

	/**
	 * Unlocks the weapon
	 */
	public void unlock()
	{
		unlocked = true;
	}

}
