package Services;

import Controllers.GameController;
import Controllers.ViewController;
import Controllers.ViewController_GUIMessages;
import Models.*;

public class BuildSellBuildingsHandler {
    Field[] fields;

    public BuildSellBuildingsHandler(Field[] fields) {
        this.fields = fields;
    }

    public void buildHouse() {
        int currentPlayer = GameController.getInstance().getCurrentPlayerNum();
        String[] colorsEligibleForBuilding = getStreetColorsEligibleForBuildingHouse();
        String userSelection = ViewController_GUIMessages.getInstance().whereToBuildUserInput(colorsEligibleForBuilding);
        String userSelectionColor = extractColor(userSelection);
        int pricePerHouse = 0;
        int[] totalHousesOnField = new int[fields.length];
        int[] housesToBuild = new int[fields.length];
        boolean[] fieldHasHotel = new boolean[fields.length];
        boolean successfulCompletion = false;
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
                        housesToBuild[counter] = ViewController_GUIMessages.getInstance().numberHousesToBuildUserInput(field.getFieldName(), pricePerHouse);
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
                ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(60, 62, -1, "" + StreetField.MAXNUMOFHOUSES, "", "");
            else if (totalNewBuilds > 0 && maxNumHouses - minNumHouses > 1) // not evenly built error message
                ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(61, 62, 47, "", "", "");
            else {
                int totalCost = totalNewBuilds * pricePerHouse;
                successfulCompletion = true;
                if (totalNewBuilds == 0)
                    ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(64, "", "", "");
                else {
                    // gives the player an opportunity to confirm the purchase of houses
                    int lineintext; // stores the word for either "a house" or "houses"
                    if (totalNewBuilds > 1)
                        lineintext = 25;
                    else
                        lineintext = 69;
                    if (!ViewController_GUIMessages.getInstance().showMessageAndGetBooleanUserInput(65, 15, 46, "" + totalCost, "" + totalNewBuilds, lineintext)) // asks player to confirm
                        ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(62, "", "", "");
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
                        String str; // stores the word for either "a house" or "houses"
                        if (totalNewBuilds > 1)
                            str = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(25);
                        else
                            str = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(69);
                        ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(66, "" + totalNewBuilds, str, "" + totalCost);
                        boolean noHotel = false; // checks if there are any hotels already
                        for (boolean test : fieldHasHotel)
                            noHotel = !test;
                        if (noHotel && playerMayNowBuildHotelsOnFieldsOfThisColor) // display message to player that they may now build hotels
                            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(67, "" + StreetField.MAXNUMOFHOUSES, userSelectionColor, "");
                        GameController.getInstance().getPlayerObject(currentPlayer).getAccount().withdrawMoney(totalCost);
                        ViewController.getInstance().updateGUIBalance();
                    }
                }
            }
        }
        while (!successfulCompletion);
    }

    public void buildHotel() {
        int currentPlayer = GameController.getInstance().getCurrentPlayerNum();
        String[] colorsEligibleForBuilding = getStreetColorsEligibleForBuildingHotel();
        String userSelection = ViewController_GUIMessages.getInstance().whereToBuildUserInput(colorsEligibleForBuilding);
        String userSelectionColor = extractColor(userSelection);
        int pricePerHouse = 0;
        boolean[] hotelsToBuild = new boolean[fields.length];
        boolean[] fieldHasHotel = new boolean[fields.length];
        int totalNewBuilds = 0;
        int counter = 0;
        for (Field field : fields) {
            // runs for all fields of the same color
            if (field.isStreetField() && userSelectionColor.equalsIgnoreCase(((StreetField) field).getStreetColor())) {
                fieldHasHotel[counter] = ((StreetField) field).hasHotel();
                pricePerHouse = ((StreetField) field).getHousePrice();
                if (!fieldHasHotel[counter] && ((StreetField) field).getNumOfHouses() == StreetField.MAXNUMOFHOUSES)
                    hotelsToBuild[counter] = ViewController_GUIMessages.getInstance().buildHotelUserInput(field.getFieldName(), pricePerHouse);
                if (hotelsToBuild[counter])
                    totalNewBuilds++;
                counter++;
            }
        }
        int totalCost = totalNewBuilds * pricePerHouse;
        if (totalNewBuilds == 0)
            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(64, "", "", "");
        else {
            // gives the player an opportunity to confirm the purchase of hotels
            int lineintext; // stores the word for either "a hotel" or "hotels"
            if (totalNewBuilds > 1)
                lineintext = 27;
            else
                lineintext = 70;
            if (!ViewController_GUIMessages.getInstance().showMessageAndGetBooleanUserInput(65, 15, 46, "" + totalCost, "" + totalNewBuilds, lineintext)) // asks player to confirm
                ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(62, "", "", "");
            else {
                // build the hotel
                counter = 0;
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].isStreetField() && userSelectionColor.equalsIgnoreCase(((StreetField) fields[i]).getStreetColor())) {
                        if (hotelsToBuild[counter]) {
                        ((StreetField) fields[i]).setHasHotel(true);
                        ((StreetField) fields[i]).setNumOfHouses(0);
                        ((StreetField) fields[i]).updateRent();
                        ViewController.getInstance().setGUIHasHotel(i, true); }
                        counter++;
                    }
                }
                String str; // stores the word for either "a hotel" or "hotels"
                if (totalNewBuilds > 1)
                    str = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(27);
                else
                    str = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(70);
                ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(66, "" + totalNewBuilds, str, "" + totalCost);
                GameController.getInstance().getPlayerObject(currentPlayer).getAccount().withdrawMoney(totalCost);
                ViewController.getInstance().updateGUIBalance();
            }
        }
    }

    public void sellHouse(int playerNum) {
        String[] colorsEligibleForSelling = getStreetColorsEligibleForSellingHouse(playerNum);
        String userSelection = ViewController_GUIMessages.getInstance().whereToUnBuildUserInput(colorsEligibleForSelling);
        String userSelectionColor = extractColor(userSelection);
        int resellvaluePerHouse = 0;
        int[] housesToSell = new int[fields.length];
        int[] totalHousesOnField = new int[fields.length];
        int totalBuildingsSold = 0;
        int counter = 0;
        for (Field field : fields) {
            // runs for all fields of the same color
            if (field.isStreetField() && userSelectionColor.equalsIgnoreCase(((StreetField) field).getStreetColor())) {
                resellvaluePerHouse = (int) Math.round(((StreetField) field).getHousePrice() * GameSettings.HOUSE_RESELL_VALUE_MULTIPLIER);
                if (((StreetField) field).getNumOfHouses() > 0)
                    housesToSell[counter] = ViewController_GUIMessages.getInstance().numberHousesToSellUserInput(field.getFieldName(), resellvaluePerHouse,((StreetField) field).getNumOfHouses());
                if (housesToSell[counter] > 0) {
                    totalHousesOnField[counter] = ((StreetField) field).getNumOfHouses() - housesToSell[counter];
                    totalBuildingsSold = housesToSell[counter];
                }
                counter++;
            }
        }
        int totalMoneyEarned = totalBuildingsSold * resellvaluePerHouse;
        if (totalBuildingsSold == 0)
            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(73, "", "", "");
        else {
            // gives the player an opportunity to confirm the sale of building
            int lineintext; // stores the word for either "a house" or "houses"
            if (totalBuildingsSold > 1)
                lineintext = 25;
            else
                lineintext = 69;
            if (!ViewController_GUIMessages.getInstance().showMessageAndGetBooleanUserInput(75, 15, 46, "" + totalMoneyEarned, "" + totalBuildingsSold, lineintext)) // asks player to confirm
                ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(74, "", "", "");
            else {
                // sell the house
                counter = 0;
                int fieldnum = 0;
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].isStreetField() && userSelectionColor.equalsIgnoreCase(((StreetField) fields[i]).getStreetColor())) {
                        fieldnum = i;
                        if (housesToSell[counter] > 0) {
                            ((StreetField) fields[i]).setNumOfHouses(totalHousesOnField[counter]);
                            ViewController.getInstance().setGUINumHouses(i, totalHousesOnField[counter]); }
                        counter++;
                    }
                }
                GameController.getInstance().getBoard().updateRentForAllFieldsOfSameType(fieldnum);
                GameController.getInstance().getPlayerObject(playerNum).getAccount().depositMoney(totalMoneyEarned);
                ViewController.getInstance().updateGUIBalance();
                String str; // stores the word for either "a house" or "houses"
                if (totalBuildingsSold > 1)
                    str = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(25);
                else
                    str = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(69);
                ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(76, "" + totalBuildingsSold, str, "" + totalMoneyEarned);
            }
        }
    }

    public void sellHotel(int playerNum) {
        String[] colorsEligibleForSelling = getStreetColorsEligibleForSellingHotel(playerNum);
        String userSelection = ViewController_GUIMessages.getInstance().whereToUnBuildUserInput(colorsEligibleForSelling);
        String userSelectionColor = extractColor(userSelection);
        int resellvaluePerHotel = 0;
        boolean[] hotelsToSell = new boolean[fields.length];
        boolean[] fieldHasHotel = new boolean[fields.length];
        int totalBuildingsSold = 0;
        int counter = 0;
        for (Field field : fields) {
            // runs for all fields of the same color
            if (field.isStreetField() && userSelectionColor.equalsIgnoreCase(((StreetField) field).getStreetColor())) {
                fieldHasHotel[counter] = ((StreetField) field).hasHotel();
                resellvaluePerHotel = (int) Math.round(((StreetField) field).getHousePrice() * (StreetField.MAXNUMOFHOUSES+1) * GameSettings.HOUSE_RESELL_VALUE_MULTIPLIER);
                if (fieldHasHotel[counter])
                    hotelsToSell[counter] = ViewController_GUIMessages.getInstance().sellHotelUserInput(field.getFieldName(), resellvaluePerHotel);
                if (hotelsToSell[counter])
                    totalBuildingsSold++;
                counter++;
            }
        }
        int totalMoneyEarned = totalBuildingsSold * resellvaluePerHotel;
        if (totalBuildingsSold == 0)
            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(73, "", "", "");
        else {
            // gives the player an opportunity to confirm the sale of hotels
            int lineintext; // stores the word for either "a hotel" or "hotels"
            if (totalBuildingsSold > 1)
                lineintext = 27;
            else
                lineintext = 70;
            if (!ViewController_GUIMessages.getInstance().showMessageAndGetBooleanUserInput(75, 15, 46, "" + totalMoneyEarned, "" + totalBuildingsSold, lineintext)) // asks player to confirm
                ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(74, "", "", "");
            else {
                // sell the hotel
                counter = 0;
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].isStreetField() && userSelectionColor.equalsIgnoreCase(((StreetField) fields[i]).getStreetColor())) {
                        if (hotelsToSell[counter]) {
                            ((StreetField) fields[i]).setHasHotel(false);
                            ((StreetField) fields[i]).updateRent();
                            ViewController.getInstance().setGUIHasHotel(i, false); }
                        counter++;
                    }
                }
                GameController.getInstance().getPlayerObject(playerNum).getAccount().depositMoney(totalMoneyEarned);
                ViewController.getInstance().updateGUIBalance();
                String str; // stores the word for either "a hotel" or "hotels"
                if (totalBuildingsSold > 1)
                    str = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(27);
                else
                    str = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(70);
                ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(76, "" + totalBuildingsSold, str, "" + totalMoneyEarned);
            }
        }
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

    public boolean playerMaySellHouses(int playernum) {
        int[] buildingsOwned = GameController.getInstance().getBoard().numBuildingsOwnedByPlayer(playernum);
        return buildingsOwned[0] > 0; // if they own any houses
    }

    public boolean currentPlayerMayBuyHotels() {
        for (int i = 0; i < fields.length; i++) {
            if (currentPlayerMayBuyHotelOnField(i))
                return true;
        }
        return false;
    }

    public boolean playerMaySellHotels(int playerNum) {
        int[] buildingsOwned = GameController.getInstance().getBoard().numBuildingsOwnedByPlayer(playerNum);
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

    public boolean playerMayBuySellHotelOnField(int fieldArrayNum, int playerNum) {
        return  fields[fieldArrayNum].isStreetField()
                && ((OwnableField) fields[fieldArrayNum]).getOwnerNum() == playerNum
                && (((StreetField) fields[fieldArrayNum]).hasHotel());
    }

    public boolean playerMaySellHouseOnField(int fieldArrayNum, int playerNum) {
        return fields[fieldArrayNum].isStreetField()
                && ((OwnableField) fields[fieldArrayNum]).getOwnerNum() == playerNum
                && (((StreetField) fields[fieldArrayNum]).getNumOfHouses() > 0);
    }

    public void currentPlayerBuildingDecision(int playernum) { // uses gui to get user input on buy/sell buildings decision
        String userinput = "";
        String userDoesNotWantToBuyResponse = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(16);
        // depending on the player's situation, need gui to only display the relevant options available for player
        boolean mayBuyHouse, maySellHouse, mayBuyHotel, maySellHotel, showAll, allExceptSellHotel, allExceptBuyHotel, allExceptBuyHouse, buyHouseSellHouse,
                buyHouseSellHotel, sellHouseSellHotel, sellHouseBuyHotel, buyHouseOnly, sellHouseOnly, sellHotelOnly;
        while (!userinput.equalsIgnoreCase(userDoesNotWantToBuyResponse)) {
            mayBuyHouse = currentPlayerMayBuyHouses();
            maySellHouse = playerMaySellHouses(playernum);
            mayBuyHotel = currentPlayerMayBuyHotels();
            maySellHotel = playerMaySellHotels(playernum);
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
                userinput = ViewController_GUIMessages.getInstance().getBuyOrSellBuildingsUserInput(56, 57, 56, 57, 25, 25, 27, 27);
            else if (allExceptSellHotel)
                userinput = ViewController_GUIMessages.getInstance().getBuyOrSellBuildingsUserInput(56, 57, 56, 25, 25, 27);
            else if (allExceptBuyHotel)
                userinput = ViewController_GUIMessages.getInstance().getBuyOrSellBuildingsUserInput(56, 57, 57, 25, 25, 27);
            else if (allExceptBuyHouse)
                userinput = ViewController_GUIMessages.getInstance().getBuyOrSellBuildingsUserInput(57, 56, 57, 25, 27, 27);
            else if (buyHouseSellHouse)
                userinput = ViewController_GUIMessages.getInstance().getBuyOrSellBuildingsUserInput(56, 57, 25, 25);
            else if (buyHouseSellHotel)
                userinput = ViewController_GUIMessages.getInstance().getBuyOrSellBuildingsUserInput(56, 57, 25, 27);
            else if (sellHouseSellHotel)
                userinput = ViewController_GUIMessages.getInstance().getBuyOrSellBuildingsUserInput(57, 57, 25, 27);
            else if (sellHouseBuyHotel)
                userinput = ViewController_GUIMessages.getInstance().getBuyOrSellBuildingsUserInput(57, 56, 25, 27);
            else if (buyHouseOnly)
                userinput = ViewController_GUIMessages.getInstance().getBuyOrSellBuildingsUserInput(56, 25);
            else if (sellHouseOnly)
                userinput = ViewController_GUIMessages.getInstance().getBuyOrSellBuildingsUserInput(57, 25);
            else if (sellHotelOnly)
                userinput = ViewController_GUIMessages.getInstance().getBuyOrSellBuildingsUserInput(57, 27);
            else
                userinput = userDoesNotWantToBuyResponse;
            // calls the relevant methods based on user response
            if (!userinput.equals(userDoesNotWantToBuyResponse)) {
                String buyHouseDecision = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(56, 25);
                String sellHouseDecision = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(57, 25);
                String buyHotelDecision = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(56, 27);
                String sellHotelDecision = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(57, 27);
                if (userinput.equalsIgnoreCase(buyHouseDecision))
                    buildHouse();
                else if (userinput.equalsIgnoreCase(sellHouseDecision))
                    sellHouse(playernum);
                else if (userinput.equalsIgnoreCase(buyHotelDecision))
                    buildHotel();
                else if (userinput.equalsIgnoreCase(sellHotelDecision))
                    sellHotel(playernum);
            }
        }
    }

    public void playerMustSellBuildings(int playerNum, Player playerobject) {
        boolean maySellHouse, maySellHotel, sellHouseSellHotel, sellHouseOnly, sellHotelOnly;
        String userinput;
        while (playerobject.getAccount().getBalance() < 0) {
            maySellHouse = playerMaySellHouses(playerNum);
            maySellHotel = playerMaySellHotels(playerNum);
            sellHouseSellHotel =  maySellHouse && maySellHotel;
            sellHouseOnly =  maySellHouse  && !maySellHotel;
            sellHotelOnly =  !maySellHouse && maySellHotel;
            if (sellHouseSellHotel)
                userinput = ViewController_GUIMessages.getInstance().getBuyOrSellBuildingsUserInput(57, 57, 25, 27);
            else if (sellHouseOnly)
                userinput = ViewController_GUIMessages.getInstance().getBuyOrSellBuildingsUserInput(57, 25);
            else if (sellHotelOnly)
                userinput = ViewController_GUIMessages.getInstance().getBuyOrSellBuildingsUserInput(57, 27);
            else
                userinput = "";
            // calls the relevant methods based on user response
            String sellHouseDecision = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(57, 25);
            String sellHotelDecision = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(57, 27);
            if (userinput.equalsIgnoreCase(sellHouseDecision))
                sellHouse(playerNum);
            else if (userinput.equalsIgnoreCase(sellHotelDecision))
                sellHotel(playerNum);
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

    private String[] getStreetColorsEligibleForSellingHouse(int playerNum) {
        // returns an array of street colors where player may sell houses
        String[] temp = new String[fields.length];
        String lastColor = "";
        int numDifferentColors = 0;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isStreetField() && playerMaySellHouseOnField(i,playerNum)) {
                temp[i] = ((StreetField) fields[i]).getStreetColor();
                if (!temp[i].equalsIgnoreCase(lastColor)) {
                    numDifferentColors++;
                    lastColor = temp[i];
                }
            }
        }
        return getStreetColors(temp, numDifferentColors);
    }

    private String[] getStreetColorsEligibleForSellingHotel(int playerNum) {
        // returns an array of street colors where player may sell hotels
        String[] temp = new String[fields.length];
        String lastColor = "";
        int numDifferentColors = 0;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isStreetField() && playerMayBuySellHotelOnField(i,playerNum)) {
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
        for (String s : temp) {
            if (s != null && !s.equalsIgnoreCase(lastColor) && counter < numDifferentColors) {
                lastColor = s;
                result[counter] = s;
                counter++;
            }
        }
        return result;
    }

    private String extractColor(String str) {
        String result = "";
        for (Field field : fields) {
            if (field.isStreetField()) {
                if (str.contains(((StreetField) field).getStreetColor())) {
                    result = ((StreetField) field).getStreetColor();
                }
            }
        }
        return result;
    }
}
