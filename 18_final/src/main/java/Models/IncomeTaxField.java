package Models;

import Controllers.GameController;
import Controllers.ViewController_GUIMessages;

public class IncomeTaxField extends TaxField {
    double taxrate;

    public IncomeTaxField(String fieldName, int taxAmount, double taxrate) {
        super(fieldName, taxAmount);
        this.taxrate = taxrate;
    }

    @Override
    public int calculateTax(Player currentplayerobject) {
        int percentageTax = (int)(GameSettings.INCOME_TAX_RATE*100);
        // use gui to ask player if they want to pay 10% or the fixed tax amount
        boolean userInput;
        userInput = ViewController_GUIMessages.getInstance().showMessageAndGetBooleanUserInput(49,50,51,""+percentageTax,""+FIXEDTAX, -1);
        if(userInput) {
            int currentPlayer = GameController.getInstance().getCurrentPlayerNum();
            // calculate 10% of assets
            return (int) Math.round(taxrate * GameController.getInstance().calculateAssets(currentPlayer));
        }
        else return FIXEDTAX;
    }
}
