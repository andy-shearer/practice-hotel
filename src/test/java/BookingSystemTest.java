import org.junit.Before;
import org.junit.Test;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BookingSystemTest {
    private BookingSystem bookingSystem;
    private static final int DEFAULT_NUM_ROOMS = 25;

    // Data for creating test bookings
    private static final String DEFAULT_NAME = "Andy Shearer";
    private static final Date DEFAULT_ARRIVAL = new Date();
    private static final Date DEFAULT_CHECKOUT = Date.from(DEFAULT_ARRIVAL.toInstant().plus(3, ChronoUnit.DAYS));
    private static final String SECOND_NAME = "John Doe";
    private static final Date SECOND_ARRIVAL = Date.from(DEFAULT_ARRIVAL.toInstant().plus(7, ChronoUnit.DAYS));
    private static final Date SECOND_CHECKOUT = Date.from(SECOND_ARRIVAL.toInstant().plus(3, ChronoUnit.DAYS));


    @Before
    public void setUp() {
        bookingSystem = new BookingSystem(DEFAULT_NUM_ROOMS);
    }

    @Test
    public void testDefaultRoomNumber() {
        assertEquals("Expected 25 rooms in the booking system", 25, bookingSystem.getTotalRooms());
    }

    @Test
    public void testCustomNumberOfRooms() {
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
    public void testFindSingleBooking() {
        // Set up a booking in the system for us to lookup
        boolean booked = bookingSystem.createBooking(DEFAULT_NAME, DEFAULT_ARRIVAL, DEFAULT_CHECKOUT);
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
    public void testFindMultipleBookingsForGuest() {
        // Set up 2 bookings with the same name, for us to lookup
        boolean booked = bookingSystem.createBooking(DEFAULT_NAME, DEFAULT_ARRIVAL, DEFAULT_CHECKOUT);
        booked = booked && bookingSystem.createBooking(DEFAULT_NAME, SECOND_ARRIVAL, SECOND_CHECKOUT);
        assertTrue("Expected bookings to be successful", booked);

        List<Booking> roomBookings = bookingSystem.bookingsForGuest(DEFAULT_NAME);
        assertNotNull("Expected bookings to be found", roomBookings);
        assertEquals("Expected 2 bookings to be returned", 2, roomBookings.size());

        Booking guestBooking = roomBookings.get(0);
        assertEquals("Expected booking name to match provided info", DEFAULT_NAME, guestBooking.getGuestName());
        assertEquals("Expected booking arrival to match provided info", DEFAULT_ARRIVAL, guestBooking.getArrival());
        assertEquals("Expected booking checkout to match provided info", DEFAULT_CHECKOUT, guestBooking.getCheckOut());

        guestBooking = roomBookings.get(1);
        assertEquals("Expected booking name to match provided info", DEFAULT_NAME, guestBooking.getGuestName());
        assertEquals("Expected booking arrival to match provided info", SECOND_ARRIVAL, guestBooking.getArrival());
        assertEquals("Expected booking checkout to match provided info", SECOND_CHECKOUT, guestBooking.getCheckOut());
    }

    @Test
    public void testFindBookingAmongstMultipleGuests() {
        // Set up 2 bookings for two different guests, for us to lookup
        boolean booked = bookingSystem.createBooking(DEFAULT_NAME, DEFAULT_ARRIVAL, DEFAULT_CHECKOUT);
        booked = booked && bookingSystem.createBooking(SECOND_NAME, SECOND_ARRIVAL, SECOND_CHECKOUT);
        assertTrue("Expected bookings to be successful", booked);

        List<Booking> roomBookings = bookingSystem.bookingsForGuest(DEFAULT_NAME);
        assertNotNull("Expected bookings to be found", roomBookings);
        assertEquals("Expected 1 booking to be returned", 1, roomBookings.size());

        Booking guestBooking = roomBookings.get(0);
        assertEquals("Expected booking name to match provided info", DEFAULT_NAME, guestBooking.getGuestName());
        assertEquals("Expected booking arrival to match provided info", DEFAULT_ARRIVAL, guestBooking.getArrival());
        assertEquals("Expected booking checkout to match provided info", DEFAULT_CHECKOUT, guestBooking.getCheckOut());
    }

    @Test
    public void testGetAvailableRoomsWithEmptySystem() {
        Date availabilityDate = Date.from(DEFAULT_ARRIVAL.toInstant().plus(1, ChronoUnit.DAYS));
        List<Integer> availableRooms = bookingSystem.getAvailableRooms(availabilityDate);

        assertEquals("Expected all rooms to be available", DEFAULT_NUM_ROOMS, availableRooms.size());
    }

    @Test
    public void testGetAvailableRooms() {
        // Set up a booking in the system
        boolean booked = bookingSystem.createBooking(DEFAULT_NAME, DEFAULT_ARRIVAL, DEFAULT_CHECKOUT);
        assertTrue("Expected booking to be successful", booked);

        Date availabilityDate = Date.from(DEFAULT_ARRIVAL.toInstant().plus(1, ChronoUnit.DAYS));

        List<Integer> availableRooms = bookingSystem.getAvailableRooms(availabilityDate);
        assertEquals("Expected all rooms except 1 to be available", DEFAULT_NUM_ROOMS - 1, availableRooms.size());
        assertFalse("Expected room number 1 to be unavailable", availableRooms.contains(1));
    }

    @Test
    public void testBookingOverlap() {
        bookingSystem = new BookingSystem(1);
        // Set up a booking in the system
        boolean booked = bookingSystem.createBooking(DEFAULT_NAME, DEFAULT_ARRIVAL, DEFAULT_CHECKOUT);
        assertTrue("Expected booking to be successful", booked);

        Date overlappingArrival = Date.from(DEFAULT_ARRIVAL.toInstant().plus(1, ChronoUnit.DAYS));
        Date overlappingCheckout = Date.from(overlappingArrival.toInstant().plus(6, ChronoUnit.DAYS));

        assertFalse("Expected booking creation to fail", bookingSystem.createBooking(SECOND_NAME, overlappingArrival, overlappingCheckout));
    }
}
