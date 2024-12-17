public abstract class Vehicle {
    protected String brand;
    protected String model;
    protected String registrationNumber;
    protected Person person;

    public Vehicle(String brand, String model, String registrationNumber, Person person) {
        this.brand = brand;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.person = person;
    }

    public abstract String getInfo();

    public void transferOwnership(Person newOwner) {
        this.person = newOwner;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}