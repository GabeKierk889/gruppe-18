package Services;

import Models.ChanceCard;
import Models.ChanceCardSubType.*;

    // sets up the chance cards in an array using the data pulled from the external text file
    // has a method that creates and returns a deck of chance cards
public class ChanceCardsCreator {
    public ChanceCard[] createChanceCardsDeck() {
        ChanceCardsTextReader read = new ChanceCardsTextReader("Chancecards_text.txt");
        String[] text = read.getChanceCardsTextArray();
        int[][] numbersInChanceCardText = read.getNumbersFromChanceCardsText();

        // uses the external file to load chance card text, and identifies any numbers within the string
        // sets up the chance cards in the order of the lines in the external file
        return new ChanceCard[]{
                new PayMoneyCard(text[0],numbersInChanceCardText[0]),
                new PayMoneyCard(text[1],numbersInChanceCardText[1]),
                new PayMoneyCard(text[2],numbersInChanceCardText[2]),
                new PayMoneyCard(text[3],numbersInChanceCardText[3]),
                new PayMoneyCard(text[4],numbersInChanceCardText[4]),
                new PayMoneyCard(text[5],numbersInChanceCardText[5]),
                new PayMoneyCard(text[6],numbersInChanceCardText[6]),
                new PayMoneyCard(text[7],numbersInChanceCardText[7]),
                new PayMoneyCard(text[8],numbersInChanceCardText[8]),
                new PayMoneyCard(text[9],numbersInChanceCardText[9]),

                new JailReleaseCard(text[43]),
                new JailReleaseCard(text[44])
        };
    }
}
