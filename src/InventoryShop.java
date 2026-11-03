import java.util.*;

public class InventoryShop {
    private Map<Flower, Integer> stock = new HashMap<>();
    private List<Supplier> suppliers = new ArrayList<>();

    public InventoryShop() {}

    public InventoryShop(Map<Flower, Integer> stock, List<Supplier> suppliers) {
        this.stock = new HashMap<>(stock);
        this.suppliers = new ArrayList<>(suppliers);
    }

    //getters
    public Map<Flower, Integer> getStock() {
        return Collections.unmodifiableMap(stock);
    }
    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    //setters
    public void setStock(Map<Flower, Integer> stock) {
        this.stock = new HashMap<>(stock);
    }
    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = new ArrayList<>(suppliers);
    }

    //methods
    public void displayInfo() {
        System.out.println("Flower stock: ");
        for (Map.Entry<Flower, Integer> entry : stock.entrySet()) {
            System.out.println(entry.getKey().getName() + " - " + entry.getValue() + " pcs");
        }
        System.out.println("All suppliers: ");
        for (Supplier s : suppliers) {
            s.displayInfo();
        }
    }

    public void displayFlowers() {
        for (Map.Entry<Flower, Integer> entry : stock.entrySet()) {
            if (entry.getValue() > 0) {
                System.out.println(entry.getKey().getName() + " - " + entry.getValue() + " pcs");
            }
        }
    }

    public boolean checkStock(Flower flower, int quantity) {
        int currentStock = stock.getOrDefault(flower, 0);
        return currentStock >= quantity;
    }

    public int getQuantity(Flower flower) {
        return stock.getOrDefault(flower ,0);
    }

    //comparator by quantity
    public Comparator<Flower> byQuantity() {
        return Comparator.comparingInt(f -> stock.getOrDefault(f, 0));
    }

    public List<Flower> getFlowersSortedByQuantity() {
        List<Flower> list = new ArrayList<>(stock.keySet());
        list.sort(byQuantity());
        return list;
    }

    public boolean addNewFlowerToStock(Flower flower, int quantity) {
        if (stock.containsKey(flower)) {
            System.out.println("Error! Flower " + flower.getName() + " already exist in the stock!");
            return false;
        } else if (quantity <= 0) {
            System.out.println("Error! Quantity must be greater than 0!");
            return false;
        } else {
            stock.put(flower, quantity);
            System.out.println("New flower " + flower.getName() + " is added to stock!");
            return true;
        }

    }

    public boolean reduceStock(Flower flower, int quantity) {
        if (!stock.containsKey(flower)) {
            System.out.println("Error! Flower " + flower.getName() + " not found in stock");
            return false;
        }
        if (quantity <= 0) {
            System.out.println("Error! Quantity must be greater than 0!");
            return false;
        }
        int currentStock = stock.get(flower);
        if (currentStock < quantity) {
            System.out.println("Error! Not enough " + flower.getName() + " in stock. Available: " + currentStock);
            return false;
        }
        stock.put(flower, currentStock - quantity);
        System.out.println("Stock updated: reduced " + flower.getName() + " by " + quantity + " pcs from inventory");
        return true;
    }

    public boolean orderFromSupplier (Supplier supplier, Flower flower, int quantity) {
        if (!supplier.getFlowerType().equals(flower)) {
            System.out.println("Error! " + supplier.getCompanyName() + " does not supply " + flower.getName());
            return false;
        }
        if (!restock(flower, quantity)) {
            return false;
        }
        supplier.supplyFlowers(flower, quantity);
        System.out.println("Stock restocked successfully!");
        return true;
    }

    public boolean restock(Flower flower, int quantity) {
        if (quantity <= 0) {
            System.out.println("Error! Quantity must be greater than 0!");
            return false;
        }
        stock.put(flower, stock.getOrDefault(flower, 0) + quantity);
        return true;
    }

    public boolean deleteFlowerFromStock(Flower flower) {
        if (stock.containsKey(flower)) {
            stock.remove(flower);
            System.out.println("Flower " + flower.getName() + " is deleted from the stock");
            return true;
        } else {
            System.out.println("Error! Flower " + flower.getName() + " not found in stock");
            return false;
        }
    }


    //Grouping
    public Map<String, List<Flower>> groupFlowersByColor() {
        Map<String, List<Flower>> groupedMap = new HashMap<>();
        for (Flower flower : stock.keySet()) {
            String color = flower.getColor();
            groupedMap.putIfAbsent(color, new ArrayList<>());
            groupedMap.get(color).add(flower);
        }

        return groupedMap;
    }

    //Filtering
    public List<Flower> filterByType(String type) {
        List<Flower> list = new ArrayList<>();
        for (Flower f : stock.keySet()) {
            if (f.getClass().getSimpleName().equalsIgnoreCase(type)) {
                list.add(f);
            }
        }
        return list;
    }
}
