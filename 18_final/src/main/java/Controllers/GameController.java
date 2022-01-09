package Controllers;

import Models.*;


public class GameController {

    private ViewController viewController;
    private Board board;
    private DiceCup diceCup;
    private static GameController single_instance;
    private String[] playerNames;
    private Player[] players;
    private int totalPlayers;
    private int currentPlayerNum;
    private int playerArrayNum;

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
                currentPlayerNum = 1;
            playerArrayNum = currentPlayerNum - 1;
            if (players[playerArrayNum].getIsBankrupt()) // skips any bankrupt players
                switchTurn(false);
        }
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

        viewController.putPlayersOnBoard();
    }

    public void gameLoop(){
        boolean extraTurn = false;
        boolean playerIsInJail = players[playerArrayNum].getIsInJail();

        if(!playerIsInJail){
            takeTurn();
        }
        if(playerIsInJail){
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
        for (Player player : players) {
            if (player.getIsBankrupt()) {
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
        viewController.rollMessage();
        diceCup.roll();
        players[playerArrayNum].movePlayerSteps(diceCup.getSum()); // moves the player according to the throw
        players[playerArrayNum].collectStartBonus(diceCup.getSum()); // the player collects START bonus if they pass START
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

    private int calculateAssets(int playerNum) {
        int totalAssetValue, valueOfBuildings, valueOfOwnableFields;
        valueOfBuildings = board.calculateAssetValueOfBuildingsOwned(playerNum);
        valueOfOwnableFields = board.calculateValueOfFieldsOwned(playerNum);
        totalAssetValue = players[playerNum-1].getAccount().getBalance() + valueOfBuildings + valueOfOwnableFields;
        return totalAssetValue;
    }

    public void sellAssets(String name, int needToPay, int creditorPlayerNum) {
        // note int creditorPlayerNum is only needed to pass in the value as a parameter to goBankrupt()
        int playerNum = getPlayerNum(name);
        int totalAssetValue = calculateAssets(playerNum);

        //checks if the player is not able to pay / is going bankrupt
        if(totalAssetValue < needToPay)
            goBankrupt(playerNum, totalAssetValue,needToPay,creditorPlayerNum);

        // if the player is not going bankrupt, add gui messages asking player to sell/mortgage assets
    }

    private void goBankrupt(int bankruptPlayerNum, int totalAssetValue,int needToPay, int creditorPlayerNum) {
        // sell all buildings owned by bankrupt player and adds the money to bankrupt player's account
        int resellValueBuildings = board.calculateAssetValueOfBuildingsOwned(bankruptPlayerNum);
        players[bankruptPlayerNum-1].getAccount().depositMoney(resellValueBuildings);
        board.removeAllBuildingsOwned(bankruptPlayerNum);
        // call to a gui method that removes all gui buildings owned by player

        // calculate the remaining money debt of the bankrupt player
        int remainingMoneyDebt = players[bankruptPlayerNum-1].getAccount().getBalance();
        // if creditor is another player, deposit an amount = needToPay + remainingMoneyDebt(<0) to creditor
        // because the transferMoney method in account has only withdrawn the amount, not made any deposits
        players[creditorPlayerNum-1].getAccount().depositMoney(needToPay+remainingMoneyDebt);

        // transfer ownable fields to creditor (creditorPlayerNum = 0 for the bank)
        // if creditor is the bank, the called method ensures an auction is held
        board.bankruptcyTransferAllFieldAssets(bankruptPlayerNum,creditorPlayerNum);

        // write message to gui that player is going bankrupt because they cannot pay needToPay
        // because they only have a total asset value of int calculateAssets
        // update gui fields info (ownership, rent)
        // update gui account
    }

    private int getPlayerNum (String playerName) {
        for (int i = 0; i < totalPlayers; i++) {
            if (players[i].getName().equals(playerName))
                return i+1;
        }
        return 0; // returns 0 if player name is not found
    }

    public Board getBoard() { return board; }

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

    public void testMethod() {
        while (true) {
            viewController.rollMessage();
            diceCup.roll();
            viewController.updateGUIDice(diceCup.getDie1Value(), diceCup.getDie2Value());
            int moveFrom, moveTo;
            moveFrom = players[playerArrayNum].OnField();
            // next line moves the player i.e. changes the onField varialbe
            players[playerArrayNum].movePlayerSteps(diceCup.getSum());
            moveTo = players[playerArrayNum].OnField();
            viewController.moveGUICar(moveFrom,moveTo,currentPlayerNum);
            switchTurn(false);
        }
    }
}
