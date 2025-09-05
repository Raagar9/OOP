
package GUI;

import javax.swing.*;
import Project.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class LoginInPage extends JFrame {
    private DatabaseConnection dbConnection;
    JLabel UsernameLabel, PasswordLabel, MessageLabel;
    JTextField UsernameField;
    JPasswordField PasswordField;
    JButton SubmitButton, RegisterButton, CompanyRegisterButton;
    String username, password;

    public LoginInPage(){
        setSize(1000, 1000);
        setLayout(null);

        UsernameLabel = new JLabel("Email id : ");
        PasswordLabel = new JLabel("Password");
        UsernameField = new JTextField();
        PasswordField = new JPasswordField();
        MessageLabel = new JLabel();  // To sent Message to user

        SubmitButton = new JButton("Login");
        RegisterButton = new JButton("Register");
        CompanyRegisterButton = new JButton("Company Registration");

        UsernameLabel.setBounds(100, 100, 100, 30);  // (x, y, width, height)
        PasswordLabel.setBounds(100, 150, 100, 30);  // (x, y, width, height)
        MessageLabel.setBounds(100, 250, 300, 30);  // Set bounds for the message label

        UsernameField.setBounds(220, 100, 200, 30);  // (x, y, width, height) Set bounds for UsernameField
        PasswordField.setBounds(220, 150, 200, 30);  // (x, y, width, height)
        SubmitButton.setBounds(100, 200, 100, 30);  // (x, y, width, height)
        RegisterButton.setBounds(220, 200, 100, 30);  // (x, y, width, height)
        CompanyRegisterButton.setBounds(330, 200, 180, 30);  // Adjust the position and size as needed


        add(UsernameLabel);
        add(PasswordLabel);
        add(UsernameField);
        add(PasswordField);
        add(SubmitButton);
        add(RegisterButton);
        add(CompanyRegisterButton);
        add(MessageLabel);

        //Submit Button Check
        SubmitButton.addActionListener(e -> {

            username = UsernameField.getText();
            char[] passwordArray = PasswordField.getPassword();
            password = new String(passwordArray);
            if(username.isEmpty()){
                MessageLabel.setText("Please enter the UserName");
            }
            if(password.isEmpty()){
                MessageLabel.setText("Please enter the Password");
            }
            if(checkUserNameExist(username)){
                if(checkUserNamePassword(username, password)) {
                    // sent message Login Successfully  and led to the next page
                    if(UserType(username).equals("Company")){  // check what kind of user has come
                        // the company home page
                        dispose();
                        CompanyHomePage page = new CompanyHomePage(username);
                        System.out.println("Company");

                    }else{
                        // the interhome page
                        dispose();
                        InternHomePage page = new InternHomePage(username);
                        System.out.println("Intern");
                    }
                    System.out.println("YEss Broo");
                    MessageLabel.setText("Login Successful.");
            }else {
                // Incorrect password

                MessageLabel.setText("Incorrect Password.");
            }
        } else {
            // User does not exist

                MessageLabel.setText("User does not exist.");
        }});

        // Register Button action, in case the user is not register
        RegisterButton.addActionListener( Al -> {
            UserRegisterPage Page = new UserRegisterPage();
            dispose();
        });

        CompanyRegisterButton.addActionListener(e -> {
            CompanyRegisterPage Page = new CompanyRegisterPage();
            dispose();
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    private String UserType(String username) {
        openDatabaseConnection();
        String type = "";

        try {
            // The query
            String query = "SELECT Type FROM users WHERE username = ?";
            //
            PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(query);
            /* dbConnection.getConnection() -- to get the database connection
            * prepare SQL statement with query as argument
            * */
            preparedStatement.setString(1, username); // set the parameters for the preparedStatement

            ResultSet resultSet = preparedStatement.executeQuery(); // to recieve the statement

            if (resultSet.next()) {  // to check if therer are multipe line
                type = resultSet.getString("Type"); // to put the type in the String
            }
        } catch (SQLException e) {
            e.printStackTrace(); // to print the excepiton or error that occur while getting the return
            // Handle the exception as needed
        } finally {
            closeDatabaseConnection();
        }

        return type;
    }

    // to check if the password exists and if then check if the password is correct
    private boolean checkUserNamePassword(String username, String password) { // Check if the password is correct
        boolean passwordCorrect = false;
        openDatabaseConnection(); // Open the connection
        try {

            Connection connection = dbConnection.getConnection();

            String query = "SELECT Passcode FROM users WHERE username = ?"; // the query
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {  // prepare the statement
                preparedStatement.setString(1, username);  // set the parameter with arguments

                // Execute the query
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Retrieve the stored password from the database
                    String storedPassword = resultSet.getString("Passcode");

                    // Compare the provided password with the stored password (case-sensitive)
                    if (storedPassword != null && storedPassword.equals(password)) {
                        // Passwords match
                        passwordCorrect = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any database errors here
        } finally {
            closeDatabaseConnection(); // Close the database connection
        }

        return passwordCorrect;
    }

    private boolean checkUserNameExist(String username) {
        boolean usernameExists = false;

        try {
            openDatabaseConnection(); // Open the connection
            Connection connection = dbConnection.getConnection();

            String query = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                // Execute the query
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    if (count == 1) {
                        // Username exists in the database
                        usernameExists = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any database errors here
        } finally {
            closeDatabaseConnection(); // Close the database connection
        }

        return usernameExists;
    }

    private void openDatabaseConnection(){

        dbConnection = new DatabaseConnection();
    }
    private void closeDatabaseConnection() {

        dbConnection.closeConnection();
    }
    private void SentMessageOnScreen(String Message){
        MessageLabel.setText(Message);
    }

    private void ReloadPage() {
        LoginInPage reload = new LoginInPage();
        dispose();
    }
}