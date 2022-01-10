package Controllers;

import Models.GameSettings;
import Services.*;
import gui_fields.*;
import gui_main.GUI;
import java.awt.Color;

public class ViewController {
    // TODO: update class diagram with some of the new methods

    private GUI_Car[] guiCars;
    private GUI_Player[] guiPlayers;
    private GUI gui;
    private static ViewController single_instance;
    int numbOfFields = GameController.getInstance().getBoard().getTotalNumOfFields();
    private GUI_Field[] guiFields = new GUI_Field[numbOfFields];
    private GUI_Street[] guiStreets= new GUI_Street[numbOfFields];
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
        GUIBoardCreator service = new GUIBoardCreator(guiFields,guiStreets);
        guiFields = service.createGuiFields();
        guiStreets = service.createGuiStreets();
        gui = new GUI(guiFields, Color.WHITE);
    }

    public String[] getPlayerNames() {
        String str = gui.getUserString(setupGameGUIMessages[1]);
        String[] strarray = str.split(" ");
        int minPlayers = 3, maxPlayers = 6;
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
            gui.showMessage(String.format(setupGameGUIMessages[3], minPlayers, maxPlayers));
        } else {
            String names = "";
            for (int i = 0; i < strarray.length; i++) {
                if (i != strarray.length - 1)
                    names += String.format(setupGameGUIMessages[4] + " ", (i + 1)) + strarray[i] + ",   ";
                else
                    names += String.format(setupGameGUIMessages[4] + " ", (i + 1)) + strarray[i] ;
            }
            if (gui.getUserLeftButtonPressed(String.format(setupGameGUIMessages[5], names)+"  "+setupGameGUIMessages[6],
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
        gui.setDice(die1,(int) (Math.random() * 359),(int) (Math.random() * 3 + 1),
                (int) (Math.random() * 6 + 3), die2,
                (int) (Math.random() * 359),
                (int) (Math.random() * 3 + 4),
                (int) (Math.random() * 3 + 7)
        );
    }

    public void updateGUIBalance() {
        // updating and showing the updated gui balances for all players
        for (int i = 0; i < guiPlayers.length; i++)
            guiPlayers[i].setBalance(GameController.getInstance().getPlayerObject(i + 1).getAccount().getBalance());
    }

    //Take turn messages
    public void diceThrow(){
        gui.showMessage(takeTurnGUIMessages[1]);
    }

    public void okButton(){
        gui.showMessage(takeTurnGUIMessages[2]);
    }

    public void sameFaceValueMessage() {
        String name = getCurrentPlayerName();
        gui.showMessage(name + ": " + takeTurnGUIMessages[3]);
    }

    public void startBonusMessage(){
        int bonusStart = GameSettings.STARTBONUS;
        String name = getCurrentPlayerName();
        gui.showMessage(name + ": "+ String.format(takeTurnGUIMessages[4], bonusStart));
    }

    public void greenSquare(){
        gui.showMessage(takeTurnGUIMessages[5]);
    }

    public void goToJailMessage(){
        String name = getCurrentPlayerName();
        gui.showMessage(name+": "+takeTurnGUIMessages[6]);
    }

    public void useChanceCard(){
        gui.showMessage(takeTurnGUIMessages[7]);
    }

    //Game setup messages
    public void clickRules() {
        gui.showMessage(setupGameGUIMessages[9]);
    }

    public void rules() {
        gui.showMessage(setupGameGUIMessages[10]);
    }

    public void startGame() {
        gui.showMessage(setupGameGUIMessages[11]);
    }

    public void gameRules() {
        gui.showMessage(setupGameGUIMessages[12]);
    }

    public void showTakeTurnMessageWithPlayerName(int txtFileLineArrayNum) {
        gui.showMessage(getCurrentPlayerName() + ": " + takeTurnGUIMessages[txtFileLineArrayNum]);
    }
    // method overloading
    public void showTakeTurnMessageWithPlayerName(int txtFileLineArrayNum, String stringInText, String stringInText2) {
        gui.showMessage(getCurrentPlayerName() + ": " + String.format(takeTurnGUIMessages[txtFileLineArrayNum],stringInText, stringInText2));
    }
    public boolean showMessageAndGetBooleanUserInput(int txtFileLineQuestion, int txtFileLineTrueButton, int txtFileLineFalseButton) {
        return gui.getUserLeftButtonPressed(getCurrentPlayerName() + ": " + takeTurnGUIMessages[txtFileLineQuestion],
        takeTurnGUIMessages[txtFileLineTrueButton], takeTurnGUIMessages[txtFileLineFalseButton]);
    }
    // method overloading
    public boolean showMessageAndGetBooleanUserInput(int txtFileLineQuestion, int txtFileLineTrueButton,
        int txtFileLineFalseButton, String stringInText, String stringInText2) {
        return gui.getUserLeftButtonPressed(getCurrentPlayerName() + ": " +
        String.format(takeTurnGUIMessages[txtFileLineQuestion],stringInText,stringInText2),
        takeTurnGUIMessages[txtFileLineTrueButton], takeTurnGUIMessages[txtFileLineFalseButton]);
    }


    //Deniz
}