// Importieren der benötigten Klassen für GUI-Elemente und Event-Handling
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
// Definition der Klasse Login, die ein Fenster (JFrame) darstellt
public class Login extends JFrame {

    private static final String FILE_NAME = "users.txt";
    private static final int KEY = 33;

    // Konstruktor für das Login-Fenster
    public Login() {

        boolean cursorLoaded = CustomCursorManager.loadCursor("cursor.png", 16, 8);
        if (!cursorLoaded) {
            System.out.println("Cursor konnte nicht geladen werden - verwende Standard");
        }

        // Setzt den Titel des Fensters
        setTitle("Login");

        // Setzt die Fenstergröße auf 600x300 Pixel
        setSize(600, 100);

        // Zentriert das Fenster auf dem Bildschirm
        setLocationRelativeTo(null);

        // Definiert, dass das Programm beendet wird, wenn das Fenster geschlossen wird
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(StyleManager.getColor("primary.color", Color.WHITE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0); // Abstand zwischen den Elementen
        gbc.fill = GridBagConstraints.NONE;

// Name-Label
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Dein Name:");
        nameLabel.setForeground(StyleManager.getColor("fixedfont.color", Color.WHITE));
        panel.add(nameLabel, gbc);

// Namensfeld
        gbc.gridy = 1;
        JTextField nameField = new JTextField(15);
        nameField.setPreferredSize(new Dimension(200, 30));
        panel.add(nameField, gbc);

// Passwort-Label
        gbc.gridy = 2;
        JLabel passLabel = new JLabel("Passwort:");
        passLabel.setForeground(StyleManager.getColor("fixedfont.color", Color.WHITE));
        panel.add(passLabel, gbc);

// Passwortfeld
        gbc.gridy = 3;
        JPasswordField passField = new JPasswordField(15);
        passField.setPreferredSize(new Dimension(200, 30));
        panel.add(passField, gbc);

// Login-Button
        gbc.gridy = 4;
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(200, 30));
        panel.add(loginButton, gbc);

// Registrieren-Button
        gbc.gridy = 5;
        JButton registerButton = new JButton("Registrieren");
        registerButton.setPreferredSize(new Dimension(200, 30));
        panel.add(registerButton, gbc);

        // Setzt die Textfarbe für bessere Lesbarkeit bei dunklem Hintergrund
        nameLabel.setForeground(StyleManager.getColor("fixedfont.color", Color.WHITE));
        // Setzt die Textfarbe für bessere Lesbarkeit bei dunklem Hintergrund
        passLabel.setForeground(StyleManager.getColor("fixedfont.color", Color.WHITE));

        // Style (using your StyleManager or default colors)
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        passLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nameField.setFont(new Font("Arial", Font.PLAIN, 20));
        passField.setFont(new Font("Arial", Font.PLAIN, 20));
        loginButton.setFont(new Font("Arial", Font.BOLD, 20));
        registerButton.setFont(new Font("Arial", Font.BOLD, 20));

        add(panel);

        // Action for Register button
        registerButton.addActionListener(e -> {
            String username = nameField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bitte Name und Passwort eingeben.");
                return;
            }

            try {
                if (userExists(username)) {
                    JOptionPane.showMessageDialog(this, "Benutzername existiert bereits.");
                } else {
                    registerUser(username, password);
                    JOptionPane.showMessageDialog(this, "Registrierung erfolgreich!");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Daten.");
                ex.printStackTrace();
            }
        });

        // Action for Login button
        loginButton.addActionListener(e -> {
            String username = nameField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bitte Name und Passwort eingeben.");
                return;
            }

            try {
                if (loginUser(username, password)) {
                    JOptionPane.showMessageDialog(this, "Login erfolgreich!");
                    // Open your main menu or next window here
                    Benutzername.username = username;  // Hier sicher setzen
                    dispose();
                    new Hauptmenu(); // your existing main menu class
                } else {
                    JOptionPane.showMessageDialog(this, "Ungültiger Benutzername oder Passwort.");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Fehler beim Lesen der Daten.");
                ex.printStackTrace();
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

// In deinem Konstruktor, nach dem Aufbau der GUI:
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = panel.getActionMap();

// Taste 1, L oder ENTER → Login
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "login");
        actionMap.put("login", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton.doClick();
            }
        });

// Taste 2 oder R → Registrieren
        inputMap.put(KeyStroke.getKeyStroke("INSERT"), "register");
        actionMap.put("register", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerButton.doClick();
            }
        });

        if (CustomCursorManager.isLoaded()) {
            CustomCursorManager.setCursorEverywhere();
        }

        // Macht das Fenster sichtbar
        setVisible(true);
    }

    // Prüft, ob ein Benutzer bereits existiert (inklusive Punktefeld)
    private boolean userExists(String username) throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // In 3 Teile aufteilen: username : verschlüsseltesPasswort : punkte
                String[] parts = line.split(":", 3);
                if (parts.length >= 1 && parts[0].equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Registriert einen neuen Benutzer mit initial 0 Punkten
    private void registerUser(String username, String password) throws IOException {
        String encryptedPassword = Verschlüsselung.caesarEncrypt(password, KEY);
        int points = 0; // Startpunkte
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(username + ":" + encryptedPassword + ":" + points);
            writer.newLine();
        }
    }

    // Meldet einen Benutzer an und liest dessen Punkte aus (falls vorhanden)
    private boolean loginUser(String username, String password) throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // In 3 Teile aufteilen: username : verschlüsseltesPasswort : punkte
                String[] parts = line.split(":", 3);
                if (parts.length < 3) continue;

                if (parts[0].equals(username)) {
                    String decryptedPass = Verschlüsselung.caesarDecrypt(parts[1], KEY);
                    if (password.equals(decryptedPass)) {
                        // Punkte einlesen und in der Benutzerklasse hinterlegen
                        int points = Integer.parseInt(parts[2]);
                        Benutzername.points = points;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Aktualisiert die Punkte eines bestehenden Benutzers in der Datei
    public static void updateUserPoints(String username, int newPoints) throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            throw new FileNotFoundException("User file not found.");
        }

        // Alle Zeilen einlesen
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 3);
                if (parts.length < 3) {
                    // Behalte fehlerhafte Zeilen bei (oder ignoriere sie)
                    lines.add(line);
                    continue;
                }

                if (parts[0].equals(username)) {
                    // Ersetze das Punktefeld durch newPoints
                    String updatedLine = parts[0] + ":" + parts[1] + ":" + newPoints;
                    lines.add(updatedLine);
                    // Setze auch die Punkte im Benutzerobjekt
                    Benutzername.points = newPoints;
                } else {
                    lines.add(line);
                }
            }
        }

        // Alle Zeilen zurück in die Datei schreiben (Überschreiben)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
        }
    }

    // Main-Methode zum Starten der Anwendung
    public static void main(String[] args) {
        // Lade externe Style-Konfiguration, falls vorhanden
        StyleManager.loadConfig("config.properties");

        // Starte die Login-GUI im Event-Dispatch-Thread
        SwingUtilities.invokeLater(Login::new);
    }

    // Konstruktor und weitere GUI-Logik hier (nicht gezeigt)
}