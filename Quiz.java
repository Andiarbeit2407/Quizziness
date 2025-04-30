// Importieren der benötigten Klassen für GUI, Layout und Event-Handling
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
                aktuelleFrageIndex++;
                frageFeld.setText(fragenListe.get(aktuelleFrageIndex).frage);
                antwortA.setText(fragenListe.get(aktuelleFrageIndex).antworten[0]);
                antwortB.setText(fragenListe.get(aktuelleFrageIndex).antworten[1]);
                antwortC.setText(fragenListe.get(aktuelleFrageIndex).antworten[2]);
                antwortD.setText(fragenListe.get(aktuelleFrageIndex).antworten[3]);
            }
        });

        // Panel dem Fenster hinzufügen
        add(panel);

        // Fenster sichtbar machen
        setVisible(true);
    }

    // Main-Methode zum Starten der Anwendung
    public static void main(String[] args) {
        // Startet das Quiz im Event-Dispatch-Thread
        SwingUtilities.invokeLater(Quiz::new);
    }
}
