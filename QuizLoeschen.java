import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse QuizLoeschen ermöglicht das Löschen von Fragen aus verschiedenen Kategorien eines Quiz.
 * Sie bietet eine grafische Benutzeroberfläche zur Auswahl der Kategorie, Anzeige der Fragen und Löschung ausgewählter Fragen.
 */
public class QuizLoeschen extends JFrame {

    private JComboBox<String> kategorieAuswahl; // Dropdown zur Auswahl der Kategorie
    private JTextArea fragenAnzeige; // Textbereich zur Anzeige der Fragen
    private JButton loeschenButton; // Button zum Löschen einer Frage
    private List<String> fragenListe = new ArrayList<>(); // Liste zur Speicherung der Fragen

    /**
     * Konstruktor für die QuizLoeschen-Klasse.
     * Initialisiert das Hauptfenster und fügt die notwendigen Komponenten hinzu.
     */
    public QuizLoeschen() {
        // Lädt einen benutzerdefinierten Cursor
        boolean cursorLoaded = CustomCursorManager.loadCursor("cursor.png", 16, 8);
        if (!cursorLoaded) {
            System.out.println("Cursor konnte nicht geladen werden - verwende Standard");
        }

        // Fenster-Einstellungen
        setTitle("Quiz Frage löschen");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);

        // Haupt-Panel mit BorderLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel für die Kategorieauswahl
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Kategorie:"));
        String[] kategorien = {"Lebewesen", "Naturwissenschaften", "Mathematik", "Allgemeinwissen", "Benutzer"};
        kategorieAuswahl = new JComboBox<>(kategorien);
        kategorieAuswahl.addActionListener(this::ladeFragen);
        topPanel.add(kategorieAuswahl);
        panel.add(topPanel, BorderLayout.NORTH);

        // Textbereich für die Anzeige der Fragen
        fragenAnzeige = new JTextArea();
        fragenAnzeige.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(fragenAnzeige);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel für die Buttons
        loeschenButton = new JButton("Frage löschen");
        loeschenButton.addActionListener(this::loescheFrage);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(loeschenButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Button zum Zurückkehren zum Hauptmenü
        JButton zurueckButton = new JButton("Zurück zum Hauptmenü");
        zurueckButton.addActionListener(this::zurueckZumHauptmenue);
        bottomPanel.add(zurueckButton);
        panel.add(bottomPanel, BorderLayout.AFTER_LAST_LINE);

        add(panel);

        // Tastaturkürzel für Löschen und Zurück
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = panel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "delete");
        actionMap.put("delete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loeschenButton.doClick();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "back");
        actionMap.put("back", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zurueckButton.doClick();
            }
        });

        // Setzt den benutzerdefinierten Cursor, falls geladen
        if (CustomCursorManager.isLoaded()) {
            CustomCursorManager.setCursorEverywhere();
        }

        setVisible(true);
        ladeFragen(null);
    }

    /**
     * Lädt die Fragen aus der ausgewählten Kategorie und zeigt sie im Textbereich an.
     *
     * @param e Das ActionEvent, das die Methode ausgelöst hat.
     */
    private void ladeFragen(ActionEvent e) {
        fragenListe.clear();
        fragenAnzeige.setText("");
        String dateiname = gibDateiname();

        try (BufferedReader reader = new BufferedReader(new FileReader(dateiname))) {
            String line;
            StringBuilder frageBlock = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (frageBlock.length() > 0) {
                        fragenListe.add(frageBlock.toString());
                        frageBlock.setLength(0);
                    }
                } else {
                    frageBlock.append(line).append("\n");
                }
            }

            if (frageBlock.length() > 0) {
                fragenListe.add(frageBlock.toString());
            }

            for (int i = 0; i < fragenListe.size(); i++) {
                fragenAnzeige.append("[" + i + "]\n" + fragenListe.get(i) + "\n");
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Datei: " + ex.getMessage());
        }
    }

    /**
     * Löscht eine ausgewählte Frage aus der Liste und aktualisiert die Datei.
     *
     * @param e Das ActionEvent, das die Methode ausgelöst hat.
     */
    private void loescheFrage(ActionEvent e) {
        String eingabe = JOptionPane.showInputDialog(this, "Gib die Nummer der zu löschenden Frage ein:");
        if (eingabe == null || eingabe.isEmpty()) return;

        try {
            int index = Integer.parseInt(eingabe);
            if (index < 0 || index >= fragenListe.size()) {
                JOptionPane.showMessageDialog(this, "Ungültiger Index!");
                return;
            }

            fragenListe.remove(index);
            schreibeFragenInDatei();
            JOptionPane.showMessageDialog(this, "Frage gelöscht.");
            ladeFragen(null);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Bitte eine gültige Zahl eingeben.");
        }
    }

    /**
     * Schreibt die aktuelle Liste der Fragen zurück in die Datei.
     */
    private void schreibeFragenInDatei() {
        String dateiname = gibDateiname();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dateiname))) {
            for (String frage : fragenListe) {
                writer.write(frage.trim());
                writer.write("\n\n");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Fehler beim Schreiben der Datei: " + ex.getMessage());
        }
    }

    /**
     * Gibt den Dateinamen basierend auf der ausgewählten Kategorie zurück.
     *
     * @return Der Dateiname der ausgewählten Kategorie.
     */
    private String gibDateiname() {
        String kategorie = (String) kategorieAuswahl.getSelectedItem();
        switch (kategorie) {
            case "Lebewesen": return "Lebewesen.txt";
            case "Naturwissenschaften": return "Naturwissenschaften.txt";
            case "Mathematik": return "Mathematik.txt";
            case "Allgemeinwissen": return "Allgemeinwissen.txt";
            case "Benutzer": return "Benutzer.txt";
            default: return "fragen.txt";
        }
    }

    /**
     * Kehrt zum Hauptmenü zurück.
     *
     * @param e Das ActionEvent, das die Methode ausgelöst hat.
     */
    private void zurueckZumHauptmenue(ActionEvent e) {
        dispose();
    }

    /**
     * Die main-Methode, die die Anwendung startet.
     *
     * @param args Kommandozeilenargumente (werden nicht verwendet).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizLoeschen::new);
    }
}
