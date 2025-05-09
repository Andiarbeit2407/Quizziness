// Importieren der benötigten Klassen für GUI, Layout und Event-Handling
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

// Definition der Klasse Quiz, die ein Fenster (JFrame) darstellt
public class Quiz extends JFrame {

    // Liste, die alle geladenen Fragen enthält
    private List<QuizDaten> fragenListe;

    // Aktueller Index der Frage, die angezeigt wird
    private int aktuelleFrageIndex = 0;

    // Konstruktor für das Quiz-Fenster
    public Quiz() {
        // Laden aller Fragen aus der Datei "fragen.txt"
        fragenListe = QuizLader.ladeAlleFragen("fragen.txt");

        // Wenn keine Fragen gefunden wurden, Fehlermeldung anzeigen
        if (fragenListe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keine Fragen gefunden!");
            return;
        }

        setTitle("Quiz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);

        // Panel erstellen und GridBagLayout verwenden
        JPanel panel = new JPanel(new GridBagLayout());

        // Layout-Beschränkungen für die Positionierung der Elemente
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Textfeld zur Anzeige der aktuellen Frage
        JTextArea frageFeld = new JTextArea(fragenListe.get(aktuelleFrageIndex).frage, 4, 3);
        frageFeld.setLineWrap(true); // Zeilenumbruch aktivieren
        frageFeld.setWrapStyleWord(true); // Wörter nicht mitten im Wort umbrechen
        frageFeld.setEditable(false); // Textfeld nicht bearbeitbar
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(frageFeld, gbc);




        // Button für Antwort A erstellen
        JButton antwortA = new JButton(fragenListe.get(aktuelleFrageIndex).antworten[0]);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(antwortA, gbc);

        // Button für Antwort B erstellen
        JButton antwortB = new JButton(fragenListe.get(aktuelleFrageIndex).antworten[1]);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(antwortB, gbc);

        // Button für Antwort C erstellen
        JButton antwortC = new JButton(fragenListe.get(aktuelleFrageIndex).antworten[2]);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(antwortC, gbc);



        // Button für Antwort D erstellen
        JButton antwortD = new JButton(fragenListe.get(aktuelleFrageIndex).antworten[3]);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(antwortD, gbc);



        // Button zum Zurückkehren ins Hauptmenü
        JButton hauptmenuButton = new JButton("Zurück zum Hauptmenü");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(hauptmenuButton, gbc);

        // ActionListener für den Hauptmenü-Button
        hauptmenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fenster schließen
                new Hauptmenu(); // Neues Hauptmenü öffnen
            }
        });

        //Überspringe Button
        JButton FrageUeberspringenButton = new JButton("Diese Frage überspringen");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
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

        panel.setBackground(StyleManager.getColor("background.color", Color.WHITE));
        Color buttonAndTextBg = StyleManager.getColor("answer.color", Color.LIGHT_GRAY);
        Color textColor = StyleManager.getColor("font.color", Color.WHITE);

        // Durchläuft alle Komponenten im Panel
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
                aktualisiereFonts(frageFeld,new JButton[]{antwortA, antwortB, antwortC, antwortD, hauptmenuButton, FrageUeberspringenButton});
            }
        });

        // Panel dem Fenster hinzufügen
        add(panel);

        // Fenster sichtbar machen
        setVisible(true);
    }
    
    

    // Methode zur Auswertung und Anzeige der nächsten Frage
    private void verarbeiteAntwort(int ausgewaehlteAnwort, JTextArea frageFeld, JButton antwortA, JButton antwortB, JButton antwortC, JButton antwortD) {
        if(ausgewaehlteAnwort != 5) {
            QuizDaten aktuelleFrage = fragenListe.get(aktuelleFrageIndex);
            boolean istRichtig = aktuelleFrage.loesung[ausgewaehlteAnwort];

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






    // Main-Methode zum Starten der Anwendung
    public static void main(String[] args) {
        // Load the external style configuration before starting the GUI
        StyleManager.loadConfig("config.properties");
        // Startet die GUI im Event-Dispatch-Thread
        SwingUtilities.invokeLater(Hauptmenu::new);
    }

    private void aktualisiereFonts(JTextArea frageFeld, JButton[] buttons){
        int breite = getWidth();
        float faktor = breite / 500.0f; // 500 ist die ursprüngliche Fensterbreite

        int frageFontSize = Math.round(15 * faktor);
        int buttonFontSize = Math.round(12 * faktor);

        frageFeld.setFont(new Font("Arial", Font.BOLD, frageFontSize));
        for (JButton b : buttons) {
            b.setFont(new Font("Arial", Font.PLAIN, buttonFontSize));
        }
    }
}