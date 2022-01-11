package Controllers;

import Models.GameSettings;
import Models.OwnableField;
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
    private String[] chanceCardMessages;

    private ViewController() {
        FileImporter reader = new FileImporter();
        takeTurnGUIMessages = reader.readAllLinesInFile("GameMessages_takeTurn.txt");
        setupGameGUIMessages = reader.readAllLinesInFile("GameMessages_setupGame.txt");
        chanceCardMessages = reader.readAllLinesInFile("Chancecards_text.txt");
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

    public void removeGUICar(int playerNum, int playeronfieldNum) {
        guiFields[playeronfieldNum].setCar(guiPlayers[playerNum-1], false);
    }

    public void moveGUICar(int moveFrom, int moveTo, int currentPlayerNum) {
        GUI_Player currentGUIPlayer = guiPlayers[currentPlayerNum - 1];
        guiFields[moveFrom].setCar(currentGUIPlayer, false);
        guiFields[moveTo].setCar(currentGUIPlayer, true);
    }

    public void setGUIHasHotel(int fieldArrayNum, boolean hasHotel) {
        guiStreets[fieldArrayNum].setHotel(hasHotel);
    }

    public void setGUINumHouses(int fieldArrayNum, int numHouses) {
        guiStreets[fieldArrayNum].setHouses(numHouses);
    }

    public void rollMessage() {
        String name = getCurrentPlayerName();
        gui.getUserButtonPressed(name+": "+ takeTurnGUIMessages[0], takeTurnGUIMessages[1]);
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

    public void sameFaceValueMessage() {
        String name = getCurrentPlayerName();
        gui.getUserButtonPressed(name+": "+ takeTurnGUIMessages[3]+" "+takeTurnGUIMessages[0], takeTurnGUIMessages[1]);
    }

    public void startBonusMessage(){
        int bonusStart = GameSettings.STARTBONUS;
        String name = getCurrentPlayerName();
        updateGUIBalance();
        gui.showMessage(name + ": "+ String.format(takeTurnGUIMessages[4], bonusStart));
    }

    public void goToJailMessage(){
        String name = getCurrentPlayerName();
        gui.showMessage(name + ": "+ takeTurnGUIMessages[6]);
    }

    public boolean releaseFromJailMessageHasCard() {
        return gui.getUserLeftButtonPressed(String.format(takeTurnGUIMessages[7],GameController.getInstance().getPlayerObject(currentPlayerNum()).getName()), takeTurnGUIMessages[15], takeTurnGUIMessages[16]);
    }

    public void releaseFromJailMessagePayMoney() {
        gui.showMessage(String.format(takeTurnGUIMessages[34],GameController.getInstance().getPlayerObject(currentPlayerNum()).getName(),takeTurnGUIMessages[35]+" "+GameSettings.JAILFEE));
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

    public String getTakeTurnGUIMessages(int txtLineArray) { return takeTurnGUIMessages[txtLineArray]; }
    // method overloading
    public String getTakeTurnGUIMessages(int txtLineArray, String stringInText, String stringInText2, String stringInText3) {
        return String.format(takeTurnGUIMessages[txtLineArray],stringInText,stringInText2,stringInText3);}
    public String getTakeTurnGUIMessages(int txtLineArray, int stringInTextLine) {
       return String.format(takeTurnGUIMessages[txtLineArray],takeTurnGUIMessages[stringInTextLine]);
    }
    // method overloading
    public void showTakeTurnMessageWithPlayerName(int txtFileLineArrayNum, String stringInText, String stringInText2, String stringInText3) {
        gui.showMessage(getCurrentPlayerName() + ": " + String.format(takeTurnGUIMessages[txtFileLineArrayNum],stringInText, stringInText2, stringInText3));
    }
    // method overloading
    public void showTakeTurnMessageWithPlayerName(String customString, int txtFileLineArrayNum) {
        gui.showMessage(getCurrentPlayerName() + ": " +customString + " " + takeTurnGUIMessages[txtFileLineArrayNum]);
    }
    // method overloading
    public void showTakeTurnMessageWithPlayerName(int txtFileLineArrayNum, int txtFileLineArrayNum2, int txtFileLineArrayNum3, String stringInText, String stringInText2, String stringInText3) {
       String str = takeTurnGUIMessages[txtFileLineArrayNum];
       if (txtFileLineArrayNum2 != -1)
           str += " " + takeTurnGUIMessages[txtFileLineArrayNum2];
        if (txtFileLineArrayNum3 != -1)
            str += " " + takeTurnGUIMessages[txtFileLineArrayNum3];
        gui.showMessage(getCurrentPlayerName() + ": " + String.format(str,stringInText, stringInText2, stringInText3));
    }
    // method overloading
    public void showTakeTurnMessageWithPlayerName(String customString, int txtFileLineArrayNum, int txtFileLineArrayNum2, int txtFileLineArrayNum3, String stringInText, String stringInText2, String stringInText3) {
        String str = takeTurnGUIMessages[txtFileLineArrayNum];
        if (txtFileLineArrayNum2 != -1)
            str += " " + takeTurnGUIMessages[txtFileLineArrayNum2];
        if (txtFileLineArrayNum3 != -1)
            str += " " + takeTurnGUIMessages[txtFileLineArrayNum3];
        gui.showMessage(getCurrentPlayerName() + ": " + customString + " " + String.format(str,stringInText, stringInText2, stringInText3));
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
    // method overloading
    public String getBuyOrSellBuildingsUserInput (int dropDownLine1,int stringInText) {
        int lineArrayNum = 55;
        return gui.getUserSelection(getCurrentPlayerName() + ": " + takeTurnGUIMessages[lineArrayNum],takeTurnGUIMessages[16],
                String.format(takeTurnGUIMessages[dropDownLine1],takeTurnGUIMessages[stringInText]));
    }
    public String getBuyOrSellBuildingsUserInput (int dropDownLine1, int dropDownLine2
            , int stringInText, int stringInText2) {
        int lineArrayNum = 55;
        return gui.getUserSelection(getCurrentPlayerName() + ": " + takeTurnGUIMessages[lineArrayNum],takeTurnGUIMessages[16],
                String.format(takeTurnGUIMessages[dropDownLine1],takeTurnGUIMessages[stringInText]),String.format(takeTurnGUIMessages[dropDownLine2],takeTurnGUIMessages[stringInText2]));
    }
    public String getBuyOrSellBuildingsUserInput (int dropDownLine1, int dropDownLine2, int dropDownLine3
            , int stringInText, int stringInText2, int stringInText3) {
        int lineArrayNum = 55;
        return gui.getUserSelection(getCurrentPlayerName() + ": " + takeTurnGUIMessages[lineArrayNum],takeTurnGUIMessages[16],
                String.format(takeTurnGUIMessages[dropDownLine1],takeTurnGUIMessages[stringInText]),String.format(takeTurnGUIMessages[dropDownLine2],takeTurnGUIMessages[stringInText2]),String.format(takeTurnGUIMessages[dropDownLine3],takeTurnGUIMessages[stringInText3]));
    }
    public String getBuyOrSellBuildingsUserInput (int dropDownLine1, int dropDownLine2, int dropDownLine3, int dropDownLine4
            , int stringInText, int stringInText2, int stringInText3, int stringInText4) {
        int lineArrayNum = 55;
        return gui.getUserSelection(getCurrentPlayerName() + ": " + takeTurnGUIMessages[lineArrayNum],takeTurnGUIMessages[16],
                String.format(takeTurnGUIMessages[dropDownLine1],takeTurnGUIMessages[stringInText]),String.format(takeTurnGUIMessages[dropDownLine2],takeTurnGUIMessages[stringInText2]),String.format(takeTurnGUIMessages[dropDownLine3],takeTurnGUIMessages[stringInText3]),String.format(takeTurnGUIMessages[dropDownLine4],takeTurnGUIMessages[stringInText4]));
    }

    public void drawChanceCardMessage(String text) {
        gui.setChanceCard(text);
        String name = getCurrentPlayerName();
        gui.getUserButtonPressed(name + ": "+ takeTurnGUIMessages[5],takeTurnGUIMessages[17]);
        gui.setChanceCard("");
    }

    // this methods code is modified from previous assignment CDIO 3 by Maj Kyllesbech, Gabriel H, Kierkegaard, Mark Bidstrup & Xiao Chen handed in 26. November 2021
    public void formatFieldBorder(int fieldArrayNum) {
        boolean isShippingField = GameController.getInstance().getBoard().getFieldObject(fieldArrayNum).isShippingField();
        boolean isBreweryField = GameController.getInstance().getBoard().getFieldObject(fieldArrayNum).isBreweryField();

        switch (((OwnableField)GameController.getInstance().getBoard().getFieldObject(fieldArrayNum)).getOwnerNum()) {
            case 1: {
                if (isShippingField || isBreweryField) {
                    guiFields[fieldArrayNum].setForeGroundColor(guiPlayers[0].getPrimaryColor());
                } else {
                    guiStreets[fieldArrayNum].setBorder(guiPlayers[0].getPrimaryColor());
                }
                break; }
            case 2: {
                if (isShippingField || isBreweryField) {
                    guiFields[fieldArrayNum].setForeGroundColor(guiPlayers[1].getPrimaryColor());
                } else {
                    guiStreets[fieldArrayNum].setBorder(guiPlayers[1].getPrimaryColor());
                }
                break; }
            case 3: {
                if (isShippingField || isBreweryField) {
                    guiFields[fieldArrayNum].setForeGroundColor(guiPlayers[2].getPrimaryColor());
                } else {
                    guiStreets[fieldArrayNum].setBorder(guiPlayers[2].getPrimaryColor());
                }
                break; }
            case 4: {
                if (isShippingField || isBreweryField) {
                    guiFields[fieldArrayNum].setForeGroundColor(guiPlayers[3].getPrimaryColor());
                } else {
                    guiStreets[fieldArrayNum].setBorder(guiPlayers[3].getPrimaryColor());
                }
                break; }
            case 5: {
                if (isShippingField || isBreweryField) {
                    guiFields[fieldArrayNum].setForeGroundColor(guiPlayers[4].getPrimaryColor());
                } else {
                    guiStreets[fieldArrayNum].setBorder(guiPlayers[4].getPrimaryColor());
                }
                break; }
            case 6: {
                if (isShippingField || isBreweryField) {
                    guiFields[fieldArrayNum].setForeGroundColor(guiPlayers[5].getPrimaryColor());
                } else {
                    guiStreets[fieldArrayNum].setBorder(guiPlayers[5].getPrimaryColor());
                }
                break; }
            default: break;
        }
    }

}