import gui_fields.*;
import java.util.Scanner;

public class MonopolyGame {
    private static Game game;
    private static GUI_Field[] fields;
    private static GUI_Street[] streets;
    private static GUI_Car[] car;
    private static GUI_Player[] player;
    private static GUI_Board board;

    public static void main(String[] args) {
        // Calling a support method that takes user input/names and initializes the game with 2-4 players
        initializeGame();
        
        // Referencing the GUI die to the die set up by the game
        Die die1 = game.getDie();
    }

    private static void initializeGame() {
        System.out.println("Enter the names of 2-4 players who will be playing today (on 1 line separated by spaces only)");
        Scanner scan = new Scanner(System.in);
        String str = scan.nextLine();
        scan.close();
        String[] strarray = str.split(" ");
        switch (strarray.length) {
            case 2: game = new Game(strarray[0],strarray[1]); break;
            case 3: game = new Game(strarray[0],strarray[1],strarray[2]); break;
            case 4: game = new Game(strarray[0],strarray[1],strarray[2],strarray[3]); break;
            default: game = new Game(strarray[0]);
        }
    }
}
