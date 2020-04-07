package hangman;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class EvilHangman {

    public static void main(String[] args) throws GuessAlreadyMadeException, IOException, EmptyDictionaryException {
        // Create input scanner for guesses
        Scanner input = new Scanner(System.in);
        char nextGuess;

        // Args
        File inputFile = new File(args[0]);
        int wordSize = Integer.parseInt(args[1]);
        int guessCount = Integer.parseInt(args[2]);

        //Set up Dictionary
        EvilHangmanGame game = new EvilHangmanGame();

        game.startGame(inputFile, wordSize);

        // While loop for when they still have guesses left
        while (guessCount > 0) {
            System.out.printf("You have %d guesses left\n", guessCount);

            System.out.print("Used Letters: ");
            for (Character c : game.getGuessedLetters()) {
                System.out.print(c + " ");
            }
            System.out.print("\n");


            System.out.print("Word: ");
            if (game.printWord()) {
                System.out.println("\n\nYou win\nYou must have cheated, punk\n");
                break;
            }

            System.out.print("\nEnter guess: ");
            String next = input.next().toLowerCase();
            nextGuess = next.charAt(0);

            Boolean pass = true;
            do{
                if (next.isBlank()) {
                    System.out.printf("Come on, give me a letter\n");
                    next = input.next().toLowerCase();
                    nextGuess = next.charAt(0);
                    pass = false;
                }

                else if (!Character.isAlphabetic(nextGuess)) {
                    System.out.printf("You fool, %c is not in the alphabet, choose a new letter: ", nextGuess);
                    next = input.next().toLowerCase();
                    nextGuess = next.charAt(0);
                    pass = false;
                }

                else if (game.guessedLetters.contains(Character.toLowerCase(nextGuess))) {
                    //throw new GuessAlreadyMadeException();
                    System.out.printf("You fool, %c has already been guessed, choose a new letter: ", nextGuess);
                    next = input.next().toLowerCase();
                    nextGuess = next.charAt(0);
                    pass = false;
                }

                else {
                    pass = true;
                }

            } while(!pass);

            game.wordList = (TreeSet<String>) game.makeGuess(nextGuess);

            Boolean contained = false;
            ArrayList<Integer> count = new ArrayList<>();
            for (int i = 0; i < game.wordList.first().length(); ++i) {
                if (game.wordList.first().charAt(i) == nextGuess) {
                    count.add(i);
                    contained = true;
                }
            }
            if (!contained) {
                System.out.printf("Sorry, there are no %c's\n\n", nextGuess);
            }
            else {
                guessCount++;
                System.out.printf("Yes, there is %d %c\n\n", count.size(), nextGuess);
            }
            guessCount--;
            if (guessCount ==0) {
                System.out.printf("You lose!\nThe word was: %s", game.wordList.first());
            }
        }

    }
}
