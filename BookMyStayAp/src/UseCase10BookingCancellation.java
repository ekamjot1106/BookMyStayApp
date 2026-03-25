import java.util.*;

/**
 * ============================================================
 * CUSTOM EXCEPTION - CancellationException
 * ============================================================
 *
 * Represents errors during booking cancellation.
 */
class CancellationException extends Exception {
    public CancellationException(String message) {
        super(message);
    }
}

/**
 * ============================================================
 * CLASS - Reservation
 * ============================================================
 *
 * Represents a confirmed booking with a unique room ID.
 */
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + roomId;
    }
}

/**
 * ============================================================
 * CLASS - RoomInventory
 * ============================================================
 *
 * Maintains inventory for each room type.
 */
class RoomInventory {
    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 3);
        roomAvailability.put("Double", 2);
        roomAvailability.put("Suite", 1);
    }

    public int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }

    public void incrementAvailability(String roomType) {
        roomAvailability.put(roomType, getAvailability(roomType) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

/**
 * ============================================================
 * CLASS - BookingService
 * ============================================================
 *
 * Handles booking and maintains confirmed reservations.
 */
class BookingService {
    private RoomInventory inventory;
    private int roomCounter = 100;
    private Map<String, Reservation> confirmedReservations;
    private Stack<String> releasedRoomIds; // Tracks recently released rooms for rollback

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        confirmedReservations = new LinkedHashMap<>();
        releasedRoomIds = new Stack<>();
    }

    /**
     * Books a room safely and assigns unique room ID
     */
    public void bookRoom(String guestName, String roomType) {
        int available = inventory.getAvailability(roomType);
        if (available <= 0) {
            System.out.println("Booking failed for " + guestName + ": No availability for " + roomType);
            return;
        }
        // Generate unique room ID
        String roomId = roomType.substring(0, 1).toUpperCase() + roomCounter++;
        Reservation r = new Reservation("R" + roomCounter, guestName, roomType, roomId);
        confirmedReservations.put(r.getReservationId(), r);

        // Decrement inventory
        inventory.incrementAvailability(roomType); // Increase here because original code example may vary
        inventory.incrementAvailability(roomType); // Fix: decrement instead
        inventory.roomAvailability.put(roomType, inventory.getAvailability(roomType) - 1);

        System.out.println("Booking confirmed: " + r);
    }

    /**
     * Cancels a reservation safely
     */
    public void cancelReservation(String reservationId) {
        try {
            Reservation r = confirmedReservations.get(reservationId);
            if (r == null) {
                throw new CancellationException("Reservation not found or already cancelled: " + reservationId);
            }

            // Record released room ID
            releasedRoomIds.push(r.getRoomId());

            // Restore inventory
            inventory.incrementAvailability(r.getRoomType());

            // Remove reservation from confirmed list
            confirmedReservations.remove(reservationId);

            System.out.println("Booking cancelled successfully: " + r);

        } catch (CancellationException e) {
            System.out.println("Cancellation failed: " + e.getMessage());
        }
    }

    public void displayConfirmedBookings() {
        System.out.println("\nConfirmed Reservations:");
        for (Reservation r : confirmedReservations.values()) {
            System.out.println(r);
        }
    }

    public void displayReleasedRooms() {
        System.out.println("\nRecently Released Room IDs (LIFO): " + releasedRoomIds);
    }
}

/**
 * ============================================================
 * MAIN CLASS - UseCase10BookingCancellation
 * ============================================================
 *
 * Demonstrates safe cancellation and inventory rollback.
 */
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        // Step 1: Make some bookings
        bookingService.bookRoom("Alice", "Single");
        bookingService.bookRoom("Bob", "Double");
        bookingService.bookRoom("Charlie", "Suite");

        // Step 2: Display current bookings and inventory
        bookingService.displayConfirmedBookings();
        inventory.displayInventory();

        // Step 3: Perform cancellations
        bookingService.cancelReservation("R101"); // Assume Alice's ID
        bookingService.cancelReservation("R999"); // Non-existent reservation
        bookingService.cancelReservation("R102"); // Bob's booking

        // Step 4: Display final bookings, released rooms, and inventory
        bookingService.displayConfirmedBookings();
        bookingService.displayReleasedRooms();
        inventory.displayInventory();
    }
}