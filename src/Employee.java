import java.util.*;

public class Employee {
    private int employeeId;
    private String name;
    private final List<Order> processedOrders = new ArrayList<>();   // 1:N

    public Employee() {}

    public Employee(int employeeId, String name) {
        this.employeeId = employeeId;
        this.name = name;
    }

    //getters
    public int getEmployeeId() {return employeeId;}
    public String getName() {return name;}
    public List<Order> getProcessedOrders() {return Collections.unmodifiableList(processedOrders);}

    //setters
    public void setName(String name) {this.name = name;}

    //methods
    public boolean processNext(ShopService service) {
        Order order = service.processNextOrder();
        if (order == null) {
            return false;
        }
        processedOrders.add(order);
        System.out.println("Processed by employee: " + name);
        order.printReceipt();
        return true;
    }

    public int processAll(ShopService service) {
        int count = 0;
        while (service.getQueueSize() > 0) {
            if (processNext(service)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return employeeId == employee.employeeId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(employeeId);
    }
}