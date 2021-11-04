public class Field {
    private static int totalnumberOfFields = 0;
    private final int fieldNumber;
    private final String fieldDescription;
    private final String fieldSubtext;
    private final int fieldEffect;
    private final boolean fieldPositiveEffect; // false for withdraw, true for deposit

    public Field(String fieldSubtext, String fieldDescription, boolean fieldPositiveEffect, int fieldEffect) {
        this.fieldSubtext = fieldSubtext;
        this.fieldDescription = fieldDescription;
        this.fieldNumber = totalnumberOfFields;
        totalnumberOfFields++;
        this.fieldPositiveEffect = fieldPositiveEffect;
        this.fieldEffect = fieldEffect;
    }
    //no setters as all attributes are final variables
    public boolean getFieldPosiveEffect (){ return fieldPositiveEffect; }
    public int getFieldEffect (){ return fieldEffect; }
    public int getFieldNumber (){ return fieldNumber; }
    public int getTotalnumberOfFields (){ return totalnumberOfFields-1; }
    public String getFieldDescription (){ return fieldDescription; }
    public String getFieldSubtext() {return fieldSubtext; }

    public String toString() {
        return "Field " + fieldNumber  + ". " + fieldDescription;
    }
}
