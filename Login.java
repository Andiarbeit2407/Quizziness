import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Login extends JFrame {

    private static final String FILE_NAME = "users.txt";
    private static final int KEY = 33;

    public Login() {
        setTitle("Login");
        setSize(600, 100);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // GUI Layout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(StyleManager.getColor("primary.color", Color.WHITE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.NONE;

        // Name Label
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Dein Name:");
        nameLabel.setForeground(StyleManager.getColor("fixedfont.color", Color.WHITE));
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(nameLabel, gbc);

        // Namensfeld
        gbc.gridy = 1;
        JTextField nameField = new JTextField(15);
        nameField.setPreferredSize(new Dimension(200, 30));
        nameField.setFont(new Font("Arial", Font.PLAIN, 20));
        panel.add(nameField, gbc);

        // Passwort Label
        gbc.gridy = 2;
        JLabel passLabel = new JLabel("Passwort:");
        passLabel.setForeground(StyleManager.getColor("fixedfont.color", Color.WHITE));
        passLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(passLabel, gbc);

        // Passwortfeld
        gbc.gridy = 3;
        JPasswordField passField = new JPasswordField(15);
        passField.setPreferredSize(new Dimension(200, 30));
        passField.setFont(new Font("Arial", Font.PLAIN, 20));
        panel.add(passField, gbc);

        // Login-Button
        gbc.gridy = 4;
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(200, 30));
        loginButton.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(loginButton, gbc);

        // Registrieren-Button
        gbc.gridy = 5;
        JButton registerButton = new JButton("Registrieren");
        registerButton.setPreferredSize(new Dimension(200, 30));
        registerButton.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(registerButton, gbc);

        // Farben für Textfelder & Buttons
        Color buttonAndTextBg = StyleManager.getColor("secondary.color", Color.LIGHT_GRAY);
        Color textColor = StyleManager.getColor("fixedfont.color", Color.WHITE);

        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton || comp instanceof JTextField) {
                comp.setBackground(buttonAndTextBg);
                comp.setForeground(textColor);
            }
        }

        // Action für Registrierung
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

        // Action für Login
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
                    Benutzername.username = username;
                    dispose();
                    new Hauptmenu();  // Hauptmenü starten
                } else {
                    JOptionPane.showMessageDialog(this, "Ungültiger Benutzername oder Passwort.");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Fehler beim Lesen der Daten.");
                ex.printStackTrace();
            }
        });

        add(panel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    // Prüft, ob ein Benutzer bereits existiert
    private boolean userExists(String username) throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2 && parts[0].equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Registriert neuen Benutzer (verschlüsselt Passwort)
    private void registerUser(String username, String password) throws IOException {
        String encryptedPassword = Verschlüsselung.caesarEncrypt(password, KEY);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(username + ":" + encryptedPassword);
            writer.newLine();
        }
    }

    // Prüft Login durch Passwort-Vergleich
    private boolean loginUser(String username, String password) throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length != 2) continue;

                if (parts[0].equals(username)) {
                    String decryptedPass = Verschlüsselung.caesarDecrypt(parts[1], KEY);
                    return password.equals(decryptedPass);
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        StyleManager.loadConfig("config.properties");
        SwingUtilities.invokeLater(Login::new);
    }
}
