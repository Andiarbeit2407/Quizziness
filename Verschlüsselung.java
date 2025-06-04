/**
 * Diese Klasse bietet Methoden zur Verschlüsselung und Entschlüsselung von Texten
 * unter Verwendung der Caesar-Verschlüsselungstechnik.
 */
public class Verschlüsselung {

	/** Der Standardverschiebungsschlüssel für die Caesar-Verschlüsselung. */
	private static final int key = 33;

	/**
	 * Verschlüsselt einen gegebenen Text mit der Caesar-Verschlüsselung.
	 * Jedes Zeichen im Text wird um eine bestimmte Anzahl von Stellen im ASCII-Zeichensatz verschoben.
	 *
	 * @param text Der zu verschlüsselnde Text.
	 * @param key Der Schlüssel, um den die Zeichen verschoben werden sollen.
	 * @return Der verschlüsselte Text.
	 */
	public static String caesarEncrypt(String text, int key) {
		StringBuilder encrypted = new StringBuilder();

		// Iteration über jedes Zeichen im Eingabetext
		for (char c : text.toCharArray()) {
			// Überprüfung, ob das Zeichen im Bereich der druckbaren ASCII-Zeichen liegt
			if (c >= 33 && c <= 126) {
				// Verschiebung des Zeichens um 'key' Stellen
				char shifted = (char) ((c - 33 + key) % 94 + 33);
				encrypted.append(shifted);
			} else {
				// Zeichen außerhalb des Bereichs werden unverändert hinzugefügt
				encrypted.append(c);
			}
		}
		return encrypted.toString();
	}

	/**
	 * Entschlüsselt einen mit der Caesar-Verschlüsselung verschlüsselten Text.
	 * Jedes Zeichen im Text wird um eine bestimmte Anzahl von Stellen im ASCII-Zeichensatz zurückverschoben.
	 *
	 * @param encrypted Der zu entschlüsselnde Text.
	 * @param key Der Schlüssel, um den die Zeichen zurückverschoben werden sollen.
	 * @return Der entschlüsselte Text.
	 */
	public static String caesarDecrypt(String encrypted, int key) {
		StringBuilder decrypted = new StringBuilder();

		// Iteration über jedes Zeichen im verschlüsselten Text
		for (char c : encrypted.toCharArray()) {
			if (c >= 33 && c <= 126) {
				// Rückverschiebung des Zeichens um 'key' Stellen
				char shifted = (char) ((c - 33 - key + 94) % 94 + 33);
				decrypted.append(shifted);
			} else {
				// Zeichen außerhalb des Bereichs werden unverändert hinzugefügt
				decrypted.append(c);
			}
		}
		return decrypted.toString();
	}
}
