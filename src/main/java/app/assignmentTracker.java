package app;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class assignmentTracker extends JFrame {

    public static boolean isScheduled = false;
    static JPanel mainPanel = new JPanel();

    public static void main(String[] args) {
        // check to see if code was ran by task scheduler
        for (String arg : args) {
            if (arg.equals("-scheduled")) {
                isScheduled = true;
                break;
            }
        }
        SwingUtilities.invokeLater(assignmentTracker::mainWindow);
    }


    // input new assignments window
    public static void mainWindow() {
        JFrame window = new JFrame("Assignment Manager");
        window.setResizable(false);
        window.setSize(700, 900);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (isScheduled) { // Reminder
             mainPanel = getReminderPanel();
        } else { // Input
             mainPanel = getInputPanel();
        }
        window.add(mainPanel);
        window.pack();
        window.setVisible(true);
    }

    private static JPanel getInputPanel() {
        JPanel assignmentPanel = new JPanel(new FlowLayout());

        // Course Code Text Field (centered, 6 characters max)
        JTextField courseCodeField = new JTextField(6);  // 6 columns wide
        ((AbstractDocument) courseCodeField.getDocument()).setDocumentFilter(new DocumentFilter() {
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= 6) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length() - length) <= 6) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        // Assignment Name Text Field (large, on the left)
        JTextField assignmentNameField = new JTextField(20);  // 20 columns wide

        // Date Text Field (right, 10 characters max)
        JTextField dateField = new JTextField(10);  // 10 columns wide
        ((AbstractDocument) dateField.getDocument()).setDocumentFilter(new DocumentFilter() {
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= 10) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length() - length) <= 10) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        // Add components to the panel
        assignmentPanel.add(assignmentNameField);
        assignmentPanel.add(courseCodeField);
        assignmentPanel.add(dateField);

        // Submit Button
        JButton submitButton = new JButton("Track Assignment");
        submitButton.addActionListener(_ -> {

            String assignmentName = assignmentNameField.getText();
            String courseCode = courseCodeField.getText();
            String dueDate = dateField.getText();

            writeToFile(assignmentName, courseCode, dueDate);
        });
        assignmentPanel.add(submitButton);

        return assignmentPanel;
    }


    private static JPanel getReminderPanel() {
        JPanel reminderPanel = new JPanel(new FlowLayout());

        return reminderPanel;

    }
    public static void writeToFile(String assignmentName, String courseCode, String dueDate) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/app/assignments.txt", true))) {
            writer.write(assignmentName + "," + courseCode + "," + dueDate);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String date() { // get current date
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = currentDateTime.format(formatter);
        return ("Formatted current date and time: " + formattedDateTime);
    }
}


