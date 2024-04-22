
public class LoadStoreBuffer {

    String name;
    boolean isBusy;
    String address;
    String fu;
    Instruction instruction;
    boolean waw ;
    boolean raw ;
    int moe ;
    boolean load;

    public LoadStoreBuffer() {
        name = "";
        isBusy = false;
        address = "";
        fu = "";
        waw = false;
        raw = false;
        moe = 1;
        load = false;

    }

    @Override
    public String toString() {
        return "LoadStoreBuffer [name=" + name + ", isBusy=" + isBusy + ", address=" + address + ", fu=" + fu
                + ", instruction=" + instruction + "]";
    }

}