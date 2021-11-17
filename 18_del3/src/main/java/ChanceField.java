public class ChanceField extends Field {
    private static ChanceCard currentCard;

    private static ChanceCard[] chanceCard = {

            new ItsYourBirthday(), new ReleaseFromJailCard(), new MoveToFieldCard("The Beach Walk"),
            new MoveToFieldCard("START"), new MoveUpToFiveFieldsCard(),
            new PaytoTheBankCard("You've eaten too much candy",2),
            new ReceiveFromTheBankCard("You've completed all your homework",2)
        };

    public ChanceField(String fieldName) {
        super(fieldName);
        shuffleCards(); // shuffles the deck of cards when the field is instantiated
    }

    @Override
    public void landOnField(Player currentplayerobject) {
        currentCard = drawChanceCard();
        MonopolyGame.showChanceCardMessage(); // shows the player the chance card via GUI, then implements the effect
        currentCard.effect(currentplayerobject);
    }

    public static ChanceCard drawChanceCard() {
//        USE THE LINE BELOW IF YOU NEED TO TEST YOU CHANCE CARD -
//        change the number according to your chance card index num in the array
//        and comment out the rest of this method

//        return chanceCard[4];

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
        if (card != null && chanceCard[0] == null) {
        chanceCard[0] = card;
        putAllNullCardsinTheBottomOfDeck();
        }
    }

    private static void putAllNullCardsinTheBottomOfDeck() {
        // gets rid of gaps / empty slots in the array and puts them all in the bottom / toward index 0
        int nulls = 0; int nullsInARow;
        for (int i = chanceCard.length - 1; i > nulls-1; i--) {
            if (chanceCard[i] == null) {
                nulls++;
                int k = i;
                nullsInARow = 1;
                int nullsBeforeLoop = nulls;
                // counts the number of nulls in a row
                while (k> nullsBeforeLoop-1 && chanceCard[k-1]== null) {
                    nulls++;
                    nullsInARow++;
                    k--;
                }
                // gets rid of the current gap in the stack of cards / currently found null cards(s)
                for (int j = i; j > nullsInARow-1; j--)
                    chanceCard[j] = chanceCard[j - nullsInARow];
            }
        }
        // puts all the found null values in the bottom of the array
        for (int i = 0; i< nulls; i++)
            chanceCard[i] = null;
    }

    public static ChanceCard getCurrentCard() {
        return currentCard;
    }
}
