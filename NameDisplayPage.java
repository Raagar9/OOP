package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class NameDisplayPage extends JFrame {
    private String[] names;

    public NameDisplayPage(String[] names) {
        this.names = names;

        setTitle("Name Display Page");
        setSize(400, 300); // Increase the frame size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(names.length, 1, 10, 10)); // Rows, Columns, Horizontal Gap, Vertical Gap
        getContentPane().add(panel, BorderLayout.CENTER);

        // Create a font with a larger size
        Font font = new Font("Arial", Font.PLAIN, 16); // Adjust the font size as needed

        for (int i = 0; i < names.length; i++) {
            String labelText = "Preference " + (i + 1) + ": " + names[i];
            JLabel nameLabel = new JLabel(labelText);
            nameLabel.setFont(font);
            panel.add(nameLabel);
        }

        // Center the JFrame on the screen
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        String[] names = {"John", "Alice", "Bob"};
        NameDisplayPage frame = new NameDisplayPage(names);
    }
}
