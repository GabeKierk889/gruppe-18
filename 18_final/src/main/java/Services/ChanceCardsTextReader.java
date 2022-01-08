package Services;

import java.util.Scanner;

// has methods to return raw chance card text, and to parse out and return any integers within the text
public class ChanceCardsTextReader {
    private String[] rawStringsData;
    private int[][] numArray;

    public ChanceCardsTextReader(String filename) {
        FileImporter reader = new FileImporter();
        rawStringsData = reader.readAllLinesInFile(filename);
        readData();
    }

    // reads and extracts any integers within the chance card text
    // can read/store up to 2 separate numbers within 1 chance card's text
    private void readData() {
        int maxNumberOfDifferentNumbersInText = 2;
        String[] temp = new String[rawStringsData.length];
        numArray = new int[rawStringsData.length][maxNumberOfDifferentNumbersInText];
        Scanner lineScan;
        for (int i = 0; i < rawStringsData.length; i++) {
            // gets rid of all characters except numbers or " "
            temp[i] = rawStringsData[i].replaceAll("[^0-9 ]","");
            lineScan = new Scanner(temp[i]); // uses scanner delimited by " " to pick out numbers
            int counter = 0;
            String next;
            while (lineScan.hasNext() && counter < numArray[i].length) { // reads up to 2 numbers and stores this data in a 2D array
                next = lineScan.next();
                if (!next.equals(""))
                    numArray[i][counter] = Integer.parseInt(next);
                counter++;
            }
        }
    }

    public String[] getChanceCardsTextArray() {
        return rawStringsData;
    }

    public int[][] getNumbersFromChanceCardsText() {
        return numArray;
    }
}


