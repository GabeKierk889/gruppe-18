public class Field {
    protected static int totalnumberOfFields = 1;
    protected final int fieldNumber;
    protected final String fieldName;

    public Field(String fieldName) {
        this.fieldName = fieldName;
        this.fieldNumber = totalnumberOfFields;
        totalnumberOfFields++;
    }

    public void landOnField(Player currentplayerobject) {     }

    //no setters as all attributes are final variables
    public int getFieldNumber (){ return fieldNumber; }
    public static int getTotalnumberOfFields (){ return totalnumberOfFields-1; }
    public String getFieldName(){ return fieldName; }

    public String toString() {
        return "field " + fieldNumber  + ": " + fieldName;
    }

    public String getClassName() { return getClass().toString().substring(7); }

}
