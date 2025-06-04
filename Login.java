import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasse für das Login-Fenster.
 * Diese Klasse stellt die Benutzeroberfläche und Funktionalität für den Login und die Registrierung bereit.
 */
public class Login extends JFrame {

    // Konstanten
    /** Name der Datei, in der die Benutzerdaten gespeichert werden */
    private static final String FILE_NAME = "users.txt";
    /** Schlüssel für die Verschlüsselung der Passwörter */
    private static final int ENCRYPTION_KEY = 33;
    /** Breite des Fensters */
    private static final int WINDOW_WIDTH = 600;
    /** Höhe des Fensters */
    private static final int WINDOW_HEIGHT = 100;
    /** Größe der Eingabefelder */
    private static final Dimension FIELD_SIZE = new Dimension(200, 30);
    /** Schriftgröße */
    private static final int FONT_SIZE = 20;

    // UI-Komponenten
    /** Hauptpanel des Logins */
    private JPanel mainPanel;
    /** Eingabefeld für den Benutzernamen */
    private JTextField nameField;
    /** Eingabefeld für das Passwort */
    private JPasswordField passField;
    /** Button für den Login */
    private JButton loginButton;
    /** Button für die Registrierung */
    private JButton registerButton;
    /** Label für das Namensfeld */
    private JLabel nameLabel;
    /** Label für das Passwortfeld */
    private JLabel passLabel;

    /**
     * Konstruktor für das Login-Fenster.
     * Initialisiert die Anwendung und die Benutzeroberfläche.
     */
    public Login() {
        initializeApplication();
        setupWindow();
        createUIComponents();
        layoutComponents();
        setupEventHandlers();
        applyStyles();
        finalizeSetup();
    }

    /**
     * Initialisiert die Anwendung.
     */
    private void initializeApplication() {
        loadCustomCursor();
    }

    /**
     * Lädt einen benutzerdefinierten Cursor.
     */
    private void loadCustomCursor() {
        boolean cursorLoaded = CustomCursorManager.loadCursor("cursor.png", 16, 8);
        if (!cursorLoaded) {
            System.out.println("Cursor konnte nicht geladen werden - verwende Standard");
        }
    }

    /**
     * Richtet das Fenster ein.
     */
    private void setupWindow() {
        setTitle("Login");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * Erstellt die UI-Komponenten.
     */
    private void createUIComponents() {
        createMainPanel();
        createLabels();
        createInputFields();
        createButtons();
    }

    /**
     * Erstellt das Hauptpanel.
     */
    private void createMainPanel() {
        mainPanel = new JPanel(new GridBagLayout());
    }

    /**
     * Erstellt die Labels.
     */
    private void createLabels() {
        nameLabel = new JLabel("Dein Name:");
        passLabel = new JLabel("Passwort:");
    }

    /**
     * Erstellt die Eingabefelder.
     */
    private void createInputFields() {
        nameField = new JTextField(15);
        nameField.setPreferredSize(FIELD_SIZE);

        passField = new JPasswordField(15);
        passField.setPreferredSize(FIELD_SIZE);
    }

    /**
     * Erstellt die Buttons.
     */
    private void createButtons() {
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(FIELD_SIZE);
        loginButton.addActionListener(this::handleLogin);

        registerButton = new JButton("Registrieren");
        registerButton.setPreferredSize(FIELD_SIZE);
        registerButton.addActionListener(this::handleRegister);
    }

    /**
     * Legt die Komponenten im Layout an.
     */
    private void layoutComponents() {
        GridBagConstraints gbc = createBaseConstraints();

        // Name Label und Feld
        gbc.gridy = 0;
        mainPanel.add(nameLabel, gbc);
        gbc.gridy = 1;
        mainPanel.add(nameField, gbc);

        // Passwort Label und Feld
        gbc.gridy = 2;
        mainPanel.add(passLabel, gbc);
        gbc.gridy = 3;
        mainPanel.add(passField, gbc);

        // Buttons
        gbc.gridy = 4;
        mainPanel.add(loginButton, gbc);
        gbc.gridy = 5;
        mainPanel.add(registerButton, gbc);

        add(mainPanel);
    }

    /**
     * Erstellt die grundlegenden Constraints für das GridBagLayout.
     * @return GridBagConstraints mit Standardwerten.
     */
    private GridBagConstraints createBaseConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.NONE;
        return gbc;
    }

    /**
     * Richtet die Event-Handler ein.
     */
    private void setupEventHandlers() {
        setupKeyboardShortcuts();
    }

    /**
     * Richtet die Tastaturkürzel ein.
     */
    private void setupKeyboardShortcuts() {
        InputMap inputMap = mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = mainPanel.getActionMap();

        // Enter-Taste für Login
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "login");
        actionMap.put("login", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton.doClick();
            }
        });

        // Einfügen-Taste für Registrierung
        inputMap.put(KeyStroke.getKeyStroke("INSERT"), "register");
        actionMap.put("register", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerButton.doClick();
            }
        });
    }

    /**
     * Wendet die Stile auf die Komponenten an.
     */
    private void applyStyles() {
        Color primaryColor = StyleManager.getColor("primary.color", Color.WHITE);
        Color secondaryColor = StyleManager.getColor("secondary.color", Color.LIGHT_GRAY);
        Color textColor = StyleManager.getColor("fixedfont.color", Color.WHITE);

        mainPanel.setBackground(primaryColor);

        // Schriftarten anwenden
        Font labelFont = new Font("Arial", Font.BOLD, FONT_SIZE);
        Font fieldFont = new Font("Arial", Font.PLAIN, FONT_SIZE);
        Font buttonFont = new Font("Arial", Font.BOLD, FONT_SIZE);

        nameLabel.setFont(labelFont);
        passLabel.setFont(labelFont);
        nameField.setFont(fieldFont);
        passField.setFont(fieldFont);
        loginButton.setFont(buttonFont);
        registerButton.setFont(buttonFont);

        // Farben anwenden
        nameLabel.setForeground(textColor);
        passLabel.setForeground(textColor);

        styleInputComponent(nameField, secondaryColor, textColor);
        styleInputComponent(passField, secondaryColor, textColor);
        styleInputComponent(loginButton, secondaryColor, textColor);
        styleInputComponent(registerButton, secondaryColor, textColor);
    }

    /**
     * Wendet einen Stil auf eine Eingabekomponente an.
     * @param component Die Komponente, auf die der Stil angewendet werden soll.
     * @param bgColor Die Hintergrundfarbe.
     * @param fgColor Die Vordergrundfarbe.
     */
    private void styleInputComponent(Component component, Color bgColor, Color fgColor) {
        component.setBackground(bgColor);
        component.setForeground(fgColor);
    }

    /**
     * Finalisiert die Einrichtung.
     */
    private void finalizeSetup() {
        if (CustomCursorManager.isLoaded()) {
            CustomCursorManager.setCursorEverywhere();
        }
        setVisible(true);
    }

    /**
     * Behandelt den Login-Button-Klick.
     * @param e Das ActionEvent.
     */
    private void handleLogin(ActionEvent e) {
        LoginCredentials credentials = getCredentials();
        if (!credentials.isValid()) {
            showErrorMessage("Bitte Name und Passwort eingeben.");
            return;
        }

        try {
            if (authenticateUser(credentials)) {
                showSuccessMessage("Login erfolgreich!");
                proceedToMainMenu(credentials.username);
            } else {
                showErrorMessage("Ungültiger Benutzername oder Passwort.");
            }
        } catch (IOException ex) {
            showErrorMessage("Fehler beim Lesen der Daten.");
            handleException(ex);
        }
    }

    /**
     * Behandelt den Registrierungs-Button-Klick.
     * @param e Das ActionEvent.
     */
    private void handleRegister(ActionEvent e) {
        LoginCredentials credentials = getCredentials();
        if (!credentials.isValid()) {
            showErrorMessage("Bitte Name und Passwort eingeben.");
            return;
        }

        try {
            if (userExists(credentials.username)) {
                showErrorMessage("Benutzername existiert bereits.");
            } else {
                registerNewUser(credentials);
                showSuccessMessage("Registrierung erfolgreich!");
            }
        } catch (IOException ex) {
            showErrorMessage("Fehler beim Speichern der Daten.");
            handleException(ex);
        }
    }

    /**
     * Holt die Anmeldedaten aus den Eingabefeldern.
     * @return Die Anmeldedaten.
     */
    private LoginCredentials getCredentials() {
        String username = nameField.getText().trim();
        String password = new String(passField.getPassword()).trim();
        return new LoginCredentials(username, password);
    }

    /**
     * Wechselt zum Hauptmenü.
     * @param username Der Benutzername.
     */
    private void proceedToMainMenu(String username) {
        Benutzername.username = username;
        dispose();
        new Hauptmenu();
    }

    /**
     * Zeigt eine Fehlermeldung an.
     * @param message Die Fehlermeldung.
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    /**
     * Zeigt eine Erfolgsmeldung an.
     * @param message Die Erfolgsmeldung.
     */
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    /**
     * Behandelt eine Ausnahme.
     * @param ex Die Ausnahme.
     */
    private void handleException(Exception ex) {
        ex.printStackTrace();
    }

    /**
     * Authentifiziert einen Benutzer.
     * @param credentials Die Anmeldedaten.
     * @return true, wenn die Authentifizierung erfolgreich war, sonst false.
     * @throws IOException Wenn ein Fehler beim Lesen der Datei auftritt.
     */
    private boolean authenticateUser(LoginCredentials credentials) throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                UserRecord record = parseUserRecord(line);
                if (record != null && record.username.equals(credentials.username)) {
                    String decryptedPassword = Verschlüsselung.caesarDecrypt(record.encryptedPassword, ENCRYPTION_KEY);
                    if (credentials.password.equals(decryptedPassword)) {
                        Benutzername.points = record.points;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Überprüft, ob ein Benutzer existiert.
     * @param username Der Benutzername.
     * @return true, wenn der Benutzer existiert, sonst false.
     * @throws IOException Wenn ein Fehler beim Lesen der Datei auftritt.
     */
    private boolean userExists(String username) throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                UserRecord record = parseUserRecord(line);
                if (record != null && record.username.equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Registriert einen neuen Benutzer.
     * @param credentials Die Anmeldedaten.
     * @throws IOException Wenn ein Fehler beim Schreiben der Datei auftritt.
     */
    private void registerNewUser(LoginCredentials credentials) throws IOException {
        String encryptedPassword = Verschlüsselung.caesarEncrypt(credentials.password, ENCRYPTION_KEY);
        UserRecord newUser = new UserRecord(credentials.username, encryptedPassword, 0);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(newUser.toFileString());
            writer.newLine();
        }
    }

    /**
     * Parsed einen Benutzerdatensatz aus einer Zeile.
     * @param line Die Zeile.
     * @return Der Benutzerdatensatz oder null, wenn die Zeile ungültig ist.
     */
    private UserRecord parseUserRecord(String line) {
        if (line == null || line.trim().isEmpty()) return null;

        String[] parts = line.split(":", 3);
        if (parts.length < 3) return null;

        try {
            String username = parts[0];
            String encryptedPassword = parts[1];
            int points = Integer.parseInt(parts[2]);
            return new UserRecord(username, encryptedPassword, points);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Aktualisiert die Punkte eines Benutzers.
     * @param username Der Benutzername.
     * @param newPoints Die neuen Punkte.
     * @throws IOException Wenn ein Fehler beim Lesen oder Schreiben der Datei auftritt.
     */
    public static void updateUserPoints(String username, int newPoints) throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            throw new FileNotFoundException("User file not found.");
        }

        List<String> lines = readAllLines(file);
        List<String> updatedLines = updatePointsInLines(lines, username, newPoints);
        writeAllLines(file, updatedLines);
    }

    /**
     * Liest alle Zeilen aus einer Datei.
     * @param file Die Datei.
     * @return Die Zeilen der Datei.
     * @throws IOException Wenn ein Fehler beim Lesen der Datei auftritt.
     */
    private static List<String> readAllLines(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * Aktualisiert die Punkte in den Zeilen.
     * @param lines Die Zeilen.
     * @param username Der Benutzername.
     * @param newPoints Die neuen Punkte.
     * @return Die aktualisierten Zeilen.
     */
    private static List<String> updatePointsInLines(List<String> lines, String username, int newPoints) {
        List<String> updatedLines = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(":", 3);
            if (parts.length < 3) {
                updatedLines.add(line); // Behalte fehlerhafte Zeilen unverändert
                continue;
            }

            if (parts[0].equals(username)) {
                String updatedLine = parts[0] + ":" + parts[1] + ":" + newPoints;
                updatedLines.add(updatedLine);
                Benutzername.points = newPoints;
            } else {
                updatedLines.add(line);
            }
        }

        return updatedLines;
    }

    /**
     * Schreibt alle Zeilen in eine Datei.
     * @param file Die Datei.
     * @param lines Die Zeilen.
     * @throws IOException Wenn ein Fehler beim Schreiben der Datei auftritt.
     */
    private static void writeAllLines(File file, List<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Hilfsklasse zur Organisation der Anmeldedaten.
     */
    private static class LoginCredentials {
        final String username;
        final String password;

        /**
         * Konstruktor für die Anmeldedaten.
         * @param username Der Benutzername.
         * @param password Das Passwort.
         */
        LoginCredentials(String username, String password) {
            this.username = username;
            this.password = password;
        }

        /**
         * Überprüft, ob die Anmeldedaten gültig sind.
         * @return true, wenn die Anmeldedaten gültig sind, sonst false.
         */
        boolean isValid() {
            return !username.isEmpty() && !password.isEmpty();
        }
    }

    /**
     * Hilfsklasse zur Organisation der Benutzerdaten.
     */
    private static class UserRecord {
        final String username;
        final String encryptedPassword;
        final int points;

        /**
         * Konstruktor für die Benutzerdaten.
         * @param username Der Benutzername.
         * @param encryptedPassword Das verschlüsselte Passwort.
         * @param points Die Punkte des Benutzers.
         */
        UserRecord(String username, String encryptedPassword, int points) {
            this.username = username;
            this.encryptedPassword = encryptedPassword;
            this.points = points;
        }

        /**
         * Konvertiert die Benutzerdaten in eine Zeile für die Datei.
         * @return Die Zeile für die Datei.
         */
        String toFileString() {
            return username + ":" + encryptedPassword + ":" + points;
        }
    }

    /**
     * Hauptmethode zum Starten der Anwendung.
     * @param args Die Kommandozeilenargumente.
     */
    public static void main(String[] args) {
        StyleManager.loadConfig("config.properties");
        SwingUtilities.invokeLater(Login::new);
    }
}
