package Models;

public class IncomeTaxField extends TaxField {

    public IncomeTaxField(String fieldName, int taxAmount) {
        super(fieldName, taxAmount);
    }

    @Override
    public int calculateTax(Player currentplayerobject) {
        return 0;
    }
}
