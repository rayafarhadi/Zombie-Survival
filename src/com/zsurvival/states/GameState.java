package com.zsurvival.states;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import com.zsurvival.assets.Asset;
import com.zsurvival.assets.CollisionMap;
import com.zsurvival.main.GamePanel;
import com.zsurvival.objects.Bullet;
import com.zsurvival.objects.Crate;
import com.zsurvival.objects.HUD;
import com.zsurvival.objects.ObjectType;
import com.zsurvival.objects.Weapon;
import com.zsurvival.objects.entities.Player;
import com.zsurvival.objects.entities.Zombie;

public class GameState implements State
{
	// Map
	private BufferedImage mapImage;
	private CollisionMap map;

	// Players
	private Player[] players;
	private int numPlayers;

	// Weapon
	private Weapon tempWeapon;

	// Zombies
	private ArrayList<Zombie> zombies;
	private Zombie tempZombie;
	private int totalZombies;
	private int zombiesSpawned;
	private int zombiesOnScreen;
	private int zombieHealth;
	private ObjectType spawnCollisionObject;
	private Rectangle spawnBox;
	private Random random;
	private int numSpawns;
	private int spawnX;
	private int spawnY;

	private final int MAX_ZOMBIES = 20;

	// Bullets
	private ArrayList<Bullet> bullets;
	private Bullet tempBullet;

	// Crates
	private Crate tempCrate;

	// HUD
	private HUD hud;
	private int scoreMultiplier;
	private int damageMultiplier;
	private int totalScore;
	private int score;
	private int wave;

	// Game over
	private boolean gameOver;

	// Screens
	private int currentScreen;

	private BufferedImage[] pauseOptions;
	private BufferedImage[] playerOptions;
	private BufferedImage[] mapOptions;
	private BufferedImage[] upgradeOptions;

	// Options
	private int currentOption;

	private final int PLAYER_SELECT_SCREEN = 0;
	private final int MAP_SELECT_SCREEN = 1;
	private final int PAUSE_SCREEN = 2;
	private final int UPGRADES_SCREEN = 3;
	private final int GAME_SCREEN = 4;

	private final int RESUME = 0;
	private final int UPGRADES = 1;
	private final int BACK_TO_MAIN_MENU = 2;
	private final int NUM_PAUSE_OPTIONS = 3;

	private final int BACK = 0;
	private final int KNIFE_UPGRADE = 1;
	private final int PISTOL_UPGRADE = 2;
	private final int RIFLE_UPGRADE = 3;
	private final int SHOTGUN_UPGRADE = 4;
	private final int NUM_UPGRADE_OPTIONS = 5;

	private final int ONE_PLAYER = 0;
	private final int TWO_PLAYER = 1;
	private final int NUM_PLAYER_OPTIONS = 2;

	private final int MAP1 = 0;
	private final int MAP2 = 1;
	private final int MAP3 = 2;
	private final int MAP4 = 3;
	private final int NUM_MAP_OPTIONS = 4;

	// Delays
	private int keyDelay = 0;
	private int waveDelay = 600;
	private int doublePointTimer = 0;
	private int doubleDamageTimer = 0;
	private final int KEY_DELAY_TIME = 5;
	private final int WAVE_DELAY_TIME = 600;
	private final int POWERUP_TIME = 1800;

	private boolean enterReleased;
	public static boolean highscore;

	/**
	 * Constructor
	 */
	public GameState()
	{
		super();

		scoreMultiplier = 1;
		damageMultiplier = 1;
		wave = 1;

		totalZombies = 10;
		zombiesSpawned = 0;
		zombiesOnScreen = 0;
		zombieHealth = 75;

		spawnCollisionObject = ObjectType.EMPTY;
		random = new Random();

		currentScreen = PLAYER_SELECT_SCREEN;

		// Pause menu options
		pauseOptions = new BufferedImage[NUM_PAUSE_OPTIONS];
		pauseOptions[RESUME] = Asset.pauseResume;
		pauseOptions[UPGRADES] = Asset.pauseUpgrades;
		pauseOptions[BACK_TO_MAIN_MENU] = Asset.pauseBackToMainMenu;

		// Upgrades menu options
		upgradeOptions = new BufferedImage[NUM_UPGRADE_OPTIONS];
		upgradeOptions[BACK] = Asset.upgradesBackSelect;
		upgradeOptions[KNIFE_UPGRADE] = Asset.upgradeKnife;
		upgradeOptions[PISTOL_UPGRADE] = Asset.upgradePistol;
		upgradeOptions[RIFLE_UPGRADE] = Asset.upgradeRifle;
		upgradeOptions[SHOTGUN_UPGRADE] = Asset.upgradeShotgun;

		// Player select menu options
		playerOptions = new BufferedImage[NUM_PLAYER_OPTIONS];
		playerOptions[ONE_PLAYER] = Asset.playerSelectOne;
		playerOptions[TWO_PLAYER] = Asset.playerSelectTwo;

		// Map select menu options
		mapOptions = new BufferedImage[NUM_MAP_OPTIONS];
		mapOptions[MAP1] = Asset.mapSelectOne;
		mapOptions[MAP2] = Asset.mapSelectTwo;
		mapOptions[MAP3] = Asset.mapSelectThree;
		mapOptions[MAP4] = Asset.mapSelectFour;
	}

	/**
	 * Selects the current option of the current screen
	 */
	public void select()
	{
		if (currentScreen == PAUSE_SCREEN)
		{
			// Resumes the game
			if (currentOption == RESUME)
			{
				currentScreen = GAME_SCREEN;
			}
			// Goes to the upgrades menu
			else if (currentOption == UPGRADES)
			{
				currentScreen = UPGRADES_SCREEN;
				currentOption = 0;
			}
			// Ends the game and goes back to the main menu
			else if (currentOption == BACK_TO_MAIN_MENU)
			{
				GamePanel.states[GameStateManager.GAME_STATE] = new GameState();
				GameStateManager.setCurrentState(GameStateManager.MAIN_MENU_STATE);
			}
		}
		else if (currentScreen == UPGRADES_SCREEN)
		{
			// Upgrades the knife
			if (currentOption == KNIFE_UPGRADE)
			{
				tempWeapon = players[0].weapons[Player.KNIFE];
				if (!tempWeapon.isMaxed() && score >= tempWeapon.getNextPrice())
				{
					score -= tempWeapon.getNextPrice();
					for (int i = 0; i < players.length; i++)
					{
						tempWeapon = players[i].weapons[Player.KNIFE];
						tempWeapon.upgrade();
						hud.setScore(score);
					}
				}
			}
			// Upgrades the pistol
			else if (currentOption == PISTOL_UPGRADE)
			{
				tempWeapon = players[0].weapons[Player.PISTOL];
				if (!tempWeapon.isMaxed() && score >= tempWeapon.getNextPrice())
				{
					score -= tempWeapon.getNextPrice();
					for (int i = 0; i < players.length; i++)
					{
						tempWeapon = players[i].weapons[Player.PISTOL];
						if (tempWeapon.isUnlocked())
						{
							tempWeapon.upgrade();
						}
						else
						{
							tempWeapon.unlock();
						}

						hud.setScore(score);
					}
				}
			}
			// Upgrades the rifle
			else if (currentOption == RIFLE_UPGRADE)
			{
				tempWeapon = players[0].weapons[Player.RIFLE];
				if (!tempWeapon.isMaxed() && score >= tempWeapon.getNextPrice())
				{
					score -= tempWeapon.getNextPrice();
					for (int i = 0; i < players.length; i++)
					{
						tempWeapon = players[i].weapons[Player.RIFLE];
						if (tempWeapon.isUnlocked())
						{
							tempWeapon.upgrade();
						}
						else
						{
							tempWeapon.unlock();
						}

						hud.setScore(score);
					}
				}
			}
			// Upgrades the shotgun
			else if (currentOption == SHOTGUN_UPGRADE)
			{
				tempWeapon = players[0].weapons[Player.SHOTGUN];
				if (!tempWeapon.isMaxed() && score >= tempWeapon.getNextPrice())
				{
					score -= tempWeapon.getNextPrice();
					for (int i = 0; i < players.length; i++)
					{
						tempWeapon = players[i].weapons[Player.SHOTGUN];
						if (tempWeapon.isUnlocked())
						{
							tempWeapon.upgrade();
						}
						else
						{
							tempWeapon.unlock();
						}

						hud.setScore(score);
					}
				}
			}
			// Goes back to the pause screen
			else if (currentOption == BACK)
			{
				currentOption = 0;
				currentScreen = PAUSE_SCREEN;
			}
		}
		else if (currentScreen == PLAYER_SELECT_SCREEN)
		{
			if (currentOption == ONE_PLAYER)
			{
				numPlayers = 1;
			}
			else if (currentOption == TWO_PLAYER)
			{
				numPlayers = 2;
			}
			currentOption = 0;
			currentScreen = MAP_SELECT_SCREEN;
			players = new Player[numPlayers];
		}
		else if (currentScreen == MAP_SELECT_SCREEN)
		{
			map = Asset.maps[currentOption];
			map.clear();
			mapImage = map.getImage();
			numSpawns = map.getNumZombieSpawns();

			for (int i = 0; i < map.getCrates().size(); i++)
			{
				map.getCrates().get(i).addGameState(this);
			}

			for (int i = 0; i < numPlayers; i++)
			{
				players[i] = new Player(i, map.getStartingX(i), map.getStartingY(i), 50, 50, 100, 100, map, this);
			}

			zombies = new ArrayList<Zombie>();
			bullets = new ArrayList<Bullet>();

			hud = new HUD(players);
			currentOption = 0;
			currentScreen = GAME_SCREEN;
		}
	}

	/**
	 * Advances the wave, increases number of zombies and the zombies health and
	 * spawns new crates
	 */
	public void nextWave()
	{
		wave++;

		// Increases number of zombies and zombie health
		totalZombies = 5 + (wave * 5);
		zombiesSpawned = 0;
		zombieHealth += 25;
		if (numPlayers > 1)
		{
			totalZombies *= 1.5;
		}

		// Respawns dead players
		for (int i = 0; i < players.length; i++)
		{
			if (players[i].isDead())
			{
				players[i].respawn();
			}
		}

		hud.nextWave();
		hud.displayWave(true);

		// Spawns crates
		for (int i = 0; i < map.getCrateLocations().length; i++)
		{
			if (!map.getCrates().get(i).active)
			{
				map.getCrates().get(i).randomizeType();
				map.getCrates().get(i).active = true;
			}
		}

		waveDelay = WAVE_DELAY_TIME;
	}

	/**
	 * Adds a bullet
	 * @param bullet The bullet to be added
	 */
	public void addBullet(Bullet bullet)
	{
		bullets.add(bullet);
	}

	/**
	 * Doubles the points gained from killing zombies for a limited time
	 */
	public void doublePoints()
	{
		doublePointTimer = POWERUP_TIME;
		scoreMultiplier = 2;
		hud.pointsActive(true);
	}

	/**
	 * Doubles the damage caused by weapons for a limited time
	 */
	public void doubleDamage()
	{
		doubleDamageTimer = POWERUP_TIME;
		damageMultiplier = 2;
		hud.damageActive(true);
	}

	/**
	 * Kills all zombies on the screen
	 */
	public void nuke()
	{
		for (int i = 0; i < zombies.size(); i++)
		{
			zombies.get(i).kill();
		}
	}

	/**
	 * Spawns a zombie, if there aren't too many zombies on the screen and if
	 * the number of zombies hasn't reached the total for the wave
	 */
	private void spawnZombie()
	{
		if (zombiesSpawned < totalZombies && zombiesOnScreen < MAX_ZOMBIES)
		{
			spawnBox = map.getSpawnBox(random.nextInt(numSpawns));

			if (spawnBox.getCenterX() < 950)
			{
				spawnX = (int) spawnBox.getCenterX();
			}
			else
			{
				spawnX = (int) spawnBox.getCenterX() - 50;
			}

			spawnY = (int) spawnBox.getCenterY() - 25;

			for (int i = 0; i < zombiesSpawned; i++)
			{
				spawnCollisionObject = map.checkSpawnCollision(spawnBox);
			}

			if (spawnCollisionObject == ObjectType.EMPTY)
			{
				zombies.add(new Zombie(zombiesSpawned, zombieHealth, spawnX, spawnY, 50, 50, 100, 100, map, players));
				zombiesSpawned++;
				zombiesOnScreen++;
			}
		}
	}

	/**
	 * Updates the game. Checks for player death, zombie death, wave end and
	 * game end
	 */
	public void update()
	{
		if (currentScreen == GAME_SCREEN && !gameOver)
		{
			gameOver = true;

			// If all of the zombies in the wave are dead start the next wave
			if (zombiesSpawned >= totalZombies && zombies.size() <= 0)
			{
				nextWave();
			}

			// Update players
			for (int i = 0; i < players.length; i++)
			{
				players[i].update();
				if (players[i].getHealth() > 0)
				{
					gameOver = false;
				}
			}

			// Fade the wave display in then out
			if (waveDelay > 400)
			{
				if (hud.isAlphaDown())
				{
					hud.setAlphaDown(false);
				}

				waveDelay--;
			}
			else if (waveDelay <= 400 && waveDelay > 200)
			{
				waveDelay--;
			}
			else if (waveDelay <= 200 && waveDelay > 0)
			{
				if (!hud.isAlphaDown())
				{
					hud.setAlphaDown(true);
				}

				waveDelay--;
			}
			else if (waveDelay <= 0)
			{
				if (hud.isDisplayingWave())
				{
					hud.displayWave(false);
				}

				// After the break between waves is over start spawning zombies
				spawnZombie();
			}

			// Update zombies
			for (int i = 0; i < zombies.size(); i++)
			{
				tempZombie = zombies.get(i);
				tempZombie.update();
				if (tempZombie.isDead())
				{
					if (random.nextInt(20) == 0)
					{
						tempCrate = new Crate(random.nextInt(4) + 1, tempZombie.getX(), tempZombie.getY(), map);
						tempCrate.addGameState(this);
						map.addCrate(tempCrate);
					}
					map.removeZombie(tempZombie);
					zombies.remove(i);
					zombiesOnScreen--;
					score += ((wave * 5) + totalZombies) * scoreMultiplier;
					totalScore += ((wave * 5) + totalZombies) * scoreMultiplier;
					hud.setScore(score);
				}
			}

			// Update bullets
			for (int i = 0; i < bullets.size(); i++)
			{
				tempBullet = bullets.get(i);
				tempBullet.update();
				if (tempBullet.collided())
				{
					if (tempBullet.isHittingZombie())
					{
						tempZombie = map.checkBulletCollision(tempBullet);
						tempZombie.hit(tempBullet.getDamage() * damageMultiplier);
					}
					bullets.remove(i);
				}
			}

			// Deactivate power ups 30 seconds after activation
			if (doublePointTimer > 0)
			{
				doublePointTimer--;
			}
			else
			{
				scoreMultiplier = 1;
				hud.pointsActive(false);
			}

			if (doubleDamageTimer > 0)
			{
				doubleDamageTimer--;
			}
			else
			{
				damageMultiplier = 1;
				hud.damageActive(false);
			}

			// Update crates
			for (int i = 0; i < map.getCrates().size(); i++)
			{
				map.getCrates().get(i).update();
			}
		}
		// Check if the game is over
		else if (currentScreen == GAME_SCREEN && gameOver)
		{
			GamePanel.states[GameStateManager.HIGHSCORE_STATE].update();
			highscore = totalScore > HighscoreState.highScores[HighscoreState.highScores.length - 1];

			hud.setScore(totalScore);
			gameOver = false;
			GameStateManager.setCurrentState(GameStateManager.GAME_OVER_STATE);
		}
	}

	/**
	 * Draws the map, the crates, the players, the zombies, the bullets and the
	 * HUD while on the game screen. Draws the current option for the pause menu
	 * if on the pause screen. Draws the current option for the upgrades menu
	 * and the current weapon stats if on the upgrades screen.
	 */
	public void draw(Graphics2D g)
	{
		if (currentScreen == GAME_SCREEN || currentScreen == PAUSE_SCREEN || currentScreen == UPGRADES_SCREEN)
		{
			g.drawImage(mapImage, 0, 0, null);

			// Draw the crates
			for (int i = 0; i < map.getCrates().size(); i++)
			{
				map.getCrates().get(i).draw(g);
			}

			// Draw the players
			for (int i = 0; i < players.length; i++)
			{
				players[i].draw(g);
			}

			// Draw the zombies
			for (int i = 0; i < zombies.size(); i++)
			{
				zombies.get(i).draw(g);
			}

			// Draw the bullets
			for (int i = 0; i < bullets.size(); i++)
			{
				bullets.get(i).draw(g);
			}

			// Draw the HUD
			hud.draw(g);
		}
		// Draw the pause screen
		if (currentScreen == PAUSE_SCREEN)
		{
			g.drawImage(pauseOptions[currentOption], 0, 0, null);
		}
		// Draw the upgrades screen
		else if (currentScreen == UPGRADES_SCREEN)
		{
			g.drawImage(upgradeOptions[currentOption], 0, 0, null);

			g.setFont(Asset.weaponFont);

			g.drawString(String.format("$%,d", score), 575, 690);

			g.setFont(Asset.healthFont);

			// Knife stats
			tempWeapon = players[0].weapons[Player.KNIFE];

			g.drawString(tempWeapon.getDamage() + "", 215, 240);

			if (!tempWeapon.isMaxed())
			{
				g.drawString("$" + tempWeapon.getNextPrice(), 250, 150);
			}
			else
			{
				g.drawString("Max", 250, 150);
			}

			// Pistol stats
			tempWeapon = players[0].weapons[Player.PISTOL];

			g.drawString(tempWeapon.getDamage() + "", 765, 200);
			g.drawString(tempWeapon.getSpeed() + "", 765, 240);
			g.drawString(tempWeapon.getMaxAmmo() + "", 765, 280);

			if (!tempWeapon.isMaxed())
			{
				g.drawString("$" + tempWeapon.getNextPrice(), 800, 150);
			}
			else
			{
				g.drawString("Max", 800, 150);
			}

			if (!tempWeapon.isUnlocked())
			{
				g.drawImage(Asset.lock, 650, 100, null);
			}

			// Rifle stats
			tempWeapon = players[0].weapons[Player.RIFLE];

			g.drawString(tempWeapon.getDamage() + "", 215, 460);
			g.drawString(String.format("%2.1f", tempWeapon.getSpeed()), 215, 500);
			g.drawString(tempWeapon.getMaxAmmo() + "", 215, 540);

			if (!tempWeapon.isMaxed())
			{
				g.drawString("$" + tempWeapon.getNextPrice(), 235, 410);
			}
			else
			{
				g.drawString("Max", 235, 410);
			}

			if (!tempWeapon.isUnlocked())
			{
				g.drawImage(Asset.lock, 100, 350, null);
			}

			// Shotgun stats
			tempWeapon = players[0].weapons[Player.SHOTGUN];

			g.drawString(tempWeapon.getDamage() + "", 765, 455);
			g.drawString(tempWeapon.getSpeed() + "", 765, 495);
			g.drawString(tempWeapon.getMaxAmmo() + "", 765, 535);

			if (!players[0].weapons[Player.SHOTGUN].isMaxed())
			{
				g.drawString("$" + tempWeapon.getNextPrice(), 800, 405);
			}
			else
			{
				g.drawString("Max", 800, 405);
			}

			if (!tempWeapon.isUnlocked())
			{
				g.drawImage(Asset.lock, 650, 345, null);
			}
		}
		// Draw the player select screen
		else if (currentScreen == PLAYER_SELECT_SCREEN)
		{
			g.drawImage(playerOptions[currentOption], 0, 0, null);
		}
		// Draw the map select screen
		else if (currentScreen == MAP_SELECT_SCREEN)
		{
			g.drawImage(mapOptions[currentOption], 0, 0, null);
		}
	}

	/**
	 * Handles key pressed events. Handles them differently depending on which
	 * screen you're on.
	 */
	public void keyPressed(int k)
	{
		if (currentScreen == GAME_SCREEN)
		{
			for (int i = 0; i < numPlayers; i++)
			{
				players[i].keyPressed(k);
			}
			if (k == KeyEvent.VK_P || k == KeyEvent.VK_ESCAPE)
			{
				currentScreen = PAUSE_SCREEN;
				enterReleased = true;
			}
		}
		else if (currentScreen == PAUSE_SCREEN)
		{
			if (k == KeyEvent.VK_ENTER || k == KeyEvent.VK_SPACE || k == KeyEvent.VK_SLASH)
			{
				if (enterReleased)
				{
					select();
					enterReleased = false;
				}
				else
				{
					enterReleased = true;
				}
			}
			if (k == KeyEvent.VK_P || k == KeyEvent.VK_ESCAPE)
			{
				currentOption = RESUME;
				select();
			}
			if (keyDelay <= 1)
			{
				if (k == KeyEvent.VK_UP || k == KeyEvent.VK_W)
				{
					currentOption--;
					if (currentOption < 0)
					{
						currentOption = pauseOptions.length - 1;
					}
					keyDelay += KEY_DELAY_TIME;
				}
				if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S)
				{
					currentOption++;
					if (currentOption >= pauseOptions.length)
					{
						currentOption = 0;
					}
					keyDelay += KEY_DELAY_TIME;
				}
			}
			else
			{
				keyDelay--;
			}
		}
		else if (currentScreen == UPGRADES_SCREEN)
		{
			if (k == KeyEvent.VK_ENTER || k == KeyEvent.VK_SPACE || k == KeyEvent.VK_SLASH)
			{
				if (enterReleased)
				{
					select();
					enterReleased = false;
				}
				else
				{
					enterReleased = true;
				}
			}
			if (keyDelay <= 1)
			{
				if (k == KeyEvent.VK_UP || k == KeyEvent.VK_W)
				{
					if (currentOption == KNIFE_UPGRADE)
					{
						currentOption = BACK;
					}
					else if (currentOption == PISTOL_UPGRADE)
					{
						currentOption = BACK;
					}
					else if (currentOption == RIFLE_UPGRADE)
					{
						currentOption = KNIFE_UPGRADE;
					}
					else if (currentOption == SHOTGUN_UPGRADE)
					{
						currentOption = PISTOL_UPGRADE;
					}
					else if (currentOption == BACK)
					{
						currentOption = RIFLE_UPGRADE;
					}

					keyDelay += KEY_DELAY_TIME;
				}
				if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S)
				{
					if (currentOption == KNIFE_UPGRADE)
					{
						currentOption = RIFLE_UPGRADE;
					}
					else if (currentOption == PISTOL_UPGRADE)
					{
						currentOption = SHOTGUN_UPGRADE;
					}
					else if (currentOption == RIFLE_UPGRADE)
					{
						currentOption = BACK;
					}
					else if (currentOption == SHOTGUN_UPGRADE)
					{
						currentOption = BACK;
					}
					else if (currentOption == BACK)
					{
						currentOption = KNIFE_UPGRADE;
					}

					keyDelay += KEY_DELAY_TIME;
				}
				if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_A || k == KeyEvent.VK_D)
				{
					if (currentOption == KNIFE_UPGRADE)
					{
						currentOption = PISTOL_UPGRADE;
					}
					else if (currentOption == PISTOL_UPGRADE)
					{
						currentOption = KNIFE_UPGRADE;
					}
					else if (currentOption == RIFLE_UPGRADE)
					{
						currentOption = SHOTGUN_UPGRADE;
					}
					else if (currentOption == SHOTGUN_UPGRADE)
					{
						currentOption = RIFLE_UPGRADE;
					}

					keyDelay += KEY_DELAY_TIME;
				}
			}
			else
			{
				keyDelay--;
			}
		}
		else if (currentScreen == PLAYER_SELECT_SCREEN)
		{
			if (k == KeyEvent.VK_ENTER || k == KeyEvent.VK_SPACE || k == KeyEvent.VK_SLASH)
			{
				select();
			}
			if (keyDelay <= 1)
			{
				if (k == KeyEvent.VK_UP || k == KeyEvent.VK_W)
				{
					currentOption--;
					if (currentOption < 0)
					{
						currentOption = playerOptions.length - 1;
					}
					keyDelay += KEY_DELAY_TIME;
				}
				if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S)
				{
					currentOption++;
					if (currentOption >= playerOptions.length)
					{
						currentOption = 0;
					}
					keyDelay += KEY_DELAY_TIME;
				}
			}
			else
			{
				keyDelay--;
			}
		}
		else if (currentScreen == MAP_SELECT_SCREEN)
		{
			if (k == KeyEvent.VK_ENTER || k == KeyEvent.VK_SPACE || k == KeyEvent.VK_SLASH)
			{
				select();
			}
			if (keyDelay <= 1)
			{
				if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_A)
				{
					currentOption--;
					if (currentOption < 0)
					{
						currentOption = mapOptions.length - 1;
					}
					keyDelay += KEY_DELAY_TIME;
				}
				if (k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D)
				{
					currentOption++;
					if (currentOption >= mapOptions.length)
					{
						currentOption = 0;
					}
					keyDelay += KEY_DELAY_TIME;
				}
			}
			else
			{
				keyDelay--;
			}
		}
	}

	/**
	 * Handles key released events. Handles them differently depending on which
	 * screen you're on.
	 */
	public void keyReleased(int k)
	{
		if (currentScreen == GAME_SCREEN)
		{
			for (int i = 0; i < players.length; i++)
			{
				players[i].keyReleased(k);
			}
		}
		else if (currentScreen == PAUSE_SCREEN)
		{
			if (k == KeyEvent.VK_UP || k == KeyEvent.VK_W)
			{
				keyDelay = 0;
				enterReleased = true;
			}
			if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S)
			{
				keyDelay = 0;
				enterReleased = true;
			}
			if (k == KeyEvent.VK_ENTER || k == KeyEvent.VK_SPACE || k == KeyEvent.VK_SLASH)
			{
				enterReleased = true;
			}
		}
		else if (currentScreen == UPGRADES_SCREEN)
		{
			if (k == KeyEvent.VK_UP || k == KeyEvent.VK_W)
			{
				keyDelay = 0;
				enterReleased = true;
			}
			if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S)
			{
				keyDelay = 0;
				enterReleased = true;
			}
			if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_A)
			{
				keyDelay = 0;
				enterReleased = true;
			}
			if (k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D)
			{
				keyDelay = 0;
				enterReleased = true;
			}
			if (k == KeyEvent.VK_ENTER || k == KeyEvent.VK_SPACE || k == KeyEvent.VK_SLASH)
			{
				enterReleased = true;
			}

		}
		else if (currentScreen == PLAYER_SELECT_SCREEN)
		{
			if (k == KeyEvent.VK_UP || k == KeyEvent.VK_W)
			{
				keyDelay = 0;
			}
			if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S)
			{
				keyDelay = 0;
			}
			if (k == KeyEvent.VK_ESCAPE || k == KeyEvent.VK_BACK_SPACE)
			{
				GamePanel.states[GameStateManager.GAME_STATE] = new GameState();
				GameStateManager.setCurrentState(GameStateManager.MAIN_MENU_STATE);
			}
		}
		else if (currentScreen == MAP_SELECT_SCREEN)
		{
			if (k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D)
			{
				keyDelay = 0;
			}
			if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_A)
			{
				keyDelay = 0;
			}
			if (k == KeyEvent.VK_ESCAPE || k == KeyEvent.VK_BACK_SPACE)
			{
				GamePanel.states[GameStateManager.GAME_STATE] = new GameState();
				GameStateManager.setCurrentState(GameStateManager.MAIN_MENU_STATE);
			}
		}
	}

	/**
	 * Unused
	 */
	public void keyTyped(KeyEvent k)
	{

	}

	/**
	 * Handles mouse clicked events. Handles them differently depending on which
	 * screen you're on.
	 */
	public void mouseClicked(int x, int y)
	{
		if (currentScreen == PAUSE_SCREEN)
		{
			select();
		}
		else if (currentScreen == UPGRADES_SCREEN)
		{
			if (y > 100)
			{
				select();
			}
		}
		else if (currentScreen == PLAYER_SELECT_SCREEN)
		{
			select();
		}
		else if (currentScreen == MAP_SELECT_SCREEN)
		{
			if (y <= 400 && y >= 300 && x <= 150 && x >= 50)
			{
				currentOption--;
				if (currentOption < 0)
				{
					currentOption = mapOptions.length - 1;
				}
			}
			else if (y <= 400 && y >= 300 && x >= 875)
			{
				currentOption++;
				if (currentOption >= mapOptions.length)
				{
					currentOption = 0;
				}
			}
			else if ((y >= 600 && x >= 600) || (x >= 225 && x <= 800 && y >= 150 && y <= 575))
			{
				select();
			}
		}
	}

	/**
	 * Handles mouse moved events. Handles them differently depending on which
	 * screen you're on.
	 */
	public void mouseMoved(int x, int y)
	{
		if (currentScreen == PAUSE_SCREEN)
		{
			if (y < 275)
			{
				currentOption = RESUME;
			}
			else if (y >= 275 && y < 425)
			{
				currentOption = UPGRADES;
			}
			else
			{
				currentOption = BACK_TO_MAIN_MENU;
			}
		}
		else if (currentScreen == PLAYER_SELECT_SCREEN)
		{
			if (y < 400)
			{
				currentOption = ONE_PLAYER;
			}
			else
			{
				currentOption = TWO_PLAYER;
			}
		}
		else if (currentScreen == UPGRADES_SCREEN)
		{
			if (y < 350 && x < 250)
			{
				currentOption = KNIFE_UPGRADE;
			}
			else if (y < 625 && y >= 350 && x < 250)
			{
				currentOption = RIFLE_UPGRADE;
			}
			else if (y < 350 && x >= 550)
			{
				currentOption = PISTOL_UPGRADE;
			}
			else if (y < 625 && y >= 350 && x >= 550)
			{
				currentOption = SHOTGUN_UPGRADE;
			}
			else if (y >= 625 && x < 300)
			{
				currentOption = BACK;
			}

		}
	}
}