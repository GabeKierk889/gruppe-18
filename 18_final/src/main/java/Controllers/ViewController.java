package Controllers;

import Models.GameSettings;
import Models.OwnableField;
import Models.StreetField;
import Services.*;
import gui_fields.*;
import gui_main.GUI;

public class ViewController {

    private GUI_Player[] guiPlayers;
    private GUI gui;
    private static ViewController single_instance;
    private final int totalNumOfFields = GameController.getInstance().getBoard().getTotalNumOfFields();
    private GUI_Field[] guiFields = new GUI_Field[totalNumOfFields];
    private GUI_Street[] guiStreets= new GUI_Street[totalNumOfFields];
    private final String[] takeTurnGUIMessages;

    private ViewController() {
        FileImporter reader = new FileImporter();
        takeTurnGUIMessages = reader.readAllLinesInFile("GameMessages_takeTurn.txt");
    }

    public static ViewController getInstance() {
        if (single_instance == null)
            single_instance = new ViewController();
        return single_instance;
    }

    public void setupGUIBoard() {
        gui = ViewController_GameSetup.getInstance().setupGUIBoard(guiFields,guiStreets);
    }

    public String[] getPlayerNames() {
        return ViewController_GameSetup.getInstance().getPlayerNames(gui);
    }

    public void putPlayersOnBoard() {
        int totalPlayers = GameController.getInstance().getTotalPlayers();
        GUI_Car[] guiCars = new GUI_Car[totalPlayers];
        ViewController_GameSetup.getInstance().setupGUICars(totalPlayers, guiCars);
        guiPlayers = new GUI_Player[totalPlayers];
        for (int i = totalPlayers - 1; i >= 0; i--) {
            guiPlayers[i] = new GUI_Player(getPlayerName(i+1), GameSettings.STARTINGBALANCE, guiCars[i]);
            gui.addPlayer(guiPlayers[i]);
            guiFields[0].setCar(guiPlayers[i], true);
        }
    }

    public void moveGUICar(int moveFrom, int moveTo, int currentPlayerNum) {
        GUI_Player currentGUIPlayer = guiPlayers[currentPlayerNum - 1];
        guiFields[moveFrom].setCar(currentGUIPlayer, false);
        guiFields[moveTo].setCar(currentGUIPlayer, true);
    }

    public void removeGUICar(int playerNum, int playerOnFieldNum) {
        guiFields[playerOnFieldNum].setCar(guiPlayers[playerNum-1], false);
    }

    public void rollMessage() {
        String name = getCurrentPlayerName();
        gui.getUserButtonPressed(name+": "+ takeTurnGUIMessages[0], takeTurnGUIMessages[1]);
    }

    public void updateGUIDice(int die1,int die2) {
        gui.setDice(die1,(int) (Math.random() * 359),(int) (Math.random() * 3 + 1),
                (int) (Math.random() * 6 + 3), die2,
                (int) (Math.random() * 359),
                (int) (Math.random() * 3 + 4),
                (int) (Math.random() * 3 + 7));
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

    // method overloading
    public String getTakeTurnGUIMessages(int txtLineArray) { return takeTurnGUIMessages[txtLineArray]; }
    public String getTakeTurnGUIMessages(int txtLineArray, String stringInText, String stringInText2, String stringInText3) {
        return String.format(takeTurnGUIMessages[txtLineArray],stringInText,stringInText2,stringInText3);}
    public String getTakeTurnGUIMessages(int txtLineArray, int stringInTextLine) {
       return String.format(takeTurnGUIMessages[txtLineArray],takeTurnGUIMessages[stringInTextLine]);}
    public void showTakeTurnMessageWithPlayerName(int txtFileLineArrayNum, String stringInText, String stringInText2, String stringInText3) {
        gui.showMessage(getCurrentPlayerName() + ": " + String.format(takeTurnGUIMessages[txtFileLineArrayNum],stringInText, stringInText2, stringInText3));}
    public void showTakeTurnMessageWithPlayerName(String customString, int txtFileLineArrayNum) {
        gui.showMessage(getCurrentPlayerName() + ": " +customString + " " + takeTurnGUIMessages[txtFileLineArrayNum]);}
    public void showTakeTurnMessageWithPlayerName(String customString) {
        gui.showMessage(getCurrentPlayerName() + ": " +customString);}
    public void showTakeTurnMessageWithPlayerName(int txtFileLineArrayNum, int txtFileLineArrayNum2, int txtFileLineArrayNum3, String stringInText, String stringInText2, String stringInText3) {
       String str = takeTurnGUIMessages[txtFileLineArrayNum];
       if (txtFileLineArrayNum2 != -1)
           str += " " + takeTurnGUIMessages[txtFileLineArrayNum2];
        if (txtFileLineArrayNum3 != -1)
            str += " " + takeTurnGUIMessages[txtFileLineArrayNum3];
        gui.showMessage(getCurrentPlayerName() + ": " + String.format(str,stringInText, stringInText2, stringInText3));}
    public void showTakeTurnMessageWithPlayerName(String customString, int txtFileLineArrayNum, int txtFileLineArrayNum2, int txtFileLineArrayNum3, String stringInText, String stringInText2, String stringInText3) {
        String str = takeTurnGUIMessages[txtFileLineArrayNum];
        if (txtFileLineArrayNum2 != -1)
            str += " " + takeTurnGUIMessages[txtFileLineArrayNum2];
        if (txtFileLineArrayNum3 != -1)
            str += " " + takeTurnGUIMessages[txtFileLineArrayNum3];
        gui.showMessage(getCurrentPlayerName() + ": " + customString + " " + String.format(str,stringInText, stringInText2, stringInText3));}

    public boolean showMessageAndGetBooleanUserInput(int txtFileLineQuestion, int txtFileLineTrueButton,
        int txtFileLineFalseButton, String stringInText, String stringInText2, int stringInText3) {
        String str = (stringInText3 < 0) ? "" : takeTurnGUIMessages[stringInText3];
        return gui.getUserLeftButtonPressed(getCurrentPlayerName() + ": " +
        String.format(takeTurnGUIMessages[txtFileLineQuestion],stringInText,stringInText2, str),
        takeTurnGUIMessages[txtFileLineTrueButton], takeTurnGUIMessages[txtFileLineFalseButton]);
    }

    public String getBuyOrSellBuildingsUserInput (int dropDownLine1,int stringInText) {
        int lineArrayNum = 55;
        return gui.getUserSelection(getCurrentPlayerName() + ": " + takeTurnGUIMessages[lineArrayNum],takeTurnGUIMessages[16],
                String.format(takeTurnGUIMessages[dropDownLine1],takeTurnGUIMessages[stringInText]));
    }
    public String getBuyOrSellBuildingsUserInput (int dropDownLine1, int dropDownLine2, int stringInText, int stringInText2) {
        int lineArrayNum = 55;
        return gui.getUserSelection(getCurrentPlayerName() + ": " + takeTurnGUIMessages[lineArrayNum],takeTurnGUIMessages[16],
                String.format(takeTurnGUIMessages[dropDownLine1],takeTurnGUIMessages[stringInText]),String.format(takeTurnGUIMessages[dropDownLine2],takeTurnGUIMessages[stringInText2]));
    }
    public String getBuyOrSellBuildingsUserInput (int dropDownLine1, int dropDownLine2, int dropDownLine3, int stringInText, int stringInText2, int stringInText3) {
        int lineArrayNum = 55;
        return gui.getUserSelection(getCurrentPlayerName() + ": " + takeTurnGUIMessages[lineArrayNum],takeTurnGUIMessages[16],
                String.format(takeTurnGUIMessages[dropDownLine1],takeTurnGUIMessages[stringInText]),String.format(takeTurnGUIMessages[dropDownLine2],takeTurnGUIMessages[stringInText2]),String.format(takeTurnGUIMessages[dropDownLine3],takeTurnGUIMessages[stringInText3]));
    }
    public String getBuyOrSellBuildingsUserInput (int dropDownLine1, int dropDownLine2, int dropDownLine3, int dropDownLine4, int stringInText, int stringInText2, int stringInText3, int stringInText4) {
        int lineArrayNum = 55;
        return gui.getUserSelection(getCurrentPlayerName() + ": " + takeTurnGUIMessages[lineArrayNum],takeTurnGUIMessages[16],
                String.format(takeTurnGUIMessages[dropDownLine1],takeTurnGUIMessages[stringInText]),String.format(takeTurnGUIMessages[dropDownLine2],takeTurnGUIMessages[stringInText2]),String.format(takeTurnGUIMessages[dropDownLine3],takeTurnGUIMessages[stringInText3]),String.format(takeTurnGUIMessages[dropDownLine4],takeTurnGUIMessages[stringInText4]));
    }

    public String whereToBuildUserInput (String[] colors) {
        String[] options = new String[colors.length];
        for (int i = 0; i < options.length; i++)
            options[i] = String.format(takeTurnGUIMessages[59],colors[i]);
        return gui.getUserSelection(getCurrentPlayerName() + ": " + takeTurnGUIMessages[58],options);
    }

    public String whereToUnBuildUserInput (String[] colors) {
        String[] options = new String[colors.length];
        for (int i = 0; i < options.length; i++)
            options[i] = String.format(takeTurnGUIMessages[59],colors[i]);
        return gui.getUserSelection(getCurrentPlayerName() + ": " + takeTurnGUIMessages[71],options);
    }

    public int numberHousesToBuildUserInput (String fieldName, int housePrice) {
        String str = getCurrentPlayerName() + ": " + String.format(takeTurnGUIMessages[63],fieldName,housePrice,StreetField.MAXNUMOFHOUSES) + " " + takeTurnGUIMessages[61];
        return gui.getUserInteger(str,0, StreetField.MAXNUMOFHOUSES);
    }

    public boolean buildHotelUserInput (String fieldName, int housePrice) {
        String str = getCurrentPlayerName() + ": " + String.format(takeTurnGUIMessages[68],fieldName,housePrice,"");
        return gui.getUserLeftButtonPressed(str,takeTurnGUIMessages[15], takeTurnGUIMessages[16]);
    }

    public boolean sellHotelUserInput (String fieldName, int housePrice) {
        String str = getCurrentPlayerName() + ": " + String.format(takeTurnGUIMessages[72],fieldName,housePrice,"");
        return gui.getUserLeftButtonPressed(str,takeTurnGUIMessages[15], takeTurnGUIMessages[16]);
    }

    public void setGUINumHouses(int fieldArrayNum, int numHouses) {
        guiStreets[fieldArrayNum].setHouses(numHouses);
    }

    public void setGUIHasHotel(int fieldArrayNum, boolean hasHotel) {
        guiStreets[fieldArrayNum].setHouses(0);
        guiStreets[fieldArrayNum].setHotel(hasHotel);
    }

    public void drawChanceCardMessage(String text) {
        gui.setChanceCard(text);
        String name = getCurrentPlayerName();
        gui.getUserButtonPressed(name + ": "+ takeTurnGUIMessages[5],takeTurnGUIMessages[17]);
        gui.setChanceCard("");
    }

    public void formatFieldBorder(int fieldArrayNum) {
        boolean isShippingField = GameController.getInstance().getBoard().getFieldObject(fieldArrayNum).isShippingField();
        boolean isBreweryField = GameController.getInstance().getBoard().getFieldObject(fieldArrayNum).isBreweryField();
        int ownerNum = ((OwnableField) GameController.getInstance().getBoard().getFieldObject(fieldArrayNum)).getOwnerNum();
        if (ownerNum > 0) {
            if (isShippingField || isBreweryField) {
                guiFields[fieldArrayNum].setForeGroundColor(guiPlayers[ownerNum - 1].getPrimaryColor());
            } else
                guiStreets[fieldArrayNum].setBorder(guiPlayers[ownerNum - 1].getPrimaryColor());
        }
    }
}