package Models;

import Controllers.GameController;

public class ShippingField extends OwnableField {

    public ShippingField(String fieldName, int price, int[] rentArray) {
        super(fieldName, price, rentArray);
    }

    @Override
    public void updateRent() {
        int fieldsOwned = GameController.getInstance().getBoard().numOfShippingFieldsOwned(ownerNum);
        if (fieldsOwned > 0 && fieldsOwned < RENTARRAY.length)
            currentRent = RENTARRAY[fieldsOwned-1];
    }

    @Override
    public boolean isShippingField() {
        return true;
    }
}
