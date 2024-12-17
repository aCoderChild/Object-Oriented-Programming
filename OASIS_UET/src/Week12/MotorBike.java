public class MotorBike extends Vehicle {
    private boolean hasSidecar;

    public MotorBike(String brand, String model, String registrationNumber, Person owner, boolean hasSidecar) {
        super(brand, model, registrationNumber, owner);
        this.hasSidecar = hasSidecar;
    }

    public String getInfo() {
        Person owner = getPerson();
        String flag;
        if (hasSidecar) {
            flag = "true";
        } else {
            flag = "false";
        }

        return "Motor Bike:\n" + "\tBrand: " + getBrand() + "\n" + "\tModel: " + getModel() + "\n" + "\tRegistration Number: "
                + getRegistrationNumber() + "\n" + "\tHas Side Car: " + flag + "\n" + "\tBelongs to " +
                owner.getName() + " - " + owner.getAddress() +"\n";
    }

    public boolean isHasSidecar() {
        return hasSidecar;
    }

    public void setHasSidecar(boolean hasSidecar) {
        this.hasSidecar = hasSidecar;
    }
}
