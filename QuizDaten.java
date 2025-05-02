// Klasse zur Speicherung einer Quizfrage und ihrer Antwortmöglichkeiten
public class QuizDaten {

	// Text der Frage
	public String frage;

	// Array mit den Antwortmöglichkeiten
	public String[] antworten;

	public boolean[] loesung;

	// Konstruktor zum Erstellen eines QuizDaten-Objekts mit Frage und Antworten
	public QuizDaten(String frage, String[] antworten, boolean[] loesung) {
		this.frage = frage;
		this.antworten = antworten;
		this.loesung = loesung;
	}
}