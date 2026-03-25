import java.util.HashMap;
import java.util.Map;

class RoomInventory {


    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }


    private void initializeInventory() {
        roomAvailability.put("Single", 10);
        roomAvailability.put("Double", 20);
        roomAvailability.put("Suite", 5);
    }


    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }


    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }
}



public class UseCase3InventorySetup {

    /**
     * Application entry point.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        // Create centralized inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial availability
        System.out.println("Initial Room Availability:");
        printInventory(inventory);

        // Update availability
        inventory.updateAvailability("Single", 8);
        inventory.updateAvailability("Suite", 3);

        // Display updated availability
        System.out.println("\nUpdated Room Availability:");
        printInventory(inventory);
    }

    private static void printInventory(RoomInventory inventory) {
        for (Map.Entry<String, Integer> entry : inventory.getRoomAvailability().entrySet()) {
            System.out.println(entry.getKey() + " Rooms: " + entry.getValue());
        }
    }
}