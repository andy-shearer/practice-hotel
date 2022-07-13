import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Booking system for DigiHotel
 */
public class BookingSystem {
    private final int numRooms;
    protected Map<Integer, List<Booking>> bookings = new HashMap<>(); // Hashmap of room number to booking list

    /**
     * Constructor for booking system
     * @param numRooms total number of rooms in the hotel
     */
    public BookingSystem(int numRooms) {
        this.numRooms = numRooms;
    }

    /**
     * Add a new booking into the system, allocating the first available room to the booking. Adding a booking is
     * synchronized to ensure multiple threads can't create overlapping bookings.
     * @param name of guest
     * @param arrive date of arrival
     * @param checkOut date of checkout
     * @return true if booking was successful, otherwise false
     */
    public boolean createBooking(String name, Date arrive, Date checkOut) {
        for(int i = 1; i <= numRooms; i++) {
            synchronized (this) {
                List<Booking> roomBookings = bookings.get(i);
                if (roomBookings == null || roomBookings.isEmpty()) {
                    // Room is free, so add the booking
                    List<Booking> newBooking = new ArrayList<>();
                    newBooking.add(new Booking(name, i, arrive, checkOut));
                    bookings.put(i, newBooking);
                    return true;
                } else if (hasNoClashes(roomBookings, arrive, checkOut)) {
                    // Existing bookings for this room don't clash with the new booking
                    roomBookings.add(new Booking(name, i, arrive, checkOut));
                    return true;
                }
            } // End synchronized block
        }

        return false;
    }

    /**
     * Iterate through all bookings in the system to check which rooms are available on the provided date.
     * @param date {@link Date} to search for room availability
     * @return {@link List} of room numbers which are available on the provided date
     */
    public List<Integer> getAvailableRooms(Date date) {
        List<Integer> avail = new ArrayList<>();

        for (int i = 1; i <= numRooms; i++) {
            List<Booking> roomBookings = bookings.get(i);
            if (hasNoClashes(roomBookings, date, date)) {
                avail.add(i);
            }
        }

        return avail;
    }

    /**
     * Lookup the bookings that have been created for a guest.
     * @param guestName to lookup
     * @return list of bookings
     */
    public List<Booking> bookingsForGuest(String guestName) {
        List<Booking> guestBookings = new ArrayList<>();

        for (List<Booking> roomBookings : bookings.values()) {
            List<Booking> matched = roomBookings.stream()
                    .filter(booking -> booking.getGuestName().matches(guestName))
                    .toList();
            guestBookings.addAll(matched);
        }

        return guestBookings;
    }

    /**
     * Helper function to iterate through the provided list of bookings checking whether any of them intersect
     * with the provided arrival/departure dates. The conditions which cause a 'clash':
     *  - Arrival date falls within the dates on existing booking
     *  - Checkout date falls within the dates on existing booking
     *
     * @param existingBookings {@link List} of existing bookings to search
     * @param arrive {@link Date} of arrival
     * @param checkOut {@link Date} of departure
     * @return true if the dates do not intersect with any booking in the provided list
     */
    private boolean hasNoClashes(List<Booking> existingBookings, Date arrive, Date checkOut) {
        if (existingBookings == null || existingBookings.isEmpty()) {
            return true;
        }

        for (Booking existingBooking : existingBookings) {
            Date existingArr = existingBooking.getArrival();
            Date existingCheckOut = existingBooking.getCheckOut();
            if (
                    (arrive.after(existingArr) && arrive.before(existingCheckOut)) ||
                    (checkOut.after(existingArr) && checkOut.before(existingCheckOut))
            ) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check how many rooms (total) are configured in the booking system
     * @return total number of rooms
     */
    public int getTotalRooms() {
        return numRooms;
    }
}
