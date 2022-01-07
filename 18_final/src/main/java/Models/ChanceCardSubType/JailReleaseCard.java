package Models.ChanceCardSubType;

import Models.ChanceCard;
import Models.Player;

public class JailReleaseCard extends ChanceCard {
    public JailReleaseCard(String text) {
        super(text);
    }

    @Override
    public void effect(Player currentplayerobject) {
        currentplayerobject.giveReleaseFromJailCard(this);
    }
}
