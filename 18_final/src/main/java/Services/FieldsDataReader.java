package Services;

import java.util.Scanner;

// extracts raw data regarding fields from an external .csv file and puts the data into separate arrays
// assumes that the external file/ list is already ordered/ sorted by field position
// assumes the following order of columns: fieldname, position, type, price, house price, rent 0, rent 1 etc. with field color as last column.

public class FieldsDataReader {
    private String[] rawStringsData;
    private String[] fieldNamesArray;
    private String[] fieldTypeArray;
    private int[] fieldPriceArray;
    private int[] housePriceArray;
    private int[][] rentArrayArray;
    private String[] streetColorsArray;

    public FieldsDataReader(String filename) {
        FileImporter reader = new FileImporter();
        rawStringsData = reader.readAllLinesInFile(filename);
    }

    private void readData() {
        int rentLevels = 6;
        Scanner lineScan;
        fieldNamesArray = new String[rawStringsData.length - 1];
        streetColorsArray = new String[rawStringsData.length - 1];
        fieldTypeArray = new String[rawStringsData.length - 1];
        fieldPriceArray = new int[rawStringsData.length - 1];
        housePriceArray = new int[rawStringsData.length - 1];
        rentArrayArray = new int[rawStringsData.length - 1][rentLevels];

        for (int i = 0; i < fieldNamesArray.length; i++) {
            // scanner reads through all the lines, one line at a time
            lineScan = new Scanner(rawStringsData[i + 1]);
            lineScan.useDelimiter(","); // csv file delimited by comma
            fieldNamesArray[i] = lineScan.next(); // reads first column - field names
            lineScan.next();
            fieldTypeArray[i] = lineScan.next();
            String next = lineScan.next();
            if (!next.equals(""))
                fieldPriceArray[i] = Integer.parseInt(next); // reads field prices
            next = lineScan.next();
            if (!next.equals(""))
                housePriceArray[i] = Integer.parseInt(next); // reads house prices
            int counter = 0;
            while (lineScan.hasNext()) { // reads rent levels and stores this data in a 2D array of rents
                next = lineScan.next();
                counter++;
                if (counter < rentLevels && !next.equals(""))
                    rentArrayArray[i][counter] = Integer.parseInt(next);
                if (counter >= rentLevels)
                    streetColorsArray[i] = next; // reads last column - field colors
            }
        }
    }

    public String[] getFieldNamesArray() {
        readData();
        return fieldNamesArray;
    }

    public int[] getFieldPriceArray() {
        readData();
        return fieldPriceArray;
    }

    public int[] getHousePriceArray() {
        readData();
        return housePriceArray;
    }

    public int[][] getRentArrayArray() {
        readData();
        return rentArrayArray;
    }

    public String[] getStreetColorsArray() {
        return streetColorsArray;
    }

    public String[] getFieldTypeArray() {
        return fieldTypeArray;
    }
}

