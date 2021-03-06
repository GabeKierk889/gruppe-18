package Controllers;

import Models.*;
import Models.FieldSubType.ChanceField;
import Services.BuildSellBuildingsHandler;

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
            if (players[playerArrayNum].getIsBankrupt())
                switchTurn(false); // skips the turn of any bankrupt players
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
        // gameloop continues as long as there is no winner
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
                extraTurn = true; // gives the player an extra turn
            }
            switchTurn(extraTurn);
        }
        // a winner has now been found
        endGame();
    }

    // a basic player turn
    private void takeTurn() {
        int moveFrom, moveTo;
        moveFrom = players[playerArrayNum].OnField(); // save player's last location

        // roll dice incl gui messages
        if (diceCup.sameFaceValue()) // message in gui that player has got an extra throw and to roll dice
            ViewController_GUIMessages.getInstance().sameFaceValueRollMessage();
        else
            ViewController_GUIMessages.getInstance().rollDiceMessage(); // the "normal" roll dice message
        diceCup.roll();
        viewController.updateGUIDice(diceCup.getDie1Value(), diceCup.getDie2Value());

        // if player has thrown same dice 3 times in a row, the player goes to jail
        if (putPlayerInJailIfSameDice3TimesInRow(moveFrom)) { // scrambles the dice so that on the next turn,
            diceCup.setDiceNotSameFaceValue();  // the next player does not get an extra turn message
        }

        // below will only run below if player has not been put in jail by throwing 2 of the same dice 3 times in a row
        else {
            // move player
            players[playerArrayNum].moveSteps(diceCup.getSum());
            moveTo = players[playerArrayNum].OnField();
            viewController.moveGUICar(moveFrom, moveTo, currentPlayerNum);

            // the below method lets the player collects START bonus if they pass START
            players[playerArrayNum].collectStartBonus(diceCup.getSum());
            moveFrom = moveTo; // saves player's new location after moving

            // landOnField method is called
            board.getFieldObject(players[playerArrayNum].OnField()).landOnField(players[playerArrayNum]); // field effect happens

            // following flow ensures landOnField method is called again in case player has landed on another field during their turn
            while (moveFrom != players[playerArrayNum].OnField() && !players[playerArrayNum].getIsBankrupt()) {
                moveFrom = players[playerArrayNum].OnField();
                board.getFieldObject(moveFrom).landOnField(players[playerArrayNum]);
            }

            // asks a player if they want to buy or sell buildings at the end of their turn
            if (!players[playerArrayNum].getIsBankrupt() && !diceCup.sameFaceValue())
                board.buildAndSellBuildings();
        }
    }

    private boolean putPlayerInJailIfSameDice3TimesInRow(int playerOnField) {
        if (diceCup.sameFaceValue())
            players[playerArrayNum].increaseThrowTwoOfSameCounter();
        // keeps track of how many times in a row player has thrown two of the same

        else players[playerArrayNum].resetThrowTwoOfSameCounter(); // reset counter

        // if same dice 3 times in a row, player must go to jail immediately and end their turn
        if (players[playerArrayNum].getThrowTwoOfSameDiceInARow() == GameSettings.GOTOJAIL_IF_THROW_SAME_DICE_X_TIMES) {
            // show a message in the GUI and move the player to jail
            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(41, "" + GameSettings.GOTOJAIL_IF_THROW_SAME_DICE_X_TIMES, "", "");
            int moveTo = 10; // jail field
            players[playerArrayNum].moveToField(moveTo);
            viewController.moveGUICar(playerOnField, moveTo, currentPlayerNum);
            players[playerArrayNum].setIsInJail(true);
            players[playerArrayNum].resetThrowTwoOfSameCounter(); // reset the counter
            return true;
        } else return false;
    }

    // releases the player from jail
    private void releaseFromJail() {
        // asks player if they want to use a chance card to be released (if they have one)
        if (players[playerArrayNum].hasAReleaseFromJailCard()) {
            boolean playerUseReleaseFromJailCard = ViewController_GUIMessages.getInstance().releaseFromJailMessageHasCard();
            if (playerUseReleaseFromJailCard) {
                // returns the chance card to the deck of cards, releases player
                ChanceField.putBackChanceCard(players[playerArrayNum].returnReleaseFromJailCard()); // player returns returnReleaseFromJailCard
                players[playerArrayNum].setIsInJail(false);
            }
        } // if player does not have a card or did not use it, asks them to pay money to be released
        if (players[playerArrayNum].getIsInJail()) {
            ViewController_GUIMessages.getInstance().releaseFromJailMessagePayMoney();
            players[playerArrayNum].setIsInJail(false);
            players[playerArrayNum].getAccount().withdrawMoney(GameSettings.JAILFEE); // player pays jail fee
            viewController.updateGUIBalance();
        }
    }

    // calculates the total value of a player's assets
    public int calculateAssets(int playerNum) {
        int totalAssetValue, valueOfBuildings, valueOfOwnableFields;
        valueOfBuildings = board.calculateAssetValueOfBuildingsOwned(playerNum);
        valueOfOwnableFields = board.calculateValueOfFieldsOwned(playerNum);
        totalAssetValue = players[playerNum - 1].getAccount().getBalance() + valueOfBuildings + valueOfOwnableFields;
        return totalAssetValue;
    }

    // calculates the value of player's liquid assets i.e. money and buildings resell value
    private int calculateLiquidAssets(int playerNum) {
        int totalLiquidAssetValue, valueOfBuildings, mortgageValueOfFields;
        valueOfBuildings = board.calculateAssetValueOfBuildingsOwned(playerNum);
        mortgageValueOfFields = 0; // mortgage feature has not been implemented in this version
//        mortgageValueOfFields = board.calculateAvailableMortgageValueOfFieldsOwned(playerNum);
        totalLiquidAssetValue = players[playerNum - 1].getAccount().getBalance() + valueOfBuildings + mortgageValueOfFields;
        return totalLiquidAssetValue;
    }

    public void sellAssets(String name, int needToPay, int creditorPlayerNum) {
        int playerNum = getPlayerNum(name);
        // needToPay needs to be added since we need to know how much money player had
        // before the withdrawal that made balance < 0 was made
        int assetsValuePriorToDebt = calculateLiquidAssets(playerNum) + needToPay;

        // if the player is not able to pay their debt, they go bankrupt
        if (assetsValuePriorToDebt < needToPay)
            goBankrupt(playerNum, assetsValuePriorToDebt, needToPay, creditorPlayerNum);
        else {
            // if the player is not going bankrupt, add gui messages asking player to sell some assets
            viewController.updateGUIBalance();
            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(52, "", "", "");
            BuildSellBuildingsHandler helper = new BuildSellBuildingsHandler(getBoard().getFields());
            helper.playerMustSellBuildings(playerNum, players[playerNum - 1]);
        }
    }

    private void goBankrupt(int bankruptPlayerNum, int assetValue, int needToPay, int creditorPlayerNum) {
        // calculate value of buildings and the money balance (before the bankrupting withdrawal was made)
        int moneyToPayCreditor;
        int resellValueBuildings = board.calculateAssetValueOfBuildingsOwned(bankruptPlayerNum);
        // calculate how much money the bankrupt player had before the bankrupting withdrawal was made
        int moneyBeforeWithdrawal = players[bankruptPlayerNum - 1].getAccount().getBalance() + needToPay;
        moneyToPayCreditor = resellValueBuildings + moneyBeforeWithdrawal;

        // write message to gui that player is going bankrupt because they cannot pay and will be removed from the game
        String line1 = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(42, "" + needToPay, "" + assetValue, "");
        String creditorName;
        // gets the relevant String for the creditor name, shows a message in the GUI
        if (creditorPlayerNum > 0) {
            creditorName = players[creditorPlayerNum - 1].getName();
            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(players[bankruptPlayerNum - 1].getName(), line1, 43, -1, -1, creditorName, "", "");
        } else {// bank is creditor
            creditorName = ViewController_GUIMessages.getInstance().getTakeTurnGUIMessages(44);
            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(players[bankruptPlayerNum - 1].getName(), line1, 43, 85, -1, creditorName, "", "");
        }

        // removes a player's buildings and the car from the board
        board.removeAllBuildingsOwned(bankruptPlayerNum);
        viewController.removeGUICar(currentPlayerNum, players[bankruptPlayerNum - 1].OnField());
        players[bankruptPlayerNum - 1].setIsBankrupt(true); // sets player's bankrupt status to true
        players[bankruptPlayerNum - 1].getAccount().setBalance(0); // set balance 0 for bankrupt players

        // transfer ownable fields to creditor (creditorPlayerNum = 0 for the bank)
        // if creditor is the bank, the called method ensures an auction is held, unless it's the end of the game
        if (!(creditorPlayerNum == 0 && checkForWinner()))
            board.bankruptcyTransferAllFieldAssets(bankruptPlayerNum, creditorPlayerNum);

        // if creditor is another player, deposit money to the creditor
        if (creditorPlayerNum > 0) {
            players[creditorPlayerNum - 1].getAccount().depositMoney(moneyToPayCreditor);
            viewController.updateGUIBalance();
            ViewController_GUIMessages.getInstance().showTakeTurnMessageWithPlayerName(45, creditorName, "" + moneyToPayCreditor, players[bankruptPlayerNum - 1].getName());
        } else
            viewController.updateGUIBalance();

        // returns a player's (up to 2) get out of jail chance cards to the deck of chance cards
        ChanceField.putBackChanceCard(players[bankruptPlayerNum - 1].returnReleaseFromJailCard());
        ChanceField.putBackChanceCard(players[bankruptPlayerNum - 1].returnReleaseFromJailCard()); // 2nd card
    }

    // finds a winner if all other than one player is bankrupt
    public boolean checkForWinner() {
        boolean gameHasAWinner = false;
        int bankruptCounter = 0;
        for (Player player : players) {
            if (player.getIsBankrupt()) {
                bankruptCounter++;
            }
            if (bankruptCounter == players.length - 1) {
                gameHasAWinner = true;
            }
        }
        return gameHasAWinner;
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

    private void endGame() {
        int winnerPlayerNum = determineWinner();
        String winnerName;
        // outputs a message in the gui about who the winner is, and ends the game
        if (winnerPlayerNum > 0) {
            winnerName = players[winnerPlayerNum - 1].getName();
            ViewController_GUIMessages.getInstance().showTakeTurnMessage(86, winnerName, "", "");
            System.exit(0);
        }
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

    public Player getPlayerObject(int playerNum) {
        return players[playerNum - 1];
    }

    public String getPlayerName(int playerNum) {
        return players[playerNum - 1].getName();
    }
}
