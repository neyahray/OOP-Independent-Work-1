import java.util.*;

public class InventoryShop {
    private Map<Flower, Integer> stock = new HashMap<>();

    public InventoryShop() {}

    public InventoryShop(Map<Flower, Integer> stock) {
        this.stock = new HashMap<>(stock);
    }

    //getters
    public Map<Flower, Integer> getStock() {
        return Collections.unmodifiableMap(stock);
    }

    //setters
    public void setStock(Map<Flower, Integer> stock) {
        this.stock = new HashMap<>(stock);
    }

    //methods
    public void displayInfo() {
        System.out.println("Flower stock: ");
        for (Map.Entry<Flower, Integer> entry : stock.entrySet()) {
            System.out.println(entry.getKey().getName() + " - " + entry.getValue() + " pcs");
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
}
