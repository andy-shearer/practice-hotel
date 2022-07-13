import java.util.InputMismatchException;
import java.util.Scanner;

public class Accessor {

    private static final int DEFAULT_ROOM_NUM = 20;
    private BookingSystem bookingSystem;

    public Accessor(int numRooms) {
        bookingSystem = new BookingSystem(numRooms);
        printMenuOpts();
    }

    private void printMenuOpts() {
        System.out.println("\n==================");
        System.out.println("Please select an option:");
        System.out.println("\t1) Book a room");
        System.out.println("\t2) Check availability");
        System.out.println("\t3) Retrieve bookings");
        System.out.println("\t4) Exit");
        System.out.println("\n> Enter option number: ");

        Scanner selection = new Scanner(System.in);
        try {
            int userInput = selection.nextInt();
            menuSelection(userInput);
        } catch (InputMismatchException e) {
            System.out.println("\nInvalid menu selection.");
            printMenuOpts();
        }
    }

    private void menuSelection(int option) {
        switch (option) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                System.exit(0);
            default:
                System.out.println("\nInvalid menu selection.");
                printMenuOpts();
                break;
        }
    }

    public static void main(String[] args) {
        int rooms = DEFAULT_ROOM_NUM;

        if (args.length > 0) {
            try {
                rooms = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                // Do nothing - we'll just fall back to the default number of rooms
            }
        }

        new Accessor(rooms);
    }

}