package random;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTest {
    @Test
    public void testBattleWithSeed4() {
        Game game = new Game(4);
        assertFalse(game.battle()); // 62 is more than 50, so the hero loses.
        assertFalse(game.battle()); // 52 is more than 50, so the hero loses.
        assertTrue(game.battle()); // 3 is less than 50, so the hero wins.
        assertFalse(game.battle()); // 58 is more than 50, so the hero loses.
        assertFalse(game.battle()); // 67 lose
        assertTrue(game.battle()); // 5 win
        assertTrue(game.battle()); // 11 win
        assertTrue(game.battle()); // 46 win
    }

    @Test
    public void testBattleWithSeedMinus4() {
        Game game = new Game(-4);
        assertTrue(game.battle()); // 39 win
        assertTrue(game.battle()); // 13 win
        assertFalse(game.battle()); // 98 lose
        assertTrue(game.battle()); // 5 win
        assertTrue(game.battle()); // 43 win
        assertFalse(game.battle()); // 89 lose
        assertTrue(game.battle()); // 20 win
        assertTrue(game.battle()); // 23 win
    }

    @Test
    public void testBattleDefaultConstructor() {
        // Create a game with the default constructor.
        Game game = new Game();

        int numBattles = 100000;
        int numWins = 0;

        // Simulate a large number of battles.
        for (int i = 0; i < numBattles; i++) {
            if (game.battle()) {
                numWins++;
            }
        }

        // The proportion of wins should be close to 0.5.
        double winProportion = (double) numWins / numBattles;

        assertTrue(Math.abs(winProportion - 0.5) < 0.005); // Allow a small margin of error.
    }
}
