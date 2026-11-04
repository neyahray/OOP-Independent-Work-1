import java.util.Comparator;
import java.util.Objects;

public abstract class Flower implements Processable, Wrappable, Comparable<Flower>{
    private int flowerId;
    private String name;
    private String color;
    private String stemSize;
    private double price;

    public Flower() {}

    public Flower(int flowerId, String name, String color, String stemSize, double price) {
        this.flowerId = flowerId;
        this.name = name;
        this.color = color;
        this.stemSize = stemSize;
        this.price = price;
    }


    //getters:
    public int getFlowerId() {return flowerId;}
    public String getName() {return name;}
    public String getColor() {return color;}
    public String getStemSize() {return stemSize;}
    public double getPrice() {return price;}


    //setters:
    void setName(String name) {this.name = name;}
    void setColor(String color) {this.color = color;}

    public void setPrice(double price) {
        if (price >= 0) {
            this.price = price;
        } else {
            System.out.println("Error: Price can't be negative!");
        }
    }

    public void setStemSize(String stemSize) {this.stemSize = stemSize;}


    //methods
    public void displayInfo() {
        System.out.println("name: " + this.name + ", color: " +  this.color + ", stem size: " + this.stemSize + ", price: " + this.price + "$");
    }

    public String getFlowerCategory() {
        if (this.price >= 3) {
            return "Luxury sort";
        } else {
            return "Standard sort";
        }
    }

    public static final Comparator<Flower> BY_PRICE_DESC = Comparator.comparingDouble(Flower::getPrice).reversed();
    public static final Comparator<Flower> BY_PRICE_THEN_BY_NAME = Comparator.comparingDouble(Flower::getPrice).thenComparing(Flower::getName);
    public static final Comparator<Flower> BY_COLOR_THEN_BY_NAME = Comparator.comparing(Flower::getColor).thenComparing(Flower::getName);

    //abstract methods
    public abstract void prepareForSale();
    public abstract String applyFlowerFood();

    @Override
    public int compareTo(Flower other) {
        int byName = this.name.compareTo(other.name);
        if (byName != 0) {
            return byName;
        }
        return Integer.compare(this.flowerId, other.flowerId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Flower flower = (Flower) o;
        return flowerId == flower.flowerId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(flowerId);
    }
}