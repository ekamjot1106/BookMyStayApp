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
    private double totalCost;

    public Reservation(String reservationId, String guestName, String roomType, double totalCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.totalCost = totalCost;
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

    public double getTotalCost() {
        return totalCost;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Total Cost: $" + totalCost;
    }
}


/**
 * ============================================================
 * CLASS - BookingHistory
 * ============================================================
 *
 * Stores confirmed reservations in insertion order.
 */
class BookingHistory {
    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    /**
     * Adds a confirmed reservation to history
     */
    public void addReservation(Reservation reservation) {
        confirmedBookings.add(reservation);
        System.out.println("Booking added to history: " + reservation.getReservationId());
    }

    /**
     * Retrieves all bookings in order
     */
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(confirmedBookings);
    }
}


/**
 * ============================================================
 * CLASS - BookingReportService
 * ============================================================
 *
 * Generates summaries and reports from booking history.
 */
class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    /**
     * Prints all bookings
     */
    public void printAllBookings() {
        System.out.println("\nAll Confirmed Bookings:");
        for (Reservation r : history.getAllReservations()) {
            System.out.println(r);
        }
    }

    /**
     * Prints a summary report by room type
     */
    public void printRoomTypeSummary() {
        System.out.println("\nBooking Summary by Room Type:");
        Map<String, Integer> roomCounts = new HashMap<>();
        Map<String, Double> roomRevenue = new HashMap<>();

        for (Reservation r : history.getAllReservations()) {
            roomCounts.put(r.getRoomType(), roomCounts.getOrDefault(r.getRoomType(), 0) + 1);
            roomRevenue.put(r.getRoomType(), roomRevenue.getOrDefault(r.getRoomType(), 0.0) + r.getTotalCost());
        }

        for (String roomType : roomCounts.keySet()) {
            System.out.println("Room Type: " + roomType +
                    " | Bookings: " + roomCounts.get(roomType) +
                    " | Total Revenue: $" + roomRevenue.get(roomType));
        }
    }
}


/**
 * ============================================================
 * MAIN CLASS - UseCase8BookingHistoryReport
 * ============================================================
 *
 * Demonstrates booking history and reporting functionality.
 */
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        // Step 1: Initialize booking history
        BookingHistory history = new BookingHistory();

        // Step 2: Simulate confirmed reservations
        Reservation r1 = new Reservation("R101", "Alice", "Single", 120.0);
        Reservation r2 = new Reservation("R102", "Bob", "Suite", 400.0);
        Reservation r3 = new Reservation("R103", "Charlie", "Double", 220.0);
        Reservation r4 = new Reservation("R104", "Diana", "Single", 130.0);

        // Step 3: Add to booking history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);
        history.addReservation(r4);

        // Step 4: Initialize report service
        BookingReportService reportService = new BookingReportService(history);

        // Step 5: Generate reports
        reportService.printAllBookings();
        reportService.printRoomTypeSummary();
    }
}