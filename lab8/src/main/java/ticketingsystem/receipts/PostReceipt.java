package ticketingsystem.receipts;

public class PostReceipt implements ReceiptStrategy {
    private String address;
    private static final double FEE = 0.5;

    public PostReceipt(String address) {
        this.address = address;
    }

    @Override
    public String send() {
        String msg = "Sending your mail to " + address + "\n";
        return msg;
    }

    @Override
    public double getCost() {
        return FEE;
    }

}
