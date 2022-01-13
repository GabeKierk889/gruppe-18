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

    private void buildHouse() {
        int currentPlayer = GameController.getInstance().getCurrentPlayerNum();
        // gets an array of street colors where player is eligible to build, then gets user input on which color
        // player selects to take an action on. E.g., player want to build on the yellow fields
        String[] colorsEligibleForBuilding = getStreetColorsEligibleForBuildingHouse();
        String userSelection = ViewController_GUIMessages.getInstance().whereToBuildUserInput(colorsEligibleForBuilding);
        String userSelectionColor = extractColor(userSelection); // saves the field color of the user choice
        int pricePerHouse = 0;
        int[] totalHousesOnField = new int[fields.length];
        int[] housesToBuild = new int[fields.length]; // will be used to store the user input
        boolean[] fieldHasHotel = new boolean[fields.length];
        boolean successfulCompletion = false; // below code will run a loop while there is an error in user input
        // error incl. for example exceeding the limit on num houses on a street
        do {
            int maxNumHouses = 0;
            int minNumHouses = StreetField.MAXNUMOFHOUSES + 1;
            int totalNewBuilds = 0;
            int counter = 0;
            for (Field field : fields) {
                // runs for all fields of the color selected by the user
                if (field.isStreetField() && userSelectionColor.equalsIgnoreCase(((StreetField) field).getStreetColor())) {
                    fieldHasHotel[counter] = ((StreetField) field).hasHotel();
                    pricePerHouse = ((StreetField) field).getHousePrice();
                    // gets user input on how many houses to build for all the fields where user is allowed to build
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
                if (totalNewBuilds == 0) // reads in a line from txt file that says 0 buildings have been built
                    ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(64, "", "", "");
                else {
                    // gives the player an opportunity to confirm the purchase of houses
                    int lineintext; // stores the word for either "a house" or "houses"
                    if (totalNewBuilds > 1)
                        lineintext = 25;
                    else
                        lineintext = 69;
                    // player may confirm purchase or cancel
                    if (!ViewController_GUIMessages.getInstance().showMessageAndGetBooleanUserInput(65, 15, 46, "" + totalCost, "" + totalNewBuilds, lineintext)) // asks player to confirm
                        ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(62, "", "", "");
                    else {
                        // if player confirms purchase, build the houses
                        counter = 0;
                        boolean playerMayNowBuildHotelsOnFieldsOfThisColor = false; // a test variable used later
                        for (int i = 0; i < fields.length; i++) {
                            if (fields[i].isStreetField()) {
                                // for the specified fields on the given color, builds the houses and updates gui
                                if (userSelectionColor.equalsIgnoreCase(((StreetField) fields[i]).getStreetColor())) {
                                    ((StreetField) fields[i]).setNumOfHouses(totalHousesOnField[counter]);
                                    ((StreetField) fields[i]).updateRent();
                                    if (!((StreetField) fields[i]).hasHotel())
                                        ViewController.getInstance().setGUINumHouses(i, ((StreetField) fields[i]).getNumOfHouses());
                                    if (currentPlayerMayBuyHotelOnField(i)) // saves if player is now qualified to build hotels
                                        playerMayNowBuildHotelsOnFieldsOfThisColor = true;
                                    counter++;
                                }
                            }
                        }
                        // checks that the player is able to pay the price to build
                        if (GameController.getInstance().getPlayerObject(currentPlayer).getAccount().getBalance() >= totalCost) {
                            String str; // stores the word for either "a house" or "houses"
                            if (totalNewBuilds > 1)
                                str = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(25);
                            else
                                str = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(69);
                            // withdraws money and updates the GUI account balance
                            GameController.getInstance().getPlayerObject(currentPlayer).getAccount().withdrawMoney(totalCost);
                            ViewController.getInstance().updateGUIBalance();
                            // displays a message to the player that the houses have been built for x price
                            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(66, "" + totalNewBuilds, str, "" + totalCost);
                            boolean noHotel = true; // checks if there are any hotels already on this color
                            for (boolean test : fieldHasHotel)
                                if (test) {
                                    noHotel = false;
                                    break;
                                }
                            // display message to player that they may now build hotels
                            if (noHotel && playerMayNowBuildHotelsOnFieldsOfThisColor)
                                ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(67, "" + StreetField.MAXNUMOFHOUSES, userSelectionColor, "");
                        }
                        else { // if player cannot pay, then don't display the messages above, but withdraw money
                            // withdrawing money will make balace <0 and call the sellAssets or gobankrupt method
                            GameController.getInstance().getPlayerObject(currentPlayer).getAccount().withdrawMoney(totalCost);
                            ViewController.getInstance().updateGUIBalance();
                        }
                    }
                }
            }
        }
        while (!successfulCompletion);
    }

    private void buildHotel() {
        int currentPlayer = GameController.getInstance().getCurrentPlayerNum();
        // gets an array of street colors where player is eligible to build, then gets user input on which color
        // player selects to take an action on. E.g., player want to build on the yellow fields
        String[] colorsEligibleForBuilding = getStreetColorsEligibleForBuildingHotel();
        String userSelection = ViewController_GUIMessages.getInstance().whereToBuildUserInput(colorsEligibleForBuilding);
        String userSelectionColor = extractColor(userSelection);
        int pricePerHouse = 0;
        boolean[] hotelsToBuild = new boolean[fields.length];
        boolean[] fieldHasHotel = new boolean[fields.length];
        int totalNewBuilds = 0;
        int counter = 0;
        for (Field field : fields) {
            // runs for all fields of the color selected by the user
            if (field.isStreetField() && userSelectionColor.equalsIgnoreCase(((StreetField) field).getStreetColor())) {
                fieldHasHotel[counter] = ((StreetField) field).hasHotel();
                pricePerHouse = ((StreetField) field).getHousePrice();
                // gets user input via gui whether they want to build a hotel on a given field
                if (!fieldHasHotel[counter] && ((StreetField) field).getNumOfHouses() == StreetField.MAXNUMOFHOUSES)
                    hotelsToBuild[counter] = ViewController_GUIMessages.getInstance().buildHotelUserInput(field.getFieldName(), pricePerHouse);
                if (hotelsToBuild[counter])
                    totalNewBuilds++;
                counter++;
            }
        }
        int totalCost = totalNewBuilds * pricePerHouse;
        if (totalNewBuilds == 0) // reads in a line from txt file that says 0 buildings have been built
            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(64, "", "", "");
        else {
            // gives the player an opportunity to confirm the purchase of hotels
            int lineintext; // stores the word for either "a hotel" or "hotels"
            if (totalNewBuilds > 1)
                lineintext = 27;
            else
                lineintext = 70;
            // player may confirm purchase or cancel
            if (!ViewController_GUIMessages.getInstance().showMessageAndGetBooleanUserInput(65, 15, 46, "" + totalCost, "" + totalNewBuilds, lineintext)) // asks player to confirm
                ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(62, "", "", "");
            else {
                // build the hotel, update in gui, and update the rent
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
                } // below checks that the player is able to pay, then withdraws money
                if (GameController.getInstance().getPlayerObject(currentPlayer).getAccount().getBalance() >= totalCost) {
                    String str; // stores the word for either "a hotel" or "hotels"
                    if (totalNewBuilds > 1)
                        str = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(27);
                    else
                        str = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(70);
                    GameController.getInstance().getPlayerObject(currentPlayer).getAccount().withdrawMoney(totalCost);
                    ViewController.getInstance().updateGUIBalance();
                    // a message is shown that the hotels have been built
                    ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(66, "" + totalNewBuilds, str, "" + totalCost);
                }
                else { // if player cannot pay, then don't display the messages above, but withdraw money
                    // withdrawing money will make balace <0 and call the sellAssets or gobankrupt method
                    GameController.getInstance().getPlayerObject(currentPlayer).getAccount().withdrawMoney(totalCost);
                ViewController.getInstance().updateGUIBalance();
                }
            }
        }
    }

    private void sellHouse(int playerNum) {
        String[] colorsEligibleForSelling = getStreetColorsEligibleForSellingHouse(playerNum);
        // gets an array of street colors where player is eligible to sell, then gets user input on which color
        // player selects to take an action on. E.g., player want to sell buildings on the yellow fields
        String userSelection = ViewController_GUIMessages.getInstance().whereToUnBuildUserInput(colorsEligibleForSelling);
        String userSelectionColor = extractColor(userSelection);
        int resellvaluePerHouse = 0;
        int[] housesToSell = new int[fields.length];
        int[] totalHousesOnField = new int[fields.length];
        int totalBuildingsSold = 0;
        int counter = 0;
        for (Field field : fields) {
            // runs for all fields of the same color selected by the player
            if (field.isStreetField() && userSelectionColor.equalsIgnoreCase(((StreetField) field).getStreetColor())) {
                resellvaluePerHouse = (int) Math.round(((StreetField) field).getHousePrice() * GameSettings.HOUSE_RESELL_VALUE_MULTIPLIER);
                // if there are houses on the field, ask player how many they want to sell
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
        if (totalBuildingsSold == 0) // displays a message that no buildings have been sold
            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(73, "", "", "");
        else {
            // gives the player an opportunity to confirm the sale of building
            int lineintext; // stores the word for either "a house" or "houses"
            if (totalBuildingsSold > 1)
                lineintext = 25;
            else
                lineintext = 69;
            // player may confirm sale or cancel
            if (!ViewController_GUIMessages.getInstance().showMessageAndGetBooleanUserInput(75, 15, 46, "" + totalMoneyEarned, "" + totalBuildingsSold, lineintext)) // asks player to confirm
                ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(74, "", "", "");
            else {
                // sell the house and update it in the gui as well
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
                } // update rent for all the fields of the same color, and deposit the money
                GameController.getInstance().getBoard().updateRentForAllFieldsOfSameType(fieldnum);
                GameController.getInstance().getPlayerObject(playerNum).getAccount().depositMoney(totalMoneyEarned);
                ViewController.getInstance().updateGUIBalance();
                String str; // stores the word for either "a house" or "houses" to display a gui message
                if (totalBuildingsSold > 1)
                    str = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(25);
                else
                    str = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(69);
                ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(76, "" + totalBuildingsSold, str, "" + totalMoneyEarned);
            }
        }
    }

    private void sellHotel(int playerNum) {
        String[] colorsEligibleForSelling = getStreetColorsEligibleForSellingHotel(playerNum);
        // gets an array of street colors where player is eligible to sell, then gets user input on which color
        // player selects to take an action on. E.g., player want to sell buildings on the yellow fields
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
                // calculate resell value of a hotel
                resellvaluePerHotel = (int) Math.round(((StreetField) field).getHousePrice() * (StreetField.MAXNUMOFHOUSES+1) * GameSettings.HOUSE_RESELL_VALUE_MULTIPLIER);
                // if there is a hotel on the field, ask player via gui if they want to sell
                if (fieldHasHotel[counter])
                    hotelsToSell[counter] = ViewController_GUIMessages.getInstance().sellHotelUserInput(field.getFieldName(), resellvaluePerHotel);
                if (hotelsToSell[counter])
                    totalBuildingsSold++;
                counter++;
            }
        }
        int totalMoneyEarned = totalBuildingsSold * resellvaluePerHotel;
        if (totalBuildingsSold == 0)  // displays a message that no buildings have been sold
            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(73, "", "", "");
        else {
            // gives the player an opportunity to confirm the sale of hotels
            int lineintext; // stores the word for either "a hotel" or "hotels"
            if (totalBuildingsSold > 1)
                lineintext = 27;
            else
                lineintext = 70;
            // player may confirm sale or cancel
            if (!ViewController_GUIMessages.getInstance().showMessageAndGetBooleanUserInput(75, 15, 46, "" + totalMoneyEarned, "" + totalBuildingsSold, lineintext)) // asks player to confirm
                ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(74, "", "", "");
            else {
                // sell the hotel and update it in the gui
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
                // deposit money from the sale and update the gui, then show a message to the player
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

    // support method that checks if the player is eligible to build any houses
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

    // support method that checks if the player is eligible to build houses on this field
    private boolean currentPlayerMayBuyHouseOnField(int fieldArrayNum) {
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

    private boolean playerMaySellHouses(int playernum) {
        int[] buildingsOwned = GameController.getInstance().getBoard().numBuildingsOwnedByPlayer(playernum);
        return buildingsOwned[0] > 0; // if they own any houses
    }

    // support method that checks if the player is eligible to build any hotels
    private boolean currentPlayerMayBuyHotels() {
        for (int i = 0; i < fields.length; i++) {
            if (currentPlayerMayBuyHotelOnField(i))
                return true;
        }
        return false;
    }

    private boolean playerMaySellHotels(int playerNum) {
        int[] buildingsOwned = GameController.getInstance().getBoard().numBuildingsOwnedByPlayer(playerNum);
        return buildingsOwned[1] > 0; // if they own any hotels
    }

    // support method that checks if the player is eligible to build a hotel on the given field
    private boolean currentPlayerMayBuyHotelOnField(int fieldArrayNum) {
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

    // support method that checks if the player is eligible to sell a hotel on the given field
    private boolean playerMaySellHotelOnField(int fieldArrayNum, int playerNum) {
        return  fields[fieldArrayNum].isStreetField()
                && ((OwnableField) fields[fieldArrayNum]).getOwnerNum() == playerNum
                && (((StreetField) fields[fieldArrayNum]).hasHotel());
    }

    // support method that checks if the player is eligible to sell a house on the given field
    private boolean playerMaySellHouseOnField(int fieldArrayNum, int playerNum) {
        return fields[fieldArrayNum].isStreetField()
                && ((OwnableField) fields[fieldArrayNum]).getOwnerNum() == playerNum
                && (((StreetField) fields[fieldArrayNum]).getNumOfHouses() > 0);
    }

    // this method is called if the player is eligible to buy or sell buildings
    // it displays a drop-down menu for the player to buy or sell houses or hotels depending on what they are eligible to do
    // and calls the buyhouse, buyhotel, or sellhouse or sellhotel methods depending on the user choice
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
            // the following combinations of options are available depending on the player situation
            // for example, sometimes a player may only be able to buy a house
            // othertimes, they are able to both buy and sell both houses and hotels
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

            // only display the relevant choices for the user in a drop-down menu
            // store the user input, which will later be used to call the method corresponding to the user choice
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

    // this method is called if the player has a negative money balance and must sell some buildings
    // it displays a drop-down menu for the player to sell houses or hotels depending on what they own
    // and calls the sellhouse or sellhotel methods depending on the user choice
    public void playerMustSellBuildings(int playerNum, Player playerobject) {
        boolean maySellHouse, maySellHotel, sellHouseSellHotel, sellHouseOnly, sellHotelOnly;
        String userinput;
        // make a loop that runs as long as the player's account is negative
        while (playerobject.getAccount().getBalance() < 0) {
            maySellHouse = playerMaySellHouses(playerNum);
            maySellHotel = playerMaySellHotels(playerNum);
            sellHouseSellHotel =  maySellHouse && maySellHotel;
            sellHouseOnly =  maySellHouse  && !maySellHotel;
            sellHotelOnly =  !maySellHouse && maySellHotel;
            if (sellHouseSellHotel)
                userinput = ViewController_GUIMessages.getInstance().getSellBuildingsUserInput(57, 57, 25, 27);
            else if (sellHouseOnly)
                userinput = ViewController_GUIMessages.getInstance().getSellBuildingsUserInput(57, 25);
            else if (sellHotelOnly)
                userinput = ViewController_GUIMessages.getInstance().getSellBuildingsUserInput(57, 27);
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
            if (fields[i].isStreetField() && playerMaySellHotelOnField(i,playerNum)) {
                temp[i] = ((StreetField) fields[i]).getStreetColor();
                if (!temp[i].equalsIgnoreCase(lastColor)) {
                    numDifferentColors++;
                    lastColor = temp[i];
                }
            }
        }
        return getStreetColors(temp, numDifferentColors);
    }

    // returns an array only with the street colors that is relevant for the current situation
    // this array will be used to create a dropdown menu in the gui
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

    // extracts the word that describes a color from within a longer string
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
