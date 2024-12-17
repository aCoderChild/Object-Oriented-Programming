import java.util.ArrayList;
import java.util.List;

public class Person {
    private String name;
    private String address;
    private List<Vehicle> vehicleList;

    public Person(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public void addVehicle(Vehicle vehicle) {
        if (vehicleList == null) {
            vehicleList = new ArrayList<>();
        }
        vehicleList.add(vehicle);
    }

    public void removeVehicle(String registrationNumber) {
        if (vehicleList != null) {
            for (int i = 0; i < vehicleList.size(); i++) {
                Vehicle vehicle = vehicleList.get(i);
                if (vehicle.registrationNumber.equals(registrationNumber)) {
                    vehicleList.remove(i);
                    break;
                }
            }
        }
    }

    public String getVehiclesInfo() {
        if (vehicleList == null) {
            return getName() + " has no vehicle!";
        }
        String prev = getName() + " has:\n\n";
        for (Vehicle vehicle : vehicleList) {
            String v = vehicle.getInfo();
            prev = prev + v;
        }
        return prev;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}