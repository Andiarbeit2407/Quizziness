import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MusicManager {
	private static Map<String, Clip> clips = new HashMap<>();
	private static Map<String, FloatControl> volumeControls = new HashMap<>();
	private static float masterVolume = 0.7f;
	private static boolean musicEnabled = true;
	private static String currentBackgroundMusic = null;

	// Musik laden
	public static void loadMusic(String name, String filepath) {
		try {
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(filepath));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInput);
			clips.put(name, clip);

			// Volume Control für jeden Clip
			if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
				FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				volumeControls.put(name, volumeControl);
				updateVolume(name);
			}
		} catch (Exception e) {
			System.err.println("Fehler beim Laden der Musik: " + filepath);
			e.printStackTrace();
		}
	}

	// Hintergrundmusik wechseln
	public static void playBackgroundMusic(String name) {
		if (!musicEnabled) return;

		// Aktuelle Hintergrundmusik stoppen
		if (currentBackgroundMusic != null) {
			stopCurrentMusic();
		}

		Clip clip = clips.get(name);
		if (clip != null) {
			clip.setFramePosition(0);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			clip.start();
			currentBackgroundMusic = name;
		}
	}

	// Aktuelle Musik stoppen
	public static void stopCurrentMusic() {
		if (currentBackgroundMusic != null) {
			Clip clip = clips.get(currentBackgroundMusic);
			if (clip != null && clip.isRunning()) {
				clip.stop();
			}
		}
	}

	// Musik pausieren
	public static void pauseCurrentMusic() {
		if (currentBackgroundMusic != null) {
			Clip clip = clips.get(currentBackgroundMusic);
			if (clip != null && clip.isRunning()) {
				clip.stop();
			}
		}
	}

	// Musik fortsetzen
	public static void resumeCurrentMusic() {
		if (currentBackgroundMusic != null && musicEnabled) {
			Clip clip = clips.get(currentBackgroundMusic);
			if (clip != null && !clip.isRunning()) {
				clip.start();
			}
		}
	}

	// Master Volume setzen (0.0 - 1.0)
	public static void setMasterVolume(float volume) {
		masterVolume = Math.max(0.0f, Math.min(1.0f, volume));

		// Alle geladenen Clips aktualisieren
		for (String name : clips.keySet()) {
			updateVolume(name);
		}

		// In config.properties speichern
		StyleManager.setProperty("audio.volume", String.valueOf(masterVolume));
	}

	// Musik aktivieren/deaktivieren
	public static void setMusicEnabled(boolean enabled) {
		musicEnabled = enabled;

		if (!enabled) {
			stopCurrentMusic();
		} else if (currentBackgroundMusic != null) {
			resumeCurrentMusic();
		}

		// In config.properties speichern
		StyleManager.setProperty("audio.enabled", String.valueOf(enabled));
	}

	// Volume für einzelnen Clip aktualisieren
	private static void updateVolume(String name) {
		FloatControl volumeControl = volumeControls.get(name);
		if (volumeControl != null) {
			float range = volumeControl.getMaximum() - volumeControl.getMinimum();
			float gain = (range * masterVolume) + volumeControl.getMinimum();
			volumeControl.setValue(gain);
		}
	}

	// Einstellungen laden
	public static void loadSettings() {
		try {
			String volumeStr = StyleManager.getProperty("audio.volume", "0.7");
			masterVolume = Float.parseFloat(volumeStr);

			String enabledStr = StyleManager.getProperty("audio.enabled", "true");
			musicEnabled = Boolean.parseBoolean(enabledStr);
		} catch (Exception e) {
			System.err.println("Fehler beim Laden der Audio-Einstellungen: " + e.getMessage());
		}
	}

	// Musik beim Programmstart laden
	public static void initializeMusic() {
		loadSettings();

		// Nur die beiden Hintergrundmusiken laden
		loadMusic("menu_background", "sounds/menu_background.wav");
		loadMusic("quiz_background", "sounds/quiz_background.wav");
	}

	// Getter
	public static float getMasterVolume() {
		return masterVolume;
	}

	public static boolean isMusicEnabled() {
		return musicEnabled;
	}

	public static boolean isCurrentlyPlaying() {
		if (currentBackgroundMusic == null) return false;
		Clip clip = clips.get(currentBackgroundMusic);
		return clip != null && clip.isRunning();
	}

	public static String getCurrentMusic() {
		return currentBackgroundMusic;
	}
}