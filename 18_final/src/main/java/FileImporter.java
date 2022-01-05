import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;

    // reads an external file and stores each line as strings in a String array
    public class FileImporter {
        private BufferedReader reader;

        public String[] readAllLinesInFile(String file) {
            String[] array = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                int linesInFile = getLinesInFile();
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

        private int getLinesInFile() throws IOException {
            int lines = 0;
            String currentLine = reader.readLine(); // read one line
            while (currentLine != null) {
                currentLine = reader.readLine();
                lines++;
            }
            return lines;
        }
}
