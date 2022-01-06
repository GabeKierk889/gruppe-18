package Models;

public class BreweryField extends OwnableField {

    public BreweryField(String fieldName, int price, int[] rentArray) {
        super(fieldName, price, rentArray);
    }

    @Override
    public void updateRent() {
    }
}
