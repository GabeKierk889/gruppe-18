public class Field {
    private static int totalnumberOfFields = 1;
    private final int fieldNumber;
    private final String fieldDescription;

    public Field(String fieldDescription) {
        this.fieldDescription = fieldDescription;
        this.fieldNumber = totalnumberOfFields;
        totalnumberOfFields++;
    }

    public void landOnField() {     }

    //no setters as all attributes are final variables
    public int getFieldNumber (){ return fieldNumber; }
    public static int getTotalnumberOfFields (){ return totalnumberOfFields-1; }
    public String getFieldDescription (){ return fieldDescription; }

    public String toString() {
        return "Field " + fieldNumber  + ". " + fieldDescription;
    }
}
