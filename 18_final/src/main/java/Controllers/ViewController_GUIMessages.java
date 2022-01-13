package Controllers;

import Models.GameSettings;
import Models.StreetField;
import Services.*;
import gui_fields.*;
import gui_main.GUI;

public class ViewController_GUIMessages {

    private GUI gui;
    private static ViewController_GUIMessages single_instance;
    private final int totalNumOfFields = GameController.getInstance().getBoard().getTotalNumOfFields();
    private GUI_Field[] guiFields = new GUI_Field[totalNumOfFields];
    private GUI_Street[] guiStreets= new GUI_Street[totalNumOfFields];
    private final String[] takeTurnGUIMessages;

    private ViewController_GUIMessages() {
        gui = ViewController.getInstance().getGui();
        FileImporter reader = new FileImporter();
        takeTurnGUIMessages = reader.readAllLinesInFile("GameMessages_takeTurn.txt");
    }

    public static ViewController_GUIMessages getInstance() {
        if (single_instance == null)
            single_instance = new ViewController_GUIMessages();
        return single_instance;
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

    public void sameFaceValueMessage() {
        String name = getCurrentPlayerName();
        gui.getUserButtonPressed(name+": "+ takeTurnGUIMessages[3]+" "+takeTurnGUIMessages[0], takeTurnGUIMessages[1]);
    }

    public void startBonusMessage(){
        int bonusStart = GameSettings.STARTBONUS;
        String name = getCurrentPlayerName();
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

    public int auctionBidding(String fieldName, int fieldPrice, String playerName) {
        return gui.getUserInteger( String.format(takeTurnGUIMessages[82],playerName,fieldName,""+fieldPrice),0,Integer.MAX_VALUE);
    }

    public void drawChanceCardMessage(String text) {
        gui.setChanceCard(text);
        String name = getCurrentPlayerName();
        gui.getUserButtonPressed(name + ": "+ takeTurnGUIMessages[5],takeTurnGUIMessages[17]);
        gui.setChanceCard("");
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

    public int numberHousesToSellUserInput (String fieldName, int housePrice, int numHousesOnField) {
        String str = getCurrentPlayerName() + ": " + String.format(takeTurnGUIMessages[77],fieldName,housePrice,StreetField.MAXNUMOFHOUSES);
        return gui.getUserInteger(str,0, numHousesOnField);
    }

    public boolean buildHotelUserInput (String fieldName, int housePrice) {
        String str = getCurrentPlayerName() + ": " + String.format(takeTurnGUIMessages[68],fieldName,housePrice,"");
        return gui.getUserLeftButtonPressed(str,takeTurnGUIMessages[15], takeTurnGUIMessages[16]);
    }

    public boolean sellHotelUserInput (String fieldName, int housePrice) {
        String str = getCurrentPlayerName() + ": " + String.format(takeTurnGUIMessages[72],fieldName,housePrice,"");
        return gui.getUserLeftButtonPressed(str,takeTurnGUIMessages[15], takeTurnGUIMessages[16]);
    }

    // method overloading - methods that display a drop down menu to take user input
    // method signatures differ in taking in a different number of drop-down menu options
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
    public String getSellBuildingsUserInput (int dropDownLine1,int stringInText) {
        int lineArrayNum = 53;
        return gui.getUserSelection(getCurrentPlayerName() + ": " + takeTurnGUIMessages[lineArrayNum],
                String.format(takeTurnGUIMessages[dropDownLine1],takeTurnGUIMessages[stringInText]));
    }
    public String getSellBuildingsUserInput (int dropDownLine1, int dropDownLine2, int stringInText, int stringInText2) {
        int lineArrayNum = 53;
        return gui.getUserSelection(getCurrentPlayerName() + ": " + takeTurnGUIMessages[lineArrayNum],
                String.format(takeTurnGUIMessages[dropDownLine1],takeTurnGUIMessages[stringInText]),String.format(takeTurnGUIMessages[dropDownLine2],takeTurnGUIMessages[stringInText2]));
    }

    public boolean showMessageAndGetBooleanUserInput(int txtFileLineQuestion, int txtFileLineTrueButton,
                                                     int txtFileLineFalseButton, String stringInText, String stringInText2, int stringInText3) {
        String str = (stringInText3 < 0) ? "" : takeTurnGUIMessages[stringInText3];
        return gui.getUserLeftButtonPressed(getCurrentPlayerName() + ": " +
                        String.format(takeTurnGUIMessages[txtFileLineQuestion],stringInText,stringInText2, str),
                takeTurnGUIMessages[txtFileLineTrueButton], takeTurnGUIMessages[txtFileLineFalseButton]);
    }

    // method overloading - various methods that display a message to the user in the gui, using
    // parameters that indicate which line(s) in the external file need to be displayed
    public String getTakeTurnGUIMessages(int txtLineArray) { return takeTurnGUIMessages[txtLineArray]; }
    public String getTakeTurnGUIMessages(int txtLineArray, String stringInText, String stringInText2, String stringInText3) {
        return String.format(takeTurnGUIMessages[txtLineArray],stringInText,stringInText2,stringInText3);}
    public String getTakeTurnGUIMessages(int txtLineArray, int stringInTextLine) {
        return String.format(takeTurnGUIMessages[txtLineArray],takeTurnGUIMessages[stringInTextLine]);}
    public void showTakeTurnMessage(int txtFileLineArrayNum, String stringInText, String stringInText2, String stringInText3) {
        gui.showMessage(String.format(takeTurnGUIMessages[txtFileLineArrayNum],stringInText, stringInText2, stringInText3));}
    public void showTakeTurnMessageWithPlayerName(int txtFileLineArrayNum, String stringInText, String stringInText2, String stringInText3) {
        gui.showMessage(getCurrentPlayerName() + ": " + String.format(takeTurnGUIMessages[txtFileLineArrayNum],stringInText, stringInText2, stringInText3));}
    public void showTakeTurnMessageWithPlayerName(String playerName, String customString, int txtFileLineArrayNum) {
        gui.showMessage(playerName + ": " +customString + " " + takeTurnGUIMessages[txtFileLineArrayNum]);}
    public void showTakeTurnMessageWithPlayerName(String playerName, String customString) {
        gui.showMessage(playerName + ": " +customString);}
    public void showTakeTurnMessageWithPlayerName(int txtFileLineArrayNum, int txtFileLineArrayNum2, int txtFileLineArrayNum3, String stringInText, String stringInText2, String stringInText3) {
        String str = takeTurnGUIMessages[txtFileLineArrayNum];
        if (txtFileLineArrayNum2 != -1)
            str += " " + takeTurnGUIMessages[txtFileLineArrayNum2];
        if (txtFileLineArrayNum3 != -1)
            str += " " + takeTurnGUIMessages[txtFileLineArrayNum3];
        gui.showMessage(getCurrentPlayerName() + ": " + String.format(str,stringInText, stringInText2, stringInText3));}
    public void showTakeTurnMessageWithPlayerName(String playerName, String customString, int txtFileLineArrayNum, int txtFileLineArrayNum2, int txtFileLineArrayNum3, String stringInText, String stringInText2, String stringInText3) {
        String str = takeTurnGUIMessages[txtFileLineArrayNum];
        if (txtFileLineArrayNum2 != -1)
            str += " " + takeTurnGUIMessages[txtFileLineArrayNum2];
        if (txtFileLineArrayNum3 != -1)
            str += " " + takeTurnGUIMessages[txtFileLineArrayNum3];
        gui.showMessage(getCurrentPlayerName() + ": " + customString + " " + String.format(str,stringInText, stringInText2, stringInText3));}
}