public class Rose extends Flower{
    private boolean hasThorns;

    public Rose() {}

    public Rose(int flowerId, String name, String color, String stemSize, double price, boolean hasThorns) {
        super(flowerId, name, color, stemSize, price);
        this.hasThorns = hasThorns;
    }

    //getters
    public boolean hasThorns() {
        return hasThorns;
    }

    //setters
    public void setHasThorns(boolean hasThorns) {
        this.hasThorns = hasThorns;
    }

    //displayInfo
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Has thorns: " + hasThorns);
    }

    @Override
    public void prepareForSale() {
        System.out.println(getName() + ": thorns trimmed, stem cut, ready for sale.");
    }

    @Override
    public String applyFlowerFood() {
        return "Food applied for " + getName();
    }

    //unique methods
    public void removeThorns() {
        if (this.hasThorns) {
            System.out.println(this.getName() + ": Thorns are removed by florist");
        } else {
            System.out.println(this.getName() + ": This rose never had thorns");
        }
    }

    public void prickWarning() {
        if (this.hasThorns) {
            System.out.println("Caution! This " + this.getName() + " has thorns - wear gloves when handling it!");
        } else {
            System.out.println("It's safe to pick this " + this.getName() + " without gloves.");
        }
    }


    //methods from Processable
    @Override
    public void cutStems() {
        System.out.println("For " + getName() + " cut stems at a 45° angle under water to stop air bubbles, and remove lower leaves and thorns.");
    }

    @Override
    public void putInWater() {
        System.out.println("Then use cold water and fill the vase two-thirds high.");
    }

    //methods from Wrappable
    @Override
    public void gatherFlowers() {
        System.out.println(getName() + ": roses gathered into a bunch, blooms aligned at the same height.");
    }

    @Override
    public void tieWithTwine() {
        System.out.println(getName() + ": bunch tied with twine just below the blooms.");
    }

    @Override
    public void wrapWithPaper(String color) {
        System.out.println(getName() + "s are wrapped in " + color + " wrapper");
    }
 }