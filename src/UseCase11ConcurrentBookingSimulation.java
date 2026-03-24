import java.util.LinkedList;
import java.util.Queue;

public class UseCase11ConcurrentBookingSimulation {
    static class Reservation {
        final String guestName;
        final String roomType;

        Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    static class SharedQueue {
        private final Queue<Reservation> queue = new LinkedList<>();

        synchronized void add(Reservation r) {
            queue.offer(r);
            notifyAll();
        }

        synchronized Reservation take() {
            while (queue.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            return queue.poll();
        }
    }

    static class InventoryService {
        private int standardRooms;

        InventoryService(int count) {
            this.standardRooms = count;
        }

        synchronized boolean allocateRoom() {
            if (standardRooms > 0) {
                standardRooms--;
                return true;
            }
            return false;
        }

        synchronized int getRemaining() {
            return standardRooms;
        }
    }

    static class BookingProcessor extends Thread {
        private final SharedQueue queue;
        private final InventoryService inventory;

        BookingProcessor(String name, SharedQueue queue, InventoryService inventory) {
            super(name);
            this.queue = queue;
            this.inventory = inventory;
        }

        public void run() {
            for (int i = 0; i < 2; i++) {
                Reservation r = queue.take();

                synchronized (inventory) {
                    if (inventory.allocateRoom()) {
                        System.out.println(getName() + " confirmed booking for " + r.guestName);
                    } else {
                        System.out.println(getName() + " failed booking for " + r.guestName + " (No rooms left)");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SharedQueue queue = new SharedQueue();
        InventoryService inventory = new InventoryService(2);

        queue.add(new Reservation("Aarav", "Standard Room"));
        queue.add(new Reservation("Meera", "Standard Room"));
        queue.add(new Reservation("Kavya", "Standard Room"));
        queue.add(new Reservation("Riya", "Standard Room"));

        BookingProcessor t1 = new BookingProcessor("Thread-1", queue, inventory);
        BookingProcessor t2 = new BookingProcessor("Thread-2", queue, inventory);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final Remaining Rooms: " + inventory.getRemaining());
    }
}