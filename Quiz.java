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
        setSize(800, 900);
        setLocationRelativeTo(null);

        // Layout: 6 Zeilen, 2 Spalten
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

// Fragefeld
        JTextArea frageFeld = new JTextArea(fragenListe.get(aktuelleFrageIndex).frage, 4, 3);
        frageFeld.setLineWrap(true);
        frageFeld.setWrapStyleWord(true);
        frageFeld.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(frageFeld, gbc);

// Antwortbuttons
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

// Zurück zum Hauptmenü
        JButton hauptmenuButton = new JButton("Zurück zum Hauptmenü");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(hauptmenuButton, gbc);

// Frage überspringen
        JButton FrageUeberspringenButton = new JButton("Diese Frage überskippen");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
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
            if (comp instanceof JButton || comp instanceof JTextField || comp instanceof JTextArea) {
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
    }

    protected abstract String getFragenDatei();

    private void aktualisiereFonts(JTextArea frageFeld, JButton[] buttons) {
        int breite = getWidth();
        float faktor = breite / 500.0f;

        int frageFontSize = Math.round(15 * faktor);
        int buttonFontSize = Math.round(12 * faktor);

        frageFeld.setFont(new Font("Arial", Font.BOLD, frageFontSize));
        for (JButton b : buttons) {
            b.setFont(new Font("Arial", Font.PLAIN, buttonFontSize));
        }
    }

    public class Main {
        public static void main(String[] args) {
            StyleManager.loadConfig("config.properties");
            SwingUtilities.invokeLater(Hauptmenu::new);
        }
    }
}
