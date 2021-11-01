public class Player {
    private String name;
    private Account account;

    public Player(String name){
        Account acct = new Account();
        account = acct;
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public Account getAccount() {
        return account;
    }

}
