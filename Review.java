import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Die Klasse Review erstellt ein Fenster zur Bewertung eines Quiz.
 * Sie ermöglicht es dem Benutzer, eine Bewertung abzugeben und Feedback zu geben.
 */
public class Review {
    private JFrame review; // Das Hauptfenster der Bewertungsanwendung

    /**
     * Konstruktor für die Review-Klasse.
     * Initialisiert das Hauptfenster und fügt die notwendigen Komponenten hinzu.
     */
    public Review() {
        // Initialisierung des Hauptfensters
        review = new JFrame("Review");
        review.setSize(300, 150);
        review.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        review.setLocationRelativeTo(null); // Zentriert das Fenster auf dem Bildschirm

        // Erstellung der Buttons
        JButton quizStarten = new JButton("Review");
        JButton beenden = new JButton("Beenden");

        // Panel für die Buttons
        JPanel panel = new JPanel();
        panel.add(quizStarten);
        panel.add(beenden);
        review.add(panel);

        // ActionListener für den "Review"-Button
        quizStarten.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                review.setVisible(false); // Versteckt das Hauptfenster

                // Erstellung des Bewertungs-Panels
                JPanel bewertungPanel = new JPanel();
                bewertungPanel.setLayout(new FlowLayout());
                bewertungPanel.setBackground(Color.WHITE);
                JButton[] sterneButtons = new JButton[5];
                final int[] gewaehlt = {0}; // Speichert die ausgewählte Bewertung

                // Erstellung der Sterne-Buttons für die Bewertung
                for (int i = 0; i < 5; i++) {
                    final int rating = i + 1;
                    JButton stern = new JButton("☆");
                    stern.setFont(new Font("SansSerif", Font.PLAIN, 32));
                    stern.setBorderPainted(false);
                    stern.setFocusPainted(false);
                    stern.setBackground(Color.WHITE);
                    stern.setCursor(new Cursor(Cursor.HAND_CURSOR));

                    // ActionListener für die Sterne-Buttons
                    stern.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            gewaehlt[0] = rating; // Setzt die ausgewählte Bewertung
                            for (int j = 0; j < 5; j++) {
                                sterneButtons[j].setText(j < rating ? "★" : "☆"); // Aktualisiert die Sterne-Anzeige
                            }
                        }
                    });

                    sterneButtons[i] = stern;
                    bewertungPanel.add(stern);
                }

                // Anzeige des Bewertungsdialogs
                int result = JOptionPane.showConfirmDialog(null, bewertungPanel,
                        "Wie fandest du das Quiz?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // Verarbeitung der Bewertung
                if (result == JOptionPane.OK_OPTION && gewaehlt[0] > 0) {
                    if (gewaehlt[0] >= 4) {
                        String sterneStr = "";
                        for (int i = 1; i <= 5; i++) {
                            sterneStr += (i <= gewaehlt[0]) ? "★" : "☆";
                        }
                        JOptionPane.showMessageDialog(null,
                                "Deine Bewertung: " + sterneStr + "\nDanke für dein Feedback!");
                    } else {
                        String grund = JOptionPane.showInputDialog("Oh, was hat dir nicht gefallen?");
                        JOptionPane.showMessageDialog(null, "Danke für dein Feedback!: \"" + grund + "\"");
                    }

                    JOptionPane.showMessageDialog(null, "Zurück zum Hauptmenü");
                    new Hauptmenu(); // Erstellt eine neue Instanz des Hauptmenüs
                    return;
                }

                review.setVisible(true); // Zeigt das Hauptfenster wieder an
            }
        });

        // ActionListener für den "Beenden"-Button
        beenden.addActionListener(e -> System.exit(0));
        review.setVisible(true); // Macht das Hauptfenster sichtbar
    }

    /**
     * Die main-Methode, die die Anwendung startet.
     *
     * @param args Kommandozeilenargumente (werden nicht verwendet).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Review()); // Startet die Anwendung in einem separaten Thread
    }
}
