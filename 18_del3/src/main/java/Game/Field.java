package Game;

public class Field {
    protected static int nextFieldNumber = 1;
    protected final int fieldNumber;
    protected final String fieldName;

    public Field(String fieldName) {
        this.fieldName = fieldName;
        this.fieldNumber = nextFieldNumber;
        nextFieldNumber++;
    }

    public void landOnField(Player currentplayerobject) {     }

    //no setters as all attributes are final variables
    public int getFieldNumber (){ return fieldNumber; }

    public String getFieldName(){ return fieldName; }

    public String toString() {
        return "field " + fieldNumber  + ": " + fieldName;
    }

    public String getClassName() { return getClass().toString().substring(6); }

}
