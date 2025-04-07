package src;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

public class TagExtractorGUI extends JFrame implements ActionListener {

    private JFileChooser fileChooser;
    private JButton openTextFileButton;
    private JButton openStopWordFileButton;
    private JLabel currentFileLabel;
    private JTextArea tagsTextArea;
    private JButton saveTagsButton;
    private Set<String> stopWords;
    private File selectedTextFile;

    public TagExtractorGUI() {
        setTitle("Tag Extractor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Initialize components
        fileChooser = new JFileChooser();
        openTextFileButton = new JButton("Open Text File");
        openStopWordFileButton = new JButton("Open Stop Word File");
        currentFileLabel = new JLabel("No text file selected");
        tagsTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(tagsTextArea);
        saveTagsButton = new JButton("Save Tags");
        stopWords = new HashSet<>();

        // Set action listeners
        openTextFileButton.addActionListener(this);
        openStopWordFileButton.addActionListener(this);
        saveTagsButton.addActionListener(this);

        // Layout using BorderLayout for the main frame
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top panel for file selection buttons and current file label
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(openTextFileButton);
        topPanel.add(openStopWordFileButton);
        topPanel.add(currentFileLabel);

        // Center panel for the tags display area
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JLabel("Extracted Tags and Frequencies:"), BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for the save button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(saveTagsButton);

        // Add panels to the main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openTextFileButton) {
            // Filter for text files
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                selectedTextFile = fileChooser.getSelectedFile();
                currentFileLabel.setText("Processing file: " + selectedTextFile.getName());
                extractTagsAndDisplay();
            }
        } else if (e.getSource() == openStopWordFileButton) {
            // Filter for text files
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File stopWordFile = fileChooser.getSelectedFile();
                loadStopWords(stopWordFile);
                // If a text file is already selected, re-extract tags with the new stop words
                if (selectedTextFile != null) {
                    extractTagsAndDisplay();
                }
            }
        } else if (e.getSource() == saveTagsButton) {
            if (tagsTextArea.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No tags to save.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            fileChooser.setDialogTitle("Save Tags");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
            int returnVal = fileChooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                    writer.write(tagsTextArea.getText());
                    JOptionPane.showMessageDialog(this, "Tags saved to " + fileToSave.getName(), "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void loadStopWords(File stopWordFile) {
        stopWords.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(stopWordFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toLowerCase();
                if (!word.isEmpty()) {
                    stopWords.add(word);
                }
            }
            JOptionPane.showMessageDialog(this, "Stop words loaded from " + stopWordFile.getName(), "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading stop words: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void extractTagsAndDisplay() {
        if (selectedTextFile == null) {
            JOptionPane.showMessageDialog(this, "Please select a text file first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Map<String, Integer> wordFrequencies = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(selectedTextFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Tokenize by splitting on non-letter characters
                String[] words = line.toLowerCase().split("[^a-z]+");
                for (String word : words) {
                    word = word.trim();
                    if (!word.isEmpty() && !stopWords.contains(word)) {
                        wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Sort word frequencies by value (descending order)
        List<Map.Entry<String, Integer>> sortedFrequencies = new ArrayList<>(wordFrequencies.entrySet());
        sortedFrequencies.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Display the sorted frequencies in the JTextArea
        tagsTextArea.setText("");
        if (sortedFrequencies.isEmpty()) {
            tagsTextArea.append("No tags found after processing.\n");
        } else {
            for (Map.Entry<String, Integer> entry : sortedFrequencies) {
                tagsTextArea.append(entry.getKey() + ": " + entry.getValue() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure GUI objects are created on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> new TagExtractorGUI());
    }
}