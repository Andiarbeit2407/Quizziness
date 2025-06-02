import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class CustomCursorManager {
	private static Cursor customCursor = null;
	private static boolean isInitialized = false;

	/**
	 * Cursor aus einer Datei laden
	 * @param imagePath Pfad zur Cursor-Datei (z.B. "cursor.png")
	 * @param hotspotX X-Position des Klick-Punktes im Bild
	 * @param hotspotY Y-Position des Klick-Punktes im Bild
	 * @return true wenn erfolgreich geladen
	 */
	public static boolean loadCursor(String imagePath, int hotspotX, int hotspotY) {
		try {
			// Bild laden
			BufferedImage cursorImage = ImageIO.read(new File(imagePath));

			// Größe prüfen und anpassen falls nötig
			if (cursorImage.getWidth() > 32 || cursorImage.getHeight() > 32) {
				cursorImage = resizeImage(cursorImage, 32, 32);
				System.out.println("Cursor wurde auf 32x32 verkleinert");
			}

			// Hotspot (Klick-Punkt) definieren
			Point hotspot = new Point(hotspotX, hotspotY);

			// Custom Cursor erstellen
			customCursor = Toolkit.getDefaultToolkit()
					.createCustomCursor(cursorImage, hotspot, "MyCursor");

			isInitialized = true;
			System.out.println("Cursor erfolgreich geladen: " + imagePath);
			return true;

		} catch (IOException e) {
			System.err.println("Datei nicht gefunden: " + imagePath);
			return false;
		} catch (Exception e) {
			System.err.println("Fehler beim Laden: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Cursor auf eine Komponente anwenden
	 */
	public static void setCursorOn(Component component) {
		if (!isInitialized) {
			System.err.println("Erst Cursor laden mit loadCursor()!");
			return;
		}
		component.setCursor(customCursor);
	}

	/**
	 * Cursor auf alle Fenster anwenden
	 */
	public static void setCursorEverywhere() {
		if (!isInitialized) {
			System.err.println("Erst Cursor laden mit loadCursor()!");
			return;
		}

		for (Window window : Window.getWindows()) {
			setCursorRecursive(window);
		}
	}

	/**
	 * Standard-Cursor wiederherstellen
	 */
	public static void resetCursor(Component component) {
		component.setCursor(Cursor.getDefaultCursor());
	}

	// Hilfsmethoden (privat)
	private static BufferedImage resizeImage(BufferedImage original, int width, int height) {
		BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = resized.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.drawImage(original, 0, 0, width, height, null);
		g2d.dispose();
		return resized;
	}

	private static void setCursorRecursive(Container container) {
		container.setCursor(customCursor);
		for (Component comp : container.getComponents()) {
			comp.setCursor(customCursor);
			if (comp instanceof Container) {
				setCursorRecursive((Container) comp);
			}
		}
	}

	/**
	 * Prüfen ob Cursor geladen ist
	 */
	public static boolean isLoaded() {
		return isInitialized;
	}
}