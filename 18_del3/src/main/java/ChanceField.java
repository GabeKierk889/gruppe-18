public class ChanceField extends Field {
    private static ChanceCard currentCard;

    private static ChanceCard[] chanceCard = {

            // added an extra jail card for now - need minimum 3 cards to test drawCard method
            new ItsYourBirthday(), new ReleaseFromJailCard(), new ReleaseFromJailCard()
        };

    public ChanceField(String fieldName) {
        super(fieldName);
        shuffleCards();
    }

    @Override
    public void landOnField(Player currentplayerobject) {
        currentCard = drawChanceCard();
        MonopolyGame.showChanceCardMessage();
        currentCard.effect(currentplayerobject);
    }

    public static ChanceCard drawChanceCard() {
//        USE THE LINE BELOW IF YOU NEED TO TEST YOU CHANCE CARD -
//        change the number according to your chance card index num in the array
//        and comment out the rest of this method

//        return chanceCard[3];

        // draws the highest indexed card from the stack/array of cards, and puts it back in the bottom
        // unless it is a releasefromJail card, which does not get put back
        ChanceCard drawn = chanceCard[chanceCard.length-1];
        for (int i = chanceCard.length-1; i > 0; i--) {
            chanceCard[i] = chanceCard[i-1];
        }
        if (drawn instanceof ReleaseFromJailCard)
            chanceCard[0] = null;
        else
            chanceCard[0] = drawn;
        putAllNullCardsinTheBottomOfDeck();
        return drawn;
    }

    public static void shuffleCards(){
        // shuffles / mixes up the deck of cards by finding a random card to swap places with
        ChanceCard temp;
        for (int i = chanceCard.length-1; i > -1; i--) {
            temp = chanceCard[i];
            int random = (int) (Math.random()*chanceCard.length);
            chanceCard[i] = chanceCard[random];
            chanceCard[random] = temp;
        }
        putAllNullCardsinTheBottomOfDeck();
    }

    public static void putBackChanceCard(ChanceCard card) {
        // inserts a chance card back into the bottom of the pile of cards (but before any other null values)
        if (card != null) {
        chanceCard[0] = card;
        putAllNullCardsinTheBottomOfDeck();
        }
    }

    private static void putAllNullCardsinTheBottomOfDeck() {
        // private support method to put empty slots in the bottom of the card array
        // gets rid of gaps / empty slots in the array and puts all null cards in the bottom / close to index 0
        // this code can accommodate maximum two empty slots
        int nulls = 0;
        for (int i = chanceCard.length - 1; i > nulls-1; i--) {
            if (chanceCard[i] == null) {
                nulls++;
                if (i>0 && chanceCard[i-1] != null)
                    for (int j = i; j > 0; j--) {
                        chanceCard[j] = chanceCard[j - 1]; }
                else if (i>1 && chanceCard[i-1] == null){
                    nulls++;
                    for (int j = i; j > 1; j--) {
                        chanceCard[j] = chanceCard[j - 2]; } } } }
        // puts all the found null values in the bottom of the array
        for (int i = 0; i< nulls; i++) {
            chanceCard[i] = null; }
    }

    public static ChanceCard getCurrentCard() {
        return currentCard;
    }
}
