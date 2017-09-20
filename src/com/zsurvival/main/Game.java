package com.zsurvival.main;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * The game
 * @author Raya and Daniel
 */
public class Game
{
	public static GamePanel panel;

	/**
	 * The main game
	 * @param args Command line arguments
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Zombie Survival");
		ImageIcon icon = new ImageIcon("res/Icon.png");

		panel = new GamePanel();
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.addKeyListener(panel);
		frame.addMouseListener(panel);
		frame.addMouseMotionListener(panel);
		frame.setIconImage(icon.getImage());
		if (GamePanel.splash != null)
		{
			while (GamePanel.splash.isVisible())
			{
				// Keeps the splash screen visible until all assets are loaded
			}
		}
		frame.setVisible(true);
	}

}
