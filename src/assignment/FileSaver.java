package assignment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileSaver {
    public static void saveToFile(String fileName, String text) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.write(text);
            writer.newLine(); // Add a newline for the next entry
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while saving to the file: " + e.getMessage());
        }
    }
}

