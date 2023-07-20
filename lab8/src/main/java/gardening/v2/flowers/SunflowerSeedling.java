package gardening.v2.flowers;

// TODO for students: Add in appropriate generic parameter
public class SunflowerSeedling extends FlowerSeedling<Sunflower> {
    public static final int MAX_SEEDS = 7;
    public static final int SEED_GROWTH = 3;

    private Sunflower sunflower;

    public Sunflower grow() {
        sunflower = new Sunflower();
        return sunflower;
    }

    @Override
    public int getPrice() {
        return sunflower.getPrice();
    }
}
