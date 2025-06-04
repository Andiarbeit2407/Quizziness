import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * Klasse für die Eingabe von Quizfragen.
 * Diese Klasse stellt die Benutzeroberfläche und Funktionalität zur Eingabe und Speicherung von Quizfragen bereit.
 */
public class QuizEingabe extends JFrame {

	// Konstanten
	/** Anzahl der Antwortmöglichkeiten */
	private static final int ANSWER_COUNT = 4;
	/** Verfügbare Kategorien für die Fragen */
	private static final String[] CATEGORIES = {
			"Lebewesen", "Naturwissenschaften", "Mathematik", "Allgemeinwissen", "Benutzer"
	};
	/** Zuordnung der Kategorien zu Dateinamen */
	private static final Map<String, String> CATEGORY_FILES = Map.of(
			"Lebewesen", "Lebewesen.txt",
			"Naturwissenschaften", "Naturwissenschaften.txt",
			"Mathematik", "Mathematik.txt",
			"Allgemeinwissen", "Allgemeinwissen.txt",
			"Benutzer", "Benutzer.txt"
	);

	// UI-Komponenten
	/** Eingabefeld für die Frage */
	private JTextField questionField;
	/** Eingabefelder für die Antworten */
	private JTextField[] answerFields;
	/** Checkboxen zur Markierung der richtigen Antworten */
	private JCheckBox[] solutionCheckboxes;
	/** Dropdown zur Auswahl der Kategorie */
	private JComboBox<String> categorySelector;
	/** Button zum Speichern der Frage */
	private JButton saveButton;
	/** Button zum Zurückkehren zum Hauptmenü */
	private JButton backButton;

	/**
	 * Konstruktor für die QuizEingabe.
	 * Initialisiert die Benutzeroberfläche und die Komponenten.
	 */
	public QuizEingabe() {
		initializeCursor();
		setupWindow();
		createComponents();
		layoutComponents();
		setupKeyBindings();
		applyStyling();
		finalizeWindow();
	}

	/**
	 * Initialisiert den benutzerdefinierten Cursor.
	 */
	private void initializeCursor() {
		boolean cursorLoaded = CustomCursorManager.loadCursor("cursor.png", 16, 8);
		if (!cursorLoaded) {
			System.out.println("Cursor konnte nicht geladen werden - verwende Standard");
		}
	}

	/**
	 * Richtet das Fenster ein.
	 */
	private void setupWindow() {
		setTitle("Quiz Eingabe");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setSize(800, 700);
		setLocationRelativeTo(null);
	}

	/**
	 * Erstellt die UI-Komponenten.
	 */
	private void createComponents() {
		// Eingabefelder erstellen mit größeren Dimensionen
		questionField = new JTextField(50);
		questionField.setPreferredSize(new Dimension(600, 35));

		answerFields = new JTextField[ANSWER_COUNT];
		solutionCheckboxes = new JCheckBox[ANSWER_COUNT];

		for (int i = 0; i < ANSWER_COUNT; i++) {
			answerFields[i] = new JTextField(40);
			answerFields[i].setPreferredSize(new Dimension(500, 35));
			solutionCheckboxes[i] = new JCheckBox("Richtig");
		}

		categorySelector = new JComboBox<>(CATEGORIES);
		categorySelector.setPreferredSize(new Dimension(200, 35));

		saveButton = new JButton("Frage speichern");
		saveButton.setPreferredSize(new Dimension(200, 45));

		backButton = new JButton("Zurück zum Hauptmenü");
		backButton.setPreferredSize(new Dimension(200, 45));

		// Action Listener hinzufügen
		saveButton.addActionListener(this::saveQuestion);
		backButton.addActionListener(this::returnToMainMenu);
	}

	/**
	 * Legt die Komponenten im Layout an.
	 */
	private void layoutComponents() {
		JPanel mainPanel = createMainPanel();

		mainPanel.add(createCategorySection());
		mainPanel.add(Box.createVerticalStrut(15));
		mainPanel.add(createQuestionSection());
		mainPanel.add(Box.createVerticalStrut(15));
		mainPanel.add(createAnswersSection());
		mainPanel.add(Box.createVerticalStrut(20));
		mainPanel.add(createButtonSection());

		add(mainPanel);
	}

	/**
	 * Erstellt das Hauptpanel.
	 * @return Das Hauptpanel.
	 */
	private JPanel createMainPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		return panel;
	}

	/**
	 * Erstellt den Bereich zur Auswahl der Kategorie.
	 * @return Das Panel für die Kategorieauswahl.
	 */
	private JPanel createCategorySection() {
		JPanel section = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel categoryLabel = new JLabel("Kategorie:");
		categoryLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		section.add(categoryLabel);
		section.add(Box.createHorizontalStrut(10));
		section.add(categorySelector);
		return section;
	}

	/**
	 * Erstellt den Bereich zur Eingabe der Frage.
	 * @return Das Panel für die Frageeingabe.
	 */
	private JPanel createQuestionSection() {
		JPanel section = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel questionLabel = new JLabel("Frage:");
		questionLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		section.add(questionLabel);
		section.add(Box.createHorizontalStrut(10));
		section.add(questionField);
		return section;
	}

	/**
	 * Erstellt den Bereich zur Eingabe der Antworten.
	 * @return Das Panel für die Antworteneingabe.
	 */
	private JPanel createAnswersSection() {
		JPanel section = new JPanel();
		section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));

		for (int i = 0; i < ANSWER_COUNT; i++) {
			section.add(createSingleAnswerPanel(i));
			if (i < ANSWER_COUNT - 1) {
				section.add(Box.createVerticalStrut(10));
			}
		}

		return section;
	}

	/**
	 * Erstellt ein Panel für eine einzelne Antwort.
	 * @param index Der Index der Antwort.
	 * @return Das Panel für die Antwort.
	 */
	private JPanel createSingleAnswerPanel(int index) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		char answerLetter = (char) ('A' + index);

		JLabel answerLabel = new JLabel("Antwort " + answerLetter + ":");
		answerLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));

		panel.add(answerLabel);
		panel.add(Box.createHorizontalStrut(10));
		panel.add(answerFields[index]);
		panel.add(Box.createHorizontalStrut(10));
		panel.add(solutionCheckboxes[index]);

		return panel;
	}

	/**
	 * Erstellt den Bereich mit den Buttons.
	 * @return Das Panel mit den Buttons.
	 */
	private JPanel createButtonSection() {
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		buttonPanel.add(saveButton);
		buttonPanel.add(backButton);
		return buttonPanel;
	}

	/**
	 * Richtet die Tastaturkürzel ein.
	 */
	private void setupKeyBindings() {
		JPanel mainPanel = (JPanel) getContentPane().getComponent(0);
		InputMap inputMap = mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = mainPanel.getActionMap();

		inputMap.put(KeyStroke.getKeyStroke("ENTER"), "save");
		actionMap.put("save", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveButton.doClick();
			}
		});

		inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "back");
		actionMap.put("back", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				backButton.doClick();
			}
		});
	}

	/**
	 * Wendet die Stile auf die Komponenten an.
	 */
	private void applyStyling() {
		Color primaryBg = StyleManager.getColor("primary.color", Color.WHITE);
		Color secondaryBg = StyleManager.getColor("secondary.color", Color.LIGHT_GRAY);
		Color textColor = StyleManager.getColor("fixedfont.color", Color.WHITE);

		// Größere Schriftarten definieren
		Font textFieldFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		Font buttonFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		Font comboBoxFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		Font checkBoxFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);

		// Hintergrundfarbe für das Hauptpanel setzen
		JPanel mainPanel = (JPanel) getContentPane().getComponent(0);
		mainPanel.setBackground(primaryBg);

		// Alle Sub-Panels ebenfalls auf die Hintergrundfarbe setzen
		setAllPanelBackgrounds(mainPanel, primaryBg);

		// Stile auf Buttons anwenden
		saveButton.setBackground(secondaryBg);
		saveButton.setForeground(textColor);
		saveButton.setFont(buttonFont);

		backButton.setBackground(secondaryBg);
		backButton.setForeground(textColor);
		backButton.setFont(buttonFont);

		// Stile auf Textfelder anwenden
		questionField.setBackground(secondaryBg);
		questionField.setForeground(textColor);
		questionField.setFont(textFieldFont);

		for (JTextField field : answerFields) {
			field.setBackground(secondaryBg);
			field.setForeground(textColor);
			field.setFont(textFieldFont);
		}

		// Stile auf ComboBox anwenden
		categorySelector.setFont(comboBoxFont);

		// Stile auf Checkboxen anwenden
		for (JCheckBox checkBox : solutionCheckboxes) {
			checkBox.setFont(checkBoxFont);
			checkBox.setBackground(primaryBg);
		}
	}

	/**
	 * Setzt die Hintergrundfarbe für alle Panels rekursiv.
	 * @param panel Das Panel, dessen Hintergrund gesetzt werden soll.
	 * @param color Die zu setzende Hintergrundfarbe.
	 */
	private void setAllPanelBackgrounds(Container panel, Color color) {
		panel.setBackground(color);
		for (Component component : panel.getComponents()) {
			if (component instanceof JPanel) {
				setAllPanelBackgrounds((JPanel) component, color);
			}
		}
	}

	/**
	 * Finalisiert das Fenster.
	 */
	private void finalizeWindow() {
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		if (CustomCursorManager.isLoaded()) {
			CustomCursorManager.setCursorEverywhere();
		}

		setVisible(true);
	}

	/**
	 * Speichert die Frage.
	 * @param e Das ActionEvent.
	 */
	private void saveQuestion(ActionEvent e) {
		try {
			QuestionData questionData = collectQuestionData();
			validateQuestionData(questionData);
			writeQuestionToFile(questionData);
			showSuccessMessage(questionData.filename);
		} catch (ValidationException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
		} catch (IOException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Frage!");
		}
	}

	/**
	 * Sammelt die Daten der Frage aus den Eingabefeldern.
	 * @return Die gesammelten Fragebaten.
	 */
	private QuestionData collectQuestionData() {
		String question = questionField.getText().trim();
		String[] answers = new String[ANSWER_COUNT];
		boolean[] solutions = new boolean[ANSWER_COUNT];

		for (int i = 0; i < ANSWER_COUNT; i++) {
			char answerLetter = (char) ('A' + i);
			answers[i] = answerLetter + ") " + answerFields[i].getText().trim();
			solutions[i] = solutionCheckboxes[i].isSelected();
		}

		String category = (String) categorySelector.getSelectedItem();
		String filename = CATEGORY_FILES.getOrDefault(category, "fragen.txt");

		return new QuestionData(question, answers, solutions, filename);
	}

	/**
	 * Validiert die Fragebaten.
	 * @param data Die zu validierenden Fragebaten.
	 * @throws ValidationException Wenn die Validierung fehlschlägt.
	 */
	private void validateQuestionData(QuestionData data) throws ValidationException {
		if (data.question.isEmpty()) {
			throw new ValidationException("Bitte Frage eingeben!");
		}

		for (int i = 0; i < ANSWER_COUNT; i++) {
			if (answerFields[i].getText().trim().isEmpty()) {
				char answerLetter = (char) ('A' + i);
				throw new ValidationException("Bitte Antwort " + answerLetter + " eingeben!");
			}
		}

		boolean hasCorrectAnswer = false;
		for (boolean solution : data.solutions) {
			if (solution) {
				hasCorrectAnswer = true;
				break;
			}
		}

		if (!hasCorrectAnswer) {
			throw new ValidationException("Bitte mindestens eine richtige Antwort markieren!");
		}
	}

	/**
	 * Schreibt die Frage in eine Datei.
	 * @param data Die zu speichernden Fragebaten.
	 * @throws IOException Wenn ein Fehler beim Schreiben der Datei auftritt.
	 */
	private void writeQuestionToFile(QuestionData data) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(data.filename, true))) {
			writer.write(data.question + "\n");

			for (String answer : data.answers) {
				writer.write(answer + "\n");
			}

			writer.write("Richtige Antwort: ");
			for (int i = 0; i < ANSWER_COUNT; i++) {
				if (data.solutions[i]) {
					writer.write((char) ('A' + i) + " ");
				}
			}
			writer.write("\n\n");
		}
	}

	/**
	 * Zeigt eine Erfolgsmeldung an.
	 * @param filename Der Dateiname, in dem die Frage gespeichert wurde.
	 */
	private void showSuccessMessage(String filename) {
		JOptionPane.showMessageDialog(this, "Frage gespeichert in '" + filename + "'!");
	}

	/**
	 * Kehrt zum Hauptmenü zurück.
	 * @param e Das ActionEvent.
	 */
	private void returnToMainMenu(ActionEvent e) {
		dispose();
	}

	/**
	 * Hilfsklasse zur Organisation der Fragebaten.
	 */
	private static class QuestionData {
		final String question;
		final String[] answers;
		final boolean[] solutions;
		final String filename;

		/**
		 * Konstruktor für die Fragebaten.
		 * @param question Die Frage.
		 * @param answers Die Antworten.
		 * @param solutions Die Lösungen.
		 * @param filename Der Dateiname.
		 */
		QuestionData(String question, String[] answers, boolean[] solutions, String filename) {
			this.question = question;
			this.answers = answers;
			this.solutions = solutions;
			this.filename = filename;
		}
	}

	/**
	 * Ausnahmeklasse für Validierungsfehler.
	 */
	private static class ValidationException extends Exception {
		/**
		 * Konstruktor für die Validierungsausnahme.
		 * @param message Die Fehlermeldung.
		 */
		ValidationException(String message) {
			super(message);
		}
	}

	/**
	 * Hauptmethode zum Starten der Anwendung.
	 * @param args Die Kommandozeilenargumente.
	 */
	public static void main(String[] args) {
		StyleManager.loadConfig("config.properties");
		SwingUtilities.invokeLater(QuizEingabe::new);
	}
}