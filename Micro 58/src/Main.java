import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }
    private static void createAndShowGUI() {
        // Create the frame
        JFrame frame = new JFrame("Tomasulo Simulator Configuration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
    
        // Panel for inputs
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField addSubStationsField = addInputField(inputPanel, "Add/Sub Reservation Stations:");
        JTextField mulDivStationsField = addInputField(inputPanel, "Mul/Div Reservation Stations:");
        JTextField loadBuffersField = addInputField(inputPanel, "Load Buffers:");
        JTextField storeBuffersField = addInputField(inputPanel, "Store Buffers:");
        JTextField addSubLatencyField = addInputField(inputPanel, "Add/Sub Latency:");
        JTextField mulLatencyField = addInputField(inputPanel, "Mul Latency:");
        JTextField subiLatencyField = addInputField(inputPanel, "Subi Latency:");
        JTextField divLatencyField = addInputField(inputPanel, "Div Latency:");
        JTextField loadStoreLatencyField = addInputField(inputPanel, "Load/Store Latency:");
        JTextField TotalRegisters = addInputField(inputPanel, "Total Number of Registers:");
        JLabel currentCycleLabel = new JLabel("Current Cycle: 0");
        frame.add(currentCycleLabel, BorderLayout.PAGE_START);
        frame.add(inputPanel, BorderLayout.NORTH);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));
        topPanel.add(inputPanel);
        topPanel.add(currentCycleLabel);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Text area for code input
        JLabel inputLabel = new JLabel("Enter Your Code Here:");
        centerPanel.add(inputLabel);
        JTextArea codeArea = new JTextArea(20, 40);
        centerPanel.add(new JScrollPane(codeArea));

        // Text area for output
        JLabel outputLabel = new JLabel("Simulation Output:");
        centerPanel.add(outputLabel);
        JTextArea outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false); // Make the output area read-only
        centerPanel.add(new JScrollPane(outputArea));
      
        // Add 	 to the frame
        frame.add(topPanel, BorderLayout.PAGE_START);

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.pack(); // Pack the frame after adding all components
        frame.setVisible(true); 
        JButton startButton = new JButton("Save and Start Simulation");
        
        startButton.addActionListener(e -> {
            try {
                outputArea.setText("Simulation started...\n");
                String filename = "Mips";  
            	 File file = new File(filename);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	                    writer.write(codeArea.getText());
			                }
			Tomasulo tomasulo= new Tomasulo();
			int totalLoadBuffers = Integer.parseInt(loadBuffersField.getText());
			int totalStoreBuffers = Integer.parseInt(storeBuffersField.getText());
			int totalAddSubReservationStations = Integer.parseInt(addSubStationsField.getText());
			int totalMultDivReservationStations = Integer.parseInt(mulDivStationsField.getText());
			int totalRegisters = Integer.parseInt(TotalRegisters.getText());
			int loadStoreLatency = Integer.parseInt(loadStoreLatencyField.getText());
			int addSubLatency = Integer.parseInt(addSubLatencyField.getText());
			int mulLatency = Integer.parseInt(mulLatencyField.getText());
			int divLatency = Integer.parseInt(divLatencyField.getText());
          int subiLatency = Integer.parseInt(subiLatencyField.getText());

        Fetch fetch = new Fetch(tomasulo, totalLoadBuffers, totalStoreBuffers, 
        totalAddSubReservationStations, totalMultDivReservationStations, 
        totalRegisters, loadStoreLatency, addSubLatency, 
        mulLatency, divLatency, subiLatency);

    
   
        fetch.loadInstructions(filename);
        tomasulo.initialize(fetch);
        PrintStream printStream = new PrintStream(new TextAreaOutputStream(outputArea));
        System.setOut(printStream);
        System.setErr(printStream);
        tomasulo.initializeSimulation();
        JPanel tablesPanel = new JPanel();
        tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS)); // Or use new GridLayout(4, 1) for equal sizing

        // Create tables
        JTable instructionsTable = createTable(new String[]{"Instruction", "Issue", "Start", "Finish", "Write-Back"});
        JTable loadStoreBuffersTable = createTable(new String[]{"Name", "Busy", "Address", "FU", "Time"});
        JTable reservationStationsTable = createTable(new String[]{"Name", "Busy", "Op", "Vj", "Vk", "Qj", "Qk", "Time"});
        JTable registerFileTable = createTable(new String[]{"Register", "Writing Unit", "Value"});

        
        
        // Add tables to tablesPanel
        tablesPanel.add(new JScrollPane(instructionsTable));
        tablesPanel.add(new JScrollPane(loadStoreBuffersTable));
        tablesPanel.add(new JScrollPane(reservationStationsTable));
        tablesPanel.add(new JScrollPane(registerFileTable));

        // Add tablesPanel to centerPanel
        centerPanel.add(tablesPanel);
        JButton nextCycleButton = new JButton("Next Cycle");
            nextCycleButton.addActionListener(e2 -> {
                tomasulo.advanceSimulation();
          
                updateTable(instructionsTable, tomasulo.getInstructionsData());
                updateTable(loadStoreBuffersTable, tomasulo.getLoadStoreBuffersData());
                updateTable(reservationStationsTable, tomasulo.getReservationStationsData());
                updateTable(registerFileTable, tomasulo.getRegisterFileData());
                
				currentCycleLabel.setText("Current Cycle: " + tomasulo.getCurrentCycle());

            });
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(startButton);
            buttonPanel.add(nextCycleButton);
            frame.add(buttonPanel, BorderLayout.SOUTH);
      

            } catch (NumberFormatException | IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
           
            
        });
        frame.add(startButton, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
       
        
    }
    
    private static JTable createTable(String[] columnNames) {
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        return new JTable(model);
    }
    private static void updateTable(JTable table, Object[][] data) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear existing data
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
    
    
   
    private static JTextField addInputField(JPanel panel, String label) {
        JLabel jLabel = new JLabel(label);
        JTextField textField = new JTextField();
        panel.add(jLabel);
        panel.add(textField);
        return textField;
    }
}
