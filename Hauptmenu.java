import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Hauptmenu extends JFrame {

    public Hauptmenu() {
        setTitle("Quiz Hauptmenü");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titelLabel = new JLabel("Willkommen zum Quiz!", SwingConstants.CENTER);
        titelLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titelLabel, BorderLayout.NORTH);

        JLabel benutzerLabel = new JLabel("Angemeldet als: " + Benutzername.username);
        benutzerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        benutzerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
        add(benutzerLabel, BorderLayout.PAGE_START);

        JButton quizButton = new JButton("Quiz starten");
        JButton frageHinzufuegenButton = new JButton("Frage hinzufügen");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        buttonPanel.add(quizButton);
        buttonPanel.add(frageHinzufuegenButton);

        add(buttonPanel, BorderLayout.CENTER);

        quizButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Quiz();
                dispose();
            }
        });

        frageHinzufuegenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new QuizEingabe();
                dispose();
            }
        });

        setVisible(true);
    }
}
