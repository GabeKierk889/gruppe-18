package Models;

public abstract class TaxField extends Field {
    protected final int FIXEDTAX;

    public TaxField(String fieldName, int taxAmount) {
        super(fieldName);
        FIXEDTAX = taxAmount;
    }

    @Override
    public void landOnField(Player currentplayerobject) {
        calculateTax(currentplayerobject);
        payTax(currentplayerobject);
    }

    public abstract int calculateTax (Player currentplayerobject);

    private void payTax (Player currentplayerobject) {
        // placeholder
    }

}
