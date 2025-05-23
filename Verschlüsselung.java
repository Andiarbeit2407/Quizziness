public class Verschlüsselung {

	private static final int key = 33;
	// Caesar-Verschlüsselung: Verschiebt jedes Zeichen im Text um 'key' Stellen in der ASCII-Tabelle
	public static String caesarEncrypt(String text, int key) {
		StringBuilder encrypted = new StringBuilder();
		for (char c : text.toCharArray()) {
			// Nur Zeichen im ASCII-Bereich 33–126 (druckbare Zeichen)
			if (c >= 33 && c <= 126) {
				char shifted = (char) ((c - 33 + key) % 94 + 33);
				encrypted.append(shifted);
			} else {
				// Unveränderte Zeichen z.B. Steuerzeichen oder Umlaute)
				encrypted.append(c);
			}
		}
		return encrypted.toString();
	}

	// Caesar-Entschlüsselung: Verschiebt jedes Zeichen umgekehrt zurück
	public static String caesarDecrypt(String encrypted, int key) {
		StringBuilder decrypted = new StringBuilder();
		for (char c : encrypted.toCharArray()) {
			if (c >= 33 && c <= 126) {
				char shifted = (char) ((c - 33 - key + 94) % 94 + 33);
				decrypted.append(shifted);
			} else {
				decrypted.append(c);
			}
		}
		return decrypted.toString();
	}
}


