package Project;

import Project.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Authentication {
    private DatabaseConnection dbConnection;

    public boolean checkUserNameExist(String name) {
        boolean usernameExists = false;
        // Establish a database connection (you may have this in your DatabaseConnection class)
        openDatabaseConnection();
        // SQL query to check if the username exists
        // for getting the number of count of user with same id  SELECT COUNT(*) FROM users WHERE username = ?
        System.out.println(name);
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(query)) {

            preparedStatement.setString(1, name);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                System.out.println(count);
                if (count == 0) {
                    // Username exists in the database
                    usernameExists = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any database errors here
        }finally {
            closeDatabaseConnection();
        }

        return usernameExists;
    }

    private void openDatabaseConnection(){
        dbConnection = new DatabaseConnection();
    }
    private void closeDatabaseConnection() {
        dbConnection.closeConnection();
    }


}