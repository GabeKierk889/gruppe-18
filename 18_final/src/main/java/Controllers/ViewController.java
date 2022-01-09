package Controllers;

import Models.Board;
import Models.GameSettings;
import Services.FileImporter;
import gui_fields.*;
import gui_main.GUI;
import java.awt.Color;
import java.io.FileReader;

public class ViewController {
    private GUI_Field[] guiFields;
    private GUI_Street[] guiStreets;
    private GUI_Car[] guiCars;
    private GUI_Player[] guiPlayers;
    private GUI gui;
    private static ViewController single_instance;
    int numbOfFields = GameController.getInstance().getBoard().getTotalNumOfFields();
    private String[] takeTurnGUIMessages;
    private String[] setupGameGUIMessages;

    private ViewController() {
        FileImporter reader = new FileImporter();
        takeTurnGUIMessages = reader.readAllLinesInFile("GameMessages_takeTurn.txt");
        setupGameGUIMessages = reader.readAllLinesInFile("GameMessages_setupGame.txt");
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

        gui = new GUI(guiFields, Color.lightGray);

        colorStreets();
        setupShipping();
    }

    public String[] getPlayerNames() {
        String str = gui.getUserString(setupGameGUIMessages[1]);
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
            gui.showMessage(setupGameGUIMessages[2]);
        } else if (strarray.length > GameSettings.MAXNUMOFPLAYERS || strarray.length < GameSettings.MINNUMOFPLAYERS) {
//            gui.showMessage();
        } else {
            String names = "";
            for (int i = 0; i < strarray.length; i++) {
                if (i != strarray.length - 1)
                    names += String.format(setupGameGUIMessages[4] + " ", (i + 1)) + strarray[i] + ",   ";
                else
                    names += String.format(setupGameGUIMessages[4] + " ", (i + 1)) + strarray[i];
            }
            if (gui.getUserLeftButtonPressed(String.format(setupGameGUIMessages[5], names),
                    setupGameGUIMessages[7],setupGameGUIMessages[8])) {
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
                guiCars[5].setPrimaryColor(Color.PINK);
            case 5:
                guiCars[4] = new GUI_Car();
                guiCars[4].setPrimaryColor(new Color(180,30,250));
            case 4:
                guiCars[3] = new GUI_Car();
                guiCars[3].setPrimaryColor(new Color(90,130,250));
            case 3:
                guiCars[2] = new GUI_Car();
                guiCars[2].setPrimaryColor(new Color(50,180,50));
            case 2: // for added flexibility, in case MINNUMOFPLAYERS is changed to 2 instead of 3
                guiCars[1] = new GUI_Car();
                guiCars[1].setPrimaryColor(Color.CYAN);
                guiCars[0] = new GUI_Car();
                guiCars[0].setPrimaryColor(new Color(255,100,150));
                break;
        }
    }

    private void setupGUIPlayers() {
        int totalPlayers = GameController.getInstance().getTotalPlayers();
        guiPlayers = new GUI_Player[totalPlayers];

        for (int i = totalPlayers - 1; i >= 0; i--) {
            guiPlayers[i] = new GUI_Player(getPlayerName(i+1),
                    GameSettings.STARTINGBALANCE, guiCars[i]);
            gui.addPlayer(guiPlayers[i]);
            guiFields[0].setCar(guiPlayers[i], true);
        }
    }

    public void moveGUICar(int moveFrom, int moveTo, int currentPlayerNum) {
        GUI_Player currentGUIPlayer = guiPlayers[currentPlayerNum - 1];
        guiFields[moveFrom].setCar(currentGUIPlayer, false);
        guiFields[moveTo].setCar(currentGUIPlayer, true);
    }

    private void colorStreets() {
        Color orange = new Color(255,165,0);
        Color lilla = new Color(128,0,128);

        guiStreets[1].setBackGroundColor(Color.blue);
        guiStreets[3].setBackGroundColor(Color.blue);

        guiStreets[6].setBackGroundColor(orange);
        guiStreets[8].setBackGroundColor(orange);
        guiStreets[9].setBackGroundColor(orange);

        guiStreets[11].setBackGroundColor(Color.green);
        guiStreets[13].setBackGroundColor(Color.green);
        guiStreets[14].setBackGroundColor(Color.green);

        guiStreets[16].setBackGroundColor(Color.gray);
        guiStreets[18].setBackGroundColor(Color.gray);
        guiStreets[19].setBackGroundColor(Color.gray);

        guiStreets[21].setBackGroundColor(Color.red);
        guiStreets[23].setBackGroundColor(Color.red);
        guiStreets[24].setBackGroundColor(Color.red);

        guiStreets[26].setBackGroundColor(Color.white);
        guiStreets[27].setBackGroundColor(Color.white);
        guiStreets[29].setBackGroundColor(Color.white);

        guiStreets[31].setBackGroundColor(Color.yellow);
        guiStreets[32].setBackGroundColor(Color.yellow);
        guiStreets[34].setBackGroundColor(Color.yellow);

        guiStreets[37].setBackGroundColor(lilla);
        guiStreets[39].setBackGroundColor(lilla);

    }

    private void setupShipping() {
        guiFields[5] = new GUI_Shipping();
        guiFields[15] = new GUI_Shipping();
        guiFields[25] = new GUI_Shipping();
        guiFields[35] = new GUI_Shipping();
    }

    public void rollMessage() {
        String name = getCurrentPlayerName();
        gui.getUserButtonPressed(name+ takeTurnGUIMessages[0], takeTurnGUIMessages[1]);
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
        gui.setDice(die1,(int) (Math.random() * 359),(int) (Math.random() * 4 + 3),
                (int) (Math.random() * 5 + 2), die2,
                (int) (Math.random() * 359),
                (int) (Math.random() * 4 + 7),
                (int) (Math.random() * 5 + 2)
        );
    }

    // TODO: Uncomment later
    public void jailMessage(){
        String name = getCurrentPlayerName();
        gui.showMessage(name+takeTurnGUIMessages[6]);
    }

    public void startBonusMessage(){
        int bonusStart = GameSettings.STARTBONUS;
        String name = getCurrentPlayerName();
        gui.showMessage(String.format(takeTurnGUIMessages[4], bonusStart));
    }

    public void sameFaceValueMessage(){
        String name = getCurrentPlayerName();
        gui.showMessage(takeTurnGUIMessages[4]);

    }
}