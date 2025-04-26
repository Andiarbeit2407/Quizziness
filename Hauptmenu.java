// Importieren der benötigten Klassen für GUI-Elemente, Layouts und Event-Handling
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Definition der Klasse Hauptmenu, die ein Fenster (JFrame) darstellt
public class Hauptmenu extends JFrame {

    // Konstruktor für das Hauptmenü-Fenster
    public Hauptmenu() {
        // Setzen des Fenstertitels
        setTitle("Quiz Hauptmenü");

        // Festlegen der Fenstergröße auf 400x300 Pixel
        setSize(400, 300);

        // Definieren, dass das Programm beendet wird, wenn das Fenster geschlossen wird
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Fenster wird zentriert auf dem Bildschirm angezeigt
        setLocationRelativeTo(null);

        // BorderLayout wird als Layout-Manager verwendet
        setLayout(new BorderLayout());

        // Erstellen eines Titel-Labels mit zentriertem Text und einer größeren, fetten Schriftart
        JLabel titelLabel = new JLabel("Willkommen zum Quiz!", SwingConstants.CENTER);
        titelLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titelLabel, BorderLayout.NORTH);

        // Erstellen eines Labels, das den Benutzernamen anzeigt
        JLabel benutzerLabel = new JLabel("Angemeldet als: " + Benutzername.username);
        benutzerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        benutzerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
        add(benutzerLabel, BorderLayout.PAGE_START);

        // Erstellen der beiden Buttons für "Quiz starten" und "Frage hinzufügen"
        JButton quizButton = new JButton("Quiz starten");
        JButton frageHinzufuegenButton = new JButton("Frage hinzufügen");

        // Erstellen eines Panels für die Buttons und Anordnung in einem Gitterlayout (2 Zeilen, 1 Spalte)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        buttonPanel.add(quizButton);
        buttonPanel.add(frageHinzufuegenButton);

        // Hinzufügen des Button-Panels in die Mitte des Fensters
        add(buttonPanel, BorderLayout.CENTER);

        // Hinzufügen eines ActionListeners zum "Quiz starten"-Button
        // Beim Klicken wird ein neues Quiz-Fenster geöffnet und das Hauptmenü geschlossen
        quizButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Quiz();
                dispose();
            }
        });

        // Hinzufügen eines ActionListeners zum "Frage hinzufügen"-Button
        // Beim Klicken wird das Fenster zur Frageneingabe geöffnet und das Hauptmenü geschlossen
        frageHinzufuegenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new QuizEingabe();
                dispose();
            }
        });

        // Sichtbarmachen des Fensters nach dem Aufbau aller Komponenten
        setVisible(true);
    }
}
