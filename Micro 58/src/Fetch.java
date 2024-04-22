import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Fetch {
    private Tomasulo tomasulo;

    LoadStoreBuffer[] loadBuffers;
    LoadStoreBuffer[] storeBuffers;
    ReservationStation[] addSubReservationStations;
    ReservationStation[] multDivReservationStations;
    int currentCycle, totalLoadBuffers, totalStoreBuffers, totalAddSubReservationStations,
    totalMultDivReservationStations, totalRegisters, totalInstructions, loadStoreLatency, addSubLatency,
    mulLatency, divLatency, subiLatency;
    Register[] registersStatus;
    Instruction[] instructions;
    int lines=0;
    public Fetch(Tomasulo tomasulo, int totalLoadBuffers, int totalStoreBuffers, 
            int totalAddSubReservationStations, int totalMultDivReservationStations, 
            int totalRegisters, int loadStoreLatency, int addSubLatency, 
            int mulLatency, int divLatency, int subiLatency) {    	
   this.tomasulo = tomasulo;
   this.totalLoadBuffers = totalLoadBuffers;
   this.totalStoreBuffers = totalStoreBuffers;
   this.totalAddSubReservationStations = totalAddSubReservationStations;
   this.totalMultDivReservationStations = totalMultDivReservationStations;
   this.totalRegisters = totalRegisters;
   this.loadStoreLatency = loadStoreLatency;
   this.addSubLatency = addSubLatency;
   this.mulLatency = mulLatency;
   this.divLatency = divLatency;
   this.subiLatency = subiLatency;
   initBuffers();
   initReservationStations();
   initRegisterFile();
  
}
  
    // Define the countLines method
    private int countLines(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(fileName )));
        int numberOfLines = 0;
        while (br.readLine() != null) {
            numberOfLines++;
        }
        br.close();
        
        return numberOfLines;
    }
    private String[] readLines(String fileName) throws IOException {
        String currentLine;
        BufferedReader br = new BufferedReader(new FileReader(new File(fileName )));
        String[] arr = new String[countLines(fileName)];
        int i = 0;
        while ((currentLine = br.readLine()) != null) {
            arr[i] = currentLine;
            i++;
        }
        br.close();
        return arr;
    }
    
    public int getfinallength(){
    	return lines;
    }
    public void loadInstructions(String filename) throws IOException {
    	
        String[] temp = readLines(filename);

        initBuffers();

	    initReservationStations();
	
        initRegisterFile();

        parseInstructions(temp);
    }
    
    private void initBuffers() {
        loadBuffers = new LoadStoreBuffer[totalLoadBuffers];
        for (int i = 0; i < totalLoadBuffers; i++) {
            loadBuffers[i] = new LoadStoreBuffer();
            loadBuffers[i].name = "LOAD" + (i + 1);
        }

        storeBuffers = new LoadStoreBuffer[totalStoreBuffers];
        for (int i = 0; i < totalStoreBuffers; i++) {
            storeBuffers[i] = new LoadStoreBuffer();
            storeBuffers[i].name = "STORE" + (i + 1);
        }


    }
    private void initReservationStations() {
        addSubReservationStations = new ReservationStation[totalAddSubReservationStations];
        for (int i = 0; i < totalAddSubReservationStations; i++) {
            addSubReservationStations[i] = new ReservationStation();
            addSubReservationStations[i].name = "ADD" + (i + 1);
        }

        multDivReservationStations = new ReservationStation[totalMultDivReservationStations];
        for (int i = 0; i < totalMultDivReservationStations; i++) {
            multDivReservationStations[i] = new ReservationStation();
            multDivReservationStations[i].name = "MUL" + (i + 1);
        }
    }

    private void initRegisterFile() {
       this.registersStatus = new Register[this.totalRegisters];
        for (int i = 0; i < this.totalRegisters; i++) {
            this.registersStatus[i] = new Register();
            this.registersStatus[i].registerName = "F" + (i + 1);
        }
    }
        
    
    
    public int getlines() {
    	return lines;
    }
    private void parseInstructions(String[] instructionLines) {
        this.instructions = new Instruction[instructionLines.length];
lines=instructionLines.length;
        for (int i = 0; i < instructionLines.length; i++) {
            String[] parts = instructionLines[i].split(" ");
            Instruction instruction = new Instruction();
            instruction.instructionStatus = new InstructionStatus();

            switch (parts[0].trim().toUpperCase()) {
                case "ADD":
                case "ADD.D":
                case "DADD":
                    instruction.instructionType = "ADD";
                    instruction.Rd = parts[1];
                    instruction.Rs = parts[2];
                    instruction.Rt = parts[3];
                    break;

                case "SUB":
                case "SUB.D":
                    instruction.instructionType = "SUB";
                    instruction.Rd = parts[1];
                    instruction.Rs = parts[2];
                    instruction.Rt = parts[3];
                    break;

                case "MUL":
                case "MUL.D":
                    instruction.instructionType = "MUL";
                    instruction.Rd = parts[1];
                    instruction.Rs = parts[2];
                    instruction.Rt = parts[3];
                    break;

                case "DIV":
                case "DIV.D":
                    instruction.instructionType = "DIV";
                    instruction.Rd = parts[1];
                    instruction.Rs = parts[2];
                    instruction.Rt = parts[3];
                    break;

                case "LOAD":
                case "LOAD.D":
                case "L":
                case "L.D":
                    instruction.instructionType = "LOAD";
                    instruction.Rt = parts[1];
                    instruction.immediateOffset = Integer.parseInt(parts[2]);
                    instruction.Rs = "0"; // Assuming base register is always zero for simplicity
                    break;

                case "STORE":
                case "STORE.D":
                case "S":
                case "S.D":
                    instruction.instructionType = "STORE";
                    instruction.Rt = parts[1]; // Source register
                    instruction.Rs = parts[2]; // Destination register
                    instruction.immediateOffset = 0; // Assuming no offset for simplicity
                    break;

                case "ADDI":
                    instruction.instructionType = "ADDI";
                    instruction.Rd = parts[1];
                    instruction.Rs = parts[2];
                    instruction.immediateOffset = Integer.parseInt(parts[3]);
                    break;

                case "SUBI":
                    instruction.instructionType = "SUBI";
                    instruction.Rd = parts[1];	
                    instruction.Rs = parts[2];
                    instruction.immediateOffset = Integer.parseInt(parts[3]);
                    break;

                case "BNEZ":
                    instruction.instructionType = "BNEZ";
                    instruction.Rs = parts[1];
                    instruction.immediateOffset = 0; // Assuming a default value for simplicity
                    break;


                default:
                  
                    break;
            }
            
            
            this.totalInstructions = instructionLines.length;
            this.instructions[i] = instruction;
        }
    
    }}
   
    
    


