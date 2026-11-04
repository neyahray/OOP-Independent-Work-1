import java.util.Map;
import java.util.Objects;

public class Order implements Comparable<Order> {
    private int orderId;
    private Customer customer;
    private Bouquet bouquet;
    private OrderStatus status;
    private double customerInitialBudget;
    private Priority priority;

    public Order() {
        this.status = OrderStatus.PENDING;
    }

    public Order(int orderId, Customer customer, Priority priority) {
        this.orderId = orderId;
        this.customer = customer;
        //Composition: Bouquet is created in order
        this.bouquet = new Bouquet();
        this.status = OrderStatus.PENDING;
        this.customerInitialBudget = customer.getBudget();
        this.priority = priority;
    }

    //getters
    public int getOrderId() {return orderId;}
    public Customer getCustomer() {return customer;}
    public Bouquet getBouquet() {return bouquet;}
    public OrderStatus getStatus() {return status;}
    public double getCustomerInitialBudget() {return customerInitialBudget;}
    public Priority getPriority() {return priority;}

    //setters
    public void setCustomer(Customer customer) {this.customer = customer;}
    public void setStatus(OrderStatus status) {this.status = status;}
    public void setCustomerInitialBudget(double customerInitialBudget) {this.customerInitialBudget = customerInitialBudget;}
    public void setPriority(Priority priority) {this.priority = priority;}

    //methods
    public void displayInfo() {
        System.out.println(this.status);
        customer.displayInfo();
        if (bouquet != null) {
            bouquet.displayInfo();
        } else {
            System.out.println("Bouquet: no details");
        }
    }

    @Override
    public int compareTo(Order other) {
        int byPriority = this.priority.compareTo(other.priority);
        if (byPriority != 0) {
            return byPriority;
        }
        return Integer.compare(this.orderId, other.orderId);
    }

    public void processOrder(InventoryShop inventory) {
        if (status != OrderStatus.PENDING) {
            System.out.println("Order already processed.");
            return;
        }

        System.out.println("--------------------------");
        System.out.println("Processing order...");

        if (bouquet == null || bouquet.getFlowers().isEmpty()) {
            System.out.println("Error: Cannot process order because the bouquet is empty!");
            this.status = OrderStatus.CANCELLED;
        } else if (customer.getBudget() < bouquet.calculateTotalPrice()) {
            System.out.println("Error: Not enough budget!");
            returnFlowersToStock(inventory);
            this.status = OrderStatus.CANCELLED;
        } else {
            customer.setBudget(customer.getBudget() - bouquet.calculateTotalPrice());
            this.status = OrderStatus.CONFIRMED;
            System.out.println("Order successfully confirmed!");
        }
    }

    private void returnFlowersToStock(InventoryShop inventory) {
        for (Map.Entry<Flower, Integer> e : bouquet.getFlowers().entrySet()) {
            inventory.restock(e.getKey(), e.getValue());
        }
        System.out.println("Flowers returned to stock.");
    }

    public void printReceipt() {
        System.out.println("---Order Receipt---");
        System.out.println("Customer name: " + customer.getName() + ", Contact Info: " + customer.getContactInfo() + ", Customer Initial Budget: " + customerInitialBudget + "$");
        System.out.println("Order status: " + status);
        if (bouquet == null || bouquet.getFlowers().isEmpty()) {
            System.out.println("Bouquet details: No details");
            System.out.println("Total price: 0$");
        } else {
            System.out.println("Bouquet details:");
            bouquet.displayInfo();
            System.out.println("Total price: " + bouquet.calculateTotalPrice() + "$");
        }
        System.out.println("---------");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId == order.orderId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderId);
    }

}
