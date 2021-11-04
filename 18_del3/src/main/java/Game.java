public class Game {
    private Player player[];
    private Die die;
    private int currentPlayer;
    private int nextPlayer;
    private final int totalPlayers = 2; // to be updated
    private int winner;
    public final int STARTBONUS = 2;
    private Board board;

    public Game() {
        currentPlayer = 1;
        nextPlayer = 1;
        die = new Die();
        board = new Board();
    }

    public void switchTurn(boolean extraTurn) {
        if (!extraTurn) {
            if (currentPlayer < totalPlayers)
                currentPlayer++;
            else
                currentPlayer = 1; }
    }

    public int nextPlayer(boolean extraTurn) {
        if (!extraTurn) {
            if (currentPlayer < totalPlayers)
                nextPlayer = currentPlayer+1;
            else
                nextPlayer = 1; }
        return nextPlayer;
    }

    public boolean checkIsAnyoneBankrupt() {
        boolean anyoneBankrupt = false;
        // check if any player has lost all their money
        return anyoneBankrupt;
    }

    public boolean isGameOver() {
        return checkIsAnyoneBankrupt(); // game ends if a player goes bankrupt
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public Die getDie() { return die;  }
    public Board getBoard() { return board; }
    public Player getPlayerObject(int playernumber) { return player[playernumber-1]; }
    public int getCurrentPlayerNumber() { return currentPlayer; }
    public int getTotalPlayers() {return totalPlayers; }
    public int getNextPlayerNumber() { return nextPlayer;}
    public int getWinner() { return winner; }
}
