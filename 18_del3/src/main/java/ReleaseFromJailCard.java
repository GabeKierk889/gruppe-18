public class ReleaseFromJailCard extends ChanceCard {

    // Du løslades uden omkostninger. Behold dette kort, indtil du får brug for det.

    public ReleaseFromJailCard() {
    }

    @Override
    public String chanceCardText() {
        return "With this card, you can be released from jail for free. " +
                "You can keep this card until you need to use it. You can only use it once.";
    }

    @Override
    public void effect(Player currentplayerobject) {
        currentplayerobject.giveReleaseFromJailCard(this);
    }
}
