package main.task6;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Solution6AFileConverterApp extends JFrame {
    private JButton selectFilesButton, startButton, cancelButton;
    private JComboBox<String> conversionOptions;
    private JProgressBar progressBar;
    private JTextArea statusArea;
    private JList<File> fileList;

    private ExecutorService executor;
    private List<Future<?>> futures;

    public Solution6AFileConverterApp() {
        // Initialize components
        selectFilesButton = new JButton("Select Files");
        startButton = new JButton("Start Conversion");
        cancelButton = new JButton("Cancel");

        conversionOptions = new JComboBox<>(new String[]{"PDF to Docx", "Image Resize"});
        progressBar = new JProgressBar();
        statusArea = new JTextArea(5, 20);
        statusArea.setEditable(false);
        fileList = new JList<>(new DefaultListModel<>());

        // Layout GUI components
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topPanel.add(selectFilesButton);
        topPanel.add(conversionOptions);
        topPanel.add(startButton);
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(fileList), BorderLayout.CENTER);
        add(progressBar, BorderLayout.SOUTH);
        add(new JScrollPane(statusArea), BorderLayout.EAST);
        add(cancelButton, BorderLayout.WEST);

        // Set up action listeners
        selectFilesButton.addActionListener(e -> selectFiles());
        startButton.addActionListener(e -> startConversion());
        cancelButton.addActionListener(e -> cancelConversion());

        // Set up the JFrame
        setTitle("File Converter");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void selectFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("All Files", "*.*");
        fileChooser.setFileFilter(filter);

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();
            DefaultListModel<File> model = (DefaultListModel<File>) fileList.getModel();
            for (File file : files) {
                model.addElement(file);
            }
        }
    }

    private void startConversion() {
        DefaultListModel<File> model = (DefaultListModel<File>) fileList.getModel();
        if (model.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No files selected.");
            return;
        }

        String conversionType = (String) conversionOptions.getSelectedItem();
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        futures = new java.util.ArrayList<>();

        progressBar.setValue(0);
        progressBar.setMaximum(model.getSize());
        statusArea.setText("");

        for (int i = 0; i < model.getSize(); i++) {
            File file = model.getElementAt(i);
            FileConversionTask task = new FileConversionTask(file, conversionType, i + 1);
            Future<?> future = executor.submit(task);
            futures.add(future);
        }

        executor.shutdown();
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                while (!executor.isTerminated()) {
                    Thread.sleep(500); // Check progress periodically
                    int completedTasks = 0;
                    for (Future<?> future : futures) {
                        if (future.isDone()) {
                            completedTasks++;
                        }
                    }
                    progressBar.setValue(completedTasks);
                }
                return null;
            }

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(Solution6AFileConverterApp.this, "Conversion Complete");
            }
        }.execute();
    }

    private void cancelConversion() {
        if (futures != null && !futures.isEmpty()) {
            for (Future<?> future : futures) {
                future.cancel(true); // Cancel each task
            }
            executor.shutdownNow();
            statusArea.append("Conversion cancelled.\n");
        }
    }

    private class FileConversionTask implements Runnable {
        private final File file;
        private final String conversionType;
        private final int taskId;

        public FileConversionTask(File file, String conversionType, int taskId) {
            this.file = file;
            this.conversionType = conversionType;
            this.taskId = taskId;
        }

        @Override
        public void run() {
            try {
                statusArea.append("Task " + taskId + ": Converting " + file.getName() + " (" + conversionType + ")\n");
                // Simulate file conversion with sleep
                TimeUnit.SECONDS.sleep(2);
                if (Thread.currentThread().isInterrupted()) {
                    return; // Task was cancelled
                }
                statusArea.append("Task " + taskId + ": Completed " + file.getName() + "\n");
            } catch (InterruptedException e) {
                statusArea.append("Task " + taskId + ": Cancelled " + file.getName() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Solution6AFileConverterApp app = new Solution6AFileConverterApp();
            app.setVisible(true);
        });
    }
}