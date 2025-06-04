/**
 * Die Klasse QuizDaten speichert eine Quizfrage zusammen mit ihren Antwortmöglichkeiten und Lösungen.
 * Sie bietet Methoden zur Serialisierung und Deserialisierung der Quizdaten.
 */
public class QuizDaten {
	public String frage; // Die Quizfrage
	public String[] antworten; // Die Antwortmöglichkeiten (Länge: 4)
	public boolean[] loesung; // Die Lösungen, die angeben, welche Antworten korrekt sind (Länge: 4)
	public int time; // Die Zeit, die für die Beantwortung der Frage zur Verfügung steht

	/**
	 * Konstruktor für die QuizDaten-Klasse.
	 *
	 * @param frage Die Quizfrage.
	 * @param antworten Die Antwortmöglichkeiten.
	 * @param loesung Die Lösungen, die angeben, welche Antworten korrekt sind.
	 * @param time Die Zeit, die für die Beantwortung der Frage zur Verfügung steht.
	 */
	public QuizDaten(String frage, String[] antworten, boolean[] loesung, int time) {
		this.frage = frage;
		this.antworten = antworten;
		this.loesung = loesung;
		this.time = time;
	}

	/**
	 * Serialisiert die Quizfrage und ihre Antwortmöglichkeiten in einen String.
	 * Semikolons in der Frage oder den Antworten werden durch Kommas ersetzt, um Konflikte zu vermeiden.
	 *
	 * @return Der serialisierte String, der die Quizfrage und ihre Antwortmöglichkeiten enthält.
	 */
	public String serialisieren() {
		StringBuilder sb = new StringBuilder();
		sb.append(frage.replace(";", ",")).append(";"); // Ersetzt Semikolons in der Frage durch Kommas
		for (String antwort : antworten) {
			sb.append(antwort.replace(";", ",")).append(";"); // Ersetzt Semikolons in den Antworten durch Kommas
		}
		for (boolean korrekt : loesung) {
			sb.append(korrekt).append(";");
		}
		sb.append(time);
		return sb.toString();
	}

	/**
	 * Deserialisiert einen String und erstellt ein QuizDaten-Objekt.
	 * Der String sollte zuvor mit der serialisieren-Methode erstellt worden sein.
	 *
	 * @param zeile Der zu deserialisierende String.
	 * @return Das deserialisierte QuizDaten-Objekt oder null, falls der String ungültig ist.
	 */
	public static QuizDaten parse(String zeile) {
		String[] teile = zeile.split(";");
		if (teile.length < 10) return null; // Überprüft, ob der String genug Teile enthält

		String frage = teile[0];
		String[] antworten = new String[4];
		boolean[] loesung = new boolean[4];

		System.arraycopy(teile, 1, antworten, 0, 4); // Kopiert die Antwortmöglichkeiten aus dem Array
		for (int i = 0; i < 4; i++) {
			loesung[i] = Boolean.parseBoolean(teile[5 + i]); // Konvertiert die Lösungen in boolesche Werte
		}
		int time = Integer.parseInt(teile[9]); // Konvertiert die Zeit in einen Integer

		return new QuizDaten(frage, antworten, loesung, time); // Erstellt und gibt ein neues QuizDaten-Objekt zurück
	}
}
