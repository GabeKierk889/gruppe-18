import gui_fields.*;
import gui_main.GUI;
import java.awt.*;
import java.util.Scanner;

public class MonopolyGame {
    static Game game;
    private static GUI_Field[] fields;
    private static GUI_Street[] streets;
    private static GUI_Car[] car;
    private static GUI_Player[] player;
    private static GUI gui;

    public static void main(String[] args) {
        // Calling a support method that takes user input/names and initializes the game with 2-4 players
        initializeGame();

        // Setting up and formatting GUI fields and streets (using a support method)
        fields = new GUI_Field[Field.getTotalnumberOfFields()];
        streets = new GUI_Street[Field.getTotalnumberOfFields()];
        formatFields();

        // Setting up GUI board
        gui = new GUI(fields, new Color(230,230,230));

        // Setting up the GUI cars (using a support method)
        car = new GUI_Car[game.getTotalPlayers()];
        setupGUICars();

        // Setting up GUI players and placing their cars on the game board (using a support method)
        player = new GUI_Player[game.getTotalPlayers()];
        addGUIPlayersAndCars();

        // Displays a welcome message and allows the players to view the rules before starting the game
        showWelcomeMessage();

        // checking that the game is not yet over (meaning no player has gone bankrupt)
        while (!game.isGameOver()) {

                // if the current player is in jail, an alternative flow is run first before the main flow
                inJail();

                // displays a message for a player to roll the die, then rolls the die
                roll(gui.getUserButtonPressed(player[game.getCurrentPlayerNumber()-1].getName() + "'s" + " turn. Press to roll the die","Roll"));

                // removes the current player's car from the previous field they stood on
                fields[game.getPlayerObject(game.getCurrentPlayerNumber()).OnField()].setCar(
                player[game.getCurrentPlayerNumber()-1], false);

                // sets the player's car on the street corresponding to the latest dice roll
                fields[game.getPlayerObject(game.getCurrentPlayerNumber()).movePlayer(game.getDie().getFaceValue())].
                setCar(player[game.getCurrentPlayerNumber()-1], true);

                // the player gets a START bonus if they land on/ pass START via a regular move after a dice-throw
                // (not via chance card situations)
                game.getPlayerObject(game.getCurrentPlayerNumber()).collectStartBonus(game.getDie().getFaceValue());

                // showing/ updating the player's balance, in case they got a START bonus
                updatePlayerBalance();

                // displays message to current player about the actions of their turn
                gui.showMessage(playerTurnMessage());

                // calls landOnField method which will do something if it is an amusement field, jail, or chance field
                game.getBoard().getFieldObject(game.getPlayerObject(game.getCurrentPlayerNumber()).OnField()).
                landOnField(game.getPlayerObject(game.getCurrentPlayerNumber()));

                // changes to the next player's turn (if the current player does not get an extra turn)
                game.switchTurn(false);
            }

            if (game.isGameOver()) {
                game.determineWinner(); // determine the winner based on the game rules
                updatePlayerBalance();
                gui.showMessage("The game is over as "+ game.getBankruptPlayerName()+" has gone bankrupt." +
                "\nThe winner of this game is " + game.getPlayerObject(game.getWinner()).getName());
            }
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
                if (gui.getUserLeftButtonPressed(player[game.getCurrentPlayerNumber()-1].getName() + ": You are in jail. Do you want to use your " +
                "\"Get out of jail for free\" card now? You can only use the card once.","Yes","No"))
                {
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

            // displays a message and withdraws get out of jail fee from the player's balance
            if(gui.getUserButtonPressed(player[game.getCurrentPlayerNumber()-1].getName() + ":" +
            "\nYou are in jail. You need to pay M$"+ Account.JAILFEE + " to be released from jail", "Pay").equalsIgnoreCase("pay"));
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
        System.out.println("On 1 line (separated by spaces only), enter the names of 2-4 players who will be playing today");
        Scanner scan = new Scanner(System.in);
        String str = scan.nextLine();
        scan.close();
        String[] strarray = str.split(" ");
        switch (strarray.length) { // initializes a new game with the player names entered
            case 2: game = new Game(strarray[0],strarray[1]); break;
            case 3: game = new Game(strarray[0],strarray[1],strarray[2]); break;
            case 4: game = new Game(strarray[0],strarray[1],strarray[2],strarray[3]); break;
            default: game = new Game(strarray[0]);
        }
    }

    private static void showWelcomeMessage() {
        if (gui.getUserLeftButtonPressed("Welcome to Monopoly Junior! Press \"Help\" to view the rules, or press \"Start game\" to begin the game.     "
                + player[0].getName() + " will go first","Help","Start game"))
            gui.showMessage("Rules: Each player starts with M$35. On a player's turn, if they land on an amusement without a booth, the player must pay the bank to set up a booth. " +
                    "If there is already a booth owned by another player, the current player must pay the owner of the booth. " +
                    "If a player lands on Chance, the player must draw a chance card and follow the instructions. " +
                    "If a player lands on or passes START, the player gets M$2. " +
                    "If a player lands on “Go to Jail”, the player goes into jail. " +
                    "To get out of jail, the player may use a \"get out of jail\" chance card, " +
                    "or pay M$1 to get out of jail on their next turn. " +
                    "If a player runs out of money, the balances of the remaining players are compared, and the player with the most money wins the game.");
    }

    private static String playerTurnMessage() {
        String str = "";
        int fieldnum = game.getPlayerObject(game.getCurrentPlayerNumber()).OnField();
        str += player[game.getCurrentPlayerNumber()-1].getName() +", you landed on " +
                game.getBoard().getFieldObject(fieldnum) + ".\n";
        if (fieldnum < game.getDie().getFaceValue())
            str += "You have received M$"+ Account.STARTBONUS +" for passing START\n";
        if (fieldnum == 6)
            str += "You go on a visit to jail\nNext player's turn";
        else if (fieldnum == 12)
            str += "Hurrah for free parking!\nNext player's turn";
        else if (fieldnum%6 != 0 && fieldnum%3 == 0)
            str += "Take a chance card and follow the instructions";
        return str;
    }

    static void goToJailMessage() {
        gui.showMessage("Oh no! You are going into jail. On your next turn, you may use a \"Get out of Jail\" chance card, " +
                "if you have one, to get out of jail. Otherwise, on your next turn, you need to pay M$"+ Account.JAILFEE +" to get out.");
    }

    static void setupBoothMessage() {
        gui.getUserButtonPressed("Press the button to setup a booth on this field", "Setup booth");
        updatePlayerBalance();
        gui.showMessage("You have now paid M$" +
                ( (AmusementField) game.getBoard().getFieldObject(game.getPlayerObject(game.getCurrentPlayerNumber()).OnField()) ).getPrice() +
                " to setup a booth");
    }

    static void payBoothPriceMessage() {
        int owner = ( (AmusementField) game.getBoard().getFieldObject(game.getPlayerObject(game.getCurrentPlayerNumber()).OnField()) ).getOwnerNum();
        int price = ( (AmusementField) game.getBoard().getFieldObject(game.getPlayerObject(game.getCurrentPlayerNumber()).OnField()) ).getPrice();
        gui.getUserButtonPressed("You have landed on " + game.getPlayerObject(owner).getName() + "'s booth"
                , "Pay M$" + price);
        updatePlayerBalance();
        gui.showMessage("You have now paid M$" + price + " to "+ game.getPlayerObject(owner).getName());
    }

    static void chanceCardMessage()
    {
        gui.setChanceCard(ChanceField.getCurrentCard().chanceCardText());
        gui.getUserButtonPressed("Press the green square to view your chance card. When you are done reading, " +
        "press the 'Apply' button to apply the instructions of the chance card","Apply");
        gui.setChanceCard(""); // otherwise the text will not be removed
        updatePlayerBalance();
    }

    static void updatePlayerBalance () {
        // updating and showing the updated balances for all players
        for (int i = 0; i< game.getTotalPlayers(); i++)
            player[i].setBalance(game.getPlayerObject(i+1).getAccount().getBalance());
    }

    private static void formatFields() {
        for (int i = 0; i < 24; i++) { // sets up all GUI fields/streets
            streets[i] = new GUI_Street();
            streets[i].setTitle(game.getBoard().getFieldObject(i).getFieldDescription());
            streets[i].setBackGroundColor(Color.lightGray);
            fields[i] = streets[i]; }

        for (int i = 0; i < 24; i++) { // displays the price and description for the ownable fields
            if (i%3 != 0) {
                String str = "M$";
                streets[i].setSubText(str + ((AmusementField)game.getBoard().getFieldObject(i)).getPrice());
                streets[i].setDescription(game.getBoard().getFieldObject(i).getFieldDescription());
            }
        }

        // gives the ownable fields the same colors as the real-life MonopolyJr game board
        streets[1].setBackGroundColor(new Color(180,140,130));
        streets[2].setBackGroundColor(new Color(180,140,130));
        streets[4].setBackGroundColor(new Color(150,220,250));
        streets[5].setBackGroundColor(new Color(150,220,250));
        streets[7].setBackGroundColor(new Color(255,120,250));
        streets[8].setBackGroundColor(new Color(255,120,250));
        streets[10].setBackGroundColor(Color.orange);
        streets[11].setBackGroundColor(Color.orange);
        streets[13].setBackGroundColor(new Color(250,60,60));
        streets[14].setBackGroundColor(new Color(250,60,60));
        streets[16].setBackGroundColor(Color.yellow);
        streets[17].setBackGroundColor(Color.yellow);
        streets[19].setBackGroundColor(new Color(70,170,90));
        streets[20].setBackGroundColor(new Color(70,170,90));
        streets[22].setBackGroundColor(new Color(110,110,250));
        streets[23].setBackGroundColor(new Color(110,110,250));

        // sets up GUI chance fields
        for (int i = 0; i < 24; i=i+6) {
            fields[i+3] = new GUI_Chance();
            fields[i+3].setSubText("Chance");
            fields[i+3].setDescription("Take a chance card");
            fields[i+3].setBackGroundColor(Color.WHITE); }

        // sets up GUI START field
        fields[0] = new GUI_Start();
        fields[0].setDescription("Collect M$"+ Account.JAILFEE + " when you pass");
        fields[0].setTitle("START");
        fields[0].setSubText("Collect M$"+ Account.STARTBONUS);

        // sets up GUI jail fields
        fields[6] = new GUI_Jail();
        fields[6].setDescription("You go on a visit to jail");
        fields[6].setSubText("On visit to jail");
        fields[18] = new GUI_Jail();
        fields[18].setDescription("You are going into jail");
        fields[18].setSubText("Go to jail");

        // sets up GUI refuge field
        fields[12] = new GUI_Refuge();
        fields[12].setSubText("Free parking");
        fields[12].setBackGroundColor(Color.white);
    }

    private static void setupGUICars() {
        switch (game.getTotalPlayers()) {// note there is intentional run-through in the switch statement
            case 4: {
                car[3] = new GUI_Car();
                car[3].setPrimaryColor(new Color(170,60,250));
                car[3].setSecondaryColor(Color.black);
            }
            case 3: {
                car[2] = new GUI_Car();
                car[2].setPrimaryColor(Color.GREEN);
                car[2].setSecondaryColor(Color.lightGray);
            }
            case 2: {
                car[1] = new GUI_Car();
                car[1].setPrimaryColor(Color.BLUE);
                car[1].setSecondaryColor(Color.CYAN);
                car[0] = new GUI_Car();
                car[0].setPrimaryColor(Color.RED);
                car[0].setSecondaryColor(Color.ORANGE);
                break;
            }
            default: break;
        }
    }

    private static void addGUIPlayersAndCars() {
        for (int i = game.getTotalPlayers()-1; i >= 0; i--) {
            player[i] = new GUI_Player(game.getPlayerObject(i+1).getName(),
                    Account.STARTINGBALANCE, car[i]);
            gui.addPlayer(player[i]);
            fields[0].setCar(player[i], true); }
    }
}
