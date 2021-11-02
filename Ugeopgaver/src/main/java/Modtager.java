public class Modtager {
    private String navn;
    private String mailadresse;
    private Inbox inbox;

    public Modtager(String navn, String mailadresse) {
        this.navn = navn;
        this.mailadresse = mailadresse;
        inbox = new Inbox();
    }

    public Inbox getInbox() {
        return inbox;
    }
}
