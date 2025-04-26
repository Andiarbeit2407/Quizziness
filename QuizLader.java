// Importieren der benötigten Klassen für Dateioperationen und Listen
import java.io.*;
import java.util.*;

// Klasse zum Laden von Quizfragen aus einer Datei
public class QuizLader {

	// Statische Methode, die alle Fragen aus einer Datei lädt und als Liste zurückgibt
	public static List<QuizDaten> ladeAlleFragen(String dateiname) {
		List<QuizDaten> fragenListe = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(dateiname))) {
			String frage;
			// Jede Frage und ihre vier Antworten werden eingelesen
			while ((frage = br.readLine()) != null) {
				String[] antworten = new String[4];
				for (int i = 0; i < 4; i++) {
					String zeile = br.readLine();
					// Entfernt die ersten drei Zeichen ("A) ", "B) ", etc.) aus der Antwortzeile
					if (zeile != null && zeile.length() > 3) {
						antworten[i] = zeile.substring(3);
					}
				}
				br.readLine(); // Leere Zeile zwischen den Fragen überspringen
				fragenListe.add(new QuizDaten(frage, antworten)); // Neue Frage hinzufügen
			}
		} catch (IOException e) {
			e.printStackTrace(); // Fehler beim Einlesen der Datei ausgeben
		}

		return fragenListe;
	}
}
