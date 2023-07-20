package ticketingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TicketingSystemTest {
    @Nested
    public class RegressionTests {
        @Test
        public void tickSystemRegressionTest() {
            TicketingSystem ts = TicketingSystem.getInstance(4);
            User user1 = new User(ts, 0, "fake@unsw.edu.au", "01234567", "000/0 Fake Street, Fake, Fake State");
            User user2 = new User(ts, 0, "fake2@unsw.edu.au", "01234567", "111/1 Fake Street, Fake, Fake State");
            User user3 = new User(ts, 0, "fake2@unsw.edu.au", "01234567", "111/1 Fake Street, Fake, Fake State");
            User user4 = new User(ts, 0, "fake2@unsw.edu.au", "01234567", "111/1 Fake Street, Fake, Fake State");
            User user5 = new User(ts, 0, "fake2@unsw.edu.au", "01234567", "111/1 Fake Street, Fake, Fake State");
            user1.setPreferences(false, false, true);
            user1.run();
            assertEquals(
                    "There is now 3 left!\n" + "Yay you bought a ticket!\n"
                            + "Sending your mail to 000/0 Fake Street, Fake, Fake State\n"
                            + "The total cost is: 50.5\n------------------------------------------",
                    user1.getMessage());
            user2.setPreferences(true, false, false);
            user2.run();
            assertEquals(
                    "There is now 2 left!\n" + "Yay you bought a ticket!\n" + "Sending an email to fake2@unsw.edu.au\n"
                            + "The total cost is: 50.0\n------------------------------------------",
                    user2.getMessage());
            user3.setPreferences(false, true, false);
            user3.run();
            assertEquals("There is now 1 left!\n" + "Yay you bought a ticket!\n"
                    + "Sending an sms to 01234567\nThe total cost is: 50.1\n------------------------------------------",
                    user3.getMessage());
            user4.setPreferences(true, true, true);
            user4.run();
            assertEquals("There is now 0 left!\n" + "Yay you bought a ticket!\n"
                    + "Sending an email to fake2@unsw.edu.au\n" + "Sending an sms to 01234567\n"
                    + "Sending your mail to 111/1 Fake Street, Fake, Fake State\nThe total cost is: 50.6\n"
                    + "------------------------------------------", user4.getMessage());
            user5.setPreferences(false, false, false);
            user5.run();
            assertEquals("Unfortunately there are no tickets left :(", user5.getMessage());
            ts.shutDown();
        }
    }

    @Nested
    public class SingletonTests {
        @Test
        public void testNoTwoTicketingSystem() {
            TicketingSystem ts = TicketingSystem.getInstance(5);
            TicketingSystem ts1 = TicketingSystem.getInstance(5);
            assertEquals(ts, ts1);
            User user1 = new User(ts, 0, "fake@unsw.edu.au", "01234567", "000/0 Fake Street, Fake, Fake State");
            User user2 = new User(ts, 1, "fake1@unsw.edu.au", "01234567", "111/1 Fake Street, Fake, Fake State");
            User user3 = new User(ts1, 2, "fake2@unsw.edu.au", "01234567", "111/2 Fake Street, Fake, Fake State");
            User user4 = new User(ts1, 2, "fake3@unsw.edu.au", "01234567", "111/3 Fake Street, Fake, Fake State");
            User user5 = new User(ts, 10, "fake4@unsw.edu.au", "01234567", "111/4 Fake Street, Fake, Fake State");
            User user6 = new User(ts, 5, "fake5@unsw.edu.au", "01234567", "111/5 Fake Street, Fake, Fake State");
            User user7 = new User(ts, 4, "fake6@unsw.edu.au", "01234567", "111/6 Fake Street, Fake, Fake State");

            user1.setPreferences(false, false, true);
            user1.run();
            assertEquals(
                    "There is now 4 left!\n" + "Yay you bought a ticket!\n"
                            + "Sending your mail to 000/0 Fake Street, Fake, Fake State\n"
                            + "The total cost is: 50.5\n------------------------------------------",
                    user1.getMessage());
            user2.setPreferences(true, false, false);
            user2.run();
            assertEquals(
                    "There is now 3 left!\n" + "Yay you bought a ticket!\n" + "Sending an email to fake1@unsw.edu.au\n"
                            + "The total cost is: 50.0\n------------------------------------------",
                    user2.getMessage());
            user3.setPreferences(false, true, false);
            user3.run();
            assertEquals("There is now 2 left!\n" + "Yay you bought a ticket!\n"
                    + "Sending an sms to 01234567\nThe total cost is: 50.1\n------------------------------------------",
                    user3.getMessage());
            user4.setPreferences(true, true, true);
            user4.run();
            assertEquals("There is now 1 left!\n" + "Yay you bought a ticket!\n"
                    + "Sending an email to fake3@unsw.edu.au\n" + "Sending an sms to 01234567\n"
                    + "Sending your mail to 111/3 Fake Street, Fake, Fake State\nThe total cost is: 50.6\n"
                    + "------------------------------------------", user4.getMessage());
            user5.setPreferences(false, false, true);
            user5.run();
            assertEquals("There is now 0 left!\n" + "Yay you bought a ticket!\n"
                    + "Sending your mail to 111/4 Fake Street, Fake, Fake State\nThe total cost is: 50.5\n"
                    + "------------------------------------------", user5.getMessage());

            user6.setPreferences(false, false, false);
            user6.run();
            assertEquals("Unfortunately there are no tickets left :(", user6.getMessage());

            user7.setPreferences(false, false, false);
            user7.run();
            assertEquals("Unfortunately there are no tickets left :(", user7.getMessage());

            ts.shutDown();
            ts1.shutDown();
        }
    }
}
