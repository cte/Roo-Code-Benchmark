import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OpticalCharacterReader {

    private static final int DIGIT_WIDTH = 3;
    private static final int DIGIT_HEIGHT = 4;
    private static final Map<String, String> DIGIT_PATTERNS = initializeDigitPatterns();

    private static Map<String, String> initializeDigitPatterns() {
        Map<String, String> patterns = new HashMap<>();
        
        // Pattern for 0
        patterns.put(" _ | ||_|   ", "0");
        
        // Pattern for 1
        patterns.put("     |  |   ", "1");
        
        // Pattern for 2
        patterns.put(" _  _||_    ", "2");
        
        // Pattern for 3
        patterns.put(" _  _| _|   ", "3");
        
        // Pattern for 4
        patterns.put("   |_|  |   ", "4");
        
        // Pattern for 5
        patterns.put(" _ |_  _|   ", "5");
        
        // Pattern for 6
        patterns.put(" _ |_ |_|   ", "6");
        
        // Pattern for 7
        patterns.put(" _   |  |   ", "7");
        
        // Pattern for 8
        patterns.put(" _ |_||_|   ", "8");
        
        // Pattern for 9
        patterns.put(" _ |_| _|   ", "9");
        
        return patterns;
    }

    String parse(List<String> input) {
        validateInput(input);
        
        int numRows = input.size();
        int numCols = input.get(0).length();
        int numDigitsPerRow = numCols / DIGIT_WIDTH;
        int numLines = numRows / DIGIT_HEIGHT;
        
        List<String> results = new ArrayList<>();
        
        for (int line = 0; line < numLines; line++) {
            StringBuilder lineResult = new StringBuilder();
            
            for (int digitPos = 0; digitPos < numDigitsPerRow; digitPos++) {
                String digitPattern = extractDigitPattern(input, line, digitPos);
                lineResult.append(recognizeDigit(digitPattern));
            }
            
            results.add(lineResult.toString());
        }
        
        return String.join(",", results);
    }
    
    private void validateInput(List<String> input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }
        
        int numRows = input.size();
        if (numRows % DIGIT_HEIGHT != 0) {
            throw new IllegalArgumentException("Number of input rows must be a positive multiple of 4");
        }
        
        int numCols = input.get(0).length();
        if (numCols % DIGIT_WIDTH != 0) {
            throw new IllegalArgumentException("Number of input columns must be a positive multiple of 3");
        }
        
        // Ensure all rows have the same length
        for (String row : input) {
            if (row.length() != numCols) {
                throw new IllegalArgumentException("All input rows must have the same length");
            }
        }
    }
    
    private String extractDigitPattern(List<String> input, int line, int digitPos) {
        StringBuilder pattern = new StringBuilder();
        int startRow = line * DIGIT_HEIGHT;
        int startCol = digitPos * DIGIT_WIDTH;
        
        for (int row = 0; row < DIGIT_HEIGHT; row++) {
            for (int col = 0; col < DIGIT_WIDTH; col++) {
                pattern.append(input.get(startRow + row).charAt(startCol + col));
            }
        }
        
        return pattern.toString();
    }
    
    private String recognizeDigit(String pattern) {
        return DIGIT_PATTERNS.getOrDefault(pattern, "?");
    }
}