import Game.*;

import gui_fields.GUI_Board;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_fields.GUI_Street;
import gui_fields.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public class GUITest {

    public static void main(String[] args) {
        Game game = new Game();

        // Setting up fields
        GUI_Field[] fields = new GUI_Field[11];
        GUI_Street[] streets = new GUI_Street[11];

        for (int i = 0; i < 11; i++) {
            streets[i] = new GUI_Street();
            streets[i].setTitle(""+(i+2));
            streets[i].setSubText(game.getField(i).getFieldSubtext());
            streets[i].setDescription(game.getField(i).getFieldDescription());
            streets[i].setBackGroundColor(Color.gray);
            fields[i] = streets[i];
        }

        GUI_Board board = new GUI_Board(fields, Color.lightGray); // Setting up fields and background color

        // Setting up cars
        GUI_Car car1 = new GUI_Car();
        car1.setPrimaryColor(Color.RED);
        car1.setSecondaryColor(Color.ORANGE);

        GUI_Car car2 = new GUI_Car();
        car2.setPrimaryColor(Color.BLUE);
        car2.setSecondaryColor(Color.CYAN);

        // Setting up players
        GUI_Player player1 = new GUI_Player(game.getPlayerObject(1).getName(), game.getPlayerObject(1).getAccount().getBalance(), car1);
        GUI_Player player2 = new GUI_Player(game.getPlayerObject(2).getName(), game.getPlayerObject(2).getAccount().getBalance(), car2);
        board.addPlayer(player2);
        board.addPlayer(player1);

        // Message (can be used for user input with second arg)
        board.getUserInput("Welcome to DiceGame v2! Each player starts with a balance of 1000. The first to reach 3000 wins.\n\nClick anywhere to roll the dice");

        // Placing cars on fields
        streets[1].setCar(player1,true); // Display player 1 on street 1
        fields[2].setCar(player2,true); // Display player 2 on field 2

        // Referencing the dice to the DiceCup set up by the game
        Die die1 = game.getCup().getDie1();
        Die die2 = game.getCup().getDie2();

        // User input: dice rolls on mouse click
        MouseInputListener listen = new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                game.getCup().roll();
                // Testing a roll's effect on player 1's balance
                game.updateBalance(1,game.getCup().getSum());
                // Showing updated player balance
                player1.setBalance(game.getPlayerObject(1).getAccount().getBalance());
                player2.setBalance(game.getPlayerObject(2).getAccount().getBalance());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                board.setDice((int)(Math.random()*11),(int)(Math.random()*11),die1.getFaceValue(),(int)(Math.random()*359),
                        (int)(Math.random()*11),(int)(Math.random()*11),die2.getFaceValue(),(int)(Math.random()*359));
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        };
        board.addMouseListener(listen);


    }
}

