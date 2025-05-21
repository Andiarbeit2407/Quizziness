import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Review {
    private JFrame review;

    public Review() {
        review = new JFrame("Review");
        review.setSize(300, 150);
        review.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        review.setLocationRelativeTo(null);

        JButton quizStarten = new JButton("Review");
        JButton beenden = new JButton("Beenden");

        JPanel panel = new JPanel();
        panel.add(quizStarten);
        panel.add(beenden);
        review.add(panel);

        quizStarten.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                review.setVisible(false);

                // Bewertungs-Panel
                JPanel bewertungPanel = new JPanel();
                bewertungPanel.setLayout(new FlowLayout());
                bewertungPanel.setBackground(Color.WHITE);
                JButton[] sterneButtons = new JButton[5];
                final int[] gewaehlt = {0};

                for (int i = 0; i < 5; i++) {
                    final int rating = i + 1;
                    JButton stern = new JButton("☆");
                    stern.setFont(new Font("SansSerif", Font.PLAIN, 32));
                    stern.setBorderPainted(false);
                    stern.setFocusPainted(false);
                    stern.setBackground(Color.WHITE);
                    stern.setCursor(new Cursor(Cursor.HAND_CURSOR));

                    stern.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            gewaehlt[0] = rating;
                            for (int j = 0; j < 5; j++) {
                                sterneButtons[j].setText(j < rating ? "★" : "☆");
                            }
                        }
                    });

                    sterneButtons[i] = stern;
                    bewertungPanel.add(stern);
                }

                int result = JOptionPane.showConfirmDialog(null, bewertungPanel,
                        "Wie fandest du das Quiz?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

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
                    new Hauptmenu();
                    return;
                }

                review.setVisible(true);
            }
        });

        beenden.addActionListener(e -> System.exit(0));
        review.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Review());
    }
}
