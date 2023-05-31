package scrabble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scrabble {
    private List<String> dictionary = new ArrayList<String>(Arrays.asList("ab", "abe", "able", "ad", "ae", "ae", "ah",
            "al", "ale", "at", "ate", "ba", "bad", "be", "be", "bead", "bed", "bra", "brad", "bread", "bred", "cabble",
            "cable", "ea", "ea", "eat", "eater", "ed", "ha", "hah", "hat", "hate", "hater", "hath", "he", "heat",
            "heater", "heath", "heather", "heathery", "het", "in", "io", "ion", "li", "lin", "lion", "on", "program",
            "ra", "rad", "re", "rea", "read", "red", "sa", "sat", "scabble", "scrabble", "se", "sea", "seat", "seathe",
            "set", "seth", "sh", "sha", "shat", "she", "shea", "sheat", "sheath", "sheathe", "sheather", "sheathery",
            "sheth", "st", "te"));
    private String word;
    private int score;
    private List<String> subword = new ArrayList<String>();

    public Scrabble(String s) {
        // Add your code
        this.word = s.toLowerCase();
        this.score = 0;

        // a subword is constructed by removing one letter from a word.
        // For a subword to be valid, it must be in our dictionary and have at least 2 letters.
        // The subwords for a word should only be recursively generated if the word itself is a valid subword.
        for (int i = 0; i < word.length(); i++) {
            // Delete the i-th letter of the original word
            String temp = word.substring(0, i) + word.substring(i + 1);
            if (temp.length() < 2) {
                break;
            }
            if (dictionary.contains(temp)) {
                subword.add(temp);
            }
        }

        if (subword.size() == 0) {
            return;
        }

        // Recursively generate subwords
        for (int i = 0; i < subword.size(); i++) {
            Scrabble snew = new Scrabble(subword.get(i));
            subword.addAll(snew.subword);
        }

        // Deweight the subword
        subword.sort(null);
        for (int i = 0; i < subword.size(); i++) {
            for (int j = i + 1; j < subword.size(); j++) {
                if (subword.get(i).equals(subword.get(j))) {
                    subword.remove(j);
                    j--;
                }
            }
        }

        score = subword.size();
    }

    public int score() {
        // Add your code
        if (dictionary.contains(word)) {
            score++;
        }
        return score;
    }
}
