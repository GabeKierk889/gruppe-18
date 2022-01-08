package Controllers;

import Models.Account;
import Models.Board;
import Models.GameSettings;
import Services.FileImporter;
import gui_fields.*;
import gui_main.GUI;
import java.awt.Color;

public class ViewController {
    private GUI_Field[] guiFields;
    private GUI_Street[] guiStreets;
    private GUI_Car[] guiCars;
    private GUI_Player[] guiPlayers;
    private GUI gui;
    private static ViewController single_instance;
    int numbOfFields = GameController.getInstance().getBoard().getTotalNumOfFields();
    private String[] guiMessage;

    private ViewController() {
        FileImporter reader = new FileImporter();
        guiMessage = reader.readAllLinesInFile("GameMessages_takeTurn.txt");
    }

    public static ViewController getInstance() {
        if (single_instance == null)
            single_instance = new ViewController();
        return single_instance;
    }


    public void setupGUIBoard() {
        guiFields = new GUI_Field[numbOfFields];
        guiStreets = new GUI_Street[numbOfFields];
        Board board = GameController.getInstance().getBoard();

        for (int i = 0; i < board.getTotalNumOfFields(); i++) { // sets up all GUI fields/streets
            guiStreets[i] = new GUI_Street();
            guiStreets[i].setTitle(board.getFieldObject(i).getFieldName());
            guiStreets[i].setBackGroundColor(Color.lightGray);
            guiFields[i] = guiStreets[i];
        }

        gui = new GUI(guiFields, new Color(230, 230, 230));

        formatStreets();
    }

    public String[] getPlayerNames() {
        String str = gui.getUserString("On 1 line (separated by spaces only), enter the names of 2-4 players who will be playing today");
        String[] strarray = str.split(" ");
        boolean duplicateNames = false;
        for (int i = 0; i < strarray.length; i++) {
            for (int j = 0; j < strarray.length; j++)
                if (j != i && strarray[j].equals(strarray[i])) {
                    duplicateNames = true;
                    break;
                }
        }
        if (duplicateNames) {
            gui.showMessage("Error, two players cannot have exactly the same name. Please try again.");
        } else if (strarray.length > GameSettings.MAXNUMOFPLAYERS || strarray.length < GameSettings.MINNUMOFPLAYERS) {
//            gui.showMessage();
        } else {
            String names = "";
            for (int i = 0; i < strarray.length; i++) {
                if (i != strarray.length - 1)
                    names += "Player  " + (i + 1) + ": " + strarray[i] + ",   ";
                else
                    names += "Player  " + (i + 1) + ": " + strarray[i];
            }
            if (gui.getUserLeftButtonPressed("Here are the names that you entered," +
                            "and the order of the players' turns: \n" + names + "\n\n\t\t\tDo you want to start the game?",
                    "Yes", "No, reset names")) {
                return strarray;
            }
        }
        return getPlayerNames();
    }

    public void putPlayersOnBoard() {
        setupGUICars();
        setupGUIPlayers();
    }

    private void setupGUICars() {
        int totalPlayers = GameController.getInstance().getTotalPlayers();
        guiCars = new GUI_Car[totalPlayers];

        switch (totalPlayers) {
            case 6:
                guiCars[5] = new GUI_Car();
                guiCars[5].setPrimaryColor(Color.CYAN);
                guiCars[5].setSecondaryColor(Color.MAGENTA);
            case 5:
                guiCars[4] = new GUI_Car();
                guiCars[4].setPrimaryColor(Color.BLUE);
                guiCars[4].setSecondaryColor(Color.MAGENTA);
            case 4:
                guiCars[3] = new GUI_Car();
                guiCars[3].setPrimaryColor(Color.GREEN);
                guiCars[3].setSecondaryColor(Color.MAGENTA);
            case 3:
                guiCars[2] = new GUI_Car();
                guiCars[2].setPrimaryColor(Color.PINK);
                guiCars[2].setSecondaryColor(Color.MAGENTA);

                guiCars[1] = new GUI_Car();
                guiCars[1].setPrimaryColor(Color.BLACK);
                guiCars[1].setSecondaryColor(Color.MAGENTA);

                guiCars[0] = new GUI_Car();
                guiCars[0].setPrimaryColor(Color.RED);
                guiCars[0].setSecondaryColor(Color.MAGENTA);
                break;
            default:
                break;
        }
    }

    private void setupGUIPlayers() {
        int totalPlayers = GameController.getInstance().getTotalPlayers();
        guiPlayers = new GUI_Player[totalPlayers];

        for (int i = totalPlayers - 1; i >= 0; i--) {
            guiPlayers[i] = new GUI_Player(GameController.getInstance().getPlayerObject(i + 1).getName(),
                    GameSettings.STARTINGBALANCE, guiCars[i]);
            gui.addPlayer(guiPlayers[i]);
            guiFields[0].setCar(guiPlayers[i], true);
        }
    }

    public void moveGUICar() {
        GUI_Field previousGUIField = guiFields[GameController.getInstance().getPlayerObject(GameController.getInstance()
                        .getCurrentPlayerNum()).OnField()];
        GUI_Field newGUIField = guiFields[GameController.getInstance().getPlayerObject(GameController.getInstance()
                .getCurrentPlayerNum()).movePlayerSteps(GameController.getInstance().getDiceCup().getSum())];

        GUI_Player currentGUIPlayer = guiPlayers[GameController.getInstance().getCurrentPlayerNum() - 1];

        previousGUIField.setCar(currentGUIPlayer, false);

        newGUIField.setCar(currentGUIPlayer, true);
    }

    // TODO: Format fields.
    public void formatStreets() {
        guiStreets[1].setBackGroundColor(new Color(0,0,255));
    }

    public void rollMessage() {
        String name = getCurrentPlayerName();
        gui.getUserButtonPressed(name+guiMessage[0],guiMessage[1]);
    }

    private int currentPlayerNum() {
        return GameController.getInstance().getCurrentPlayerNum();
    }

    private String getCurrentPlayerName() {
        return GameController.getInstance().getPlayerObject(currentPlayerNum()).getName();
    }

    private String getPlayerName(int playerNum) {
        return GameController.getInstance().getPlayerObject(playerNum).getName();
    }

    public void updateGUIDice(int die1,int die2) {
        gui.setDice((int) (Math.random() * 4 + 3),
                (int) (Math.random() * 5 + 2),
                die1,
                (int) (Math.random() * 359),
                (int) (Math.random() * 4 + 7),
                (int) (Math.random() * 5 + 2),
                die2,
                (int) (Math.random() * 359));
    }
}