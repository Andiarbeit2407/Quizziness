// Importieren der benötigten Klassen für GUI, Layout, Event-Handling und Dateiverarbeitung
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

// Klasse für die Eingabe neuer Quizfragen
public class QuizEingabe extends JFrame {

	// Textfeld für die Eingabe der Frage
	private JTextField frageFeld;

	// Array von Textfeldern für die vier Antwortmöglichkeiten
	private JTextField[] antwortenFelder = new JTextField[4];

	// Konstruktor zum Erstellen des Eingabefensters
	public QuizEingabe() {
		setTitle("Quiz Eingabe");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 400);
		setLocationRelativeTo(null);

		// Panel mit einem GridLayout zur Anordnung der Felder und Buttons
		JPanel panel = new JPanel(new GridLayout(7, 1));

		// Textfeld zur Eingabe der Frage
		frageFeld = new JTextField(30);
		panel.add(new JLabel("Frage:"));
		panel.add(frageFeld);

		// Textfelder zur Eingabe der vier Antwortmöglichkeiten
		for (int i = 0; i < 4; i++) {
			antwortenFelder[i] = new JTextField(30);
			panel.add(new JLabel("Antwort " + (char) ('A' + i) + ":"));
			panel.add(antwortenFelder[i]);
		}

		// Button zum Speichern der eingegebenen Frage
		JButton speichernButton = new JButton("Frage speichern");
		speichernButton.addActionListener(this::speichernFrage);
		panel.add(speichernButton);

		// Button zum Zurückkehren ins Hauptmenü
		JButton zurueckButton = new JButton("Zurück zum Hauptmenü");
		zurueckButton.addActionListener(this::zurueckZumHauptmenue);
		panel.add(zurueckButton);

		// Panel dem Fenster hinzufügen
		add(panel);

		// Fenster sichtbar machen
		setVisible(true);
	}

	// Methode zum Speichern der Frage und Antworten in eine Datei
	private void speichernFrage(ActionEvent e) {
		String frage = frageFeld.getText();
		String[] antworten = new String[4];

		// Antworttexte mit "A)", "B)", "C)" und "D)" prefixen
		for (int i = 0; i < 4; i++) {
			antworten[i] = (char) ('A' + i) + ") " + antwortenFelder[i].getText();
		}

		// Überprüfen, ob alle Felder ausgefüllt wurden
		if (frage.isEmpty() || antworten[0].isEmpty() || antworten[1].isEmpty() || antworten[2].isEmpty() || antworten[3].isEmpty()) {
			JOptionPane.showMessageDialog(this, "Bitte alle Felder ausfüllen!");
			return;
		}

		// Speichern der Frage und Antworten in die Datei "fragen.txt"
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("fragen.txt", true))) {
			writer.write(frage + "\n");
			for (String antwort : antworten) {
				writer.write(antwort + "\n");
			}
			writer.write("\n"); // Leere Zeile zwischen den Fragen
			JOptionPane.showMessageDialog(this, "Frage gespeichert!");
		} catch (IOException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Frage!");
		}
	}

	// Methode zum Zurückkehren ins Hauptmenü
	private void zurueckZumHauptmenue(ActionEvent e) {
		dispose(); // Aktuelles Fenster schließen
		new Hauptmenu(); // Neues Hauptmenü öffnen
	}

	// Main-Methode zum Starten der Anwendung
	public static void main(String[] args) {
		// Startet die GUI im Event-Dispatch-Thread
		SwingUtilities.invokeLater(QuizEingabe::new);
	}
}
