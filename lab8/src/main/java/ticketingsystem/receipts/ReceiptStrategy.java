package ticketingsystem.receipts;

public interface ReceiptStrategy {
    double getCost();

    String send();
}
