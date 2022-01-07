package Models;

import Services.FieldsCreator;

public class Board {
    private Field[] fields;

    public Board() {
        FieldsCreator service = new FieldsCreator();
        fields = service.createFields();
    }

    public boolean ownsAllOfSameType(int fieldArrayNum){
        return false;
    }
    public void updateRentForAllOfTheSameType(int fieldArrayNum){}
    public int numOfShippingFieldsOwned(int playerNum){
        return 0;
    }
    public int getTotalNumOfFields(){
        return fields.length;
    }

    public void buildHouse(){}
    public void buildHotel(){}
    public void sellHouse(){}

    public Field getFieldObject(int arrayindex) {
        return fields[arrayindex];
    }


    public int getFieldArrayNumber(String fieldName) {
        // returns the field array number of the specified field name
        // returns -1 if the field cannot be found;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getFieldName().equalsIgnoreCase(fieldName)) {
                return i;
            }
        }
        return -1;
    }
}
