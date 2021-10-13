package Game;

public class Game {
    private Player player[];
    private Field gameFields[];
    private DiceCup cup;


    public Game() {
        cup = new DiceCup();
        player = new Player[]{new Player("Player 1"), new Player("Player 2")};
        gameFields = new Field[]{
                new Field("Tower", "You climbed an abandoned Tower and found a treasure at the top which you sell for 250", true,250),
                new Field("Crater","You dropped your watch down a deep Crater - it will cost you 100 to replace it",false,100),
                new Field("Palace Gates","You made it to the Palace Gates and receive 100 from the generous king", true,100),
                new Field("Cold Desert","You find yourself in the Cold Desert - you need to pay 20 for a warm scarf", false, 20),
                new Field("Walled City","You made it to the Walled City - you spend the day working at the inn and earn 180",true,180),
                new Field("Monastery","You made a retreat to the Monastery. You meditate in peace. Your balance is unchanged", true,0),
                new Field("Black Cave","You need to venture into the Black Cave - you have to pay 70 to buy some high quality headlights",false,70),
                new Field("MountainHut","You made it to some Huts in the Mountain - a generous mountain dweller gives you 60 in pocket money for your onward journey", true, 60),
                new Field("WereWall","You accidentally went to the WereWall! You lose 80 of your money as you run away from the werewolves, but you get an extra turn", false,80),
                new Field("The Pit","Oh no, you fell into The Pit - you are completely muddy and dirty and have to pay 50 to buy new clothes", false,50),
                new Field("Goldmine","You hit the jackpot! You found a Goldmine in the mountains and cash in 650!", true,650),
        };
    }
    public void updateBalance(int playernumber, int fieldnumber) {
        if (gameFields[fieldnumber-2].getFieldPosiveEffect())
            player[playernumber-1].getAccount().depositMoney(gameFields[fieldnumber-2].getFieldEffect());
        else
            player[playernumber-1].getAccount().withdrawMoney(gameFields[fieldnumber-2].getFieldEffect());
    }
    public Field getField(int no) {
        return gameFields[no];
    }
    public Player getPlayerObject(int playernumber) {
        return player[playernumber-1];
    }
    public DiceCup getCup() {
        return cup;
    }
}
