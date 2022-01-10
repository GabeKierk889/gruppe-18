package Models;

import Controllers.GameController;
import Controllers.ViewController;

public class JailField extends Field {

    public JailField(String fieldName) {
        super(fieldName);
    }

    @Override
    public void landOnField(Player currentplayerobject) {
        currentplayerobject.setIsInJail(true);
        ViewController.getInstance().goToJailMessage();
        currentplayerobject.movePlayerToField(10);
        ViewController.getInstance().moveGUICar(30,10, GameController.getInstance().getCurrentPlayerNum());
    }

    @Override
    public boolean isJailField() {
        return true;
    }

}
