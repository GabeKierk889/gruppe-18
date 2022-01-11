package Controllers;

import Models.*;


public class GameController {
    // TODO: update class diagram with some of the new methods

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
    private void setupPlayers() {
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
    public void initializeGame() {
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

    public void gameLoop() {
        while (!checkForWinner()) {
            boolean extraTurn = false;
            boolean playerIsInJail = players[playerArrayNum].getIsInJail();

            if (!playerIsInJail) {
                takeTurn();
            }
            if (playerIsInJail) {
                releaseFromJail();
                takeTurn();
            }
            if (diceCup.sameFaceValue() && !players[playerArrayNum].getIsBankrupt()) {
                extraTurn = true;
            }
            switchTurn(extraTurn);
        }
        determineWinner();
    }

    // finds a winner if all other than one player is bankrupt
    private boolean checkForWinner() {
        // TODO: add gui output about the winner. end game method to ask if they want to play again, reset game etc
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
    private int determineWinner() {
        if (checkForWinner()) {
            for (int i = 0; i < players.length; i++) {
                if (!players[i].getIsBankrupt()) {
                    return i + 1;
                }
            }
        }
        return 0;
    }

    // a basic player turn
    private void takeTurn() {
        int moveFrom, moveTo;
        moveFrom = players[playerArrayNum].OnField(); // save player's last location
        // roll dice
        if (diceCup.sameFaceValue()) // message in gui that player has got an extra throw + roll message
            viewController.sameFaceValueMessage();
        else
            viewController.rollMessage(); // the "normal" roll message
        diceCup.roll();
        viewController.updateGUIDice(diceCup.getDie1Value(), diceCup.getDie2Value());
        if (diceCup.sameFaceValue())
            players[playerArrayNum].increaseThrowTwoOfSameCounter();
        else players[playerArrayNum].resetThrowTwoOfSameCounter();
        if (players[playerArrayNum].getThrowTwoOfSameDiceInARow() == GameSettings.GOTOJAIL_IF_THROW_SAME_DICE_X_TIMES) {
            // if same dice 3 times in a row, player must go to jail immediately and end their turn
            moveTo = 10; // jail
            players[playerArrayNum].moveToField(moveTo);
            viewController.moveGUICar(moveFrom, moveTo, currentPlayerNum);
            players[playerArrayNum].setIsInJail(true);
            viewController.showTakeTurnMessageWithPlayerName(41, "" + GameSettings.GOTOJAIL_IF_THROW_SAME_DICE_X_TIMES, "", "");
        }
        if (!players[playerArrayNum].getIsInJail()) { // only run below if player has not been put in jail
            // move player
            players[playerArrayNum].moveSteps(diceCup.getSum());
            moveTo = players[playerArrayNum].OnField();
            viewController.moveGUICar(moveFrom, moveTo, currentPlayerNum);
            players[playerArrayNum].collectStartBonus(diceCup.getSum()); // the player collects START bonus if they pass START
            moveFrom = moveTo; // saves player's new location after moving
            board.getFieldObject(players[playerArrayNum].OnField()).landOnField(players[playerArrayNum]); // field effect happens
            // following flow ensures landOnField method is called again in case player has landed on another field during their turn
            while (moveFrom != players[playerArrayNum].OnField() && !players[playerArrayNum].getIsBankrupt()) {
                moveFrom = players[playerArrayNum].OnField();
                board.getFieldObject(moveFrom).landOnField(players[playerArrayNum]);
            }
            // asks a player if they want to buy or sell buildings
            board.currentPlayerBuildingDecision();
        }
    }

    // releases the player from jail
    private void releaseFromJail() {
        if (players[playerArrayNum].hasAReleaseFromJailCard()) {
            boolean playerUseReleaseFromJailCard = viewController.releaseFromJailMessageHasCard();
            viewController.releaseFromJailMessageHasCard();
            if (playerUseReleaseFromJailCard) {
                ChanceField.putBackChanceCard(players[playerArrayNum].returnReleaseFromJailCard()); // player returns returnReleaseFromJailCard
                players[playerArrayNum].setIsInJail(false);
            }
        }
        if (players[playerArrayNum].getIsInJail()) { // using return; call did not work
            viewController.releaseFromJailMessagePayMoney();
            players[playerArrayNum].setIsInJail(false);
            players[playerArrayNum].getAccount().withdrawMoney(GameSettings.JAILFEE); // player pays jail fee
            viewController.updateGUIBalance();
        }
    }

    public int calculateAssets(int playerNum) {
        int totalAssetValue, valueOfBuildings, valueOfOwnableFields;
        valueOfBuildings = board.calculateAssetValueOfBuildingsOwned(playerNum);
        valueOfOwnableFields = board.calculateValueOfFieldsOwned(playerNum);
        totalAssetValue = players[playerNum - 1].getAccount().getBalance() + valueOfBuildings + valueOfOwnableFields;
        return totalAssetValue;
    }

    private int calculateLiquidAssets(int playerNum) {
        int totalLiquidAssetValue, valueOfBuildings, mortgageValueOfFields;
        valueOfBuildings = board.calculateAssetValueOfBuildingsOwned(playerNum);
        mortgageValueOfFields = board.calculateAvailableMortgageValueOfFieldsOwned(playerNum);
        totalLiquidAssetValue = players[playerNum - 1].getAccount().getBalance() + valueOfBuildings + mortgageValueOfFields;
        return totalLiquidAssetValue;
    }

    public void sellAssets(String name, int needToPay, int creditorPlayerNum) {
        // note int creditorPlayerNum is only needed to pass in the value as a parameter to goBankrupt()
        int playerNum = getPlayerNum(name);
        // since fields cannot be traded in this game, they can only be put on mortgage to raise money
        // liquid assets = moneyBalanceBeforeWithdrawal + resellValueBuildings + mortgageValueFields
        int liquidAssetValue = calculateLiquidAssets(playerNum) + needToPay;
        // needToPay needs to be added since we need to know how much money player had
        // before the withdrawal that made balance < 0 was made

        // if the player is not able to pay with their liquid assets, they go bankrupt
        if (liquidAssetValue < needToPay)
            goBankrupt(playerNum, liquidAssetValue, needToPay, creditorPlayerNum);

        // TODO: gui
        // if the player is not going bankrupt, add gui messages asking player to sell/mortgage assets
    }

    private void goBankrupt(int bankruptPlayerNum, int assetvalue, int needToPay, int creditorPlayerNum) {
        // sell all buildings owned by bankrupt player and adds the money to bankrupt player's account
        int resellValueBuildings = board.calculateAssetValueOfBuildingsOwned(bankruptPlayerNum);
        players[bankruptPlayerNum - 1].getAccount().depositMoney(resellValueBuildings);
        board.removeAllBuildingsOwned(bankruptPlayerNum);

        // calculate the remaining money debt of the bankrupt player
        int remainingMoneyDebt = players[bankruptPlayerNum - 1].getAccount().getBalance();
        // if creditor is another player, deposit an amount = needToPay + remainingMoneyDebt(<0) to creditor
        // because the transferMoney method in account has only withdrawn the amount, not made any deposits
        if (creditorPlayerNum > 0)
            players[creditorPlayerNum - 1].getAccount().depositMoney(needToPay + remainingMoneyDebt);

        // transfer ownable fields to creditor (creditorPlayerNum = 0 for the bank)
        // if creditor is the bank, the called method ensures an auction is held
        board.bankruptcyTransferAllFieldAssets(bankruptPlayerNum, creditorPlayerNum);

        players[bankruptPlayerNum - 1].setIsBankrupt(true); // sets player's bankrupt status to true
        // returns a player's (up to 2) get out of jail chance cards to the deck of chance cards
        ChanceField.putBackChanceCard(players[bankruptPlayerNum - 1].returnReleaseFromJailCard());
        ChanceField.putBackChanceCard(players[bankruptPlayerNum - 1].returnReleaseFromJailCard()); // 2nd card

        // write message to gui that player is going bankrupt because they cannot pay and will be removed from the game
        String line1 = viewController.getTakeTurnGUIMessages(42, players[bankruptPlayerNum - 1].getName(), "" + needToPay, "" + assetvalue);
        String creditorName;
        if (creditorPlayerNum > 0)
            creditorName = players[creditorPlayerNum - 1].getName();
        else // bank is creditor
            creditorName = viewController.getTakeTurnGUIMessages(44);
        viewController.showTakeTurnMessageWithPlayerName(line1, 43, -1, -1, players[bankruptPlayerNum - 1].getName(), creditorName, "");
        viewController.updateGUIBalance();
        viewController.removeGUICar(currentPlayerNum, players[bankruptPlayerNum - 1].OnField());
        if (creditorPlayerNum > 0)
            viewController.showTakeTurnMessageWithPlayerName(45, creditorName, "" + remainingMoneyDebt, players[bankruptPlayerNum - 1].getName());
    }

    private int getPlayerNum(String playerName) {
        for (int i = 0; i < totalPlayers; i++) {
            if (players[i].getName().equals(playerName))
                return i + 1;
        }
        return 0; // returns 0 if player name is not found
    }

    public Board getBoard() {
        return board;
    }

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

    public Player getPlayerObject(int playerNum) {
        return players[playerNum - 1];
    }

    public String getPlayerName(int playerNum) {
        return players[playerNum - 1].getName();
    }


    public void testMethod() {
    }
}
