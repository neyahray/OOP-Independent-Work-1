import java.util.*;

public class Customer {
    private int customerId;
    private String name;
    private String contactInfo;
    private double budget;
    private final List<Order> orders = new ArrayList<>();

    public Customer() {}

    public Customer(int customerId, String name, String contactInfo, double budget) {
        this.customerId = customerId;
        this.name = name;
        this.contactInfo = contactInfo;
        setBudget(budget);
    }

    //getters
    public int getCustomerId() {return customerId;}
    public String getName() {
        return name;
    }
    public String getContactInfo() {
        return contactInfo;
    }
    public double getBudget() {
        return budget;
    }
    public List<Order> getOrders() {return Collections.unmodifiableList(orders);}

    //setters
    public void setName(String name) {
        this.name = name;
    }
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
    public void setBudget(double budget) {
        if (budget >= 0) {
            this.budget = budget;
        } else {
            System.out.println("Error! Budget can not be less than 0");
        }
    }

    //methods
    public void displayInfo() {
        System.out.println("Customer name: " + this.name +  ", Contact Info: " + this.contactInfo + ", Budget: " + this.budget + "$");
        System.out.println("Total orders placed: " + orders.size());
    }



    void addOrder(Order order) {
        orders.add(order);
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerId == customer.customerId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(customerId);
    }
}
