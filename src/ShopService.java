import java.util.*;
import java.util.function.Predicate;

public class ShopService {
    private final InventoryShop inventory;

    //Block B:
    private final Map<Integer, Flower> flowersById = new HashMap<>();
    private final Map<String, Flower> flowersByName = new HashMap<>();
    private final Map<String, List<Flower>> flowersByColor = new HashMap<>();

    public ShopService(InventoryShop inventory) {
        this.inventory = inventory;
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
    public List<Flower> filterFlowers(Predicate<Flower> condition) {
        List<Flower> result = new ArrayList<>();
        for (Flower f : flowersById.values()) {
            if (condition.test(f)) {
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


}
