class FoodChain {
    private static final String[] ANIMALS = {
        "fly", "spider", "bird", "cat", "dog", "goat", "cow", "horse"
    };
    
    private static final String[] SECOND_LINES = {
        "I don't know why she swallowed the fly. Perhaps she'll die.",
        "It wriggled and jiggled and tickled inside her.",
        "How absurd to swallow a bird!",
        "Imagine that, to swallow a cat!",
        "What a hog, to swallow a dog!",
        "Just opened her throat and swallowed a goat!",
        "I don't know how she swallowed a cow!",
        "She's dead, of course!"
    };

    String verse(int verse) {
        // Adjust verse to 0-based index
        int index = verse - 1;
        
        if (index < 0 || index >= ANIMALS.length) {
            throw new IllegalArgumentException("Verse must be between 1 and 8");
        }
        
        StringBuilder result = new StringBuilder();
        
        // First line is always the same pattern
        result.append("I know an old lady who swallowed a ").append(ANIMALS[index]).append(".\n");
        
        // Second line is unique for each animal
        result.append(SECOND_LINES[index]);
        
        // For the horse (last verse), we're done
        if (index == ANIMALS.length - 1) {
            return result.toString();
        }
        
        // For all other verses, add the cumulative part
        for (int i = index; i > 0; i--) {
            result.append("\n");
            
            if (i == 2) {
                // Special case for the bird mentioning the spider
                result.append("She swallowed the ").append(ANIMALS[i])
                      .append(" to catch the ").append(ANIMALS[i-1])
                      .append(" that wriggled and jiggled and tickled inside her.");
            } else {
                // Standard line for other animals
                result.append("She swallowed the ").append(ANIMALS[i])
                      .append(" to catch the ").append(ANIMALS[i-1]).append(".");
            }
        }
        
        // Only add the last line for non-fly verses (since for fly it's already the second line)
        if (index != 0) {
            result.append("\n").append(SECOND_LINES[0]);
        }
        
        return result.toString();
    }

    String verses(int startVerse, int endVerse) {
        StringBuilder result = new StringBuilder();
        
        for (int i = startVerse; i <= endVerse; i++) {
            result.append(verse(i));
            
            // Add a blank line between verses, but not after the last one
            if (i < endVerse) {
                result.append("\n\n");
            }
        }
        
        return result.toString();
    }
}