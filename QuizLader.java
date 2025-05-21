import java.io.*;
import java.util.*;

public class QuizLader {

	public static List<QuizDaten> ladeAlleFragen(String dateiname) {
		List<QuizDaten> fragenListe = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(dateiname))) {
			String frage;

			while ((frage = br.readLine()) != null) {
				String[] antworten = new String[4];
				for (int i = 0; i < 4; i++) {
					String zeile = br.readLine();
					if (zeile != null && zeile.length() > 3) {
						antworten[i] = zeile.substring(3);
					}
				}

				boolean[] loesungen = new boolean[4];
				String loesungsZeile = br.readLine(); // z.B. "Richtige Antworten: A C"

				if (loesungsZeile != null && loesungsZeile.startsWith("Richtige Antworten:")) {
					String[] teile = loesungsZeile.substring(20).trim().split("\\s+");
					for (String teil : teile) {
						char buchstabe = teil.charAt(0);
						if (buchstabe >= 'A' && buchstabe <= 'D') {
							loesungen[buchstabe - 'A'] = true;
						}
					}
				}

				//TODO add a time option in seconds in the Quiz constructor
				int time = 20;

				br.readLine(); // Leere Zeile zwischen Fragen überspringen
				fragenListe.add(new QuizDaten(frage, antworten, loesungen, time));

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fragenListe;
	}
}
