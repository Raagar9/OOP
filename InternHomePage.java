/**
 */

package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InternHomePage extends JFrame {
    public InternHomePage(String name) {
        // The name of the home page
        setTitle("Intern Home Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        // make a Jpanel for better organization, components better visibilty
        JPanel buttonPanel = new JPanel(new GridLayout(5 /* 5 to make the space look better*/, 4, 10, 10));

        // the String array for the languages of the coding
        String[] languages = {
                "Android (Java/Kotlin)", "C", "C++", "Rust", "Go",
                "iOS (Swift/Objective-C)", "Java (Non-Android)", "JavaScript", "TypeScript", "Julia",
                "PHP", "Python", "Ruby", "SQL (MySQL, PostgreSQL, etc.)", "Haskell", "OCaml"
        };

        // adding the Languages using enhaced for loop instead of seprate buttons for each
        for (String language : languages) {
            JButton button = new JButton(language);
            // button ka action listener and then lead to language page
            button.addActionListener(e ->  {
                    // Create an instance of LanguagePage and display it
                    LanguagePage languagePage = new LanguagePage(language); // send the language to LanguagePage constructor
                }
            );
            // add them to the frame
            buttonPanel.add(button);
        }

        // add the panel to the frame
        getContentPane().add(buttonPanel);
        // add the panel to the frame relative to none
        setLocationRelativeTo(null);
        setVisible(true);
    }

}