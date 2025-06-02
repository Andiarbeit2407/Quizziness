import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import java.util.List;

public class Leaderboard extends JFrame {

	private static final String FILE_NAME = "users.txt";

	public Leaderboard() {

		boolean cursorLoaded = CustomCursorManager.loadCursor("cursor.png", 16, 8);
		if (!cursorLoaded) {
			System.out.println("Cursor konnte nicht geladen werden - verwende Standard");
		}

		setTitle("Leaderboard");
		setSize(600, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Daten vorbereiten
		String[] columnNames = {"Benutzername", "Punkte"};
		List<UserData> users = loadUsers();
		users.sort(Comparator.comparingInt(UserData::getPoints).reversed());

		Object[][] tableData = new Object[users.size()][2];
		for (int i = 0; i < users.size(); i++) {
			tableData[i][0] = users.get(i).getUsername();
			tableData[i][1] = users.get(i).getPoints();
		}

		// Tabelle
		DefaultTableModel model = new DefaultTableModel(tableData, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		JTable table = new JTable(model);
		table.setFillsViewportHeight(true);
		table.setFont(new Font("Arial", Font.PLAIN, 25));
		table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 30));
		table.setRowHeight(35);

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		// Schließen-Button
		JButton zurueckButton = new JButton("Zurück zum Hauptmenü");
		zurueckButton.setFont(new Font("Arial", Font.PLAIN, 24));
		zurueckButton.addActionListener(this::zurueckZumHauptmenue);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		buttonPanel.add(zurueckButton);
		add(buttonPanel, BorderLayout.SOUTH);

		// In deinem Konstruktor, nach dem Aufbau der GUI:
		InputMap inputMap = buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = buttonPanel.getActionMap();

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
	}

	private static class UserData {
		private final String username;
		private final int points;

		public UserData(String username, int points) {
			this.username = username;
			this.points = points;
		}

		public String getUsername() {
			return username;
		}

		public int getPoints() {
			return points;
		}
	}

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
	private void zurueckZumHauptmenue(ActionEvent e) {
		dispose();
	}
}
