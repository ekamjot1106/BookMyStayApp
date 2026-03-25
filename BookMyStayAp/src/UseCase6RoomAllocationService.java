import java.util.*;

/**
 * ============================================================
 * CLASS - Reservation
 * ============================================================
 *
 * Represents a guest booking request.
 */
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "Guest: " + guestName + ", Room Type: " + roomType;
    }
}


/**
 * ============================================================
 * CLASS - RoomInventory
 * ============================================================
 *
 * Centralized inventory with read/write operations.
 */
class RoomInventory {
    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
        roomAvailability.put("Single", 3);
        roomAvailability.put("Double", 2);
        roomAvailability.put("Suite", 1);
    }

    public int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }

    public boolean decrementAvailability(String roomType) {
        int available = roomAvailability.getOrDefault(roomType, 0);
        if (available > 0) {
            roomAvailability.put(roomType, available - 1);
            return true;
        } else {
            return false;
        }
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
 * CLASS - BookingRequestQueue
 * ============================================================
 *
 * Stores pending reservations (FIFO queue).
 */
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Request added: " + reservation);
    }

    public Reservation pollRequest() {
        return requestQueue.poll();
    }

    public boolean isEmpty() {
        return requestQueue.isEmpty();
    }
}


/**
 * ============================================================
 * CLASS - RoomAllocationService
 * ============================================================
 *
 * Confirms reservations, assigns unique room IDs, and updates inventory.
 */
class RoomAllocationService {

    private RoomInventory inventory;
    private BookingRequestQueue requestQueue;

    // Map of roomType -> Set of allocated room IDs
    private Map<String, Set<String>> allocatedRooms;

    // Room ID generator counter
    private int roomIdCounter = 100;

    public RoomAllocationService(RoomInventory inventory, BookingRequestQueue requestQueue) {
        this.inventory = inventory;
        this.requestQueue = requestQueue;
        allocatedRooms = new HashMap<>();
    }

    /**
     * Processes queued requests one by one (FIFO)
     */
    public void processBookings() {
        while (!requestQueue.isEmpty()) {
            Reservation reservation = requestQueue.pollRequest();
            String type = reservation.getRoomType();

            if (inventory.getAvailability(type) > 0) {
                // Allocate room
                String roomId = generateUniqueRoomId(type);
                allocatedRooms.computeIfAbsent(type, k -> new HashSet<>()).add(roomId);

                // Update inventory immediately
                inventory.decrementAvailability(type);

                // Confirm reservation
                System.out.println("Reservation confirmed for " + reservation.getGuestName() +
                        " | Room Type: " + type + " | Room ID: " + roomId);
            } else {
                System.out.println("Reservation failed for " + reservation.getGuestName() +
                        " | Room Type: " + type + " | Reason: No availability");
            }
        }
    }

    /**
     * Generates a unique room ID per room type
     */
    private String generateUniqueRoomId(String roomType) {
        return roomType.substring(0, 1).toUpperCase() + roomIdCounter++;
    }

    /**
     * Display all allocated rooms
     */
    public void displayAllocatedRooms() {
        System.out.println("\nAllocated Rooms:");
        for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}


/**
 * ============================================================
 * MAIN CLASS - UseCase6RoomAllocationService
 * ============================================================
 *
 * Demonstrates full reservation confirmation and allocation.
 */
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        // Step 1: Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Step 2: Initialize booking request queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Step 3: Simulate guest booking requests
        requestQueue.addRequest(new Reservation("Alice", "Single"));
        requestQueue.addRequest(new Reservation("Bob", "Suite"));
        requestQueue.addRequest(new Reservation("Charlie", "Single"));
        requestQueue.addRequest(new Reservation("Diana", "Double"));
        requestQueue.addRequest(new Reservation("Eve", "Single"));   // May fail if Single exhausted
        requestQueue.addRequest(new Reservation("Frank", "Suite"));  // May fail if Suite exhausted

        // Step 4: Process booking requests and allocate rooms
        RoomAllocationService allocationService = new RoomAllocationService(inventory, requestQueue);
        allocationService.processBookings();

        // Step 5: Display final inventory
        inventory.displayInventory();

        // Step 6: Display allocated rooms
        allocationService.displayAllocatedRooms();
    }
}