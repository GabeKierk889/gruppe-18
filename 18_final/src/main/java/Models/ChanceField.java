package Models;

//This code has been modified from previous assignment CDIO 3 by Maj Kyllesbech, Gabriel H, Kierkegaard, Mark Bidstrup & Xiao Chen handed in 26. November 2021

public class ChanceField extends Field{

    private static ChanceCard currentCard;

    private static ChanceCard[] chanceCards = {

    };

    public ChanceField(String fieldName) {
        super(fieldName);
        shuffleChanceCards(); // shuffles the deck of cards when the field is instantiated
    }

    @Override
    public void landOnField(Player currentplayerobject) {
        drawChanceCard();
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
        ChanceCard drawn = chanceCards[chanceCards.length-1];
        for (int i = chanceCards.length-1; i > 0; i--) {
            chanceCards[i] = chanceCards[i-1];
        }
        if (drawn instanceof ReleaseFromJailCard)
            chanceCards[0] = null;
        else
            chanceCards[0] = drawn;
        putAllNullCardsinBottom();
        currentCard = drawn;
        return drawn;
    }

    public static void shuffleChanceCards(){
        // shuffles / mixes up the deck of cards by finding a random card to swap places with
        ChanceCard temp;
        for (int i = chanceCards.length-1; i > -1; i--) {
            temp = chanceCards[i];
            int random = (int) (Math.random()* chanceCards.length);
            chanceCards[i] = chanceCards[random];
            chanceCards[random] = temp;
        }
        putAllNullCardsinBottom();
    }

    public static void putBackChanceCard(ChanceCard card) {
        // inserts a chance card back into the bottom of the pile of cards (but before any other null values)
        if (card != null && chanceCards[0] == null) {
            chanceCards[0] = card;
            putAllNullCardsinBottom();
        }
    }

    public static void putAllNullCardsinBottom() {
        // gets rid of gaps / empty slots in the array and puts them all in the bottom / toward index 0
        int nulls = 0; int nullsInARow;
        for (int i = chanceCards.length - 1; i > nulls-1; i--) {
            if (chanceCards[i] == null) {
                nulls++;
                int k = i;
                nullsInARow = 1;
                int nullsBeforeLoop = nulls;
                // counts the number of nulls in a row
                while (k> nullsBeforeLoop-1 && chanceCards[k-1]== null) {
                    nulls++;
                    nullsInARow++;
                    k--;
                }
                // gets rid of the current gap in the stack of cards / currently found null cards(s)
                for (int j = i; j > nullsInARow-1; j--)
                    chanceCards[j] = chanceCards[j - nullsInARow];
            }
        }
        // puts all the found null values in the bottom of the array
        for (int i = 0; i< nulls; i++)
            chanceCards[i] = null;
    }

    public static ChanceCard getCurrentCard() {
        return currentCard;
    }

    public static ChanceCard getCard(int arrayindex) { return chanceCards[arrayindex];  }

    public static int getTotalNumberOfChanceCards() {
        return chanceCards.length;
    }

}
