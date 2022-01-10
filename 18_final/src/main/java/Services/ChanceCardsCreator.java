package Services;

import Models.ChanceCard;
import Models.ChanceCardSubType.*;

    // sets up the chance cards in an array using the data pulled from the external text file
    // has a method that creates and returns a deck of chance cards
public class ChanceCardsCreator {
    public ChanceCard[] createChanceCardsDeck() {
        ChanceCardsDataReader read = new ChanceCardsDataReader("Chancecards_text.txt");
        String[] text = read.getChanceCardsTextArray();
        String[] fieldNameWithinText = read.getFieldNameWithinText();
        int[][] numbersInChanceCardText = read.getNumbersFromChanceCardsText();

        // uses the external file to load chance card text, and identifies any numbers within the string
        // sets up the chance cards in the order of the lines in the external file
        return new ChanceCard[]{
//                new PayMoneyCard(text[0],numbersInChanceCardText[0]),
//                new PayMoneyCard(text[1],numbersInChanceCardText[1]),
//                new PayMoneyCard(text[2],numbersInChanceCardText[2]),
//                new PayMoneyCard(text[3],numbersInChanceCardText[3]),
//                new PayMoneyCard(text[4],numbersInChanceCardText[4]),
//                new PayMoneyCard(text[5],numbersInChanceCardText[5]),
//                new PayMoneyCard(text[6],numbersInChanceCardText[6]),
//                new PayMoneyCard(text[7],numbersInChanceCardText[7]),
//                new PayMoneyCard(text[8],numbersInChanceCardText[8]),
//                new PayMoneyCard(text[9],numbersInChanceCardText[9]),
//                new PayMoneyForBuildingsCard(text[10],numbersInChanceCardText[10]),
//                new PayMoneyForBuildingsCard(text[11],numbersInChanceCardText[11]),
//                new ReceiveMoneyCard(text[12],numbersInChanceCardText[12]),
//                new ReceiveMoneyCard(text[13],numbersInChanceCardText[13]),
//                new ReceiveMoneyCard(text[14],numbersInChanceCardText[14]),
//                new ReceiveMoneyCard(text[15],numbersInChanceCardText[15]),
//                new ReceiveMoneyCard(text[16],numbersInChanceCardText[16]),
//                new ReceiveMoneyCard(text[17],numbersInChanceCardText[17]),
//                new ReceiveMoneyCard(text[18],numbersInChanceCardText[18]),
//                new ReceiveMoneyCard(text[19],numbersInChanceCardText[19]),
//                new ReceiveMoneyCard(text[20],numbersInChanceCardText[20]),
//                new ReceiveMoneyCard(text[21],numbersInChanceCardText[21]),
//                new ReceiveMoneyCard(text[22],numbersInChanceCardText[22]),
//                new ReceiveMoneyCard(text[23],numbersInChanceCardText[23]),
//                new ReceiveFromEachPlayerCard(text[24],numbersInChanceCardText[24]),
//                new ReceiveFromEachPlayerCard(text[25],numbersInChanceCardText[25]),
//                new ReceiveFromEachPlayerCard(text[26],numbersInChanceCardText[26]),
//                new MoveXFieldsForward(text[27],numbersInChanceCardText[27]),
//                new MoveXFieldsBackwards(text[28],numbersInChanceCardText[28]),
//                new MoveXFieldsBackwards(text[29],numbersInChanceCardText[29]),
                new MoveToFieldCard(text[30],fieldNameWithinText[30]),
                new MoveToFieldCard(text[31],fieldNameWithinText[31]),
//                new MoveToFieldCard(text[32],fieldNameWithinText[32]),
//                new MoveToFieldCard(text[33],fieldNameWithinText[33]),
//                new MoveToFieldCard(text[34],fieldNameWithinText[34]),
//                new MoveToFieldCard(text[35],fieldNameWithinText[35]),
//                new MoveToFieldCard(text[36],fieldNameWithinText[36]),
//                new MoveToFieldCard(text[37],fieldNameWithinText[37]),
//                new MoveToFieldCard(text[38],fieldNameWithinText[38]),
//                new MoveToFieldCard(text[39],fieldNameWithinText[39]),




                new JailReleaseCard(text[43]),
                new JailReleaseCard(text[44]),
        };
    }
}
