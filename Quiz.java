import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public abstract class Quiz extends JFrame {

    protected List<QuizDaten> fragenListe;
    protected int aktuelleFrageIndex = 0;

    // Aktueller Index der Frage, die angezeigt wird
    private int aktuelleFrageIndex = 0;

    // Konstruktor für das Quiz-Fenster
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

        // Textfeld zur Anzeige der aktuellen Frage
        JTextArea frageFeld = new JTextArea(fragenListe.get(aktuelleFrageIndex).frage, 4, 3);
        frageFeld.setLineWrap(true);
        frageFeld.setWrapStyleWord(true);
        frageFeld.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(frageFeld, gbc);



        // Buttons (ohne Rundungen)
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

        JButton FrageUeberspringenButton = new JButton("Diese Frage überspringen");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(FrageUeberspringenButton, gbc);

        //Actionlistener für Überspringen Button
        FrageUeberspringenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verarbeiteAntwort(5, frageFeld,  antwortA,  antwortB,  antwortC,  antwortD);
            }
        });
//Actionlistener für Button Anwtort A
        antwortA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verarbeiteAntwort(0, frageFeld,  antwortA,  antwortB,  antwortC,  antwortD);
            }
        });
//Actionlistener für Button Anwtort B
        antwortB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verarbeiteAntwort(1, frageFeld,  antwortA,  antwortB,  antwortC,  antwortD);
            }
        });
//Actionlistener für Button Anwtort C
        antwortC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verarbeiteAntwort(2, frageFeld,  antwortA,  antwortB,  antwortC,  antwortD);
            }
        });
//Actionlistener für Button Anwtort D
        antwortD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verarbeiteAntwort(3, frageFeld,  antwortA,  antwortB,  antwortC,  antwortD);
            }
        });

        panel.setBackground(StyleManager.getColor("primary.color", Color.WHITE));
        Color buttonAndTextBg = StyleManager.getColor("secondary.color", Color.LIGHT_GRAY);
        Color textColor = StyleManager.getColor("fixedfont.color", Color.WHITE);

        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton || comp instanceof JTextField || comp instanceof JTextArea) {
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

        add(panel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        timeRemaining = fragenListe.get(aktuelleFrageIndex).time;
        timer.start();
    }
    
    

    private void verarbeiteAntwort(int ausgewaehlteAntwort, JTextArea frageFeld,
                                   JButton antwortA, JButton antwortB,
                                   JButton antwortC, JButton antwortD) {



        if (ausgewaehlteAntwort != 5) {
            QuizDaten aktuelleFrage = fragenListe.get(aktuelleFrageIndex);
            boolean istRichtig = aktuelleFrage.loesung[ausgewaehlteAntwort];

            // Rücksetzen der Farben und Borders auf Standard
            JButton[] buttons = new JButton[]{antwortA, antwortB, antwortC, antwortD};
            Color defaultBg = StyleManager.getColor("secondary.color", Color.LIGHT_GRAY);
            Color defaultFg = StyleManager.getColor("fixedfont.color", Color.WHITE);
            for (JButton btn : buttons) {
                btn.setBackground(defaultBg);
                btn.setForeground(defaultFg);
                btn.setBorder(null);
            }

            // Rückmeldung geben
            if (istRichtig) {
                JOptionPane.showMessageDialog(this, "Richtig!");
            } else {
                JOptionPane.showMessageDialog(this, "Falsch!");
            }
        }
        // Nächste Frage anzeigen
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
            b.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
        }
    }

    public static class Main {
        public static void main(String[] args) {
            StyleManager.loadConfig("config.properties");
            SwingUtilities.invokeLater(Hauptmenu::new);
        }
    }
}