import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasse für einen Countdown-Timer.
 * Diese Klasse erstellt ein Fenster mit einem Countdown-Timer, der jede Sekunde aktualisiert wird.
 */
public class CountdownTimer extends JFrame {

    /** Verbleibende Zeit in Sekunden */
    private int timeRemaining;

    /** Timer zur Aktualisierung des Countdowns jede Sekunde */
    private Timer timer;

    /**
     * Konstruktor für den Countdown-Timer.
     *
     * @param startTimeInSeconds Die Startzeit des Countdowns in Sekunden
     */
    public CountdownTimer(int startTimeInSeconds) {
        JLabel timerLabel; // Label zur Anzeige des Countdowns

        // Initialisiert die verbleibende Zeit
        timeRemaining = startTimeInSeconds;

        // Erstellt ein Label zur Anzeige des Countdowns
        timerLabel = new JLabel(String.valueOf(timeRemaining), SwingConstants.CENTER);
        timerLabel.setFont(new Font("Serif", Font.BOLD, 15));
        add(timerLabel, BorderLayout.CENTER);

        // Erstellt einen Timer, der jede Sekunde aktualisiert wird
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeRemaining > 0) {
                    timeRemaining--;
                    timerLabel.setText(String.valueOf(timeRemaining));
                } else {
                    // Countdown beendet
                    timerLabel.setText("Time's up!");
                    timer.stop();
                }
            }
        });

        // Startet den Timer
        timer.start();
    }

    /**
     * Hauptmethode zum Starten des Countdown-Timers.
     * Diese Methode ist auskommentiert, da sie nur für Testzwecke verwendet wird.
     *
     * @param args Kommandozeilenargumente
     */
    /*
    public static void main(String[] args) {
        // Setzt den Countdown auf 10 Sekunden (dieser Wert kann geändert werden)
        int startTime = 10;

        // Erstellt und zeigt das Fenster an
        SwingUtilities.invokeLater(() -> {
            CountdownTimer frame = new CountdownTimer(startTime);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(200, 100);
            frame.setVisible(true);
        });
    }
    */
}
