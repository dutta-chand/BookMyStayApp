import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UseCase8BookingHistoryReport {
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

    static class BookingHistory {
        private final List<Reservation> history;

        BookingHistory() {
            history = new ArrayList<>();
        }

        void addReservation(Reservation reservation) {
            history.add(reservation);
        }

        List<Reservation> getAllReservations() {
            return new ArrayList<>(history);
        }
    }

    static class BookingReportService {
        private final BookingHistory history;

        BookingReportService(BookingHistory history) {
            this.history = history;
        }

        void displayAllBookings() {
            List<Reservation> reservations = history.getAllReservations();

            if (reservations.isEmpty()) {
                System.out.println("No booking history available.");
                return;
            }

            System.out.println("Booking History:");
            for (Reservation r : reservations) {
                System.out.println(r);
            }
        }

        void generateSummaryReport() {
            List<Reservation> reservations = history.getAllReservations();

            Map<String, Integer> countByRoomType = new HashMap<>();

            for (Reservation r : reservations) {
                countByRoomType.put(
                        r.roomType,
                        countByRoomType.getOrDefault(r.roomType, 0) + 1
                );
            }

            System.out.println("\nSummary Report:");
            for (String type : countByRoomType.keySet()) {
                System.out.println(type + " bookings: " + countByRoomType.get(type));
            }

            System.out.println("Total Bookings: " + reservations.size());
        }
    }

    public static void main(String[] args) {
        BookingHistory history = new BookingHistory();

        history.addReservation(new Reservation("RES-1", "Aarav", "Standard Room", "STANDARDROOM-1"));
        history.addReservation(new Reservation("RES-2", "Meera", "Deluxe Room", "DELUXEROOM-1"));
        history.addReservation(new Reservation("RES-3", "Kavya", "Standard Room", "STANDARDROOM-2"));

        BookingReportService reportService = new BookingReportService(history);

        reportService.displayAllBookings();
        reportService.generateSummaryReport();
    }
}