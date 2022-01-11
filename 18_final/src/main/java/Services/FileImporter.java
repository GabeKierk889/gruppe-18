package Services;

import java.io.*;
import java.util.Scanner;

// reads an external file and stores each line as strings in a String array
public class FileImporter {
    private BufferedReader reader;

    public String[] readAllLinesInFile(String filename) {
        String[] array = null;
        try {
            int linesInFile = getLinesInFile(filename);
            reader = new BufferedReader(new FileReader(filename));
            array = new String[linesInFile];
            for (int i = 0; i < linesInFile; i++)
                array[i] = reader.readLine();
        }
        catch (FileNotFoundException i) {
            System.out.println("System error - the file cannot be found");
        } catch (IOException e) {
            System.out.println("System error - cannot read file"); }
        finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                System.out.println("System error - cannot close file");
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
