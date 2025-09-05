package GUI;

import Project.AHP;
import Project.Authentication;
import Project.DatabaseConnection;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;



public class CompanyListingPage extends JFrame {
    private DatabaseConnection dbConnection;
    Authentication Auth = new Authentication();

    private String name;
    JLabel LocationLabel, StartDateLabel, EndDateLabel, StipendLabel, PositionNameLabel;
    JTextField LocationField, StipendField, PositionNameField;
    JButton HomeFrameButton, SubmitFrameButton;
    JTextField MessageField;
    JPanel inputPanel = new JPanel();

    JDatePicker startDatePicker, endDatePicker ;
    public CompanyListingPage(String username) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setTitle("Company Listing Page");  // Set a title for the frame

        // Create and configure layout manager (e.g., BorderLayout)
        setLayout(new BorderLayout());

        // Create a panel to hold the input components
        inputPanel.setLayout(new GridLayout(6, 2, 10, 10)); // 6 rows, 2 columns, with gaps

        // Add labels and input components to the input panel
        LocationLabel = new JLabel("Location:");
        LocationField = new JTextField();
        StartDateLabel = new JLabel("Start Date:");
        EndDateLabel = new JLabel("End Date:");
        StipendLabel = new JLabel("Stipend (Amount only):");
        StipendField = new JTextField();
        PositionNameLabel = new JLabel("Position Name:");
        PositionNameField = new JTextField();
        MessageField = new JTextField();


        // Create date pickers for start and end dates
        UtilDateModel startDateModel = new UtilDateModel();
        UtilDateModel endDateModel = new UtilDateModel();
        startDatePicker = new JDatePicker(startDateModel, "yyyy-MM-dd");
        endDatePicker = new JDatePicker(endDateModel, "yyyy-MM-dd");



        // Add input components to the input panel with specified bounds
        inputPanel.add(LocationLabel);
        inputPanel.add(LocationField);
        inputPanel.add(StartDateLabel);
        inputPanel.add(startDatePicker); // Use the start date picker here
        inputPanel.add(EndDateLabel);
        inputPanel.add(endDatePicker); // Use the end date picker here
        inputPanel.add(StipendLabel);
        inputPanel.add(StipendField);
        inputPanel.add(PositionNameLabel);
        inputPanel.add(PositionNameField);
        inputPanel.add(MessageField);


        // Create buttons and add action listeners
        HomeFrameButton = new JButton("Home");
        HomeFrameButton.addActionListener(e ->
        {
            dispose();
            CompanyHomePage page = new CompanyHomePage(name);
        });

        SubmitFrameButton = new JButton("Submit");
        SubmitFrameButton.addActionListener(e -> {
            insertData(username);
            dispose();
            InternPreferenceTable ipt = new InternPreferenceTable(Position);
        });

        // Add input panel to the center of the frame, Home button to the top-left corner, and Submit button to the bottom
        add(inputPanel, BorderLayout.CENTER);
        add(HomeFrameButton, BorderLayout.NORTH);
        add(SubmitFrameButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    String location, startdate, endDate, Position;
    int Stipend; // Corrected variable name to Stipend

    private void insertData(String username) {
        location = LocationField.getText();
        Position = PositionNameField.getText();


        if (location.isEmpty() || Position.isEmpty() ) {
            MessageField.setText("All fields must be filled.");
        } else {
            try {
                // Retrieve the start date from the JDatePicker and convert to java.sql.Date
                java.util.Date utilStartDate = (java.util.Date) startDatePicker.getModel().getValue();
                Date startDate = new Date(utilStartDate.getTime());

                // Retrieve the end date from the JDatePicker and convert to java.sql.Date
                java.util.Date utilEndDate = (java.util.Date) endDatePicker.getModel().getValue();
                Date endDate = new Date(utilEndDate.getTime());

                if(!checkDate(startDate,endDate)){
                    MessageField.setText(" End Date be a after Start Date.");
                    return;
                };
                addDataToDatabase(username, location, Stipend, Position, startDate,endDate );
                try {
                    Stipend = Integer.parseInt(StipendField.getText());
                    System.out.println(Stipend);
                    if (Stipend <= 0) {
                        MessageField.setText("Stipend amount can't be zero."); }
                } catch (NumberFormatException e) {
                    MessageField.setText("Stipend must be a valid integer.");
                    return; // Exit the method if the Stipend is not a valid integer
                }
            }catch (Exception e){
                MessageField.setText("Date fields has ERROR, FIll properly.");
            }
        }
    }

    private boolean checkDate(Date startDate, Date endDate) {
        return endDate.compareTo(startDate) > 0;
    }


    private void addDataToDatabase(String username,String location, int Stipend, String Position, Date startdate, Date endDate) {
        openDatabaseConnection();

        try {
            String insertQuery = "INSERT INTO Listing (location, Stipend, Position, startdate, endDate, username ) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(insertQuery);
            System.out.println(Stipend);
            preparedStatement.setString(1, location);
               preparedStatement.setInt(2, Stipend);
            preparedStatement.setString(3, Position);
              preparedStatement.setDate(4, startdate);
              preparedStatement.setDate(5, endDate);
            preparedStatement.setString(6, username);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                MessageField.setText("Listing Done !!");
                System.out.println(true);
            } else {
                MessageField.setText("Error: Listing Failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            MessageField.setText("Error: Listing Failed.");
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
   /* public static void main(String[] args) {
        new CompanyListingPage("sujal");
    }*/
}