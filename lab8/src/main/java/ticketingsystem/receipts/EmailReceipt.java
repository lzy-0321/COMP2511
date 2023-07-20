package ticketingsystem.receipts;

public class EmailReceipt implements ReceiptStrategy {
    private String email;
    private static final double FEE = 0;

    public EmailReceipt(String email) {
        this.email = email;
    }

    @Override
    public String send() {
        String msg = "Sending an email to " + email + "\n";
        return msg;
    }

    @Override
    public double getCost() {
        return FEE;
    }
}
