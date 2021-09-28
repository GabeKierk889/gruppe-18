package Game;
import java.util.Scanner;

public class DiceGame {
    public static void main(String[]args){
        DiceCup pair1 = new DiceCup();
        DiceCup pair2 = new DiceCup();
        int gameRound = 0;
        int winner = 0;
        int sum1 = 0; int sum2 = 0;
        int lastThrow1 = 0; int lastThrow2 = 0;
        int lastSum1 = 0; int lastSum2 = 0;
        Scanner scan = new Scanner(System.in);
        while (winner==0){
            gameRound++;
            System.out.println("Player " + PlayerTurn(gameRound)+ " turn: Press enter to roll");
            String Str = scan.nextLine();
            switch (PlayerTurn(gameRound)) {
                case 1:
                    lastThrow1 = pair1.getSum();
                    lastSum1 = sum1;
                    pair1.roll();
                    if (pair1.getSum() == 2) {
                        sum1 = 0;}
                    else
                        sum1 += pair1.getSum();
                    System.out.println("Player 1" + "\t" + pair1);
                    System.out.println("\t" +"\t"+"\t"+"\t" +"\t"+"\t"+"  Running total: " + sum1);
                    if (pair1.getSum() == 2) {
                        System.out.println("You threw two 1's - you lost all your points!");}
                    gameRound += pair1.sameFaceValue(); //To skip the next player's turn when you throw two of the same
                    if ((sum1 >= 40) && pair1.sameFaceValue() == 0)
                        System.out.println("You need to throw a pair of the same to win");
                    if (pair1.sameFaceValue() == 1 && lastSum1 < 40 )
                        System.out.println("You threw a pair of the same - you get an extra turn");
                    break;
                case 2:
                    lastThrow2 = pair2.getSum();
                    lastSum2 = sum2;
                    pair2.roll();
                    if (pair2.getSum() == 2) {
                        sum2 = 0;}
                    else
                        sum2 += pair2.getSum();
                    System.out.println("Player 2" + "\t" + pair2);
                    System.out.println("\t" +"\t"+"\t"+"\t" +"\t"+"\t"+"  Running total: " + sum2);
                    if (pair2.getSum() == 2) {
                        System.out.println("You threw two 1's - you lost all your points!");}
                    gameRound += pair2.sameFaceValue(); //To skip the next player's turn when you throw two of the same
                    if ((sum2 >= 40) && pair2.sameFaceValue() == 0)
                        System.out.println("You need to throw a pair of the same to win");
                    if (pair2.sameFaceValue() == 1 && lastSum2 < 40 )
                        System.out.println("You threw a pair of the same - you get an extra turn");
                    break;
            }
            System.out.println();
            if (lastSum1 >= 40 && pair1.getSum() != 2 && pair1.sameFaceValue() == 1)
                winner =1;
            if (lastSum2 >= 40 && pair2.getSum() != 2 && pair2.sameFaceValue() == 1)
                winner =2;
            if (lastThrow1 + pair1.getSum() == 24) // if player throws two 6's in a row, player wins
                { winner = 1;
                System.out.println("Congratulations, you threw a pair of 6's twice in a row - you win!");}
            if (lastThrow2 + pair2.getSum() == 24) // if player throws two 6's in a row, player wins
                { winner = 2;
                System.out.println("Congratulations, you threw a pair of 6's twice in a row - you win!");}
        }
        System.out.printf("Player " + winner + " wins this game");
        scan.close();
    }

    public static int PlayerTurn(int Round) {
        if (Round % 2 == 0)
            return 2;
        else
            return 1;
    }
}