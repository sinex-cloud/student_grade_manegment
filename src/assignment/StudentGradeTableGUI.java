package assignment;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class StudentGradeTableGUI {
    private JFrame frame1; // Declare frame1 as an instance variable
    private DefaultTableModel tableModel; // Declare tableModel as an instance variable
    public void createAndShowGUI() {
        frame1 = new JFrame("Student");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setSize(600, 400);

        JPanel panel = new JPanel(null);

        // Create a JLabel to describe the text field and set its position
        JLabel label = new JLabel("Enter the student name:");
        label.setBounds(50, 100, 150, 20); // (x, y, width, height)

        // Create a JTextField and set its position
        JTextField textField = new JTextField(20);
        textField.setBounds(200, 100, 200, 20); // (x, y, width, height)

        JLabel label2 = new JLabel("Enter the student grade:");
        label2.setBounds(50, 200, 150, 20); // (x, y, width, height)

        // Create a JTextField and set its position
        JTextField textField1 = new JTextField(20);
        textField1.setBounds(200, 200, 200, 20);

        // Create a button to perform an action and set its position
        JButton button = new JButton("Submit");
        button.setBounds(500, 300, 80, 30); // (x, y, width, height)

        JButton nextButton = new JButton("Next");
        nextButton.setBounds(100, 300, 80, 30);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the text from the text fields
                String studentName = textField.getText();
                String studentGrade = textField1.getText();

                // Check if either student name or grade is empty
                if (studentName.isEmpty() || studentGrade.isEmpty()) {
                    // Display an error label
                    JLabel errorLabel = new JLabel("Student name or grade is empty.");
                    errorLabel.setBounds(300, 250, 250, 20); // Adjust the coordinates as needed
                    panel.add(errorLabel);
                } else {
                    // Format the student information
                    String studentInfo = studentName + "|" + studentGrade;

                    // Save the text to a text file using FileSaver
                    FileSaver.saveToFile("output3.txt", studentInfo);
                    FileSaver.saveToFile("C:\\Users\\adamb\\eclipse-workspace\\assignment\\src\\assignment\\output2.txt", studentInfo);

                    // Clear the text fields after saving
                    textField.setText("");
                    textField1.setText("");
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame1.dispose();
                showStudentTable(); // Call the method to show the student table
            }
        });

        // Add components to the panel
        panel.add(label);
        panel.add(textField);
        panel.add(button);
        panel.add(label2);
        panel.add(textField1);
        panel.add(nextButton);

        // Add the panel to the JFrame
        frame1.getContentPane().add(panel);
        frame1.setVisible(true);
    }
    public void clearTable(DefaultTableModel tableModel) {
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
    }

    public void loadDataFromTextFile(String fileName, DefaultTableModel tableModel) {
        try {
        	int      studentCount=0;
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            List<Student> studentso = new ArrayList<>(); // Store student data

            String line;
            while ((line = reader.readLine()) != null && studentCount < 30) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String studentName = parts[0];
                    try {
                        int grade = Integer.parseInt(parts[1]);

                        studentso.add(new Student(studentName, grade));
                        studentCount++;

                        bubbleSort(studentso);
                        
                        int rowCount = tableModel.getRowCount();
                        for (int i = rowCount - 1; i >= 0; i--) {
                        	System.out.println(i);
                            tableModel.removeRow(i);
                        }

                       
                        
                        for (Student student : studentso) {
                        
                            tableModel.addRow(new Object[]{student.getName(), student.getGrade()});
                        }
                    } catch (NumberFormatException e) {
                        // Handle the case where the grade is not a valid integer
                        System.err.println("Invalid grade found in the file: " + parts[1]);
                    }
                } else {
                    // Handle the case where the line does not have the expected format
                    System.err.println("Invalid line format found in the file: " + line);
                }
                
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading data from the file: " + e.getMessage());
        }
    }


    private void bubbleSort(List <Student> students) {
        int n = students.size();
        boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < n - 1; i++) {
                if (students.get(i).getGrade() > students.get(i + 1).getGrade()) {
                    // Swap students
                    Student temp = students.get(i);
                    students.set(i, students.get(i + 1));
                    students.set(i + 1, temp);
                    swapped = true;
                }
            }
        } while (swapped);
    }
	private void showStudentTable() {
    	

        JFrame frame = new JFrame("Student");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        tableModel = new DefaultTableModel(new String[]{"Student Name", "Grade"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        clearTable(tableModel);

        // Load student data from the text file
        loadDataFromTextFile("output3.txt", tableModel);

        JTable table = new JTable(tableModel);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                 int grade = (int) value;
                if (grade < 50) {
                    component.setBackground(Color.RED);
                } else {
                    component.setBackground(Color.GREEN);
                }
                return component;
            }
        };

        // Apply the custom cell renderer to the "Grade" column
        table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JButton colorButton = new JButton("next");
        colorButton.addActionListener(new ActionListener() {
            @Override
           public void actionPerformed(ActionEvent e) {
                frame.dispose();
                students();
        }});
        frame.getContentPane().add(colorButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
  
    
    private void students() {
    	
    	 JFrame frame3 = new JFrame("Student");
         frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame3.setSize(600, 400);
         int minGrade = (int) tableModel.getValueAt(0, 1);; // Initialize with the highest possible value
         int maxGrade = (int) tableModel.getValueAt(0, 1);; // Initialize with the lowest possible value
         String studentNameWithMinGrade = "";
         String studentNameWithMaxGrade = "";

         for (int row = 0; row < tableModel.getRowCount(); row++) {
             String studentName = (String) tableModel.getValueAt(row, 0); // Assuming "Student Name" is in column 0
             int grade = (int) tableModel.getValueAt(row, 1); // Assuming "Grade" is in column 1

             if (grade <= minGrade) {
                 minGrade = grade; // Update minGrade if a lower grade is found
                 studentNameWithMinGrade = studentName; // Update studentNameWithMinGrade
             }
             if (grade >= maxGrade) {
                 maxGrade = grade; // Update maxGrade if a higher grade is found
                 studentNameWithMaxGrade = studentName; // Update studentNameWithMaxGrade
             }
         }
         JPanel panel2 = new JPanel(null);

         JLabel label2 = new JLabel(" the student with the heigest grade is "+  studentNameWithMaxGrade+" with "+maxGrade );
         label2.setBounds(50,50, 350, 20);
         JLabel label23 = new JLabel(" the student with the lowest grade is "+ studentNameWithMinGrade+" with "+minGrade );
         label23.setBounds(50, 100, 350, 20);
         JLabel label234 = new JLabel(" the student average grade is "+   studentgrade() );
         label234.setBounds(50,150, 350, 20);
         JLabel label2345 = new JLabel(" the f students beyond a user given threshold "+countStudentsWithGradeAbove50( )    );
         label2345.setBounds(50,200, 350, 20);
         panel2.add(label23);
         panel2.add(label2);
         panel2.add(label234);
         panel2.add(label2345);
         frame3.getContentPane().add(panel2);
         frame3.setVisible(true);

    }
    	private  float studentgrade() {
    		int count=0;
    		int grade1=0;
            for (int row = 0; row < tableModel.getRowCount(); row++) {
            	count++;
            	grade1 += (int) tableModel.getValueAt(row, 1);
            }
			return grade1/count;	
    	}
    	public int countStudentsWithGradeAbove50() {
    	    int count = 0;
    	    for (int row = 0; row < tableModel.getRowCount(); row++) {
    	        int grade = (int) tableModel.getValueAt(row, 1); // Assuming "Grade" is in column 1
    	        if (grade >= 50) {
    	            count++;
    	        }
    	    }
    	    return count;
    	}
}
