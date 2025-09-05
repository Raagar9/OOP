/*
* Progress bar is not showing up.
* */

package GUI;


import Project.DatabaseConnection;
import Project.Authentication;
import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRegisterPage extends JFrame {
    private DatabaseConnection dbConnection;
    Authentication Auth = new Authentication();
    JLabel usernameLabel, contactLabel, passwordLabel, confirmPasswordLabel, emailLabel, instituteLabel, addressLabel, messageLabel; // Label Declaration

    JTextField usernameField, contactField, emailField, messageField, instituteField, addressField; // info field
    JPasswordField passwordField,confirmPasswordField;  // Register field
    JButton nextFrameButton; // register button
    public UserRegisterPage(){
        setSize(1000, 1000);
        setLayout(null);

        // the label for the details
         usernameLabel = new JLabel("Username:");
         contactLabel = new JLabel("Contact no:");
         passwordLabel = new JLabel("Password:");
         confirmPasswordLabel = new JLabel("Confirm Password:");
         emailLabel = new JLabel("Email:");
         messageLabel = new JLabel();
         instituteLabel = new JLabel("Institute Name:");
        addressLabel = new JLabel("Address:");

        // Field initiation
        usernameField = new JTextField();
         contactField = new JTextField();
         passwordField = new JPasswordField();
         confirmPasswordField = new JPasswordField();
         emailField = new JTextField();
         messageField = new JTextField();
        instituteField = new JTextField();
        addressField = new JTextField();


        // Seting the bounds of the label
        usernameLabel.setBounds(100, 100, 100, 30);
        contactLabel.setBounds(100, 150, 100, 30);
        passwordLabel.setBounds(100, 200, 100, 30);
        confirmPasswordLabel.setBounds(100, 250, 150, 30);
        emailLabel.setBounds(100, 300, 100, 30);
        messageLabel.setBounds(100, 400, 300, 30);
        instituteLabel.setBounds(100, 350, 100, 30);
        addressLabel.setBounds(100, 400, 100, 30);
        // Adjust the bounds as needed

        // Seting the bounds of the field
        usernameField.setBounds(250, 100, 200, 30);
        contactField.setBounds(250, 150, 200, 30);
        passwordField.setBounds(250, 200, 200, 30);
        confirmPasswordField.setBounds(250, 250, 200, 30);
        emailField.setBounds(250, 300, 200, 30);
        messageField.setBounds(250, 400, 300, 30);  // Adjust the coordinates and size as needed
        instituteField.setBounds(250, 350, 200, 30);
        addressField.setBounds(250, 400, 200, 30);
        messageField.setBounds(250, 500, 300, 30);

        // the Register button leds, to Login page again, so they can continue
        nextFrameButton = new JButton("Next");
        nextFrameButton.setBounds(250, 450, 100, 30);
        // Register Button  Action Listener
        nextFrameButton.addActionListener(e -> {
            // Handle registration logic here
            insertData();

        });


        add(usernameLabel);
        add(contactLabel);
        add(passwordLabel);
        add(confirmPasswordLabel);
        add(emailLabel);
        add(nextFrameButton);
        add(instituteLabel);
        add(addressLabel);

        add(usernameField);
        add(contactField);
        add(passwordField);
        add(confirmPasswordField);
        add(emailField);
        add(messageField);
        add(instituteField);
        add(addressField);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    String name,email,phoneNumber, password, confirmpassowrd,InstituteName,address;
    int progress;
    private void insertData() {
        // Retrieve the data
        name = usernameField.getText();
        email = emailField.getText();
        phoneNumber = contactField.getText();
        // added this, new, for the institute
        InstituteName = instituteField.getText();
        address = addressField.getText();
        // Convert the password field data into char array then to password
        char[] passwordArray = passwordField.getPassword();
        password = new String(passwordArray);

        char[] confirmPasswordArray = confirmPasswordField.getPassword();
        confirmpassowrd = new String(confirmPasswordArray);

        // Check if any of the fields are empty or null
        if (name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || confirmpassowrd.isEmpty() || InstituteName.isEmpty() || address.isEmpty()) {
            messageField.setText("Please fill in all the fields.");
        } else {
            // Check if the password and confirm password match
            if (password.equals(confirmpassowrd)) {
                // Set up progress bar then send the message

                if(Auth.checkUserNameExist(name)){
                    System.out.println("System authetication going on !!");
                    messageField.setText("Welcome !!!");
                    ProgressBarSetUp();
                    addDataToDatabase(name, phoneNumber, email, password, InstituteName ,address);

                }else{
                    messageField.setText("User Name Exists in the database ");
                    ProgressBarSetUp();

                }
            } else {
                // Passwords don't match, show an error message
                // Add a progress bar here if needed
                messageField.setText("Password & Confirm Password Don't match");
                ProgressBarSetUp();
                // Reload the registration page
            }
        }
    }

    private void ProgressBarSetUp() {
        JProgressBar progressBar = new JProgressBar(0, 10); // Changed the maximum value to 10
        progressBar.setBounds(100, 550, 300, 30);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        add(progressBar);

        Timer timer = new Timer(1000, e -> { // Changed timer duration to 1000 milliseconds (1 second)
            if (progress >= 10) {
                ((Timer) e.getSource()).stop(); // Stop the timer when progress reaches 10
                addDataToDatabase(name, phoneNumber, email, password, InstituteName, address);
                dispose();
                LanguageTable obj = new LanguageTable(name);
            } else {
                progress++;
                progressBar.setValue(progress);
            }
        });

        timer.start();
    }


    private void addDataToDatabase(String name, String phoneNumber, String email, String password , String InstituteName, String address) {
        openDatabaseConnection();

        try {
            String insertQuery = "INSERT INTO users (username, contact_no, email, Passcode, Type, InstitueName, Address) VALUES (?, ?, ?, ?, ?, ? ,?)";
            PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(insertQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phoneNumber);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, "Intern");
            preparedStatement.setString(6,InstituteName);  // add the label and field for the same and then enter the data
            preparedStatement.setString(7,address);  // add the label and field fro the same and then enter the details

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                messageField.setText("Congrats!! on Registration");
            } else {
                messageField.setText("Error: Registration Failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageField.setText("Error: Registration Failed.");
        }
        closeDatabaseConnection();
        //Close the Database
        // The rest of your code to handle UI or page redirection
    }

    private void openDatabaseConnection(){
        dbConnection = new DatabaseConnection();
    }
    private void closeDatabaseConnection() {
        dbConnection.closeConnection();
    }

    public static void main(String[] args){
        UserRegisterPage page = new UserRegisterPage();
    }

}