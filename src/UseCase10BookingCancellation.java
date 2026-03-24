import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class UseCase10BookingCancellation {
    static class Reservation {
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

    static class InventoryService {
        private final Map<String, Integer> inventory;

        InventoryService() {
            inventory = new HashMap<>();
        }

        void addRoomType(String type, int count) {
            inventory.put(type, count);
        }

        int getAvailability(String type) {
            return inventory.getOrDefault(type, 0);
        }

        void increment(String type) {
            inventory.put(type, getAvailability(type) + 1);
        }

        void decrement(String type) {
            inventory.put(type, getAvailability(type) - 1);
        }

        void display() {
            System.out.println("Inventory:");
            for (String k : inventory.keySet()) {
                System.out.println(k + " -> " + inventory.get(k));
            }
        }
    }

    static class BookingHistory {
        private final Map<String, Reservation> active;
        private final Set<String> cancelled;

        BookingHistory() {
            active = new HashMap<>();
            cancelled = new HashSet<>();
        }

        void add(Reservation r) {
            active.put(r.reservationId, r);
        }

        Reservation get(String id) {
            return active.get(id);
        }

        void markCancelled(String id) {
            cancelled.add(id);
        }

        boolean isCancelled(String id) {
            return cancelled.contains(id);
        }

        void display() {
            System.out.println("Active Bookings:");
            for (Reservation r : active.values()) {
                if (!cancelled.contains(r.reservationId)) {
                    System.out.println(r);
                }
            }

            System.out.println("Cancelled Bookings:");
            for (String id : cancelled) {
                System.out.println(id);
            }
        }
    }

    static class CancellationService {
        private final InventoryService inventory;
        private final BookingHistory history;
        private final Stack<String> rollbackStack;

        CancellationService(InventoryService inventory, BookingHistory history) {
            this.inventory = inventory;
            this.history = history;
            this.rollbackStack = new Stack<>();
        }

        void cancel(String reservationId) {
            Reservation r = history.get(reservationId);

            if (r == null) {
                System.out.println("Cancellation Failed: Reservation does not exist");
                return;
            }

            if (history.isCancelled(reservationId)) {
                System.out.println("Cancellation Failed: Already cancelled");
                return;
            }

            rollbackStack.push(r.roomId);

            inventory.increment(r.roomType);

            history.markCancelled(reservationId);

            System.out.println("Cancellation Successful: " + reservationId + " | Room Released: " + r.roomId);
        }

        void showRollbackStack() {
            System.out.println("Rollback Stack: " + rollbackStack);
        }
    }

    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Standard Room", 1);

        BookingHistory history = new BookingHistory();

        Reservation r1 = new Reservation("RES-1", "Aarav", "Standard Room", "STANDARDROOM-1");

        history.add(r1);
        inventory.decrement("Standard Room");

        CancellationService service = new CancellationService(inventory, history);

        service.cancel("RES-1");
        service.cancel("RES-1");
        service.cancel("RES-99");

        inventory.display();
        history.display();
        service.showRollbackStack();
    }
}