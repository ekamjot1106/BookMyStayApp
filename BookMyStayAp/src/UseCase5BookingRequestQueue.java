import java.util.*;

/**
 * ============================================================
 * CLASS - Reservation
 * ============================================================
 *
 * Represents a guest booking request.
 * No allocation is done here.
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
 * CLASS - BookingRequestQueue
 * ============================================================
 *
 * Handles incoming booking requests using FIFO queue.
 * No inventory updates happen here.
 */
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    /**
     * Add a booking request to the queue
     */
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Request added: " + reservation);
    }

    /**
     * View next request (without removing)
     */
    public Reservation peekNextRequest() {
        return requestQueue.peek();
    }

    /**
     * Get and remove next request (FIFO)
     */
    public Reservation processNextRequest() {
        return requestQueue.poll();
    }

    /**
     * Display all queued requests
     */
    public void displayQueue() {
        System.out.println("\nCurrent Booking Queue:");
        for (Reservation r : requestQueue) {
            System.out.println(r);
        }
    }
}


/**
 * ============================================================
 * MAIN CLASS - UseCase5BookingRequestQueue
 * ============================================================
 *
 * Demonstrates FIFO booking request handling.
 */
public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        // Step 1: Create booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Step 2: Simulate guest booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single"));
        bookingQueue.addRequest(new Reservation("Bob", "Suite"));
        bookingQueue.addRequest(new Reservation("Charlie", "Double"));
        bookingQueue.addRequest(new Reservation("Diana", "Single"));

        // Step 3: Display queue (FIFO order preserved)
        bookingQueue.displayQueue();

        // Step 4: Peek next request
        System.out.println("\nNext request to process:");
        System.out.println(bookingQueue.peekNextRequest());

        // Step 5: Process requests (still no inventory updates)
        System.out.println("\nProcessing requests (FIFO):");
        while (bookingQueue.peekNextRequest() != null) {
            Reservation processed = bookingQueue.processNextRequest();
            System.out.println("Processing: " + processed);
        }

        // Step 6: Queue should now be empty
        bookingQueue.displayQueue();
    }
}