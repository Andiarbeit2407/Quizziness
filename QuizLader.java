import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Klasse zum Laden von Quizfragen aus einer Datei.
 * Diese Klasse stellt Methoden zum Einlesen und Parsen von Quizfragen bereit.
 */
public class QuizLader {

	/** Logger für die Protokollierung von Informationen und Fehlern */
	private static final Logger logger = Logger.getLogger(QuizLader.class.getName());

	/** Standardzeit für Fragen in Sekunden */
	private static final int DEFAULT_TIME_SECONDS = 20;

	/** Erwartete Anzahl von Antworten pro Frage */
	private static final int EXPECTED_ANSWERS = 4;

	/** Präfix für die Zeile mit den richtigen Antworten */
	private static final String CORRECT_ANSWER_PREFIX = "Richtige Antwort:";

	/** Präfix für Antwortzeilen */
	private static final String ANSWER_PREFIX = "   ";

	/**
	 * Lädt alle Quizfragen aus einer Datei.
	 *
	 * @param dateiname Der Dateiname, aus dem die Fragen geladen werden sollen
	 * @return Liste von QuizDaten-Objekten, leere Liste, wenn die Datei nicht gelesen werden kann
	 */
	public static List<QuizDaten> ladeAlleFragen(String dateiname) {
		List<QuizDaten> fragenListe = new ArrayList<>();

		try {
			List<String> lines = Files.readAllLines(Paths.get(dateiname));
			fragenListe = parseQuizLines(lines);
			logger.info("Erfolgreich " + fragenListe.size() + " Fragen aus " + dateiname + " geladen");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Fehler beim Lesen der Quizdatei: " + dateiname, e);
		}

		return fragenListe;
	}

	/**
	 * Parsed Quizdaten aus einer Liste von Zeilen.
	 *
	 * @param lines Die zu parsenden Zeilen
	 * @return Liste von QuizDaten-Objekten
	 */
	private static List<QuizDaten> parseQuizLines(List<String> lines) {
		List<QuizDaten> questions = new ArrayList<>();

		for (int i = 0; i < lines.size();) {
			QuizDaten question = parseQuestion(lines, i);
			if (question != null) {
				questions.add(question);
				i += calculateNextQuestionOffset();
			} else {
				i++; // Überspringe fehlerhafte Frage
			}
		}

		return questions;
	}

	/**
	 * Parsed eine einzelne Frage, beginnend beim gegebenen Zeilenindex.
	 *
	 * @param lines Die zu parsenden Zeilen
	 * @param startIndex Der Startindex der Frage
	 * @return Das geparste QuizDaten-Objekt oder null, wenn das Parsen fehlschlägt
	 */
	private static QuizDaten parseQuestion(List<String> lines, int startIndex) {
		if (startIndex >= lines.size()) {
			return null;
		}

		String questionText = lines.get(startIndex).trim();
		if (questionText.isEmpty()) {
			return null;
		}

		String[] answers = parseAnswers(lines, startIndex + 1);
		if (answers == null) {
			return null;
		}

		boolean[] correctAnswers = parseCorrectAnswers(lines, startIndex + 1 + EXPECTED_ANSWERS);
		if (correctAnswers == null) {
			return null;
		}

		return new QuizDaten(questionText, answers, correctAnswers, DEFAULT_TIME_SECONDS);
	}

	/**
	 * Parsed die vier Antwortmöglichkeiten aus den Zeilen.
	 *
	 * @param lines Die zu parsenden Zeilen
	 * @param startIndex Der Startindex der Antworten
	 * @return Array der Antworten oder null, wenn das Parsen fehlschlägt
	 */
	private static String[] parseAnswers(List<String> lines, int startIndex) {
		String[] answers = new String[EXPECTED_ANSWERS];

		for (int i = 0; i < EXPECTED_ANSWERS; i++) {
			int lineIndex = startIndex + i;
			if (lineIndex >= lines.size()) {
				return null; // Nicht genug Zeilen für Antworten
			}

			String line = lines.get(lineIndex);
			if (line.length() <= ANSWER_PREFIX.length()) {
				return null; // Ungültiges Antwortformat
			}

			answers[i] = line.substring(ANSWER_PREFIX.length()).trim();
		}

		return answers;
	}

	/**
	 * Parsed, welche Antworten richtig sind, aus der Lösungszeile.
	 *
	 * @param lines Die zu parsenden Zeilen
	 * @param solutionLineIndex Der Index der Lösungszeile
	 * @return Array von booleschen Werten, die angeben, welche Antworten richtig sind, oder null, wenn das Parsen fehlschlägt
	 */
	private static boolean[] parseCorrectAnswers(List<String> lines, int solutionLineIndex) {
		if (solutionLineIndex >= lines.size()) {
			return null;
		}

		String solutionLine = lines.get(solutionLineIndex);
		if (!solutionLine.startsWith(CORRECT_ANSWER_PREFIX)) {
			return null;
		}

		boolean[] correctAnswers = new boolean[EXPECTED_ANSWERS];
		String answerLetters = solutionLine.substring(CORRECT_ANSWER_PREFIX.length()).trim();

		for (String letter : answerLetters.split("\\s+")) {
			if (!letter.isEmpty()) {
				char answerChar = letter.charAt(0);
				if (isValidAnswerLetter(answerChar)) {
					correctAnswers[answerChar - 'A'] = true;
				}
			}
		}

		return correctAnswers;
	}

	/**
	 * Überprüft, ob das gegebene Zeichen ein gültiger Antwortbuchstabe (A-D) ist.
	 *
	 * @param letter Der zu überprüfende Buchstabe
	 * @return true, wenn der Buchstabe gültig ist, sonst false
	 */
	private static boolean isValidAnswerLetter(char letter) {
		return letter >= 'A' && letter <= 'D';
	}

	/**
	 * Berechnet den Offset zur nächsten Frage im Dateiformat.
	 *
	 * @return Der Offset zur nächsten Frage
	 */
	private static int calculateNextQuestionOffset() {
		return 1 + EXPECTED_ANSWERS + 1 + 1; // Frage + 4 Antworten + Lösung + Leerzeile
	}
}
