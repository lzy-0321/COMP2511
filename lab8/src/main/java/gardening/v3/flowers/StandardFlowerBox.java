package gardening.v3.flowers;

import java.util.List;

// TODO for student: Fill in the appropriate generic parameters for the class
// so that the code works
public class StandardFlowerBox<T extends Flower> extends FlowerBox<T, T> {

    // TODO for student: Fill in the appropriate generic parameter to resolve compiler errors
    // will depend on the generic parameters you have chosen for the class
    @Override
    public List<T> harvestFlowers() {
        return extractReadyFlowers();
    }

}
