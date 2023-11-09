import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class FileSearchApp extends JFrame {

    JTextField searchField;
    JTextArea originalTextArea;
    JTextArea filteredTextArea;
    JButton loadFileButton;
    JButton searchButton;
    JButton quitButton;
    java.io.File selectedFile;

    public FileSearchApp(){
        searchField = new JTextField(10);
        loadFileButton = new JButton("Load File");
        searchButton = new JButton("Search");
        quitButton = new JButton("Quit");
        originalTextArea = new JTextArea(15,30);
        filteredTextArea = new JTextArea(15,30);
        JScrollPane originalScrollPane = new JScrollPane(originalTextArea);
        JScrollPane filteredScrollPane = new JScrollPane(filteredTextArea);

        setLayout(new BorderLayout());
        JPanel topPnl = new JPanel(new FlowLayout());
        topPnl.add(loadFileButton);
        topPnl.add(new JLabel("Enter search string:"));
        topPnl.add(searchField);
        topPnl.add(searchButton);
        topPnl.add(quitButton);
        JPanel bottomPnl = new JPanel();
        bottomPnl.add(originalScrollPane);
        bottomPnl.add(filteredScrollPane);

        loadFileButton.addActionListener(e -> loadFile());
        searchButton.addActionListener(e -> searchFile());
        quitButton.addActionListener(e -> System.exit(0));

        add(topPnl, BorderLayout.NORTH);
        add(bottomPnl, BorderLayout.CENTER);

        setTitle("File Search App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setCurrentDirectory(workingDirectory);

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(this, "File loaded successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                StringBuilder content = new StringBuilder();
                reader.lines().forEach(line -> content.append(line).append("\n"));
                originalTextArea.setText(content.toString());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading the file", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void searchFile() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Please load a file first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String searchString = searchField.getText();

        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
            List<String> resultLines = reader.lines()
                    .filter(line -> line.contains(searchString))
                    .toList();

            StringBuilder content = new StringBuilder();
            for (String line : resultLines) {
                content.append(line).append("\n");
            }
            filteredTextArea.setText(content.toString());

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading the file", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new FileSearchApp();
    }
}