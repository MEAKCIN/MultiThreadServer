package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;

public class GasCurrencyGUI {

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel mainMenu;
    private boolean isDarkMode = false;
    private JTextArea consoleOutput;

    public GasCurrencyGUI() {
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

        JButton specificDateButton = createModernButton("Specific Date");
        specificDateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        specificDateButton.addActionListener(e -> {
            showSpecificDatePopup();
            System.out.println("Specific Date option selected.");
        });
        gasPricesMenu.add(specificDateButton);
        gasPricesMenu.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton differenceButton = createModernButton("Difference Between Two Dates");
        differenceButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        differenceButton.addActionListener(e -> {
            showDifferenceBetweenDatesPopup();
            System.out.println("Difference Between Two Dates option selected.");
        });
        gasPricesMenu.add(differenceButton);
        gasPricesMenu.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton averageButton = createModernButton("Average Between Two Dates");
        averageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        averageButton.addActionListener(e -> {
            showAverageBetweenDatesPopup();
            System.out.println("Average Between Two Dates option selected.");
        });
        gasPricesMenu.add(averageButton);
        gasPricesMenu.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton backButton = createModernButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "MainMenu");
            System.out.println("Back to main menu.");
        });
        gasPricesMenu.add(backButton);

        mainPanel.add(gasPricesMenu, "GasPricesMenu");
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
            String message = "Gas price for date " + date + " is: [Result]";
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
            String message = "Difference in gas prices between " + fromDate + " and " + toDate + " is: [Result]";
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
            String message = "Average gas price between " + fromDate + " and " + toDate + " is: [Result]";
            JOptionPane.showMessageDialog(frame, message, "Average Gas Price", JOptionPane.INFORMATION_MESSAGE);
            System.out.println(message);
        }
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

        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        currencyExchangeMenu.add(dateLabel, gbc);

        JPanel datePanel = new JPanel();
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        datePanel.add(dateSpinner);
        gbc.gridx = 1;
        gbc.gridy = 2;
        currencyExchangeMenu.add(dateSpinner, gbc);

        JButton submitButton = createModernButton("Submit");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        currencyExchangeMenu.add(submitButton, gbc);

        JButton backButton = createModernButton("Back");
        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "MainMenu");
            System.out.println("Back to main menu.");
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        currencyExchangeMenu.add(backButton, gbc);

        mainPanel.add(currencyExchangeMenu, "CurrencyExchangeMenu");
    }

    private void createConsolePanel() {
        consoleOutput = new JTextArea(8, 40);
        consoleOutput.setEditable(false);
        consoleOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(consoleOutput);

        PrintStream consoleStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                consoleOutput.append(String.valueOf((char) b));
                consoleOutput.setCaretPosition(consoleOutput.getDocument().getLength());
            }
        });
        System.setOut(consoleStream);
        System.setErr(consoleStream);

        frame.add(scrollPane, BorderLayout.SOUTH);
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
        SwingUtilities.invokeLater(GasCurrencyGUI::new);
    }
}
