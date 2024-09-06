import java.io.IOException;

public class FileHandler {
    public void readFile(String filePath) {
        try {
            // File reading logic
            System.out.println("File read successfully");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}

