package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {

    TreeSet<String> wordList = new TreeSet<String>();
    Set<Character> guessedLetters = new TreeSet<Character>();
    int wordLength;
    private Object GuessAlreadyMadeException;

    // Method to read in a dictionary and store all possible words in the wordList
    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        wordList.clear();
        // Check wordLength exception
        if (wordLength < 2) {
            System.out.println("Invalid Word Length");
            //throw new IOException();
        }
        this.wordLength = wordLength;

        // Create file scanner
        Scanner fileScanner = new Scanner(dictionary);
        String nextString= null;


        // For every word in the dictionary file
        if (!fileScanner.hasNext()){
            throw new EmptyDictionaryException();
        }

        while (fileScanner.hasNext()) {
            nextString = fileScanner.next().toLowerCase();
            // If it's the correct size,
            // Add it to the alphabetized word list
            if (nextString.length() == wordLength) {
                wordList.add(nextString.toLowerCase());
            }
        }

        //If the wordList is empty after filling, there are no words with the correct size to play from
        if (wordList.isEmpty()) {
            throw new EmptyDictionaryException();
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException{

        if (guessedLetters.contains(Character.toLowerCase(guess))) {
            throw new GuessAlreadyMadeException();
        }

        // Haven't guessed the letter yet
        else {
            // Add the letter to the set of guessedLetters
            guessedLetters.add(Character.toLowerCase(guess));

            // Now for each word in our updated wordList, we want to categorize them into different categories
            // hashMap will store a set of set<Strings> that you can tell are different based on an arrayList<int>
            HashMap<ArrayList<Integer>, TreeSet<String>> hashMap = new HashMap<>();

            // for each word
            for (String s : wordList) {
                //we want to check through the word to see how many times the guessed letter shows up
                //Store every accurance in an intList
                ArrayList<Integer> intList = new ArrayList<>();
                for (int i = 0; i < wordLength; ++i) {
                    if (s.charAt(i) == guess) {
                        intList.add(i);
                    }
                }

                //Store this word in a set
                TreeSet<String> subSet = new TreeSet<>();
                //If the same set already exists in the hashMap, grab it
                if (hashMap.get(intList) != null) {
                    subSet = hashMap.get(intList);
                }
                // Then add the word to that set
                subSet.add(s);
                // And put it back in the hashMap
                hashMap.put(intList, subSet);

            } // end for each word

            // At this point, the hashMap is full
            // We must now choose which set to act as the new dictionary

            //Lets clear out the existing dictionary, we will reset it in this step
            wordList.clear();

            // For each word group
            for (TreeSet<String> iSet : hashMap.values()) {
                // If there's no words in the dictionary, set it
                if (wordList.size() == 0) {
                    wordList.addAll(iSet);
                }
                // Else the most important step is if the word group is larger
                else if (iSet.size() > wordList.size()) {
                    wordList.clear();
                    wordList.addAll(iSet);
                }

                // if the new set is smaller, just break out
                else if (iSet.size() < wordList.size()) {
                    //dont do anything
                }

                // But if they're the same size...
                else if (iSet.size() == wordList.size()) {

                    String iSetString = iSet.first();
                    ArrayList<Integer> iSetStringArrayList = new ArrayList<>();
                    String wordListString = wordList.first();
                    ArrayList<Integer> wordListStringArrayList = new ArrayList<>();


                    for (int i = 0; i < iSetString.length(); ++i) {
                        if (iSetString.charAt(i) == guess) {
                            iSetStringArrayList.add(i);
                        }
                    }
                    for (int i = 0; i < wordListString.length(); ++i) {
                        if (wordListString.charAt(i) == guess) {
                            wordListStringArrayList.add(i);
                        }
                    }

                    int iSetCount = iSetStringArrayList.size();
                    int wordListCount = wordListStringArrayList.size();

                    // If the word does not appear in the new set, but does appear in the normal set
                    if (iSetCount == 0 && wordListCount != 0) {
                        wordList.clear();
                        wordList.addAll(iSet);
                    }
                    // Else, if the normal set doesn't have the char appear, just break
                    else if (wordListCount == 0 && iSetCount != 0) {
                        // do nothing
                    }

                    // Neither set is of size 0
                    else {
                        // the letter shows up less in the normal dict
                        if (iSetCount > wordListCount) {

                        }
                        // else if the letter shows up less in the new set
                        else if (wordListCount > iSetCount) {
                            wordList.clear();
                            wordList.addAll(iSet);
                        }

                        // both sets are the same size
                        else {
                            // I have to fetching see which one shows up on the rightmost side first
                            //fetch my life
                            //ugg
                            // myLife.commitSuicide(bathTub, toaster);
                            // ahh that's funny

                            for (int i = 0; i < wordLength - 1; ++i) {
                                boolean wordListBool = wordList.first().charAt(wordLength - i - 1) == guess;
                                boolean iSetBool = iSet.first().charAt(wordLength - i - 1) == guess;
                                if (wordListBool && !iSetBool) {
                                    // do nothing
                                    break;
                                }
                                else if (iSetBool && !wordListBool) {
                                    wordList.clear();
                                    wordList.addAll(iSet);
                                    break;
                                }
                            }
                        }
                    }
                }
            } // for each word group
        }
        return wordList;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return (SortedSet<Character>) guessedLetters;
    }

    public boolean printWord() {
        StringBuilder sb = new StringBuilder();
        String poop = wordList.first();

        boolean finished = true;
        for (int i = 0; i < wordLength; ++i) {
            if (guessedLetters.contains(poop.charAt(i))) {
                sb.append(poop.charAt(i));
            }
            else {
                finished = false;
                sb.append("-");
            }
        }

        System.out.print(sb);

        return finished;
    }
}
