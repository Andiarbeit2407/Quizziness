import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Abstrakte Basisklasse für ein Quiz-Spiel.
 * Diese Klasse stellt die grundlegende Funktionalität und Benutzeroberfläche für ein Quiz bereit.
 */
public abstract class Quiz extends JFrame {

    // Spielzustand
    /** Liste aller Fragen für das Quiz */
    protected List<QuizDaten> fragenListe;
    /** Index der aktuellen Frage */
    protected int aktuelleFrageIndex = 0;
    /** Aktuelle Punkte des Spielers */
    protected int punkte = 0;
    /** Gesamtpunkte, die möglich sind */
    protected int gesamtPunkte = 0;

    // Timer-Komponenten
    /** Verbleibende Zeit für die aktuelle Frage */
    protected int timeRemaining;
    /** Timer für die Fragenzeit */
    protected Timer timer;
    /** Fortschrittsbalken für den Timer */
    private JProgressBar timerBar;

    // UI-Komponenten
    /** Textbereich für die Anzeige der Frage */
    private JTextArea frageFeld;
    /** Buttons für die Antwortmöglichkeiten */
    private JButton[] antwortButtons;
    /** Button zum Zurückkehren zum Hauptmenü */
    private JButton hauptmenuButton;
    /** Button zum Überspringen der aktuellen Frage */
    private JButton frageUeberspringenButton;
    /** Label für Statusinformationen */
    private JLabel statusLabel;
    /** Hauptpanel des Quiz */
    private JPanel mainPanel;

    // UI-Konstanten
    /** Breite des Fensters */
    private static final int WINDOW_WIDTH = 800;
    /** Höhe des Fensters */
    private static final int WINDOW_HEIGHT = 800;
    /** Intervall des Timers in Millisekunden */
    private static final int TIMER_INTERVAL = 1000;
    /** Verzögerung für die Anzeige der Antwort in Millisekunden */
    private static final int ANSWER_DISPLAY_DELAY = 1000;

    /**
     * Konstruktor für das Quiz.
     * Initialisiert das Spiel und die Benutzeroberfläche.
     */
    public Quiz() {
        initializeGame();
        if (shouldExitEarly()) return;

        setupWindow();
        createUIComponents();
        layoutComponents();
        setupEventHandlers();
        applyStyles();
        finalizeSetup();
    }

    /**
     * Initialisiert das Spiel, indem der Cursor geladen und die Fragen geladen werden.
     */
    private void initializeGame() {
        loadCustomCursor();
        fragenListe = QuizLader.ladeAlleFragen(getFragenDatei());
        MusicManager.playBackgroundMusic("quiz_background");
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
     * Überprüft, ob das Spiel frühzeitig beendet werden soll.
     * @return true, wenn das Spiel frühzeitig beendet werden soll, sonst false.
     */
    private boolean shouldExitEarly() {
        if (fragenListe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keine Fragen gefunden!");
            MusicManager.stopCurrentMusic();
            new Hauptmenu();
            return true;
        }
        return false;
    }

    /**
     * Richtet das Fenster ein.
     */
    private void setupWindow() {
        setTitle("Quiz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setLayout(new BorderLayout());
    }

    /**
     * Erstellt die UI-Komponenten.
     */
    private void createUIComponents() {
        createMainPanel();
        createQuestionArea();
        createAnswerButtons();
        createControlButtons();
        createTimer();
        createStatusLabel();
    }

    /**
     * Erstellt das Hauptpanel.
     */
    private void createMainPanel() {
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setFocusable(true);
    }

    /**
     * Erstellt den Bereich für die Frage.
     */
    private void createQuestionArea() {
        QuizDaten currentQuestion = fragenListe.get(aktuelleFrageIndex);
        frageFeld = new JTextArea(currentQuestion.frage, 4, 3);
        frageFeld.setLineWrap(true);
        frageFeld.setWrapStyleWord(true);
        frageFeld.setEditable(false);
    }

    /**
     * Erstellt die Antwort-Buttons.
     */
    private void createAnswerButtons() {
        QuizDaten currentQuestion = fragenListe.get(aktuelleFrageIndex);
        antwortButtons = new JButton[4];

        for (int i = 0; i < 4; i++) {
            antwortButtons[i] = new JButton(currentQuestion.antworten[i]);
            final int answerIndex = i;
            antwortButtons[i].addActionListener(e ->
                    verarbeiteAntwort(answerIndex, frageFeld, antwortButtons));
        }
    }

    /**
     * Erstellt die Steuerungs-Buttons.
     */
    private void createControlButtons() {
        hauptmenuButton = new JButton("Zurück zum Hauptmenü");
        hauptmenuButton.addActionListener(this::returnToMainMenu);

        frageUeberspringenButton = new JButton("Diese Frage überspringen");
        frageUeberspringenButton.addActionListener(e ->
                verarbeiteAntwort(5, frageFeld, antwortButtons));
    }

    /**
     * Erstellt den Timer.
     */
    private void createTimer() {
        timerBar = new JProgressBar(0, 100);
        timerBar.setStringPainted(false);
        timerBar.setFont(new Font("Arial", Font.BOLD, 16));
        timerBar.setForeground(Color.GREEN);
        timerBar.setBackground(Color.LIGHT_GRAY);
    }

    /**
     * Erstellt das Status-Label.
     */
    private void createStatusLabel() {
        updateStatusLabel();
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
        statusLabel.setForeground(Color.BLACK);
    }

    /**
     * Aktualisiert das Status-Label.
     */
    private void updateStatusLabel() {
        if (statusLabel == null) {
            statusLabel = new JLabel();
        }
        statusLabel.setText("Angemeldet als: " + Benutzername.username +
                " | Punkte: " + punkte + "/" + gesamtPunkte);
    }

    /**
     * Legt die Komponenten im Layout an.
     */
    private void layoutComponents() {
        GridBagConstraints gbc = createBaseConstraints();

        // Fragebereich
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 0.8;
        mainPanel.add(frageFeld, gbc);

        // Antwort-Buttons (2x2 Raster)
        gbc.gridwidth = 1; gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 1; mainPanel.add(antwortButtons[0], gbc);
        gbc.gridx = 1; gbc.gridy = 1; mainPanel.add(antwortButtons[1], gbc);
        gbc.gridx = 0; gbc.gridy = 2; mainPanel.add(antwortButtons[2], gbc);
        gbc.gridx = 1; gbc.gridy = 2; mainPanel.add(antwortButtons[3], gbc);

        // Steuerungs-Buttons
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 3; mainPanel.add(hauptmenuButton, gbc);
        gbc.gridx = 0; gbc.gridy = 4; mainPanel.add(frageUeberspringenButton, gbc);

        // Timer
        gbc.gridx = 0; gbc.gridy = 5; mainPanel.add(timerBar, gbc);

        // Zum Frame hinzufügen
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(statusLabel, BorderLayout.SOUTH);
    }

    /**
     * Erstellt die grundlegenden Constraints für das GridBagLayout.
     * @return GridBagConstraints mit Standardwerten.
     */
    private GridBagConstraints createBaseConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        return gbc;
    }

    /**
     * Richtet die Event-Handler ein.
     */
    private void setupEventHandlers() {
        setupKeyboardShortcuts();
        setupResizeHandler();
    }

    /**
     * Richtet die Tastaturkürzel ein.
     */
    private void setupKeyboardShortcuts() {
        mainPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }
        });
        mainPanel.requestFocusInWindow();
    }

    /**
     * Behandelt Tastendrücke.
     * @param keyCode Der Code der gedrückten Taste.
     */
    private void handleKeyPress(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_1 -> antwortButtons[0].doClick();
            case KeyEvent.VK_2 -> antwortButtons[1].doClick();
            case KeyEvent.VK_3 -> antwortButtons[2].doClick();
            case KeyEvent.VK_4 -> antwortButtons[3].doClick();
            case KeyEvent.VK_ENTER, KeyEvent.VK_S -> frageUeberspringenButton.doClick();
            case KeyEvent.VK_ESCAPE, KeyEvent.VK_B -> returnToMainMenu(null);
        }
    }

    /**
     * Richtet den Handler für das Ändern der Fenstergröße ein.
     */
    private void setupResizeHandler() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateFonts();
            }
        });
    }

    /**
     * Wendet die Stile auf die Komponenten an.
     */
    private void applyStyles() {
        Color primaryColor = StyleManager.getColor("primary.color", Color.WHITE);
        Color secondaryColor = StyleManager.getColor("secondary.color", Color.LIGHT_GRAY);
        Color textColor = StyleManager.getColor("fixedfont.color", Color.WHITE);

        mainPanel.setBackground(primaryColor);

        // Stile auf alle Komponenten anwenden
        styleComponent(frageFeld, secondaryColor, textColor);
        for (JButton button : antwortButtons) {
            styleComponent(button, secondaryColor, textColor);
        }
        styleComponent(hauptmenuButton, secondaryColor, textColor);
        styleComponent(frageUeberspringenButton, secondaryColor, textColor);
        styleComponent(timerBar, secondaryColor, textColor);
    }

    /**
     * Wendet einen Stil auf eine Komponente an.
     * @param component Die Komponente, auf die der Stil angewendet werden soll.
     * @param bgColor Die Hintergrundfarbe.
     * @param fgColor Die Vordergrundfarbe.
     */
    private void styleComponent(Component component, Color bgColor, Color fgColor) {
        component.setBackground(bgColor);
        component.setForeground(fgColor);
    }

    /**
     * Finalisiert die Einrichtung.
     */
    private void finalizeSetup() {
        if (CustomCursorManager.isLoaded()) {
            CustomCursorManager.setCursorEverywhere();
        }
        setVisible(true);
        startTimer();
    }

    /**
     * Startet den Timer für die aktuelle Frage.
     */
    private void startTimer() {
        QuizDaten currentQuestion = fragenListe.get(aktuelleFrageIndex);
        timeRemaining = currentQuestion.time;
        int startTime = timeRemaining;

        updateTimerDisplay(100, Color.GREEN);

        timer = new Timer(TIMER_INTERVAL, new TimerActionListener(startTime));
        timer.start();
    }

    /**
     * Aktualisiert die Anzeige des Timers.
     * @param percent Der Prozentsatz der verbleibenden Zeit.
     * @param color Die Farbe des Timers.
     */
    private void updateTimerDisplay(int percent, Color color) {
        timerBar.setValue(percent);
        timerBar.setForeground(color);
    }

    /**
     * ActionListener für den Timer.
     */
    private class TimerActionListener implements ActionListener {
        private int currentTime;
        private final int startTime;

        /**
         * Konstruktor für den TimerActionListener.
         * @param startTime Die Startzeit des Timers.
         */
        public TimerActionListener(int startTime) {
            this.startTime = startTime;
            this.currentTime = startTime;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            currentTime--;
            timeRemaining = currentTime;

            int percent = calculateTimePercent(currentTime, startTime);
            Color timerColor = getTimerColor(percent);
            updateTimerDisplay(percent, timerColor);

            if (currentTime <= 0) {
                timer.stop();
                verarbeiteAntwort(5, frageFeld, antwortButtons); // Zeit abgelaufen
            }
        }

        /**
         * Berechnet den Prozentsatz der verbleibenden Zeit.
         * @param current Die aktuelle Zeit.
         * @param start Die Startzeit.
         * @return Der Prozentsatz der verbleibenden Zeit.
         */
        private int calculateTimePercent(int current, int start) {
            return (int) (((double) current / start) * 100);
        }

        /**
         * Bestimmt die Farbe des Timers basierend auf dem verbleibenden Prozentsatz.
         * @param percent Der Prozentsatz der verbleibenden Zeit.
         * @return Die Farbe des Timers.
         */
        private Color getTimerColor(int percent) {
            if (percent <= 30) return Color.RED;
            if (percent <= 60) return Color.ORANGE;
            return Color.GREEN;
        }
    }

    /**
     * Verarbeitet die Antwort des Spielers.
     * @param selectedAnswer Die ausgewählte Antwort.
     * @param frageFeld Der Textbereich mit der Frage.
     * @param buttons Die Antwort-Buttons.
     */
    private void verarbeiteAntwort(int selectedAnswer, JTextArea frageFeld, JButton[] buttons) {
        if (timer != null) timer.stop();

        processAnswer(selectedAnswer);
        updateButtonColors(selectedAnswer, buttons);
        updateStatusLabel();

        // Zeige das Ergebnis kurz an, dann gehe zur nächsten Frage
        Timer delayTimer = new Timer(ANSWER_DISPLAY_DELAY, e -> {
            ((Timer) e.getSource()).stop();
            showNextQuestion();
        });
        delayTimer.start();
    }

    /**
     * Verarbeitet die Antwort und aktualisiert die Punkte.
     * @param selectedAnswer Die ausgewählte Antwort.
     */
    private void processAnswer(int selectedAnswer) {
        QuizDaten currentQuestion = fragenListe.get(aktuelleFrageIndex);
        gesamtPunkte += currentQuestion.time;

        if (selectedAnswer != 5 && currentQuestion.loesung[selectedAnswer]) {
            punkte += timeRemaining;
        }
    }

    /**
     * Aktualisiert die Farben der Antwort-Buttons basierend auf der ausgewählten Antwort.
     * @param selectedAnswer Die ausgewählte Antwort.
     * @param buttons Die Antwort-Buttons.
     */
    private void updateButtonColors(int selectedAnswer, JButton[] buttons) {
        resetButtonColors(buttons);

        if (selectedAnswer == 5) return; // Frage übersprungen

        QuizDaten currentQuestion = fragenListe.get(aktuelleFrageIndex);
        Color correctColor = new Color(0, 160, 0);
        Color incorrectColor = new Color(200, 0, 0);

        // Farbe des ausgewählten Buttons
        if (currentQuestion.loesung[selectedAnswer]) {
            buttons[selectedAnswer].setBackground(correctColor);
        } else {
            buttons[selectedAnswer].setBackground(incorrectColor);
            // Zeige die richtige Antwort
            int correctAnswerIndex = findCorrectAnswerIndex(currentQuestion.loesung);
            if (correctAnswerIndex != -1) {
                buttons[correctAnswerIndex].setBackground(correctColor);
            }
        }
    }

    /**
     * Setzt die Farben der Antwort-Buttons zurück.
     * @param buttons Die Antwort-Buttons.
     */
    private void resetButtonColors(JButton[] buttons) {
        Color defaultBg = StyleManager.getColor("secondary.color", Color.LIGHT_GRAY);
        Color defaultFg = StyleManager.getColor("fixedfont.color", Color.WHITE);

        for (JButton button : buttons) {
            button.setBackground(defaultBg);
            button.setForeground(defaultFg);
            button.setBorder(null);
        }
    }

    /**
     * Findet den Index der richtigen Antwort.
     * @param solutions Die Lösungen für die Frage.
     * @return Der Index der richtigen Antwort oder -1, wenn keine richtige Antwort gefunden wurde.
     */
    private int findCorrectAnswerIndex(boolean[] solutions) {
        return IntStream.range(0, solutions.length)
                .filter(i -> solutions[i])
                .findFirst()
                .orElse(-1);
    }

    /**
     * Zeigt die nächste Frage an.
     */
    private void showNextQuestion() {
        aktuelleFrageIndex++;

        if (isQuizFinished()) {
            finishQuiz();
            return;
        }

        updateUIForNextQuestion();
        resetButtonColors(antwortButtons);
        startTimer();
    }

    /**
     * Überprüft, ob das Quiz beendet ist.
     * @return true, wenn das Quiz beendet ist, sonst false.
     */
    private boolean isQuizFinished() {
        return aktuelleFrageIndex >= fragenListe.size();
    }

    /**
     * Beendet das Quiz und zeigt das Endergebnis an.
     */
    private void finishQuiz() {
        MusicManager.stopCurrentMusic();
        showFinalScore();
        saveUserPoints();
        returnToMainMenu(null);
    }

    /**
     * Zeigt die Endpunktzahl an.
     */
    private void showFinalScore() {
        String message = "Quiz beendet.\nEndpunktestand: " + punkte + "/" + gesamtPunkte;
        JOptionPane.showMessageDialog(this, message);
    }

    /**
     * Speichert die Punkte des Spielers.
     */
    private void saveUserPoints() {
        try {
            Login.updateUserPoints(Benutzername.username, Benutzername.points + punkte);
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Punkte: " + e.getMessage());
        }
    }

    /**
     * Aktualisiert die Benutzeroberfläche für die nächste Frage.
     */
    private void updateUIForNextQuestion() {
        QuizDaten nextQuestion = fragenListe.get(aktuelleFrageIndex);
        frageFeld.setText(nextQuestion.frage);

        for (int i = 0; i < antwortButtons.length; i++) {
            antwortButtons[i].setText(nextQuestion.antworten[i]);
        }
    }

    /**
     * Kehrt zum Hauptmenü zurück.
     * @param e Das ActionEvent.
     */
    private void returnToMainMenu(ActionEvent e) {
        if (timer != null) timer.stop();
        MusicManager.stopCurrentMusic();
        dispose();
        new Hauptmenu();
    }

    /**
     * Aktualisiert die Schriftarten basierend auf der Fenstergröße.
     */
    private void updateFonts() {
        float scaleFactor = getWidth() / 500.0f;

        int questionFontSize = Math.round(15 * scaleFactor);
        int buttonFontSize = Math.round(12 * scaleFactor);
        int labelFontSize = Math.round(12 * scaleFactor);
        int timerFontSize = Math.round(16 * scaleFactor);

        frageFeld.setFont(new Font("Arial", Font.BOLD, questionFontSize));

        for (JButton button : antwortButtons) {
            button.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
        }
        hauptmenuButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
        frageUeberspringenButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));

        timerBar.setFont(new Font("Arial", Font.BOLD, timerFontSize));
        statusLabel.setFont(new Font("Arial", Font.BOLD, labelFontSize));
    }

    /**
     * Gibt den Dateinamen der Fragen zurück.
     * @return Der Dateiname der Fragen.
     */
    protected abstract String getFragenDatei();

    /**
     * Hauptklasse zum Starten des Quiz.
     */
    public static class Main {
        /**
         * Hauptmethode zum Starten des Quiz.
         * @param args Die Kommandozeilenargumente.
         */
        public static void main(String[] args) {
            StyleManager.loadConfig("config.properties");
            MusicManager.stopCurrentMusic();
            SwingUtilities.invokeLater(Hauptmenu::new);
        }
    }
}
