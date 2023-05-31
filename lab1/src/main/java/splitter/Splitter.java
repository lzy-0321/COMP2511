package splitter;

import java.util.Scanner;

public class Splitter {
    public static void main(String[] args) {
        System.out.println("Enter a sentence specified by spaces only: ");
        // Add your code
        // read in a sentence from the user
        Scanner scanner = new Scanner(System.in);
        // split the sentence into words
        String sentence = scanner.nextLine();
        scanner.close();
        String[] words = sentence.split(" ");
        // print each word on a new line
        for (int i = 0; i < words.length; i++) {
            System.out.println(words[i]);
        }
    }
}
