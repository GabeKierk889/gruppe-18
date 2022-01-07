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
    private int currentPlayerNum;
    private int playerArrayNum = currentPlayerNum - 1;

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
            if (currentPlayerNum < totalPlayers)
                currentPlayerNum++;
            else
                currentPlayerNum = 1; }
        playerArrayNum = currentPlayerNum - 1;
    }

    public void initializeGame(){
        board = new Board();
        viewController = ViewController.getInstance();
        viewController.setupGUIBoard();
        String playerNames = viewController.getPlayerNames();
        setupPlayers(playerNames);
        currentPlayerNum = 1;
        playerArrayNum = 0;

        diceCup = new DiceCup();

        viewController.putPlayersOnBoard();
    }

    public void gameLoop(){
        boolean extraTurn = false;
        if(!players[playerArrayNum].getIsBankrupt() && !players[playerArrayNum].getIsInJail()){
            takeTurn();
        }
        if(players[playerArrayNum].getIsInJail()){
            releaseFromOfJail();
            takeTurn();
        }
        if(diceCup.sameFaceValue()) {
            extraTurn = true;
        }
        switchTurn(extraTurn);
        checkForWinner();
        determineWinner();

    }

    private boolean checkForWinner(){
        boolean winner = false;
        int bankruptCounter = 0;
        for(int i = 0; i < players.length; i++){
            if(players[i].getIsBankrupt()){
                bankruptCounter++;
            }
            if (bankruptCounter == players.length - 1) {
                winner = true;
            }
        }
        return winner;
    }

    private int determineWinner(){
        if(checkForWinner()) {
            for (int i = 0; i < players.length; i++) {
                if (!players[i].getIsBankrupt()) {
                    return i + 1;
                }
            }
        }
        return 0;
    }

    private void takeTurn(){
        diceCup.roll();
        players[playerArrayNum].movePlayerSteps(diceCup.getSum());
        players[playerArrayNum].collectStartBonus(diceCup.getSum());
        viewController.updateGUIBalance();
        board.getFieldObject(player.OnField()).landOnField(players[playerArrayNum]);
    }

    private void releaseFromOfJail(){
        if(players[playerArrayNum].hasAReleaseFromJailCard()){
            ChanceField.putBackChanceCard(players[playerArrayNum].returnReleaseFromJailCard());
        } else {
            players[playerArrayNum].getAccount().withdrawMoney(GameSettings.JAILFEE);
        }
    }

    public static Board getBoard() { return board; }
    public DiceCup getDiceCup() {
        return diceCup;
    }
    public int getCurrentPlayerNum() {
        return currentPlayerNum;
    }
    public int getTotalPlayers() {
        return totalPlayers;
    }
    public void setCurrentPlayerNum(int currentPlayerNum) {
        this.currentPlayerNum = currentPlayerNum;
    }
    public Player getPlayerObject(int playerNum) { return players[playerArrayNum]; }
}
