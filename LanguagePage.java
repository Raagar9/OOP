/*
* make the pages for each langauge
* Add the recommedation ka fucking good the last thing to do
*
* */
package GUI;

import javax.swing.*;
import Project.DatabaseConnection;
import java.lang.reflect.InvocationTargetException;

public class LanguagePage extends JFrame {
    public LanguagePage(String language) {
        switch (language) {
            case "Android (Java/Kotlin)":
                break;
            case "C":
                break;
            case "C++":
                break;
            case "Rust":
                break;
            case "Go":
                break;
            case "iOS (Swift/Objective-C)":
                break;
            case "Java (Non-Android)":
                break;
            case "JavaScript":
                break;
            case "TypeScript":
                break;
            case "Julia":
                break;
            case "PHP":
                break;
            case "Python":
                break;
            case "Ruby":
                break;
            case "SQL (MySQL, PostgreSQL, etc.)":
                break;
            case "Haskell":
                break;
            case "OCaml":
                break;
            default:
                System.out.println("There is an error");
                LoginInPage loginPage = new LoginInPage();
        }
    }

}