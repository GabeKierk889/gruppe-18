package Game;

public class Jail extends Field {
    public Jail(String fieldName) {
        super(fieldName);
    }

    @Override
    public void landOnField(Player currentplayerobject) {
        currentplayerobject.setIsInJail(true);
        MonopolyGame.goToJailMessage();
    }
}
