import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public abstract class Quiz extends JFrame {

    protected List<QuizDaten> fragenListe;
    protected int aktuelleFrageIndex = 0;

    public Quiz() {
        fragenListe = QuizLader.ladeAlleFragen(getFragenDatei());

        if (fragenListe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keine Fragen gefunden!");
            return;
        }

        setTitle("Quiz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JTextArea frageFeld = new JTextArea(fragenListe.get(aktuelleFrageIndex).frage, 4, 3);
        frageFeld.setLineWrap(true);
        frageFeld.setWrapStyleWord(true);
        frageFeld.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
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
        gbc.fill = GridBagConstraints.NONE;
        panel.add(hauptmenuButton, gbc);

        JButton FrageUeberspringenButton = new JButton("Diese Frage überspringen");
        gbc.gridy = 4;
        panel.add(FrageUeberspringenButton, gbc);

        // Listener
        hauptmenuButton.addActionListener(e -> {
            dispose();
            new Hauptmenu();
        });

        FrageUeberspringenButton.addActionListener(e ->
                verarbeiteAntwort(5, frageFeld, antwortA, antwortB, antwortC, antwortD));

        antwortA.addActionListener(e ->
                verarbeiteAntwort(0, frageFeld, antwortA, antwortB, antwortC, antwortD));
        antwortB.addActionListener(e ->
                verarbeiteAntwort(1, frageFeld, antwortA, antwortB, antwortC, antwortD));
        antwortC.addActionListener(e ->
                verarbeiteAntwort(2, frageFeld, antwortA, antwortB, antwortC, antwortD));
        antwortD.addActionListener(e ->
                verarbeiteAntwort(3, frageFeld, antwortA, antwortB, antwortC, antwortD));

        // Styling
        panel.setBackground(StyleManager.getColor("background.color", Color.WHITE));
        Color buttonAndTextBg = StyleManager.getColor("answer.color", Color.LIGHT_GRAY);
        Color textColor = StyleManager.getColor("font.color", Color.WHITE);
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton || comp instanceof JTextField) {
                comp.setBackground(buttonAndTextBg);
                comp.setForeground(textColor);
            }
        }

        // Dynamische Schriftanpassung bei Größenänderung
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                aktualisiereFonts(frageFeld, new JButton[]{antwortA, antwortB, antwortC, antwortD, hauptmenuButton, FrageUeberspringenButton});
            }
        });
        // 🔑 Tastatureingabe für Antworten und Navigation
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
                    case KeyEvent.VK_ENTER -> FrageUeberspringenButton.doClick();
                    case KeyEvent.VK_ESCAPE, KeyEvent.VK_Q -> {
                        dispose();
                        new Hauptmenu();

                    }
                }
            }
        });

        // Panel dem Fenster hinzufügen
        add(panel);
        setVisible(true);
    }

    private void verarbeiteAntwort(int ausgewaehlteAntwort, JTextArea frageFeld,
                                   JButton antwortA, JButton antwortB,
                                   JButton antwortC, JButton antwortD) {
        if (ausgewaehlteAntwort != 5) {
            QuizDaten aktuelleFrage = fragenListe.get(aktuelleFrageIndex);
            boolean istRichtig = aktuelleFrage.loesung[ausgewaehlteAntwort];
            JOptionPane.showMessageDialog(this, istRichtig ? "Richtig!" : "Falsch!");
        }

        aktuelleFrageIndex++;
        if (aktuelleFrageIndex >= fragenListe.size()) {
            JOptionPane.showMessageDialog(this, "Quiz beendet.");
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
        JButton[] buttons = {antwortA, antwortB, antwortC, antwortD};

        if (ausgewaehlteAntwort != 5) {
            QuizDaten aktuelleFrage = fragenListe.get(aktuelleFrageIndex);
            boolean istRichtig = aktuelleFrage.loesung[ausgewaehlteAntwort];

            // 🔸 Visuelles Feedback (grün/rot)
            buttons[ausgewaehlteAntwort].setBackground(istRichtig ? Color.GREEN : Color.RED);
            JOptionPane.showMessageDialog(this, istRichtig ? "Richtig!" : "Falsch!");
        }

        aktuelleFrageIndex++;
        if (aktuelleFrageIndex >= fragenListe.size()) {
            JOptionPane.showMessageDialog(this, "Quiz beendet.");
            dispose();
            new Hauptmenu();
            return;
        }

        // Neue Frage anzeigen
        for (int i = 0; i < 4; i++) {
            buttons[i].setText(naechsteFrage.antworten[i]);
            buttons[i].setBackground(StyleManager.getColor("answer.color", Color.LIGHT_GRAY)); // Hintergrund zurücksetzen




        }
    }

    // 🔁 Muss von Unterklassen überschrieben werden
    protected abstract String getFragenDatei();


    private void aktualisiereFonts(JTextArea frageFeld, JButton[] buttons) {
        int breite = getWidth();
        float faktor = breite / 500.0f; // 500 ist die ursprüngliche Fensterbreite

        int frageFontSize = Math.round(15 * faktor);
        int buttonFontSize = Math.round(12 * faktor);

        frageFeld.setFont(new Font("Arial", Font.BOLD, frageFontSize));
        for (JButton b : buttons) {
            b.setFont(new Font("Arial", Font.PLAIN, buttonFontSize));
        }
    }
    // 👈 Wichtige schließende Klammer für die Klasse

    // 🔁 Main-Methode außerhalb der Klasse definieren
    public class Main {
        public static void main(String[] args) {
            // Load the external style configuration before starting the GUI
            StyleManager.loadConfig("config.properties");
            // Startet die GUI im Event-Dispatch-Thread
            SwingUtilities.invokeLater(Hauptmenu::new);
        }
    }
}
