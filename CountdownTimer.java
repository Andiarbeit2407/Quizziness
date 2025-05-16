import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

    public class CountdownTimer extends JFrame {

        private int timeRemaining;  // Time remaining in seconds
        private Timer timer;        // Timer to update the countdown every second

        public CountdownTimer(int startTimeInSeconds) {
            JLabel timerLabel;  // Label to display the countdown
            // Initialize the time remaining
            timeRemaining = startTimeInSeconds;

            // Create a label to display the countdown
            timerLabel = new JLabel(String.valueOf(timeRemaining), SwingConstants.CENTER);
            timerLabel.setFont(new Font("Serif", Font.BOLD, 15));
            add(timerLabel, BorderLayout.CENTER);

            // Create a timer that updates every second
            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (timeRemaining > 0) {
                        timeRemaining--;
                        timerLabel.setText(String.valueOf(timeRemaining));
                    } else {
                        // Countdown finished
                        timerLabel.setText("Time's up!");
                        timer.stop();
                    }
                }
            });

            // Start the timer
            timer.start();
        }
/*
        public static void main(String[] args) {
            // Set the countdown to 10 seconds (you can change this value)
            int startTime = 10;

            // Create and show the frame
            SwingUtilities.invokeLater(() -> {
                CountdownTimer frame = new CountdownTimer(startTime);
                frame.setVisible(true);
            });
        }


 */
    }