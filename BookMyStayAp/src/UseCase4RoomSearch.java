import java.util.*;

/**
 * ============================================================
 * CLASS - Room (Domain Model)
 * ============================================================
 *
 * Represents a hotel room with pricing and features.
 * This avoids duplicating room data in inventory.
 */
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }
}


/**
 * ============================================================
 * CLASS - RoomInventory (Centralized State Holder)
 * ============================================================
 *
 * Maintains room availability (read-only for search).
 */
class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
        roomAvailability.put("Single", 10);
        roomAvailability.put("Double", 0);   // Unavailable (for filtering demo)
        roomAvailability.put("Suite", 5);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }
}


/**
 * ============================================================
 * CLASS - RoomSearchService
 * ============================================================
 *
 * Handles read-only search operations.
 * Does NOT modify inventory (important!).
 */
class RoomSearchService {

    private RoomInventory inventory;
    private Map<String, Room> roomCatalog;

    public RoomSearchService(RoomInventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    /**
     * Displays only available rooms (availability > 0)
     */
    public void searchAvailableRooms() {
        Map<String, Integer> availability = inventory.getRoomAvailability();

        System.out.println("Available Rooms:\n");

        for (String roomType : availability.keySet()) {

            int count = availability.get(roomType);

            // Validation: only show rooms with availability > 0
            if (count > 0 && roomCatalog.containsKey(roomType)) {

                Room room = roomCatalog.get(roomType);

                System.out.println("Room Type: " + room.getType());
                System.out.println("Price: $" + room.getPrice());
                System.out.println("Available Count: " + count);
                System.out.println("Amenities: " + room.getAmenities());
                System.out.println("----------------------------------");
            }
        }
    }
}


/**
 * ============================================================
 * MAIN CLASS - UseCase4RoomSearch
 * ============================================================
 *
 * Demonstrates read-only search functionality.
 */
public class UseCase4RoomSearch {

    public static void main(String[] args) {

        // Step 1: Initialize inventory (state holder)
        RoomInventory inventory = new RoomInventory();

        // Step 2: Create room catalog (domain model)
        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Single",
                new Room("Single", 100.0, Arrays.asList("WiFi", "TV")));

        roomCatalog.put("Double",
                new Room("Double", 180.0, Arrays.asList("WiFi", "TV", "Mini Bar")));

        roomCatalog.put("Suite",
                new Room("Suite", 300.0, Arrays.asList("WiFi", "TV", "Mini Bar", "Jacuzzi")));

        // Step 3: Create search service (read-only)
        RoomSearchService searchService =
                new RoomSearchService(inventory, roomCatalog);

        // Step 4: Perform search (no state mutation)
        searchService.searchAvailableRooms();
    }
}