package GUI;

import Project.AHP;
import Project.DatabaseConnection;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InternPreferenceTable extends JFrame {
    private DatabaseConnection dbConnection; // connection
    ButtonGroup[] buttonGroups; // the ButtonGroups array for a buttongroups that are going to be formed for each row
    Integer[] selectedValues; // Array to store selected values
    private JButton SubmitButton;
    public InternPreferenceTable(String pos) {
        setTitle("Language Proficiency Test");  // the Titel of the table

        // The languages String
        String[] languages = {
                "Android (Java/Kotlin)", "C", "C++", "Rust", "Go",
                "iOS (Swift/Objective-C)", "Java (Non-Android)", "JavaScript", "TypeScript", "Julia",
                "PHP", "Python", "Ruby", "SQL (MySQL, PostgreSQL, etc.)", "Haskell", "OCaml"
        };
        // the proficiency level string
        String[] Preferencelevel = {"Least Priority", "Less Priority", " Moderate Priority", "More Priority", "Most Priority"};

        // Initialize the selected values array
        selectedValues = new Integer[languages.length];

        // creating the table model ( dtm is a model of the table like how you want it )
        DefaultTableModel dtm = new DefaultTableModel();
        dtm.addColumn("Language"); // add the column language

        // Add columns for proficiency levels
        for (String language : languages) {
            dtm.addColumn(language);  // adding the langauges to the model
        }
        // the Value been passed .
        int value = 1; // Initialize the value
        for (String level : Preferencelevel) {
            // each row is an object which gets the stuff passed in it, we are creating the same for each row
            Object[] row = new Object[languages.length + 1];
            row[0] = level; // passing the level for each row

            //  System.out.println(value);
            for (int i = 1; i <= languages.length; i++) {
                JRadioButton radioButton = new JRadioButton();  // creating the radiobutton for each column
                row[i] = radioButton; // adding the radio buttons to the row
                int finalValue = value; // Store the value for this radio button
                int finalI = i;  // final index of the radiobutton
                radioButton.addItemListener(e -> {
                    int index = finalI - 1;  // the index of the buttons
                    selectedValues[index] = radioButton.isSelected() ? finalValue : null;  // setting the values of the button if they are selected if not then the value is null, but we are not passing null values
                });
            }
            value += 2; // Increment the value for the next radio button
            // add the row to the table model
            dtm.addRow(row);
        }

        // create the table
        JTable table = new JTable(dtm) {
            public void tableChanged(TableModelEvent tme) {
                super.tableChanged(tme);  // passing to the parent class the changed table format
                repaint();  // repaint the model
            }
        };

        // each column's button group
        buttonGroups = new ButtonGroup[languages.length];
        for (int i = 0; i < languages.length; i++) {
            buttonGroups[i] = new ButtonGroup();  // adding the all the columns, buttons in the buttongroups array
        }

        // Assign radio buttons to button groups
        for (int row = 0; row < dtm.getRowCount(); row++) {
            for (int col = 1; col <= languages.length; col++) {
                JRadioButton radioButton = (JRadioButton) dtm.getValueAt(row, col); // Adding the buttons to the table
                buttonGroups[col - 1].add(radioButton);  // addint them the radio button the buttongroups array -> in which buttongroup are stored
            }
        }

        // Set cell renderer and editor for proficiency level columns
        for (String language : languages) {
            table.getColumn(language).setCellRenderer(new RadioButtonRenderer()); // setting custom render for the radiobutotns for the table
            table.getColumn(language).setCellEditor(new RadioButtonEditor(new JCheckBox()));  // fpr the checkbox
        }
        // add the table to the panel to the  North side
        JScrollPane jsp = new JScrollPane(table);
        add(jsp, BorderLayout.NORTH);

        // Submit button
        SubmitButton = new JButton("Submit");
        SubmitButton.setBounds(10, 350, 100, 30); // Adjusted bounds
        SubmitButton.addActionListener(e -> {
            final int length = languages.length;
            int[] AlgoritmValueData = new int[length];
            for (int i = 0; i < buttonGroups.length; i++) {
                AlgoritmValueData[i] = selectedValues[i];
            }
            System.out.println("Button clicked");
            addDataToDatabase(pos, AlgoritmValueData);
            dispose();
            //CompanyHomePage page = new CompanyHomePage(name);  // led to the home page
            AHP a = new AHP();
        });


        // Create a JPanel to hold the table and the Submit button
        JPanel panel = new JPanel(new BorderLayout());

        // Add the JScrollPane to the panel
        panel.add(jsp, BorderLayout.CENTER);

        // Submit button
        panel.add(SubmitButton, BorderLayout.SOUTH);

        // Add the panel to the JFrame
        add(panel);

        pack(); // Pack the frame to fit its contents
        setSize(1600, 175);
        setVisible(true);;
    }

    private void addDataToDatabase(String pos, int[] algorithmValueData) {
        openDatabaseConnection();
        try {
            String insertSQL = "INSERT INTO intern_preferences (position, Android_Java_Kotlin, C, Cpp, Rust, Go, iOS_Swift_Objective_C, Java_Non_Android, JavaScript, TypeScript, Julia, PHP, Python, Ruby, codeSQL, Haskell, OCaml) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(insertSQL);

            // enter the username
            preparedStatement.setString(1, pos);

            // Set the proficiency levels for each language into the table
            for (int i = 2, proficiencyValue = 1; i <= 17; i++, proficiencyValue += 2) {
                preparedStatement.setInt(i, algorithmValueData[i - 2]);
            }

            // add the data in to the table
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully for " + pos);
            } else {
                System.out.println("Data insertion failed for " + pos);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

    public static void main(String[] args) {
    	InternPreferenceTable languageTable = new InternPreferenceTable("Gautam");
    }
   }