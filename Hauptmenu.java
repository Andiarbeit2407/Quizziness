import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Hauptmenu extends JFrame {
    private JLabel titelLabel;
    private JLabel benutzerLabel;
    private RoundedButton quizButton;
    private RoundedButton frageHinzufuegenButton;
    private JPanel buttonPanel;

    public Hauptmenu() {
        setTitle("Quiz Hauptmenü");
        setSize(800, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        titelLabel = new JLabel("Willkommen zum Quiz!", SwingConstants.CENTER);
        titelLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titelLabel, BorderLayout.NORTH);

        benutzerLabel = new JLabel("Angemeldet als: " + Benutzername.username);
        benutzerLabel.setHorizontalAlignment(SwingConstants.LEFT);
        benutzerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
        add(benutzerLabel, BorderLayout.SOUTH);

        quizButton = new RoundedButton("Quiz starten", 30);
        frageHinzufuegenButton = new RoundedButton("Frage hinzufügen", 30);

        buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        buttonPanel.add(quizButton);
        buttonPanel.add(frageHinzufuegenButton);
        add(buttonPanel, BorderLayout.CENTER);

        // Quiz Button mit Kategorieauswahl
        quizButton.addActionListener(e -> {
            String[] kategorien = {"Mathe", "Geschichte", "Technik"};
            String auswahl = (String) JOptionPane.showInputDialog(
                    Hauptmenu.this,
                    "Wähle eine Quiz-Kategorie:",
                    "Kategorie auswählen",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    kategorien,
                    kategorien[0]);
            if (auswahl != null) {
                dispose();
                switch (auswahl) {
                    case "Mathe" -> new MatheQuiz();
                    case "Geschichte" -> new GeschichteQuiz();
                    case "Technik" -> new TechnikQuiz();
                    default -> JOptionPane.showMessageDialog(Hauptmenu.this, "Unbekannte Kategorie.");
                }
            }
        });

        // Frage hinzufügen Button
        frageHinzufuegenButton.addActionListener(e -> {
            new QuizEingabe();
            dispose();
        });

        // Menüleiste mit Profil, Personaliserung und Hilfe
        JMenuBar menuBar = new JMenuBar();

        // Profil-Menü mit Logout
        JMenu profilMenu = new JMenu("Profil");
        JMenuItem ausloggenItem = new JMenuItem("Ausloggen");
        ausloggenItem.addActionListener(e -> {
            new Login();
            dispose();
        });
        ausloggenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        profilMenu.add(ausloggenItem);
        menuBar.add(profilMenu);

        // Personaliserung-Menü mit Farbwahl
        JMenu personalisierungMenu = new JMenu("Personalisierung");

        JMenuItem farbschemaItem = new JMenuItem("Farbschema ändern...");
        farbschemaItem.addActionListener(e -> {
            Color neuePrimary = JColorChooser.showDialog(this, "Wähle Primary-Farbe", StyleManager.getColor("primary.color", Color.BLUE));
            if (neuePrimary != null) {
                String hexPrimary = String.format("#%02x%02x%02x", neuePrimary.getRed(), neuePrimary.getGreen(), neuePrimary.getBlue());
                StyleManager.setColor("primary.color", hexPrimary);
            }
            Color neueSecondary = JColorChooser.showDialog(this, "Wähle Secondary-Farbe", StyleManager.getColor("secondary.color", Color.MAGENTA));
            if (neueSecondary != null) {
                String hexSecondary = String.format("#%02x%02x%02x", neueSecondary.getRed(), neueSecondary.getGreen(), neueSecondary.getBlue());
                StyleManager.setColor("secondary.color", hexSecondary);
            }
            aktualisiereFarben();
        });
        farbschemaItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

        JMenuItem textfarbeItem = new JMenuItem("Textfarbe wählen...");
        textfarbeItem.addActionListener(e -> {
            Object[] optionen = {"Weiß (#FFFFFF)", "Schwarz (#000000)"};
            String auswahl = (String) JOptionPane.showInputDialog(
                    this,
                    "Wähle eine Textfarbe:",
                    "Textfarbe",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    optionen,
                    optionen[0]
            );
            if (auswahl != null) {
                String hex = auswahl.contains("Weiß") ? "#FFFFFF" : "#000000";
                StyleManager.setColor("fixedfont.color", hex);
                aktualisiereTextfarben();
            }
        });
        textfarbeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

        personalisierungMenu.add(farbschemaItem);
        personalisierungMenu.add(textfarbeItem);
        menuBar.add(personalisierungMenu);

        // Hilfe-Menü mit Shortcut-Öffner
        JMenu hilfeMenu = new JMenu("Hilfe");
        JMenuItem shortcutItem = new JMenuItem("Shortcuts...");
        shortcutItem.addActionListener(e -> {
            try {
                File file = new File("shortcuts.txt");
                if (!file.exists()) file.createNewFile();
                Desktop.getDesktop().open(file);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Datei konnte nicht geöffnet werden!", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });
        shortcutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        hilfeMenu.add(shortcutItem);
        menuBar.add(hilfeMenu);

        setJMenuBar(menuBar);

        // Bild oben
        ImageIcon icon = new ImageIcon("Quizziness162.png");
        Image skaliertesBild = icon.getImage().getScaledInstance(800, 160, Image.SCALE_DEFAULT);
        JLabel bildLabel = new JLabel(new ImageIcon(skaliertesBild));
        bildLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(bildLabel, BorderLayout.BEFORE_FIRST_LINE);

        aktualisiereFarben();
        aktualisiereTextfarben();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                aktualisiereFonts();
            }
        });

        // Tastenkürzel für Buttons
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_1, KeyEvent.VK_Q -> quizButton.doClick();
                    case KeyEvent.VK_2, KeyEvent.VK_F -> frageHinzufuegenButton.doClick();
                }
            }
        });

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        aktualisiereFonts();
    }

    private void aktualisiereFonts() {
        int breite = getWidth();
        float faktor = breite / 500.0f;

        int titelFontSize = Math.round(20 * faktor);
        int labelFontSize = Math.round(12 * faktor);
        int buttonFontSize = Math.round(15 * faktor);

        titelLabel.setFont(new Font("Arial", Font.BOLD, titelFontSize));
        benutzerLabel.setFont(new Font("Arial", Font.BOLD, labelFontSize));
        quizButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
        frageHinzufuegenButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
    }

    private void aktualisiereFarben() {
        Color primary = StyleManager.getColor("primary.color", Color.BLUE);
        Color secondary = StyleManager.getColor("secondary.color", Color.MAGENTA);
        Color text = StyleManager.getColor("fixedfont.color", Color.WHITE);

        buttonPanel.setBackground(primary);
        for (Component comp : buttonPanel.getComponents()) {
            if (comp instanceof JButton) {
                comp.setBackground(secondary);
                comp.setForeground(text);
            }
        }
    }

    private void aktualisiereTextfarben() {
        Color textColor = StyleManager.getColor("fixedfont.color", Color.WHITE);
        titelLabel.setForeground(textColor);
        benutzerLabel.setForeground(textColor);
        quizButton.setForeground(textColor);
        frageHinzufuegenButton.setForeground(textColor);
    }

    public static void main(String[] args) {
        StyleManager.loadConfig("config.properties");
        SwingUtilities.invokeLater(Hauptmenu::new);
    }
}

// Runde Button Klasse unverändert übernehmen
class RoundedButton extends JButton {
    private int radius;

    public RoundedButton(String text, int radius) {
        super(text);
        this.radius = radius;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    public void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground());
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
        g2.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        Shape shape = new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius);
        return shape.contains(x, y);
    }
}
