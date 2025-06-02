// Klasse zur Speicherung einer Quizfrage und ihrer Antwortmöglichkeiten
public class QuizDaten {
	public String frage;
	public String[] antworten; // Länge: 4
	public boolean[] loesung;  // Länge: 4
	public int time;

	public QuizDaten(String frage, String[] antworten, boolean[] loesung, int time) {
		this.frage = frage;
		this.antworten = antworten;
		this.loesung = loesung;
		this.time = time;
	}

	public String serialisieren() {
		StringBuilder sb = new StringBuilder();
		sb.append(frage.replace(";", ",")).append(";"); // Semikolon-Schutz
		for (String antwort : antworten) {
			sb.append(antwort.replace(";", ",")).append(";");
		}
		for (boolean korrekt : loesung) {
			sb.append(korrekt).append(";");
		}
		sb.append(time);
		return sb.toString();
	}

	// Optional: zum Einlesen wieder
	public static QuizDaten parse(String zeile) {
		String[] teile = zeile.split(";");
		if (teile.length < 10) return null;
		String frage = teile[0];
		String[] antworten = new String[4];
		boolean[] loesung = new boolean[4];

		System.arraycopy(teile, 1, antworten, 0, 4);
		for (int i = 0; i < 4; i++) {
			loesung[i] = Boolean.parseBoolean(teile[5 + i]);
		}
		int time = Integer.parseInt(teile[9]);

		return new QuizDaten(frage, antworten, loesung, time);
	}
}
