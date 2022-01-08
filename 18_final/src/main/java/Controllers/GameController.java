package Controllers;

import Models.*;


public class GameController {

    private ViewController viewController;
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

    // makes this class a singleton
    public static GameController getInstance() {
        if (single_instance == null)
            single_instance = new GameController();
        return single_instance;
    }

    // sets up player names and player array
    private void setupPlayers(){
        totalPlayers = playerNames.length;
        players = new Player[totalPlayers];
        for (int i = 0; i < totalPlayers; i++) {
            players[i] = new Player(playerNames[i]);
        }
    }

    // switches turn if there is no extra turn
    public void switchTurn(boolean extraTurn) {
        if (!extraTurn) {
            if (currentPlayerNum < totalPlayers)
                currentPlayerNum++;
            else
                currentPlayerNum = 1; }
        playerArrayNum = currentPlayerNum - 1;
    }

    // setups up the game board, players and dice
    public void initializeGame(){
        board = new Board();
        viewController = ViewController.getInstance();
        viewController.setupGUIBoard();
        playerNames = viewController.getPlayerNames();
        setupPlayers();
        currentPlayerNum = 1;
        playerArrayNum = 0;

        diceCup = new DiceCup();

//        viewController.putPlayersOnBoard();
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

    // finds a winner if all other than one player is bankrupt
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

    // the winner is the only player who is not bankrupt
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

    // a basic player turn
    private void takeTurn(){
//        diceCup.roll();
//        players[playerArrayNum].movePlayerSteps(diceCup.getSum()); // moves the player according to the throw
//        players[playerArrayNum].collectStartBonus(diceCup.getSum()); // the player collects START bonus if they pass START
//        viewController.updateGUIBalance();
//        board.getFieldObject(player.OnField()).landOnField(players[playerArrayNum]); // field effect happens
    }

    // releases the player from jail
    private void releaseFromOfJail(){
//        if(players[playerArrayNum].hasAReleaseFromJailCard()){
//            ChanceField.putBackChanceCard(players[playerArrayNum].returnReleaseFromJailCard()); // player returns returnReleaseFromJailCard
//        } else {
//            players[playerArrayNum].getAccount().withdrawMoney(GameSettings.JAILFEE); // player pays jail fee
//        }
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
    public Player getPlayerObject(int playerNum) { return players[playerNum - 1]; }
}
