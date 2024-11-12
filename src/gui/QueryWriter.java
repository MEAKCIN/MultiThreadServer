package gui;

import server.MultithreadServer;
import client.MultithreadClient;

import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

public class QueryWriter {

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel mainMenu;
    private boolean isDarkMode = false;
    private JTextArea consoleOutput;

    public QueryWriter() {
        frame = new JFrame("Gas Prices and Currency Exchange");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        createMainMenu();
        createGasPricesMenu();
        createCurrencyExchangeMenu();
        createConsolePanel();

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void createMainMenu() {
        mainMenu = new JPanel();
        mainMenu.setLayout(new BoxLayout(mainMenu, BoxLayout.Y_AXIS));
        mainMenu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Select an option:");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainMenu.add(label);
        mainMenu.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton gasButton = createModernButton("Gas Prices");
        gasButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        gasButton.addActionListener(e -> cardLayout.show(mainPanel, "GasPricesMenu"));
        mainMenu.add(gasButton);
        mainMenu.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton currencyButton = createModernButton("Currency Exchange Rate");
        currencyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        currencyButton.addActionListener(e -> cardLayout.show(mainPanel, "CurrencyExchangeMenu"));
        mainMenu.add(currencyButton);
        mainMenu.add(Box.createRigidArea(new Dimension(0, 20)));

        // Add Start Server Button
        JButton startServerButton = createModernButton("Start Server");
        startServerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startServerButton.addActionListener(e -> startServer());
        mainMenu.add(startServerButton);
        mainMenu.add(Box.createRigidArea(new Dimension(0, 10)));

        // Add Start Client Button
        JButton startClientButton = createModernButton("Start Client");
        startClientButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startClientButton.addActionListener(e -> startClient());
        mainMenu.add(startClientButton);
        mainMenu.add(Box.createRigidArea(new Dimension(0, 20)));

        JToggleButton darkModeToggle = createToggleButton();
        darkModeToggle.setAlignmentX(Component.CENTER_ALIGNMENT);
        darkModeToggle.addActionListener(e -> toggleDarkMode(darkModeToggle.isSelected()));
        mainMenu.add(darkModeToggle);

        mainPanel.add(mainMenu, "MainMenu");
    }

    private void createGasPricesMenu() {
        JPanel gasPricesMenu = new JPanel();
        gasPricesMenu.setLayout(new BoxLayout(gasPricesMenu, BoxLayout.Y_AXIS));
        gasPricesMenu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Gas Prices Options:");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        gasPricesMenu.add(label);
        gasPricesMenu.add(Box.createRigidArea(new Dimension(0, 20)));

        // Specific Date Button
        JButton specificDateButton = createModernButton("Specific Date");
        specificDateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        specificDateButton.addActionListener(e -> {
            // Show input dialog for specific date
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel dateLabel = new JLabel("Select Date:");
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(dateLabel, gbc);

            SpinnerDateModel dateModel = new SpinnerDateModel();
            JSpinner dateSpinner = new JSpinner(dateModel);
            dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
            gbc.gridx = 1;
            gbc.gridy = 0;
            panel.add(dateSpinner, gbc);

            int result = JOptionPane.showConfirmDialog(frame, panel, "Select Date", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String date = ((JSpinner.DateEditor) dateSpinner.getEditor()).getFormat().format(dateSpinner.getValue());
                String command = "Alpha416 GAS -date " + date;
                JOptionPane.showMessageDialog(frame, "Generated Command: " + command, "Gas Price Result", JOptionPane.INFORMATION_MESSAGE);
                System.out.println(command);
                // Here you could also send this command to the client
            }
        });
        gasPricesMenu.add(specificDateButton);
        gasPricesMenu.add(Box.createRigidArea(new Dimension(0, 10)));

        // Average Between Two Dates Button
        JButton averageButton = createModernButton("Average Between Two Dates");
        averageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        averageButton.addActionListener(e -> {
            // Show input dialog for selecting two dates
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel fromLabel = new JLabel("From Date:");
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(fromLabel, gbc);

            SpinnerDateModel fromDateModel = new SpinnerDateModel();
            JSpinner fromDateSpinner = new JSpinner(fromDateModel);
            fromDateSpinner.setEditor(new JSpinner.DateEditor(fromDateSpinner, "yyyy-MM-dd"));
            gbc.gridx = 1;
            gbc.gridy = 0;
            panel.add(fromDateSpinner, gbc);

            JLabel toLabel = new JLabel("To Date:");
            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(toLabel, gbc);

            SpinnerDateModel toDateModel = new SpinnerDateModel();
            JSpinner toDateSpinner = new JSpinner(toDateModel);
            toDateSpinner.setEditor(new JSpinner.DateEditor(toDateSpinner, "yyyy-MM-dd"));
            gbc.gridx = 1;
            gbc.gridy = 1;
            panel.add(toDateSpinner, gbc);

            int result = JOptionPane.showConfirmDialog(frame, panel, "Average Between Two Dates", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String fromDate = ((JSpinner.DateEditor) fromDateSpinner.getEditor()).getFormat().format(fromDateSpinner.getValue());
                String toDate = ((JSpinner.DateEditor) toDateSpinner.getEditor()).getFormat().format(toDateSpinner.getValue());
                String command = "Alpha416 GAS -date " + fromDate + " -average " + toDate;
                JOptionPane.showMessageDialog(frame, "Generated Command: " + command, "Average Gas Price", JOptionPane.INFORMATION_MESSAGE);
                System.out.println(command);
                // Here you could also send this command to the client
            }
        });
        gasPricesMenu.add(averageButton);
        gasPricesMenu.add(Box.createRigidArea(new Dimension(0, 10)));

        // Change Between Two Dates Button
        JButton differenceButton = createModernButton("Difference Between Two Dates");
        differenceButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        differenceButton.addActionListener(e -> {
            // Show input dialog for selecting two dates
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel fromLabel = new JLabel("From Date:");
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(fromLabel, gbc);

            SpinnerDateModel fromDateModel = new SpinnerDateModel();
            JSpinner fromDateSpinner = new JSpinner(fromDateModel);
            fromDateSpinner.setEditor(new JSpinner.DateEditor(fromDateSpinner, "yyyy-MM-dd"));
            gbc.gridx = 1;
            gbc.gridy = 0;
            panel.add(fromDateSpinner, gbc);

            JLabel toLabel = new JLabel("To Date:");
            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(toLabel, gbc);

            SpinnerDateModel toDateModel = new SpinnerDateModel();
            JSpinner toDateSpinner = new JSpinner(toDateModel);
            toDateSpinner.setEditor(new JSpinner.DateEditor(toDateSpinner, "yyyy-MM-dd"));
            gbc.gridx = 1;
            gbc.gridy = 1;
            panel.add(toDateSpinner, gbc);

            int result = JOptionPane.showConfirmDialog(frame, panel, "Difference Between Two Dates", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String fromDate = ((JSpinner.DateEditor) fromDateSpinner.getEditor()).getFormat().format(fromDateSpinner.getValue());
                String toDate = ((JSpinner.DateEditor) toDateSpinner.getEditor()).getFormat().format(toDateSpinner.getValue());
                String command = "Alpha416 GAS -date " + fromDate + " -change " + toDate;
                JOptionPane.showMessageDialog(frame, "Generated Command: " + command, "Gas Price Difference", JOptionPane.INFORMATION_MESSAGE);
                System.out.println(command);
                // Here you could also send this command to the client
            }
        });
        gasPricesMenu.add(differenceButton);
        gasPricesMenu.add(Box.createRigidArea(new Dimension(0, 20)));

        // Back Button
        JButton backButton = createModernButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "MainMenu");
            System.out.println("Back to main menu.");
        });
        gasPricesMenu.add(backButton);

        mainPanel.add(gasPricesMenu, "GasPricesMenu");
    }


    private void createCurrencyExchangeMenu() {
        JPanel currencyExchangeMenu = new JPanel(new GridBagLayout());
        currencyExchangeMenu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel fromLabel = new JLabel("From Currency:");
        fromLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        currencyExchangeMenu.add(fromLabel, gbc);

        String[] currencies = {"USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "INR", "NZD", "SGD", "HKD", "SEK", "NOK", "DKK", "ZAR", "MXN", "BRL", "RUB", "KRW", "PLN", "THB", "MYR", "IDR", "TRY", "SAR", "AED", "PHP", "VND", "EGP", "CLP", "CZK", "HUF", "ILS", "TWD"};
        JComboBox<String> fromDropdown = new JComboBox<>(currencies);
        gbc.gridx = 1;
        gbc.gridy = 0;
        currencyExchangeMenu.add(fromDropdown, gbc);

        JLabel toLabel = new JLabel("To Currency:");
        toLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        currencyExchangeMenu.add(toLabel, gbc);

        JComboBox<String> toDropdown = new JComboBox<>(currencies);
        gbc.gridx = 1;
        gbc.gridy = 1;
        currencyExchangeMenu.add(toDropdown, gbc);

        JLabel optionsLabel = new JLabel("Options:");
        optionsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        currencyExchangeMenu.add(optionsLabel, gbc);

        // Checkbox for show from_name
        JCheckBox showFromNameCheckBox = new JCheckBox("Show from_name");
        gbc.gridx = 1;
        gbc.gridy = 2;
        currencyExchangeMenu.add(showFromNameCheckBox, gbc);

        // Checkbox for show to_name
        JCheckBox showToNameCheckBox = new JCheckBox("Show to_name");
        gbc.gridx = 1;
        gbc.gridy = 3;
        currencyExchangeMenu.add(showToNameCheckBox, gbc);

        // Checkbox for last refresh
        JCheckBox lastRefreshCheckBox = new JCheckBox("Last refresh");
        gbc.gridx = 1;
        gbc.gridy = 4;
        currencyExchangeMenu.add(lastRefreshCheckBox, gbc);

        JButton submitButton = createModernButton("Submit");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        submitButton.addActionListener(e -> {
            // Get the selected currencies
            String fromCurrency = (String) fromDropdown.getSelectedItem();
            String toCurrency = (String) toDropdown.getSelectedItem();

            // Start building the command string
            StringBuilder command = new StringBuilder("Alpha416 EXC -from " + fromCurrency + " -to " + toCurrency);

            // Add optional parameters based on checkbox selections
            if (showFromNameCheckBox.isSelected()) {
                command.append(" -from_name");
            }
            if (showToNameCheckBox.isSelected()) {
                command.append(" -to_name");
            }
            if (lastRefreshCheckBox.isSelected()) {
                command.append(" -refresh");
            }

            // Output the generated command
            String finalCommand = command.toString();
            JOptionPane.showMessageDialog(frame, "Generated Command: " + finalCommand, "Currency Exchange Command", JOptionPane.INFORMATION_MESSAGE);
            System.out.println(finalCommand);

            // Here you could also send this command to the client terminal
            // For example:
            // connectionToServer.sendForAnswer(finalCommand);
        });
        currencyExchangeMenu.add(submitButton, gbc);

        JButton backButton = createModernButton("Back");
        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "MainMenu");
            System.out.println("Back to main menu.");
        });
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        currencyExchangeMenu.add(backButton, gbc);

        mainPanel.add(currencyExchangeMenu, "CurrencyExchangeMenu");
    }


    private void createConsolePanel() {
        consoleOutput = new JTextArea(8, 40);
        consoleOutput.setEditable(false);
        consoleOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(consoleOutput);

        // Redirect console output to the JTextArea
        redirectOutputToGUI();

        frame.add(scrollPane, BorderLayout.SOUTH);
    }

    private void showSpecificDatePopup() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel dateLabel = new JLabel("Select Date:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(dateLabel, gbc);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(dateSpinner, gbc);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Select Date", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String date = ((JSpinner.DateEditor) dateSpinner.getEditor()).getFormat().format(dateSpinner.getValue());
            String message = "Gas price for date " + date + " inputStream: [Result]";
            JOptionPane.showMessageDialog(frame, message, "Gas Price Result", JOptionPane.INFORMATION_MESSAGE);
            System.out.println(message);
        }
    }

    private void showDifferenceBetweenDatesPopup() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel fromLabel = new JLabel("From Date:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(fromLabel, gbc);

        SpinnerDateModel fromDateModel = new SpinnerDateModel();
        JSpinner fromDateSpinner = new JSpinner(fromDateModel);
        fromDateSpinner.setEditor(new JSpinner.DateEditor(fromDateSpinner, "yyyy-MM-dd"));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(fromDateSpinner, gbc);

        JLabel toLabel = new JLabel("To Date:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(toLabel, gbc);

        SpinnerDateModel toDateModel = new SpinnerDateModel();
        JSpinner toDateSpinner = new JSpinner(toDateModel);
        toDateSpinner.setEditor(new JSpinner.DateEditor(toDateSpinner, "yyyy-MM-dd"));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(toDateSpinner, gbc);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Difference Between Two Dates", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String fromDate = ((JSpinner.DateEditor) fromDateSpinner.getEditor()).getFormat().format(fromDateSpinner.getValue());
            String toDate = ((JSpinner.DateEditor) toDateSpinner.getEditor()).getFormat().format(toDateSpinner.getValue());
            String message = "Difference in gas prices between " + fromDate + " and " + toDate + " inputStream: [Result]";
            JOptionPane.showMessageDialog(frame, message, "Gas Price Difference", JOptionPane.INFORMATION_MESSAGE);
            System.out.println(message);
        }
    }

    private void showAverageBetweenDatesPopup() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel fromLabel = new JLabel("From Date:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(fromLabel, gbc);

        SpinnerDateModel fromDateModel = new SpinnerDateModel();
        JSpinner fromDateSpinner = new JSpinner(fromDateModel);
        fromDateSpinner.setEditor(new JSpinner.DateEditor(fromDateSpinner, "yyyy-MM-dd"));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(fromDateSpinner, gbc);

        JLabel toLabel = new JLabel("To Date:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(toLabel, gbc);

        SpinnerDateModel toDateModel = new SpinnerDateModel();
        JSpinner toDateSpinner = new JSpinner(toDateModel);
        toDateSpinner.setEditor(new JSpinner.DateEditor(toDateSpinner, "yyyy-MM-dd"));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(toDateSpinner, gbc);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Average Between Two Dates", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String fromDate = ((JSpinner.DateEditor) fromDateSpinner.getEditor()).getFormat().format(fromDateSpinner.getValue());
            String toDate = ((JSpinner.DateEditor) toDateSpinner.getEditor()).getFormat().format(toDateSpinner.getValue());
            String message = "Average gas price between " + fromDate + " and " + toDate + " inputStream: [Result]";
            JOptionPane.showMessageDialog(frame, message, "Average Gas Price", JOptionPane.INFORMATION_MESSAGE);
            System.out.println(message);
        }
    }


    private void redirectOutputToGUI() {
        PrintStream consoleStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                consoleOutput.append(String.valueOf((char) b));
                consoleOutput.setCaretPosition(consoleOutput.getDocument().getLength());
            }
        });

        System.setOut(consoleStream);
        System.setErr(consoleStream);
    }



    private void startServer() {
        // Run the MultithreadServer in a separate thread
        Thread serverThread = new Thread(() -> {
            try {
                MultithreadServer.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        System.out.println("Server started successfully.");
    }

    private void startClient() {
        // Run the MultithreadClient in a separate thread
        Thread clientThread = new Thread(() -> {
            try {
                MultithreadClient.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        clientThread.start();
        System.out.println("Client started successfully.");
    }

    private JButton createModernButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private JToggleButton createToggleButton() {
        JToggleButton toggleButton = new JToggleButton();
        toggleButton.setPreferredSize(new Dimension(80, 40));
        toggleButton.setText("Light Mode");
        toggleButton.setFocusPainted(false);
        toggleButton.setFont(new Font("Arial", Font.BOLD, 12));
        toggleButton.addItemListener(e -> {
            if (toggleButton.isSelected()) {
                toggleButton.setText("Dark Mode");
            } else {
                toggleButton.setText("Light Mode");
            }
        });
        return toggleButton;
    }

    private void toggleDarkMode(boolean isSelected) {
        if (isSelected) {
            mainMenu.setBackground(Color.DARK_GRAY);
            for (Component component : mainMenu.getComponents()) {
                if (component instanceof JLabel || component instanceof JButton || component instanceof JToggleButton) {
                    component.setForeground(Color.WHITE);
                }
            }
            isDarkMode = true;
        } else {
            mainMenu.setBackground(Color.WHITE);
            for (Component component : mainMenu.getComponents()) {
                if (component instanceof JLabel || component instanceof JButton || component instanceof JToggleButton) {
                    component.setForeground(Color.BLACK);
                }
            }
            isDarkMode = false;
        }
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QueryWriter::new);
    }
}
