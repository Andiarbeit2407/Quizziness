import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

// Runde Button-Klasse
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

// Hauptmenu-Klasse
public class Hauptmenu extends JFrame {

    private JLabel titelLabel;
    private JLabel benutzerLabel;
    private RoundedButton quizButton;
    private RoundedButton frageHinzufuegenButton;
    private RoundedButton frageLoeschenButton;
    private JPanel buttonPanel;

    public Hauptmenu() {
        setTitle("Quiz Hauptmenü");
        setSize(800, 800);
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
        benutzerLabel.setBackground((StyleManager.getColor("", Color.BLACK)));

        quizButton = new RoundedButton("Quiz starten", 30);
        frageHinzufuegenButton = new RoundedButton("Frage hinzufügen", 30);
        frageLoeschenButton = new RoundedButton("Frage löschen", 30);

        buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        buttonPanel.add(quizButton);
        buttonPanel.add(frageHinzufuegenButton);
        buttonPanel.add(frageLoeschenButton);
        add(buttonPanel, BorderLayout.CENTER);

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

        frageHinzufuegenButton.addActionListener(e -> {
            new QuizEingabe();
        });

        frageLoeschenButton.addActionListener(e -> {
            SwingUtilities.invokeLater(QuizLoeschen::new); // Öffnet das Fenster zum Löschen von Fragen
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu personalisierungMenu = new JMenu("Personalisierung");
        // Audio-Menü erstellen
        JMenu audioMenu = new JMenu("Audio");

        // Musik Ein/Aus Toggle
        JCheckBoxMenuItem musikToggle = new JCheckBoxMenuItem("Musik aktiviert", MusicManager.isMusicEnabled());
        musikToggle.addActionListener(e -> {
            MusicManager.setMusicEnabled(musikToggle.isSelected());
            if (musikToggle.isSelected()) {
                MusicManager.playBackgroundMusic("menu_background");
            }
        });

        // Pause/Resume Button
        JMenuItem pauseResumeItem = new JMenuItem("Musik pausieren");
        pauseResumeItem.addActionListener(e -> {
            if (MusicManager.isCurrentlyPlaying()) {
                MusicManager.pauseCurrentMusic();
                pauseResumeItem.setText("Musik fortsetzen");
            } else {
                MusicManager.resumeCurrentMusic();
                pauseResumeItem.setText("Musik pausieren");
            }
        });

        // Lautstärke-Dialog
        JMenuItem lautstaerkeItem = new JMenuItem("Lautstärke einstellen...");
        lautstaerkeItem.addActionListener(e -> {
            JDialog volumeDialog = new JDialog(this, "Lautstärke", true);
            volumeDialog.setSize(300, 150);
            volumeDialog.setLocationRelativeTo(this);

            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel label = new JLabel("Lautstärke: " + Math.round(MusicManager.getMasterVolume() * 100) + "%");
            label.setHorizontalAlignment(SwingConstants.CENTER);

            JSlider volumeSlider = new JSlider(0, 100, Math.round(MusicManager.getMasterVolume() * 100));
            volumeSlider.setMajorTickSpacing(25);
            volumeSlider.setMinorTickSpacing(5);
            volumeSlider.setPaintTicks(true);
            volumeSlider.setPaintLabels(true);

            volumeSlider.addChangeListener(e2 -> {
                float volume = volumeSlider.getValue() / 100f;
                MusicManager.setMasterVolume(volume);
                label.setText("Lautstärke: " + volumeSlider.getValue() + "%");
            });

            JButton okButton = new JButton("OK");
            okButton.addActionListener(e3 -> volumeDialog.dispose());

            panel.add(label, BorderLayout.NORTH);
            panel.add(volumeSlider, BorderLayout.CENTER);
            panel.add(okButton, BorderLayout.SOUTH);

            volumeDialog.add(panel);
            volumeDialog.setVisible(true);
        });

        // Shortcuts für Audio-Menü Items
        musikToggle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        pauseResumeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        lautstaerkeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

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

        JMenu hilfeMenu = new JMenu("Hilfe");

        JMenuItem shortcutItem = new JMenuItem("Shortcuts...");
        shortcutItem.addActionListener(e -> {
            try {
                File file = new File("shortcuts.txt");
                if (!file.exists()) {
                    file.createNewFile(); // optional: Datei anlegen, falls nicht vorhanden
                }
                Desktop.getDesktop().open(file);
            } catch (Exception ex) {
                ex.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null, "Datei konnte nicht geöffnet werden!", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        JMenu profilMenu = new JMenu("Profil");
        JMenuItem ausloggenItem = new JMenuItem("Ausloggen");
        ausloggenItem.addActionListener(e -> {
            MusicManager.stopCurrentMusic(); // Musik stoppen
            new Login();
            dispose();
        });

        shortcutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        farbschemaItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        textfarbeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        ausloggenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

        hilfeMenu.add(shortcutItem);
        personalisierungMenu.add(farbschemaItem);
        profilMenu.add(ausloggenItem);
        // Items zum Menü hinzufügen
        audioMenu.add(musikToggle);
        audioMenu.add(pauseResumeItem);
        audioMenu.addSeparator();
        audioMenu.add(lautstaerkeItem);
        // Audio-Menü zur Menüleiste hinzufügen (nach Profil, vor Personalisierung z. B.)
        menuBar.add(profilMenu);
        menuBar.add(audioMenu);  // ← hier zwischen Profil und Personalisierung
        menuBar.add(personalisierungMenu);
        menuBar.add(hilfeMenu);
        personalisierungMenu.add(textfarbeItem);
        setJMenuBar(menuBar);

        ImageIcon icon = new ImageIcon("Quizziness162.png");
        Image skaliertesBild = icon.getImage().getScaledInstance(800, 160, Image.SCALE_DEFAULT);
        JLabel bildLabel = new JLabel(new ImageIcon(skaliertesBild));
        bildLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(bildLabel, BorderLayout.BEFORE_FIRST_LINE);

        buttonPanel.setBackground(StyleManager.getColor("primary.color", Color.WHITE));
        Color buttonAndTextBg = StyleManager.getColor("secondary.color", Color.LIGHT_GRAY);
        Color textColor = StyleManager.getColor("fixedfont.color", Color.WHITE);
        for (Component comp : buttonPanel.getComponents()) {
            if (comp instanceof JButton || comp instanceof JTextField) {
                comp.setBackground(buttonAndTextBg);
                comp.setForeground(textColor);
            }
        }
        // Musik beim Start initialisieren und abspielen
        MusicManager.initializeMusic();
        if (MusicManager.isMusicEnabled()) {
            MusicManager.playBackgroundMusic("menu_background");
        }

        // Beim Schließen Musik stoppen
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                MusicManager.stopCurrentMusic();
                System.exit(0);
            }
        });

        aktualisiereTextfarben();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                aktualisiereFonts();
            }
        });

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Tastenkürzel hinzufügen
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_1, KeyEvent.VK_Q -> quizButton.doClick(); // 1 für Quiz starten
                    case KeyEvent.VK_2, KeyEvent.VK_F -> frageHinzufuegenButton.doClick(); // 2 für Frage hinzufügen
                    case KeyEvent.VK_3, KeyEvent.VK_L -> frageLoeschenButton.doClick(); // 3 für Frage löschen
                }
            }
        });

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
        frageLoeschenButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
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
        quizButton.setForeground(textColor);
        frageHinzufuegenButton.setForeground(textColor);
        frageLoeschenButton.setForeground(textColor);
    }

    public static void main(String[] args) {
        StyleManager.loadConfig("config.properties");
        SwingUtilities.invokeLater(Hauptmenu::new);
    }
}
