public class AmusementField extends Field {
    private int price;

    public AmusementField(String fieldDescription, int price) {
        super(fieldDescription);
        this.price = price;
    }

    @Override
    public void landOnField() {
        // set up booth or pay money
    }
}
