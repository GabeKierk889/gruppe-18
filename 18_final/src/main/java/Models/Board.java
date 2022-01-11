package Models;

import Controllers.GameController;
import Controllers.ViewController;
import Services.FieldsCreator;

//This code has been modified from previous assignment CDIO 3 by Maj Kyllesbech, Gabriel H, Kierkegaard, Mark Bidstrup & Xiao Chen handed in 26. November 2021

public class Board {
    private Field[] fields;

    public Board() {
        FieldsCreator service = new FieldsCreator();
        fields = service.createFields();
    }

    public void buildHouse(){
        int currentPlayer = GameController.getInstance().getCurrentPlayerNum();
        // viewcontroller method
    }

    public void buildHotel(){}
    public void sellHouse(){}
    public void sellHotel(){}

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
                    else if (field.isStreetField() && streetColor.equalsIgnoreCase(((StreetField) field).getStreetColor()))
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
                housesValueAsNew += ((StreetField) field).getNumOfHouses() * ((StreetField) field).getHousePrice();
                if (((StreetField) field).hasHotel())
                    // the price of a new hotel is the equivalent of housePrice * (MaxNumHousesOn1Field + 1)
                    hotelsValueAsNew += ((StreetField) field).getHousePrice() * (1 + StreetField.MAXNUMOFHOUSES);
            }
        }
        totalValueAsNew = housesValueAsNew + hotelsValueAsNew;
        return (int) Math.round(totalValueAsNew * GameSettings.HOUSE_RESELL_VALUE_MULTIPLIER);
    }

    // removes all buildings owned by player
    public void removeAllBuildingsOwned(int playerNum) {
        for (int i = 0; i < fields.length; i++) {
            // checks for streetFields owned by player and removes all buildings
            if (fields[i].isStreetField() && ((OwnableField) fields[i]).getOwnerNum() == playerNum) {
                ((StreetField) fields[i]).setNumOfHouses(0);
                ((StreetField) fields[i]).setHasHotel(false);
                ViewController.getInstance().setGUIHasHotel(i, false);
                ViewController.getInstance().setGUINumHouses(i,0);
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
                    totalValue += ((OwnableField) field).getFieldPrice();
            }
        }
        return totalValue;
    }

    // calculates how much extra money player can raise by putting all non-mortgaged fields on mortgage
    public int calculateAvailableMortgageValueOfFieldsOwned(int playerNum) {
        int totalValue = 0;
        for (Field field : fields) {
            // checks for fields owned by player and calculates available mortgage value
            if (field.isOwnableField() && ((OwnableField) field).getOwnerNum() == playerNum) {
                if (!((OwnableField) field).isMortgaged)
                    totalValue += ((OwnableField) field).MORTGAGEVALUE;
            }
        }
        return totalValue;
    }

    public void bankruptcyTransferAllFieldAssets(int oldOwnerPlayerNum, int newOwnerPlayerNum) {
        // this method should only called when a player goes bankrupt and needs to transfer ownership of all fields
        for (Field field : fields) {
            if (field.isOwnableField() && ((OwnableField) field).getOwnerNum() == oldOwnerPlayerNum) {
                ((OwnableField) field).setOwnerNum(newOwnerPlayerNum);
                // if the creditor/ new owner of the fields is the bank, unmortgage the fields and sell the fields on auction
                if (newOwnerPlayerNum == 0) {
                    ((OwnableField) field).setMortgageStatus(false);
                    ((OwnableField) field).auctionField();
                }
            }
        }
        // if ownership is transferred to another player (not the bank), update the rents
        // (the auction method will update the rents after each auction, so does not need to be done twice)
        if (newOwnerPlayerNum != 0)
            for (Field field : fields) {
                if (field.isOwnableField() && ((OwnableField) field).getOwnerNum() == newOwnerPlayerNum) {
                    ((OwnableField) field).updateRent();
                    ViewController.getInstance().formatFieldBorder(newOwnerPlayerNum);
                }
            }
    }

    public boolean currentPlayerMayBuyHouses() {
        int currentPlayer = GameController.getInstance().getCurrentPlayerNum();
        for (int i = 0; i< fields.length; i++) {
            // checks for streets owned by current player
            if (fields[i].isStreetField()
                    && ((OwnableField) fields[i]).getOwnerNum() == currentPlayer
                    // check that there aren't hotels or 4 houses on all the relevant fields already
                    && !(((StreetField) fields[i]).hasHotel() )
                    && (((StreetField) fields[i]).getNumOfHouses() < StreetField.MAXNUMOFHOUSES ) ) {
                if (ownsAllFieldsOfSameType(i))
                    return true;
            }
        }
        return false;
    }

    public boolean currentPlayerMaySellHouses() {
        int[] buildingsOwned = numBuildingsOwnedByCurrentPlayer();
        return buildingsOwned[0] > 0; // if they own any houses
    }

    public boolean currentPlayerMayBuyHotels() {
        for (int i = 0; i< fields.length; i++) {
            if (currentPlayerMayBuyHotelsOnField(i))
                return true;
        }
        return false;
    }

    public boolean currentPlayerMaySellHotels() {
        int[] buildingsOwned = numBuildingsOwnedByCurrentPlayer();
        return buildingsOwned[1] > 0; // if they own any hotels
    }

    public boolean currentPlayerMayBuyHotelsOnField(int fieldArrayNum) {
        boolean test = false;
        int currentPlayer = GameController.getInstance().getCurrentPlayerNum();
        if (fields[fieldArrayNum].isStreetField()
                && ((OwnableField) fields[fieldArrayNum]).getOwnerNum() == currentPlayer
                && (((StreetField) fields[fieldArrayNum]).getNumOfHouses() == 4 )
                && ownsAllFieldsOfSameType(fieldArrayNum) )
        { // checks other fields of the same color if they also have 4 houses or even a hotel built already
            String color = ((StreetField) fields[fieldArrayNum]).getStreetColor();
            for (Field field : fields) {
                if (field.isStreetField() && color.equalsIgnoreCase(((StreetField) field).getStreetColor())) {
                    if (((StreetField) field).getNumOfHouses() == 4 || ((StreetField) field).hasHotel()) {
                        test = true;
                    } else return false;
                }
            }
        }
        return test;
    }

    public void currentPlayerBuildingDecision() { // uses gui to get user input on buy/sell buildings decision
        String userinput = "";
        String userDoesNotWantToBuyResponse = ViewController.getInstance().getTakeTurnGUIMessages(16);
        // depending on the player's situation, need gui to only display the relevant options available for player
        boolean mayBuyHouse,maySellHouse,mayBuyHotel,maySellHotel,showAll, allExceptSellHotel, allExceptBuyHotel, allExceptBuyHouse, buyHouseSellHouse,
                buyHouseSellHotel, sellHouseSellHotel, sellHouseBuyHotel, buyHouseOnly,sellHouseOnly,sellHotelOnly;
        while (!userinput.equalsIgnoreCase(userDoesNotWantToBuyResponse)) {
            mayBuyHouse = currentPlayerMayBuyHouses();
            maySellHouse = currentPlayerMaySellHouses();
            mayBuyHotel = currentPlayerMayBuyHotels();
            maySellHotel = currentPlayerMaySellHotels();
            showAll = mayBuyHouse && maySellHouse && mayBuyHotel && maySellHotel;
            allExceptSellHotel = mayBuyHouse && maySellHouse && mayBuyHotel && !maySellHotel;
            allExceptBuyHotel = mayBuyHouse && maySellHouse && !mayBuyHotel && maySellHotel;
            allExceptBuyHouse = !mayBuyHouse && maySellHouse && mayBuyHotel && maySellHotel;
            buyHouseSellHouse = mayBuyHouse && maySellHouse && !mayBuyHotel && !maySellHotel;
            buyHouseSellHotel = mayBuyHouse && !maySellHouse && !mayBuyHotel && maySellHotel;
            sellHouseSellHotel = !mayBuyHouse && maySellHouse && !mayBuyHotel && maySellHotel;
            sellHouseBuyHotel = !mayBuyHouse && maySellHouse && mayBuyHotel && !maySellHotel;
            buyHouseOnly = mayBuyHouse && !maySellHouse && !mayBuyHotel && !maySellHotel;
            sellHouseOnly = !mayBuyHouse && maySellHouse && !mayBuyHotel && !maySellHotel;
            sellHotelOnly = !mayBuyHouse && !maySellHouse && !mayBuyHotel && maySellHotel;

            if (showAll)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(56,57,56,57,25,25,27,27);
            else if (allExceptSellHotel)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(56,57,56,25,25,27);
            else if (allExceptBuyHotel)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(56,57,57,25,25,27);
            else if (allExceptBuyHouse)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(57,56,57,25,27,27);
            else if (buyHouseSellHouse)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(56,57,25,25);
            else if (buyHouseSellHotel)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(56,57,25,27);
            else if (sellHouseSellHotel)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(57,57,25,27);
            else if (sellHouseBuyHotel)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(57,56,25,27);
            else if (buyHouseOnly)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(56,25);
            else if (sellHouseOnly)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(57,25);
            else if (sellHotelOnly)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(57,27);
            else
                userinput = userDoesNotWantToBuyResponse;
            // calls the relevant methods based on user response
            if (!userinput.equals(userDoesNotWantToBuyResponse)) {
                String buyHouseDecision = ViewController.getInstance().getTakeTurnGUIMessages(56,25);
                String sellHouseDecision = ViewController.getInstance().getTakeTurnGUIMessages(57,25);
                String buyHotelDecision = ViewController.getInstance().getTakeTurnGUIMessages(56,27);
                String sellHotelDecision = ViewController.getInstance().getTakeTurnGUIMessages(57,27);
                if (userinput.equalsIgnoreCase(buyHouseDecision))
                    buildHouse();
                else if (userinput.equalsIgnoreCase(sellHouseDecision))
                    sellHouse();
                else if (userinput.equalsIgnoreCase(buyHotelDecision))
                    buildHotel();
                else if (userinput.equalsIgnoreCase(sellHotelDecision))
                    sellHotel();
            }
        }
    }

    public int getTotalNumOfFields(){
        return fields.length;
    }
}
