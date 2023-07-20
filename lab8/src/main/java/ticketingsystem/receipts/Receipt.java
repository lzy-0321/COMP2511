package ticketingsystem.receipts;

public class Receipt implements ReceiptStrategy {
    private static final double TICKET_COST = 50;

    @Override
    public String send() {
        String msg = "Yay you bought a ticket!" + "\n";
        return msg;
    }

    @Override
    public double getCost() {
        return TICKET_COST;
    }

}
