package Game;

import Game.DiceCup;
import java.util.Scanner;

public class DiceGame {
    public static void main(String[]args){
        DiceCup pair1 = new DiceCup();
        DiceCup pair2 = new DiceCup();
        int gameRound = 0;
        int winner = 0;
        int sum1 = 0; int sum2 = 0;
        Scanner scan = new Scanner(System.in);
        while (winner==0){
            gameRound++;
            System.out.println("Player " + PlayerTurn(gameRound)+ " turn: Press enter to roll");
            String Str = scan.nextLine();
            switch (PlayerTurn(gameRound)) {
                case 1:
                    pair1.roll();
                    sum1 += pair1.getSum();
                    System.out.println("Player 1" + "\t" + pair1);
                    System.out.println("\t" +"\t"+"\t"+"\t" +"\t"+"\t"+"  Running total: " + sum1);
                    break;
                case 2:
                    pair2.roll();
                    sum2 += pair2.getSum();
                    System.out.println("Player 2" + "\t" + pair2);
                    System.out.println("\t" +"\t"+"\t"+"\t" +"\t"+"\t"+"  Running total: " + sum2);
                    break;
            }
            System.out.println();
            if (sum1 >= 40)
                winner =1;
            if (sum2 >= 40)
                winner =2;
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





