import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import git.tools.client.GitSubprocessClient;
import github.tools.client.GitHubApiClient;
import github.tools.client.RequestParams;

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

    private void initializeAndPush() {
        String username = usernameField.getText();
        String projectPath = pathField.getText();
        String token = tokenField.getText();
        String projectName = projectNameField.getText();
        boolean isPrivate = isPrivateCheckBox.isSelected();
    
        if (username.isEmpty() || projectPath.isEmpty() || token.isEmpty() || projectName.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            GitSubprocessClient gitClient = new GitSubprocessClient(projectPath);
            System.out.println("Initializing Git repository...");
            gitClient.gitInit();
    
            // Create .gitignore and README.md, and add content
            createGitignore(projectPath);
            createReadMe(projectPath, projectName);
    
            // Stage all changes
            System.out.println("Adding all files...");
            gitClient.gitAddAll();
    
            // Commit changes
            System.out.println("Committing files...");
            gitClient.gitCommit("Initial commit");
    
            // Setup GitHub API client and create repo
            GitHubApiClient githubClient = new GitHubApiClient(username, token);
            RequestParams params = new RequestParams();
            params.addParam("name", projectName);
            params.addParam("description", "Automatically created repo");
            params.addParam("private", isPrivate);
    
            System.out.println("Creating GitHub repository...");
            githubClient.createRepo(params);
    
            String repoUrl = "https://github.com/" + username + "/" + projectName + ".git";
            System.out.println("Setting remote origin to " + repoUrl);
            gitClient.gitRemoteAdd("origin", repoUrl);
    
            
            System.out.println("Pushing to GitHub...");
            gitClient.gitPush("master");

    
            JOptionPane.showMessageDialog(frame, "Repository initialized and pushed successfully! URL: " + repoUrl);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();  //  debugging
        }
    }
    
    
    
    private void createGitignore(String projectPath) throws IOException {
        String gitignoreContent = "*.class\n*.log\n.DS_Store";
        Files.write(Paths.get(projectPath, ".gitignore"), gitignoreContent.getBytes());
    }
    
    private void createReadMe(String projectPath, String projectName) throws IOException {
        String readmeContent = "# " + projectName;
        Files.write(Paths.get(projectPath, "README.md"), readmeContent.getBytes());
    }
}    