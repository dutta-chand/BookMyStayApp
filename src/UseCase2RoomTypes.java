public class UseCase2RoomTypes {
    static abstract class Room {
        final String roomType;
        final double price;
        final int availableRooms;

        Room(String roomType, double price, int availableRooms) {
            this.roomType = roomType;
            this.price = price;
            this.availableRooms = availableRooms;
        }

        abstract void displayDetails();
    }

    static class StandardRoom extends Room {
        StandardRoom(int availableRooms) {
            super("Standard Room", 2000.0, availableRooms);
        }

        void displayDetails() {
            System.out.println("Room Type: " + roomType);
            System.out.println("Price: ₹" + price);
            System.out.println("Available Rooms: " + availableRooms);
            System.out.println("---------------------------");
        }
    }

    static class DeluxeRoom extends Room {
        DeluxeRoom(int availableRooms) {
            super("Deluxe Room", 4000.0, availableRooms);
        }

        void displayDetails() {
            System.out.println("Room Type: " + roomType);
            System.out.println("Price: ₹" + price);
            System.out.println("Available Rooms: " + availableRooms);
            System.out.println("---------------------------");
        }
    }

    static class SuiteRoom extends Room {
        SuiteRoom(int availableRooms) {
            super("Suite Room", 7000.0, availableRooms);
        }

        void displayDetails() {
            System.out.println("Room Type: " + roomType);
            System.out.println("Price: ₹" + price);
            System.out.println("Available Rooms: " + availableRooms);
            System.out.println("---------------------------");
        }
    }

    public static void main(String[] args) {
        Room standard = new StandardRoom(5);
        Room deluxe = new DeluxeRoom(3);
        Room suite = new SuiteRoom(2);

        standard.displayDetails();
        deluxe.displayDetails();
        suite.displayDetails();
    }
}