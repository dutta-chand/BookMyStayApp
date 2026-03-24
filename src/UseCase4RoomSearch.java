import java.util.HashMap;

public class UseCase4RoomSearch {
    static class RoomInventory {
        private final HashMap<String, Integer> inventory;

        RoomInventory() {
            inventory = new HashMap<>();
        }

        void addRoomType(String type, int count) {
            inventory.put(type, count);
        }

        int getAvailability(String type) {
            return inventory.getOrDefault(type, 0);
        }

        HashMap<String, Integer> getAllRooms() {
            return new HashMap<>(inventory);
        }
    }

    static abstract class Room {
        final String type;
        final double price;

        Room(String type, double price) {
            this.type = type;
            this.price = price;
        }

        abstract void display();
    }

    static class StandardRoom extends Room {
        StandardRoom() {
            super("Standard Room", 2000.0);
        }

        void display() {
            System.out.println(type + " | Price: ₹" + price);
        }
    }

    static class DeluxeRoom extends Room {
        DeluxeRoom() {
            super("Deluxe Room", 4000.0);
        }

        void display() {
            System.out.println(type + " | Price: ₹" + price);
        }
    }

    static class SuiteRoom extends Room {
        SuiteRoom() {
            super("Suite Room", 7000.0);
        }

        void display() {
            System.out.println(type + " | Price: ₹" + price);
        }
    }

    static class RoomSearchService {
        private final RoomInventory inventory;

        RoomSearchService(RoomInventory inventory) {
            this.inventory = inventory;
        }

        void searchRooms() {
            System.out.println("Available Rooms:");
            HashMap<String, Integer> rooms = inventory.getAllRooms();

            for (String type : rooms.keySet()) {
                int available = rooms.get(type);

                if (available > 0) {
                    Room room = null;

                    if (type.equals("Standard Room")) {
                        room = new StandardRoom();
                    } else if (type.equals("Deluxe Room")) {
                        room = new DeluxeRoom();
                    } else if (type.equals("Suite Room")) {
                        room = new SuiteRoom();
                    }

                    if (room != null) {
                        room.display();
                        System.out.println("Available: " + available);
                        System.out.println("-----------------------");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Standard Room", 5);
        inventory.addRoomType("Deluxe Room", 0);
        inventory.addRoomType("Suite Room", 2);

        RoomSearchService searchService = new RoomSearchService(inventory);
        searchService.searchRooms();
    }
}