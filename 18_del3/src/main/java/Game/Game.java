package Game;

public class Game {
    public final int MAXPLAYERS = 4;
    public final int MINPLAYERS = 2;
    private String[] playerNames;
    private Player[] players;
    private Die die;
    private int currentPlayer;
    private int totalPlayers;
    private Board board;

    public Game(String ... player_names) {
        playerNames = player_names;
        if (playerNames.length > MAXPLAYERS || playerNames.length <MINPLAYERS)
            System.out.println("Error - game cannot be started. There must be "+
                    MINPLAYERS + "-"+ MAXPLAYERS+ " players in the game.");
        else {
            totalPlayers = playerNames.length;
            players = new Player[totalPlayers];
            for (int i = 0; i < totalPlayers; i++) {
                players[i] = new Player(player_names[i]);
            }
            die = new Die();
            board = new Board();
            currentPlayer = 1;
        }
    }

    public void switchTurn(boolean extraTurn) {
        if (!extraTurn) {
            if (currentPlayer < totalPlayers)
                currentPlayer++;
            else
                currentPlayer = 1; }
    }

    public boolean checkIsAnyoneBankrupt() {
        boolean anyoneBankrupt = false;
        for (int i = 0; i < totalPlayers; i++) {
            if (players[i].getAccount().getBalance() <= 0) {
                players[i].setIsBankrupt(true);
                anyoneBankrupt = true;
            }
        }        return anyoneBankrupt;
    }

    public boolean isGameOver() {
        return checkIsAnyoneBankrupt(); // game ends if a player goes bankrupt
    }

    public int determineWinner() {
        int mostMoneyAmount = 0;
        int playerWithMostMoney = 0;
        for (int i = 0; i < totalPlayers; i++) {
            if (players[i].getAccount().getBalance() > mostMoneyAmount) {
                mostMoneyAmount = players[i].getAccount().getBalance();
                playerWithMostMoney = i + 1; // player number is 1 greater than player-array-position
            }
        }
        return playerWithMostMoney;    }

    // checks if there are two winners with the same balance
    public int determineWinner2() {
        int mostMoneyAmount = 0;
        int playerWithMostMoney = 0;
        for (int i = 0; i < totalPlayers; i++) {
            if (players[i].getAccount().getBalance() >= mostMoneyAmount) {
                mostMoneyAmount = players[i].getAccount().getBalance();
                playerWithMostMoney = i + 1; // player number is 1 greater than player-array-position
            }
        }
        return playerWithMostMoney;    }

    public String getBankruptPlayerName() {
        String str = "";
        for (int i = 0; i < totalPlayers; i++) {
            if (players[i].getIsBankrupt())
                str = players[i].getName();
        }
        return str;
    }

    public Die getDie() { return die;  }
    public Board getBoard() { return board; }
    public Player getPlayerObject(int playernumber) { return players[playernumber-1]; }
    public int getCurrentPlayerNumber() { return currentPlayer; }
    public int getTotalPlayers() {return totalPlayers; }
    public void setCurrentPlayer(int playernumber) { currentPlayer = playernumber; }
}
