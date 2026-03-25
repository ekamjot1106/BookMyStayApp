import java.util.*;
import java.util.concurrent.*;

/**
 * ============================================================
 * CLASS - RoomInventory
 * ============================================================
 *
 * Thread-safe inventory management.
 */
class RoomInventory {
    private final Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    /**
     * Thread-safe room allocation
     */
    public synchronized boolean allocateRoom(String roomType) {
        int available = roomAvailability.getOrDefault(roomType, 0);
        if (available > 0) {
            roomAvailability.put(roomType, available - 1);
            return true;
        } else {
            return false;
        }
    }

    public synchronized void incrementRoom(String roomType) {
        roomAvailability.put(roomType, roomAvailability.getOrDefault(roomType, 0) + 1);
    }

    public synchronized void displayInventory() {
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
 */
class Reservation {
    private static int counter = 100;
    private final String reservationId;
    private final String guestName;
    private final String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.reservationId = "R" + counter++;
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
 * Handles bookings concurrently using synchronized blocks.
 */
class BookingService {
    private final RoomInventory inventory;
    private final List<Reservation> confirmedBookings;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.confirmedBookings = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Thread-safe booking attempt
     */
    public void bookRoom(String guestName, String roomType) {
        synchronized (inventory) { // critical section for inventory check + allocation
            boolean allocated = inventory.allocateRoom(roomType);
            if (allocated) {
                Reservation r = new Reservation(guestName, roomType);
                confirmedBookings.add(r);
                System.out.println("Booking confirmed: " + r);
            } else {
                System.out.println("Booking failed for " + guestName + ": No availability for " + roomType);
            }
        }
    }

    public void displayConfirmedBookings() {
        System.out.println("\nConfirmed Bookings:");
        synchronized (confirmedBookings) {
            for (Reservation r : confirmedBookings) {
                System.out.println(r);
            }
        }
    }
}

/**
 * ============================================================
 * MAIN CLASS - UseCase11ConcurrentBookingSimulation
 * ============================================================
 *
 * Demonstrates concurrent booking with thread safety.
 */
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) throws InterruptedException {

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        // Simulate multiple guests booking concurrently
        Runnable guest1 = () -> bookingService.bookRoom("Alice", "Single");
        Runnable guest2 = () -> bookingService.bookRoom("Bob", "Suite");
        Runnable guest3 = () -> bookingService.bookRoom("Charlie", "Single");
        Runnable guest4 = () -> bookingService.bookRoom("Diana", "Double");
        Runnable guest5 = () -> bookingService.bookRoom("Eve", "Single");
        Runnable guest6 = () -> bookingService.bookRoom("Frank", "Suite");

        // Use thread pool for concurrency
        ExecutorService executor = Executors.newFixedThreadPool(6);
        executor.submit(guest1);
        executor.submit(guest2);
        executor.submit(guest3);
        executor.submit(guest4);
        executor.submit(guest5);
        executor.submit(guest6);

        // Shutdown executor and wait for completion
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Display final bookings and inventory
        bookingService.displayConfirmedBookings();
        inventory.displayInventory();
    }
}