import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Login extends JFrame {

    private static final String FILE_NAME = "users.txt";
    private static final int KEY = 33;

    public Login() {
        setTitle("Login");

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(StyleManager.getColor("primary.color", Color.WHITE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.NONE;

        JLabel nameLabel = new JLabel("Dein Name:");
        nameLabel.setForeground(StyleManager.getColor("fixedfont.color", Color.WHITE));
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(15);
        nameField.setPreferredSize(new Dimension(200, 30));
        nameField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridy = 1;
        panel.add(nameField, gbc);

        JLabel passLabel = new JLabel("Passwort:");
        passLabel.setForeground(StyleManager.getColor("fixedfont.color", Color.WHITE));
        passLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 2;
        panel.add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(15);
        passField.setPreferredSize(new Dimension(200, 30));
        passField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridy = 3;
        panel.add(passField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(200, 30));
        loginButton.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 4;
        panel.add(loginButton, gbc);

        JButton registerButton = new JButton("Registrieren");
        registerButton.setPreferredSize(new Dimension(200, 30));
        registerButton.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 5;
        panel.add(registerButton, gbc);

        Color buttonAndTextBg = StyleManager.getColor("secondary.color", Color.LIGHT_GRAY);
        Color textColor = StyleManager.getColor("fixedfont.color", Color.WHITE);

        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton || comp instanceof JTextField || comp instanceof JPasswordField) {
                comp.setBackground(buttonAndTextBg);
                comp.setForeground(textColor);
            }
        }

        add(panel);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Tastenkürzel
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = panel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "login");
        actionMap.put("login", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton.doClick();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke('R'), "register");
        actionMap.put("register", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerButton.doClick();
            }
        });

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
                    new Hauptmenu();
                } else {
                    JOptionPane.showMessageDialog(this, "Ungültiger Benutzername oder Passwort.");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Fehler beim Lesen der Daten.");
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }

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

    private void registerUser(String username, String password) throws IOException {
        String encryptedPassword = Verschlüsselung.caesarEncrypt(password, KEY);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(username + ":" + encryptedPassword);
            writer.newLine();
        }
    }

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
