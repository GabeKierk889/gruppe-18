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

}
