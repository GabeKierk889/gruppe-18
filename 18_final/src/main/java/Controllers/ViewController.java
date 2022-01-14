package Controllers;

import Models.GameSettings;
import Models.FieldSubType.OwnableField;
import gui_fields.GUI_Car;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_fields.GUI_Street;
import gui_main.GUI;

public class ViewController {
    private GUI_Player[] guiPlayers;
    private GUI gui;
    private static ViewController single_instance;
    private final int totalNumOfFields = GameController.getInstance().getBoard().getTotalNumOfFields();
    private GUI_Field[] guiFields = new GUI_Field[totalNumOfFields];
    private GUI_Street[] guiStreets = new GUI_Street[totalNumOfFields];

    public static ViewController getInstance() {
        if (single_instance == null)
            single_instance = new ViewController();
        return single_instance;
    }

    public void setupGUIBoard() {
        gui = ViewController_GameSetup.getInstance().setupGUIBoard(guiFields, guiStreets);
    }

    public String[] getPlayerNames() {
        return ViewController_GameSetup.getInstance().getPlayerNames(gui);
    }

    public void putPlayersOnBoard() {
        // sets up the gui players and the cars, puts the cars on START
        int totalPlayers = GameController.getInstance().getTotalPlayers();
        GUI_Car[] guiCars = new GUI_Car[totalPlayers];
        ViewController_GameSetup.getInstance().setupGUICars(totalPlayers, guiCars);
        guiPlayers = new GUI_Player[totalPlayers];
        for (int i = totalPlayers - 1; i >= 0; i--) {
            guiPlayers[i] = new GUI_Player(getPlayerName(i + 1), GameSettings.STARTINGBALANCE, guiCars[i]);
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
        guiFields[playerOnFieldNum].setCar(guiPlayers[playerNum - 1], false);
    }

    public void updateGUIDice(int die1, int die2) {
        gui.setDice(die1, (int) (Math.random() * 359), (int) (Math.random() * 3 + 1),
                (int) (Math.random() * 6 + 3), die2,
                (int) (Math.random() * 359),
                (int) (Math.random() * 3 + 4),
                (int) (Math.random() * 3 + 7));
    }

    private String getPlayerName(int playerNum) {
        return GameController.getInstance().getPlayerObject(playerNum).getName();
    }

    public void updateGUIBalance() {
        // updating and showing the updated gui balances for all players
        for (int i = 0; i < guiPlayers.length; i++)
            guiPlayers[i].setBalance(GameController.getInstance().getPlayerObject(i + 1).getAccount().getBalance());
    }

    public void formatFieldBorder(int fieldArrayNum) {
        // when a field is purchased by a player, format the field according to the player's color
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

    public void setGUINumHouses(int fieldArrayNum, int numHouses) {
        guiStreets[fieldArrayNum].setHouses(numHouses);
    }

    public void setGUIHasHotel(int fieldArrayNum, boolean hasHotel) {
        guiStreets[fieldArrayNum].setHouses(0);
        guiStreets[fieldArrayNum].setHotel(hasHotel);
    }

    public GUI getGui() {
        return gui;
    }
}
