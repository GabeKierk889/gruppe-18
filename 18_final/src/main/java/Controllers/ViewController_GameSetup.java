package Controllers;

import Models.GameSettings;
import Services.FileImporter;
import Services.GUIBoardCreator;
import gui_fields.GUI_Car;
import gui_fields.GUI_Field;
import gui_fields.GUI_Street;
import gui_main.GUI;
import java.awt.*;

public class ViewController_GameSetup {
    private String[] setupGameGUIMessages;
    private static ViewController_GameSetup single_instance;

    private ViewController_GameSetup() {
        FileImporter reader = new FileImporter();
        setupGameGUIMessages = reader.readAllLinesInFile("GameMessages_setupGame.txt");
    }

    public static ViewController_GameSetup getInstance() {
        if (single_instance == null)
            single_instance = new ViewController_GameSetup();
        return single_instance;
    }

    public GUI setupGUIBoard(GUI_Field[] guiFields, GUI_Street[] guiStreets) {
        // sets up and formats the GUI board, shows a welcome message with the game rules
        GUIBoardCreator service = new GUIBoardCreator(guiFields,guiStreets);
        service.setUpAndFormatGUIBoard();
        GUI gui = new GUI(guiFields, Color.WHITE);
        showGameRules(gui);
        return gui;
    }

    // asks the players to enter the player names, checks for errors, then returns the player names
    public String[] getPlayerNames(GUI gui) {
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
        return getPlayerNames(gui);
    }

    // sets up the number of gui cars needed - note there is intentional run-through in switch statement
    public void setupGUICars(int players, GUI_Car[] guiCars) {
        switch (players) {
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

    public void showGameRules(GUI gui) {
        if(gui.getUserLeftButtonPressed(setupGameGUIMessages[0] +" "+setupGameGUIMessages[9],setupGameGUIMessages[10],setupGameGUIMessages[11]))
            gui.showMessage(setupGameGUIMessages[12]);}
}
