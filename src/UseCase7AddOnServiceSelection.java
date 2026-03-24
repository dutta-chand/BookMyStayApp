import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UseCase7AddOnServiceSelection {
    static class AddOnService {
        final String name;
        final double cost;

        AddOnService(String name, double cost) {
            this.name = name;
            this.cost = cost;
        }

        public String toString() {
            return name + " (₹" + cost + ")";
        }
    }

    static class AddOnServiceManager {
        private final Map<String, List<AddOnService>> servicesByReservation;

        AddOnServiceManager() {
            servicesByReservation = new HashMap<>();
        }

        void addService(String reservationId, AddOnService service) {
            servicesByReservation.putIfAbsent(reservationId, new ArrayList<>());
            servicesByReservation.get(reservationId).add(service);
        }

        List<AddOnService> getServices(String reservationId) {
            return servicesByReservation.getOrDefault(reservationId, new ArrayList<>());
        }

        double calculateTotalCost(String reservationId) {
            double total = 0;
            for (AddOnService service : getServices(reservationId)) {
                total += service.cost;
            }
            return total;
        }

        void displayServices(String reservationId) {
            List<AddOnService> services = getServices(reservationId);

            if (services.isEmpty()) {
                System.out.println("No add-on services selected for " + reservationId);
                return;
            }

            System.out.println("Add-on Services for " + reservationId + ":");
            for (AddOnService service : services) {
                System.out.println(service);
            }
            System.out.println("Total Add-on Cost: ₹" + calculateTotalCost(reservationId));
        }
    }

    public static void main(String[] args) {
        AddOnServiceManager manager = new AddOnServiceManager();

        String reservationId = "STANDARDROOM-1";

        manager.addService(reservationId, new AddOnService("Breakfast", 500));
        manager.addService(reservationId, new AddOnService("Airport Pickup", 1200));
        manager.addService(reservationId, new AddOnService("Spa Access", 1500));

        manager.displayServices(reservationId);
    }
}