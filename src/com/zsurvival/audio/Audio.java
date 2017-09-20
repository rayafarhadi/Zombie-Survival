package com.zsurvival.audio;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * An audio clip
 * @author Raya and Daniel
 */
public class Audio
{
	private Clip clip;

	/**
	 * Constructor
	 * @param path The path to the clip location
	 */
	public Audio(String path)
	{
		try
		{
			AudioInputStream input = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(path));
			AudioFormat baseFormat = input.getFormat();
			AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);

			AudioInputStream decodeStream = AudioSystem.getAudioInputStream(decodeFormat, input);

			clip = AudioSystem.getClip();
			clip.open(decodeStream);
		}
		catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Plays the audio clip
	 */
	public void play()
	{
		if (clip == null)
		{
			return;
		}
		stop();
		clip.setFramePosition(0);
		clip.start();
	}

	/**
	 * Stops the audio clip
	 */
	public void stop()
	{
		if (clip.isRunning())
		{
			clip.stop();
		}
	}

	/**
	 * Loops the audio clip until the clip is stopped
	 */
	public void loop()
	{
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	/**
	 * Closes the audio clip
	 */
	public void close()
	{
		stop();
		clip.close();
	}

	/**
	 * Returns whether or not the audio clip has finished
	 * @return Whether or not the audio clip has finished
	 */
	public boolean isRunning()
	{
		return clip.isRunning();
	}
}
