package Models;

public class IncomeTaxField extends TaxField {

    public IncomeTaxField(String fieldName, int taxAmount) {
        super(fieldName, taxAmount);
    }

    @Override
    public int calculateTax(Player currentplayerobject) {
        // placeholder - use gui to ask player if they want to pay 10% or the fixed tax amount
        if(false) {
            // calculate 10% of assets
            int tenPercent;
            tenPercent = 0;
            return tenPercent;
        }
        else return FIXEDTAX;
    }
}
