package com.zsurvival.objects.entities;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.zsurvival.assets.Asset;
import com.zsurvival.assets.CollisionMap;
import com.zsurvival.audio.Audio;
import com.zsurvival.objects.Animation;
import com.zsurvival.objects.Bullet;
import com.zsurvival.objects.Direction;
import com.zsurvival.objects.ObjectType;
import com.zsurvival.objects.Weapon;
import com.zsurvival.states.GameState;

/**
 * The player object
 * @author Raya and Daniel
 */
public class Player extends Entity
{
	// Stats
	private String name;
	private int health;
	private int damage;
	private boolean dead;

	// Player number
	private final int PLAYER_ONE = 0;
	private final int PLAYER_TWO = 1;

	// Knife animations
	private Animation knifeIdleAnimation;
	private Animation knifeMeleeAnimation;
	private Animation knifeMoveAnimation;

	// Pistol animations
	private Animation pistolIdleAnimation;
	private Animation pistolShootAnimation;
	private Animation pistolMoveAnimation;

	// Rifle animations
	private Animation rifleIdleAnimation;
	private Animation rifleShootAnimation;
	private Animation rifleMoveAnimation;

	// Shotgun animations
	private Animation shotgunIdleAnimation;
	private Animation shotgunShootAnimation;
	private Animation shotgunMoveAnimation;

	// Actions
	private boolean attacking;
	private boolean attackFinished;
	private boolean movingUp;
	private boolean movingDown;
	private boolean movingRight;
	private boolean movingLeft;

	// Weapons
	public Weapon[] weapons;
	private int numWeapons;

	private int weaponIndex;
	public static final int KNIFE = 0;
	public static final int PISTOL = 1;
	public static final int RIFLE = 2;
	public static final int SHOTGUN = 3;

	// Bullets
	private int bulletX;
	private int bulletY;

	// Direction
	private Direction staticDirection;
	private Direction currentDirection;

	// SFX
	private String shotFXPath;

	// Game State
	private GameState gamestate;

	/**
	 * Constructor
	 * @param playerNum The player's number
	 * @param x The x coordinate of the top left corner
	 * @param y The y coordinate of the top left corner
	 * @param width The width of the player's hit box
	 * @param height The height of the player's hit box
	 * @param imageWidth The width of the player's image
	 * @param imageHeight The height of the player's image
	 * @param map The collision map
	 */
	public Player(int playerNum, int x, int y, int width, int height, int imageWidth, int imageHeight, CollisionMap map, GameState gamestate)
	{
		super(playerNum, x, y, width, height, imageWidth, imageHeight, map, ObjectType.PLAYER);

		name = "Player " + (playerNum + 1);
		health = 100;
		dead = false;

		up = true;

		// Animations
		// Knife
		knifeIdleAnimation = new Animation(5, Asset.knifeIdle);
		knifeMeleeAnimation = new Animation(1, Asset.knifeMelee);
		knifeMoveAnimation = new Animation(3, Asset.knifeMove);

		// Pistol
		pistolIdleAnimation = new Animation(5, Asset.pistolIdle);
		pistolShootAnimation = new Animation(5, 5, Asset.pistolShoot);
		pistolMoveAnimation = new Animation(5, Asset.pistolMove);

		// Rifle
		rifleIdleAnimation = new Animation(5, Asset.rifleIdle);
		rifleShootAnimation = new Animation(5, Asset.rifleShoot);
		rifleMoveAnimation = new Animation(5, Asset.rifleMove);

		// Shotgun
		shotgunIdleAnimation = new Animation(5, Asset.shotgunIdle);
		shotgunShootAnimation = new Animation(5, 20, Asset.shotgunShoot);
		shotgunMoveAnimation = new Animation(5, Asset.shotgunMove);

		animation = knifeIdleAnimation;

		// Weapons
		numWeapons = 4;
		weapons = new Weapon[numWeapons];
		weapons[KNIFE] = new Weapon("Knife", 75, 60.0 / knifeMeleeAnimation.getDelay(), 1, 1, true, 0, this);
		weapons[PISTOL] = new Weapon("Pistol", 100, 60.0 / pistolShootAnimation.getDelay(), 200, 200, false, 150, this);
		weapons[RIFLE] = new Weapon("Rifle", 200, 60.0 / rifleShootAnimation.getDelay(), 100, 100, false, 1500, this);
		weapons[SHOTGUN] = new Weapon("Shotgun", 400, 60.0 / shotgunShootAnimation.getDelay(), 50, 50, false, 1500, this);

		damage = weapons[KNIFE].getDamage();

		weaponIndex = KNIFE;

		// SFX
		shotFXPath = "/Audio/SFX/Pistol Shot.mp3";

		// Direction
		staticDirection = Direction.RIGHT;
		currentDirection = Direction.RIGHT;

		// Game State
		this.gamestate = gamestate;

		map.addPlayer(this);

	}

	/**
	 * Move the player while checking for collision
	 */
	private void move()
	{
		if (movingUp)
		{
			up = true;
			down = false;
			staticDirection = Direction.UP;
			y -= 2;
			checkCollision();

			if (collisionObject == ObjectType.WALL || collisionObject == ObjectType.ZOMBIE || collisionObject == ObjectType.PLAYER
					|| collisionObject == ObjectType.KNIFE)
			{
				y += 2;
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
			staticDirection = Direction.DOWN;
			y += 2;
			checkCollision();

			if (collisionObject == ObjectType.WALL || collisionObject == ObjectType.ZOMBIE || collisionObject == ObjectType.PLAYER
					|| collisionObject == ObjectType.KNIFE)
			{
				y -= 2;
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
			x -= 2;
			left = true;
			right = false;
			staticDirection = Direction.LEFT;
			checkCollision();

			if (collisionObject == ObjectType.WALL || collisionObject == ObjectType.ZOMBIE || collisionObject == ObjectType.PLAYER
					|| collisionObject == ObjectType.KNIFE)
			{
				x += 2;
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
			staticDirection = Direction.RIGHT;
			x += 2;
			checkCollision();

			if (collisionObject == ObjectType.WALL || collisionObject == ObjectType.ZOMBIE || collisionObject == ObjectType.PLAYER
					|| collisionObject == ObjectType.KNIFE)
			{
				x -= 2;
				right = false;
			}
		}
		else if (!movingLeft && !movingRight)
		{
			left = false;
			right = false;
		}

		if (!movingUp && !movingDown && !movingLeft && !movingRight)
		{
			up = false;
			down = false;
			left = false;
			right = false;

			if (staticDirection == Direction.UP)
			{
				up = true;
			}
			else if (staticDirection == Direction.DOWN)
			{
				down = true;
			}
			else if (staticDirection == Direction.LEFT)
			{
				left = true;
			}
			else
			{
				right = true;
			}
		}
	}

	/**
	 * Shoot the players current weapon if the current weapon isn't a knife
	 */
	private void shoot()
	{
		if (attacking && weaponIndex != KNIFE && animation.getFrame() == 1 && animation.active() && weapons[weaponIndex].getAmmo() > 0)
		{
			setBulletPosition();
			weapons[weaponIndex].shoot();
			gamestate.addBullet(new Bullet(bulletX, bulletY, currentDirection, map, damage));
			new Audio(shotFXPath).play();
		}
	}

	/**
	 * Makes sure that the player can not stand in a diagonal direction but can
	 * move in a diagonal direction. Being able to stand in a diagonal direction
	 * makes the game too easy
	 */
	private void checkStaticDirection()
	{
		if (up)
		{
			if (left)
			{
				currentDirection = Direction.UP_LEFT;
			}
			else if (right)
			{
				currentDirection = Direction.UP_RIGHT;
			}
			else
			{
				currentDirection = Direction.UP;
			}
		}
		else if (down)
		{
			if (left)
			{
				currentDirection = Direction.DOWN_LEFT;
			}
			else if (right)
			{
				currentDirection = Direction.DOWN_RIGHT;
			}
			else
			{
				currentDirection = Direction.DOWN;
			}
		}
		else
		{
			if (left)
			{
				currentDirection = Direction.LEFT;
			}
			else
			{
				currentDirection = Direction.RIGHT;
			}
		}
	}

	/**
	 * Check which animation should be playing
	 */
	private void checkAnimation()
	{
		if (weaponIndex == KNIFE)
		{
			if (attacking)
			{
				animation = knifeMeleeAnimation;
				return;
			}

			if (movingUp || movingDown || movingLeft || movingRight)
			{
				animation = knifeMoveAnimation;
			}
			else
			{
				animation = knifeIdleAnimation;
			}
		}
		else if (weaponIndex == PISTOL)
		{
			if (attacking)
			{
				animation = pistolShootAnimation;
				return;
			}

			if (movingUp || movingDown || movingLeft || movingRight)
			{
				animation = pistolMoveAnimation;
			}
			else
			{
				animation = pistolIdleAnimation;
			}
		}
		else if (weaponIndex == RIFLE)
		{
			if (attacking)
			{
				animation = rifleShootAnimation;
				return;
			}

			if (movingUp || movingDown || movingLeft || movingRight)
			{
				animation = rifleMoveAnimation;
			}
			else
			{
				animation = rifleIdleAnimation;
			}
		}
		else if (weaponIndex == SHOTGUN)
		{
			if (attacking)
			{
				animation = shotgunShootAnimation;
				return;
			}

			if (movingUp || movingDown || movingLeft || movingRight)
			{
				animation = shotgunMoveAnimation;
			}
			else
			{
				animation = shotgunIdleAnimation;
			}
		}
	}

	/**
	 * Check collision
	 */
	public void checkCollision()
	{
		collisionObject = map.checkEntityCollision(this, CollisionMap.PLAYER);
	}

	/**
	 * Set the position from which the bullet comes out of the gun
	 */
	private void setBulletPosition()
	{
		if (currentDirection == Direction.RIGHT)
		{
			bulletX = x + 70;

			if (weaponIndex == PISTOL)
			{
				bulletY = y + 64;
			}
			else
			{
				bulletY = y + 60;
			}
		}
		else if (currentDirection == Direction.LEFT)
		{
			bulletX = x + 28;

			if (weaponIndex == PISTOL)
			{
				bulletY = y + 64;
			}
			else
			{
				bulletY = y + 60;
			}
		}
		else if (currentDirection == Direction.UP)
		{
			if (weaponIndex == PISTOL)
			{
				bulletX = x + 64;
			}
			else
			{
				bulletX = x + 62;
			}

			bulletY = y + 20;
		}
		else if (currentDirection == Direction.DOWN)
		{
			if (weaponIndex == PISTOL)
			{
				bulletX = x + 33;
			}
			else
			{
				bulletX = x + 37;
			}

			bulletY = y + 65;
		}
		else if (currentDirection == Direction.UP_LEFT)
		{
			bulletX = x + 5;
			bulletY = y + 30;
		}
		else if (currentDirection == Direction.UP_RIGHT)
		{
			bulletX = x + 100;
			bulletY = y + 15;
		}
		else if (currentDirection == Direction.DOWN_LEFT)
		{
			bulletX = x + 54;
			bulletY = y + 64;
		}
		else if (currentDirection == Direction.DOWN_RIGHT)
		{
			bulletX = x + 45;
			bulletY = y + 70;
		}
	}

	/**
	 * Take damage
	 * @param damage The amount of damage to be taken
	 */
	public void hit(int damage)
	{
		health -= damage;
		if (health < 0)
		{
			health = 0;
		}
	}

	/**
	 * Fills all of the player's currently held weapons ammo
	 */
	public void fillAmmo()
	{
		for (int i = 0; i < weapons.length; i++)
		{
			weapons[i].fillAmmo();
		}
	}

	/**
	 * Increases the player's health by 50
	 */
	public void increaseHealth()
	{
		health += 50;

		if (health > 100)
		{
			health = 100;
		}
	}

	/**
	 * Returns the player's health
	 * @return The player's health
	 */
	public int getHealth()
	{
		return health;
	}

	/**
	 * Return the player's name (Player 1 or Player 2)
	 * @return the player's name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Returns the player's current weapon
	 * @return The player's current weapon
	 */
	public Weapon getCurrentWeapon()
	{
		return weapons[weaponIndex];
	}

	/**
	 * Returns whether or not the player is knifing
	 * @return Whether or not the player is knifing
	 */
	public boolean isKnifing()
	{
		return weaponIndex == KNIFE && animation.getFrame() == 7 && animation.active() && animation.equals(knifeMeleeAnimation);

	}

	/**
	 * Returns whether or not the player is dead
	 * @return Whether or not the player is dead
	 */
	public boolean isDead()
	{
		return dead;
	}

	/**
	 * Respawns the player
	 */
	public void respawn()
	{
		health = 50;
		dead = false;

		if (collisionObject != ObjectType.EMPTY)
			;
	}

	/**
	 * Upgrade a weapons fire rate
	 * @param weapon The weapon to upgrade
	 * @param upgradeIndex The upgrade number (To know how much to increase the
	 *            fire rate by)
	 */
	public void increaseWeaponSpeed(int weapon, int upgradeIndex)
	{
		if (weapon == PISTOL)
		{
			if (upgradeIndex == 2)
			{
				pistolShootAnimation.setActionDelay(2);
			}
			else if (upgradeIndex == 5)
			{
				pistolShootAnimation.setActionDelay(0);
			}
			else if (upgradeIndex == 8)
			{
				pistolShootAnimation.decreaseFrameDelay();
			}

			weapons[PISTOL].setSpeed(60.0 / pistolShootAnimation.getDelay());
		}
		else if (weapon == RIFLE)
		{
			if (upgradeIndex % 3 == 2 && upgradeIndex < 9)
			{
				rifleShootAnimation.decreaseFrameDelay();
			}

			weapons[RIFLE].setSpeed(60.0 / rifleShootAnimation.getDelay());
		}
		else if (weapon == SHOTGUN)
		{
			if (upgradeIndex == 2)
			{
				shotgunShootAnimation.setActionDelay(10);
			}
			else if (upgradeIndex == 5)
			{
				shotgunShootAnimation.setActionDelay(5);
			}
			else if (upgradeIndex == 8)
			{
				shotgunShootAnimation.setActionDelay(2);
			}

			weapons[SHOTGUN].setSpeed(60.0 / shotgunShootAnimation.getDelay());
		}
	}

	/**
	 * Updates the players movement, direction, animation and health
	 */
	public void update()
	{
		if (!dead)
		{
			type = ObjectType.PLAYER;

			move();
			checkStaticDirection();

			checkAnimation();
			if (attacking && attackFinished && animation.isFinished())
			{
				attacking = false;
			}

			if (weapons[weaponIndex].getAmmo() <= 0 && weaponIndex != KNIFE)
			{
				attacking = false;
			}

			animation.update();

			shoot();

			if (health <= 0)
			{
				dead = true;
			}
		}
	}

	/**
	 * Draws the player in it's current animation and direction, if it isn't
	 * dead
	 */
	public void draw(Graphics2D g)
	{
		if (!dead)
		{
			if (num == PLAYER_ONE)
			{
				g.drawImage(Asset.redRing, x, y, null);
			}
			else if (num == PLAYER_TWO)
			{
				g.drawImage(Asset.blueRing, x, y, null);
			}

			rotate();
			g.rotate(rotation, x + (imageWidth / 2), y + (imageHeight / 2));

			if (reflect)
			{
				g.drawImage(animation.getImage(), x + imageWidth, y, -imageWidth, imageHeight, null);
			}
			else
			{
				g.drawImage(animation.getImage(), x, y, null);
			}

			reflect = false;

			g.setTransform(reset);
		}

		// g.drawRect((int) getHitBox().getMinX(), (int) getHitBox().getMinY(),
		// (int) getHitBox().getWidth(), (int) getHitBox().getHeight());
	}

	/**
	 * Checks for key pressed events
	 * @param k The key code of the key being pressed
	 */
	public void keyPressed(int k)
	{
		if (num == PLAYER_ONE)
		{
			if (k == KeyEvent.VK_UP)
			{
				movingUp = true;
				movingDown = false;
			}
			if (k == KeyEvent.VK_DOWN)
			{
				movingDown = true;
				movingUp = false;
			}
			if (k == KeyEvent.VK_LEFT)
			{
				movingLeft = true;
				movingRight = false;
			}
			if (k == KeyEvent.VK_RIGHT)
			{
				movingRight = true;
				movingLeft = false;
			}
			if (k == KeyEvent.VK_SLASH)
			{
				if (weapons[weaponIndex].getAmmo() > 0)
				{
					attacking = true;
					attackFinished = false;
				}
			}
		}
		else if (num == PLAYER_TWO)
		{
			if (k == KeyEvent.VK_W)
			{
				movingUp = true;
				movingDown = false;
			}
			if (k == KeyEvent.VK_S)
			{
				movingDown = true;
				movingUp = false;
			}
			if (k == KeyEvent.VK_A)
			{
				movingLeft = true;
				movingRight = false;
			}
			if (k == KeyEvent.VK_D)
			{
				movingRight = true;
				movingLeft = false;
			}
			if (k == KeyEvent.VK_SPACE)
			{
				if (weapons[weaponIndex].getAmmo() > 0)
				{
					attacking = true;
					attackFinished = false;
				}
			}
		}

	}

	/**
	 * Checks for key released events
	 * @param k The key code of the key being released
	 */
	public void keyReleased(int k)
	{
		if (num == PLAYER_ONE)
		{
			if (k == KeyEvent.VK_UP)
			{
				movingUp = false;
			}
			if (k == KeyEvent.VK_DOWN)
			{
				movingDown = false;
			}
			if (k == KeyEvent.VK_LEFT)
			{
				movingLeft = false;
			}
			if (k == KeyEvent.VK_RIGHT)
			{
				movingRight = false;
			}
			if (k == KeyEvent.VK_SLASH)
			{
				attackFinished = true;
			}
			if (k == KeyEvent.VK_COMMA)
			{
				weaponIndex--;
				if (weaponIndex < KNIFE)
				{
					weaponIndex = SHOTGUN;
				}

				while (!weapons[weaponIndex].isUnlocked())
				{
					weaponIndex--;
					if (weaponIndex < KNIFE)
					{
						weaponIndex = SHOTGUN;
					}
				}

				damage = weapons[weaponIndex].getDamage();
			}
			if (k == KeyEvent.VK_PERIOD)
			{
				weaponIndex++;
				if (weaponIndex > SHOTGUN)
				{
					weaponIndex = KNIFE;
				}

				while (!weapons[weaponIndex].isUnlocked())
				{
					weaponIndex++;
					if (weaponIndex > SHOTGUN)
					{
						weaponIndex = KNIFE;
					}
				}

				damage = weapons[weaponIndex].getDamage();
			}
		}
		else if (num == PLAYER_TWO)
		{
			if (k == KeyEvent.VK_W)
			{
				movingUp = false;
			}
			if (k == KeyEvent.VK_S)
			{
				movingDown = false;
			}
			if (k == KeyEvent.VK_A)
			{
				movingLeft = false;
			}
			if (k == KeyEvent.VK_D)
			{
				movingRight = false;
			}
			if (k == KeyEvent.VK_SPACE)
			{
				attackFinished = true;
			}
			if (k == KeyEvent.VK_Q)
			{
				weaponIndex--;
				if (weaponIndex < KNIFE)
				{
					weaponIndex = SHOTGUN;
				}

				while (!weapons[weaponIndex].isUnlocked())
				{
					weaponIndex--;
					if (weaponIndex < KNIFE)
					{
						weaponIndex = SHOTGUN;
					}
				}

				damage = weapons[weaponIndex].getDamage();
			}
			if (k == KeyEvent.VK_E)
			{
				weaponIndex++;
				if (weaponIndex > SHOTGUN)
				{
					weaponIndex = KNIFE;
				}

				while (!weapons[weaponIndex].isUnlocked())
				{
					weaponIndex++;
					if (weaponIndex > SHOTGUN)
					{
						weaponIndex = KNIFE;
					}
				}

				damage = weapons[weaponIndex].getDamage();
			}
		}
	}

}
