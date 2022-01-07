package Models.ChanceCardSubType;

import Models.ChanceCard;
import Models.Player;

//This code is from previous assignment CDIO 3 by Maj Kyllesbech, Gabriel H, Kierkegaard, Mark Bidstrup & Xiao Chen handed in 26. November 2021

public class JailReleaseCard extends ChanceCard {
    public JailReleaseCard(String text) {
        super(text);
    }

    @Override
    public void effect(Player currentplayerobject) {
        currentplayerobject.giveReleaseFromJailCard(this);
    }
}
