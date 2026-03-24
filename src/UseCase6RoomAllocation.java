import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class UseCase6RoomAllocation {
    static class Reservation {
        final String guestName;
        final String roomType;

        Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String toString() {
            return guestName + " -> " + roomType;
        }
    }

    static class BookingRequestQueueService {
        private final Queue<Reservation> queue;

        BookingRequestQueueService() {
            queue = new LinkedList<>();
        }

        void addRequest(Reservation reservation) {
            queue.offer(reservation);
        }

        Reservation dequeueRequest() {
            return queue.poll();
        }

        boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    static class InventoryService {
        private final HashMap<String, Integer> inventory;

        InventoryService() {
            inventory = new HashMap<>();
        }

        void addRoomType(String roomType, int count) {
            inventory.put(roomType, count);
        }

        int getAvailability(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }

        void decrementAvailability(String roomType) {
            inventory.put(roomType, getAvailability(roomType) - 1);
        }

        void displayInventory() {
            System.out.println("Current Inventory:");
            for (String roomType : inventory.keySet()) {
                System.out.println(roomType + " -> " + inventory.get(roomType));
            }
        }
    }

    static class BookingService {
        private final InventoryService inventoryService;
        private final Set<String> allocatedRoomIds;
        private final HashMap<String, Set<String>> roomsByType;
        private final HashMap<String, Integer> typeCounters;

        BookingService(InventoryService inventoryService) {
            this.inventoryService = inventoryService;
            this.allocatedRoomIds = new HashSet<>();
            this.roomsByType = new HashMap<>();
            this.typeCounters = new HashMap<>();
        }

        void processBooking(Reservation reservation) {
            String roomType = reservation.roomType;

            if (inventoryService.getAvailability(roomType) <= 0) {
                System.out.println("Booking failed for " + reservation.guestName + ": " + roomType + " not available");
                return;
            }

            int nextId = typeCounters.getOrDefault(roomType, 0) + 1;
            String roomId = roomType.replace(" ", "").toUpperCase() + "-" + nextId;

            while (allocatedRoomIds.contains(roomId)) {
                nextId++;
                roomId = roomType.replace(" ", "").toUpperCase() + "-" + nextId;
            }

            typeCounters.put(roomType, nextId);
            allocatedRoomIds.add(roomId);

            roomsByType.putIfAbsent(roomType, new HashSet<>());
            roomsByType.get(roomType).add(roomId);

            inventoryService.decrementAvailability(roomType);

            System.out.println("Reservation confirmed for " + reservation.guestName);
            System.out.println("Room Type: " + roomType);
            System.out.println("Room ID: " + roomId);
            System.out.println("Remaining " + roomType + ": " + inventoryService.getAvailability(roomType));
            System.out.println("---------------------------");
        }

        void displayAllocatedRooms() {
            System.out.println("Allocated Rooms:");
            for (String roomType : roomsByType.keySet()) {
                System.out.println(roomType + " -> " + roomsByType.get(roomType));
            }
        }
    }

    public static void main(String[] args) {
        InventoryService inventoryService = new InventoryService();
        inventoryService.addRoomType("Standard Room", 2);
        inventoryService.addRoomType("Deluxe Room", 1);
        inventoryService.addRoomType("Suite Room", 1);

        BookingRequestQueueService requestQueue = new BookingRequestQueueService();
        requestQueue.addRequest(new Reservation("Aarav", "Standard Room"));
        requestQueue.addRequest(new Reservation("Meera", "Deluxe Room"));
        requestQueue.addRequest(new Reservation("Kavya", "Standard Room"));
        requestQueue.addRequest(new Reservation("Riya", "Suite Room"));

        BookingService bookingService = new BookingService(inventoryService);

        while (!requestQueue.isEmpty()) {
            bookingService.processBooking(requestQueue.dequeueRequest());
        }

        inventoryService.displayInventory();
        bookingService.displayAllocatedRooms();
    }
}