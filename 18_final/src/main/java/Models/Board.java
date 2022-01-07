package Models;

import Services.FieldsCreator;

//This code has been modified from previous assignment CDIO 3 by Maj Kyllesbech, Gabriel H, Kierkegaard, Mark Bidstrup & Xiao Chen handed in 26. November 2021

public class Board {
    private Field[] fields;

    public Board() {
        FieldsCreator service = new FieldsCreator();
        fields = service.createFields();
    }

    public void buildHouse(){}
    public void buildHotel(){}
    public void sellHouse(){}

    // checks if a player owns all the fields of a given type
    // field type refers to shippingfield, or company, or street color
    public boolean ownsAllFieldsOfSameType(int fieldArrayNum){
        boolean test = false;
        String streetColor = "";
        String fieldClassName = fields[fieldArrayNum].getClass().toString();
        int ownernum = ((OwnableField) fields[fieldArrayNum]).getOwnerNum();
        if (fields[fieldArrayNum].isStreetField()) // updates color, if the field is a street
            streetColor = ((StreetField) fields[fieldArrayNum]).getStreetColor();
        for (int i = 0; i< fields.length; i++) {
            // checks other fields of the same class name, if they are of the same type, and have the same owner
            if (fields[i].getClass().toString().equalsIgnoreCase(fieldClassName) && i != fieldArrayNum) {
                // checks shipping and breweries
                if (streetColor.equalsIgnoreCase("")) {
                    if (((OwnableField) fields[i]).getOwnerNum() == ownernum && ownernum != 0)
                        test = true;
                    else
                        return false;
                }
                // checks streets of same color
                else if (streetColor.equalsIgnoreCase(((StreetField) fields[i]).getStreetColor())) {
                    if (((OwnableField) fields[i]).getOwnerNum() == ownernum && ownernum != 0)
                        test = true;
                    else
                        return false;
                }
            }
        }
        return test;
    }

    public void updateRentForAllFieldsOfSameType(int fieldArrayNum){
        // updates rent for the field itself and all other fields of the same type
        // field type refers to shippingfield, or company, or color of a lot
        String fieldClassName = fields[fieldArrayNum].getClass().toString();
        String streetColor = "";
        int ownernum = ((OwnableField) fields[fieldArrayNum]).getOwnerNum();
        if (fields[fieldArrayNum].isStreetField()) // if the field is a street
            streetColor = ((StreetField) fields[fieldArrayNum]).getStreetColor();
        if (ownsAllFieldsOfSameType(fieldArrayNum) || fields[fieldArrayNum].isShippingField())
        // updates rent for multiple fields
        // note: rent needs to be updated for multiple shipping fields regardless of whether player owns all the shipping fields
        {
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getClass().toString().equalsIgnoreCase(fieldClassName)) {
                    // updates rent for other shipping and brewery fields
                    // needs to check that ownernum is the same for the shipping fields
                    if (streetColor.equalsIgnoreCase("") && ownernum == ((OwnableField) fields[i]).getOwnerNum())
                        ((OwnableField) fields[i]).updateRent();
                        // updates rent for streets of the same color
                        // ownernum has already been verified to be the same as ownsAllFieldsOfSameType is true
                    else if (streetColor.equalsIgnoreCase(((StreetField) fields[i]).getStreetColor()))
                        ((OwnableField) fields[i]).updateRent();
                }
            }
        }
        // following line of code updates rent for one field only
        else ((OwnableField) fields[fieldArrayNum]).updateRent();
    }

    public int numOfShippingFieldsOwned(int playerNum){
        int numberOfShippingFieldsOwned = 0;
        for (int i = 0; i< fields.length; i++) {
            // counts how many shipping fields the player owns in total
            if (fields[i].isShippingField()) {
                if (((OwnableField) fields[i]).getOwnerNum() == playerNum && playerNum != 0)
                    numberOfShippingFieldsOwned++;
            }
        }
        return numberOfShippingFieldsOwned;
    }

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

    public int getTotalNumOfFields(){
        return fields.length;
    }
}
