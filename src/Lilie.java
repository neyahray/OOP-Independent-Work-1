public class Lilie extends Flower{
    private boolean isPollenCleaned;

    public Lilie() {}

    public Lilie (int flowerId, String name, String color, String stemSize, double price, boolean isPollenCleaned) {
        super(flowerId, name, color, stemSize, price);
        this.isPollenCleaned = isPollenCleaned;
    }

    //getters
    public boolean isPollenCleaned() {
        return isPollenCleaned;
    }

    //setters
    public void setPollenCleaned(boolean pollenCleaned) {
        isPollenCleaned = pollenCleaned;
    }

    //displayInfo
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Is pollen cleaned: " + isPollenCleaned);
    }

    @Override
    public void prepareForSale() {
        System.out.println(getName() + ": pollen checked, stems cut, ready for sale.");
    }

    @Override
    public String applyFlowerFood() {
        return "Food applied for " + getName();
    }

    //unique methods
    public void checkPollenStatus() {
        if (this.isPollenCleaned) {
            System.out.println(this.getName() + ": Pollen is cleaned, safe for clothing.");
        } else {
            System.out.println(this.getName() + ": Pollen is present, be careful.");
        }
    }

    public void cleanPollen() {
        if (this.isPollenCleaned) {
            System.out.println(this.getName() + ": Pollen was already cleaned.");
        } else {
            this.isPollenCleaned = true;
            System.out.println(this.getName() + ": Pollen successfully removed.");
        }
    }


    //methods from Processable
    @Override
    public void cutStems() {
        System.out.println("For " + getName() + " cut stems cleanly at a 45° angle with a sharp knife, remove lower leaves, and take off pollen.");
    }

    @Override
    public void putInWater() {
        System.out.println("Then Use cool or room-temperature water and fill the vase one-third high to prevent stem rot.");
    }

    //methods from Wrappable
    @Override
    public void gatherFlowers() {
        System.out.println(getName() + ": lilies gathered into a bunch, buds placed at different heights.");
    }

    @Override
    public void tieWithTwine() {
        System.out.println(getName() + ": bunch tied with twine at the middle of the stems.");
    }

    @Override
    public void wrapWithPaper(String color) {
        System.out.println(getName() + "s are wrapped in " + color + " wrapper");
    }
}
