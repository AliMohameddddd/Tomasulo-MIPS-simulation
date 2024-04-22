import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

public class TextAreaOutputStream extends OutputStream {
    private final JTextArea textArea;

    public TextAreaOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        // Append the character to the JTextArea (convert byte to char)
        textArea.append(String.valueOf((char) b));
        // Ensure the last line is always visible
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
