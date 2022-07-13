import java.util.Date;

public class Booking {
    private final String guestName;
    private final int roomNo;
    private final Date arrive;
    private final Date checkOut;

    public Booking(String name, int roomNo, Date arrive, Date depart) {
        this.guestName = name;
        this.roomNo = roomNo;
        this.arrive = arrive;
        this.checkOut = depart;
    }

    public String getGuestName() {
        return guestName;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public Date getArrival() {
        return arrive;
    }

    public Date getCheckOut() {
        return checkOut;
    }
}