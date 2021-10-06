package Game;

public class Field {
    private static int nextfieldNumber = 0; //for Matador, remember when initializing to make an empty field to let the actual fields start from 1
    private final int fieldNumber;
    private final String fieldName; //field description for this version of the dice game
    private int fieldEffect;
    private boolean fieldPositiveEffect; // false for withdraw, true for deposit

    public Field(String fieldName, boolean fieldPositiveEffect, int fieldEffect) {
        this.fieldName = fieldName;
        this.fieldNumber = nextfieldNumber;
        this.fieldPositiveEffect = fieldPositiveEffect;
        this.fieldEffect = fieldEffect;
        nextfieldNumber++;
    }
    public boolean getFieldType (){ return fieldPositiveEffect; }
    public int getFieldEffect (){ return fieldEffect; }

    public String toString() {
        return "Field " + fieldNumber  + ". " + fieldName;
    }
}
