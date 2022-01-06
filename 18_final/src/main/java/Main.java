import Services.FieldsDataReader;

public class Main {

    public static void main(String[] args) {
        FieldsDataReader read = new FieldsDataReader("Fields.csv");
        String[] fieldnames = read.getFieldNamesArray();
        int[] fieldPrice = read.getFieldPriceArray();
        int[][] rentArray = read.getRentArrayArray();
        for (String s : fieldnames)
            System.out.println(s);
        for (int i : fieldPrice)
            System.out.println(i);
        for (int r : rentArray[1])
            System.out.println(r);
    }
}
