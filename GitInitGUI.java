import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class GitInitGUI {
    private JFrame frame;
    private JTextField pathField;
    private JTextField tokenField;
    private JTextField projectNameField;
    private JTextField usernameField;
    private JCheckBox isPrivateCheckBox;

    public static void main(String[] args) {
        new GitInitGUI().createGUI();
    }

    public void createGUI() {
        frame = new JFrame("GitHub Repository Initializer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 250);
        frame.setLayout(new GridLayout(6, 2, 5, 5));  

        frame.add(new JLabel("GitHub Username:"));
        usernameField = new JTextField();
        frame.add(usernameField);

        frame.add(new JLabel("Project Path:"));
        pathField = new JTextField();
        frame.add(pathField);

        frame.add(new JLabel("GitHub Token:"));
        tokenField = new JTextField();
        frame.add(tokenField);

        frame.add(new JLabel("Project Name:"));
        projectNameField = new JTextField();
        frame.add(projectNameField);

        frame.add(new JLabel("Private Repository:"));
        isPrivateCheckBox = new JCheckBox();
        frame.add(isPrivateCheckBox);

        JButton submitButton = new JButton("Initialize and Push");
        submitButton.addActionListener(e -> initializeAndPush());
        frame.add(submitButton);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }