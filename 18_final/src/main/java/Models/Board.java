package Models;

import Controllers.GameController;
import Services.FieldsCreator;

//This code has been modified from previous assignment CDIO 3 by Maj Kyllesbech, Gabriel H, Kierkegaard, Mark Bidstrup & Xiao Chen handed in 26. November 2021

public class Board {
    private Field[] fields;

    public Board() {
        FieldsCreator service = new FieldsCreator();
        fields = service.createFields();
    }

    // TODO: Forsøg på at lave buildHouse()
    public void buildHouse(){
        // Hent hus pris
        int currentPlayer = GameController.getInstance().getCurrentPlayerNum();
        // TODO: Problem herunder
//        int currentField = GameController.getInstance().getPlayerObject(currentPlayer).OnField();
//        System.out.println(currentField);
//        int[] currentHousePrice = fieldsDataReader.getHousePriceArray(currentField);
        // Køb hus
        // Træk penge
//        GameController.getInstance().getPlayerObject(currentPlayer).getAccount().withdrawMoney(currentHousePrice);
        // Opsæt hus
    }

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
        for (Field field : fields) {
            // checks any fields of the same class name, if they are of the same type, and have the same owner
            if (field.getClass().toString().equalsIgnoreCase(fieldClassName)) {
                // if condition only runs if it is either a shipping or brewery field, or if it is a streetField of same color
                if (!fields[fieldArrayNum].isStreetField() ||
                    (fields[fieldArrayNum].isStreetField() && streetColor.equalsIgnoreCase(((StreetField) field).getStreetColor())) ) {
                    if (((OwnableField) field).getOwnerNum() == ownernum && ownernum != 0)
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
            for (Field field : fields) {
                if (field.getClass().toString().equalsIgnoreCase(fieldClassName) && ownernum == ((OwnableField) field).getOwnerNum()) {
                    if (!field.isStreetField())
                        ((OwnableField) field).updateRent();
                    else if (streetColor.equalsIgnoreCase(((StreetField) field).getStreetColor()))
                        ((OwnableField) field).updateRent();
                }
            }
        }
        // following line of code updates rent for one field only
        else ((OwnableField) fields[fieldArrayNum]).updateRent();
    }

    public int numOfShippingFieldsOwned(int playerNum){
        int numberOfShippingFieldsOwned = 0;
        for (Field field : fields) {
            // counts how many shipping fields the player owns in total
            if (field.isShippingField()) {
                if (((OwnableField) field).getOwnerNum() == playerNum && playerNum != 0)
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

    public int[] numBuildingsOwnedByCurrentPlayer() {
        // stores data in an array with length 2, first numofhouses, then numofhotels
        int[] buildingsOwned = new int[2];
        int currentPlayer = GameController.getInstance().getCurrentPlayerNum();
        for (Field field : fields) {
            // checks for streetFields owned by current player
            if (field.isStreetField() && ((OwnableField) field).getOwnerNum() == currentPlayer) {
                buildingsOwned[0] += ((StreetField) field).getNumOfHouses();
                if (((StreetField) field).hasHotel())
                    buildingsOwned[1]++;
            }
        }
        return buildingsOwned;
    }

    // calculates the asset/ resell value of all buildings owned by player
    public int calculateAssetValueOfBuildingsOwned(int playerNum) {
        int housesValueAsNew, hotelsValueAsNew, totalValueAsNew;
        housesValueAsNew = 0; hotelsValueAsNew = 0;
        for (Field field : fields) {
            // checks for streetFields owned by player and calculates asset/resell value of buildings
            if (field.isStreetField() && ((OwnableField) field).getOwnerNum() == playerNum) {
                housesValueAsNew += ((StreetField) field).getNumOfHouses() * ((StreetField) field).getHOUSEPRICE();
                if (((StreetField) field).hasHotel())
                    // the price of a new hotel is the equivalent of housePrice * (MaxNumHousesOn1Field + 1)
                    hotelsValueAsNew += ((StreetField) field).getHOUSEPRICE() * (1 + StreetField.MAXNUMOFHOUSES);
            }
        }
        totalValueAsNew = housesValueAsNew + hotelsValueAsNew;
        return (int) Math.round(totalValueAsNew * GameSettings.HOUSE_RESELL_VALUE_MULTIPLIER);
    }

    // removes all buildings owned by player
    public void removeAllBuildingsOwned(int playerNum) {
        for (Field field : fields) {
            // checks for streetFields owned by player and removes all buildings
            if (field.isStreetField() && ((OwnableField) field).getOwnerNum() == playerNum) {
                ((StreetField) field).setNumOfHouses(0);
                ((StreetField) field).setHasHotel(false);
            }
        }
    }

    // calculates the asset value of all ownable fields owned by player
    public int calculateValueOfFieldsOwned(int playerNum) {
        int totalValue = 0;
        for (Field field : fields) {
            // checks for fields owned by player and calculates total asset value of ownable fields
            if (field.isOwnableField() && ((OwnableField) field).getOwnerNum() == playerNum) {
                if (((OwnableField) field).isMortgaged)
                    totalValue += ((OwnableField) field).MORTGAGEVALUE;
                else
                    totalValue += ((OwnableField) field).getPRICE();
            }
        }
        return totalValue;
    }

    public void bankruptcyTransferAllFieldAssets(int oldOwnerPlayerNum, int newOwnerPlayerNum) {
        // this method should only called when a player goes bankrupt and needs to transfer ownership of all fields
        for (Field field : fields) {
            if (field.isOwnableField() && ((OwnableField) field).getOwnerNum() == oldOwnerPlayerNum) {
                ((OwnableField) field).setOwnerNum(newOwnerPlayerNum);
                // if the creditor/ new owner of the fields is the bank, hold auction
                if (newOwnerPlayerNum == 0)
                    ((OwnableField) field).auctionField();
            }
        }
        // if ownership is transferred to another player (not the bank), update the rents
        // (the auction method will update the rents after each auction, so does not need to be done twice)
        if (newOwnerPlayerNum != 0)
            for (Field field : fields) {
                if (field.isOwnableField() && ((OwnableField) field).getOwnerNum() == newOwnerPlayerNum)
                    ((OwnableField) field).updateRent();
            }
    }

    public int getTotalNumOfFields(){
        return fields.length;
    }
}
