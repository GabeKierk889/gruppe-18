package Controllers;

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

    public void setupGUIBoard() {
        guiFields = new GUI_Field[40];
        guiStreets = new GUI_Street[40];
        gui = new GUI(guiFields, new Color(230,230,230));

    }

    public void getPlayerNames() {

    }

    public void putPlayersOnBoard() {

    }
}

class Main {
    public static void main(String[] args) {
        ViewController viewController = new ViewController();

        viewController.setupGUIBoard();
    }
}
