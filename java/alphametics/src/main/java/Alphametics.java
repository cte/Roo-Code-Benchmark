import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Alphametics {
    private final List<String> addends;
    private final String result;
    private final Set<Character> letters;
    private final Set<Character> leadingLetters;

    Alphametics(String userInput) {
        // Parse the equation
        String[] parts = userInput.split("==");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid equation format");
        }

        // Parse left side (addends)
        String[] leftParts = parts[0].split("\\+");
        this.addends = new ArrayList<>();
        for (String part : leftParts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                addends.add(trimmed);
            }
        }

        // Parse right side (result)
        this.result = parts[1].trim();

        // Collect all unique letters and leading letters
        this.letters = new HashSet<>();
        this.leadingLetters = new HashSet<>();

        // Add letters from addends
        for (String addend : addends) {
            if (!addend.isEmpty()) {
                letters.addAll(getLetters(addend));
                leadingLetters.add(addend.charAt(0));
            }
        }

        // Add letters from result
        letters.addAll(getLetters(result));
        if (!result.isEmpty()) {
            leadingLetters.add(result.charAt(0));
        }
    }

    private Set<Character> getLetters(String word) {
        Set<Character> chars = new HashSet<>();
        for (char c : word.toCharArray()) {
            if (Character.isLetter(c)) {
                chars.add(c);
            }
        }
        return chars;
    }

    Map<Character, Integer> solve() throws UnsolvablePuzzleException {
        // Check if we have more than 10 letters (impossible to solve)
        if (letters.size() > 10) {
            throw new UnsolvablePuzzleException();
        }

        // Convert sets to lists for indexing
        List<Character> lettersList = new ArrayList<>(letters);
        
        // Try to find a solution
        Map<Character, Integer> solution = new HashMap<>();
        Set<Integer> usedDigits = new HashSet<>();
        
        if (backtrack(lettersList, 0, solution, usedDigits)) {
            return solution;
        } else {
            throw new UnsolvablePuzzleException();
        }
    }

    private boolean backtrack(List<Character> letters, int index, Map<Character, Integer> assignment, Set<Integer> usedDigits) {
        // Base case: all letters have been assigned
        if (index == letters.size()) {
            return isValidSolution(assignment);
        }

        char letter = letters.get(index);
        
        // Try each possible digit
        for (int digit = 0; digit <= 9; digit++) {
            // Skip if digit already used
            if (usedDigits.contains(digit)) {
                continue;
            }
            
            // Skip if this is a leading letter and digit is 0
            if (digit == 0 && leadingLetters.contains(letter)) {
                continue;
            }
            
            // Try this assignment
            assignment.put(letter, digit);
            usedDigits.add(digit);
            
            // Recursively try to assign the rest of the letters
            if (backtrack(letters, index + 1, assignment, usedDigits)) {
                return true;
            }
            
            // Backtrack
            assignment.remove(letter);
            usedDigits.remove(digit);
        }
        
        // No valid assignment found
        return false;
    }

    private boolean isValidSolution(Map<Character, Integer> assignment) {
        // Calculate the sum of addends
        long sum = 0;
        for (String addend : addends) {
            long value = evaluateWord(addend, assignment);
            sum += value;
        }
        
        // Calculate the value of the result
        long resultValue = evaluateWord(result, assignment);
        
        // Check if the equation is valid
        return sum == resultValue;
    }

    private long evaluateWord(String word, Map<Character, Integer> assignment) {
        StringBuilder numStr = new StringBuilder();
        for (char c : word.toCharArray()) {
            if (Character.isLetter(c)) {
                numStr.append(assignment.get(c));
            }
        }
        
        // Handle empty string case
        if (numStr.length() == 0) {
            return 0;
        }
        
        return Long.parseLong(numStr.toString());
    }
}