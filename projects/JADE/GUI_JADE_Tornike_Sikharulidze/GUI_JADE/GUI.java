
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.IntStream;

public final class GUI extends JFrame implements ActionListener {
    JLabel leftPanelRoundsLabel;
    JLabel leftPanelExtraInformation;
    JList<String> list;
    private MainAgent mainAgent;
    private JPanel rightPanel;
    private JTextArea rightPanelLoggingTextArea;
    private LoggingOutputStream loggingOutputStream;
    private static DefaultTableModel tModel;
    private MainAgent.PlayerInformation.GameParametersStruct gameParametersStruct;

    public GUI() {
        initUI();
    }

    public GUI (MainAgent agent) {
        mainAgent = agent;
        initUI();
        loggingOutputStream = new LoggingOutputStream (rightPanelLoggingTextArea);
    }

    public void log (String s) {
        Runnable appendLine = () -> {
            rightPanelLoggingTextArea.append('[' + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] - " + s);
            rightPanelLoggingTextArea.setCaretPosition(rightPanelLoggingTextArea.getDocument().getLength());
        };
        SwingUtilities.invokeLater(appendLine);
    }

    public OutputStream getLoggingOutputStream() {
        return loggingOutputStream;
    }

    public void logLine (String s) {
        log(s + "\n");
    }

    public void setPlayersUI (String[] players) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String s : players) {
            listModel.addElement(s);
        }
        list.setModel(listModel);
    }

    public void initUI() {
        setTitle("GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        setPreferredSize(new Dimension(1000, 600));
        setJMenuBar(createMainMenuBar());
        setContentPane(createMainContentPane());

        JLabel authorLabel = new JLabel("Author: Tornike Sikharulidze");
        authorLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        authorLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // Set font and size
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 2; // Put it in the rightmost column
        gc.gridy = GridBagConstraints.RELATIVE; // Put it at the bottom
        gc.anchor = GridBagConstraints.SOUTHEAST; // Bottom-right corner
        gc.insets = new Insets(10, 10, 10, 10); // Padding
        getContentPane().add(authorLabel, gc);


        pack();
        setVisible(true);
    }

    private Container createMainContentPane() {
        JPanel pane = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridy = 0;
        gc.weightx = 0.5;
        gc.weighty = 0.5;

        //LEFT PANEL
        gc.gridx = 0;
        gc.weightx = 1;
        pane.add(createLeftPanel(), gc);

        //CENTRAL PANEL
        gc.gridx = 1;
        gc.weightx = 8;
        pane.add(createCentralPanel(), gc);

        //RIGHT PANEL
        gc.gridx = 2;
        gc.weightx = 8;
        pane.add(createRightPanel(), gc);
        return pane;
    }



    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        leftPanelRoundsLabel = new JLabel("Round 0 / null");
        JButton leftPanelNewButton = new JButton("New");
        leftPanelNewButton.addActionListener(actionEvent -> mainAgent.newGame());
        JButton leftPanelStopButton = new JButton("Stop");
        leftPanelStopButton.addActionListener(this);
        JButton leftPanelContinueButton = new JButton("Continue");
        leftPanelContinueButton.addActionListener(this);
        JButton leftPanelSetRoundButton = createSetRoundsButton();
        JButton leftPanelSetCommissionFeeButton = getCommissionFeeButton();

        leftPanelExtraInformation = new JLabel("Parameters:" + "rounds: ");

        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx = 0;
        gc.weightx = 0.5;
        gc.weighty = 0.5;

        gc.gridy = 0;
        leftPanel.add(leftPanelRoundsLabel, gc);
        gc.gridy = 1;
        leftPanel.add(leftPanelNewButton, gc);
        gc.gridy = 2;
        leftPanel.add(leftPanelStopButton, gc);
        gc.gridy = 3;
        leftPanel.add(leftPanelContinueButton, gc);
        gc.gridy = 4;
        leftPanel.add(leftPanelSetRoundButton,gc);
        gc.gridy = 5;
        leftPanel.add(leftPanelSetCommissionFeeButton,gc);
        gc.gridy = 6;
        gc.weighty = 10;
        leftPanel.add(leftPanelExtraInformation, gc);


        return leftPanel;
    }

    private JButton getCommissionFeeButton() {
        JButton leftPanelSetCommissionFeeButton = new JButton("Set commission fee");

        leftPanelSetCommissionFeeButton.addActionListener(actionEvent -> {
            String input = JOptionPane.showInputDialog(new Frame("Configure commission fee"), "set fee");

            if (isValidInput(input)) {
                double fee = Double.parseDouble(input.trim());
                updateGameParameters(fee);
            } else {
                showInvalidInputMessage();
            }
        });

        return leftPanelSetCommissionFeeButton;
    }

    private boolean isValidInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(input.trim());
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private void updateGameParameters(double fee) {
        setLeftPanelInformation(gameParametersStruct.R, fee);
        gameParametersStruct.F = fee;
    }

    private void showInvalidInputMessage() {
        JOptionPane.showMessageDialog(
                null,
                "Invalid input!",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }


    private JButton createSetRoundsButton() {
        JButton setRoundsButton = new JButton("Set rounds");

        setRoundsButton.addActionListener(event -> {
            Frame frame = new Frame("Configure rounds");
            String input = JOptionPane.showInputDialog(frame, "How many rounds?");

            if (input != null && !input.trim().isEmpty()) {
                handleRoundsInput(input.trim());
            }
        });

        return setRoundsButton;
    }

    private void handleRoundsInput(String input) {
        try {
            int rounds = Integer.parseInt(input);
            leftPanelRoundsLabel.setText("Round 0 / " + rounds);
            gameParametersStruct.R = rounds;
            logLine(rounds + " rounds");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a valid number.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private JButton createCommissionFeeButton() {
        JButton commissionFeeButton = new JButton("Set commission fee");

        commissionFeeButton.addActionListener(event -> {
            Frame frame = new Frame("Configure commission fee");
            String input = JOptionPane.showInputDialog(frame, "Set fee:");

            if (input != null && !input.trim().isEmpty()) {
                handleFeeInput(input.trim());
            }
        });

        return commissionFeeButton;
    }

    private void handleFeeInput(String input) {
        try {
            double fee = Double.parseDouble(input);
            setLeftPanelInformation(gameParametersStruct.R, fee);
            gameParametersStruct.F = fee;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a valid number.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setLeftPanelInformation(int R, double F){
        leftPanelExtraInformation.setText("Parameters:\n" +  "rounds: " + R +"\n" + "comission fee: " + F);
    }

    private JPanel createCentralPanel() {
        JPanel centralPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 0.5;

        gc.fill = GridBagConstraints.BOTH;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx = 0;

        gc.gridy = 0;
        gc.weighty = 1;
        centralPanel.add(createCentralTopSubpanel(), gc);
        gc.gridy = 1;
        gc.weighty = 4;
        centralPanel.add(createCentralBottomSubpanel(), gc);

        return centralPanel;
    }

    private JPanel createCentralTopSubpanel() {
        JPanel centralTopSubpanel = new JPanel(new GridBagLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("Empty");
        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        JLabel info1 = new JLabel("Selected player info");
        JButton updatePlayersButton = new JButton("Update players");
        updatePlayersButton.addActionListener(actionEvent -> mainAgent.updatePlayers());

        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.anchor = GridBagConstraints.CENTER;

        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridheight = 666;
        gc.fill = GridBagConstraints.BOTH;
        centralTopSubpanel.add(listScrollPane, gc);
        gc.gridx = 1;
        gc.gridheight = 1;
        gc.fill = GridBagConstraints.NONE;
        centralTopSubpanel.add(info1, gc);
        gc.gridy = 1;
        centralTopSubpanel.add(updatePlayersButton, gc);

        return centralTopSubpanel;
    }

    private JPanel createCentralBottomSubpanel() {
        JPanel centralBottomSubpanel = new JPanel(new GridBagLayout());

        String[] columnNames = {"Player", "Reward", "Defects", "Cooperates", "totalSum", "Shares"};

        Object[][] data = {
                // Add your data here, for example:

        };


        tModel = new DefaultTableModel(data, columnNames);
        JTable payoffTable = new JTable(tModel);

        JLabel payoffLabel = new JLabel("Player Results");
        payoffTable.setEnabled(false);

        JScrollPane player1ScrollPane = new JScrollPane(payoffTable);

        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 0.5;
        gc.fill = GridBagConstraints.BOTH;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;

        gc.gridx = 0;
        gc.gridy = 0;
        gc.weighty = 0.5;
        centralBottomSubpanel.add(payoffLabel, gc);
        gc.gridy = 1;
        gc.gridx = 0;
        gc.weighty = 2;
        centralBottomSubpanel.add(player1ScrollPane, gc);

        return centralBottomSubpanel;
    }


    public void addRow(String player, String Reward, String defects, String cooperates, String totalSum, String Shares) {
        tModel.addRow(new Object[]{player, Reward, defects, cooperates, totalSum, Shares});
    }

    public Object[] getRow(int rowIndex) {
        return IntStream.range(0, tModel.getColumnCount())
                .mapToObj(col -> tModel.getValueAt(rowIndex, col))
                .toArray();
    }


    public static void updateTable(ArrayList<MainAgent.PlayerInformation> players) {
        for (int i = 0; i < tModel.getRowCount(); i++) {
            MainAgent.PlayerInformation player = null;

            for (MainAgent.PlayerInformation p : players) {
                if (tModel.getValueAt(i, 0).equals(p.aid.getName())) {
                    player = p;
                    break;
                }
            }

            if (player != null) {
                tModel.setValueAt(player.currentReward, i, 1); // Reward
                tModel.setValueAt(player.decisionD, i, 2);     // Defects
                tModel.setValueAt(player.decisionC, i, 3);     // Cooperates
                tModel.setValueAt(player.currentReward, i, 4); // Total Reward
                tModel.setValueAt(player.Shares, i, 5);        // Shares
            }
        }
    }




    // Utility method to check if a string can be parsed as an integer
    private boolean isValidInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public void updateStockAndPayoff(int rowIndex, String Shares,String Reward){
        tModel.setValueAt(Reward, rowIndex, 1);
        tModel.setValueAt(Shares, rowIndex, 5);
    }
    public void removeRow(int rowIndex) {
        tModel.removeRow(rowIndex);
    }


    private JPanel createRightPanel() {
        rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weighty = 1d;
        c.weightx = 1d;

        rightPanelLoggingTextArea = new JTextArea("");
        rightPanelLoggingTextArea.setEditable(false);
        JScrollPane jScrollPane = new JScrollPane(rightPanelLoggingTextArea);
        rightPanel.add(jScrollPane, c);
        return rightPanel;
    }

    private JMenuBar createMainMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = new JMenu("File");
        JMenuItem exitFileMenu = new JMenuItem("Exit");
        exitFileMenu.setToolTipText("Exit application");
        exitFileMenu.addActionListener(this);

        JMenuItem newGameFileMenu = new JMenuItem("New Game");
        newGameFileMenu.setToolTipText("Start a new game");
        newGameFileMenu.addActionListener(this);

        menuFile.add(newGameFileMenu);
        menuFile.add(exitFileMenu);
        menuBar.add(menuFile);

        JMenu menuEdit = new JMenu("Edit");
        JMenuItem resetPlayerEditMenu = new JMenuItem("Reset Players");
        resetPlayerEditMenu.setToolTipText("Reset all player");
        resetPlayerEditMenu.setActionCommand("reset_players");
        resetPlayerEditMenu.addActionListener(this);

        JMenuItem parametersEditMenu = new JMenuItem("Parameters");
        parametersEditMenu.setToolTipText("Modify the parameters of the game");
        parametersEditMenu.addActionListener(actionEvent -> logLine("Parameters: " + JOptionPane.showInputDialog(new Frame("Configure parameters"), "Enter parameters N,S,R,I,P")));

        menuEdit.add(resetPlayerEditMenu);
        menuEdit.add(parametersEditMenu);
        menuBar.add(menuEdit);

        JMenu menuRun = new JMenu("Run");

        JMenuItem newRunMenu = new JMenuItem("New");
        newRunMenu.setToolTipText("Starts a new series of games");
        newRunMenu.addActionListener(this);

        JMenuItem stopRunMenu = new JMenuItem("Stop");
        stopRunMenu.setToolTipText("Stops the execution of the current round");
        stopRunMenu.addActionListener(this);

        JMenuItem continueRunMenu = new JMenuItem("Continue");
        continueRunMenu.setToolTipText("Resume the execution");
        continueRunMenu.addActionListener(this);

        JMenuItem roundNumberRunMenu = new JMenuItem("Number of rounds");
        roundNumberRunMenu.setToolTipText("Change the number of rounds");
        roundNumberRunMenu.addActionListener(actionEvent -> logLine(JOptionPane.showInputDialog(new Frame("Configure rounds"), "How many rounds?") + " rounds"));

        menuRun.add(newRunMenu);
        menuRun.add(stopRunMenu);
        menuRun.add(continueRunMenu);
        menuRun.add(roundNumberRunMenu);
        menuBar.add(menuRun);

        JMenu menuWindow = new JMenu("Window");

        JCheckBoxMenuItem toggleVerboseWindowMenu = new JCheckBoxMenuItem("Verbose", true);
        toggleVerboseWindowMenu.addActionListener(actionEvent -> rightPanel.setVisible(toggleVerboseWindowMenu.getState()));

        menuWindow.add(toggleVerboseWindowMenu);
        menuBar.add(menuWindow);

        return menuBar;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            logLine("Button " + button.getText());
            if (button.getText().equalsIgnoreCase("Stop")) {
                // Pause the process
                try {
                    mainAgent.pauseGame();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                // Define this method to handle pausing logic
            } else if (button.getText().equalsIgnoreCase("Continue")) {
                // Continue the process
                mainAgent.continueGame(); // Define this method to handle continuation logic
            }
        } else if (e.getSource() instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) e.getSource();
            logLine("Menu " + menuItem.getText());
        }
    }


    public class LoggingOutputStream extends OutputStream {
        private JTextArea textArea;

        public LoggingOutputStream(JTextArea jTextArea) {
            textArea = jTextArea;
        }

        @Override
        public void write(int i) throws IOException {
            textArea.append(String.valueOf((char) i));
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }
}
