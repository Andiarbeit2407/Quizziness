import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RounderButton extends JButton {
    private int radius;

    public RounderButton(String text, int radius) {
        super(text);
        this.radius = radius;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 15));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Hintergrundfarbe des Buttons
        if (getModel().isPressed()) {
            g2.setColor(getBackground().darker());
        } else if (getModel().isRollover()) {
            g2.setColor(getBackground().brighter());
        } else {
            g2.setColor(getBackground());
        }

        // Abgerundetes Rechteck malen
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Text malen
        FontMetrics fm = g2.getFontMetrics();
        Rectangle r = getBounds();
        String text = getText();
        int x = (r.width - fm.stringWidth(text)) / 2;
        int y = (r.height + fm.getAscent() - fm.getDescent()) / 2;

        g2.setColor(getForeground());
        g2.setFont(getFont());
        g2.drawString(text, x, y);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
    }
}
