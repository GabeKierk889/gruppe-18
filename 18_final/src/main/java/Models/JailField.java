package Models;

import Controllers.ViewController;

public class JailField extends Field {

    public JailField(String fieldName) {
        super(fieldName);
    }

    @Override
    public void landOnField(Player currentplayerobject) {
        currentplayerobject.setIsInJail(true);
        ViewController.getInstance().goToJailMessage();
    }

    @Override
    public boolean isJailField() {
        return true;
    }

}
