package Game;

import gui_fields.*;
import gui_main.GUI;
import java.awt.*;

public class MonopolyGame {
    static Game game;
    private static GUI_Field[] guiFields;
    private static GUI_Street[] guiStreets;
    private static GUI_Car[] guiCars;
    private static GUI_Player[] guiPlayers;
    private static GUI gui;

    public static void main(String[] args) {
        // Setting up and formatting GUI fields and streets (using a support method)
        guiFields = new GUI_Field[Board.getTotalNumberOfFields()];
        guiStreets = new GUI_Street[Board.getTotalNumberOfFields()];
        formatFields();

        // Setting up GUI board
        gui = new GUI(guiFields, new Color(230,230,230));

        // Calling a support method that takes user input/names and initializes the game with 2-4 players
        initializeGame();

        // Setting up the GUI cars (using a support method)
        guiCars = new GUI_Car[game.getTotalPlayers()];
        setupGUICars();

        // Setting up GUI players and placing their cars on the game board (using a support method)
        guiPlayers = new GUI_Player[game.getTotalPlayers()];
        addGUIPlayersAndCars();

        // Play the main game loop
        gameLoop();
        }

    private static void gameLoop() {
        // Displays a welcome message and allows the players to view the rules before starting the game
        showWelcomeMessage();

        // checking that the game is not yet over (meaning no player has gone bankrupt)
        while (!game.isGameOver()) {

            // if the current player is in jail, an alternative flow is run first before the main flow
            inJail();

            // displays a message for a player to roll the die, then rolls the die
            roll(gui.getUserButtonPressed(guiPlayers[game.getCurrentPlayerNumber()-1].getName() + "'s" + " turn. Press to roll the die","Roll"));

            // removes the current player's car from the previous field they stood on
            guiFields[game.getPlayerObject(game.getCurrentPlayerNumber()).OnField()].setCar(
                    guiPlayers[game.getCurrentPlayerNumber()-1], false);

            // sets the player's car on the street corresponding to the latest dice roll
            guiFields[game.getPlayerObject(game.getCurrentPlayerNumber()).movePlayerSteps(game.getDie().getFaceValue())].
                    setCar(guiPlayers[game.getCurrentPlayerNumber()-1], true);

            // the player gets a START bonus if they land on/ pass START via a regular move after a dice-throw
            // (not via chance card situations)
            game.getPlayerObject(game.getCurrentPlayerNumber()).collectStartBonus(game.getDie().getFaceValue());

            // showing/ updating the player's balance, in case they got a START bonus
            updatePlayerBalance();

            // displays message to current player about the actions of their turn
            gui.showMessage(playerTurnMessage());

            // calls landOnField method which will do something if it is an amusement field, jail, or chance field
            int playerOnFieldNumber = game.getPlayerObject(game.getCurrentPlayerNumber()).OnField();
            Game.getBoard().getFieldObject(playerOnFieldNumber).landOnField(game.getPlayerObject(game.getCurrentPlayerNumber()));

            // alternate flow that calls landOnField method again in case player has landed on another field via chance cards
            while(playerOnFieldNumber != game.getPlayerObject(game.getCurrentPlayerNumber()).OnField()) {
                // stores the fieldnumber the player is on before landonfield is called, and compares after the turn
                playerOnFieldNumber = game.getPlayerObject(game.getCurrentPlayerNumber()).OnField();
                Game.getBoard().getFieldObject(playerOnFieldNumber).landOnField(game.getPlayerObject(game.getCurrentPlayerNumber()));
            }

            // changes to the next player's turn (if the current player does not get an extra turn)
            game.switchTurn(false);
        }

        // Checks whether the game needs to end, finds the winner and asks if they want to play again
        endGame();
    }

    private static void endGame() {
        if (game.isGameOver()) {
            game.determineWinner(); // determine the winner based on the game rules
            updatePlayerBalance();
            // if there is one winner
            if (game.determineWinner() == game.determineWinner2())
            gui.showMessage("The game is over as " + game.getBankruptPlayerName() + " has gone bankrupt." +
                    "\nThe winner of this game is " + game.getPlayerObject(game.determineWinner()).getName());
            // if there are two winners
            if (game.determineWinner() != game.determineWinner2())
                gui.showMessage("The game is over as " + game.getBankruptPlayerName() + " has gone bankrupt." +
                        "\nThe winners of this game are " + game.getPlayerObject(game.determineWinner()).getName() +
                        " and " + game.getPlayerObject(game.determineWinner2()).getName());
            // asks if they want to play again. If yes, resets the game and starts a new game
            if (gui.getUserLeftButtonPressed("Do you want to play again?", "Yes", "No")) {
                resetGame();
                gameLoop();
            } else {
                gui.showMessage("Thank you for playing today! Goodbye!");
                gui.close();
                System.exit(0); }
        }
    }

    static void moveCarToFieldArrayNum(int destinationFieldArrayNum) {
        // removes the current player's car from the previous field they stood on
        guiFields[game.getPlayerObject(game.getCurrentPlayerNumber()).OnField()].setCar(
                guiPlayers[game.getCurrentPlayerNumber()-1], false);

        // sets the player's car on the destination field
        guiFields[(destinationFieldArrayNum)%Board.getTotalNumberOfFields()].setCar(guiPlayers[game.getCurrentPlayerNumber()-1], true);
    }

    static int pickZeroToFive() {
        return gui.getUserInteger("Please select a number between 0 and 5 to move the corresponding number of fields.",0,5);
    }

    private static void roll(String roll) {
        if (roll.equalsIgnoreCase("Roll")) {
        game.getDie().roll();
            int x = (int) (Math.random() * 2 + 9);
            int y = (int) (Math.random() * 10);
            gui.setDice(
                    1,1,x,y, // this die is never shown as there is only 1 die in this game
                    game.getDie().getFaceValue(), (int) (Math.random() * 359),x,y );
        }
    }

    private static void inJail() {
        // alternate flow if the current player is in jail at the start of the turn
        if (game.getPlayerObject(game.getCurrentPlayerNumber()).getIsInJail()) {

            // if player owns a release from jail card
            if (game.getPlayerObject(game.getCurrentPlayerNumber()).hasAReleaseFromJailCard() )
            {
                if (gui.getUserLeftButtonPressed(guiPlayers[game.getCurrentPlayerNumber()-1].getName() + ": You are in jail. Do you want to use your " +
                "\"Get out of jail for free\" card now? You can only use the card once.","Yes","No"))
                {  // if player chooses to use their card to get out, otherwise skips this section (until return;)
                    // removing their ownership of the card and returning the card to the deck of chance cards
                    ChanceField.putBackChanceCard(game.getPlayerObject(game.getCurrentPlayerNumber()).returnReleaseFromJailCard());

                    // releasing the player from jail
                    game.getPlayerObject(game.getCurrentPlayerNumber()).setIsInJail(false);

                    // shows a message to the player that they been released, and to take a turn
                    gui.showMessage("You have used your chance card to be released from jail at no cost. " +
                    "You are now a free (wo)man and can take a turn. ");

                    // return to main flow
                    return;
                }
            }

            // displays a message for player to pay; withdraws get out of jail fee from the player's balance
            if(gui.getUserButtonPressed(guiPlayers[game.getCurrentPlayerNumber()-1].getName() + ": " +
            "You are in jail. You need to pay M$"+ Account.JAILFEE + " to be released from jail", "Pay").equalsIgnoreCase("pay"))
                game.getPlayerObject(game.getCurrentPlayerNumber()).getAccount().withdrawMoney(Account.JAILFEE);

            // releasing the player from jail
            game.getPlayerObject(game.getCurrentPlayerNumber()).setIsInJail(false);

            // showing/ updating the player's balance
            updatePlayerBalance();

            // shows a message to the player that they have paid the fee, and to take a turn
            gui.showMessage("You have paid M$"+ Account.JAILFEE +" to be released from jail. You are now a free (wo)man and can take a turn. ");
        }
    }

    private static void initializeGame() {
        String str = gui.getUserString("On 1 line (separated by spaces only), enter the names of 2-4 players who will be playing today");
        String[] strarray = str.split(" ");
        boolean duplicateNames = false;
        for (int i = 0; i < strarray.length; i++) {
            for (int j = 0; j < strarray.length; j++)
                if (j != i && strarray[j].equals(strarray[i])) {
                    duplicateNames = true;
                    break;}
        }
        if (duplicateNames) {
            gui.showMessage("Error, two players cannot have exactly the same name. Please try again.");
            initializeGame();
        }
        else if (strarray.length > Game.MAXPLAYERS || strarray.length < Game.MINPLAYERS ) {
            gui.showMessage("Error  - the game cannot be started. There must be " +
            Game.MINPLAYERS + "-"+ Game.MAXPLAYERS + " players in the game.");
            initializeGame(); }
        else {
            String names = "";
            for (int i = 0; i < strarray.length; i++) {
                if (i != strarray.length - 1)
                    names += "Player  " + (i+1) + ": " + strarray[i] +",   ";
                else
                    names += "Player  " + (i+1) + ": " + strarray[i] ;
            }
        if ("Yes, begin game".equalsIgnoreCase(gui.getUserButtonPressed("These are the players that will be set up: \n" + names+"\n\n\t\t\tIs this correct?", "Yes, begin game", "No, reset names"))) {
        switch (strarray.length) { // initializes a new game with the player names entered
            case 2: game = new Game(strarray[0],strarray[1]); break;
            case 3: game = new Game(strarray[0],strarray[1],strarray[2]); break;
            case 4: game = new Game(strarray[0],strarray[1],strarray[2],strarray[3]); break;
            default: game = new Game(strarray[0]);
        } }
        else
            initializeGame();
    } }

    private static void showWelcomeMessage() {
        if (gui.getUserLeftButtonPressed("Welcome to Monopoly Junior! Press \"Help\" to view the rules, or press \"Start game\" to begin the game.     "
                + guiPlayers[0].getName() + " will go first","Help","Start game"))
            gui.showMessage("Rules: Each player starts with M$35. On a player's turn, if they land on an amusement without a booth, the player must pay the bank to set up a booth. " +
                    "If there is already a booth owned by another player, the current player must pay the owner (double, if the owner also owns the other fields of the same color). " +
                    "If a player lands on Chance, the player must draw a chance card and follow the instructions. " +
                    "If a player lands on or passes START, the player gets M$2. " +
                    "If a player lands on “Go to Jail”, the player goes into jail. " +
                    "To get out of jail, the player may use a \"get out of jail\" chance card, " +
                    "or pay M$1 to get out of jail on their next turn. " +
                    "If a player runs out of money, the players' balances are compared, and the player with the most money wins the game.");
    }

    private static String playerTurnMessage() {
        // display message shown to player after a dice throw
        String str = "";
        int fieldnum = game.getPlayerObject(game.getCurrentPlayerNumber()).OnField();
        str += guiPlayers[game.getCurrentPlayerNumber()-1].getName() +", you landed on " +
                Game.getBoard().getFieldObject(fieldnum) + ".\n";
        if (fieldnum < game.getDie().getFaceValue())
            str += "You have received M$"+ Account.STARTBONUS +" for passing START\n";
        if (fieldnum == 6)
            str += "You go on a visit to jail\nNext player's turn";
        else if (fieldnum == 12)
            str += "Hurrah for free parking!\nNext player's turn";
        else if (fieldnum%6 != 0 && fieldnum%3 == 0)
            str += "Click OK to draw a chance card";
        return str;
    }

    static void goToJailMessage() {
        gui.showMessage("Oh no! You are going into jail. On your next turn, you may use a \"Get out of Game.Jail\" chance card, " +
                "if you have one, to get out of jail. Otherwise, on your next turn, you need to pay M$"+ Account.JAILFEE +" to get out.");
    }

    static void setupBoothMessage(int fieldArrayNum, int price) {
        gui.getUserButtonPressed("Press the button to setup a booth on this field", "Setup booth");
        updatePlayerBalance();
        formatFieldBorder(fieldArrayNum);
        updateGUIRentAndOwnerInfoAllFieldsOfSameColor(fieldArrayNum);
        String color = ((AmusementField) Game.getBoard().getFieldObject(fieldArrayNum)).getFieldColor();
        String str = "You have paid M$" + price + " to setup a booth.";
        if (Board.onePlayerOwnsAllFieldsOfSameColor(fieldArrayNum))
            str += "\nAs you now own a booth on all the "+ color +" fields, from now on the price that other players " +
                    "have to pay you when they land on these fields will be doubled.";
        gui.showMessage(str);
    }

    static void payBoothPriceMessage(int fieldArrayNum, int owner, int rent) {
        String color = ((AmusementField) Game.getBoard().getFieldObject(fieldArrayNum)).getFieldColor();
        if (!Board.onePlayerOwnsAllFieldsOfSameColor(fieldArrayNum))
            gui.getUserButtonPressed("You have landed on " + game.getPlayerObject(owner).getName() + "'s booth"
                , "Pay M$" + rent);
        else gui.getUserButtonPressed("You have landed on " + game.getPlayerObject(owner).getName() + "'s booth. " +
                        "As "+ game.getPlayerObject(owner).getName() + " owns a booth on all the "
                + color +" fields, you have to pay double the price.", "Pay M$" + rent);
        updatePlayerBalance();
        gui.showMessage("You have now paid M$" + rent + " to "+ game.getPlayerObject(owner).getName());
    }

    static void showChanceCardMessage()
    {
        gui.setChanceCard(ChanceField.getCurrentCard().chanceCardText());
        gui.getUserButtonPressed("Press the green square in the middle to view your chance card. Keep hovering your cursor over the card while you're reading it. " +
                "When you are done reading, press the 'Apply' button to apply the instructions of the chance card","Apply");
        gui.setChanceCard(""); // removes the text after the player has read it
    }

    static void showStartBonusMessage() {
        // used for chance card situations if a player passes START
        gui.showMessage("You have received M$"+ Account.STARTBONUS +" for passing START");
    }

    static void updatePlayerBalance () {
        // updating and showing the updated balances for all players
        for (int i = 0; i< game.getTotalPlayers(); i++)
            guiPlayers[i].setBalance(game.getPlayerObject(i+1).getAccount().getBalance());
    }

    // checks if a player owns all the fields of one color, and updates the GUI info for all those fields
    private static void updateGUIRentAndOwnerInfoAllFieldsOfSameColor(int fieldArrayNum) {
        if (Board.onePlayerOwnsAllFieldsOfSameColor(fieldArrayNum)) {
            String color = ((AmusementField) Game.getBoard().getFieldObject(fieldArrayNum)).getFieldColor();
            String fieldClassName = Game.getBoard().getFieldObject(fieldArrayNum).getClassName();
            for (int i = 0; i< Board.getTotalNumberOfFields(); i++) {
                if (Game.getBoard().getFieldObject(i).getClassName().equalsIgnoreCase(fieldClassName)) {
                    if (color.equalsIgnoreCase(((AmusementField) Game.getBoard().getFieldObject(i)).getFieldColor()))
                        updateGUIRentAndOwnerOneField(i);
                }
            }
        }
        else updateGUIRentAndOwnerOneField(fieldArrayNum);
    }

    private static void updateGUIRentAndOwnerOneField (int fieldArrayNum) {
        // updates rent and owner info in the GUI center field for the given field
        int rent = ( (AmusementField) Game.getBoard().getFieldObject(fieldArrayNum) ).getRent();
        int ownernumber = ((AmusementField)Game.getBoard().getFieldObject(fieldArrayNum)).getOwnerNum();
        guiStreets[fieldArrayNum].setRentLabel("The rent is: ");
        guiStreets[fieldArrayNum].setRent("M$"+ rent);
        guiStreets[fieldArrayNum].setOwnableLabel("The field is owned by: ");
        guiStreets[fieldArrayNum].setOwnerName(game.getPlayerObject(ownernumber).getName());
    }

    // sets a border around a field owned by a player, corresponding to the color of their car
    private static void formatFieldBorder(int fieldArrayNum) {
        switch (((AmusementField)Game.getBoard().getFieldObject(fieldArrayNum)).getOwnerNum()) {
            case 1: {
                guiStreets[fieldArrayNum].setBorder(Color.red);
                break; }
            case 2: {
                guiStreets[fieldArrayNum].setBorder(Color.blue);
                break; }
            case 3: {
                guiStreets[fieldArrayNum].setBorder(Color.GREEN);
                break; }
            case 4: {
                guiStreets[fieldArrayNum].setBorder(new Color(170,60,250));
                break; }
            default: break;
            }
    }

    // formats the game board with the same layout and colors as the real life game board
    private static void formatFields() {
        for (int i = 0; i < Board.getTotalNumberOfFields(); i++) { // sets up all GUI fields/streets
            guiStreets[i] = new GUI_Street();
            guiStreets[i].setTitle(Game.getBoard().getFieldObject(i).getFieldName());
            guiStreets[i].setBackGroundColor(Color.lightGray);
            guiFields[i] = guiStreets[i]; }

        for (int i = 0; i < Board.getTotalNumberOfFields(); i++) { // displays the price and description for the ownable fields
            if (i%3 != 0) {
                String str = "M$";
                guiStreets[i].setSubText(str + ((AmusementField)Game.getBoard().getFieldObject(i)).getPrice());
                guiStreets[i].setDescription(Game.getBoard().getFieldObject(i).getFieldName());
            }
        }

        // gives the ownable fields the same colors as the real-life MonopolyJr game board
        guiStreets[1].setBackGroundColor(new Color(180,140,130));
        guiStreets[2].setBackGroundColor(new Color(180,140,130));
        guiStreets[4].setBackGroundColor(new Color(150,220,250));
        guiStreets[5].setBackGroundColor(new Color(150,220,250));
        guiStreets[7].setBackGroundColor(new Color(255,120,250));
        guiStreets[8].setBackGroundColor(new Color(255,120,250));
        guiStreets[10].setBackGroundColor(Color.orange);
        guiStreets[11].setBackGroundColor(Color.orange);
        guiStreets[13].setBackGroundColor(new Color(250,60,60));
        guiStreets[14].setBackGroundColor(new Color(250,60,60));
        guiStreets[16].setBackGroundColor(Color.yellow);
        guiStreets[17].setBackGroundColor(Color.yellow);
        guiStreets[19].setBackGroundColor(new Color(70,170,90));
        guiStreets[20].setBackGroundColor(new Color(70,170,90));
        guiStreets[22].setBackGroundColor(new Color(110,110,250));
        guiStreets[23].setBackGroundColor(new Color(110,110,250));

        // sets up GUI chance fields
        for (int i = 0; i < 24; i=i+6) {
            guiFields[i+3] = new GUI_Chance();
            guiFields[i+3].setSubText("Chance");
            guiFields[i+3].setDescription("Take a chance card");
            guiFields[i+3].setBackGroundColor(Color.WHITE); }

        // sets up GUI START field
        guiFields[0] = new GUI_Start();
        guiFields[0].setDescription("Collect M$"+ Account.JAILFEE + " when you pass");
        guiFields[0].setTitle("START");
        guiFields[0].setSubText("Collect M$"+ Account.STARTBONUS);

        // sets up GUI jail fields
        guiFields[6] = new GUI_Jail();
        guiFields[6].setDescription("You go on a visit to jail");
        guiFields[6].setSubText("On visit to jail");
        guiFields[18] = new GUI_Jail();
        guiFields[18].setDescription("You are going into jail");
        guiFields[18].setSubText("Go to jail");

        // sets up GUI refuge field
        guiFields[12] = new GUI_Refuge();
        guiFields[12].setSubText("Free parking");
        guiFields[12].setBackGroundColor(Color.white);
    }

    // creates the GUI cars for each player
    private static void setupGUICars() {
        switch (game.getTotalPlayers()) {// note there is intentional run-through in the switch statement
            case 4: {
                guiCars[3] = new GUI_Car();
                guiCars[3].setPrimaryColor(new Color(170,60,250));
                guiCars[3].setSecondaryColor(Color.black);
            }
            case 3: {
                guiCars[2] = new GUI_Car();
                guiCars[2].setPrimaryColor(Color.GREEN);
                guiCars[2].setSecondaryColor(Color.lightGray);
            }
            case 2: {
                guiCars[1] = new GUI_Car();
                guiCars[1].setPrimaryColor(Color.BLUE);
                guiCars[1].setSecondaryColor(Color.CYAN);
                guiCars[0] = new GUI_Car();
                guiCars[0].setPrimaryColor(Color.RED);
                guiCars[0].setSecondaryColor(Color.ORANGE);
                break;
            }
            default: break;
        }
    }

    // adds the GUI players and cars to the board
    private static void addGUIPlayersAndCars() {
        for (int i = game.getTotalPlayers()-1; i >= 0; i--) {
            guiPlayers[i] = new GUI_Player(game.getPlayerObject(i+1).getName(),
                    Account.STARTINGBALANCE, guiCars[i]);
            gui.addPlayer(guiPlayers[i]);
            guiFields[0].setCar(guiPlayers[i], true); }
    }

    // resets the game after one game has ended, if the players choose to play again
    private static void resetGame() {
        gui.close(); // closes current gui board and then sets up a new one
        formatFields();
        gui = new GUI(guiFields, new Color(230,230,230));
        // sets all the cars on START and resets players' accounts
        addGUIPlayersAndCars();
        for (int i = 0; i < Board.getTotalNumberOfFields(); i++)
            if ("Game.AmusementField".equalsIgnoreCase(Game.getBoard().getFieldObject(i).getClassName())) {
            ((AmusementField)Game.getBoard().getFieldObject(i)).setOwnerNum(0); // removes owner from all fields
            ((AmusementField)Game.getBoard().getFieldObject(i)).resetRentToDefault();} // resets rents/prices to default level
        // sets players' accounts to the starting balance, sets player attributes to initial values, returns chance cards
        for (int i = 0; i< game.getTotalPlayers(); i++) {
            game.getPlayerObject(i + 1).getAccount().setCurrentBalance(Account.STARTINGBALANCE);
            game.getPlayerObject(i + 1).movePlayerToField(0);
            game.getPlayerObject(i + 1).setIsBankrupt(false);
            game.getPlayerObject(i + 1).setIsInJail(false);
            // returns a player's get out of jail chance cards to the deck of chance cards
            ChanceField.putBackChanceCard(game.getPlayerObject(i + 1).returnReleaseFromJailCard());
            // called twice as a player may own up to two chance card
            ChanceField.putBackChanceCard(game.getPlayerObject(i + 1).returnReleaseFromJailCard());
        }
        game.setCurrentPlayer(1); // sets the active player to player 1
        ChanceField.shuffleCards(); // shuffles the deck of chance cards
    }
}
