package Models;

public class JailField extends Field {

    public JailField(String fieldName) {
        super(fieldName);
    }

    @Override
    public void landOnField(Player currentplayerobject) {
        currentplayerobject.setIsInJail(true);
        // placeholder - print out message to GUI
    }

    @Override
    public boolean isJailField() {
        return true;
    }

}
