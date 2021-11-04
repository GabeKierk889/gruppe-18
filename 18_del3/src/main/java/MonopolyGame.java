import gui_fields.*;

import java.awt.*;
import java.util.Scanner;

public class MonopolyGame {
    private static Game game;
    private static GUI_Field[] fields;
    private static GUI_Street[] streets;
    private static GUI_Car[] car;
    private static GUI_Player[] player;
    private static GUI_Board board;

    public static void main(String[] args) {
        // Calling a support method that takes user input/names and initializes the game with 2-4 players
        initializeGame();

        // Setting up and formatting GUI fields and streets (using a support method)
        fields = new GUI_Field[Field.getTotalnumberOfFields()];
        streets = new GUI_Street[Field.getTotalnumberOfFields()];
        formatFields();

        // Setting up GUI board
        board = new GUI_Board(fields, new Color(230,230,230));

        // Setting up the GUI cars (using a support method)
        car = new GUI_Car[game.getTotalPlayers()];
        setupGUICars();

        // Setting up GUI players and placing their cars on the game board (using a support method)
        player = new GUI_Player[game.getTotalPlayers()];
        addGUIPlayersAndCars();

        // Start up message in GUI (can be used for user input with second arg)
        board.getUserInput("Welcome to Monopoly Junior!\n\n" +
                player[0].getName() + "'s turn\nClick anywhere to roll the dice");

        // Referencing the GUI die to the die set up by the game
        Die die1 = game.getDie();


    }

    private static void initializeGame() {
        System.out.println("Enter the names of 2-4 players who will be playing today (on 1 line separated by spaces only)");
        Scanner scan = new Scanner(System.in);
        String str = scan.nextLine();
        scan.close();
        String[] strarray = str.split(" ");
        switch (strarray.length) {
            case 2: game = new Game(strarray[0],strarray[1]); break;
            case 3: game = new Game(strarray[0],strarray[1],strarray[2]); break;
            case 4: game = new Game(strarray[0],strarray[1],strarray[2],strarray[3]); break;
            default: game = new Game(strarray[0]);
        }
    }

    private static void formatFields() {
        for (int i = 0; i < 24; i++) { // sets up all fields
            streets[i] = new GUI_Street();
            streets[i].setTitle(game.getBoard().getFieldObject(i).getFieldDescription());
            streets[i].setBackGroundColor(Color.lightGray);
            fields[i] = streets[i]; }

        for (int i = 0; i < 24; i++) { // displays price for the ownable fields
            if (i%3 != 0) {
                String str = "M$";
                streets[i].setSubText(str + ((AmusementField)game.getBoard().getFieldObject(i)).getPrice());
            }
        }

        // gives the ownable fields the same colors as the real-life game board
        streets[1].setBackGroundColor(new Color(180,140,130));
        streets[2].setBackGroundColor(new Color(180,140,130));
        streets[4].setBackGroundColor(new Color(150,220,250));
        streets[5].setBackGroundColor(new Color(150,220,250));
        streets[7].setBackGroundColor(Color.magenta);
        streets[8].setBackGroundColor(Color.magenta);
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
        fields[0].setDescription("Collect M$2 when you pass");
        fields[0].setTitle("START");
        fields[0].setSubText("Collect M$2");

        // sets up GUI jail fields
        fields[6] = new GUI_Jail();
        fields[6].setDescription("You go on visit to jail");
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
                car[3].setPrimaryColor(Color.magenta);
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
            player[i]= new GUI_Player(game.getPlayerObject(i+1).getName(),
                    game.getPlayerObject(i+1).getAccount().getBalance(), car[i]);
            board.addPlayer(player[i]);
            fields[0].setCar(player[i], true); }
    }
}
