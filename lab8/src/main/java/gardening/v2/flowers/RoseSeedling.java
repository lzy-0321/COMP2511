package gardening.v2.flowers;

// TODO for students: Add in appropriate generic parameter
public class RoseSeedling extends FlowerSeedling<Rose> {
    public static final String[] MESSAGES = {
            "Abscence makes the heart grow fonder", "A rose by any other name would smell as sweet",
            "One rose says more than the dozen",
            "If every tiny flower wanted to be a rose, spring would lose its loveliness"
    };

    private Rose rose;

    public Rose grow() {
        rose = new Rose();
        return rose;
    }

    @Override
    public int getPrice() {
        return rose.getPrice();
    }
}
