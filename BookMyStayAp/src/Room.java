
public abstract class Room {

    /** Number of beds available in the room */
    protected int numberOfBeds;

    /** Total size of the room in square feet */
    protected int squareFeet;

    /** Price charged per night for this room type */
    protected double pricePerNight;

    /**
     * Constructor to initialize room attributes
     *
     * @param numberOfBeds number of beds in the room
     * @param squareFeet size of the room
     * @param pricePerNight cost per night
     */
    public Room(int numberOfBeds, int squareFeet, double pricePerNight) {
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
    }

    /**
     * Displays room details
     */
    public void displayRoomDetails() {
        System.out.println("Room Details:");
        System.out.println("Number of Beds: " + numberOfBeds);
        System.out.println("Room Size: " + squareFeet + " sq.ft");
        System.out.println("Price per Night: $" + pricePerNight);
    }
}