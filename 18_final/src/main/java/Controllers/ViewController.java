package Controllers;

import Models.Board;
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

        gui = new GUI(guiFields, new Color(230,230,230));
    }

//    public String getPlayerNames() {
//
//    }

    public void putPlayersOnBoard() {

    }
}
