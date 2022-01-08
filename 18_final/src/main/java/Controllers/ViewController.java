package Controllers;

import Models.Board;
import Models.GameSettings;
import gui_fields.GUI_Car;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_fields.GUI_Street;
import gui_main.GUI;

import java.awt.Color;

public class ViewController {
    private GUI_Field[] guiFields;
    private GUI_Street[] guiStreets;
    private GUI_Car guiCars;
    private GUI_Player[] guiPlayers;
    private GUI gui;
    private static ViewController single_instance;
    int numbOfFields = GameController.getInstance().getBoard().getTotalNumOfFields();

    private ViewController() {

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

//    public void putPlayersOnBoard() {
//
//    }
}