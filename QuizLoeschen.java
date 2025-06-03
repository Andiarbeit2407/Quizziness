import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QuizLoeschen extends JFrame {

    private JComboBox<String> kategorieAuswahl;
    private JTextArea fragenAnzeige;
    private JButton loeschenButton;
    private List<String> fragenListe = new ArrayList<>();

    public QuizLoeschen() {

        boolean cursorLoaded = CustomCursorManager.loadCursor("cursor.png", 16, 8);
        if (!cursorLoaded) {
            System.out.println("Cursor konnte nicht geladen werden - verwende Standard");
        }

        setTitle("Quiz Frage löschen");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Kategorieauswahl
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Kategorie:"));
        String[] kategorien = {"Lebewesen", "Naturwissenschaften", "Mathematik", "Allgemeinwissen", "Benutzer"};
        kategorieAuswahl = new JComboBox<>(kategorien);
        kategorieAuswahl.addActionListener(this::ladeFragen);
        topPanel.add(kategorieAuswahl);
        panel.add(topPanel, BorderLayout.NORTH);

        // Fragenanzeige
        fragenAnzeige = new JTextArea();
        fragenAnzeige.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(fragenAnzeige);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button
        loeschenButton = new JButton("Frage löschen");
        loeschenButton.addActionListener(this::loescheFrage);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(loeschenButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        JButton zurueckButton = new JButton("Zurück zum Hauptmenü");
        zurueckButton.addActionListener(this::zurueckZumHauptmenue);
        bottomPanel.add(zurueckButton);
        panel.add(bottomPanel, BorderLayout.AFTER_LAST_LINE);



        add(panel);

        // In deinem Konstruktor, nach dem Aufbau der GUI:
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

        if (CustomCursorManager.isLoaded()) {
            CustomCursorManager.setCursorEverywhere();
        }

        setVisible(true);
        ladeFragen(null);
    }

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
    private void zurueckZumHauptmenue(ActionEvent e) {
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizLoeschen::new);
    }
}
