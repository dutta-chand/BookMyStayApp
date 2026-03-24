import java.util.HashMap;

public class UseCase3InventorySetup {
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

        void updateAvailability(String type, int newCount) {
            inventory.put(type, newCount);
        }

        void displayInventory() {
            System.out.println("Current Room Inventory:");
            for (String type : inventory.keySet()) {
                System.out.println(type + " -> " + inventory.get(type));
            }
        }
    }

    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();

        inventory.addRoomType("Standard Room", 5);
        inventory.addRoomType("Deluxe Room", 3);
        inventory.addRoomType("Suite Room", 2);

        inventory.displayInventory();

        System.out.println();
        inventory.updateAvailability("Deluxe Room", 4);
        inventory.displayInventory();

        System.out.println();
        System.out.println("Available Suite Rooms: " + inventory.getAvailability("Suite Room"));
    }
}