
public class Instruction {

    String instructionType;
    String Rd;
    String Rs;
    String Rt;
    int immediateOffset; // used only for i-type instructions
    InstructionStatus instructionStatus;

    public Instruction() {
        instructionType = Rd = Rs = Rt = "";
        immediateOffset = -1;
    }
    
    
    public void setRd(String Rd) {
        this.Rd = Rd;
    }

    public void setRs(String Rs) {
        this.Rs = Rs;
    }
    public String getRs() {
        return Rs;
    }

    
    public void setInstructionType(String instructionType) {
        this.instructionType = instructionType;
    }

    public void setRt(String Rt) {
        this.Rt = Rt;
    }

    public void setImmediateOffset(int immediateOffset) {
        this.immediateOffset = immediateOffset;
    }
    public static void addInstructionToArray(Instruction[] instructions, Instruction newInstruction) {
        // Find the first null element in the array to place the new instruction
        for (int i = 0; i < instructions.length; i++) {
            if (instructions[i] == null) {
                instructions[i] = newInstruction;
                return; // Exit the method once the instruction is added
            }
        }
    }
        public static int findInstructionIndex(Instruction[] instructions, Instruction targetInstruction) {
            for (int i = 0; i < instructions.length; i++) {
                if (instructions[i] != null && instructions[i].equals(targetInstruction)) {
                    return i;
                }
            }
            return -1; // Return -1 if the instruction is not found
        }

        // Override the equals method to define how two Instructions are compared
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Instruction other = (Instruction) obj;
            
            return instructionType.equals(other.instructionType)
                    && Rd.equals(other.Rd)
                    && Rs.equals(other.Rs)
                    && Rt.equals(other.Rt)
                   && immediateOffset == other.immediateOffset;
        }

    
        
    
    
    
    
    @Override
    public String toString() {
        return "Instruction [instructionType=" + instructionType + ", Rd=" + Rd + ", Rs=" + Rs + ", Rt=" + Rt
                + ", immediateOffset=" + immediateOffset + ", instructionStatus=" + instructionStatus + "]";
    }

}