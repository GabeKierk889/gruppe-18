public class ItsYourBirthday extends ChanceCard
{
    // Det er din fødselsdag! Alle giver dig M1 TILLYKKE MED FØDSELSDAGEN!
    public void receiveBirthdayMoney()
    {
        int currentPlayer = MonopolyGame.game.getCurrentPlayerNumber();
        final int GIFTAMOUNT = 1;

        for (int i = 0; i< MonopolyGame.game.getTotalPlayers(); i++)
        {
            if(currentPlayer != i + 1)
            {
                MonopolyGame.game.getPlayerObject(i + 1).getAccount().transferMoney(GIFTAMOUNT, currentPlayer);
            }
        }

    }
}