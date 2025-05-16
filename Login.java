// Importieren der benötigten Klassen für GUI-Elemente und Event-Handling
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Definition der Klasse Login, die ein Fenster (JFrame) darstellt
public class Login extends JFrame {

    // Konstruktor für das Login-Fenster
    public Login() {
        // Setzt den Titel des Fensters
        setTitle("Login");

        // Setzt die Fenstergröße auf 600x300 Pixel
        setSize(600, 100);

        // Zentriert das Fenster auf dem Bildschirm
        setLocationRelativeTo(null);

        // Definiert, dass das Programm beendet wird, wenn das Fenster geschlossen wird
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Erstellt ein Label für den Namen
        JLabel nameLabel = new JLabel("Dein Name:");

        // Setzt die Textfarbe für bessere Lesbarkeit bei dunklem Hintergrund
        nameLabel.setForeground(StyleManager.getColor("fixedfont.color", Color.WHITE));

        // Erstellt ein Textfeld für die Eingabe des Namens
        JTextField nameField = new JTextField(15);

        // Erstellt einen Button zum Bestätigen des Logins
        JButton loginButton = new JButton("Los geht's!");



        // Erstellt ein Panel und fügt das Label, Textfeld und den Button hinzu
        JPanel panel = new JPanel();
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(loginButton);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nameField.setFont(new Font("Arial", Font.BOLD, 20));
        loginButton.setFont(new Font("Arial", Font.BOLD, 20));

        // Fügt das Panel dem Fenster hinzu
        add(panel);
        getRootPane().setDefaultButton(loginButton);



        // Fügt dem Login-Button einen ActionListener hinzu
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Holt den eingegebenen Namen aus dem Textfeld und entfernt Leerzeichen
                String name = nameField.getText().trim();

                // Prüft, ob der Name nicht leer ist
                if (!name.isEmpty()) {
                    // Speichert den Namen in der statischen Variable und öffnet das Hauptmenü
                    Benutzername.username = name;
                    dispose();
                    new Hauptmenu();
                } else {
                    // Zeigt eine Fehlermeldung, wenn kein Name eingegeben wurde
                    JOptionPane.showMessageDialog(null, "Bitte gib deinen Namen ein.");
                }
            }
        });

        panel.setBackground(StyleManager.getColor("primary.color", Color.WHITE));
        Color buttonAndTextBg = StyleManager.getColor("secondary.color", Color.LIGHT_GRAY);
        Color textColor = StyleManager.getColor("fixedfont.color", Color.WHITE);

        // Durchläuft alle Komponenten im Panel
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton || comp instanceof JTextField) {
                comp.setBackground(buttonAndTextBg);
                comp.setForeground(textColor);
            }
        }

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Macht das Fenster sichtbar
        setVisible(true);
    }

    // Main-Methode zum Starten der Anwendung
    public static void main(String[] args) {
        // Lade externe Style-Konfiguration, falls vorhanden
        StyleManager.loadConfig("config.properties");

        // Starte die Login-GUI im Event-Dispatch-Thread
        SwingUtilities.invokeLater(Login::new);
    }

}