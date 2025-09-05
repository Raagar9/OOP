/**/
package GUI;

import Project.Authentication;
import Project.DatabaseConnection;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CompanyRegisterPage extends JFrame {
    private DatabaseConnection dbConnection;
    Authentication Auth = new Authentication();
    JLabel usernameLabel,contactLabel,passwordLabel,confirmPasswordLabel,emailLabel,CompanyNameLabel,AddressLabel; // Label Declaration

    JTextField usernameField,contactField,emailField, CompanyNameField, AddressField, messageField; // info field
    JPasswordField passwordField,confirmPasswordField;  // Register field
    JButton nextFrameButton; // register button
    private String phoneNumber, name, email, password, companyName, address, confirmpassowrd;

    public CompanyRegisterPage() {
        setSize(1000, 1000);
        setLayout(null);

        // Labels
        usernameLabel = new JLabel("Username:");
        contactLabel = new JLabel("Contact:");
        passwordLabel = new JLabel("Password:");
        confirmPasswordLabel = new JLabel("Confirm Password:");
        emailLabel = new JLabel("Email:");
        CompanyNameLabel = new JLabel("Company Name");
        AddressLabel = new JLabel("Address:");

        // Text Fields
        usernameField = new JTextField();
        contactField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        emailField = new JTextField();
        messageField = new JTextField();
        AddressField = new JTextField();
        messageField = new JTextField();
        CompanyNameField = new JTextField();

        // Button
        nextFrameButton = new JButton("Next");

        // Set positions using setBounds
        usernameLabel.setBounds(100, 50, 100, 30);
        contactLabel.setBounds(100, 100, 100, 30);
        passwordLabel.setBounds(100, 150, 100, 30);
        confirmPasswordLabel.setBounds(100, 200, 150, 30);
        emailLabel.setBounds(100, 250, 100, 30);
        CompanyNameLabel.setBounds(100, 300, 300, 30);
        AddressLabel.setBounds(100, 350, 100, 30);

        // set the bounds
        usernameField.setBounds(220, 50, 200, 30);
        contactField.setBounds(220, 100, 200, 30);
        passwordField.setBounds(220, 150, 200, 30);
        confirmPasswordField.setBounds(220, 200, 200, 30);
        emailField.setBounds(220, 250, 200, 30);
        CompanyNameField.setBounds(220, 300, 200, 30);
        AddressField.setBounds(220, 350, 200, 30);
        messageField.setBounds(220,500,200,30);

        // set the bounds, for the next
        nextFrameButton.setBounds(100, 400, 100, 30);

        // Add all the labels
        add(usernameLabel);
        add(contactLabel);
        add(passwordLabel);
        add(confirmPasswordLabel);
        add(emailLabel);
        add(CompanyNameLabel);
        add(AddressLabel);

        // add all the fields
        add(usernameField);
        add(contactField);
        add(passwordField);
        add(confirmPasswordField);
        add(emailField);
        add(CompanyNameField);
        add(AddressField);
        add(nextFrameButton);
        add(messageField);

        // Define all the button
        nextFrameButton.addActionListener(e -> {
            insertData();
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void insertData() {
        // Retrieve the data
        name = usernameField.getText();
        email = emailField.getText();
        phoneNumber = contactField.getText();
        companyName = CompanyNameField.getText();
        address = AddressField.getText();
        // Convert the password field data into char array then to password
        char[] passwordArray = passwordField.getPassword();
        password = new String(passwordArray);

        char[] confirmPasswordArray = confirmPasswordField.getPassword();
        confirmpassowrd = new String(confirmPasswordArray);

        // Check if any of the fields are empty or null
        if (name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || confirmpassowrd.isEmpty() || companyName.isEmpty() || address.isEmpty() ) {
            messageField.setText("Please fill in all the fields.");
        } else {
            // Check if the password and confirm password match
            if (password.equals(confirmpassowrd)) {
                // Set up progress bar then send the message

                if(Auth.checkUserNameExist(name)){
                    System.out.println("System authetication going on !!");
                    messageField.setText("Welcome !!!");
                    ProgressBarSetUp(name, phoneNumber, email, password, companyName, address);

                    // add the Company listing page here
                }else{
                    messageField.setText("User Name Exists in the database ");

                }
            } else {
                // Passwords don't match, show an error message
                // Add a progress bar here if needed
                messageField.setText("Password & Confirm Password Don't match");
                // Reload the registration page
            }
        }
    }

    int progress  = 0;

    private void ProgressBarSetUp(String name, String phoneNumber, String email, String password, String companyName, String address) {
        JProgressBar progressBar = new JProgressBar(0, 20);
        progressBar.setBounds(100, 450, 300, 30);  // Adjust the bounds as needed
        progressBar.setStringPainted(true); // Show the progress value
        progressBar.setValue(0);
        add(progressBar); // Add this to the frame
        Timer timer = new Timer(100, e -> {
            if (progress == 10) {
                // The code to add the data to the database
                addDataToDatabase(name, phoneNumber, email, password, companyName, address);
                dispose();
                CompanyListingPage page = new CompanyListingPage(name);
            }
            progress++;
            progressBar.setValue(progress);
        });
        timer.start();
    }


    private void addDataToDatabase(String name, String phoneNumber, String email, String password, String companyName, String address) {
        openDatabaseConnection();

        try {
            String insertQuery = "INSERT INTO users (username, contact_no, email, Passcode,Type, InstitueName, Address) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(insertQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phoneNumber);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, "Company");
            preparedStatement.setString(6, companyName);
            preparedStatement.setString(7, address);


            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                messageField.setText("Congrats!! on Registration");
                System.out.print(true);
            } else {
                messageField.setText("Error: Registration Failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageField.setText("Error: Registration Failed.");
        } finally {
            closeDatabaseConnection();
        }
    }


    private void openDatabaseConnection(){
        dbConnection = new DatabaseConnection();
    }
    private void closeDatabaseConnection() {
        dbConnection.closeConnection();
    }
    private void ReloadPage() {
        CompanyRegisterPage reload = new CompanyRegisterPage();
        dispose();
    }

    /*public static void main(String[] args){
        ComapnyRegisterPage page = new ComapnyRegisterPage();
    }*/
}

