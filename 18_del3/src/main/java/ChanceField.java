public class ChanceField extends Field {
    private ChanceCard currentCard;

    private ChanceCard[] chanceCard =
            {
                    new ItsYourBirthday()
            };

    public ChanceField(String fieldName) {
        super(fieldName);
    }

    @Override
    public void landOnField(Player currentplayerobject) {
        currentCard = drawChanceCard();
        MonopolyGame.chanceCardMessage();
    }

    public ChanceCard drawChanceCard()
    {
        ChanceCard tmpCard = chanceCard[0];
        return tmpCard;
    }

    public ChanceCard getCurrentCard() {
        return currentCard;
    }
}
