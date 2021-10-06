package Game;

public class DiceGame {
    public static void main(String[] args) {
        Field[] gameField = {
                new Field(null,false,0),new Field(null,false,0), // empty array placeholders to allow array index to match up to 2-12
                new Field("You climbed an abandoned Tower and found a treasure at the top which you sell for 250", true,250),
                new Field("You dropped your watch down a deep Crater - it will cost you 100 to replace it",false,100),
                new Field("You made it to the Palace Gates and receive 100 from the generous king", true,100),
                new Field("You find yourself in the Cold Desert - you need to pay 20 for a warm scarf", false, 20),
                new Field("You made it to the Walled City - you spend the day working at the inn and earn 180",true,180),
                new Field("You made a retreat to the Monastery. You meditate in peace. Your balance is unchanged", true,0),
                new Field("You need to venture into the Black Cave - you have to pay 70 to buy some high quality headlights",false,70),
                new Field("You made it to some Huts in the Mountain - a generous mountain dweller gives you 60 in pocket money for your onward journey", true, 60),
                new Field("You accidentally went to the WereWall! You lose 80 of your money as you run away from the werewolves, but you get an extra turn", false,80),
                new Field("Oh no, you fell into The Pit - you are completely muddy and dirty and have to pay 50 to buy new clothes", false,50),
                new Field("You hit the jackpot! You found a Goldmine in the mountains and cash in 650!", true,650),
        };
        for (int t= 2; t<gameField.length; t++) {
            System.out.println(gameField[t]);
        }
    }
}
