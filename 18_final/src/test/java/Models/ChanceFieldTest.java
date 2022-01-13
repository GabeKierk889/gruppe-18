/**
 * TC 08
 */

package Models;

import static org.junit.jupiter.api.Assertions.*;

import Models.ChanceCardSubType.JailReleaseCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ChanceFieldTest { // tested by Xiao 7 Jan 22
    //This code is from previous assignment CDIO 3 by Maj Kyllesbech, Gabriel H, Kierkegaard, Mark Bidstrup & Xiao Chen handed in 26. November 2021

    @Test
    void drawChanceCardFromTop() {
        ChanceField field = new ChanceField("");
        ChanceCard drawn = ChanceField.drawChanceCard();
        // checks that the card drawn is put back in the bottom of the stack (but not jail cards)
        if (drawn instanceof JailReleaseCard)
            assertNull(ChanceField.getCard(0));
        else
            Assertions.assertEquals(drawn, ChanceField.getCard(0));
        // checks that there are no duplicates of the card that has been drawn
        for (int i = ChanceField.getTotalNumberOfChanceCards()-1; i>0; i--)
        {
            assertNotEquals(drawn,ChanceField.getCard(i));
        }
    }

    @Test
    void shuffleCards() {
        ChanceField field = new ChanceField("");

        boolean isStackDifferent = false;

        // copies the current array of cards
        ChanceCard[] array = new ChanceCard[ChanceField.getTotalNumberOfChanceCards()];
        for (int i = ChanceField.getTotalNumberOfChanceCards()-1; i>0; i--)
            array[i] = ChanceField.getCard(i);

        ChanceField.shuffleChanceCards();

        // checks that there is at least one difference in the stack of cards after it has been shuffled
        for (int i = ChanceField.getTotalNumberOfChanceCards()-1; i>0; i--)
        {
            if (array[i] != ChanceField.getCard(i)) {
                isStackDifferent = true;
                break;
            }
        }
        assert isStackDifferent;
    }

    @Test
    void putAllNullCardsinTheBottomOfDeck() {
        ChanceField field = new ChanceField("");

        // draws all the chance cards to ensure we draw the jail card
        ChanceCard jailCard = null;
        for (int i = ChanceField.getTotalNumberOfChanceCards()-1; i>0; i--)
            ChanceField.drawChanceCard();

        // shuffles the deck 1000 times and checks that the null card is always put at the bottom
        for (int i = 0 ; i< 1000; i++) {
            ChanceField.shuffleChanceCards();
            ChanceField.putAllNullCardsinBottom();
            // checks that the bottom card is null
            assertNull(ChanceField.getCard(0));

            for (int k = 2; k< ChanceField.getTotalNumberOfChanceCards(); k++) {
                // checks that all the other cards are not null
                ChanceCard temp = ChanceField.getCard(k);
                assertNotNull(temp);
                // checks that there are no duplicate cards in the stack
                for (int j = 0; j< ChanceField.getTotalNumberOfChanceCards(); j++) {
                    if(j != k)
                        assertNotEquals(temp,ChanceField.getCard(j));
                }
            }
        }
    }

    @Test
    void putBackChanceCard() {
        ChanceField field = new ChanceField("");

        ChanceCard jailCard = null;
        // draws all the chance cards to ensure we draw the jail card
        for (int i = ChanceField.getTotalNumberOfChanceCards()-1; i>0; i--) {
            ChanceField.drawChanceCard();
            if (ChanceField.getCurrentCard() instanceof JailReleaseCard)
                jailCard = ChanceField.getCurrentCard();
        }
        // put back the jail card
        ChanceField.putBackChanceCard(jailCard);
        // check that the deck of cards is complete / no empty slots
        for (int i = ChanceField.getTotalNumberOfChanceCards()-1; i>0; i--) {
            assertNotEquals(ChanceField.getCard(i),null);
        }
    }

    @Test
    void getCurrentCard() {
        ChanceField field = new ChanceField("");

        // checks that the card that was drawn last is returned via getCurrentCard
        ChanceCard drawn = ChanceField.drawChanceCard();
        Assertions.assertEquals(drawn,ChanceField.getCurrentCard());
    }

}