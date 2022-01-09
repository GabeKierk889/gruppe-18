package Models;

public class JailField extends Field {

    public JailField(String fieldName) {
        super(fieldName);
    }

    @Override
    public void landOnField(Player currentplayerobject) {
        currentplayerobject.setIsInJail(true);
        // TODO: gui
        // placeholder - print out message to GUI that player is now in jail
    }

    @Override
    public boolean isJailField() {
        return true;
    }

}
