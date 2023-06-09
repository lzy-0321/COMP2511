package hotel;

import java.time.LocalDate;

import org.json.JSONObject;

public class PenthouseRoom extends Room {
    //private List<Booking> bookings = new ArrayList<Booking>();
    @Override
    public Booking book(LocalDate arrival, LocalDate departure) {
        return super.book(arrival, departure);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject room = super.toJSON();
        room.put("type", "penthouse");
        return room;
    }

    @Override
    public void printWelcomeMessage() {
        super.printWelcomeMessage();
    }

}
