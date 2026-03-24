import java.util.LinkedList;
import java.util.Queue;

public class UseCase5BookingRequestQueue {
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

        Reservation peekNextRequest() {
            return queue.peek();
        }

        Reservation processNextRequest() {
            return queue.poll();
        }

        void displayRequests() {
            if (queue.isEmpty()) {
                System.out.println("No booking requests in queue.");
                return;
            }

            System.out.println("Booking Request Queue:");
            for (Reservation reservation : queue) {
                System.out.println(reservation);
            }
        }

        boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    public static void main(String[] args) {
        BookingRequestQueueService requestQueue = new BookingRequestQueueService();

        requestQueue.addRequest(new Reservation("Aarav", "Standard Room"));
        requestQueue.addRequest(new Reservation("Meera", "Deluxe Room"));
        requestQueue.addRequest(new Reservation("Kavya", "Suite Room"));

        requestQueue.displayRequests();
        System.out.println();
        System.out.println("Next request to be processed: " + requestQueue.peekNextRequest());
    }
}