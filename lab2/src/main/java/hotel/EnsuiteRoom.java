package hotel;

import java.time.LocalDate;

import org.json.JSONObject;

public class EnsuiteRoom extends Room {
    //private List<Booking> bookings = new ArrayList<Booking>();
    @Override
    public Booking book(LocalDate arrival, LocalDate departure) {
        return super.book(arrival, departure);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject room = super.toJSON();
        room.put("type", "ensuite");
        return room;
    }

    @Override
    public void printWelcomeMessage() {
        System.out
                .println("Welcome to your beautiful ensuite room which overlooks the Sydney harbour. Enjoy your stay");
    }

}
