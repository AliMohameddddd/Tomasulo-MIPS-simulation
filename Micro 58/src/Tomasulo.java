import java.io.IOException;
	import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
	import java.util.List;
	import java.util.Scanner;
	
	public class Tomasulo {
	
	    Scanner sc;
	    String reason = "";
	
	    LoadStoreBuffer[] loadBuffers;
	    LoadStoreBuffer[] storeBuffers;
	    ReservationStation[] addSubReservationStations;
	    ReservationStation[] multDivReservationStations;
	    Register[] registersStatus;
	    Instruction[] instructions;
	    int currentInstructionNumberToBeIssued = 0;
	    int currentCycle, totalLoadBuffers, totalStoreBuffers, totalAddSubReservationStations,
	            totalMultDivReservationStations, totalRegisters, totalInstructions, loadStoreLatency, addSubLatency,
	            mulLatency, divLatency, subiLatency, bufferNo;;
	
	    int loadNum = 1;
	    int addSubNum = 1;
	    int mulDivNum = 1;
	    
	    int rsValue = 0;
		 int rtValue = 0;
		 int rdValue = 0;
	    int result;
	    int result1;
	    int result22;
	    int result3;
	    
	    boolean writeback = false;
	    
	    
	    private boolean addSubReservationStationsisFull() {
	    	for(int i =0 ; i< addSubReservationStations.length; i++) {
	    		if(!addSubReservationStations[i].isBusy) {
	    			return false;
	    		}
	    	}
	    	return true;
	    }
	    
	    private boolean multDivReservationStationsisFull() {
	    	for(int i =0 ; i< multDivReservationStations.length; i++) {
	    		if(!multDivReservationStations[i].isBusy) {
	    			return false;
	    		}
	    	}
	    	return true;
	    }
	    
	    
	
	    public Tomasulo() {
	
	    }
	   
	    public void initializeSimulation() {
	        currentInstructionNumberToBeIssued = 0;
	        currentCycle = 1;
	        System.out.println("############## SIMULATION STARTED ##############" + "\n");
	    }

	    public int getCurrentCycle(){
	    	
	    	
	    	return currentCycle-1;
	    }
	    
	    public void initialize(Fetch fetch) {
	        this.loadBuffers = fetch.loadBuffers;
	        System.out.println("Load Buffers: " + Arrays.toString(this.loadBuffers));

	        this.storeBuffers = fetch.storeBuffers;
	        System.out.println("Store Buffers: " + Arrays.toString(this.storeBuffers));

	        this.totalAddSubReservationStations = fetch.totalAddSubReservationStations;
	        System.out.println("Total Add/Sub Reservation Stations: " + this.totalAddSubReservationStations);

	        this.totalMultDivReservationStations = fetch.totalMultDivReservationStations;
	        System.out.println("Total Mult/Div Reservation Stations: " + this.totalMultDivReservationStations);

	        this.registersStatus = fetch.registersStatus;
	        System.out.println("Registers Status: " + Arrays.toString(this.registersStatus));

	        this.totalRegisters = fetch.totalRegisters;
	        System.out.println("Total Registers: " + this.totalRegisters);

	        this.instructions = fetch.instructions;
	        System.out.println("Instructions: " + Arrays.toString(this.instructions));

	        this.totalLoadBuffers = fetch.totalLoadBuffers;
	        System.out.println("Total Load Buffers: " + this.totalLoadBuffers);

	        this.totalInstructions = fetch.totalInstructions;
	        System.out.println("Total Instructions: " + this.totalInstructions);

	        this.totalStoreBuffers = fetch.totalStoreBuffers;
	        System.out.println("Total Store Buffers: " + this.totalStoreBuffers);

	        this.addSubReservationStations = fetch.addSubReservationStations;
	        System.out.println("Add/Sub Reservation Stations: " + Arrays.toString(this.addSubReservationStations));

	        this.multDivReservationStations = fetch.multDivReservationStations;
	        System.out.println("Mult/Div Reservation Stations: " + Arrays.toString(this.multDivReservationStations));

	        this.addSubLatency = fetch.addSubLatency;
	        System.out.println("Add/Sub Latency: " + this.addSubLatency);
	        

	        this.mulLatency = fetch.mulLatency;
	        System.out.println("Mul Latency: " + this.mulLatency);

	        this.divLatency = fetch.divLatency;
	        System.out.println("Div Latency: " + this.divLatency);

	        this.loadStoreLatency = fetch.loadStoreLatency;
	        System.out.println("Load/Store Latency: " + this.loadStoreLatency);

	        this.subiLatency = fetch.subiLatency;
	        System.out.println("SUBI Latency: " + this.subiLatency);
	    }

		 public void advanceSimulation() {
			 if (currentInstructionNumberToBeIssued < totalInstructions) {
		            int flag = issue(currentInstructionNumberToBeIssued);
		            if (flag != -1 ) {
		                currentInstructionNumberToBeIssued++;
		            }
		        }
			 if((loadBuffers.length !=0)) {
					writeBackLoadBuffers(result);

			 }
			
			if(!checkrawhazards()  ) {
				
				for(int i =0; i< instructions.length;i++) {
					
					switch (instructions[i].instructionType) {
			        case "ADD":
			        case "SUB":
			        case "ADDI":
			        case "SUBI":
						  writeBackAddSubReservationStations(result22);
				           break;


			        case "MUL":
			        case "DIV": 
			        	
						  writeBackMultDivReservationStations(result3);
				           break;

			        	
			        case "LOAD":
						writeBackLoadBuffers(result);
				           break;


			        case "STORE":
			       	 
						  writeBackStoreBuffers(result1);


				           break;

			        case "BNEZ":
			           break;


			        default:
			          
			            break;
			   	 }
			    	
					
				}
				
				 execute();
				 for(int i =0; i< instructions.length;i++) {
						
						switch (instructions[i].instructionType) {
				        case "ADD":
				        case "SUB":
				        case "ADDI":
				        case "SUBI":
							  writeBackAddSubReservationStations(result22);
					           break;

				        case "MUL":
				        case "DIV": 
				        	
							  writeBackMultDivReservationStations(result3);
					           break;

				        case "LOAD":
							writeBackLoadBuffers(result);
					           break;

				        case "STORE":
				       	 
							  writeBackStoreBuffers(result1);
					           break;

				        case "BNEZ":
				           break;


				        default:
				          
				            break;
				   	 }
				    	
						
					}
				
			}else {
				
				 execute();
				 for(int i =0; i< instructions.length;i++) {
						
						switch (instructions[i].instructionType) {
				        case "ADD":
				        case "SUB":
				        case "ADDI":
				        case "SUBI":
							  writeBackAddSubReservationStations(result22);
					           break;

				        case "MUL":
				        case "DIV": 
				        	
							  writeBackMultDivReservationStations(result3);
					           break;

				        case "LOAD":
							writeBackLoadBuffers(result);
					           break;

							case "STORE":
				       	 
							  writeBackStoreBuffers(result1);
					           break;

				        case "BNEZ":
				           break;


				        default:
				          
				            break;
				   	 }
				    	
						
					}
				
			}
			 
					

				currentCycle++;		

		        
		        
		        writeback = false;
		       
			       
		    }
		 
		 private boolean checkrawhazards() {
			 
			 for(int i = 0; i<storeBuffers.length;i++) {
				 
				 if(storeBuffers[i].instruction !=null) {
					 if( problemStoreWAR(storeBuffers[i].instruction)) {
						 return true;
					 }
				 }
				 
			 }
			 
			 for(int i = 0; i<addSubReservationStations.length;i++) {
				 
				 if(addSubReservationStations[i].instruction !=null) {
					 if( problemAddSub(addSubReservationStations[i].instruction) || problemAddiSubi(addSubReservationStations[i].instruction)) {
						 return true;
					 }
				 }
				 
			 }
			 
			 	for(int i = 0; i<multDivReservationStations.length;i++) {
				 
				 if(multDivReservationStations[i].instruction !=null) {
					 if( problemMulDiv(multDivReservationStations[i].instruction) ) {
						 return true;
					 }
				 }
				 
				 
				 
			 }
			 return false;
			 
		 }
	    
	    //ISSUING
	    public int issue(int instructionToBeIssued) {
	        if (instructionToBeIssued >= totalInstructions) {
	            return 0;
	        }

	        if (instructions[instructionToBeIssued] == null) {
	            return 0;
	        }

	        String instructionType = instructions[instructionToBeIssued].instructionType;

	        switch (instructionType.toUpperCase()) {
	            case "BNEZ":
	                return handleBNEZ(instructionToBeIssued);
	            case "LOAD":
	                return handleLoad(instructionToBeIssued);
	            case "STORE":
	                return handleStore(instructionToBeIssued);
	            case "ADD":
	            case "SUB":
	                return handleAddSub(instructionToBeIssued);
	            case "MUL":
	            case "DIV":
	                return handleMulDiv(instructionToBeIssued);
	            case "ADDI":
	            case "SUBI":
	                return handleAddiSubi(instructionToBeIssued, instructionType);
	            default:
	               
	                reason += "-> Unknown instruction type: " + instructionType + "\n";
	                return -1;
	        }
	    }
	    
	    
	    private int handleBNEZ(int instructionToBeIssued) {
	        // Check if the instruction is a BNEZ
	        if (instructions[instructionToBeIssued].instructionType.equalsIgnoreCase("BNEZ")) {
	            String sourceRegister = instructions[instructionToBeIssued].Rs;

	            // Remove any non-numeric characters (e.g., commas) before parsing
	            try {
	                
	                String rs =sourceRegister.substring(1);
	            	rs = rs.replaceAll("[^0-9]", ""); // Remove non-numeric characters
	            	
	            	int regNumber = Integer.parseInt(rs);
	            	if(!registersStatus[regNumber-1].writingUnit.isEmpty()) {
	                	return -1;
	                }
	                // Check if the source register value is not zero (assuming branch condition)
	                if (regNumber - 1 != -1) {
	                    if (registersStatus[regNumber - 1].value != 0) {
	                        // Branch taken, update the program counter
	                        int branchTarget = instructions[instructionToBeIssued].immediateOffset;
	                        currentInstructionNumberToBeIssued = branchTarget;
	                        
	                        for(int i =branchTarget; i< instructions.length; i++)
	                        {
	                        	instructions[i].instructionStatus.executionStart = -1;
	                        	


	                        }
	                      
	                        reason += "-> The instruction number: " + instructionToBeIssued +
	                                " is a BNEZ instruction and the branch was taken.\n";
	                    } else {
	    	                instructions[instructionToBeIssued].instructionStatus.issue = (short) currentCycle;

	                        reason += "-> The instruction number: " + instructionToBeIssued +
	                                " is a BNEZ instruction, and the branch was not taken.\n";
	                    	return 0;

	                    }
	                }
	               
	                // Update instruction status
	                instructions[instructionToBeIssued].instructionStatus.issue = (short) currentCycle;
	                return -1; // Successful issue of the instruction
	            } catch (NumberFormatException e) {
	                // Handle the exception (e.g., log an error) if parsing fails
	                return -1;
	            }
	        } else {
	            return -1; // Not a BNEZ instruction, should not reach here
	        }
	    }


	 private int handleLoad(int instructionToBeIssued) {
    int bufferNo = findFreeLoadBuffer();

    String rt = instructions[instructionToBeIssued].Rt.replaceAll("[^0-9]", "");
    int regNumber = Integer.parseInt(rt);
    
    if (bufferNo == -1) {
        reason += "-> Load instruction " + instructionToBeIssued + " not issued: No free Load Buffer.\n";
        return -1;
    }
    
    if(!registersStatus[regNumber-1].writingUnit.isEmpty()) {
    	loadBuffers[bufferNo].waw = true;
   }

    
    

    loadBuffers[bufferNo].isBusy = true;
    loadBuffers[bufferNo].instruction = instructions[instructionToBeIssued];
    loadBuffers[bufferNo].address = instructions[instructionToBeIssued].immediateOffset + "";
    instructions[instructionToBeIssued].instructionStatus.issue = (short) currentCycle;
    instructions[instructionToBeIssued].instructionStatus.executionCyclesRemaining = (short) loadStoreLatency;

    int regNum = Integer.parseInt(instructions[instructionToBeIssued].Rt.replaceAll("[^0-9]", ""));
    if (regNum - 1 != -1) {
        registersStatus[regNum-1].writingUnit = loadBuffers[bufferNo].name;

    }

    return 0;
}
     private int handleStore(int instructionToBeIssued) {
    int bufferNo = findFreeStoreBuffer();
    
    String rs = instructions[instructionToBeIssued].Rs.replaceAll("[^0-9]", "");
    int regNumber = Integer.parseInt(rs);

    if (bufferNo == -1) {
        reason += "-> Store instruction " + instructionToBeIssued + " not issued: No free Store Buffer.\n";
        return -1;
    }
    
    if(!registersStatus[regNumber-1].writingUnit.isEmpty()) {
    	storeBuffers[bufferNo].waw = true;
   }

    storeBuffers[bufferNo].isBusy = true;
    storeBuffers[bufferNo].instruction = instructions[instructionToBeIssued];
    storeBuffers[bufferNo].address = instructions[instructionToBeIssued].immediateOffset + "";
    instructions[instructionToBeIssued].instructionStatus.issue = (short) currentCycle;
    instructions[instructionToBeIssued].instructionStatus.executionCyclesRemaining = (short) loadStoreLatency;

    int regNum = Integer.parseInt(storeBuffers[bufferNo].instruction.Rs.replaceAll("[^0-9]", ""));
    if (regNum - 1 != -1) {
    
    registersStatus[regNum-1].writingUnit = storeBuffers[bufferNo].name;
    }
    return 0;
}
	private int handleAddSub(int instructionToBeIssued) {	
		   
        int bufferNo = findFreeAddSubReservationStation();
        String rd = instructions[instructionToBeIssued].Rd.replaceAll("[^0-9]", "");
        int regNumber = Integer.parseInt(rd);

        if (bufferNo == -1) {
            reason += "-> The instruction Number: " + instructionToBeIssued
                    + " has not been issued due to the structural hazard of non-empty buffers.\n";
            return -1;
        }else if( addSubReservationStationsisFull()) {
        	return -1;
        }
        else {
            reason += "-> The instruction No: " + instructionToBeIssued
                    + " has been issued at Reservation Station= " + addSubReservationStations[bufferNo].name + "\n";

            addSubReservationStations[bufferNo].isBusy = true;
            addSubReservationStations[bufferNo].instruction = instructions[instructionToBeIssued];
            addSubReservationStations[bufferNo].instructionType = instructions[instructionToBeIssued].instructionType;

            instructions[instructionToBeIssued].instructionStatus.issue = (short) currentCycle;
            instructions[instructionToBeIssued].instructionStatus.executionCyclesRemaining = (short) addSubLatency;

            // RAW hazard check for Rs and Rt
            
        
			if(!registersStatus[regNumber-1].writingUnit.isEmpty()) {
				 addSubReservationStations[bufferNo].waw = true;
	        }
            
            String rs = instructions[instructionToBeIssued].Rs.replaceAll("[^0-9]", "");
            int regNumber1 = Integer.parseInt(rs);
        
            if (regNumber1 - 1 != -1) {

            	addSubReservationStations[bufferNo].Qj = registersStatus[regNumber1-1].writingUnit;
            }
           
            String rt = instructions[instructionToBeIssued].Rt.replaceAll("[^0-9]", "");
            regNumber = Integer.parseInt(rt);
            if (regNumber - 1 != -1) {

            	addSubReservationStations[bufferNo].Qk = registersStatus[regNumber-1].writingUnit;
            }
            if (addSubReservationStations[bufferNo].Qj.equals("")) {
            	addSubReservationStations[bufferNo].Vj = "R(" + rs + ")";
            }

            if (addSubReservationStations[bufferNo].Qk.equals("")) {
            	addSubReservationStations[bufferNo].Vk = "R(" + rt + ")";
            }

           
            
            // Set register status for destination
            String rd1 = instructions[instructionToBeIssued].Rd.replaceAll("[^0-9]", "");
            regNumber = Integer.parseInt(rd1);
            if (regNumber - 1 != -1) {

            registersStatus[regNumber-1].writingUnit = addSubReservationStations[bufferNo].name;
            }
            return 0; // Successful issue of the instruction
        }
}
    private int handleMulDiv(int instructionToBeIssued) {
   
        int bufferNo = findFreeMulDivReservationStation();
        
        String rd = instructions[instructionToBeIssued].Rd.replaceAll("[^0-9]", "");
        int regNumber = Integer.parseInt(rd);

        if (bufferNo == -1) {
            reason += "-> The instruction Number: " + instructionToBeIssued
                    + " has not been issued due to the structural hazard of non-empty buffers.\n";
            return -1;
        }else if( multDivReservationStationsisFull()) {
        	return -1;
        }
        else {
            reason += "-> The instruction No: " + instructionToBeIssued
                    + " has been issued at Reservation Station= " + multDivReservationStations[bufferNo].name + "\n";

            multDivReservationStations[bufferNo].isBusy = true;
            multDivReservationStations[bufferNo].instruction = instructions[instructionToBeIssued];
            multDivReservationStations[bufferNo].instructionType = instructions[instructionToBeIssued].instructionType;

            instructions[instructionToBeIssued].instructionStatus.issue = (short) currentCycle;
            instructions[instructionToBeIssued].instructionStatus.executionCyclesRemaining = 
                instructions[instructionToBeIssued].instructionType.equalsIgnoreCase("MUL") ? (short) mulLatency : (short) divLatency;

            
            if(!registersStatus[regNumber-1].writingUnit.isEmpty()) {
            	multDivReservationStations[bufferNo].waw = true;
	        }
            // RAW hazard check for Rs and Rt
            
            String rs = instructions[instructionToBeIssued].Rs.replaceAll("[^0-9]", "");
            int regNumber1 = Integer.parseInt(rs);
           
            if (regNumber1 - 1 != -1) {

            multDivReservationStations[bufferNo].Qj = registersStatus[regNumber1-1].writingUnit;
            }
            String rt = instructions[instructionToBeIssued].Rt.replaceAll("[^0-9]", "");
            int regNumber2 = Integer.parseInt(rt);
            if (regNumber2 - 1 != -1) {

            multDivReservationStations[bufferNo].Qk = registersStatus[regNumber2-1].writingUnit;
            }
            if (multDivReservationStations[bufferNo].Qj.equals("")) {
                multDivReservationStations[bufferNo].Vj = "R(" + rs + ")";
            }

            if (multDivReservationStations[bufferNo].Qk.equals("")) {
                multDivReservationStations[bufferNo].Vk = "R(" + rt + ")";
            }

            
            
            // Set register status for destination
            String rd1 = instructions[instructionToBeIssued].Rd.replaceAll("[^0-9]", "");
            regNumber = Integer.parseInt(rd1);
            if (regNumber - 1 != -1) {

            registersStatus[regNumber-1].writingUnit = multDivReservationStations[bufferNo].name;
            }
            return 0; // Successful issue of the instruction
        }
    } 

    private int handleAddiSubi(int instructionToBeIssued, String instructionType) {
        if (instructionType.equalsIgnoreCase("ADDI") || instructionType.equalsIgnoreCase("SUBI")) {
        	
            int bufferNo = findFreeAddSubReservationStation();
            
            String rd = instructions[instructionToBeIssued].Rd.replaceAll("[^0-9]", "");
            int regNumber = Integer.parseInt(rd);
            
            if (bufferNo == -1) {
                reason += "-> The instruction Number: " + instructionToBeIssued
                        + " has not been issued due to the structural hazard of non-empty buffers.\n";
                return -1; // Indicating failure to issue due to no free buffer
            }else if( addSubReservationStationsisFull()) {
            	return -1;
            }
            else {
                reason += "-> The instruction No: " + instructionToBeIssued
                        + " has been issued at Reservation Station= " + addSubReservationStations[bufferNo].name + "\n";

                addSubReservationStations[bufferNo].isBusy = true;
                addSubReservationStations[bufferNo].instruction = instructions[instructionToBeIssued];
                addSubReservationStations[bufferNo].instructionType = instructionType;

                instructions[instructionToBeIssued].instructionStatus.issue = (short) currentCycle;
                instructions[instructionToBeIssued].instructionStatus.executionCyclesRemaining = (short) (instructionType.equalsIgnoreCase("ADDI") ? 1 : subiLatency);

                
                if(!registersStatus[regNumber-1].writingUnit.isEmpty()) {
                	addSubReservationStations[bufferNo].waw = true;
    	        }
                String rs = instructions[instructionToBeIssued].Rs.replaceAll("[^0-9]", "");
                int regNumber1 = Integer.parseInt(rs);
                // RAW hazard check for Rs
                
                if (regNumber1 - 1 != -1) {

                addSubReservationStations[bufferNo].Qj = registersStatus[regNumber-1].writingUnit;
                }
                // Immediate value does not create a hazard, so Vk and Qk are not used
                addSubReservationStations[bufferNo].Vk = "Imm(" + instructions[instructionToBeIssued].immediateOffset + ")";
                addSubReservationStations[bufferNo].Qk = "";

                if (addSubReservationStations[bufferNo].Qj.equals("")) {
                    addSubReservationStations[bufferNo].Vj = "R(" + rs + ")";
                }
               
                
                if (regNumber - 1 != -1) {

                registersStatus[regNumber-1].writingUnit = addSubReservationStations[bufferNo].name;
                }
                return 0; // Successful issue of the instruction
            }
        } else {
            return -1; // Not an ADDI or SUBI instruction, should not reach here
        }
    }

	    


	    
	    
	    
	    
	     
	    
	    
public void execute() {
    executeLoadBuffers();
    executeStoreBuffers();
    executeAddSubReservationStations();
    executeMultDivReservationStations();
}
private void executeLoadBuffers() {
    for (int i = 0; i < totalLoadBuffers; i++) {
        if (!loadBuffers[i].isBusy || !loadBuffers[i].fu.isEmpty())
            continue;

        if(this.loadBuffers[i].waw) {
			continue;
		}
        
        if (loadBuffers[i].instruction.instructionStatus.executionStart == -1) {
            if (loadBuffers[i].instruction.instructionStatus.issue == currentCycle)
                continue;

            loadBuffers[i].instruction.instructionStatus.executionStart = (short) currentCycle;
            loadBuffers[i].instruction.instructionStatus.executionCyclesRemaining--;

            if (loadBuffers[i].instruction.instructionStatus.executionCyclesRemaining == 0) {
                loadBuffers[i].instruction.instructionStatus.executionComplete = (short) currentCycle;
                reason += "-> The instruction at Load Buffer= " + loadBuffers[i].name + " has completed execution.\n";
                
                result=0;
                result = loadBuffers[i].instruction.immediateOffset;
            } else {
                reason += "-> The instruction at Load Buffer= " + loadBuffers[i].name + " has started execution.\n";
            }
        } else {
            if (loadBuffers[i].instruction.instructionStatus.executionCyclesRemaining > 0) {
                loadBuffers[i].instruction.instructionStatus.executionCyclesRemaining--;
                if (loadBuffers[i].instruction.instructionStatus.executionCyclesRemaining == 0) {
                    loadBuffers[i].instruction.instructionStatus.executionComplete = (short) currentCycle;
                    result=0;
                    result = loadBuffers[i].instruction.immediateOffset;

                    reason += "-> The instruction at Load Buffer= " + loadBuffers[i].name + " has completed execution.\n";
                }
            }
        }
    }
}
private void executeStoreBuffers() {	
	for (int i = 0; i < totalStoreBuffers; i++) {
		if (this.storeBuffers[i].isBusy == false)
			continue;

		if (this.storeBuffers[i].fu != "")
			continue; // execution not started due to RAW hazard
		if(this.storeBuffers[i].waw) {
			continue;
		}
		if(!this.storeBuffers[i].raw) {
			if(problemStoreWAR(this.storeBuffers[i].instruction)) {
				continue;
			}

		}
		
		
		if (this.storeBuffers[i].instruction.instructionStatus.executionStart == -1) {
			if (this.storeBuffers[i].instruction.instructionStatus.issue == currentCycle)
				continue; // the instruction has been issued in the current cycle, so it cannot start
							// execution in the current cycle

			// execution started
			this.storeBuffers[i].instruction.instructionStatus.executionStart = (short) currentCycle;
			this.storeBuffers[i].instruction.instructionStatus.executionCyclesRemaining--;

			if (this.storeBuffers[i].instruction.instructionStatus.executionCyclesRemaining == 0) { // execution
																									// completed
				this.storeBuffers[i].instruction.instructionStatus.executionComplete = (short) currentCycle;
				reason += "-> The instruction at Store Buffer= " + this.storeBuffers[i].name
						+ " has completed execution.\n";
				
	
				String rs3 = this.storeBuffers[i].instruction.Rt.substring(1);
				rs3 = rs3.replaceAll("[^0-9]", ""); // Remove non-numeric characters
				int regNum1 = Integer.parseInt(rs3);
				if(regNum1!= -1) {
					result1=0;

					result1 = registersStatus[regNum1-1].value;

				}
				continue;
			} else {
				reason += "-> The instruction at Store Buffer= " + this.storeBuffers[i].name
						+ " has started execution.\n";
				continue;
			}
		}

		if (this.storeBuffers[i].instruction.instructionStatus.executionCyclesRemaining != 0)
			this.storeBuffers[i].instruction.instructionStatus.executionCyclesRemaining--;
		else
			continue;

		if (this.storeBuffers[i].instruction.instructionStatus.executionCyclesRemaining == 0) { // execution
																								// completed
			this.storeBuffers[i].instruction.instructionStatus.executionComplete = (short) currentCycle;
			reason += "-> The instruction at Store Buffer= " + this.storeBuffers[i].name
					+ " has completed execution.\n";
			

			String rs3 = this.storeBuffers[i].instruction.Rt.substring(1);
			rs3 = rs3.replaceAll("[^0-9]", ""); // Remove non-numeric characters
			int regNum1 = Integer.parseInt(rs3);
			if(regNum1!= -1) {
				result1=0;
				result1 = registersStatus[regNum1-1].value;

			}
			continue;
		} else {
			reason += "-> the instruction at Store Buffer= " + this.storeBuffers[i].name
					+ " has completed one more execution cycle.\n";
			continue;
		}

	} // end of store buffers loop
 // end of load buffers loop
}
private void executeAddSubReservationStations() {
	

	for (int i = 0; i < totalAddSubReservationStations; i++) {
		if (this.addSubReservationStations[i].isBusy == false)
			continue;

	
		
		
		if (this.addSubReservationStations[i].Qj != "" || this.addSubReservationStations[i].Qk != "")
			continue; 	// execution not started due to RAW hazard
		if (this.addSubReservationStations[i].instruction.instructionStatus.issue == currentCycle)
			continue;
		if(this.addSubReservationStations[i].waw) {
			continue;
		}
		if(!this.addSubReservationStations[i].raw) {
			if(problemAddSub(this.addSubReservationStations[i].instruction) || problemAddiSubi(this.addSubReservationStations[i].instruction)) {
				continue;
			}
		}else {
			if(	this.addSubReservationStations[i].moe !=0 && this.addSubReservationStations[i].load) {
				this.addSubReservationStations[i].moe--;
				continue;
				
			}

		}
		
		
		if (this.addSubReservationStations[i].instruction.instructionStatus.executionStart == -1) {
			 // the instruction has been issued in the current cycle, so it cannot start
							// execution in the current cycle
			
			this.addSubReservationStations[i].instruction.instructionStatus.executionStart = (short) currentCycle;
			 String instType = addSubReservationStations[i].instruction.instructionType.toUpperCase();
	            if ("SUB".equals(instType) || "SUBI".equals(instType)) {
	                addSubReservationStations[i].instruction.instructionStatus.executionCyclesRemaining -= 1;
	            } else if ("ADD".equals(instType) || "ADDI".equals(instType)) {
	                addSubReservationStations[i].instruction.instructionStatus.executionCyclesRemaining -= 1;
	            }
			
			if (this.addSubReservationStations[i].instruction.instructionStatus.executionCyclesRemaining == 0) { // execution
					this.addSubReservationStations[i].instruction.instructionStatus.executionComplete = (short) (currentCycle);

			
				reason += "-> The instruction at Reservation Station= " + this.addSubReservationStations[i].name
						+ " has completed execution.\n";
				result22=0;
				result22 = executeOperation(this.addSubReservationStations[i].instruction);
				continue;
			} else {
				reason += "-> The instruction at Reservation Station= " + this.addSubReservationStations[i].name
						+ " has started execution.\n";
				continue;
			}
		}

		if (this.addSubReservationStations[i].instruction.instructionStatus.executionCyclesRemaining != 0)
			this.addSubReservationStations[i].instruction.instructionStatus.executionCyclesRemaining--;
		else
			continue;

		if (this.addSubReservationStations[i].instruction.instructionStatus.executionCyclesRemaining == 0) {
			
				this.addSubReservationStations[i].instruction.instructionStatus.executionComplete = (short) (currentCycle);

			
			reason += "-> The instruction at Reservation Station= " + this.addSubReservationStations[i].name
					+ " has completed execution.\n";
			result22=0;
			result22 = executeOperation(this.addSubReservationStations[i].instruction);

			continue;
		} else {
			reason += "-> the instruction at Reservation Station= " + this.addSubReservationStations[i].name
					+ " has completed one more execution cycle.\n";
			continue;
		}
	}

	}
private void executeMultDivReservationStations() {

	for (int i = 0; i < totalMultDivReservationStations; i++) {

		if (this.multDivReservationStations[i].isBusy == false)
			continue;

		if (this.multDivReservationStations[i].Qj != "" || this.multDivReservationStations[i].Qk != "")
			continue; // execution not started due to RAW hazard
		
		if(this.multDivReservationStations[i].waw) {
			continue;
		}
		if(!this.multDivReservationStations[i].raw) {
			if(problemMulDiv(this.multDivReservationStations[i].instruction)) {
				continue;
			}

		}else {
			if(	this.multDivReservationStations[i].moe !=0 && this.multDivReservationStations[i].load) {
				this.multDivReservationStations[i].moe--;
				continue;
				
			}

		}
	
				

		if (this.multDivReservationStations[i].instruction.instructionStatus.executionStart == -1) {
			if (this.multDivReservationStations[i].instruction.instructionStatus.issue == currentCycle)
				continue; // the instruction has been issued in the current cycle, so it cannot start
							// execution in the current cycle

			// execution started
			
				this.multDivReservationStations[i].instruction.instructionStatus.executionStart = (short) currentCycle;

			
			this.multDivReservationStations[i].instruction.instructionStatus.executionCyclesRemaining--;
			System.out.print(this.multDivReservationStations[i].instruction.instructionStatus.executionCyclesRemaining);
			if (this.multDivReservationStations[i].instruction.instructionStatus.executionCyclesRemaining == 0) { // execution
				// execution started
			
					this.multDivReservationStations[i].instruction.instructionStatus.executionComplete = (short) currentCycle;

																										// completed
				reason += "-> The instruction at Reservation Station= " + this.multDivReservationStations[i].name
						+ " has completed execution.\n";
				result3 =0;
				result3 = executeOperation(this.multDivReservationStations[i].instruction);

				continue;
			} else {
				reason += "-> The instruction at Reservation Station= " + this.multDivReservationStations[i].name
						+ " has started execution.\n";
				continue;
			}
		}
		


		if (this.multDivReservationStations[i].instruction.instructionStatus.executionCyclesRemaining != 0) {
			this.multDivReservationStations[i].instruction.instructionStatus.executionCyclesRemaining--;
	}
		else
			continue;

		if (this.multDivReservationStations[i].instruction.instructionStatus.executionCyclesRemaining == 0) { // execution
		
				this.multDivReservationStations[i].instruction.instructionStatus.executionComplete = (short) currentCycle;
																				// completed
			reason += "-> The instruction at Reservation Station= " + this.multDivReservationStations[i].name
					+ " has completed execution.\n";
			result3 =0;

			result3 = executeOperation(this.multDivReservationStations[i].instruction);


			continue;
		} else {
			reason += "-> the instruction at Reservation Station= " + this.multDivReservationStations[i].name
					+ " has completed one more execution cycle.\n";
			continue;
		}

	} 

}
private int executeOperation(Instruction instruction) {
	String rs =instruction.Rs.substring(1);
	rs = rs.replaceAll("[^0-9]", ""); // Remove non-numeric characters
	int regNum = Integer.parseInt(rs);
   
		rsValue= this.registersStatus[regNum-1].value;
	
	
	String rd =instruction.Rd.substring(1);
	rd = rd.replaceAll("[^0-9]", ""); // Remove non-numeric characters
	int regNumDest = Integer.parseInt(rd);
   
		rdValue= this.registersStatus[regNumDest-1].value;
    
    
    
		
		if (instruction.Rt != null && !instruction.Rt.isEmpty()) {
		    String rt1Value = instruction.Rt.substring(1);
		    rt1Value = rt1Value.replaceAll("[^0-9]", ""); // Remove non-numeric characters

		    // Additional check to ensure string is not empty after replacement
		    if (!rt1Value.isEmpty()) {
		        int reg1Num = Integer.parseInt(rt1Value);
		        if (reg1Num - 1 >= 0) {
		            rtValue = this.registersStatus[reg1Num-1].value;
		        }
		    }
		}

   
   // rtValue = instruction.get;
    switch (instruction.instructionType) {
	    case "ADD":
	    case "ADD.D":
	    case "DADD":
	        return rsValue + rtValue;
        case "SUB":
        case "SUB.D":
            return rsValue - rtValue;
        case "ADDI":
            return rsValue + instruction.immediateOffset;
        case "SUBI":
            return rsValue - instruction.immediateOffset;
        case "MUL":
        case "MUL.D":
            return rsValue * rtValue;
        case "DIV":
        case "DIV.D":
            return rsValue / rtValue;
         
        default:
            throw new IllegalArgumentException("Unknown operation type");
    }
}

public void writeBack(int result) {
    writeBackLoadBuffers(result);
    writeBackStoreBuffers(result);
    writeBackAddSubReservationStations(result);
    writeBackMultDivReservationStations(result);
}






private void writeBackLoadBuffers(int result2) {
	for (int i = 0; i < totalLoadBuffers; i++) {

		if (this.loadBuffers[i].isBusy == false)
			continue;

		if (this.loadBuffers[i].instruction.instructionStatus.executionCyclesRemaining != 0)
			continue; // the instruction is still executing

		if (this.loadBuffers[i].instruction.instructionStatus.executionComplete == currentCycle)
			continue; // the instruction has completed execution in the current cycle, so it cannot be

		
	

        if(writeback) {
        	continue;
        }
        
        writeback = true;
		reason += "-> The instruction at Load Buffer= " + this.loadBuffers[i].name + " has written back.\n";

		 Instruction currentInstruction = this.loadBuffers[i].instruction;
	        int index = instructions.length - 1; // Start from the last instruction
	        String rt = currentInstruction.Rt.replaceAll("[^0-9]", "");
	        int regNumber = Integer.parseInt(rt);
	        
	        
	        if(regNumber !=-1) {
	        	 if (this.registersStatus[regNumber-1].writingUnit.equals(this.loadBuffers[i].name)) {
	                 this.registersStatus[regNumber-1].value = result2;///LOOK HERE  YA AHMED
	                 this.registersStatus[regNumber-1].writingUnit = ""; // Clear the writing unit 
	             } else {
	                     this.registersStatus[regNumber-1].value = result2;///LOOK HERE  YA AHMED
	                 }
	        }

	        while (index >= 0 && instructions[index] != null && instructions[index] != currentInstruction) {
	            Instruction inst = instructions[index];
	            switch (inst.instructionType) {
	            case "ADD":
	            case "SUB":
	            case "MUL":
	            case "DIV":
	            	 String rd2 = inst.Rd.replaceAll("[^0-9]", "");
	                 int regNumber2 = Integer.parseInt(rd2);
	                 
	                 String rs20 = inst.Rs.replaceAll("[^0-9]", "");
	                 int regNumber20 = Integer.parseInt(rs20);
	                 
	                 String rt20 = inst.Rt.replaceAll("[^0-9]", "");
	                 int regNumber21 = Integer.parseInt(rt20);
	                 
	                 if (regNumber2 == regNumber) {
	                     int index2 = containsAddSub(inst);
	                     int index3 = containsMulDiv(inst);
	                     if (index2 != -1) {
	                         this.addSubReservationStations[index2].waw = false;
	                     }
	                     if (index3 != -1) {
	                         this.multDivReservationStations[index3].waw = false;
	                     }
	                 }
	                 
	                 if (regNumber20 == regNumber || regNumber21 == regNumber ) {
	                     int index77 = containsAddSub(inst);
	                     int index80 = containsMulDiv(inst);

	                     if (index77 != -1) {
	                         this.addSubReservationStations[index77].raw = true;
	                         this.addSubReservationStations[index77].load = true;

	                     }
	                     
	                     if (index80 != -1) {
	                         this.multDivReservationStations[index80].raw = true;
	                         this.multDivReservationStations[index80].load = true;

	                     }
	                     
	                 }
	                 
	                 break;

	                 
	               
	            case "ADDI":
	            case "SUBI":
	            	 String rd4 = inst.Rd.replaceAll("[^0-9]", "");
	                 int regNumber10 = Integer.parseInt(rd4);
	                 
	                 String rs7 = inst.Rs.replaceAll("[^0-9]", "");
	                 int regNumber89 = Integer.parseInt(rs7);
	                 
	                 if (regNumber10 == regNumber) {
	                     int index2 = containsAddSub(inst);
	                     if (index2 != -1) {
	                         this.addSubReservationStations[index2].waw = false;
	                     }
	                    
	                 }
	                 
	                 if (regNumber89 == regNumber) {
	                     int index60 = containsAddSub(inst);
	                     if (index60 != -1) {
	                         this.addSubReservationStations[index60].raw = true;
	                         this.addSubReservationStations[index60].load = true;

	                     }
	                     
	                 }
	                 break;


	            case "LOAD":
	           	    
	                String rt44 = inst.Rt.replaceAll("[^0-9]", "");
	                int regNumber4 = Integer.parseInt(rt44);
	                
	                if (regNumber4 == regNumber) {
	               	 	int index4 = containsLoad(inst);

	                    if (index4 != -1) {
	                        this.loadBuffers[index4].waw = false;
	                    }
	               	
	               }
	               
	                break;


	            case "STORE":
	           	           	  
	                String rs32 = inst.Rs.replaceAll("[^0-9]", "");
	                int regNumber3 = Integer.parseInt(rs32);
	                
	                String rt2 = inst.Rt.replaceAll("[^0-9]", "");
	                int regNumber8= Integer.parseInt(rt2);
	                if (regNumber3 == regNumber) {
	                     int index5 = containsStore(inst);

	                     if (index5 != -1) {
	                         this.storeBuffers[index5].waw = false;
	                     }
	                	
	                }
	                
	                if (regNumber8 == regNumber) {
	                    int index8 = containsStore(inst);

	                    if (index8 != -1) {
	                        this.storeBuffers[index8].raw = true;
	                        this.storeBuffers[index8].load = true;
	                    }
	               	
	               }
	                
	                break;


	            case "BNEZ":
	               break;


	            default:
	              
	                break;
	       	 }
	            index--; // Move to the previous instruction
	        }
		
		
		this.loadBuffers[i].isBusy = false;
		this.loadBuffers[i].instruction.instructionStatus.writeBack = (short) currentCycle;
		this.loadBuffers[i].address = "";
		this.loadBuffers[i].instruction = null;

		String val = "M(A" + loadNum + ")";
		publishResult(val, this.loadBuffers[i].name);

		loadNum++;

	}

}

private void writeBackStoreBuffers(int result2) {

	for (int i = 0; i < totalStoreBuffers; i++) {

		if (this.storeBuffers[i].isBusy == false)
			continue;

		if (this.storeBuffers[i].instruction.instructionStatus.executionCyclesRemaining != 0)
			continue; // the instruction is still executing

		if (this.storeBuffers[i].instruction.instructionStatus.executionComplete == currentCycle)
			continue; // the instruction has completed execution in the current cycle, so it cannot be
		Boolean conflictingInstruction = findConflictingInstruction(this.storeBuffers[i].instruction);
        if (conflictingInstruction) {
            // Determine the earliest issued instruction between the two
          
                continue; // Defer this instruction to the next cycle
            }
		

        if(writeback) {
        	continue;
        }
        
        writeback = true;
        
        
        Instruction currentInstruction = this.storeBuffers[i].instruction;
        int index = instructions.length - 1; // Start from the last instruction
        String rs = currentInstruction.Rs.replaceAll("[^0-9]", "");
        int regNumber = Integer.parseInt(rs);
        
        
        if(regNumber !=-1) {
        	 if (this.registersStatus[regNumber-1].writingUnit.equals(this.storeBuffers[i].name)) {
                 this.registersStatus[regNumber-1].value = result2;///LOOK HERE  YA AHMED
                 this.registersStatus[regNumber-1].writingUnit = ""; // Clear the writing unit 
             } else {
                     this.registersStatus[regNumber-1].value = result2;///LOOK HERE  YA AHMED
                 }
        }

        while (index >= 0 && instructions[index] != null && instructions[index] != currentInstruction) {
            Instruction inst = instructions[index];
            
            switch (inst.instructionType) {
            case "ADD":
            case "SUB":
            case "MUL":
            case "DIV":
            	 String rd2 = inst.Rd.replaceAll("[^0-9]", "");
                 int regNumber2 = Integer.parseInt(rd2);
                 
                 String rs20 = inst.Rs.replaceAll("[^0-9]", "");
                 int regNumber20 = Integer.parseInt(rs20);
                 
                 String rt20 = inst.Rt.replaceAll("[^0-9]", "");
                 int regNumber21 = Integer.parseInt(rt20);
                 
                 if (regNumber2 == regNumber) {
                     int index2 = containsAddSub(inst);
                     int index3 = containsMulDiv(inst);
                     if (index2 != -1) {
                         this.addSubReservationStations[index2].waw = false;
                     }
                     if (index3 != -1) {
                         this.multDivReservationStations[index3].waw = false;
                     }
                 }
                 
                 if (regNumber20 == regNumber || regNumber21 == regNumber ) {
                     int index77 = containsAddSub(inst);
                     int index80 = containsMulDiv(inst);

                     if (index77 != -1) {
                         this.addSubReservationStations[index77].raw = true;
                     }
                     
                     if (index80 != -1) {
                         this.multDivReservationStations[index80].raw = true;
                     }
                     
                 }
                 
                 break;

                 
               
            case "ADDI":
            case "SUBI":
            	 String rd4 = inst.Rd.replaceAll("[^0-9]", "");
                 int regNumber10 = Integer.parseInt(rd4);
                 
                 String rs7 = inst.Rs.replaceAll("[^0-9]", "");
                 int regNumber89 = Integer.parseInt(rs7);
                 
                 if (regNumber10 == regNumber) {
                     int index2 = containsAddSub(inst);
                     if (index2 != -1) {
                         this.addSubReservationStations[index2].waw = false;
                     }
                    
                 }
                 
                 if (regNumber89 == regNumber) {
                     int index60 = containsAddSub(inst);
                     if (index60 != -1) {
                         this.addSubReservationStations[index60].raw = true;
                     }
                     
                 }
                 break;


            case "LOAD":
           	    
                String rt = inst.Rt.replaceAll("[^0-9]", "");
                int regNumber4 = Integer.parseInt(rt);
                
                if (regNumber4 == regNumber) {
               	 	int index4 = containsLoad(inst);

                    if (index4 != -1) {
                        this.loadBuffers[index4].waw = false;
                    }
               	
               }
               
                break;


            case "STORE":
           	           	  
                String rs32 = inst.Rs.replaceAll("[^0-9]", "");
                int regNumber3 = Integer.parseInt(rs32);
                
                String rt2 = inst.Rt.replaceAll("[^0-9]", "");
                int regNumber8= Integer.parseInt(rt2);
                if (regNumber3 == regNumber) {
                     int index5 = containsStore(inst);

                     if (index5 != -1) {
                         this.storeBuffers[index5].waw = false;
                     }
                	
                }
                
                if (regNumber8 == regNumber) {
                    int index8 = containsStore(inst);

                    if (index8 != -1) {
                        this.storeBuffers[index8].raw = true;
                    }
               	
               }
                
                break;


            case "BNEZ":
               break;


            default:
              
                break;
       	 }
            
            index--; // Move to the previous instruction
        }
		
		reason += "-> The instruction at Store Buffer= " + this.storeBuffers[i].name + " has written back.\n";

		this.storeBuffers[i].isBusy = false;
		this.storeBuffers[i].instruction.instructionStatus.writeBack = (short) currentCycle;
		this.storeBuffers[i].address = "";
		this.storeBuffers[i].instruction = null;
	}

}
private void writeBackAddSubReservationStations(int result2) {
    for (int i = 0; i < totalAddSubReservationStations; i++) {
     
    	if (!this.addSubReservationStations[i].isBusy) continue;
		if (this.addSubReservationStations[i].instruction.instructionStatus.executionCyclesRemaining != 0)continue;
		if (this.addSubReservationStations[i].instruction.instructionStatus.executionComplete == currentCycle) continue;
		Boolean conflictingInstruction = findConflictingInstruction(this.addSubReservationStations[i].instruction);
        if (conflictingInstruction) {
            // Determine the earliest issued instruction between the two
         
                continue; // Defer this instruction to the next cycle
            }
        
        if(writeback) {
        	continue;
        }
        
        writeback = true;
        String destReg = this.addSubReservationStations[i].instruction.Rd.substring(1).replaceAll("[^0-9]", "");
        int regNum = Integer.parseInt(destReg);
        if (regNum - 1 < 0) continue;

        // Check if this instruction is the next one to write back to the destination register
        if (this.registersStatus[regNum-1].writingUnit.equals(this.addSubReservationStations[i].name)) {
            this.registersStatus[regNum-1].value = result2;///LOOK HERE  YA AHMED
            this.registersStatus[regNum-1].writingUnit = ""; // Clear the writing unit 
        } else {
                this.registersStatus[regNum-1].value = result2;///LOOK HERE  YA AHMED
            }
           
                this.addSubReservationStations[i].instruction.instructionStatus.writeBack = (short) (currentCycle);
            	
            
			
            
            reason += "-> The instruction at Reservation Station= " + this.addSubReservationStations[i].name + " has written back.\n";

            String val = "V" + addSubNum;
            publishResult(val, this.addSubReservationStations[i].name);
            addSubNum++;
        Instruction currentInstruction = this.addSubReservationStations[i].instruction;
        int index = instructions.length - 1; // Start from the last instruction
        String rd = currentInstruction.Rd.replaceAll("[^0-9]", "");
        int regNumber = Integer.parseInt(rd);

        while (index >= 0 && instructions[index] != null && instructions[index] != currentInstruction) {
            Instruction inst = instructions[index];
            
            switch (inst.instructionType) {
            case "ADD":
            case "SUB":
            case "MUL":
            case "DIV":
            	 String rd2 = inst.Rd.replaceAll("[^0-9]", "");
                 int regNumber2 = Integer.parseInt(rd2);
                 
                 String rs20 = inst.Rs.replaceAll("[^0-9]", "");
                 int regNumber20 = Integer.parseInt(rs20);
                 
                 String rt20 = inst.Rt.replaceAll("[^0-9]", "");
                 int regNumber21 = Integer.parseInt(rt20);
                 
                 if (regNumber2 == regNumber) {
                     int index2 = containsAddSub(inst);
                     int index3 = containsMulDiv(inst);
                     if (index2 != -1) {
                         this.addSubReservationStations[index2].waw = false;
                     }
                     if (index3 != -1) {
                         this.multDivReservationStations[index3].waw = false;
                     }
                 }
                 
                 if (regNumber20 == regNumber || regNumber21 == regNumber ) {
                     int index77 = containsAddSub(inst);
                     int index80 = containsMulDiv(inst);

                     if (index77 != -1) {
                         this.addSubReservationStations[index77].raw = true;
                     }
                     
                     if (index80 != -1) {
                         this.multDivReservationStations[index80].raw = true;
                     }
                     
                 }
                 break;

                 
               
            case "ADDI":
            case "SUBI":
            	 String rd4 = inst.Rd.replaceAll("[^0-9]", "");
                 int regNumber10 = Integer.parseInt(rd4);
                 
                 String rs7 = inst.Rs.replaceAll("[^0-9]", "");
                 int regNumber89 = Integer.parseInt(rs7);
                 
                 if (regNumber10 == regNumber) {
                     int index2 = containsAddSub(inst);
                     if (index2 != -1) {
                         this.addSubReservationStations[index2].waw = false;
                     }
                    
                 }
                 
                 if (regNumber89 == regNumber) {
                     int index60 = containsAddSub(inst);
                     if (index60 != -1) {
                         this.addSubReservationStations[index60].raw = true;
                     }
                     
                 }
                 break;


            case "LOAD":
           	    
                String rt = inst.Rt.replaceAll("[^0-9]", "");
                int regNumber4 = Integer.parseInt(rt);
                
                if (regNumber4 == regNumber) {
               	 	int index4 = containsLoad(inst);

                    if (index4 != -1) {
                        this.loadBuffers[index4].waw = false;
                    }
               	
               }
                break;

               

            case "STORE":
           	           	  
                String rs = inst.Rs.replaceAll("[^0-9]", "");
                int regNumber3 = Integer.parseInt(rs);
                
                String rt2 = inst.Rt.replaceAll("[^0-9]", "");
                int regNumber8= Integer.parseInt(rt2);
                
                if (regNumber3 == regNumber) {
                     int index5 = containsStore(inst);

                     if (index5 != -1) {
                         this.storeBuffers[index5].waw = false;
                     }
                	
                }
                
                if (regNumber8 == regNumber) {
                    int index8 = containsStore(inst);

                    if (index8 != -1) {
                        this.storeBuffers[index8].raw = true;
                    }
               	
               }
                break;


            case "BNEZ":
               break;


            default:
              
                break;
       	 }
            
            
            index--; // Move to the previous instruction
        }


        // Reset the reservation station
        this.addSubReservationStations[i].isBusy = false;
        this.addSubReservationStations[i].instructionType = "";
        this.addSubReservationStations[i].Qj = "";
        this.addSubReservationStations[i].Qk = "";
        this.addSubReservationStations[i].Vj = "";
        this.addSubReservationStations[i].Vk = "";
        this.addSubReservationStations[i].instruction = null;
    }
}


private void writeBackMultDivReservationStations(int result2) {
    for (int i = 0; i < totalMultDivReservationStations; i++) {

        if (!this.multDivReservationStations[i].isBusy)
            continue;
        if (this.multDivReservationStations[i].instruction.instructionStatus.executionCyclesRemaining != 0)
            continue; // the instruction is still executing
        Boolean conflictingInstruction = findConflictingInstruction(this.multDivReservationStations[i].instruction);
        if (conflictingInstruction)
            continue; 
        

        if(writeback) {
        	continue;
        }
        
        writeback = true;
         // Defer this instruction to the next cycle
        if (this.multDivReservationStations[i].instruction.instructionStatus.executionComplete >= currentCycle)
            continue; // the instruction has not completed execution in the current cycle
       
	        this.multDivReservationStations[i].instruction.instructionStatus.writeBack = (short) ((short) currentCycle);
		
        reason += "-> The instruction at Reservation Station= " + this.multDivReservationStations[i].name
                + " has written back.\n";
        String rdValue = this.multDivReservationStations[i].instruction.Rd.substring(1);
        rdValue = rdValue.replaceAll("[^0-9]", ""); // Remove non-numeric characters
        int regNum = Integer.parseInt(rdValue);
        if (regNum - 1 != -1) {
            if (this.registersStatus[regNum-1].writingUnit.equals(this.multDivReservationStations[i].name)) {
                this.registersStatus[regNum-1].value = result2;
                this.registersStatus[regNum-1].writingUnit = ""; // Clear the writing unit 

                String val = "V" + mulDivNum;
                publishResult(val, this.multDivReservationStations[i].name);
                mulDivNum++;
            }
        }
        Instruction currentInstruction = this.multDivReservationStations[i].instruction;
        int index = instructions.length - 1; // Start from the last instruction
        String rd = currentInstruction.Rd.replaceAll("[^0-9]", "");
        int regNumber = Integer.parseInt(rd);

        while (index >= 0 && instructions[index] != null && instructions[index] != currentInstruction) {
            Instruction inst = instructions[index];
            
            
            switch (inst.instructionType) {
            case "ADD":
            case "SUB":
            case "MUL":
            case "DIV":
            	 String rd2 = inst.Rd.replaceAll("[^0-9]", "");
                 int regNumber2 = Integer.parseInt(rd2);
                 
                 String rs20 = inst.Rs.replaceAll("[^0-9]", "");
                 int regNumber20 = Integer.parseInt(rs20);
                 
                 String rt20 = inst.Rt.replaceAll("[^0-9]", "");
                 int regNumber21 = Integer.parseInt(rt20);
                 
                 if (regNumber2 == regNumber) {
                     int index2 = containsAddSub(inst);
                     int index3 = containsMulDiv(inst);
                     if (index2 != -1) {
                         this.addSubReservationStations[index2].waw = false;
                     }
                     if (index3 != -1) {
                         this.multDivReservationStations[index3].waw = false;
                     }
                 }
                 
                 if (regNumber20 == regNumber || regNumber21 == regNumber ) {
                     int index77 = containsAddSub(inst);
                     int index80 = containsMulDiv(inst);

                     if (index77 != -1) {
                         this.addSubReservationStations[index77].raw = true;
                     }
                     
                     if (index80 != -1) {
                         this.multDivReservationStations[index80].raw = true;
                     }
                     
                 }
                 break;

                 
               
            case "ADDI":
            case "SUBI":
            	 String rd4 = inst.Rd.replaceAll("[^0-9]", "");
                 int regNumber10 = Integer.parseInt(rd4);
                 
                 String rs7 = inst.Rs.replaceAll("[^0-9]", "");
                 int regNumber89 = Integer.parseInt(rs7);
                 
                 if (regNumber10 == regNumber) {
                     int index2 = containsAddSub(inst);
                     if (index2 != -1) {
                         this.addSubReservationStations[index2].waw = false;
                     }
                    
                 }
                 
                 if (regNumber89 == regNumber) {
                     int index60 = containsAddSub(inst);
                     if (index60 != -1) {
                         this.addSubReservationStations[index60].raw = true;
                     }
                     
                 }
                 break;


            case "LOAD":
           	    
                String rt = inst.Rt.replaceAll("[^0-9]", "");
                int regNumber4 = Integer.parseInt(rt);
                
                if (regNumber4 == regNumber) {
               	 	int index4 = containsLoad(inst);

                    if (index4 != -1) {
                        this.loadBuffers[index4].waw = false;
                    }
               	
               }
                break;


            case "STORE":
           	           	  
                String rs = inst.Rs.replaceAll("[^0-9]", "");
                int regNumber3 = Integer.parseInt(rs);
                
                String rt2 = inst.Rt.replaceAll("[^0-9]", "");
                int regNumber8= Integer.parseInt(rt2);
                if (regNumber3 == regNumber) {
                     int index5 = containsStore(inst);

                     if (index5 != -1) {
                         this.storeBuffers[index5].waw = false;
                     }
                	
                }
                
                if (regNumber8 == regNumber) {
                    int index8 = containsStore(inst);

                    if (index8 != -1) {
                        this.storeBuffers[index8].raw = true;
                    }
               	
               }
                break;

            case "BNEZ":
               break;


            default:
              
                break;
       	 }
            
           
            index--; // Move to the previous instruction
        }

        
        
        this.multDivReservationStations[i].isBusy = false;
        this.multDivReservationStations[i].instructionType = "";
        this.multDivReservationStations[i].Qj = "";
        this.multDivReservationStations[i].Qk = "";
        this.multDivReservationStations[i].Vj = "";
        this.multDivReservationStations[i].Vk = "";
        this.multDivReservationStations[i].instruction = null;
    }
}


public Object[][] getInstructionsData() {
    Object[][] data = new Object[totalInstructions][5]; 

    for (int i = 0; i < totalInstructions; i++) {
        if (instructions[i] != null && instructions[i].instructionStatus != null) {
            data[i][0] = formatInstruction(i); // Instruction
            data[i][1] = instructions[i].instructionStatus.issue; // Issue
            data[i][2] = instructions[i].instructionStatus.executionStart; // Start
            data[i][3] = instructions[i].instructionStatus.executionComplete; // Finish
            data[i][4] = instructions[i].instructionStatus.writeBack; // Write-Back
        } else {
            data[i][0] = "Instruction " + i + " not initialized";
            // Fill other cells with appropriate default values (e.g., empty string or null)
        }
    }

    return data;
}


public Object[][] getLoadStoreBuffersData() {
    Object[][] data = new Object[totalLoadBuffers + totalStoreBuffers][5]; // Assuming 5 columns: Name, Busy, Address, FU, Time

    // Process Load Buffers
    for (int i = 0; i < totalLoadBuffers; i++) {
        data[i][0] = loadBuffers[i].name;
        data[i][1] = loadBuffers[i].isBusy ? "Yes" : "No";
        data[i][2] = loadBuffers[i].address;
        data[i][3] = loadBuffers[i].fu;
        data[i][4] = loadBuffers[i].instruction != null ? loadBuffers[i].instruction.instructionStatus.executionCyclesRemaining : "";
    }

    // Process Store Buffers
    for (int i = 0; i < totalStoreBuffers; i++) {
        data[totalLoadBuffers + i][0] = storeBuffers[i].name;
        data[totalLoadBuffers + i][1] = storeBuffers[i].isBusy ? "Yes" : "No";
        data[totalLoadBuffers + i][2] = storeBuffers[i].address;
        data[totalLoadBuffers + i][3] = storeBuffers[i].fu;
        data[totalLoadBuffers + i][4] = storeBuffers[i].instruction != null ? storeBuffers[i].instruction.instructionStatus.executionCyclesRemaining : "";
    }

    return data;
}
public Object[][] getReservationStationsData() {
    Object[][] data = new Object[totalAddSubReservationStations + totalMultDivReservationStations][8]; // Assuming 8 columns: Name, Busy, Op, Vj, Vk, Qj, Qk, Time

    // Process Add/Sub Reservation Stations
    for (int i = 0; i < totalAddSubReservationStations; i++) {
        data[i][0] = addSubReservationStations[i].name; // Name
        data[i][1] = addSubReservationStations[i].isBusy ? "Yes" : "No"; // Busy
        data[i][2] = addSubReservationStations[i].instructionType; // Op
        data[i][3] = addSubReservationStations[i].Vj; // Vj
        data[i][4] = addSubReservationStations[i].Vk; // Vk
        data[i][5] = addSubReservationStations[i].Qj; // Qj
        data[i][6] = addSubReservationStations[i].Qk; // Qk
        data[i][7] = addSubReservationStations[i].instruction != null ? addSubReservationStations[i].instruction.instructionStatus.executionCyclesRemaining : ""; // Time
    }

    // Process Mult/Div Reservation Stations
    for (int i = 0; i < totalMultDivReservationStations; i++) {
        int index = totalAddSubReservationStations + i;
        data[index][0] = multDivReservationStations[i].name; // Name
        data[index][1] = multDivReservationStations[i].isBusy ? "Yes" : "No"; // Busy
        data[index][2] = multDivReservationStations[i].instructionType; // Op
        data[index][3] = multDivReservationStations[i].Vj; // Vj
        data[index][4] = multDivReservationStations[i].Vk; // Vk
        data[index][5] = multDivReservationStations[i].Qj; // Qj
        data[index][6] = multDivReservationStations[i].Qk; // Qk
        data[index][7] = multDivReservationStations[i].instruction != null ? multDivReservationStations[i].instruction.instructionStatus.executionCyclesRemaining : ""; // Time
    }

    return data;
}

public Object[][] getRegisterFileData() {
    Object[][] data = new Object[totalRegisters][3]; // Assuming 2 columns: Register, Writing Unit

    for (int i = 0; i < totalRegisters; i++) {
        data[i][0] = registersStatus[i].registerName;
        data[i][1] = registersStatus[i].writingUnit;
        data[i][2] = registersStatus[i].value;
    }
    
    

    return data;
}

private String formatInstruction(int i) {
    Instruction inst = instructions[i];
    String type = inst.instructionType;
    String operands;
    if ("BNEZ".equalsIgnoreCase(type)) {
        operands = String.format("%s, %d", inst.Rs, inst.immediateOffset);
    } else if (Arrays.asList("ADDI", "SUBI").contains(type.toUpperCase())) {
        operands = String.format("%s, %s, %d", inst.Rd, inst.Rs, inst.immediateOffset);
    } else if (Arrays.asList("LOAD", "STORE").contains(type.toUpperCase())) {
        operands = String.format("%s, %d", inst.Rt, inst.immediateOffset);
    } else {
        operands = String.format("%s, %s, %s", inst.Rd, inst.Rs, inst.Rt);
    }
    return type + " " + operands;
}

	    
	    
private boolean findConflictingInstruction(Instruction currentInstruction) {
	int index = 0; // Start from the first instruction

    while (index < instructions.length && instructions[index] != currentInstruction) {
        Instruction inst = instructions[index];
        if (inst != null && inst.instructionStatus.writeBack == currentCycle) {
            return true;
        }
        index++; // Move to the next instruction
    }

    return false; // No conflicting instruction found
}

private boolean problemMulDiv(Instruction currentInstruction) {
	
	
    int index = 0; // Start from the first instruction
    String rs = currentInstruction.Rs.replaceAll("[^0-9]", "");
    int regNumber = Integer.parseInt(rs);
    
    String rt = currentInstruction.Rt.replaceAll("[^0-9]", "");
    int regNumber2 = Integer.parseInt(rt);
    while (index < instructions.length && instructions[index] != currentInstruction) {
    	Instruction inst = instructions[index];
    	 switch (inst.instructionType) {
         case "ADD":
         case "SUB":
         case "MUL":
         case "DIV":
         case "ADDI":
         case "SUBI":
        	 
        	 String rd = inst.Rd.replaceAll("[^0-9]", "");
        	 int regNumber3 = Integer.parseInt(rd);
            
            if (inst != null  && ((regNumber3==regNumber) ||(regNumber3==regNumber2))) {
                
                return true; // Conflict found
            }
            
            break;

        	 
         case "LOAD":
        	 
        	 String rt1 = inst.Rt.replaceAll("[^0-9]", "");
        	 int regNumber4 = Integer.parseInt(rt1);
            
            if (inst != null  && ((regNumber4==regNumber) ||(regNumber4==regNumber2))) {
                
                return true; // Conflict found
            }
      
            break;


         case "STORE":
        	 
        	 String rs1 = inst.Rs.replaceAll("[^0-9]", "");
        	 int regNumber5 = Integer.parseInt(rs1);
            
            if (inst != null && ((regNumber5==regNumber) ||(regNumber5==regNumber2))) {
                
                return true; // Conflict found
            }

           

            break;


         case "BNEZ":
            break;


         default:
           
             break;
    	 }
    	
        index++; // Move to the next instruction
    }

    return false; // No conflicting instruction found
}

private boolean problemAddSub(Instruction currentInstruction) {
	
	if((currentInstruction.instructionType == "ADDI") || (currentInstruction.instructionType == "SUBI")) {
		return false;
	}
    int index = 0; // Start from the first instruction
    String rs = currentInstruction.Rs.replaceAll("[^0-9]", "");
    int regNumber = Integer.parseInt(rs);
    
    String rt = currentInstruction.Rt.replaceAll("[^0-9]", "");
    int regNumber2 = Integer.parseInt(rt);
    while (index < instructions.length && instructions[index] != currentInstruction) {
    	Instruction inst = instructions[index];
    	 switch (inst.instructionType) {
         case "ADD":
         case "SUB":
         case "MUL":
         case "DIV":
         case "ADDI":
         case "SUBI":
        	 
        	 String rd = inst.Rd.replaceAll("[^0-9]", "");
        	 int regNumber3 = Integer.parseInt(rd);
            
            if (inst != null && ((regNumber3==regNumber) ||(regNumber3==regNumber2))) {
                
                return true; // Conflict found
            }
            
            break;

        	 
         case "LOAD":
        	 
        	 String rt1 = inst.Rt.replaceAll("[^0-9]", "");
        	 int regNumber4 = Integer.parseInt(rt1);
            
            if (inst != null  && ((regNumber4==regNumber) ||(regNumber4==regNumber2))) {
                
                return true; // Conflict found
            }
      
            break;


         case "STORE":
        	 
        	 String rs1 = inst.Rs.replaceAll("[^0-9]", "");
        	 int regNumber5 = Integer.parseInt(rs1);
            
            if (inst != null&& ((regNumber5==regNumber) ||(regNumber5==regNumber2))) {
                
                return true; // Conflict found
            }

           

            break;


         case "BNEZ":
            break;


         default:
           
             break;
    	 }
    	
        index++; // Move to the next instruction
    }

    return false; // No conflicting instruction found
}

private boolean problemAddiSubi(Instruction currentInstruction) {
	
	if((currentInstruction.instructionType == "ADD") || (currentInstruction.instructionType == "SUB") ) {
		return false;
	}
    int index = 0; // Start from the first instruction
    String rs = currentInstruction.Rs.replaceAll("[^0-9]", "");
    int regNumber = Integer.parseInt(rs);
    
   
    while (index < instructions.length && instructions[index] != currentInstruction) {
    	Instruction inst = instructions[index];
    	switch (inst.instructionType) {
        case "ADD":
        case "SUB":
        case "MUL":
        case "DIV":
        case "ADDI":
        case "SUBI":
       	 
       	 String rd = inst.Rd.replaceAll("[^0-9]", "");
       	 int regNumber3 = Integer.parseInt(rd);
           
           if (inst != null && ((regNumber3==regNumber) )) {
               
               return true; // Conflict found
           }
           break;

        case "LOAD":
       	 
       	 String rt1 = inst.Rt.replaceAll("[^0-9]", "");
       	 int regNumber4 = Integer.parseInt(rt1);
           
           if (inst != null && ((regNumber4==regNumber) )) {
               
               return true; // Conflict found
           }
           break;

           

        case "STORE":
       	 
       	 String rs1 = inst.Rs.replaceAll("[^0-9]", "");
       	 int regNumber5 = Integer.parseInt(rs1);
           
           if (inst != null && ((regNumber5==regNumber) )) {
               
               return true; // Conflict found
           }

           break;


    

        case "BNEZ":
           break;


        default:
          
            break;
   	 }
    	
        index++; // Move to the next instruction
    }

    return false; // No conflicting instruction found
}



private boolean problemStoreWAR(Instruction currentInstruction) {
	
	
    int index = 0; // Start from the first instruction
    String rt = currentInstruction.Rt.replaceAll("[^0-9]", "");
    int regNumber = Integer.parseInt(rt);
    
   
    while (index < instructions.length && instructions[index] != currentInstruction) {
    	Instruction inst = instructions[index];
    	switch (inst.instructionType) {
        case "ADD":
        case "SUB":
        case "MUL":
        case "DIV":
        case "ADDI":
        case "SUBI":
       	 
       	 String rd = inst.Rd.replaceAll("[^0-9]", "");
       	 int regNumber3 = Integer.parseInt(rd);
           
           if (inst != null && ((regNumber3==regNumber) )) {
               
               return true; // Conflict found
           }
           break;
       	 
        case "LOAD":
       	 
       	 String rt1 = inst.Rt.replaceAll("[^0-9]", "");
       	 int regNumber4 = Integer.parseInt(rt1);
           
           if (inst != null && ((regNumber4==regNumber) )) {
               
               return true; // Conflict found
           }
     
           
           break;

        case "STORE":
       	 
       	 String rs1 = inst.Rs.replaceAll("[^0-9]", "");
       	 int regNumber5 = Integer.parseInt(rs1);
           
           if (inst != null && ((regNumber5==regNumber) )) {
               
               return true; // Conflict found
           }
           break;

          

    

        case "BNEZ":
           break;


        default:
          
            break;
   	 }
    	
        index++; // Move to the next instruction
    }

    return false; // No conflicting instruction found
}


private boolean problemLoadWAR(Instruction currentInstruction) {
	
	
    int index = 0; // Start from the first instruction
    String rt = currentInstruction.Rt.replaceAll("[^0-9]", "");
    int regNumber = Integer.parseInt(rt);
    
   
    while (index < instructions.length && instructions[index] != currentInstruction) {
    	Instruction inst = instructions[index];
    	switch (inst.instructionType) {
        case "ADD":
        case "SUB":
        case "MUL":
        case "DIV":
        case "ADDI":
        case "SUBI":
       	break;
       	 
        case "LOAD":
       	 
       	 String rt1 = inst.Rt.replaceAll("[^0-9]", "");
       	 int regNumber4 = Integer.parseInt(rt1);
           
           if (inst != null && ((regNumber4==regNumber) )) {
               
               return true; // Conflict found
           }
     
           
           break;

        case "STORE":


           break;

          

    

        case "BNEZ":
           break;


        default:
          
            break;
   	 }
    	
        index++; // Move to the next instruction
    }

    return false; // No conflicting instruction found
}






private int containsAddSub(Instruction searchInstruction) {
    // Loop through the reservation station and check if it contains the given instruction
    for (int i = 0; i < this.addSubReservationStations.length; i++) {
        ReservationStation rs = this.addSubReservationStations[i];
        if (rs.isBusy && rs.instruction.equals(searchInstruction)) {
            return i; // Return the index if the instruction is found
        }
    }
    return -1; // Return -1 if the instruction is not found
}

private int containsMulDiv(Instruction searchInstruction) {
    // Loop through the reservation station and check if it contains the given instruction
    for (int i = 0; i < this.multDivReservationStations.length; i++) {
        ReservationStation rs = this.multDivReservationStations[i];
        if (rs.isBusy && rs.instruction.equals(searchInstruction)) {
            return i; // Return the index if the instruction is found
        }
    }
    return -1; // Return -1 if the instruction is not found
}
	 
private int containsLoad(Instruction searchInstruction) {
    // Loop through the reservation station and check if it contains the given instruction
    for (int i = 0; i < this.loadBuffers.length; i++) {
    	LoadStoreBuffer rs = this.loadBuffers[i];
        if (rs.isBusy && rs.instruction.equals(searchInstruction)) {
            return i; // Return the index if the instruction is found
        }
    }
    return -1; // Return -1 if the instruction is not found
}

private int containsStore(Instruction searchInstruction) {
    // Loop through the reservation station and check if it contains the given instruction
    for (int i = 0; i < this.storeBuffers.length; i++) {
    	LoadStoreBuffer rs = this.storeBuffers[i];
        if (rs.isBusy && rs.instruction.equals(searchInstruction)) {
            return i; // Return the index if the instruction is found
        }
    }
    return -1; // Return -1 if the instruction is not found
}
	    
	    public int findFreeStoreBuffer() {
	        for (int i = 0; i < totalStoreBuffers; i++) {
	            if (storeBuffers[i].isBusy == false)
	                return i;
	        }
	        return -1;
	    }
	
	    public int findFreeAddSubReservationStation() {
	        for (int i = 0; i < totalAddSubReservationStations; i++) {
	            if (addSubReservationStations[i].isBusy == false) {///this line 
	                return i;
	            }
	        }
	        return -1;
	    }
	
	    public int findFreeMulDivReservationStation() {
	        for (int i = 0; i < totalMultDivReservationStations; i++) {
	            if (multDivReservationStations[i].isBusy == false) {
	                return i;
	            }
	        }
	        return -1;
	    }
	
	    public int findFreeLoadBuffer() {
	        for (int i = 0; i < totalLoadBuffers; i++) {
	            if (loadBuffers[i].isBusy == false)
	                return i;
	        }
	        return -1;
	    }
	
	    public void publishResult(String val, String name) {
	        for (int i = 0; i < totalLoadBuffers; i++) {
	            if (this.loadBuffers[i].isBusy == false)
	                continue;
	
	            if (this.loadBuffers[i].fu == name) {
	                this.loadBuffers[i].fu = "";
	            }
	        }
	
	        for (int i = 0; i < totalStoreBuffers; i++) {
	            if (this.storeBuffers[i].isBusy == false)
	                continue;
	
	            if (this.storeBuffers[i].fu == name) {
	                this.storeBuffers[i].fu = "";
	            }
	        }
	
	        for (int i = 0; i < totalAddSubReservationStations; i++) {
	            if (this.addSubReservationStations[i].Qj == name) {
	                this.addSubReservationStations[i].Qj = "";
	                this.addSubReservationStations[i].Vj = val;
	            }
	
	            if (this.addSubReservationStations[i].Qk == name) {
	                this.addSubReservationStations[i].Qk = "";
	                this.addSubReservationStations[i].Vk = val;
	            }
	        }
	
	        for (int i = 0; i < totalMultDivReservationStations; i++) {
	            if (this.multDivReservationStations[i].Qj == name) {
	                this.multDivReservationStations[i].Qj = "";
	                this.multDivReservationStations[i].Vj = val;
	            }
	
	            if (this.multDivReservationStations[i].Qk == name) {
	                this.multDivReservationStations[i].Qk = "";
	                this.multDivReservationStations[i].Vk = val;
	            }
	        }
	    }
	  
	
	}