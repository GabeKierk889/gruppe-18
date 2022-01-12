package Models;

import Controllers.ViewController;

public abstract class TaxField extends Field {
    protected final int FIXEDTAX;

    public TaxField(String fieldName, int taxAmount) {
        super(fieldName);
        FIXEDTAX = taxAmount;
    }

    @Override
    public void landOnField(Player currentplayerobject) {
        payTax(currentplayerobject);
    }

    public abstract int calculateTax (Player currentplayerobject);

    private void payTax(Player currentplayerobject) {
        int taxToPay = calculateTax(currentplayerobject);
        ViewController.getInstance().showTakeTurnMessageWithPlayerName(12,""+taxToPay,"","");
        currentplayerobject.getAccount().withdrawMoney(taxToPay);
        ViewController.getInstance().updateGUIBalance();
    }

}
