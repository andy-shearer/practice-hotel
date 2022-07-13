import org.junit.Before;
import org.junit.Test;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BookingSystemTest {

    private BookingSystem bookingSystem;
    private static final Date DEFAULT_ARRIVAL = new Date();
    private static final Date DEFAULT_CHECKOUT = Date.from(DEFAULT_ARRIVAL.toInstant().plus(3, ChronoUnit.DAYS));
    private static final String DEFAULT_NAME = "Andy Shearer";
    private static final int DEFAULT_NUM_ROOMS = 25;


    @Before
    public void setUp() {
        bookingSystem = new BookingSystem(DEFAULT_NUM_ROOMS);
    }

    @Test
    public void testDefaultRoomNumber() {
        assertEquals("Expected 25 rooms in the booking system", 25, bookingSystem.getTotalRooms());
    }

    @Test
    public void testSetRoomNumber() {
        bookingSystem = new BookingSystem(50);
        assertEquals("Expected 50 rooms in the booking system", 50, bookingSystem.getTotalRooms());
    }

    @Test
    public void testCreateBooking() {
        boolean booked = bookingSystem.createBooking(DEFAULT_NAME, DEFAULT_ARRIVAL, DEFAULT_CHECKOUT);
        assertTrue("Expected booking to be successful", booked);
        assertEquals("Expected total bookings to be 1", 1, bookingSystem.bookings.size());
    }

    @Test
    public void testFindSingleGuestBooking() {
        // Set up a booking in the system for us to lookup
        boolean booked = bookingSystem.createBooking("Andy Shearer", DEFAULT_ARRIVAL, DEFAULT_CHECKOUT);
        assertTrue("Expected booking to be successful", booked);

        List<Booking> roomBookings = bookingSystem.bookingsForGuest(DEFAULT_NAME);
        assertNotNull("Expected bookings to be found", roomBookings);
        assertEquals("Expected 1 booking to be returned", 1, roomBookings.size());

        Booking guestBooking = roomBookings.get(0);
        assertEquals("Expected booking name to match provided info", DEFAULT_NAME, guestBooking.getGuestName());
        assertEquals("Expected booking arrival to match provided info", DEFAULT_ARRIVAL, guestBooking.getArrival());
        assertEquals("Expected booking checkout to match provided info", DEFAULT_CHECKOUT, guestBooking.getCheckOut());
    }

    @Test
    public void testFindMultipleGuestBookings() {
        Date secondArrival = Date.from(DEFAULT_ARRIVAL.toInstant().plus(7, ChronoUnit.DAYS));
        Date secondCheckout = Date.from(secondArrival.toInstant().plus(3, ChronoUnit.DAYS));

        // Set up a booking in the system for us to lookup
        boolean booked = bookingSystem.createBooking("Andy Shearer", DEFAULT_ARRIVAL, DEFAULT_CHECKOUT);
        booked = booked && bookingSystem.createBooking("Andy Shearer", secondArrival, secondCheckout);
        assertTrue("Expected bookings to be successful", booked);

        List<Booking> roomBookings = bookingSystem.bookingsForGuest(DEFAULT_NAME);
        assertNotNull("Expected bookings to be found", roomBookings);
        assertEquals("Expected 2 booking to be returned", 2, roomBookings.size());

        Booking guestBooking = roomBookings.get(0);
        assertEquals("Expected booking name to match provided info", DEFAULT_NAME, guestBooking.getGuestName());
        assertEquals("Expected booking arrival to match provided info", DEFAULT_ARRIVAL, guestBooking.getArrival());
        assertEquals("Expected booking checkout to match provided info", DEFAULT_CHECKOUT, guestBooking.getCheckOut());

        guestBooking = roomBookings.get(1);
        assertEquals("Expected booking name to match provided info", DEFAULT_NAME, guestBooking.getGuestName());
        assertEquals("Expected booking arrival to match provided info", secondArrival, guestBooking.getArrival());
        assertEquals("Expected booking checkout to match provided info", secondCheckout, guestBooking.getCheckOut());
    }
}
