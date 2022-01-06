package Services;

import java.io.*;
import java.util.Scanner;

// reads an external file and stores each line as strings in a String array
public class FileImporter {
    private BufferedReader reader;

    public String[] readAllLinesInFile(String file) {
        String[] array = null;
        try {
            int linesInFile = getLinesInFile(file);
            reader = new BufferedReader(new FileReader(file));
            array = new String[linesInFile];
            for (int i = 0; i < linesInFile; i++)
                array[i] = reader.readLine();
        }
        catch (FileNotFoundException i) {
            System.out.println("The file cannot be found");
        } catch (IOException e) {
            System.out.println("Error in reading file"); }
        finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                System.out.println("Error in closing file");
            }
        }
        return array;
    }

    private int getLinesInFile(String filename) throws IOException {
        int lines = 0;
        Scanner filescan = new Scanner(new File(filename));
        while (filescan.hasNext()) {
            filescan.nextLine();
            lines++;
        }
        return lines;
    }
}
