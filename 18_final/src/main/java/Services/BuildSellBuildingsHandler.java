package Services;

import Controllers.GameController;
import Controllers.ViewController;
import Models.Field;
import Models.GameSettings;
import Models.OwnableField;
import Models.StreetField;

public class BuildSellBuildingsHandler {
    Field[] fields;

    public BuildSellBuildingsHandler(Field[] fields) {
        this.fields = fields;
    }

    private boolean currentPlayerMayBuyHouses() {
        int currentPlayer = GameController.getInstance().getCurrentPlayerNum();
        for (int i = 0; i < fields.length; i++) {
            // checks for streets owned by current player
            if (fields[i].isStreetField()
                    && ((OwnableField) fields[i]).getOwnerNum() == currentPlayer
                    // check that there aren't hotels or 4 houses on all the relevant fields already
                    && !(((StreetField) fields[i]).hasHotel())
                    && (((StreetField) fields[i]).getNumOfHouses() < StreetField.MAXNUMOFHOUSES)) {
                if (GameController.getInstance().getBoard().ownsAllFieldsOfSameType(i))
                    return true;
            }
        }
        return false;
    }

    public boolean currentPlayerMayBuyHouseOnField(int fieldArrayNum) {
        int currentPlayer = GameController.getInstance().getCurrentPlayerNum();
        if (fields[fieldArrayNum].isStreetField()
                && ((OwnableField) fields[fieldArrayNum]).getOwnerNum() == currentPlayer
                // check that there aren't hotels or 4 houses on the field already
                && !(((StreetField) fields[fieldArrayNum]).hasHotel())
                && (((StreetField) fields[fieldArrayNum]).getNumOfHouses() < StreetField.MAXNUMOFHOUSES)) {
            return GameController.getInstance().getBoard().ownsAllFieldsOfSameType(fieldArrayNum);
        }
        return false;
    }

    public boolean currentPlayerMaySellHouses() {
        int[] buildingsOwned = GameController.getInstance().getBoard().numBuildingsOwnedByCurrentPlayer();
        return buildingsOwned[0] > 0; // if they own any houses
    }

    public boolean currentPlayerMayBuyHotels() {
        for (int i = 0; i < fields.length; i++) {
            if (currentPlayerMayBuyHotelOnField(i))
                return true;
        }
        return false;
    }

    public boolean currentPlayerMaySellHotels() {
        int[] buildingsOwned = GameController.getInstance().getBoard().numBuildingsOwnedByCurrentPlayer();
        return buildingsOwned[1] > 0; // if they own any hotels
    }

    public boolean currentPlayerMayBuyHotelOnField(int fieldArrayNum) {
        boolean test = false;
        int currentPlayer = GameController.getInstance().getCurrentPlayerNum();
        if (fields[fieldArrayNum].isStreetField()
                && ((OwnableField) fields[fieldArrayNum]).getOwnerNum() == currentPlayer
                && (((StreetField) fields[fieldArrayNum]).getNumOfHouses() == 4)
                && GameController.getInstance().getBoard().ownsAllFieldsOfSameType(fieldArrayNum)) {
            String color = ((StreetField) fields[fieldArrayNum]).getStreetColor();
            // checks other fields of the same color if they also have 4 houses or even a hotel built already
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
        boolean mayBuyHouse, maySellHouse, mayBuyHotel, maySellHotel, showAll, allExceptSellHotel, allExceptBuyHotel, allExceptBuyHouse, buyHouseSellHouse,
                buyHouseSellHotel, sellHouseSellHotel, sellHouseBuyHotel, buyHouseOnly, sellHouseOnly, sellHotelOnly;
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
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(56, 57, 56, 57, 25, 25, 27, 27);
            else if (allExceptSellHotel)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(56, 57, 56, 25, 25, 27);
            else if (allExceptBuyHotel)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(56, 57, 57, 25, 25, 27);
            else if (allExceptBuyHouse)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(57, 56, 57, 25, 27, 27);
            else if (buyHouseSellHouse)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(56, 57, 25, 25);
            else if (buyHouseSellHotel)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(56, 57, 25, 27);
            else if (sellHouseSellHotel)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(57, 57, 25, 27);
            else if (sellHouseBuyHotel)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(57, 56, 25, 27);
            else if (buyHouseOnly)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(56, 25);
            else if (sellHouseOnly)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(57, 25);
            else if (sellHotelOnly)
                userinput = ViewController.getInstance().getBuyOrSellBuildingsUserInput(57, 27);
            else
                userinput = userDoesNotWantToBuyResponse;
            // calls the relevant methods based on user response
            if (!userinput.equals(userDoesNotWantToBuyResponse)) {
                String buyHouseDecision = ViewController.getInstance().getTakeTurnGUIMessages(56, 25);
                String sellHouseDecision = ViewController.getInstance().getTakeTurnGUIMessages(57, 25);
                String buyHotelDecision = ViewController.getInstance().getTakeTurnGUIMessages(56, 27);
                String sellHotelDecision = ViewController.getInstance().getTakeTurnGUIMessages(57, 27);
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

    private String[] getStreetColorsEligibleForBuildingHouse() {
        // returns an array of street colors where player may build houses
        String[] temp = new String[fields.length];
        String lastColor = "";
        int numDifferentColors = 0;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isStreetField() && currentPlayerMayBuyHouseOnField(i)) {
                temp[i] = ((StreetField) fields[i]).getStreetColor();
                if (!temp[i].equalsIgnoreCase(lastColor)) {
                    numDifferentColors++;
                    lastColor = temp[i];
                }
            }
        }
        return getStreetColors(temp, numDifferentColors);
    }

    private String[] getStreetColorsEligibleForBuildingHotel() {
        // returns an array of street colors where player may build hotels
        String[] temp = new String[fields.length];
        String lastColor = "";
        int numDifferentColors = 0;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isStreetField() && currentPlayerMayBuyHotelOnField(i)) {
                temp[i] = ((StreetField) fields[i]).getStreetColor();
                if (!temp[i].equalsIgnoreCase(lastColor)) {
                    numDifferentColors++;
                    lastColor = temp[i];
                }
            }
        }
        return getStreetColors(temp, numDifferentColors);
    }

    private String[] getStreetColors(String[] temp, int numDifferentColors) {
        String lastColor;
        String[] result = new String[numDifferentColors];
        lastColor = "";
        int counter = 0;
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != null && !temp[i].equalsIgnoreCase(lastColor) && counter < numDifferentColors) {
                lastColor = temp[i];
                result[counter] = temp[i];
                counter++;
            }
        }
        return result;
    }

    private String extractColor(String str) {
        String result = "";
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isStreetField()) {
                if (str.contains(((StreetField) fields[i]).getStreetColor())) {
                    result = ((StreetField) fields[i]).getStreetColor();
                }
            }
        }
        return result;
    }

    public void buildHouse() {
        int currentPlayer = GameController.getInstance().getCurrentPlayerNum();
        String[] colorsEligibleForBuilding = getStreetColorsEligibleForBuildingHouse();
        String userSelection = ViewController.getInstance().whereToBuildUserInput(colorsEligibleForBuilding);
        String userSelectionColor = extractColor(userSelection);
        int pricePerHouse = 0;
        int[] totalHousesOnField = new int[fields.length];
        int[] housesToBuild = new int[fields.length];
        boolean[] fieldHasHotel = new boolean[fields.length];
        boolean succesfulCompletion = false;
        do {
            int maxNumHouses = 0;
            int minNumHouses = StreetField.MAXNUMOFHOUSES + 1;
            int totalNewBuilds = 0;
            int counter = 0;
            for (Field field : fields) {
                // runs for all fields of the same color
                if (field.isStreetField() && userSelectionColor.equalsIgnoreCase(((StreetField) field).getStreetColor())) {
                    fieldHasHotel[counter] = ((StreetField) field).hasHotel();
                    pricePerHouse = ((StreetField) field).getHousePrice();
                    if (!fieldHasHotel[counter] && ((StreetField) field).getNumOfHouses() < StreetField.MAXNUMOFHOUSES)
                        housesToBuild[counter] = ViewController.getInstance().numberHousesToBuildUserInput(field.getFieldName(), pricePerHouse);
                    totalHousesOnField[counter] = housesToBuild[counter] + ((StreetField) field).getNumOfHouses();
                    totalNewBuilds += housesToBuild[counter];
                    // check: for streets of the same color, what is the min/max number houses on any one lot
                    // if there is a hotel, there will be 0 houses, but ignore this in minimum calculation
                    if (!fieldHasHotel[counter] && totalHousesOnField[counter] < minNumHouses)
                        minNumHouses = totalHousesOnField[counter];
                    if (totalHousesOnField[counter] > maxNumHouses)
                        maxNumHouses = totalHousesOnField[counter];
                    counter++;
                }
            }
            if (maxNumHouses > StreetField.MAXNUMOFHOUSES) // too many houses on one street error message
                ViewController.getInstance().showTakeTurnMessageWithPlayerName(60, 62, -1, "" + StreetField.MAXNUMOFHOUSES, "", "");
            else if (totalNewBuilds > 0 && maxNumHouses - minNumHouses > 1) // not evenly built error message
                ViewController.getInstance().showTakeTurnMessageWithPlayerName(61, 62, 47, "", "", "");
            else {
                int totalCost = totalNewBuilds * pricePerHouse;
                succesfulCompletion = true;
                if (totalNewBuilds == 0)
                    ViewController.getInstance().showTakeTurnMessageWithPlayerName(64, "", "", "");
                else {
                    // gives the player an opportunity to confirm the purchase of houses
                    if (!ViewController.getInstance().showMessageAndGetBooleanUserInput(65, 18, 46, "" + totalCost, "" + totalNewBuilds, 25)) // asks player to confirm
                        ViewController.getInstance().showTakeTurnMessageWithPlayerName(62, "", "", "");
                    else {
                        // build the house
                        counter = 0;
                        boolean playerMayNowBuildHotelsOnFieldsOfThisColor = false;
                        for (int i = 0; i < fields.length; i++) {
                            if (fields[i].isStreetField()) {
                                if (userSelectionColor.equalsIgnoreCase(((StreetField) fields[i]).getStreetColor())) {
                                    ((StreetField) fields[i]).setNumOfHouses(totalHousesOnField[counter]);
                                    ((StreetField) fields[i]).updateRent();
                                    ViewController.getInstance().setGUINumHouses(i, ((StreetField) fields[i]).getNumOfHouses());
                                    if (currentPlayerMayBuyHotelOnField(i))
                                        playerMayNowBuildHotelsOnFieldsOfThisColor = true;
                                    counter++;
                                }
                            }
                        }
                        GameController.getInstance().getPlayerObject(currentPlayer).getAccount().withdrawMoney(totalCost);
                        ViewController.getInstance().updateGUIBalance();
                        if (!GameController.getInstance().getPlayerObject(currentPlayer).getIsBankrupt()) {
                            String str; // stores the word for either "a house" or "houses"
                            if (totalNewBuilds > 1)
                                str = ViewController.getInstance().getTakeTurnGUIMessages(25);
                            else
                                str = ViewController.getInstance().getTakeTurnGUIMessages(24);
                            ViewController.getInstance().showTakeTurnMessageWithPlayerName(66, "" + totalNewBuilds, str, "" + totalCost);
                            boolean noHotel = false; // checks if there are any hotels already
                            for (boolean test : fieldHasHotel)
                                noHotel = !test;
                            if (noHotel && playerMayNowBuildHotelsOnFieldsOfThisColor) // display message to player that they may now build hotels
                                ViewController.getInstance().showTakeTurnMessageWithPlayerName(67, "" + StreetField.MAXNUMOFHOUSES, userSelectionColor, "");
                        }
                    }
                }
            }
        }
        while (!succesfulCompletion);
    }

    public void buildHotel() {
        // set num houses = 0 and hotel true
        // update gui
    }

    public void sellHouse() {
    }

    public void sellHotel() {
    }

}
