/*
 * make the pages for each langauge
 * Add the recommedation ka fucking good the last thing to do
 * Ask who add the developer chocies page in faster way and effiecent way 
 * */

package GUI;

import javax.swing.*;
import Project.DatabaseConnection;
import java.lang.reflect.InvocationTargetException;

public class DeveloperPage extends JFrame {
    public DeveloperPage(String language) {
        switch (language) {
            case "Developer Android (Java/Kotlin)":
                break;
            case "Developer C":
                break;
            case "Developer C++":
                break;
            case "Developer Rust":
                break;
            case "Developer Go":
                break;
            case "Developer iOS (Swift/Objective-C)":
                break;
            case "Developer Java (Non-Android)":
                break;
            case "Developer JavaScript":
                break;
            case "Developer TypeScript":
                break;
            case "Developer Julia":
                break;
            case "Developer PHP":
                break;
            case "Developer Python":
                break;
            case "Developer Ruby":
                break;
            case "Developer SQL (MySQL, PostgreSQL, etc.)":
                break;
            case "Developer Haskell":
                break;
            case "Developer OCaml":
                break;
            default:
                // Handle the case where the language name doesn't match any of the cases
                System.out.println("Invalid language selection");
                break;
        }
    }

}