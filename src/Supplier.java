import java.util.Objects;

public class Supplier {
    private int supplierId;
    private String companyName;
    private Flower flowerType;

    public Supplier() {}

    public Supplier(int supplierId, String companyName, Flower flowerType) {
        this.supplierId = supplierId;
        this.companyName = companyName;
        this.flowerType = flowerType;
    }

    //getters
    public int getSupplierId() {return supplierId;}
    public String getCompanyName() {
        return companyName;
    }
    public Flower getFlowerType() {
        return flowerType;
    }

    //setters
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public void setFlowerType(Flower flowerType) {
        this.flowerType = flowerType;
    }

    //methods
    public void displayInfo() {
        System.out.println("Supplier: " + this.companyName + ", supplies: "  + this.flowerType.getName());
    }

    public void supplyFlowers(Flower flower, int quantity) {
        System.out.println(flower.getName() + " is supplied by " + companyName + " on " + quantity + " pcs");
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
