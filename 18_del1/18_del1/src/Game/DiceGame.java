package Game;
import java.util.Scanner;

public class DiceGame {
    public static void main(String[] args) {
        int playerOneSum = 0, playerTwoSum = 0;
        boolean gameOn = true, playerOneTurn = true, playerTwoTurn = true;
        String turn;

        DiceCup dc = new DiceCup();
        Scanner scanner = new Scanner(System.in);

        while (gameOn) {

            while (playerOneTurn) {
                playerOneTurn = false;
                System.out.print("Player 1 turn, press enter to roll the dice: ");
                turn = scanner.nextLine();
                dc.roll();
                System.out.println(dc);
                playerOneSum += dc.getSum();
                System.out.println("The total sum is: " + playerOneSum);
                System.out.println();
            }
            if (playerOneSum >= 40) {
                gameOn = false;
            }
            if (playerOneSum < 40) {
                while (playerTwoTurn) {
                    playerTwoTurn = false;
                    System.out.print("Player 2 turn, press enter to roll the dice");
                    turn = scanner.nextLine();
                    dc.roll();
                    System.out.println(dc);
                    playerTwoSum += dc.getSum();
                    System.out.println("The total sum is: " + playerTwoSum);
                    System.out.println();
                }
            }
            playerTwoTurn = true;

            if (playerTwoSum >= 40) {
                gameOn = false;
            }
            playerOneTurn = true;
        }
    }
}
