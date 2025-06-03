import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
    private RoundedButton leaderboardButton;
    private JTextArea nachrichtenfeld;
    private JLabel zeitLabel;
    private Timer zeitTimer;

    public Hauptmenu() {

        boolean cursorLoaded = CustomCursorManager.loadCursor("cursor.png", 16, 8);
        if (!cursorLoaded) {
            System.out.println("Cursor konnte nicht geladen werden - verwende Standard");
        }

        setTitle("Quiz Hauptmenü");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel mit drei Bereichen erstellen
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Linkes Nachrichtenfeld (ohne Scroll)
        nachrichtenfeld = new JTextArea();
        nachrichtenfeld.setFont(new Font("Arial", Font.BOLD, 14));
        nachrichtenfeld.setEditable(false);
        nachrichtenfeld.setOpaque(false);
        nachrichtenfeld.setWrapStyleWord(false);
        nachrichtenfeld.setLineWrap(false);
        nachrichtenfeld.setText("Allgemeine Infos: \n Musik aus Hit Indie-Videospiel Undertale \n Unser Programmierteam: \n Ipek, Ekin, Simon, Andreas, Maja, Colin \nP.S. Bei uns gibt's sogar Shortcuts!");
        nachrichtenfeld.setPreferredSize(new Dimension(400, 30));



        // Logo in der Mitte
        ImageIcon icon = new ImageIcon(getClass().getResource("Quizziness162.png"));
        Image skaliertesBild = icon.getImage().getScaledInstance(600, 120, Image.SCALE_DEFAULT);
        JLabel bildLabel = new JLabel(new ImageIcon(skaliertesBild));
        bildLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Zeit-Label rechts
        zeitLabel = new JLabel();
        zeitLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        zeitLabel.setHorizontalAlignment(SwingConstants.CENTER);
        zeitLabel.setPreferredSize(new Dimension(400, 30));
        aktualisiereZeit();

        // Timer für Zeit-Updates (jede Sekunde)
        zeitTimer = new Timer(1000, e -> aktualisiereZeit());
        zeitTimer.start();

        headerPanel.add(nachrichtenfeld, BorderLayout.WEST);
        headerPanel.add(bildLabel, BorderLayout.CENTER);
        headerPanel.add(zeitLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        titelLabel = new JLabel("Willkommen zum Quiz!", SwingConstants.CENTER);
        titelLabel.setFont(new Font("Arial", Font.BOLD, 20));
        // Titel-Label nicht mehr hinzufügen, da wir das Header-Panel haben

        benutzerLabel = new JLabel("Angemeldet als: " + Benutzername.username + " | Gesamte Punktzahl: " + Benutzername.points);
        benutzerLabel.setHorizontalAlignment(SwingConstants.LEFT);
        benutzerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
        add(benutzerLabel, BorderLayout.SOUTH);
        benutzerLabel.setBackground((StyleManager.getColor("", Color.BLACK)));

        quizButton = new RoundedButton("Quiz starten", 30);
        frageHinzufuegenButton = new RoundedButton("Frage hinzufügen", 30);
        frageLoeschenButton = new RoundedButton("Frage löschen", 30);
        leaderboardButton = new RoundedButton("Leaderboard", 30);

        buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        buttonPanel.add(quizButton);
        buttonPanel.add(frageHinzufuegenButton);
        buttonPanel.add(frageLoeschenButton);
        buttonPanel.add(leaderboardButton);
        add(buttonPanel, BorderLayout.CENTER);

        quizButton.addActionListener(e -> {
            String[] kategorien = {"Lebewesen", "Naturwissenschaften", "Mathematik", "Allgemeinwissen", "Benutzer"};
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
                    case "Lebewesen" -> new LebewesenQuiz();
                    case "Naturwissenschaften" -> new NaturwissenschaftenQuiz();
                    case "Mathematik" -> new MathematikQuiz();
                    case "Allgemeinwissen" -> new AllgemeinwissenQuiz();
                    case "Benutzer" -> new BenutzerQuiz();
                    default -> JOptionPane.showMessageDialog(Hauptmenu.this, "Unbekannte Kategorie.");
                }
            }
        });

        frageHinzufuegenButton.addActionListener(e -> {
            new QuizEingabe();
        });

        frageLoeschenButton.addActionListener(e -> {
            SwingUtilities.invokeLater(QuizLoeschen::new);
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu personalisierungMenu = new JMenu("Personalisierung");
        JMenu audioMenu = new JMenu("Audio");

        JCheckBoxMenuItem musikToggle = new JCheckBoxMenuItem("Musik aktiviert", MusicManager.isMusicEnabled());
        musikToggle.addActionListener(e -> {
            MusicManager.setMusicEnabled(musikToggle.isSelected());
            if (musikToggle.isSelected()) {
                MusicManager.playBackgroundMusic("menu_background");
            }
        });

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

        leaderboardButton.addActionListener(e -> {
            new Leaderboard();
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
                    file.createNewFile();
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
            zeitTimer.stop(); // Timer stoppen beim Ausloggen
            MusicManager.stopCurrentMusic();
            new Login();
            dispose();
        });

        Font menuFont = new Font("Arial", Font.BOLD, 16);
        personalisierungMenu.setFont(menuFont);
        audioMenu.setFont(menuFont);
        hilfeMenu.setFont(menuFont);
        profilMenu.setFont(menuFont);

        shortcutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        farbschemaItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        textfarbeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        ausloggenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

        hilfeMenu.add(shortcutItem);
        personalisierungMenu.add(farbschemaItem);
        profilMenu.add(ausloggenItem);
        audioMenu.add(musikToggle);
        audioMenu.add(pauseResumeItem);
        audioMenu.addSeparator();
        audioMenu.add(lautstaerkeItem);
        menuBar.add(profilMenu);
        menuBar.add(audioMenu);
        menuBar.add(personalisierungMenu);
        menuBar.add(hilfeMenu);
        personalisierungMenu.add(textfarbeItem);
        setJMenuBar(menuBar);

        buttonPanel.setBackground(StyleManager.getColor("primary.color", Color.WHITE));
        Color buttonAndTextBg = StyleManager.getColor("secondary.color", Color.LIGHT_GRAY);
        Color textColor = StyleManager.getColor("fixedfont.color", Color.WHITE);
        for (Component comp : buttonPanel.getComponents()) {
            if (comp instanceof JButton || comp instanceof JTextField) {
                comp.setBackground(buttonAndTextBg);
                comp.setForeground(textColor);
            }
        }

        MusicManager.initializeMusic();
        if (MusicManager.isMusicEnabled()) {
            MusicManager.playBackgroundMusic("menu_background");
        }

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                zeitTimer.stop(); // Timer stoppen beim Schließen
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

        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), "quiz");
        am.put("quiz", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                quizButton.doClick();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "quiz");

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), "hinzufuegen");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0), "hinzufuegen");
        am.put("hinzufuegen", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                frageHinzufuegenButton.doClick();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0), "loeschen");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "loeschen");
        am.put("loeschen", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                frageLoeschenButton.doClick();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0), "leaderboard");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0), "leaderboard");
        am.put("leaderboard", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                leaderboardButton.doClick();
            }
        });

        if (CustomCursorManager.isLoaded()) {
            CustomCursorManager.setCursorEverywhere();
        }

        setVisible(true);
        aktualisiereFonts();
    }

    private void aktualisiereZeit() {
        LocalTime aktuelleZeit = LocalTime.now();
        String zeitString = aktuelleZeit.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        zeitLabel.setText("      " + zeitString);
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
        leaderboardButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));

        // Schriftgrößen für Header-Elemente anpassen
        int headerFontSize = Math.round(14 * faktor);
        nachrichtenfeld.setFont(new Font("Arial", Font.PLAIN, 20));
        zeitLabel.setFont(new Font("Arial", Font.PLAIN, 35));
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

        // Header-Elemente färben
        nachrichtenfeld.setBackground(Color.decode("#EEEEEE"));
        nachrichtenfeld.setForeground(Color.BLACK);
        zeitLabel.setBackground(secondary);
        zeitLabel.setForeground(Color.BLACK);
    }

    private void aktualisiereTextfarben() {
        Color textColor = StyleManager.getColor("fixedfont.color", Color.WHITE);
        titelLabel.setForeground(textColor);
        quizButton.setForeground(textColor);
        frageHinzufuegenButton.setForeground(textColor);
        frageLoeschenButton.setForeground(textColor);
        leaderboardButton.setForeground(textColor);
        nachrichtenfeld.setBackground(StyleManager.getColor("secondary.color", Color.LIGHT_GRAY));
    }

    public static void main(String[] args) {
        StyleManager.loadConfig("config.properties");
        SwingUtilities.invokeLater(Hauptmenu::new);
    }
}