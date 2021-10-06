package Game;

public class Field {
    private static int totalnumberOfFields = 0; //for Matador, remember when initializing to make an empty/placeholder field to let the actual fields start from array index 1
    private final int fieldNumber;
    private final String fieldName; //field description for this version of the dice game
    private int fieldEffect;
    private boolean fieldPositiveEffect; // false for withdraw, true for deposit

    public Field(String fieldName, boolean fieldPositiveEffect, int fieldEffect) {
        this.fieldName = fieldName;
        this.fieldNumber = totalnumberOfFields;
        this.fieldPositiveEffect = fieldPositiveEffect;
        this.fieldEffect = fieldEffect;
        totalnumberOfFields++;
    }
    public boolean getFieldType (){ return fieldPositiveEffect; }
    public int getFieldEffect (){ return fieldEffect; }

    public String toString() {
        return "Field " + fieldNumber  + ". " + fieldName;
    }
}
