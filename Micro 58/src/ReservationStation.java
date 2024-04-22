public class ReservationStation {

    String name;
    boolean isBusy;
    String instructionType = "";
    String Vj = "";
    String Vk = "";
    String Qj = "";
    String Qk = "";
    Instruction instruction; // the pointer to the current instruction being executed
    boolean waw ;
    boolean raw ;
    int moe ;
    boolean load;
    public ReservationStation() {
        isBusy = false;
        waw = false;
        raw = false;
        moe = 1;
        load = false;

    }

    @Override
    public String toString() {
        return "ReservationStation [name=" + name + ", isBusy=" + isBusy + ", instructionType=" + instructionType
                + ", Vj=" + Vj + ", Vk=" + Vk + ", Qj=" + Qj + ", Qk=" + Qk + ", instruction=" + instruction + "]";
    }
    public void clear() {
        this.isBusy = false;
        this.Qj = "";
        this.Qk = "";
        this.instruction = null; // Assuming you can set it to null, or create a new empty Instruction
        // ... reset other fields as necessary
    }
    
    
    
    
    
    


}