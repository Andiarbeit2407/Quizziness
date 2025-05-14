// Importieren der benötigten Klassen für GUI-Elemente, Layouts und Event-Handling
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Definition der Klasse Hauptmenu, die ein Fenster (JFrame) darstellt
public class Hauptmenu extends JFrame {

    private JLabel titelLabel;
    private JLabel benutzerLabel;
    private JButton quizButton;
    private JButton frageHinzufuegenButton;
    private JPanel buttonPanel;

    // Konstruktor für das Hauptmenü-Fenster
    public Hauptmenu() {
        setTitle("Quiz Hauptmenü");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === GUI-Komponenten erstellen ===
        titelLabel = new JLabel("Willkommen zum Quiz!", SwingConstants.CENTER);
        titelLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titelLabel, BorderLayout.NORTH);

        benutzerLabel = new JLabel("Angemeldet als: " + Benutzername.username);
        benutzerLabel.setHorizontalAlignment(SwingConstants.LEFT);
        benutzerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
        add(benutzerLabel, BorderLayout.PAGE_START);
        benutzerLabel.setForeground(StyleManager.getColor("background.color", Color.BLACK));
        benutzerLabel.setBackground(StyleManager.getColor("accent.color", Color.BLACK));

        quizButton = new JButton("Quiz starten");
        frageHinzufuegenButton = new JButton("Frage hinzufügen");

        buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        buttonPanel.add(quizButton);
        buttonPanel.add(frageHinzufuegenButton);
        add(buttonPanel, BorderLayout.CENTER);

        // === Aktionen hinzufügen ===
        quizButton.addActionListener(e -> {
            String[] kategorien = {"Mathe", "Geschichte", "Technik"};
            String auswahl = (String) JOptionPane.showInputDialog(
                    Hauptmenu.this,
                    "Wähle eine Quiz-Kategorie:",
                    "Kategorie auswählen",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    kategorien,
                    kategorien[0]);

            if (auswahl != null) {
                dispose();
                switch (auswahl) {
                    case "Mathe" -> new MatheQuiz();
                    case "Geschichte" -> new GeschichteQuiz();
                    case "Technik" -> new TechnikQuiz();
                    default -> JOptionPane.showMessageDialog(Hauptmenu.this, "Unbekannte Kategorie.");
                }
            }
        });

        frageHinzufuegenButton.addActionListener(e -> {
            new QuizEingabe();
            dispose();
        });

        // Farben anwenden
        buttonPanel.setBackground(StyleManager.getColor("background.color", Color.WHITE));
        Color buttonAndTextBg = StyleManager.getColor("answer.color", Color.LIGHT_GRAY);
        Color textColor = StyleManager.getColor("font.color", Color.WHITE);
        for (Component comp : buttonPanel.getComponents()) {
            if (comp instanceof JButton || comp instanceof JTextField) {
                comp.setBackground(buttonAndTextBg);
                comp.setForeground(textColor);
            }
        }

        // === Dynamisches Resizing der Schriftgrößen ===
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                aktualisiereFonts();
            }
        });

        setVisible(true);
        aktualisiereFonts(); // Initiale Anpassung
    }

    // Schriftgrößen aktualisieren basierend auf Fensterbreite
    private void aktualisiereFonts() {
        int breite = getWidth();
        float faktor = breite / 500.0f;

        int titelFontSize = Math.round(20 * faktor);
        int labelFontSize = Math.round(12 * faktor);
        int buttonFontSize = Math.round(15 * faktor);

        titelLabel.setFont(new Font("Arial", Font.BOLD, titelFontSize));
        benutzerLabel.setFont(new Font("Arial", Font.BOLD, labelFontSize));
        quizButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
        frageHinzufuegenButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
    }

    // Main-Methode zum Starten der Anwendung
    public static void main(String[] args) {
        StyleManager.loadConfig("config.properties");
        SwingUtilities.invokeLater(Hauptmenu::new);
    }
}
