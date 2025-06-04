import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Die Klasse Leaderboard zeigt eine Rangliste der Benutzer mit ihren Punkten an.
 * Sie lädt die Benutzerdaten aus einer Datei und zeigt sie in einer Tabelle an.
 */
public class Leaderboard extends JFrame {

	// Konstante für den Dateinamen der Benutzerdaten
	private static final String FILE_NAME = "users.txt";

	/**
	 * Konstruktor für das Leaderboard-Fenster.
	 * Initialisiert das Fenster, lädt die Benutzerdaten und zeigt sie in einer Tabelle an.
	 */
	public Leaderboard() {
		// Versuche, einen benutzerdefinierten Cursor zu laden
		boolean cursorLoaded = CustomCursorManager.loadCursor("cursor.png", 16, 8);
		if (!cursorLoaded) {
			System.out.println("Cursor konnte nicht geladen werden - verwende Standard");
		}

		// Fenster-Eigenschaften setzen
		setTitle("Leaderboard");
		setSize(600, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Daten vorbereiten
		String[] columnNames = {"Benutzername", "Punkte"};
		List<UserData> users = loadUsers();
		users.sort(Comparator.comparingInt(UserData::getPoints).reversed());

		// Daten für die Tabelle vorbereiten
		Object[][] tableData = new Object[users.size()][2];
		for (int i = 0; i < users.size(); i++) {
			tableData[i][0] = users.get(i).getUsername();
			tableData[i][1] = users.get(i).getPoints();
		}

		// Tabelle erstellen und konfigurieren
		DefaultTableModel model = new DefaultTableModel(tableData, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Zellen nicht editierbar machen
			}
		};

		JTable table = new JTable(model);
		table.setFillsViewportHeight(true);
		table.setFont(new Font("Arial", Font.PLAIN, 25));
		table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 30));
		table.setRowHeight(35);

		// Tabelle in einen ScrollPane einbetten
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		// Schließen-Button erstellen und konfigurieren
		JButton zurueckButton = new JButton("Zurück zum Hauptmenü");
		zurueckButton.setFont(new Font("Arial", Font.PLAIN, 24));
		zurueckButton.addActionListener(this::zurueckZumHauptmenue);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		buttonPanel.add(zurueckButton);
		add(buttonPanel, BorderLayout.SOUTH);

		// Tastaturkürzel für den Zurück-Button hinzufügen
		InputMap inputMap = buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = buttonPanel.getActionMap();

		inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "back");
		actionMap.put("back", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zurueckButton.doClick();
			}
		});

		// Benutzerdefinierten Cursor anwenden, falls geladen
		if (CustomCursorManager.isLoaded()) {
			CustomCursorManager.setCursorEverywhere();
		}

		// Fenster sichtbar machen
		setVisible(true);
	}

	/**
	 * Innere Klasse zur Speicherung von Benutzerdaten.
	 */
	private static class UserData {
		private final String username;
		private final int points;

		/**
		 * Konstruktor für UserData.
		 *
		 * @param username Benutzername
		 * @param points Punkte des Benutzers
		 */
		public UserData(String username, int points) {
			this.username = username;
			this.points = points;
		}

		/**
		 * Gibt den Benutzernamen zurück.
		 *
		 * @return Benutzername
		 */
		public String getUsername() {
			return username;
		}

		/**
		 * Gibt die Punkte des Benutzers zurück.
		 *
		 * @return Punkte des Benutzers
		 */
		public int getPoints() {
			return points;
		}
	}

	/**
	 * Lädt die Benutzerdaten aus einer Datei.
	 *
	 * @return Liste von UserData-Objekten
	 */
	private List<UserData> loadUsers() {
		List<UserData> users = new ArrayList<>();
		File file = new File(FILE_NAME);
		if (!file.exists()) {
			JOptionPane.showMessageDialog(this, "Benutzerdaten-Datei nicht gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
			return users;
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(":", 3);
				if (parts.length == 3) {
					String username = parts[0];
					int points;
					try {
						points = Integer.parseInt(parts[2]);
					} catch (NumberFormatException e) {
						points = 0;
					}
					users.add(new UserData(username, points));
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Fehler beim Lesen der Benutzerdaten.", "Fehler", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return users;
	}

	/**
	 * Methode zum Schließen des Fensters und Zurückkehren zum Hauptmenü.
	 *
	 * @param e ActionEvent
	 */
	private void zurueckZumHauptmenue(ActionEvent e) {
		dispose();
	}
}
