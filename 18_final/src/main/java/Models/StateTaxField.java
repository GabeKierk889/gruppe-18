package Models;

public class StateTaxField extends TaxField {
    public StateTaxField(String fieldName, int taxAmount) {
        super(fieldName, taxAmount);
    }

    @Override
    public int calculateTax(Player currentplayerobject) {
        return FIXEDTAX;
    }
}
