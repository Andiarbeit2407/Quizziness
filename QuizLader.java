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
				br.readLine();
				fragenListe.add(new QuizDaten(frage, antworten));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fragenListe;
	}
}
