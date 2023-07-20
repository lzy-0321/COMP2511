package ticketingsystem.receipts;

public class SMSReceipt implements ReceiptStrategy {
    private String phoneNumber;
    private static final double FEE = 0.1;

    public SMSReceipt(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String send() {
        String msg = "Sending an sms to " + phoneNumber + "\n";
        return msg;
    }

    @Override
    public double getCost() {
        return FEE;
    }

}
