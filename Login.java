import javax.swing.*;
import java.awt.event.*;

public class Login extends JFrame {

    public Login() {
        setTitle("Login");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel nameLabel = new JLabel("Dein Name:");
        JTextField nameField = new JTextField(15);
        JButton loginButton = new JButton("Los geht's!");

        JPanel panel = new JPanel();
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(loginButton);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                if (!name.isEmpty()) {
                    Benutzername.username = name;
                    dispose();
                    new Hauptmenu();
                } else {
                    JOptionPane.showMessageDialog(null, "Bitte gib deinen Namen ein.");
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}
