import java.util.*;

/**
 * ============================================================
 * CUSTOM EXCEPTION - InvalidBookingException
 * ============================================================
 *
 * Represents validation errors for booking requests.
 */
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}


/**
 * ============================================================
 * CLASS - RoomInventory
 * ============================================================
 *
 * Maintains inventory and validates room types and availability.
 */
class RoomInventory {
    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    /**
     * Checks if room type is valid
     */
    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!roomAvailability.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    /**
     * Checks if requested quantity can be allocated
     */
    public void validateAvailability(String roomType) throws InvalidBookingException {
        int available = roomAvailability.getOrDefault(roomType, 0);
        if (available <= 0) {
            throw new InvalidBookingException("No availability for room type: " + roomType);
        }
    }

    /**
     * Allocate room safely
     */
    public void allocateRoom(String roomType) throws InvalidBookingException {
        validateRoomType(roomType);
        validateAvailability(roomType);
        int available = roomAvailability.get(roomType);
        roomAvailability.put(roomType, available - 1);
    }

    /**
     * Display current inventory
     */
    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}


/**
 * ============================================================
 * CLASS - Reservation
 * ============================================================
 *
 * Represents a confirmed reservation.
 */
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType;
    }
}


/**
 * ============================================================
 * CLASS - BookingService
 * ============================================================
 *
 * Handles booking requests with validation and error handling.
 */
class BookingService {
    private RoomInventory inventory;
    private int reservationCounter = 100;
    private List<Reservation> confirmedBookings;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.confirmedBookings = new ArrayList<>();
    }

    /**
     * Attempts to book a room with fail-fast validation
     */
    public void bookRoom(String guestName, String roomType) {
        try {
            // Validate room type and availability
            inventory.validateRoomType(roomType);
            inventory.validateAvailability(roomType);

            // Allocate room
            inventory.allocateRoom(roomType);

            // Create reservation
            String reservationId = "R" + reservationCounter++;
            Reservation r = new Reservation(reservationId, guestName, roomType);
            confirmedBookings.add(r);

            System.out.println("Booking confirmed: " + r);

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking failed for guest '" + guestName + "': " + e.getMessage());
        }
    }

    public void displayConfirmedBookings() {
        System.out.println("\nConfirmed Bookings:");
        for (Reservation r : confirmedBookings) {
            System.out.println(r);
        }
    }
}


/**
 * ============================================================
 * MAIN CLASS - UseCase9ErrorHandlingValidation
 * ============================================================
 *
 * Demonstrates validation, custom exceptions, and fail-fast booking.
 */
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        // Test cases with valid and invalid inputs
        bookingService.bookRoom("Alice", "Single");    // Valid
        bookingService.bookRoom("Bob", "Suite");       // Valid
        bookingService.bookRoom("Charlie", "Penthouse"); // Invalid room type
        bookingService.bookRoom("Diana", "Double");    // Valid
        bookingService.bookRoom("Eve", "Suite");       // Valid
        bookingService.bookRoom("Frank", "Suite");     // No availability (fail)

        // Display final confirmed bookings and inventory
        bookingService.displayConfirmedBookings();
        inventory.displayInventory();
    }
}