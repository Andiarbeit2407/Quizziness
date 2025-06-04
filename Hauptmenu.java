import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Eine benutzerdefinierte JButton-Klasse, die abgerundete Ecken hat.
 */
class RoundedButton extends JButton {
    private int radius;

    /**
     * Erstellt einen neuen RoundedButton mit dem angegebenen Text und Radius.
     *
     * @param text Der Text des Buttons
     * @param radius Der Radius der abgerundeten Ecken
     */
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

/**
 * Die Hauptklasse für das Quiz-Hauptmenü.
 * Diese Klasse erstellt das Hauptfenster der Anwendung und verwaltet die Benutzerinteraktionen.
 */
public class Hauptmenu extends JFrame {
    // UI Components
    private JLabel titelLabel;
    private JLabel benutzerLabel;
    private JLabel zeitLabel;
    private JTextArea nachrichtenfeld;

    // Buttons
    private RoundedButton quizButton;
    private RoundedButton frageHinzufuegenButton;
    private RoundedButton frageLoeschenButton;
    private RoundedButton leaderboardButton;

    // Panels
    private JPanel buttonPanel;

    // Services
    private Timer zeitTimer;

    // Constants
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;
    private static final String WINDOW_TITLE = "Quiz Hauptmenü";
    private static final String[] QUIZ_KATEGORIEN = {
            "Lebewesen", "Naturwissenschaften", "Mathematik", "Allgemeinwissen", "Benutzer"
    };
    private static final String INFO_TEXT =
            "Allgemeine Infos: \n Musik aus Hit Indie-Videospiel Undertale \n" +
                    "Unser Programmierteam: \n Ipek, Ekin, Simon, Andreas, Maja, Colin \n" +
                    "P.S. Bei uns gibt's sogar Shortcuts!";

    /**
     * Erstellt ein neues Hauptmenü-Fenster.
     */
    public Hauptmenu() {
        initializeWindow();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupMenuBar();
        setupKeyboardShortcuts();
        finalizeWindow();
    }

    /**
     * Initialisiert das Fenster mit grundlegenden Einstellungen.
     */
    private void initializeWindow() {
        loadCustomCursor();
        setTitle(WINDOW_TITLE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    /**
     * Lädt einen benutzerdefinierten Cursor.
     */
    private void loadCustomCursor() {
        boolean cursorLoaded = CustomCursorManager.loadCursor("cursor.png", 16, 8);
        if (!cursorLoaded) {
            System.out.println("Cursor konnte nicht geladen werden - verwende Standard");
        }
    }

    /**
     * Initialisiert die Komponenten des Fensters.
     */
    private void initializeComponents() {
        createHeaderComponents();
        createMainButtons();
        createUserLabel();
        startTimeUpdater();
    }

    /**
     * Erstellt die Header-Komponenten.
     */
    private void createHeaderComponents() {
        nachrichtenfeld = createInfoTextArea();
        zeitLabel = createTimeLabel();
        aktualisiereZeit();
    }

    /**
     * Erstellt ein Textfeld für die Informationsanzeige.
     *
     * @return Das erstellte JTextArea-Objekt
     */
    private JTextArea createInfoTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.BOLD, 14));
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setWrapStyleWord(false);
        textArea.setLineWrap(false);
        textArea.setText(INFO_TEXT);
        textArea.setPreferredSize(new Dimension(400, 30));
        return textArea;
    }

    /**
     * Erstellt ein Label für die Zeitanzeige.
     *
     * @return Das erstellte JLabel-Objekt
     */
    private JLabel createTimeLabel() {
        JLabel label = new JLabel();
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(400, 30));
        return label;
    }

    /**
     * Erstellt die Hauptbuttons des Menüs.
     */
    private void createMainButtons() {
        quizButton = new RoundedButton("Quiz starten", 30);
        frageHinzufuegenButton = new RoundedButton("Frage hinzufügen", 30);
        frageLoeschenButton = new RoundedButton("Frage löschen", 30);
        leaderboardButton = new RoundedButton("Leaderboard", 30);
    }

    /**
     * Erstellt ein Label für den Benutzer.
     */
    private void createUserLabel() {
        benutzerLabel = new JLabel("Angemeldet als: " + Benutzername.username +
                " | Gesamte Punktzahl: " + Benutzername.points);
        benutzerLabel.setHorizontalAlignment(SwingConstants.LEFT);
        benutzerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
        benutzerLabel.setBackground(StyleManager.getColor("", Color.BLACK));
    }

    /**
     * Startet den Timer für die Zeitaktualisierung.
     */
    private void startTimeUpdater() {
        zeitTimer = new Timer(1000, e -> aktualisiereZeit());
        zeitTimer.start();
    }

    /**
     * Richtet das Layout des Fensters ein.
     */
    private void setupLayout() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createButtonPanel(), BorderLayout.CENTER);
        add(benutzerLabel, BorderLayout.SOUTH);

        // Initialize unused but referenced title label
        titelLabel = new JLabel("Willkommen zum Quiz!", SwingConstants.CENTER);
        titelLabel.setFont(new Font("Arial", Font.BOLD, 20));
    }

    /**
     * Erstellt das Header-Panel.
     *
     * @return Das erstellte JPanel-Objekt
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel logoLabel = createLogoLabel();

        headerPanel.add(nachrichtenfeld, BorderLayout.WEST);
        headerPanel.add(logoLabel, BorderLayout.CENTER);
        headerPanel.add(zeitLabel, BorderLayout.EAST);

        return headerPanel;
    }

    /**
     * Erstellt ein Label für das Logo.
     *
     * @return Das erstellte JLabel-Objekt
     */
    private JLabel createLogoLabel() {
        ImageIcon icon = new ImageIcon(getClass().getResource("Quizziness162.png"));
        Image skaliertesBild = icon.getImage().getScaledInstance(600, 120, Image.SCALE_DEFAULT);
        JLabel bildLabel = new JLabel(new ImageIcon(skaliertesBild));
        bildLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return bildLabel;
    }

    /**
     * Erstellt das Button-Panel.
     *
     * @return Das erstellte JPanel-Objekt
     */
    private JPanel createButtonPanel() {
        buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        buttonPanel.add(quizButton);
        buttonPanel.add(frageHinzufuegenButton);
        buttonPanel.add(frageLoeschenButton);
        buttonPanel.add(leaderboardButton);

        return buttonPanel;
    }

    /**
     * Richtet die Event-Handler für die Komponenten ein.
     */
    private void setupEventHandlers() {
        quizButton.addActionListener(e -> handleQuizStart());
        frageHinzufuegenButton.addActionListener(e -> new QuizEingabe());
        frageLoeschenButton.addActionListener(e -> SwingUtilities.invokeLater(QuizLoeschen::new));
        leaderboardButton.addActionListener(e -> new Leaderboard());

        addWindowListener(new WindowCloseHandler());
        addComponentListener(new ComponentResizeHandler());
    }

    /**
     * Behandelt das Starten eines Quiz.
     */
    private void handleQuizStart() {
        String auswahl = (String) JOptionPane.showInputDialog(
                this,
                "Wähle eine Quiz-Kategorie:",
                "Kategorie auswählen",
                JOptionPane.PLAIN_MESSAGE,
                null,
                QUIZ_KATEGORIEN,
                QUIZ_KATEGORIEN[0]
        );

        if (auswahl != null) {
            dispose();
            createQuizWindow(auswahl);
        }
    }

    /**
     * Erstellt ein Quiz-Fenster basierend auf der ausgewählten Kategorie.
     *
     * @param kategorie Die ausgewählte Quiz-Kategorie
     */
    private void createQuizWindow(String kategorie) {
        switch (kategorie) {
            case "Lebewesen" -> new LebewesenQuiz();
            case "Naturwissenschaften" -> new NaturwissenschaftenQuiz();
            case "Mathematik" -> new MathematikQuiz();
            case "Allgemeinwissen" -> new AllgemeinwissenQuiz();
            case "Benutzer" -> new BenutzerQuiz();
            default -> JOptionPane.showMessageDialog(this, "Unbekannte Kategorie.");
        }
    }

    /**
     * Richtet die Menüleiste ein.
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Create menus
        JMenu profilMenu = createProfilMenu();
        JMenu audioMenu = createAudioMenu();
        JMenu personalisierungMenu = createPersonalisierungMenu();
        JMenu hilfeMenu = createHilfeMenu();

        // Set fonts
        Font menuFont = new Font("Arial", Font.BOLD, 16);
        profilMenu.setFont(menuFont);
        audioMenu.setFont(menuFont);
        personalisierungMenu.setFont(menuFont);
        hilfeMenu.setFont(menuFont);

        // Add to menu bar
        menuBar.add(profilMenu);
        menuBar.add(audioMenu);
        menuBar.add(personalisierungMenu);
        menuBar.add(hilfeMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Erstellt das Profil-Menü.
     *
     * @return Das erstellte JMenu-Objekt
     */
    private JMenu createProfilMenu() {
        JMenu profilMenu = new JMenu("Profil");
        JMenuItem ausloggenItem = new JMenuItem("Ausloggen");
        ausloggenItem.addActionListener(e -> handleLogout());
        ausloggenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        profilMenu.add(ausloggenItem);
        return profilMenu;
    }

    /**
     * Behandelt das Ausloggen des Benutzers.
     */
    private void handleLogout() {
        zeitTimer.stop();
        MusicManager.stopCurrentMusic();
        new Login();
        dispose();
    }

    /**
     * Erstellt das Audio-Menü.
     *
     * @return Das erstellte JMenu-Objekt
     */
    private JMenu createAudioMenu() {
        JMenu audioMenu = new JMenu("Audio");

        JCheckBoxMenuItem musikToggle = new JCheckBoxMenuItem("Musik aktiviert", MusicManager.isMusicEnabled());
        musikToggle.addActionListener(e -> {
            MusicManager.setMusicEnabled(musikToggle.isSelected());
            if (musikToggle.isSelected()) {
                MusicManager.playBackgroundMusic("menu_background");
            }
        });
        musikToggle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

        JMenuItem pauseResumeItem = createPauseResumeItem();
        JMenuItem lautstaerkeItem = createVolumeItem();

        audioMenu.add(musikToggle);
        audioMenu.add(pauseResumeItem);
        audioMenu.addSeparator();
        audioMenu.add(lautstaerkeItem);

        return audioMenu;
    }

    /**
     * Erstellt das Menüelement zum Pausieren und Fortsetzen der Musik.
     *
     * @return Das erstellte JMenuItem-Objekt
     */
    private JMenuItem createPauseResumeItem() {
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
        pauseResumeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        return pauseResumeItem;
    }

    /**
     * Erstellt das Menüelement zum Einstellen der Lautstärke.
     *
     * @return Das erstellte JMenuItem-Objekt
     */
    private JMenuItem createVolumeItem() {
        JMenuItem lautstaerkeItem = new JMenuItem("Lautstärke einstellen...");
        lautstaerkeItem.addActionListener(e -> showVolumeDialog());
        lautstaerkeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        return lautstaerkeItem;
    }

    /**
     * Zeigt einen Dialog zur Einstellung der Lautstärke an.
     */
    private void showVolumeDialog() {
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

        volumeSlider.addChangeListener(e -> {
            float volume = volumeSlider.getValue() / 100f;
            MusicManager.setMasterVolume(volume);
            label.setText("Lautstärke: " + volumeSlider.getValue() + "%");
        });

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> volumeDialog.dispose());

        panel.add(label, BorderLayout.NORTH);
        panel.add(volumeSlider, BorderLayout.CENTER);
        panel.add(okButton, BorderLayout.SOUTH);

        volumeDialog.add(panel);
        volumeDialog.setVisible(true);
    }

    /**
     * Erstellt das Personalisierungsmenü.
     *
     * @return Das erstellte JMenu-Objekt
     */
    private JMenu createPersonalisierungMenu() {
        JMenu personalisierungMenu = new JMenu("Personalisierung");

        JMenuItem farbschemaItem = createColorSchemeItem();
        JMenuItem textfarbeItem = createTextColorItem();

        personalisierungMenu.add(farbschemaItem);
        personalisierungMenu.add(textfarbeItem);

        return personalisierungMenu;
    }

    /**
     * Erstellt das Menüelement zum Ändern des Farbschemas.
     *
     * @return Das erstellte JMenuItem-Objekt
     */
    private JMenuItem createColorSchemeItem() {
        JMenuItem farbschemaItem = new JMenuItem("Farbschema ändern...");
        farbschemaItem.addActionListener(e -> handleColorSchemeChange());
        farbschemaItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        return farbschemaItem;
    }

    /**
     * Behandelt die Änderung des Farbschemas.
     */
    private void handleColorSchemeChange() {
        Color neuePrimary = JColorChooser.showDialog(this, "Wähle Primary-Farbe",
                StyleManager.getColor("primary.color", Color.BLUE));
        if (neuePrimary != null) {
            String hexPrimary = String.format("#%02x%02x%02x", neuePrimary.getRed(),
                    neuePrimary.getGreen(), neuePrimary.getBlue());
            StyleManager.setColor("primary.color", hexPrimary);
        }

        Color neueSecondary = JColorChooser.showDialog(this, "Wähle Secondary-Farbe",
                StyleManager.getColor("secondary.color", Color.MAGENTA));
        if (neueSecondary != null) {
            String hexSecondary = String.format("#%02x%02x%02x", neueSecondary.getRed(),
                    neueSecondary.getGreen(), neueSecondary.getBlue());
            StyleManager.setColor("secondary.color", hexSecondary);
        }

        aktualisiereFarben();
    }

    /**
     * Erstellt das Menüelement zum Ändern der Textfarbe.
     *
     * @return Das erstellte JMenuItem-Objekt
     */
    private JMenuItem createTextColorItem() {
        JMenuItem textfarbeItem = new JMenuItem("Textfarbe wählen...");
        textfarbeItem.addActionListener(e -> handleTextColorChange());
        textfarbeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        return textfarbeItem;
    }

    /**
     * Behandelt die Änderung der Textfarbe.
     */
    private void handleTextColorChange() {
        Object[] optionen = {"Weiß (#FFFFFF)", "Schwarz (#000000)"};
        String auswahl = (String) JOptionPane.showInputDialog(
                this, "Wähle eine Textfarbe:", "Textfarbe",
                JOptionPane.PLAIN_MESSAGE, null, optionen, optionen[0]
        );
        if (auswahl != null) {
            String hex = auswahl.contains("Weiß") ? "#FFFFFF" : "#000000";
            StyleManager.setColor("fixedfont.color", hex);
            aktualisiereTextfarben();
        }
    }

    /**
     * Erstellt das Hilfemenü.
     *
     * @return Das erstellte JMenu-Objekt
     */
    private JMenu createHilfeMenu() {
        JMenu hilfeMenu = new JMenu("Hilfe");
        JMenuItem shortcutItem = createShortcutItem();
        hilfeMenu.add(shortcutItem);
        return hilfeMenu;
    }

    /**
     * Erstellt das Menüelement zum Anzeigen der Shortcuts.
     *
     * @return Das erstellte JMenuItem-Objekt
     */
    private JMenuItem createShortcutItem() {
        JMenuItem shortcutItem = new JMenuItem("Shortcuts...");
        shortcutItem.addActionListener(e -> openShortcutFile());
        shortcutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        return shortcutItem;
    }

    /**
     * Öffnet die Shortcut-Datei.
     */
    private void openShortcutFile() {
        try {
            File file = new File("shortcuts.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            Desktop.getDesktop().open(file);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Datei konnte nicht geöffnet werden!",
                    "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Richtet die Tastaturkürzel ein.
     */
    private void setupKeyboardShortcuts() {
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        setupQuizShortcuts(im, am);
        setupFunctionShortcuts(im, am);
    }

    /**
     * Richtet die Tastaturkürzel für das Quiz ein.
     *
     * @param im Die InputMap
     * @param am Die ActionMap
     */
    private void setupQuizShortcuts(InputMap im, ActionMap am) {
        // Quiz shortcuts
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), "quiz");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "quiz");
        am.put("quiz", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                quizButton.doClick();
            }
        });
    }

    /**
     * Richtet die Tastaturkürzel für die Funktionen ein.
     *
     * @param im Die InputMap
     * @param am Die ActionMap
     */
    private void setupFunctionShortcuts(InputMap im, ActionMap am) {
        // Add question shortcuts
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), "hinzufuegen");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0), "hinzufuegen");
        am.put("hinzufuegen", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                frageHinzufuegenButton.doClick();
            }
        });

        // Delete question shortcuts
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0), "loeschen");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "loeschen");
        am.put("loeschen", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                frageLoeschenButton.doClick();
            }
        });

        // Leaderboard shortcuts
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0), "leaderboard");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0), "leaderboard");
        am.put("leaderboard", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                leaderboardButton.doClick();
            }
        });
    }

    /**
     * Finalisiert das Fenster und zeigt es an.
     */
    private void finalizeWindow() {
        applyStyling();
        initializeMusic();

        if (CustomCursorManager.isLoaded()) {
            CustomCursorManager.setCursorEverywhere();
        }

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        aktualisiereFonts();
    }

    /**
     * Wendet die Stile auf die Komponenten an.
     */
    private void applyStyling() {
        buttonPanel.setBackground(StyleManager.getColor("primary.color", Color.WHITE));
        Color buttonAndTextBg = StyleManager.getColor("secondary.color", Color.LIGHT_GRAY);
        Color textColor = StyleManager.getColor("fixedfont.color", Color.WHITE);

        for (Component comp : buttonPanel.getComponents()) {
            if (comp instanceof JButton || comp instanceof JTextField) {
                comp.setBackground(buttonAndTextBg);
                comp.setForeground(textColor);
            }
        }

        aktualisiereTextfarben();
    }

    /**
     * Initialisiert die Musik.
     */
    private void initializeMusic() {
        MusicManager.initializeMusic();
        if (MusicManager.isMusicEnabled()) {
            MusicManager.playBackgroundMusic("menu_background");
        }
    }

    /**
     * Aktualisiert die Zeitanzeige.
     */
    private void aktualisiereZeit() {
        LocalTime aktuelleZeit = LocalTime.now();
        String zeitString = aktuelleZeit.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        zeitLabel.setText("      " + zeitString);
    }

    /**
     * Aktualisiert die Schriftarten basierend auf der Fenstergröße.
     */
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

        int headerFontSize = Math.round(14 * faktor);
        nachrichtenfeld.setFont(new Font("Arial", Font.PLAIN, 20));
        zeitLabel.setFont(new Font("Arial", Font.PLAIN, 35));
    }

    /**
     * Aktualisiert die Farben der Komponenten.
     */
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

        nachrichtenfeld.setBackground(Color.decode("#EEEEEE"));
        nachrichtenfeld.setForeground(Color.BLACK);
        zeitLabel.setBackground(secondary);
        zeitLabel.setForeground(Color.BLACK);
    }

    /**
     * Aktualisiert die Textfarben der Komponenten.
     */
    private void aktualisiereTextfarben() {
        Color textColor = StyleManager.getColor("fixedfont.color", Color.WHITE);
        titelLabel.setForeground(textColor);
        quizButton.setForeground(textColor);
        frageHinzufuegenButton.setForeground(textColor);
        frageLoeschenButton.setForeground(textColor);
        leaderboardButton.setForeground(textColor);
        nachrichtenfeld.setBackground(StyleManager.getColor("secondary.color", Color.LIGHT_GRAY));
    }

    /**
     * Die Hauptmethode, die die Anwendung startet.
     *
     * @param args Die Befehlszeilenargumente
     */
    public static void main(String[] args) {
        StyleManager.loadConfig("config.properties");
        SwingUtilities.invokeLater(Hauptmenu::new);
    }

    /**
     * Ein Handler für das Schließen des Fensters.
     */
    private class WindowCloseHandler extends java.awt.event.WindowAdapter {
        @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            zeitTimer.stop();
            MusicManager.stopCurrentMusic();
            System.exit(0);
        }
    }

    /**
     * Ein Handler für das Ändern der Fenstergröße.
     */
    private class ComponentResizeHandler extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {
            aktualisiereFonts();
        }
    }
}
