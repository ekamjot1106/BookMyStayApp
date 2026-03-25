import java.util.*;

/**
 * ============================================================
 * CLASS - Reservation
 * ============================================================
 *
 * Represents a confirmed booking.
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

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
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
 * CLASS - AddOnService
 * ============================================================
 *
 * Represents an optional service that can be added to a reservation.
 */
class AddOnService {
    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return name + " ($" + cost + ")";
    }
}


/**
 * ============================================================
 * CLASS - AddOnServiceManager
 * ============================================================
 *
 * Manages mapping of reservations to selected services.
 */
class AddOnServiceManager {
    // Map from reservation ID -> list of selected services
    private Map<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    /**
     * Adds a service to a reservation
     */
    public void addServiceToReservation(Reservation reservation, AddOnService service) {
        reservationServices.computeIfAbsent(reservation.getReservationId(), k -> new ArrayList<>())
                .add(service);
        System.out.println("Added service " + service + " to reservation " + reservation.getReservationId());
    }

    /**
     * Retrieves list of services for a reservation
     */
    public List<AddOnService> getServicesForReservation(Reservation reservation) {
        return reservationServices.getOrDefault(reservation.getReservationId(), Collections.emptyList());
    }

    /**
     * Calculates total cost of add-on services for a reservation
     */
    public double calculateTotalAddOnCost(Reservation reservation) {
        double total = 0.0;
        for (AddOnService s : getServicesForReservation(reservation)) {
            total += s.getCost();
        }
        return total;
    }

    /**
     * Displays all add-on selections
     */
    public void displayAllAddOns() {
        System.out.println("\nAll Add-On Services:");
        for (Map.Entry<String, List<AddOnService>> entry : reservationServices.entrySet()) {
            System.out.println("ReservationID: " + entry.getKey() + " -> " + entry.getValue());
        }
    }
}


/**
 * ============================================================
 * MAIN CLASS - UseCase7AddOnServiceSelection
 * ============================================================
 *
 * Demonstrates attaching add-on services to reservations.
 */
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        // Step 1: Create some confirmed reservations
        Reservation r1 = new Reservation("R101", "Alice", "Single");
        Reservation r2 = new Reservation("R102", "Bob", "Suite");
        Reservation r3 = new Reservation("R103", "Charlie", "Double");

        // Step 2: Define available add-on services
        AddOnService breakfast = new AddOnService("Breakfast", 20.0);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 50.0);
        AddOnService spa = new AddOnService("Spa Access", 100.0);
        AddOnService extraBed = new AddOnService("Extra Bed", 30.0);

        // Step 3: Initialize Add-On Service Manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Step 4: Attach services to reservations
        serviceManager.addServiceToReservation(r1, breakfast);
        serviceManager.addServiceToReservation(r1, spa);

        serviceManager.addServiceToReservation(r2, airportPickup);
        serviceManager.addServiceToReservation(r2, spa);
        serviceManager.addServiceToReservation(r2, extraBed);

        serviceManager.addServiceToReservation(r3, breakfast);

        // Step 5: Display add-ons for individual reservations
        System.out.println("\nAdd-On Services for Each Reservation:");
        for (Reservation r : Arrays.asList(r1, r2, r3)) {
            List<AddOnService> services = serviceManager.getServicesForReservation(r);
            double totalCost = serviceManager.calculateTotalAddOnCost(r);
            System.out.println(r);
            System.out.println("Selected Services: " + services);
            System.out.println("Total Add-On Cost: $" + totalCost);
            System.out.println("-------------------------------------");
        }

        // Step 6: Display all add-on mappings
        serviceManager.displayAllAddOns();
    }
}