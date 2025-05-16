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

    private int punkte = 0;
    private int punkteTotal = 0;
    private double startTime = (int) System.currentTimeMillis();
    private double endTime = 0;




    private int timeRemaining = 0;// Time remaining in seconds



    JLabel timerLabel = new JLabel(String.valueOf(timeRemaining));

    Timer timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (timeRemaining > 0) {
                timeRemaining--;
                timerLabel.setText(String.valueOf(timeRemaining));
            } else {
                // Countdown finished
                timerLabel.setText("Time's up!");
                timer.stop();
            }
        }
    });





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

        //Timer anzeigen
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        timerLabel.setFont(new Font("Serif", Font.BOLD, 15));
        panel.add(timerLabel, gbc);


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
                endTime = (int) System.currentTimeMillis();
                verarbeiteAntwort(0, frageFeld,  antwortA,  antwortB,  antwortC,  antwortD);
            }
        });
//Actionlistener für Button Anwtort B
        antwortB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endTime = (int) System.currentTimeMillis();
                verarbeiteAntwort(1, frageFeld,  antwortA,  antwortB,  antwortC,  antwortD);
            }
        });
//Actionlistener für Button Anwtort C
        antwortC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endTime = (int) System.currentTimeMillis();
                verarbeiteAntwort(2, frageFeld,  antwortA,  antwortB,  antwortC,  antwortD);
            }
        });
//Actionlistener für Button Anwtort D
        antwortD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endTime = (int) System.currentTimeMillis();
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

        // Panel dem Fenster hinzufügen
        add(panel);

        // Fenster sichtbar machen
        setVisible(true);

        timeRemaining = fragenListe.get(aktuelleFrageIndex).time;
        timer.start();
    }
    
    private void verarbeitungPunkte(){
        if ((endTime - startTime) / fragenListe.get(aktuelleFrageIndex).time * 1000 <= 1) {
            punkte = (int) Math.ceil( 1000 - 500 * ((endTime - startTime) / fragenListe.get(aktuelleFrageIndex).time * 1000));
        }
        else{
            punkte = 500;
        }
        punkteTotal = punkteTotal + punkte;
    }

    // Methode zur Auswertung und Anzeige der nächsten Frage
    private void verarbeiteAntwort(int ausgewaehlteAnwort, JTextArea frageFeld, JButton antwortA, JButton antwortB, JButton antwortC, JButton antwortD) {



        if(ausgewaehlteAnwort != 5) {
            QuizDaten aktuelleFrage = fragenListe.get(aktuelleFrageIndex);
            boolean istRichtig = aktuelleFrage.loesung[ausgewaehlteAnwort];

            // Rückmeldung geben
            if (istRichtig) {
                verarbeitungPunkte();
                JOptionPane.showMessageDialog(this, "Richtig! +" + punkte + " punkte :3");
            } else {
                JOptionPane.showMessageDialog(this, "Falsch! Keine Punkte!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Frage übersprungen, keine Punkte");
        }

        // Nächste Frage anzeigen
        aktuelleFrageIndex++;
        if (aktuelleFrageIndex >= fragenListe.size()) {
            JOptionPane.showMessageDialog(this, "Quiz beendet. Du hast " + punkteTotal + " Punkte erzielt X3");
            dispose();
            new Review();
            return;
        }

        QuizDaten naechsteFrage = fragenListe.get(aktuelleFrageIndex);
        frageFeld.setText(naechsteFrage.frage);
        antwortA.setText(naechsteFrage.antworten[0]);
        antwortB.setText(naechsteFrage.antworten[1]);
        antwortC.setText(naechsteFrage.antworten[2]);
        antwortD.setText(naechsteFrage.antworten[3]);
        startTime = (int) System.currentTimeMillis();

        timeRemaining = fragenListe.get(aktuelleFrageIndex).time;
        timer.start();

    }








    // Main-Methode zum Starten der Anwendung
    public static void main(String[] args) {

        // Load the external style configuration before starting the GUI
        StyleManager.loadConfig("config.properties");
        // Startet die GUI im Event-Dispatch-Thread
        SwingUtilities.invokeLater(Hauptmenu::new);
    }
}


