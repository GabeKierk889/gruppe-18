package Controllers;

import Models.*;


public class GameController {

    private ViewController viewController;
    private Player player;
    private static Board board;
    private DiceCup diceCup;
    private static GameController single_instance;
    private String[] playerNames;
    private Player[] players;
    private int totalPlayers;
    private int currentPlayer;

    private GameController() {

    }

    public static GameController getInstance() {
        if (single_instance == null)
            single_instance = new GameController();
        return single_instance;
    }

    private void setupPlayers(String ... player_names){
        playerNames = player_names;
        totalPlayers = playerNames.length;
        players = new Player[totalPlayers];
        for (int i = 0; i < totalPlayers; i++) {
            players[i] = new Player(player_names[i]);
        }
    }

    public void switchTurn(boolean extraTurn) {
        if (!extraTurn) {
            if (currentPlayer < totalPlayers)
                currentPlayer++;
            else
                currentPlayer = 1; }
    }

    public void initializeGame(){
        board = new Board();
        viewController = new ViewController();
        viewController.setupGUIBoard();
        String playerNames = viewController.getPlayerNames();
        setupPlayers(playerNames);
        currentPlayer = 1;

        diceCup = new DiceCup();

        viewController.putPlayersOnBoard();
    }

    public static Board getBoard() { return board; }
    public DiceCup getDiceCup() {
        return diceCup;
    }
    public int getCurrentPlayer() {
        return currentPlayer;
    }
    public int getTotalPlayers() {
        return totalPlayers;
    }
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
