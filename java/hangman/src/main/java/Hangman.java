import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Hangman {

    Observable<Output> play(Observable<String> words, Observable<String> letters) {
        // Create a BehaviorSubject to hold the current state of the game
        BehaviorSubject<Output> gameState = BehaviorSubject.createDefault(Output.empty());
        
        // Combine the words and letters observables to create the game logic
        return words
            // For each word, initialize a new game state
            .switchMap(word -> {
                // Create initial state for this word
                String maskedWord = "_".repeat(word.length());
                Output initialState = new Output(
                    word,
                    maskedWord,
                    new HashSet<>(),
                    new HashSet<>(),
                    new ArrayList<>(),
                    Status.PLAYING
                );
                
                // Update the game state with the initial state
                gameState.onNext(initialState);
                
                // Process each letter guess
                return letters
                    .scan(initialState, (state, letter) -> {
                        // Check if the letter has already been played
                        if (state.guess.contains(letter) || state.misses.contains(letter)) {
                            throw new IllegalArgumentException("Letter " + letter + " was already played");
                        }
                        
                        // Check if the game is already over
                        if (state.status != Status.PLAYING) {
                            return state;
                        }
                        
                        // Check if the letter is in the word
                        boolean isCorrectGuess = word.contains(letter);
                        
                        // Create new sets for guesses and misses
                        Set<String> newGuesses = new HashSet<>(state.guess);
                        Set<String> newMisses = new HashSet<>(state.misses);
                        List<Part> newParts = new ArrayList<>(state.parts);
                        
                        // Update the discovered word
                        StringBuilder newDiscovered = new StringBuilder(state.discovered);
                        
                        if (isCorrectGuess) {
                            // Add to guesses
                            newGuesses.add(letter);
                            
                            // Update the discovered word
                            for (int i = 0; i < word.length(); i++) {
                                if (word.charAt(i) == letter.charAt(0)) {
                                    newDiscovered.setCharAt(i, letter.charAt(0));
                                }
                            }
                        } else {
                            // Add to misses
                            newMisses.add(letter);
                            
                            // Add a new part to the hangman
                            if (newParts.size() < Part.values().length) {
                                newParts.add(Part.values()[newParts.size()]);
                            }
                        }
                        
                        // Check if the game is won or lost
                        Status newStatus = state.status;
                        if (!newDiscovered.toString().contains("_")) {
                            newStatus = Status.WIN;
                        } else if (newParts.size() >= Part.values().length) {
                            newStatus = Status.LOSS;
                        }
                        
                        // Create and return the new state
                        return new Output(
                            word,
                            newDiscovered.toString(),
                            newGuesses,
                            newMisses,
                            newParts,
                            newStatus
                        );
                    })
                    // Emit each state update
                    .doOnNext(gameState::onNext);
            })
            // Return the game state observable
            .startWith(gameState)
            .flatMap(output -> gameState);
    }
}