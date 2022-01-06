package Models;

public class ShippingField extends OwnableField {

    public ShippingField(String fieldName, int price, int[] rentArray) {
        super(fieldName, price, rentArray);
        currentRent = rentArray[0];
    }

    @Override
    public void updateRent() {
    }
}
