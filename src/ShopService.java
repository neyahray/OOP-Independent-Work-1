import java.io.*;
import java.util.*;

public class ShopService {
    private final InventoryShop inventory;
    //Block B:
    private final Map<Integer, Flower> flowersById = new HashMap<>();
    private final Map<String, Flower> flowersByName = new HashMap<>();
    private final Map<String, List<Flower>> flowersByColor = new HashMap<>();

    public ShopService(InventoryShop inventory) {
        this.inventory = inventory;
        for (OrderStatus o : OrderStatus.values()) {
            ordersByStatus.put(o, new ArrayList<>());
        }
    }

    //CRUD OPERATIONS:
    //C-Create:
    public boolean addFlower(Flower flower, int quantity) {
        if (flower == null) {
            System.out.println("Error! Flower is null");
            return false;
        }

        if (flowersById.containsKey(flower.getFlowerId())) {
            System.out.println("Error! Flower with id " + flower.getFlowerId() + " already exists");
            return false;
        }

        String nameKey = flower.getName().trim().toLowerCase();
        if (flowersByName.containsKey(nameKey)) {
            System.out.println("Error! Flower with name " + flower.getName() + " already exists");
            return false;
        }

        if (!inventory.addNewFlowerToStock(flower, quantity)) {
            return false;
        }

        flowersById.put(flower.getFlowerId(), flower);
        flowersByName.put(nameKey, flower);
        String colorKey = flower.getColor().toLowerCase();
        List<Flower> list = flowersByColor.get(colorKey);
        if (list == null) {
            list = new ArrayList<>();
            flowersByColor.put(colorKey, list);
        }
        list.add(flower);

        return true;
    }

    //R-Read
    //O(1)
    public Flower findFlowerById(int id) {
        return flowersById.get(id);
    }

    public Flower findFlowerByName(String name) {
        return flowersByName.get(name.trim().toLowerCase());
    }

    public List<Flower> getFlowersByColor(String color) {
        List<Flower> list = flowersByColor.get(color.toLowerCase());
        if (list == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(list);
    }


    //Search / Filter
    public List<Flower> filterByMinPrice(double minPrice) {
        List<Flower> result = new ArrayList<>();
        for (Flower f : flowersById.values()) {
            if (f.getPrice() > minPrice) {
                result.add(f);
            }
        }
        return result;
    }

    public List<Flower> filterByColor(String color) {
        List<Flower> result = new ArrayList<>();
        for (Flower f : flowersById.values()) {
            if (f.getColor().equalsIgnoreCase(color)) {
                result.add(f);
            }
        }
        return result;
    }

    public List<Flower> getFlowersSorted(Comparator<Flower> comparator) {
        List<Flower> list = new ArrayList<>(flowersById.values());
        list.sort(comparator);
        return list;
    }


    //U-Update
    public boolean updateFlowerPrice(int id, double newPrice) {
        Flower flower = flowersById.get(id);
        if (flower == null || newPrice < 0) {
            System.out.println("Error! Cannot update price for id " + id);
            return false;
        }
        flower.setPrice(newPrice);
        return true;
    }

    public boolean updateFlowerColor(int id, String newColor) {
        Flower flower = flowersById.get(id);
        if (flower == null || newColor == null || newColor.trim().isEmpty()) {
            System.out.println("Error! Cannot update color for id " + id);
            return false;
        }
        removeFromColorIndex(flower);
        flower.setColor(newColor);
        List<Flower> list = flowersByColor.get(newColor.toLowerCase());

        if (list == null) {
            list = new ArrayList<>();
            flowersByColor.put(newColor.toLowerCase(), list);
        }
        list.add(flower);
        return true;
    }

    //D-Delete
    public boolean removeFlower(int id) {
        Flower flower = flowersById.get(id);
        if (flower == null) {
            System.out.println("Error! Flower with id " + id + " not found");
            return false;
        }
        inventory.deleteFlowerFromStock(flower);
        List<Supplier> affected = suppliersByFlower.remove(flower);
        if (affected != null) {
            for (Supplier s : affected) {
                s.removeFlower(flower);
            }
        }
        flowersById.remove(id);
        flowersByName.remove(flower.getName().trim().toLowerCase());
        removeFromColorIndex(flower);
        return true;
    }

    private void removeFromColorIndex(Flower flower) {
        String key = flower.getColor().toLowerCase();
        List<Flower> list = flowersByColor.get(key);
        if (list != null) {
            list.remove(flower);
            if (list.isEmpty()) {
                flowersByColor.remove(key);
            }
        }
    }

    //Block A:
    //register
    private final List<Customer> customers = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();

    //Block B:
    //index
    private final Map<Integer, Customer> customersById = new HashMap<>();
    private final Map<Integer, Order> ordersById = new HashMap<>();
    private final Map<OrderStatus, List<Order>> ordersByStatus = new EnumMap<>(OrderStatus.class);

    //Block D:
    //Orders queue
    private final Queue<Order> orderQueue = new PriorityQueue<>();
    private int nextOrderId = 1;



    //----------Customers
    public boolean addCustomer(Customer customer) {
        if (customer == null || customersById.containsKey(customer.getCustomerId())) {
            System.out.println("Error! Customer is null or id already exists");
            return false;
        }
        customers.add(customer);
        customersById.put(customer.getCustomerId(), customer);
        return true;
    }

    public Customer findCustomerById(int id) {
        return customersById.get(id);
    }

    public boolean removeCustomer(int id) {
        Customer customer = customersById.get(id);
        if (customer == null) {
            System.out.println("Error! Customer with id " + id + " not found");
            return false;
        }
        customers.remove(customer);
        customersById.remove(id);
        return true;
    }

    //---------Orders
    public Order createOrder(int customerId, Priority priority) {
        Customer customer = customersById.get(customerId);
        if (customer == null) {
            System.out.println("Error! Customer with id " + customerId + " not found");
            return null;
        }
        Order order = new Order(nextOrderId++, customer, priority);
        orders.add(order);
        ordersById.put(order.getOrderId(), order);
        ordersByStatus.get(OrderStatus.PENDING).add(order);
        customer.addOrder(order);
        return order;
    }

    //-----Customer adds a flower in his order's bouquet
    public boolean addToBouquet(Order order, String flowerName, int quantity) {
        if (order == null || order.getStatus() != OrderStatus.PENDING) {
            System.out.println("Error! Order is missing or already processed");
            return false;
        }

        Flower flower = findFlowerByName(flowerName);
        if (flower == null) {
            System.out.println("Error! Flower '" + flowerName + "' not found");
            return false;
        }

        if (!inventory.reduceStock(flower, quantity)) {
            return false;
        }

        order.getBouquet().addFlower(flower, quantity);
        return true;
    }


    //-----Queueing an order
    public boolean submitOrder(Order order) {
        if (order == null || order.getStatus() != OrderStatus.PENDING || orderQueue.contains(order)) {
            System.out.println("Error! Order cannot be submitted");
            return false;
        }
        orderQueue.offer(order);
        return true;
    }

    public Order processNextOrder() {
        Order order = orderQueue.poll();
        if (order == null) {
            System.out.println("Queue is empty");
            return null;
        }
        OrderStatus before = order.getStatus();
        order.processOrder(inventory);
        ordersByStatus.get(before).remove(order);
        ordersByStatus.get(order.getStatus()).add(order);
        return order;
    }

    public int getQueueSize() {
        return orderQueue.size();
    }

    public Order findOrderById(int id) {
        return ordersById.get(id);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return new ArrayList<>(ordersByStatus.get(status));
    }


    //Suppliers
    private final List<Supplier> suppliers = new ArrayList<>();
    private final Map<Integer, Supplier> suppliersById = new HashMap<>();
    private final Map<Flower, List<Supplier>> suppliersByFlower = new HashMap<>();

    public boolean addSupplier(Supplier supplier) {
        if (supplier == null || suppliersById.containsKey(supplier.getSupplierId())) {
            System.out.println("Error! Supplier is null or id already exists");
            return false;
        }

        suppliers.add(supplier);
        suppliersById.put(supplier.getSupplierId(), supplier);
        return true;
     }

     public Supplier findSupplierById(int id) {
        return suppliersById.get(id);
     }

     // N: M
    public boolean linkSupplierToFlower(int supplierId, int flowerId) {
        Supplier supplier = suppliersById.get(supplierId);
        Flower flower = flowersById.get(flowerId);
        if (supplier == null || flower == null) {
            System.out.println("Error! Supplier or flower not found");
            return false;
        }
        if (!supplier.addFlower(flower)) {
            System.out.println("Error! Supplier already supplies this flower");
            return false;
        }
        List<Supplier> list = suppliersByFlower.get(flower);

        if (list == null) {
            list = new ArrayList<>();
            suppliersByFlower.put(flower, list);
        }

        list.add(supplier);
        return true;
    }

    public boolean removeSupplier(int id) {
        Supplier supplier = suppliersById.get(id);
        if (supplier == null) {
            System.out.println("Error! Supplier with id " + id + " not found");
            return false;
        }
        for (Flower flower : supplier.getFlowers()) {
            List<Supplier> list = suppliersByFlower.get(flower);
            if (list != null) {
                list.remove(supplier);
                if (list.isEmpty()) {
                    suppliersByFlower.remove(flower);
                }
            }
        }
        suppliers.remove(supplier);
        suppliersById.remove(id);
        return true;
    }

    public List<Supplier> getSuppliersForFlower(int flowerId) {
        Flower flower = flowersById.get(flowerId);
        if (flower == null) {
            return new ArrayList<>();
        }
        List<Supplier> list = suppliersByFlower.get(flower);
        if (list == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(list);
    }

    public boolean orderFromSupplier(int supplierId, int flowerId, int quantity) {
        Supplier supplier = suppliersById.get(supplierId);
        Flower flower = flowersById.get(flowerId);
        if (supplier == null || flower == null) {
            System.out.println("Error! Supplier or flower not found");
            return false;
        }
        if (!supplier.supplies(flower)) {
            System.out.println("Error! " + supplier.getCompanyName() + " does not supply " + flower.getName());
            return false;
        }

        if (!inventory.restock(flower, quantity)) {
            return false;
        }
        supplier.supplyFlowers(flower, quantity);
        return true;
    }


    //Employees
    private final List<Employee> employees = new ArrayList<>();
    private final Map<Integer, Employee> employeesById = new HashMap<>();

    public boolean addEmployee(Employee employee) {
        if (employee == null || employeesById.containsKey(employee.getEmployeeId())) {
            System.out.println("Error! Employee is null or id already exists");
            return false;
        }
        employees.add(employee);
        employeesById.put(employee.getEmployeeId(), employee);
        return true;
    }

    public Employee findEmployeeById(int id) {
        return employeesById.get(id);
    }


    //FILTERING
    public List<Flower> filterByType(Class<? extends Flower> type) {
        List<Flower> result = new ArrayList<>();
        for (Flower f : flowersById.values()) {
            if (type.isInstance(f)) {
                result.add(f);
            }
        }
        return result;
    }

    public List<Flower> getLowStockFlowers(int threshold) {
        List<Flower> result = new ArrayList<>();
        for (Flower f : flowersById.values()) {
            if (inventory.getQuantity(f) <= threshold) {
                result.add(f);
            }
        }
        return result;
    }


    //ANALYTICS
    public double getTotalStockValue() {
        double total = 0;
        for (Flower f : flowersById.values()) {
            total += f.getPrice() * inventory.getQuantity(f);
        }
        return total;
    }

    public int getTotalStockUnits() {
        int total = 0;
        for (Flower f : flowersById.values()) {
            total += inventory.getQuantity(f);
        }
        return total;
    }

    public double getAverageFlowerPrice() {
        if (flowersById.isEmpty()) {
            return 0;
        }
        double sum = 0;
        for (Flower f : flowersById.values()) {
            sum += f.getPrice();
        }
        return sum / flowersById.size();
    }


    //REVENUE - only confirmed orders
    public double getTotalRevenue() {
        double revenue = 0;
        for (Order o : ordersByStatus.get(OrderStatus.CONFIRMED)) {
            revenue += o.getBouquet().calculateTotalPrice();
        }
        return revenue;
    }

    //How much orders in every status
    public Map<OrderStatus, Integer> getOrderCountByStatus() {
        Map<OrderStatus, Integer> result = new EnumMap<>(OrderStatus.class);
        for (OrderStatus s : OrderStatus.values()) {
            result.put(s, ordersByStatus.get(s).size());
        }
        return result;
    }

    //How many of each flower were sold (based on confirmed orders)
    public Map<Flower, Integer> getSoldQuantities() {
        Map<Flower, Integer> sold = new HashMap<>();
        for (Order o : ordersByStatus.get(OrderStatus.CONFIRMED)) {
            for (Map.Entry<Flower, Integer> e : o.getBouquet().getFlowers().entrySet()) {
                sold.put(e.getKey(), sold.getOrDefault(e.getKey(), 0) + e.getValue());
            }
        }
        return sold;
    }

    //Most sold flower
    public Flower getBestSellingFlower() {
        Flower best = null;
        int bestQty = 0;
        for (Map.Entry<Flower, Integer> e : getSoldQuantities().entrySet()) {
            if (e.getValue() > bestQty) {
                bestQty = e.getValue();
                best = e.getKey();
            }
        }
        return best;
    }

    //REPORT:

    public void printSortedReport(String title, Comparator<Flower> comparator) {
        File myFile = new File("C:\\Users\\user\\IdeaProjects\\FlowerShopCopy\\src\\sortedFlowers.txt");

        System.out.println("--- " + title + " ---");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(myFile))) {
            for (Flower f : getFlowersSorted(comparator)) {
                String str = " - " + f.getName() + " | " + f.getColor() + " | " + f.getPrice() + "$ | in stock: " + inventory.getQuantity(f);
                System.out.println(str);
                bw.write(str);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(" ");
    }

    public void printSummaryReport() {
        System.out.println("----- SHOP SUMMARY -------");
        System.out.println("Flower types: " + flowersById.size());
        System.out.println("Total units in stock: " + getTotalStockUnits());
        System.out.println("Total stock value: " + getTotalStockValue() + "$");
        System.out.println("Average flower price: " + getAverageFlowerPrice() + "$");
        System.out.println("Customers: " + customers.size() + ", suppliers: " + suppliers.size() + ", employees: " + employees.size());
        System.out.println("Orders by status: " + getOrderCountByStatus());
        System.out.println("Revenue: " + getTotalRevenue() + "$");
        Flower best = getBestSellingFlower();
        System.out.println("Best-selling flower: ");
        if (best == null) {
            System.out.println("none yet");
        } else {
            System.out.println(best.getName());
        }
    }
}
