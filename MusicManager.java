import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Die Klasse MusicManager verwaltet das Laden, Abspielen und Steuern von Hintergrundmusik.
 * Sie ermöglicht das Einstellen der Lautstärke und das Aktivieren/Deaktivieren der Musik.
 */
public class MusicManager {
	private static Map<String, Clip> clips = new HashMap<>(); // Speichert geladene Audio-Clips
	private static Map<String, FloatControl> volumeControls = new HashMap<>(); // Speichert Lautstärkeregelungen für jeden Clip
	private static float masterVolume = 0.7f; // Standardlautstärke
	private static boolean musicEnabled = true; // Status, ob Musik aktiviert ist
	private static String currentBackgroundMusic = null; // Name der aktuellen Hintergrundmusik

	/**
	 * Lädt eine Audiodatei und speichert sie unter einem bestimmten Namen.
	 *
	 * @param name Der Name, unter dem die Musik gespeichert wird.
	 * @param filepath Der Pfad zur Audiodatei.
	 */
	public static void loadMusic(String name, String filepath) {
		try {
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(filepath));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInput);
			clips.put(name, clip);

			// Lautstärkeregelung für jeden Clip
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

	/**
	 * Spielt die Hintergrundmusik in einer Endlosschleife ab.
	 *
	 * @param name Der Name der abzuspielenden Hintergrundmusik.
	 */
	public static void playBackgroundMusic(String name) {
		if (!musicEnabled) return;

		// Aktuelle Hintergrundmusik stoppen
		if (currentBackgroundMusic != null) {
			stopCurrentMusic();
		}

		Clip clip = clips.get(name);
		if (clip != null) {
			clip.setFramePosition(0); // Setzt die Wiedergabe auf den Anfang
			clip.loop(Clip.LOOP_CONTINUOUSLY); // Schleifenmodus aktivieren
			clip.start();
			currentBackgroundMusic = name;
		}
	}

	/**
	 * Stoppt die aktuell abgespielte Musik.
	 */
	public static void stopCurrentMusic() {
		if (currentBackgroundMusic != null) {
			Clip clip = clips.get(currentBackgroundMusic);
			if (clip != null && clip.isRunning()) {
				clip.stop();
			}
		}
	}

	/**
	 * Pausiert die aktuell abgespielte Musik.
	 */
	public static void pauseCurrentMusic() {
		if (currentBackgroundMusic != null) {
			Clip clip = clips.get(currentBackgroundMusic);
			if (clip != null && clip.isRunning()) {
				clip.stop();
			}
		}
	}

	/**
	 * Setzt die aktuell pausierte Musik fort.
	 */
	public static void resumeCurrentMusic() {
		if (currentBackgroundMusic != null && musicEnabled) {
			Clip clip = clips.get(currentBackgroundMusic);
			if (clip != null && !clip.isRunning()) {
				clip.start();
			}
		}
	}

	/**
	 * Setzt die Master-Lautstärke für alle Clips.
	 *
	 * @param volume Die Lautstärke im Bereich von 0.0 bis 1.0.
	 */
	public static void setMasterVolume(float volume) {
		masterVolume = Math.max(0.0f, Math.min(1.0f, volume));

		// Aktualisiert die Lautstärke für alle geladenen Clips
		for (String name : clips.keySet()) {
			updateVolume(name);
		}

		// Speichert die Lautstärke in den Einstellungen
		StyleManager.setProperty("audio.volume", String.valueOf(masterVolume));
	}

	/**
	 * Aktiviert oder deaktiviert die Musik.
	 *
	 * @param enabled Gibt an, ob die Musik aktiviert werden soll.
	 */
	public static void setMusicEnabled(boolean enabled) {
		musicEnabled = enabled;

		if (!enabled) {
			stopCurrentMusic();
		} else if (currentBackgroundMusic != null) {
			resumeCurrentMusic();
		}

		// Speichert den Musikstatus in den Einstellungen
		StyleManager.setProperty("audio.enabled", String.valueOf(enabled));
	}

	/**
	 * Aktualisiert die Lautstärke für einen bestimmten Clip.
	 *
	 * @param name Der Name des Clips, dessen Lautstärke aktualisiert werden soll.
	 */
	private static void updateVolume(String name) {
		FloatControl volumeControl = volumeControls.get(name);
		if (volumeControl != null) {
			float range = volumeControl.getMaximum() - volumeControl.getMinimum();
			float gain = (range * masterVolume) + volumeControl.getMinimum();
			volumeControl.setValue(gain);
		}
	}

	/**
	 * Lädt die Audio-Einstellungen aus den gespeicherten Einstellungen.
	 */
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

	/**
	 * Initialisiert die Musik beim Programmstart.
	 */
	public static void initializeMusic() {
		loadSettings();

		// Lädt die Hintergrundmusiken
		loadMusic("menu_background", "sounds/menu_background.wav");
		loadMusic("quiz_background", "sounds/quiz_background.wav");
	}

	/**
	 * Gibt die aktuelle Master-Lautstärke zurück.
	 *
	 * @return Die aktuelle Master-Lautstärke.
	 */
	public static float getMasterVolume() {
		return masterVolume;
	}

	/**
	 * Gibt an, ob die Musik aktiviert ist.
	 *
	 * @return true, wenn die Musik aktiviert ist, sonst false.
	 */
	public static boolean isMusicEnabled() {
		return musicEnabled;
	}

	/**
	 * Überprüft, ob aktuell Musik abgespielt wird.
	 *
	 * @return true, wenn Musik abgespielt wird, sonst false.
	 */
	public static boolean isCurrentlyPlaying() {
		if (currentBackgroundMusic == null) return false;
		Clip clip = clips.get(currentBackgroundMusic);
		return clip != null && clip.isRunning();
	}

	/**
	 * Gibt den Namen der aktuellen Hintergrundmusik zurück.
	 *
	 * @return Der Name der aktuellen Hintergrundmusik.
	 */
	public static String getCurrentMusic() {
		return currentBackgroundMusic;
	}
}
