import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Eine Utility-Klasse zur Verwaltung von benutzerdefinierten Cursorn in Java Swing Anwendungen.
 * Bietet Funktionalität zum Laden, Anwenden und Zurücksetzen von benutzerdefinierten Cursorn.
 */
public class CustomCursorManager {

	// Konstanten
	private static final int MAX_CURSOR_SIZE = 32;
	private static final String CURSOR_NAME = "CustomCursor";
	private static final Logger LOGGER = Logger.getLogger(CustomCursorManager.class.getName());

	// Zustand
	private static Cursor customCursor = null;
	private static boolean isInitialized = false;

	/**
	 * Lädt einen Cursor aus einer Bilddatei.
	 *
	 * @param imagePath Pfad zur Cursor-Bilddatei (z.B. "cursor.png")
	 * @param hotspotX X-Koordinate des Klickpunkts des Cursors
	 * @param hotspotY Y-Koordinate des Klickpunkts des Cursors
	 * @return true, wenn der Cursor erfolgreich geladen wurde, sonst false
	 */
	public static boolean loadCursor(String imagePath, int hotspotX, int hotspotY) {
		if (imagePath == null || imagePath.trim().isEmpty()) {
			LOGGER.warning("Bildpfad darf nicht null oder leer sein");
			return false;
		}

		try {
			BufferedImage cursorImage = loadAndValidateImage(imagePath);
			if (cursorImage == null) return false;

			cursorImage = ensureValidSize(cursorImage);
			Point hotspot = createValidHotspot(hotspotX, hotspotY, cursorImage);

			customCursor = createCursor(cursorImage, hotspot);
			isInitialized = true;

			LOGGER.info("Cursor erfolgreich geladen: " + imagePath);
			return true;

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Fehler beim Laden des Cursors: " + imagePath, e);
			return false;
		}
	}

	/**
	 * Wendet den benutzerdefinierten Cursor auf eine bestimmte Komponente an.
	 *
	 * @param component Die Komponente, auf die der Cursor angewendet werden soll
	 * @return true, wenn erfolgreich angewendet, false, wenn der Cursor nicht geladen wurde
	 */
	public static boolean setCursorOn(Component component) {
		if (!validateCursorState() || component == null) {
			return false;
		}

		component.setCursor(customCursor);
		return true;
	}

	/**
	 * Wendet den benutzerdefinierten Cursor auf alle geöffneten Fenster und deren Komponenten an.
	 *
	 * @return true, wenn erfolgreich angewendet, false, wenn der Cursor nicht geladen wurde
	 */
	public static boolean setCursorEverywhere() {
		if (!validateCursorState()) {
			return false;
		}

		for (Window window : Window.getWindows()) {
			applyCursorRecursively(window);
		}
		return true;
	}

	/**
	 * Setzt den Cursor einer Komponente auf den Standard-Systemcursor zurück.
	 *
	 * @param component Die Komponente, deren Cursor zurückgesetzt werden soll
	 */
	public static void resetCursor(Component component) {
		if (component != null) {
			component.setCursor(Cursor.getDefaultCursor());
		}
	}

	/**
	 * Überprüft, ob ein benutzerdefinierter Cursor geladen und bereit zur Verwendung ist.
	 *
	 * @return true, wenn der Cursor geladen ist, sonst false
	 */
	public static boolean isLoaded() {
		return isInitialized && customCursor != null;
	}

	// Private Hilfsmethoden

	/**
	 * Lädt und validiert ein Bild aus einer Datei.
	 *
	 * @param imagePath Pfad zur Bilddatei
	 * @return Das geladene BufferedImage oder null, wenn ein Fehler auftritt
	 */
	private static BufferedImage loadAndValidateImage(String imagePath) {
		try {
			File imageFile = new File(imagePath);
			if (!imageFile.exists()) {
				LOGGER.warning("Bilddatei nicht gefunden: " + imagePath);
				return null;
			}

			return ImageIO.read(imageFile);

		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Fehler beim Lesen der Bilddatei: " + imagePath, e);
			return null;
		}
	}

	/**
	 * Stellt sicher, dass das Bild eine gültige Größe hat.
	 *
	 * @param image Das zu überprüfende Bild
	 * @return Das Bild mit gültiger Größe
	 */
	private static BufferedImage ensureValidSize(BufferedImage image) {
		if (image.getWidth() <= MAX_CURSOR_SIZE && image.getHeight() <= MAX_CURSOR_SIZE) {
			return image;
		}

		LOGGER.info("Cursor wird auf " + MAX_CURSOR_SIZE + "x" + MAX_CURSOR_SIZE + " skaliert");
		return resizeImage(image, MAX_CURSOR_SIZE, MAX_CURSOR_SIZE);
	}

	/**
	 * Erstellt einen gültigen Hotspot innerhalb der Bildgrenzen.
	 *
	 * @param x X-Koordinate des Hotspots
	 * @param y Y-Koordinate des Hotspots
	 * @param image Das Cursor-Bild
	 * @return Ein gültiger Hotspot innerhalb der Bildgrenzen
	 */
	private static Point createValidHotspot(int x, int y, BufferedImage image) {
		int validX = Math.max(0, Math.min(x, image.getWidth() - 1));
		int validY = Math.max(0, Math.min(y, image.getHeight() - 1));

		if (validX != x || validY != y) {
			LOGGER.info("Hotspot wurde auf gültige Grenzen angepasst: (" + validX + "," + validY + ")");
		}

		return new Point(validX, validY);
	}

	/**
	 * Erstellt einen Cursor aus einem Bild und einem Hotspot.
	 *
	 * @param image Das Cursor-Bild
	 * @param hotspot Der Hotspot des Cursors
	 * @return Der erstellte Cursor
	 */
	private static Cursor createCursor(BufferedImage image, Point hotspot) {
		return Toolkit.getDefaultToolkit().createCustomCursor(image, hotspot, CURSOR_NAME);
	}

	/**
	 * Überprüft, ob der Cursor geladen und bereit zur Verwendung ist.
	 *
	 * @return true, wenn der Cursor geladen ist, sonst false
	 */
	private static boolean validateCursorState() {
		if (!isInitialized || customCursor == null) {
			LOGGER.warning("Cursor nicht geladen. Rufen Sie zuerst loadCursor() auf.");
			return false;
		}
		return true;
	}

	/**
	 * Skaliert ein Bild auf die angegebene Breite und Höhe.
	 *
	 * @param original Das Originalbild
	 * @param width Die gewünschte Breite
	 * @param height Die gewünschte Höhe
	 * @return Das skalierte Bild
	 */
	private static BufferedImage resizeImage(BufferedImage original, int width, int height) {
		BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = resized.createGraphics();

		try {
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2d.drawImage(original, 0, 0, width, height, null);
		} finally {
			g2d.dispose();
		}

		return resized;
	}

	/**
	 * Wendet den Cursor rekursiv auf alle Komponenten in einem Container an.
	 *
	 * @param container Der Container, auf den der Cursor angewendet werden soll
	 */
	private static void applyCursorRecursively(Container container) {
		if (container == null) return;

		container.setCursor(customCursor);

		for (Component component : container.getComponents()) {
			component.setCursor(customCursor);
			if (component instanceof Container) {
				applyCursorRecursively((Container) component);
			}
		}
	}
}
