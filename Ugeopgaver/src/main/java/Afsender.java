public class Afsender {
    private String navn;
    private String mailadresse;
    private Outbox outbox;

    public Afsender(String navn, String mailadresse) {
        this.navn = navn;
        this.mailadresse = mailadresse;
        outbox = new Outbox();
    }

    public Outbox getOutbox() {
        return outbox;
    }

    public void setName(String name) {
        this.navn = name;
    }

    public void newMessage() {
        Besked message = new Besked();
    }
}
