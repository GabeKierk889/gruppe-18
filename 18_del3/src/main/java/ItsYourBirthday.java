public class ItsYourBirthday extends ChanceCard
{
    // Det er din fødselsdag! Alle giver dig M1 TILLYKKE MED FØDSELSDAGEN!
    @Override
    public void effect(Player currentplayerobject)
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
        MonopolyGame.updatePlayerBalance();
    }

    @Override
    public String chanceCardText()
    {
        return "It's your birthday! All the other players give you M$1 HAPPY BIRTHDAY!";
    }
}