import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UseCase9ErrorHandlingValidation {
    static class InvalidBookingException extends Exception {
        InvalidBookingException(String message) {
            super(message);
        }
    }

    static class Reservation {
        final String guestName;
        final String roomType;

        Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    static class InventoryService {
        private final HashMap<String, Integer> inventory;

        InventoryService() {
            inventory = new HashMap<>();
        }

        void addRoomType(String type, int count) {
            inventory.put(type, count);
        }

        int getAvailability(String type) {
            return inventory.getOrDefault(type, -1);
        }

        void decrement(String type) {
            inventory.put(type, inventory.get(type) - 1);
        }

        boolean containsRoomType(String type) {
            return inventory.containsKey(type);
        }
    }

    static class BookingService {
        private final InventoryService inventory;
        private final Set<String> allocatedIds;
        private int counter;

        BookingService(InventoryService inventory) {
            this.inventory = inventory;
            this.allocatedIds = new HashSet<>();
            this.counter = 0;
        }

        void process(Reservation r) throws InvalidBookingException {
            validate(r);

            counter++;
            String roomId = r.roomType.replace(" ", "").toUpperCase() + "-" + counter;

            while (allocatedIds.contains(roomId)) {
                counter++;
                roomId = r.roomType.replace(" ", "").toUpperCase() + "-" + counter;
            }

            allocatedIds.add(roomId);
            inventory.decrement(r.roomType);

            System.out.println("Booking Confirmed: " + r.guestName + " | " + r.roomType + " | " + roomId);
        }

        void validate(Reservation r) throws InvalidBookingException {
            if (r.guestName == null || r.guestName.trim().isEmpty()) {
                throw new InvalidBookingException("Guest name cannot be empty");
            }

            if (r.roomType == null || r.roomType.trim().isEmpty()) {
                throw new InvalidBookingException("Room type cannot be empty");
            }

            if (!inventory.containsRoomType(r.roomType)) {
                throw new InvalidBookingException("Invalid room type: " + r.roomType);
            }

            int available = inventory.getAvailability(r.roomType);

            if (available <= 0) {
                throw new InvalidBookingException("No availability for " + r.roomType);
            }
        }
    }

    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Standard Room", 1);
        inventory.addRoomType("Deluxe Room", 0);

        BookingService bookingService = new BookingService(inventory);

        Reservation[] requests = {
                new Reservation("Aarav", "Standard Room"),
                new Reservation("", "Standard Room"),
                new Reservation("Meera", "Deluxe Room"),
                new Reservation("Riya", "Suite Room")
        };

        for (Reservation r : requests) {
            try {
                bookingService.process(r);
            } catch (InvalidBookingException e) {
                System.out.println("Booking Failed: " + e.getMessage());
            }
        }

        System.out.println("System continues running safely...");
    }
}