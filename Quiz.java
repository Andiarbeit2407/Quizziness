import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public abstract class Quiz extends JFrame {

    protected List<QuizDaten> fragenListe;
    protected int aktuelleFrageIndex = 0;
    protected int timeRemaining;
    protected Timer timer;
    private JProgressBar timerBar;
    private JLabel statusLabel;
    protected int punkte = 0;
    protected int gesamtPunkte = 0;

    public Quiz() {
        fragenListe = QuizLader.ladeAlleFragen(getFragenDatei());

        if (fragenListe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keine Fragen gefunden!");
            return;
        }

        setTitle("Quiz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        JTextArea frageFeld = new JTextArea(fragenListe.get(aktuelleFrageIndex).frage, 4, 3);
        frageFeld.setLineWrap(true);
        frageFeld.setWrapStyleWord(true);
        frageFeld.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(frageFeld, gbc);

        JButton antwortA = new JButton(fragenListe.get(aktuelleFrageIndex).antworten[0]);
        JButton antwortB = new JButton(fragenListe.get(aktuelleFrageIndex).antworten[1]);
        JButton antwortC = new JButton(fragenListe.get(aktuelleFrageIndex).antworten[2]);
        JButton antwortD = new JButton(fragenListe.get(aktuelleFrageIndex).antworten[3]);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(antwortA, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(antwortB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(antwortC, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(antwortD, gbc);

        JButton hauptmenuButton = new JButton("Zurück zum Hauptmenü");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(hauptmenuButton, gbc);

        hauptmenuButton.addActionListener(e -> {
            if (timer != null) timer.stop();
            dispose();
            new Hauptmenu();
        });

        JButton FrageUeberspringenButton = new JButton("Diese Frage überspringen");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(FrageUeberspringenButton, gbc);

        timerBar = new JProgressBar(0, 100);
        timerBar.setStringPainted(false);
        timerBar.setFont(new Font("Arial", Font.BOLD, 16));
        timerBar.setForeground(Color.GREEN);
        timerBar.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(timerBar, gbc);

        statusLabel = new JLabel("Angemeldet als: " + Benutzername.username + " | Punkte: " + punkte + "/" + gesamtPunkte);
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
        statusLabel.setForeground(Color.BLACK);
        add(statusLabel, BorderLayout.SOUTH);

        FrageUeberspringenButton.addActionListener(e -> verarbeiteAntwort(5, frageFeld, antwortA, antwortB, antwortC, antwortD));
        antwortA.addActionListener(e -> verarbeiteAntwort(0, frageFeld, antwortA, antwortB, antwortC, antwortD));
        antwortB.addActionListener(e -> verarbeiteAntwort(1, frageFeld, antwortA, antwortB, antwortC, antwortD));
        antwortC.addActionListener(e -> verarbeiteAntwort(2, frageFeld, antwortA, antwortB, antwortC, antwortD));
        antwortD.addActionListener(e -> verarbeiteAntwort(3, frageFeld, antwortA, antwortB, antwortC, antwortD));

        panel.setBackground(StyleManager.getColor("primary.color", Color.WHITE));
        Color buttonAndTextBg = StyleManager.getColor("secondary.color", Color.LIGHT_GRAY);
        Color textColor = StyleManager.getColor("fixedfont.color", Color.WHITE);

        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton || comp instanceof JTextField || comp instanceof JTextArea || comp instanceof JLabel || comp instanceof JProgressBar) {
                comp.setBackground(buttonAndTextBg);
                comp.setForeground(textColor);
            }
        }

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                aktualisiereFonts(frageFeld, new JButton[]{antwortA, antwortB, antwortC, antwortD, hauptmenuButton, FrageUeberspringenButton});
            }
        });

        panel.setFocusable(true);
        panel.requestFocusInWindow();
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_1 -> antwortA.doClick();
                    case KeyEvent.VK_2 -> antwortB.doClick();
                    case KeyEvent.VK_3 -> antwortC.doClick();
                    case KeyEvent.VK_4 -> antwortD.doClick();
                    case KeyEvent.VK_ENTER, KeyEvent.VK_S -> FrageUeberspringenButton.doClick();
                    case KeyEvent.VK_ESCAPE, KeyEvent.VK_INSERT, KeyEvent.VK_B -> {
                        dispose();
                        new Hauptmenu();
                    }
                }
            }
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(statusLabel, BorderLayout.SOUTH);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        starteTimer(frageFeld, antwortA, antwortB, antwortC, antwortD);
    }

    private void starteTimer(JTextArea frageFeld, JButton antwortA, JButton antwortB, JButton antwortC, JButton antwortD) {
        timeRemaining = fragenListe.get(aktuelleFrageIndex).time;
        int startTime = timeRemaining;

        timerBar.setValue(100);
        timerBar.setForeground(Color.GREEN);

        timer = new Timer(1000, new ActionListener() {
            int currentTime = startTime;

            @Override
            public void actionPerformed(ActionEvent e) {
                currentTime--;
                timeRemaining = currentTime;
                int percent = (int) (((double) currentTime / startTime) * 100);
                timerBar.setValue(percent);

                if (percent <= 30) {
                    timerBar.setForeground(Color.RED);
                } else if (percent <= 60) {
                    timerBar.setForeground(Color.ORANGE);
                } else {
                    timerBar.setForeground(Color.GREEN);
                }

                if (currentTime <= 0) {
                    timer.stop();
                    verarbeiteAntwort(5, frageFeld, antwortA, antwortB, antwortC, antwortD);
                }
            }
        });
        timer.start();
    }

    private void verarbeiteAntwort(int ausgewaehlteAntwort, JTextArea frageFeld,
                                   JButton antwortA, JButton antwortB,
                                   JButton antwortC, JButton antwortD) {

        if (timer != null) timer.stop();

        JButton[] buttons = new JButton[]{antwortA, antwortB, antwortC, antwortD};
        Color defaultBg = StyleManager.getColor("secondary.color", Color.LIGHT_GRAY);
        Color defaultFg = StyleManager.getColor("fixedfont.color", Color.WHITE);
        Color richtigColor = new Color(0, 160, 0);
        Color falschColor = new Color(200, 0, 0);

        for (JButton btn : buttons) {
            btn.setBackground(defaultBg);
            btn.setForeground(defaultFg);
            btn.setBorder(null);
        }

        QuizDaten aktuelleFrage = fragenListe.get(aktuelleFrageIndex);
        gesamtPunkte += aktuelleFrage.time;

        if (ausgewaehlteAntwort != 5) {
            boolean istRichtig = aktuelleFrage.loesung[ausgewaehlteAntwort];
            if (istRichtig) {
                buttons[ausgewaehlteAntwort].setBackground(richtigColor);
                punkte += timeRemaining;
            } else {
                buttons[ausgewaehlteAntwort].setBackground(falschColor);
            }
        }

        statusLabel.setText("Angemeldet als: " + Benutzername.username + " | Punkte: " + punkte + "/" + gesamtPunkte);

        new Timer(1000, e -> {
            ((Timer) e.getSource()).stop();
            zeigeNaechsteFrage(frageFeld, antwortA, antwortB, antwortC, antwortD);
        }).start();
    }

    private void zeigeNaechsteFrage(JTextArea frageFeld,
                                    JButton antwortA, JButton antwortB,
                                    JButton antwortC, JButton antwortD) {

        aktuelleFrageIndex++;
        if (aktuelleFrageIndex >= fragenListe.size()) {
            JOptionPane.showMessageDialog(this, "Quiz beendet.\nEndpunktestand: " + punkte + "/" + gesamtPunkte);
            dispose();
            new Hauptmenu();
            return;
        }

        QuizDaten naechsteFrage = fragenListe.get(aktuelleFrageIndex);
        frageFeld.setText(naechsteFrage.frage);
        antwortA.setText(naechsteFrage.antworten[0]);
        antwortB.setText(naechsteFrage.antworten[1]);
        antwortC.setText(naechsteFrage.antworten[2]);
        antwortD.setText(naechsteFrage.antworten[3]);

        JButton[] buttons = new JButton[]{antwortA, antwortB, antwortC, antwortD};
        Color defaultBg = StyleManager.getColor("secondary.color", Color.LIGHT_GRAY);
        Color defaultFg = StyleManager.getColor("fixedfont.color", Color.WHITE);
        for (JButton btn : buttons) {
            btn.setBackground(defaultBg);
            btn.setForeground(defaultFg);
            btn.setBorder(null);
        }

        starteTimer(frageFeld, antwortA, antwortB, antwortC, antwortD);
    }

    protected abstract String getFragenDatei();

    private void aktualisiereFonts(JTextArea frageFeld, JButton[] buttons) {
        int breite = getWidth();
        float faktor = breite / 500.0f;
        int labelFontSize = Math.round(12 * faktor);

        int frageFontSize = Math.round(15 * faktor);
        int buttonFontSize = Math.round(12 * faktor);

        frageFeld.setFont(new Font("Arial", Font.BOLD, frageFontSize));
        for (JButton b : buttons) {
            b.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
        }
        timerBar.setFont(new Font("Arial", Font.BOLD, Math.round(16 * faktor)));
        statusLabel.setFont(new Font("Arial", Font.BOLD, labelFontSize));
    }

    public static class Main {
        public static void main(String[] args) {
            StyleManager.loadConfig("config.properties");
            SwingUtilities.invokeLater(Hauptmenu::new);
        }
    }
}
