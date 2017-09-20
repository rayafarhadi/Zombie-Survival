package com.zsurvival.objects.entities;

import java.awt.Graphics2D;
import java.util.Random;

import com.zsurvival.assets.Asset;
import com.zsurvival.assets.CollisionMap;
import com.zsurvival.objects.Animation;
import com.zsurvival.objects.ObjectType;

/**
 * The zombie object
 * @author Raya and Daniel
 */
public class Zombie extends Entity
{
	// Stats
	private int health;
	private int damage;
	private boolean die;
	private boolean dead;

	// Tracking and attacking
	private Player[] players;
	private int trackingPlayer;
	private Player attackingPlayer;
	private Random random;

	// Animations
	private Animation attackAnimation;
	private Animation moveAnimation;
	private Animation bloodAnimation;

	// Actions
	private boolean attacking;
	private boolean attacked;
	private boolean movingUp;
	private boolean movingDown;
	private boolean movingRight;
	private boolean movingLeft;

	// Direction
	private int trackDirection;

	private final int HORIZONTAL = 0;
	private final int VERTICAL = 1;

	private int changeWait;
	private final int CHANGE_TIME = 25;

	/**
	 * Constructor
	 * @param num The zombie's number
	 * @param health The zombie's starting health
	 * @param x The x coordinate of the top left corner
	 * @param y The y coordinate of the top left corner
	 * @param width The width of the zombie's hit box
	 * @param height The height of the zombie's hit box
	 * @param imageWidth The width of the zombie's image
	 * @param imageHeight The height of the zombie's image
	 * @param map The collision map
	 * @param players Array of players
	 */
	public Zombie(int num, int health, int x, int y, int width, int height, int imageWidth, int imageHeight, CollisionMap map, Player[] players)
	{
		super(num, x, y, width, height, imageWidth, imageHeight, map, ObjectType.ZOMBIE);
		this.players = players;

		this.health = health;
		damage = 10;
		die = false;
		dead = false;

		up = true;

		// Animations
		moveAnimation = new Animation(5, Asset.zombieWalk);
		attackAnimation = new Animation(5, Asset.zombieAttack);
		bloodAnimation = new Animation(3, Asset.zombieBlood);

		random = new Random();

		map.addZombie(this);

		findPlayer();
	}

	/**
	 * Move the zombie and check for collision
	 */
	private void move()
	{
		if (movingUp)
		{
			up = true;
			down = false;
			y--;
			checkCollision();

			if (collisionObject == ObjectType.WALL || collisionObject == ObjectType.ZOMBIE || collisionObject == ObjectType.PLAYER)
			{
				y++;
				up = false;
				if (!movingLeft && !movingRight)
				{
					up = true;
				}
			}
		}
		else if (movingDown)
		{
			up = false;
			down = true;
			y++;
			checkCollision();

			if (collisionObject == ObjectType.WALL || collisionObject == ObjectType.ZOMBIE || collisionObject == ObjectType.PLAYER)
			{
				y--;
				down = false;
				if (!movingLeft && !movingRight)
				{
					down = true;
				}
			}
		}
		else if (!movingUp && !movingDown)
		{
			up = false;
			down = false;
		}

		if (movingLeft)
		{
			x--;
			left = true;
			right = false;
			checkCollision();

			if (collisionObject == ObjectType.WALL || collisionObject == ObjectType.ZOMBIE || collisionObject == ObjectType.PLAYER)
			{
				x++;
				left = false;
				if (!movingUp && !movingDown)
				{
					left = true;
				}
			}
		}
		else if (movingRight)
		{
			left = false;
			right = true;
			x++;
			checkCollision();

			if (collisionObject == ObjectType.WALL || collisionObject == ObjectType.ZOMBIE || collisionObject == ObjectType.PLAYER)
			{
				x--;
				right = false;
			}
		}
		else if (!movingLeft && !movingRight)
		{
			left = false;
			right = false;
		}
	}

	/**
	 * Kills the zombie instantly (used with the nuke power up)
	 */
	public void kill()
	{
		health = 0;
	}

	/**
	 * Find the player that the zombie is going to track
	 */
	private void findPlayer()
	{
		if (players.length > 1)
		{
			trackingPlayer = random.nextInt(2);
			if (players[trackingPlayer].isDead())
			{
				if (trackingPlayer == 0)
				{
					trackingPlayer = 1;
				}
				else
				{
					trackingPlayer = 0;
				}
			}
		}
		else
		{
			trackingPlayer = 0;
		}
	}

	/**
	 * Track the player and add a little bit of randomized movement
	 */
	private void trackPlayer()
	{
		int playerX = map.getPlayerX(trackingPlayer);
		int playerY = map.getPlayerY(trackingPlayer);

		// Adds a bit of randomization to the movement so they don't all follow
		// the same tracking line
		if (changeWait <= 1)
		{
			trackDirection = random.nextInt(3);
			changeWait += CHANGE_TIME;
		}
		else
		{
			changeWait--;
		}

		movingLeft = false;
		movingRight = false;
		movingUp = false;
		movingDown = false;

		if (trackDirection == HORIZONTAL && playerX != x && collisionObject == ObjectType.EMPTY || collisionObject == ObjectType.CRATE)
		{
			if (playerX < x)
			{
				movingLeft = true;
				movingRight = false;
			}
			else if (playerX > x)
			{
				movingRight = true;
				movingLeft = false;
			}
		}
		else if (trackDirection == VERTICAL && playerY != y && collisionObject == ObjectType.EMPTY || collisionObject == ObjectType.CRATE)
		{
			if (playerY < y)
			{
				movingUp = true;
				movingDown = false;
			}
			else if (playerY > y)
			{
				movingDown = true;
				movingUp = false;
			}
		}
		else
		{
			if (playerX < x)
			{
				movingLeft = true;
				movingRight = false;
			}
			else if (playerX > x)
			{
				movingRight = true;
				movingLeft = false;
			}

			if (playerY < y)
			{
				movingUp = true;
				movingDown = false;
			}
			else if (playerY > y)
			{
				movingDown = true;
				movingUp = false;
			}
		}
	}

	/**
	 * Attack the player
	 */
	private void attack()
	{
		attackingPlayer.hit(damage);
	}

	/**
	 * Check for collision
	 */
	public void checkCollision()
	{
		// Checks regular entity collision
		collisionObject = map.checkEntityCollision(this, CollisionMap.ZOMBIE);

		// If it is colliding with a player it checks zombie specific collision
		// to find out which player it is attacking
		if (collisionObject == ObjectType.PLAYER || collisionObject == ObjectType.KNIFE)
		{
			attackingPlayer = map.checkZombieCollision(this);
		}
		else if (collisionObject == ObjectType.CRATE)
		{
			collisionObject = ObjectType.EMPTY;
		}
	}

	/**
	 * Check which animation should be played
	 */
	public void checkAnimation()
	{
		if (attacking)
		{
			animation = attackAnimation;
		}
		else
		{
			animation = moveAnimation;
		}
	}

	/**
	 * Returns whether or not the zombie is dead
	 * @return Whether or not the zombie is dead
	 */
	public boolean isDead()
	{
		return dead;
	}

	/**
	 * Returns whether or not the zombie is dying (Used for death animation
	 * purposes)
	 * @return Whether or not the zombie is dying
	 */
	public boolean isDying()
	{
		return die;
	}

	/**
	 * Take damage
	 * @param damage The amount of damage to be taken
	 */
	public void hit(int damage)
	{
		health -= damage;
	}

	/**
	 * Returns the zombie's number
	 * @return The zombie's number
	 */
	public int getNum()
	{
		return num;
	}

	/**
	 * Updates the zombie's animation, movement and collision
	 */
	public void update()
	{

		// plays the blood splatter animation
		if (die)
		{
			bloodAnimation.update();
			animation = moveAnimation;
		}
		else
		{
			if (collisionObject == ObjectType.KNIFE)
			{
				hit(players[trackingPlayer].weapons[0].getDamage());
			}

			if (collisionObject == ObjectType.PLAYER || collisionObject == ObjectType.KNIFE)
			{
				attacking = true;
			}
			else
			{
				attacking = false;
			}

			if (!attacking)
			{
				trackPlayer();
			}
			else
			{
				if (animation.getFrame() != 4)
				{
					attacked = false;
				}

				if (animation.getFrame() == 4 && !attacked)
				{
					attack();
					attacked = true;
				}
			}
			move();
			checkAnimation();

			// If the player the zombie was currently attacking dies it will
			// find the other player on the map
			if (players[trackingPlayer].isDead())
			{
				findPlayer();
			}
		}

		animation.update();

		if (health <= 0)
		{
			die = true;
		}

		if (bloodAnimation.getFrame() == 15)
		{
			dead = true;
			die = false;
		}

	}

	/**
	 * Draws the zombie with the correct animation and direction
	 */
	public void draw(Graphics2D g)
	{
		if (animation != null)
		{
			rotate();
			g.rotate(rotation, x + (imageWidth / 2), y + (imageHeight / 2));

			if (reflect)
			{
				if (bloodAnimation.getFrame() < 5)
				{
					g.drawImage(animation.getImage(), x + imageWidth, y, -imageWidth, imageHeight, null);
				}
				if (die)
				{
					g.drawImage(bloodAnimation.getImage(), x + imageWidth, y, -imageWidth, imageHeight, null);
				}
			}
			else
			{

				if (bloodAnimation.getFrame() < 5)
				{
					g.drawImage(animation.getImage(), x, y, null);
				}
				if (die)
				{
					g.drawImage(bloodAnimation.getImage(), x, y, null);
				}
			}

			reflect = false;

			g.setTransform(reset);
		}
	}
}
