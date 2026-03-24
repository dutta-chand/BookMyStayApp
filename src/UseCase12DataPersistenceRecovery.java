import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UseCase12DataPersistenceRecovery {
    static class Reservation implements Serializable {
        final String reservationId;
        final String guestName;
        final String roomType;
        final String roomId;

        Reservation(String reservationId, String guestName, String roomType, String roomId) {
            this.reservationId = reservationId;
            this.guestName = guestName;
            this.roomType = roomType;
            this.roomId = roomId;
        }

        public String toString() {
            return reservationId + " | " + guestName + " | " + roomType + " | " + roomId;
        }
    }

    static class SystemState implements Serializable {
        Map<String, Integer> inventory;
        List<Reservation> bookings;

        SystemState(Map<String, Integer> inventory, List<Reservation> bookings) {
            this.inventory = inventory;
            this.bookings = bookings;
        }
    }

    static class PersistenceService {
        private final String fileName = "hotel_state.ser";

        void save(SystemState state) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
                out.writeObject(state);
                System.out.println("State saved successfully.");
            } catch (IOException e) {
                System.out.println("Error saving state: " + e.getMessage());
            }
        }

        SystemState load() {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
                System.out.println("State loaded successfully.");
                return (SystemState) in.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("No saved state found. Starting fresh.");
            } catch (Exception e) {
                System.out.println("Error loading state. Starting with safe defaults.");
            }

            return new SystemState(new HashMap<>(), new ArrayList<>());
        }
    }

    public static void main(String[] args) {
        PersistenceService service = new PersistenceService();

        SystemState state = service.load();

        if (state.inventory.isEmpty() && state.bookings.isEmpty()) {
            state.inventory.put("Standard Room", 2);
            state.inventory.put("Deluxe Room", 1);

            state.bookings.add(new Reservation("RES-1", "Aarav", "Standard Room", "STANDARDROOM-1"));
            state.bookings.add(new Reservation("RES-2", "Meera", "Deluxe Room", "DELUXEROOM-1"));

            System.out.println("Initialized fresh state.");
        }

        System.out.println("\nInventory:");
        for (String k : state.inventory.keySet()) {
            System.out.println(k + " -> " + state.inventory.get(k));
        }

        System.out.println("\nBookings:");
        for (Reservation r : state.bookings) {
            System.out.println(r);
        }

        service.save(state);
    }
}
