import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class StyleManager {
    private static Properties props = new Properties();

    // Loads the properties file
    public static void loadConfig(String path) {
        try (FileInputStream fis = new FileInputStream(path)) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println("Konnte Konfigurationsdatei nicht laden: " + path);
            e.printStackTrace();
        }
    }

    // Returns a color from the config or a fallback if not found or invalid
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

    // Returns a font using name and size keys, or fallback if missing/invalid
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
