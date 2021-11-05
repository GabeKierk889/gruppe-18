public class AmusementField extends Field {
    private int price;

    public AmusementField(String fieldName, int price) {
        super(fieldName);
        this.price = price;
    }

    @Override
    public void landOnField() {
        // set up booth or pay money
    }

    public int getPrice() {
        return price;
    }
}
