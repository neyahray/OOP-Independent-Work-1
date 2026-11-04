import java.util.*;

public class Supplier {
    private int supplierId;
    private String companyName;
    private final Set<Flower> flowers = new HashSet<>();

    public Supplier() {}

    public Supplier(int supplierId, String companyName) {
        this.supplierId = supplierId;
        this.companyName = companyName;
    }

    //getters
    public int getSupplierId() {return supplierId;}
    public String getCompanyName() {
        return companyName;
    }
    public Set<Flower> getFlowers() {
        return Collections.unmodifiableSet(flowers);
    }

    //setters
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    //methods
    public void displayInfo() {
        System.out.print("Supplier: " + companyName + ", supplies: ");
        if (flowers.isEmpty()) {
            System.out.println("nothing yet");
            return;
        }
        List<String> names = new ArrayList<>();
        for (Flower f : flowers) {
            names.add(f.getName());
        }
        Collections.sort(names);
        System.out.println(String.join(", ", names));
    }

    public void supplyFlowers(Flower flower, int quantity) {
        System.out.println(flower.getName() + " is supplied by " + companyName + " on " + quantity + " pcs");
    }

    boolean addFlower(Flower flower) {
        return flowers.add(flower);
    }

    boolean removeFlower(Flower flower) {
        return flowers.remove(flower);
    }

    public boolean supplies(Flower flower) {
        return flowers.contains(flower);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Supplier supplier = (Supplier) o;
        return supplierId == supplier.supplierId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(supplierId);
    }
}
