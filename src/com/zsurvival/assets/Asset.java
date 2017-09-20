package com.zsurvival.assets;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.zsurvival.audio.Audio;

/**
 * Loads all of the assets (images, sprite sheets, maps, etc.) necessary for
 * running the game at the beginning so that there is no load lag during the
 * actual game play. However it does slow down initial start up time.
 * @author Raya and Daniel
 */
public class Asset
{
	private Loader load;

	/*********************** Sprite Sheets *******************************/

	// Knife
	public static SpriteSheet knifeIdleSheet;
	public static SpriteSheet knifeMeleeSheet;
	public static SpriteSheet knifeMoveSheet;

	public static BufferedImage[] knifeIdle;
	public static BufferedImage[] knifeMelee;
	public static BufferedImage[] knifeMove;

	// Pistol
	public static SpriteSheet pistolIdleSheet;
	public static SpriteSheet pistolShootSheet;
	public static SpriteSheet pistolMoveSheet;

	public static BufferedImage[] pistolIdle;
	public static BufferedImage[] pistolShoot;
	public static BufferedImage[] pistolMove;

	// Rifle
	public static SpriteSheet rifleIdleSheet;
	public static SpriteSheet rifleShootSheet;
	public static SpriteSheet rifleMoveSheet;

	public static BufferedImage[] rifleIdle;
	public static BufferedImage[] rifleShoot;
	public static BufferedImage[] rifleMove;

	// Shotgun
	public static SpriteSheet shotgunIdleSheet;
	public static SpriteSheet shotgunShootSheet;
	public static SpriteSheet shotgunMoveSheet;

	public static BufferedImage[] shotgunIdle;
	public static BufferedImage[] shotgunShoot;
	public static BufferedImage[] shotgunMove;

	// Zombie
	public static SpriteSheet zombieWalkSheet;
	public static SpriteSheet zombieAttackSheet;
	public static SpriteSheet zombieBloodSheet;

	public static BufferedImage[] zombieWalk;
	public static BufferedImage[] zombieAttack;
	public static BufferedImage[] zombieBlood;

	// Bullet
	public static BufferedImage bullet;

	// Rings
	public static BufferedImage redRing;
	public static BufferedImage blueRing;

	/**************************** Maps ******************************/

	public static BufferedImage map1Image;
	public static BufferedImage map2Image;
	public static BufferedImage map3Image;
	public static BufferedImage map4Image;

	public static CollisionMap[] maps;

	/*********************** Crate Locations *************************/

	private Point[] map1Crates = { new Point(165, 390), new Point(650, 300) };
	private Point[] map2Crates = { new Point(300, 190), new Point(650, 520) };
	private Point[] map3Crates = { new Point(550, 190), new Point(200, 520), new Point(650, 520) };
	private Point[] map4Crates = { new Point(540, 160), new Point(205, 485), new Point(650, 515), new Point(840, 300) };

	/**************************** Menus *******************************/

	// Main menu
	public static BufferedImage mainStart;
	public static BufferedImage mainInstructions;
	public static BufferedImage mainHighscores;
	public static BufferedImage mainQuit;

	// Highscores
	public static BufferedImage highscoreMenu;
	public static Font highscoresFont;

	// Instructions menu
	public static BufferedImage instructionsObjective;
	public static BufferedImage instructionsControls;
	public static BufferedImage instructionsHUD;
	public static BufferedImage instructionsCrates;
	public static BufferedImage instructionsBackToMainMenu;

	// Instructions sub menus
	public static BufferedImage objectiveMenu;
	public static BufferedImage controlsMenu;
	public static BufferedImage HUDMenu;
	public static BufferedImage cratesMenu;

	// Pause menu
	public static BufferedImage pauseResume;
	public static BufferedImage pauseUpgrades;
	public static BufferedImage pauseBackToMainMenu;

	// Upgrades menu
	public static BufferedImage upgradesBackSelect;
	public static BufferedImage upgradeKnife;
	public static BufferedImage upgradePistol;
	public static BufferedImage upgradeRifle;
	public static BufferedImage upgradeShotgun;
	public static BufferedImage lock;

	// Game over menu
	public static BufferedImage gameOverPlayAgain;
	public static BufferedImage gameOverCredits;
	public static BufferedImage gameOverBackToMainMenu;
	public static BufferedImage congratsMenu;
	public static Color fontColour;
	public static Font finalScoreFont;

	// Player select menu
	public static BufferedImage playerSelectOne;
	public static BufferedImage playerSelectTwo;

	// Map select menu
	public static BufferedImage mapSelectOne;
	public static BufferedImage mapSelectTwo;
	public static BufferedImage mapSelectThree;
	public static BufferedImage mapSelectFour;

	// Credits menu
	public static BufferedImage credits;

	/******************************* HUD ******************************/

	// HUD fonts
	public static Font newWaveFont;
	public static Font scoreFont;
	public static Font healthFont;
	public static Font weaponFont;

	// Power ups
	public static BufferedImage damageActive;
	public static BufferedImage pointsActive;

	/*********************** Crates *********************************/
	public static BufferedImage ammoCrate;
	public static BufferedImage damageCrate;
	public static BufferedImage pointsCrate;
	public static BufferedImage healthCrate;
	public static BufferedImage nukeCrate;

	/***************************** Audio *****************************/
	public static Audio backgroundMusic;

	/*************************** Constants **************************/

	// Sprites
	public static final int SPRITE_SIZE = 100;

	// Map
	public static final int MAP_WIDTH = 40;
	public static final int MAP_HEIGHT = 30;

	/**
	 * Constructor
	 */
	public Asset()
	{
		// Loader (loads fonts and images)
		load = new Loader();

		/*************************** Load Sprites ****************************/
		// Knife
		knifeIdleSheet = new SpriteSheet(load.loadImage("/Sprites/Player/Knife Idle.png"));
		knifeMeleeSheet = new SpriteSheet(load.loadImage("/Sprites/Player/Knife Melee.png"));
		knifeMoveSheet = new SpriteSheet(load.loadImage("/Sprites/Player/Knife Move.png"));

		knifeIdle = load.loadSheet(knifeIdleSheet, 20);
		knifeMelee = load.loadSheet(knifeMeleeSheet, 15);
		knifeMove = load.loadSheet(knifeMoveSheet, 20);

		// Pistol
		pistolIdleSheet = new SpriteSheet(load.loadImage("/Sprites/Player/Pistol Idle.png"));
		pistolShootSheet = new SpriteSheet(load.loadImage("/Sprites/Player/Pistol Shoot.png"));
		pistolMoveSheet = new SpriteSheet(load.loadImage("/Sprites/Player/Pistol Move.png"));

		pistolIdle = load.loadSheet(pistolIdleSheet, 20);
		pistolShoot = load.loadSheet(pistolShootSheet, 3);
		pistolMove = load.loadSheet(pistolMoveSheet, 20);

		// Rifle
		rifleIdleSheet = new SpriteSheet(load.loadImage("/Sprites/Player/Rifle Idle.png"));
		rifleShootSheet = new SpriteSheet(load.loadImage("/Sprites/Player/Rifle Shoot.png"));
		rifleMoveSheet = new SpriteSheet(load.loadImage("/Sprites/Player/Rifle Move.png"));

		rifleIdle = load.loadSheet(rifleIdleSheet, 20);
		rifleShoot = load.loadSheet(rifleShootSheet, 3);
		rifleMove = load.loadSheet(rifleMoveSheet, 20);

		// Shotgun
		shotgunIdleSheet = new SpriteSheet(load.loadImage("/Sprites/Player/Shotgun Idle.png"));
		shotgunShootSheet = new SpriteSheet(load.loadImage("/Sprites/Player/Shotgun Shoot.png"));
		shotgunMoveSheet = new SpriteSheet(load.loadImage("/Sprites/Player/Shotgun Move.png"));

		shotgunIdle = load.loadSheet(shotgunIdleSheet, 20);
		shotgunShoot = load.loadSheet(shotgunShootSheet, 3);
		shotgunMove = load.loadSheet(shotgunMoveSheet, 20);

		// Zombie
		zombieWalkSheet = new SpriteSheet(load.loadImage("/Sprites/Zombie/Zombie Walk.png"));
		zombieAttackSheet = new SpriteSheet(load.loadImage("/Sprites/Zombie/Zombie Attack.png"));
		zombieBloodSheet = new SpriteSheet(load.loadImage("/Sprites/Explosion/Blood Splatter.png"));

		zombieWalk = load.loadSheet(zombieWalkSheet, 6);
		zombieAttack = load.loadSheet(zombieAttackSheet, 6);
		zombieBlood = load.loadSheet(zombieBloodSheet, 16);

		// Bullet
		bullet = load.loadImage("/Sprites/Explosion/Bullet Hit.png");

		// Rings
		redRing = load.loadImage("/Sprites/Player/Rings/Red Ring.png");
		blueRing = load.loadImage("/Sprites/Player/Rings/Blue Ring.png");

		/*************************** Load Maps **************************/
		// Images
		map1Image = load.loadImage("/Maps/Map 1.png");
		map2Image = load.loadImage("/Maps/Map 2.png");
		map3Image = load.loadImage("/Maps/Map 3.png");
		map4Image = load.loadImage("/Maps/Map 4.png");

		// Collison Maps
		maps = new CollisionMap[4];

		// Map 1
		maps[0] = new CollisionMap(map1Image, new Point(300, 250), new Point(425, 250), map1Crates);

		maps[0].addWall(0, 0, 250, 150);
		maps[0].addWall(425, 0, 575, 150);
		maps[0].addWall(725, 300, 275, 25);
		maps[0].addWall(175, 450, 25, 300);
		maps[0].addWall(825, 600, 25, 150);

		maps[0].addZombieSpawn(225, 0, 225, 150);
		maps[0].addZombieSpawn(850, 125, 200, 200);

		// Map 2
		maps[1] = new CollisionMap(map2Image, new Point(300, 250), new Point(425, 250), map2Crates);

		maps[1].addWall(0, 0, 350, 175);
		maps[1].addWall(550, 0, 450, 175);
		maps[1].addWall(0, 575, 450, 175);
		maps[1].addWall(650, 575, 350, 175);

		maps[1].addZombieSpawn(325, 0, 250, 175);
		maps[1].addZombieSpawn(425, 575, 250, 175);

		// Map 3
		maps[2] = new CollisionMap(map3Image, new Point(400, 350), new Point(525, 350), map3Crates);

		maps[2].addWall(0, 0, 350, 275);
		maps[2].addWall(550, 0, 450, 175);
		maps[2].addWall(0, 450, 200, 150);
		maps[2].addWall(0, 575, 450, 175);
		maps[2].addWall(650, 575, 350, 175);

		maps[2].addZombieSpawn(325, 0, 250, 175);
		maps[2].addZombieSpawn(425, 575, 250, 175);
		maps[2].addZombieSpawn(0, 250, 200, 225);

		// Map 4
		maps[3] = new CollisionMap(map4Image, new Point(400, 350), new Point(525, 350), map4Crates);

		maps[3].addWall(0, 0, 350, 275);
		maps[3].addWall(0, 450, 200, 125);
		maps[3].addWall(550, 0, 25, 150);
		maps[3].addWall(425, 600, 25, 150);
		maps[3].addWall(900, 0, 100, 450);
		maps[3].addWall(650, 575, 350, 175);

		maps[3].addZombieSpawn(325, 0, 250, 175);
		maps[3].addZombieSpawn(425, 575, 250, 175);
		maps[3].addZombieSpawn(0, 250, 200, 225);
		maps[3].addZombieSpawn(900, 425, 100, 175);

		/*************************** Load Menus **************************/
		// Start menu
		mainStart = load.loadImage("/Menus/Main/Start Select.png");
		mainInstructions = load.loadImage("/Menus/Main/Instructions Select.png");
		mainHighscores = load.loadImage("/Menus/Main/Highscore Select.png");
		mainQuit = load.loadImage("/Menus/Main/Quit Select.png");

		// Highscore menu
		highscoreMenu = load.loadImage("/Menus/Highscore/Highscores Menu.png");

		// Instructions menu
		instructionsObjective = load.loadImage("/Menus/Instructions/Objective Select.png");
		instructionsControls = load.loadImage("/Menus/Instructions/Controls Select.png");
		instructionsHUD = load.loadImage("/Menus/Instructions/HUD Select.png");
		instructionsCrates = load.loadImage("/Menus/Instructions/Crates Select.png");
		instructionsBackToMainMenu = load.loadImage("/Menus/Instructions/Back To Main Menu Select.png");

		// Instructions sub menus
		objectiveMenu = load.loadImage("/Menus/Instructions/Objective Menu.png");
		controlsMenu = load.loadImage("/Menus/Instructions/Controls Menu.png");
		HUDMenu = load.loadImage("/Menus/Instructions/HUD Menu.png");
		cratesMenu = load.loadImage("/Menus/Instructions/Crates Menu.png");

		// Pause menu
		pauseResume = load.loadImage("/Menus/Pause/Resume Select.png");
		pauseUpgrades = load.loadImage("/Menus/Pause/Upgrades Select.png");
		pauseBackToMainMenu = load.loadImage("/Menus/Pause/Back To Main Menu Select.png");

		// Upgrades menu
		upgradesBackSelect = load.loadImage("/Menus/Upgrades/Upgrades Back Select.png");
		upgradeKnife = load.loadImage("/Menus/Upgrades/Knife Upgrade.png");
		upgradePistol = load.loadImage("/Menus/Upgrades/Pistol Upgrade.png");
		upgradeRifle = load.loadImage("/Menus/Upgrades/Rifle Upgrade.png");
		upgradeShotgun = load.loadImage("/Menus/Upgrades/Shotgun Upgrade.png");
		lock = load.loadImage("/Menus/Upgrades/Lock.png");

		// Game over menu
		gameOverPlayAgain = load.loadImage("/Menus/GameOver/Play Again Select.png");
		gameOverCredits = load.loadImage("/Menus/GameOver/Credits Select.png");
		gameOverBackToMainMenu = load.loadImage("/Menus/GameOver/Back To Main Menu Select.png");
		congratsMenu = load.loadImage("/Menus/GameOver/Congrats Menu.png");

		// Player select menu
		playerSelectOne = load.loadImage("/Menus/PlayerSelect/One Player.png");
		playerSelectTwo = load.loadImage("/Menus/PlayerSelect/Two Player.png");

		// Map select menu
		mapSelectOne = load.loadImage("/Menus/MapSelect/Map 1.png");
		mapSelectTwo = load.loadImage("/Menus/MapSelect/Map 2.png");
		mapSelectThree = load.loadImage("/Menus/MapSelect/Map 3.png");
		mapSelectFour = load.loadImage("/Menus/MapSelect/Map 4.png");

		// Credits menu
		credits = load.loadImage("/Menus/Credits/Credits.png");

		// Menu font and colour
		fontColour = new Color(186, 0, 0);

		load.loadFont("/HUD/True Lies.ttf");
		finalScoreFont = new Font("True Lies", Font.PLAIN, 70);
		highscoresFont = new Font("True Lies", Font.PLAIN, 50);

		/*************************** Load HUD **************************/
		// HUD Fonts
		newWaveFont = new Font("True Lies", Font.PLAIN, 100);
		scoreFont = new Font("True Lies", Font.PLAIN, 36);
		healthFont = new Font("True Lies", Font.PLAIN, 32);
		weaponFont = new Font("True Lies", Font.PLAIN, 40);

		// Power ups
		damageActive = load.loadImage("/HUD/Double Damage Active.png");
		pointsActive = load.loadImage("/HUD/Double Points Active.png");

		/*************************** Load Crates **********************/
		ammoCrate = load.loadImage("/Sprites/Crates/Ammo Crate.png");
		damageCrate = load.loadImage("/Sprites/Crates/Double Damage Crate.png");
		pointsCrate = load.loadImage("/Sprites/Crates/Double Points Crate.png");
		healthCrate = load.loadImage("/Sprites/Crates/Health Crate.png");
		nukeCrate = load.loadImage("/Sprites/Crates/Nuke Crate.png");

		/***************************** Audio *****************************/
		backgroundMusic = new Audio("/Audio/Music/Theme Music.mp3");
	}
}