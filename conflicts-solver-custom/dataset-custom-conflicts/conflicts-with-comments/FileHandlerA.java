import java.io.IOException;

public class FileHandler {
    // Use and exception to deal with the error
    public void readFile(String filePath) throws IOException {
        throw new IOException("File not found");
    }
}
