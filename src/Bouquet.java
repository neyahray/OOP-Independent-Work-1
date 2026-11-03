import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Bouquet {
    private Map<Flower, Integer> flowers = new HashMap<>();

    public Bouquet() {}

    public Bouquet(Map<Flower, Integer> flowers) {
        this.flowers = new HashMap<>(flowers);
    }

    //getter
    public Map<Flower, Integer> getFlowers() {
        return flowers;
    }

    //setter
    public void setFlowers(Map<Flower, Integer> flowers) {
        this.flowers = new HashMap<>(flowers);
    }

    //methods
    public void displayInfo() {
        for (Map.Entry<Flower, Integer> entry : flowers.entrySet()) {
            Flower flower = entry.getKey();
            Integer quantity = entry.getValue();
            double cost = flower.getPrice() * quantity;
            System.out.println(flower.getName() + " - " + quantity + " pcs (" + flower.getPrice() + "$ each) = " + cost + "$");

        }
    }

    public void addFlower(Flower flower, int quantity) {
        int currentQuantity = flowers.getOrDefault(flower, 0);
        flowers.put(flower, currentQuantity + quantity);
    }

    public double calculateTotalPrice() {
        double price = 0;
        for (Map.Entry<Flower, Integer> entry : flowers.entrySet()) {
            price += entry.getKey().getPrice() * entry.getValue();
        }
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Bouquet bouquet = (Bouquet) o;
        return Objects.equals(flowers, bouquet.flowers);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(flowers);
    }
}

