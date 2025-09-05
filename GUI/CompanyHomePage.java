package GUI;

import javax.swing.*;
import java.awt.*;

public class CompanyHomePage extends JFrame {
    public CompanyHomePage(String username) {
        // The name of the home page
        setTitle("Intern Home Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);

        // TO redirect to listing page
        JButton ListingPage = new JButton("Listing Page");
        ListingPage.addActionListener(e -> {
            dispose();
            CompanyListingPage page = new CompanyListingPage(username);
        });

        // Create a button panel for other buttons
        JPanel buttonPanel = new JPanel(new GridLayout(6, 4, 10, 10));

        // The String array for the developer of the coding
        String[] developers = {
                "Developer Android (Java/Kotlin)", "Developer C", "Developer C++", "Developer Rust", "Developer Go",
                "Developer iOS (Swift/Objective-C)", "Developer Java (Non-Android)", "Developer JavaScript", "Developer TypeScript", "Developer Julia",
                "Developer PHP", "Developer Python", "Developer Ruby", "Developer SQL (MySQL, PostgreSQL, etc.)", "Developer Haskell", "Developer OCaml"
        };

        // Adding the developer buttons using an enhanced for loop
        for (String developer : developers) {
            JButton button = new JButton(developer);
            // Button's action listener to lead to the language page
            button.addActionListener(e -> {
                // Create an instance of DeveloperPage and display it
                DeveloperPage languagePage = new DeveloperPage(developer); // Send the language to LanguagePage constructor
            });
            // Add the button to the panel
            buttonPanel.add(button);
        }

        // Set the layout of the frame to BorderLayout
        setLayout(new BorderLayout());

        // Add the "Listing Page" button to the NORTH region
        add(ListingPage, BorderLayout.NORTH);

        // Add the button panel to the CENTER region
        add(buttonPanel, BorderLayout.CENTER);

        // Set the frame location relative to none
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        CompanyHomePage page = new CompanyHomePage("Gautam");
    }
}