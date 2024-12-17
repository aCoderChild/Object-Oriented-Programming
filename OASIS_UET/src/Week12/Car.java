public class Car extends Vehicle {
    private int numberOfDoors;

    public Car(String brand, String model, String registrationNumber, Person owner, int numberOfDoors) {
        super(brand, model, registrationNumber, owner);
        this.numberOfDoors = numberOfDoors;
    }

    public String getInfo() {
        Person owner = getPerson();
        return "Car:\n" + "\tBrand: " + getBrand() + "\n" + "\tModel: " + getModel() + "\n" + "\tRegistration Number: "
                + getRegistrationNumber() + "\n" + "\tNumber of Doors: " + numberOfDoors + "\n" + "\tBelongs to " +
                owner.getName() + " - " + owner.getAddress() +"\n";
    }

    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(int numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }
}