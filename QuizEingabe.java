import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class QuizEingabe extends JFrame {

	private JTextField frageFeld;
	private JTextField[] antwortenFelder = new JTextField[4];
	private JCheckBox[] loesungsFeld = new JCheckBox[4];
	private JComboBox<String> kategorieAuswahl;

	public QuizEingabe() {
		setTitle("Quiz Eingabe");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 500);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Kategorieauswahl
		panel.add(new JLabel("Kategorie:"));
		String[] kategorien = {"Mathe", "Geschichte", "Technik"};
		kategorieAuswahl = new JComboBox<>(kategorien);
		panel.add(kategorieAuswahl);

		// Frage
		panel.add(new JLabel("Frage:"));
		frageFeld = new JTextField(30);
		panel.add(frageFeld);

		// Antworten und Lösungen
		for (int i = 0; i < 4; i++) {
			JPanel antwortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			antwortenFelder[i] = new JTextField(20);
			loesungsFeld[i] = new JCheckBox("Richtig");
			antwortPanel.add(new JLabel("Antwort " + (char) ('A' + i) + ":"));
			antwortPanel.add(antwortenFelder[i]);
			antwortPanel.add(loesungsFeld[i]);
			panel.add(antwortPanel);
		}

		// Buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton speichernButton = new JButton("Frage speichern");
		speichernButton.addActionListener(this::speichernFrage);
		JButton zurueckButton = new JButton("Zurück zum Hauptmenü");
		zurueckButton.addActionListener(this::zurueckZumHauptmenue);
		buttonPanel.add(speichernButton);
		buttonPanel.add(zurueckButton);
		panel.add(Box.createVerticalStrut(10));
		panel.add(buttonPanel);

		// Style
		buttonPanel.setBackground(StyleManager.getColor("primary.color", Color.WHITE));
		Color buttonAndTextBg = StyleManager.getColor("secondary.color", Color.LIGHT_GRAY);
		Color textColor = StyleManager.getColor("fixedfont.color", Color.WHITE);

		for (Component comp : buttonPanel.getComponents()) {
			if (comp instanceof JButton || comp instanceof JTextField) {
				comp.setBackground(buttonAndTextBg);
				comp.setForeground(textColor);
			}
		}

		add(panel);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		// In deinem Konstruktor, nach dem Aufbau der GUI:
		InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = panel.getActionMap();

// Taste 1, L oder ENTER → Login
		inputMap.put(KeyStroke.getKeyStroke("ENTER"), "login");
		actionMap.put("login", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				speichernButton.doClick();
			}
		});

		inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "register");
		actionMap.put("register", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zurueckButton.doClick();
			}
		});

		setVisible(true);
	}

	private void speichernFrage(ActionEvent e) {
		String frage = frageFeld.getText().trim();
		String[] antworten = new String[4];
		boolean[] loesungen = new boolean[4];

		for (int i = 0; i < 4; i++) {
			antworten[i] = (char) ('A' + i) + ") " + antwortenFelder[i].getText().trim();
			loesungen[i] = loesungsFeld[i].isSelected();
		}

		// Validierung
		if (frage.isEmpty() || antworten[0].isEmpty() || antworten[1].isEmpty() ||
				antworten[2].isEmpty() || antworten[3].isEmpty()) {
			JOptionPane.showMessageDialog(this, "Bitte alle Felder ausfüllen!");
			return;
		}

		boolean mindestensEineRichtige = false;
		for (boolean l : loesungen) {
			if (l) {
				mindestensEineRichtige = true;
				break;
			}
		}

		if (!mindestensEineRichtige) {
			JOptionPane.showMessageDialog(this, "Bitte mindestens eine richtige Antwort markieren!");
			return;
		}

		// Datei anhand der Kategorie bestimmen
		String kategorie = (String) kategorieAuswahl.getSelectedItem();
			String dateiname;
			switch (kategorie) {
				case "Mathe":
					dateiname = "Mathe.txt";
					break;
				case "Geschichte":
					dateiname = "Geschichte.txt";
					break;
				case "Technik":
					dateiname = "Technik.txt";
					break;
				default:
					dateiname = "fragen.txt";
					break;
			};

		// Schreiben in Datei
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(dateiname, true))) {
			writer.write(frage + "\n");
			for (String antwort : antworten) {
				writer.write(antwort + "\n");
			}
			writer.write("Richtige Antworten: ");
			for (int i = 0; i < 4; i++) {
				if (loesungen[i]) {
					writer.write((char) ('A' + i) + " ");
				}
			}
			writer.write("\n\n");
			JOptionPane.showMessageDialog(this, "Frage gespeichert in '" + dateiname + "'!");
		} catch (IOException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Frage!");
		}
	}

	private void zurueckZumHauptmenue(ActionEvent e) {
		dispose();
		new Hauptmenu();
	}

	// Main-Methode (optional, falls direkt testbar)
	public static void main(String[] args) {
		StyleManager.loadConfig("config.properties");
		SwingUtilities.invokeLater(QuizEingabe::new);
	}
}
