package gardening.v3.flowers;

import java.util.List;
import java.util.stream.Collectors;

// TODO for student: fill in the generic parameters for the class so that the
// code works
// look in the FlowerSystem class to see how the class is used and look at the
// other classes to see how their generic parameters are defined
public class MutationFlowerBox<T extends Flower, R extends Mutation<T>> extends FlowerBox<T, R> {

    // TODO for student: fill in appropriate generic parameters.
    // Will depend on the generic parameters you have chosen for the class
    private Mutator<T, R> mutator;

    // TODO for student: fill in appropriate generic parameters.
    // Will depend on the generic parameters you have chosen for the class
    public MutationFlowerBox(Mutator<T, R> mutator) {
        this.mutator = mutator;
    }

    // TODO for student: fill in appropriate generic parameters.
    // Will depend on the generic parameters you have chosen for the class
    @Override
    public List<R> harvestFlowers() {
        List<T> readyFlowers = extractReadyFlowers();
        // mutate the ready flowers and return the mutated flowers
        return readyFlowers.stream().map((flower) -> mutator.mutateFlower(flower)).collect(Collectors.toList());
    }
}
