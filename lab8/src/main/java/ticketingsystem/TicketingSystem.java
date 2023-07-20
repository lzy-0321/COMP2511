package ticketingsystem;

import java.util.List;

import ticketingsystem.receipts.Receipt;
import ticketingsystem.receipts.ReceiptStrategy;

public class TicketingSystem {
    private int ticketsLeft;

    private static TicketingSystem instance = null;

    public TicketingSystem(int numTickets) {
        this.ticketsLeft = numTickets;
    }

    public static synchronized TicketingSystem getInstance(int numTickets) {
        if (instance == null) {
            instance = new TicketingSystem(numTickets);
        }
        return instance;
    }

    public void shutDown() {
        instance = null;
    }

    public int getTicketsLeft() {
        return ticketsLeft;
    }

    private void setTicketsLeft(int ticketsLeft) {
        this.ticketsLeft = ticketsLeft;
    }

    public synchronized String buyTicket(List<ReceiptStrategy> receipts) {
        String msg = "";
        if (getTicketsLeft() > 0) {
            setTicketsLeft(getTicketsLeft() - 1);
            msg += "There is now " + getTicketsLeft() + " left!" + "\n";
            // add at the front of the list
            receipts.add(0, new Receipt());
            double cost = 0;
            for (ReceiptStrategy receipt : receipts) {
                cost += receipt.getCost();
                msg += receipt.send();
            }

            msg += "The total cost is: " + cost + "\n";
            msg += "------------------------------------------";
        } else {
            msg += "Unfortunately there are no tickets left :(";
        }
        System.out.println(msg);
        return msg;
    }

}
