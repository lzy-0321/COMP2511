package ticketingsystem;

import java.util.ArrayList;
import java.util.List;

import ticketingsystem.receipts.EmailReceipt;
import ticketingsystem.receipts.PostReceipt;
import ticketingsystem.receipts.SMSReceipt;
import ticketingsystem.receipts.ReceiptStrategy;

public class User extends Thread {
    private TicketingSystem ticketSystem;
    private int timeTakenToBuyTicket;
    private String email;
    private String phoneNumber;
    private String address;
    private Boolean emailReceipt;
    private Boolean smsReceipt;
    private Boolean postReceipt;

    private String message;

    public User(TicketingSystem ticketSystem, int timeTakenToBuyTicket, String email, String phoneNumber,
            String address) {
        this.ticketSystem = ticketSystem;
        this.timeTakenToBuyTicket = timeTakenToBuyTicket;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public void setPreferences(Boolean emailReceipt, Boolean smsReceipt, Boolean postReceipt) {
        this.emailReceipt = emailReceipt;
        this.smsReceipt = smsReceipt;
        this.postReceipt = postReceipt;

    }

    public void run() {
        try {
            Thread.sleep(timeTakenToBuyTicket);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        List<ReceiptStrategy> receipts = new ArrayList<>();
        if (emailReceipt) {
            receipts.add(new EmailReceipt(email));
        }
        if (smsReceipt) {
            receipts.add(new SMSReceipt(phoneNumber));
        }
        if (postReceipt) {
            receipts.add(new PostReceipt(address));
        }

        message = ticketSystem.buyTicket(receipts);
    }

    public String getMessage() {
        return this.message;
    }
}
