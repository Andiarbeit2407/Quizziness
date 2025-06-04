import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Eine benutzerdefinierte JButton-Komponente mit abgerundeten Ecken.
 */
public class RounderButton extends JButton {
    private int radius; // Der Radius für die abgerundeten Ecken des Buttons

    /**
     * Erstellt einen neuen RounderButton mit dem angegebenen Text und Radius für die abgerundeten Ecken.
     *
     * @param text Der Text, der auf dem Button angezeigt wird.
     * @param radius Der Radius für die abgerundeten Ecken des Buttons.
     */
    public RounderButton(String text, int radius) {
        super(text);
        this.radius = radius;
        setContentAreaFilled(false); // Deaktiviert das Füllen des Inhaltsbereichs
        setFocusPainted(false); // Deaktiviert die Fokus-Markierung
        setBorderPainted(false); // Deaktiviert die Rand-Markierung
        setOpaque(false); // Macht den Button transparent
        setForeground(Color.WHITE); // Setzt die Textfarbe auf Weiß
        setFont(new Font("Arial", Font.BOLD, 15)); // Setzt die Schriftart und -größe
    }

    /**
     * Zeichnet die Komponente mit abgerundeten Ecken und dem Button-Text.
     *
     * @param g Das Graphics-Objekt, das zum Zeichnen verwendet wird.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Hintergrundfarbe des Buttons basierend auf dem Zustand setzen
        if (getModel().isPressed()) {
            g2.setColor(getBackground().darker()); // Dunkler bei gedrücktem Zustand
        } else if (getModel().isRollover()) {
            g2.setColor(getBackground().brighter()); // Heller bei Mausüberfahrt
        } else {
            g2.setColor(getBackground()); // Standard-Hintergrundfarbe
        }

        // Zeichnet ein abgerundetes Rechteck als Hintergrund
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Zeichnet den Text zentriert auf dem Button
        FontMetrics fm = g2.getFontMetrics();
        Rectangle r = getBounds();
        String text = getText();
        int x = (r.width - fm.stringWidth(text)) / 2; // Zentriert den Text horizontal
        int y = (r.height + fm.getAscent() - fm.getDescent()) / 2; // Zentriert den Text vertikal

        g2.setColor(getForeground()); // Setzt die Farbe für den Text
        g2.setFont(getFont()); // Setzt die Schriftart für den Text
        g2.drawString(text, x, y); // Zeichnet den Text

        g2.dispose(); // Gibt die Ressourcen des Graphics2D-Objekts frei
    }

    /**
     * Zeichnet den Rand des Buttons mit abgerundeten Ecken.
     *
     * @param g Das Graphics-Objekt, das zum Zeichnen verwendet wird.
     */
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground()); // Setzt die Farbe für den Rand
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius); // Zeichnet den abgerundeten Rand
        g2.dispose(); // Gibt die Ressourcen des Graphics2D-Objekts frei
    }
}
