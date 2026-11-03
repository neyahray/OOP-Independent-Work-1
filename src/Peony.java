public class Peony extends Flower{
    private BudStage budOpenStage; //types: Tight Bud, Marshmallow Stage, Early Blooming, Full Bloom

    public Peony() {}

    public Peony(int flowerId, String name, String color, String stemSize, double price, BudStage budOpenStage) {
        super(flowerId, name, color, stemSize, price);
        this.budOpenStage = budOpenStage;
    }

    //getters
    public BudStage getBudOpenStage() {
        return budOpenStage;
    }

    //setters
    public void setBudOpenStage(BudStage budOpenStage) {
        this.budOpenStage = budOpenStage;
    }

    //displayInfo
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Bud open stage: " + budOpenStage);
    }

    @Override
    public void prepareForSale() {
        System.out.println(getName() + ": bud stage checked (" + budOpenStage + "), stems cut, ready for sale.");
    }


    @Override
    public String applyFlowerFood() {
        return "Food applied for " + getName();
    }

    //unique methods
    public void checkReadiness() {
        if (budOpenStage == BudStage.TIGHT_BUD) {
            System.out.println(this.getName() + ": Closed bud, needs warmth to open.");
        } else {
            System.out.println(this.getName() + ": Ready for bouquet assembly.");
        }
    }

    public void checkSheddingRisk() {
        if (budOpenStage == BudStage.FULL_BLOOM) {
            System.out.println(this.getName() + ": Warning, fully bloomed and may shed quickly.");
        } else {
            System.out.println(this.getName() + ": Stable bud condition.");
        }
    }


    //methods from Processable
    @Override
    public void cutStems() {
        System.out.println("For " + getName() + " cut thick stems at a 45° angle, make a small slit at the bottom, and strip lower leaves.");
    }

    @Override
    public void putInWater() {
        System.out.println("Then use cool water (or slightly warm to speed up blooming) and fill the vase halfway or two-thirds.");
    }

    //methods from Wrappable
    @Override
    public void gatherFlowers() {
        System.out.println(getName() + ": peonies gathered gently, heavy blooms supported to avoid damage.");
    }

    @Override
    public void tieWithTwine() {
        System.out.println(getName() + ": bunch tied loosely with twine so the blooms have room to open.");
    }

    @Override
    public void wrapWithPaper(String color) {
        System.out.println(getName() + "s are wrapped in " + color + " wrapper");
    }
}