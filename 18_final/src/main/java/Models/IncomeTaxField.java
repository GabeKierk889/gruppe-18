package Models;

import Controllers.GameController;

public class IncomeTaxField extends TaxField {
    double taxrate;

    public IncomeTaxField(String fieldName, int taxAmount, double taxrate) {
        super(fieldName, taxAmount);
        this.taxrate = taxrate;
    }

    @Override
    public int calculateTax(Player currentplayerobject) {
        // TODO: gui that asks the player and takes user input
        // placeholder - use gui to ask player if they want to pay 10% or the fixed tax amount
        if(false) {
            int currentPlayer = GameController.getInstance().getCurrentPlayerNum();
            // calculate 10% of assets
            return (int) Math.round(taxrate * GameController.getInstance().calculateAssets(currentPlayer));
        }
        else return FIXEDTAX;
    }
}
