import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class QuizEingabe extends JFrame {
	private JTextField frageFeld;
	private JTextField[] antwortenFelder = new JTextField[4];

	public QuizEingabe() {
		setTitle("Quiz Eingabe");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 400);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(new GridLayout(7, 1));

		frageFeld = new JTextField(30);
		panel.add(new JLabel("Frage:"));
		panel.add(frageFeld);

		for (int i = 0; i < 4; i++) {
			antwortenFelder[i] = new JTextField(30);
			panel.add(new JLabel("Antwort " + (char) ('A' + i) + ":"));
			panel.add(antwortenFelder[i]);
		}

		JButton speichernButton = new JButton("Frage speichern");
		speichernButton.addActionListener(this::speichernFrage);
		panel.add(speichernButton);

		JButton zurueckButton = new JButton("Zurück zum Hauptmenü");
		zurueckButton.addActionListener(this::zurueckZumHauptmenue);
		panel.add(zurueckButton);

		add(panel);
		setVisible(true);
	}

	private void speichernFrage(ActionEvent e) {
		String frage = frageFeld.getText();
		String[] antworten = new String[4];

		for (int i = 0; i < 4; i++) {
			antworten[i] = (char) ('A' + i) + ") " + antwortenFelder[i].getText();
		}

		if (frage.isEmpty() || antworten[0].isEmpty() || antworten[1].isEmpty() || antworten[2].isEmpty() || antworten[3].isEmpty()) {
			JOptionPane.showMessageDialog(this, "Bitte alle Felder ausfüllen!");
			return;
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter("fragen.txt", true))) {
			writer.write(frage + "\n");
			for (String antwort : antworten) {
				writer.write(antwort + "\n");
			}
			writer.write("\n");
			JOptionPane.showMessageDialog(this, "Frage gespeichert!");
		} catch (IOException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Frage!");
		}
	}

	private void zurueckZumHauptmenue(ActionEvent e) {
		dispose();
		new Hauptmenu();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(QuizEingabe::new);
	}
}
