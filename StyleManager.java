import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Die Klasse StyleManager verwaltet und lädt Stileinstellungen aus einer Konfigurationsdatei.
 * Sie ermöglicht das Speichern und Abrufen von Farben und Schriftarten.
 */
public class StyleManager {
    private static Properties props = new Properties();

    /**
     * Lädt die Konfigurationsdatei von einem angegebenen Pfad.
     *
     * @param path Der Pfad zur Konfigurationsdatei.
     */
    public static void loadConfig(String path) {
        try (FileInputStream fis = new FileInputStream(path)) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println("Konnte Konfigurationsdatei nicht laden: " + path);
            e.printStackTrace();
        }
    }

    /**
     * Gibt den Wert einer Eigenschaft zurück oder einen Standardwert, falls die Eigenschaft nicht gefunden wird.
     *
     * @param key Der Schlüssel der Eigenschaft.
     * @param defaultValue Der Standardwert, der zurückgegeben wird, falls der Schlüssel nicht gefunden wird.
     * @return Der Wert der Eigenschaft oder der Standardwert.
     */
    public static String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    /**
     * Setzt eine Eigenschaft und speichert die Änderungen.
     *
     * @param key Der Schlüssel der Eigenschaft.
     * @param value Der Wert der Eigenschaft.
     */
    public static void setProperty(String key, String value) {
        props.setProperty(key, value);
        saveProperties();
    }

    /**
     * Speichert die aktuellen Eigenschaften in die Konfigurationsdatei.
     */
    private static void saveProperties() {
        try (OutputStream output = new FileOutputStream("config.properties")) {
            props.store(output, null);
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Konfiguration: " + e.getMessage());
        }
    }

    /**
     * Gibt eine Farbe aus der Konfiguration zurück oder eine Fallback-Farbe, falls der Wert nicht gefunden oder ungültig ist.
     *
     * @param key Der Schlüssel der Farbeigenschaft.
     * @param fallback Die Fallback-Farbe, die zurückgegeben wird, falls der Schlüssel nicht gefunden oder ungültig ist.
     * @return Die Farbe aus der Konfiguration oder die Fallback-Farbe.
     */
    public static Color getColor(String key, Color fallback) {
        String value = props.getProperty(key);
        if (value != null) {
            try {
                return Color.decode(value);
            } catch (NumberFormatException e) {
                System.err.println("Ungültige Farbe für " + key + ": " + value);
            }
        }
        return fallback;
    }

    /**
     * Setzt eine Farbe in der Konfiguration.
     *
     * @param key Der Schlüssel der Farbeigenschaft.
     * @param hex Der Hexadezimalwert der Farbe.
     */
    public static void setColor(String key, String hex) {
        props.setProperty(key, hex);
        try (FileOutputStream out = new FileOutputStream("config.properties")) {
            props.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gibt eine Schriftart zurück, die aus der Konfiguration geladen wird, oder eine Fallback-Schriftart, falls die Werte nicht gefunden oder ungültig sind.
     *
     * @param nameKey Der Schlüssel für den Schriftartnamen.
     * @param sizeKey Der Schlüssel für die Schriftgröße.
     * @param fallback Die Fallback-Schriftart, die zurückgegeben wird, falls die Werte nicht gefunden oder ungültig sind.
     * @return Die Schriftart aus der Konfiguration oder die Fallback-Schriftart.
     */
    public static Font getFont(String nameKey, String sizeKey, Font fallback) {
        String fontName = props.getProperty(nameKey);
        String fontSizeStr = props.getProperty(sizeKey);

        int size = fallback.getSize();
        if (fontSizeStr != null) {
            try {
                size = Integer.parseInt(fontSizeStr);
            } catch (NumberFormatException e) {
                System.err.println("Ungültige Schriftgröße: " + fontSizeStr);
            }
        }

        return new Font(
                fontName != null ? fontName : fallback.getName(),
                Font.PLAIN,
                size
        );
    }
}
