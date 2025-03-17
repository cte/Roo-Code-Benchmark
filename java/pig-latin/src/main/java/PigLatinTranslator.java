class PigLatinTranslator {
    public String translate(String phrase) {
        // Split the input phrase into words
        String[] words = phrase.split(" ");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                result.append(" ");
            }
            result.append(translateWord(words[i]));
        }
        
        return result.toString();
    }
    
    private String translateWord(String word) {
        // Rule 1: Word begins with vowel or starts with "xr" or "yt"
        if (startsWithVowel(word) || word.startsWith("xr") || word.startsWith("yt")) {
            return word + "ay";
        }
        
        // Rule 3: Word starts with consonants followed by "qu"
        int quIndex = word.indexOf("qu");
        if (quIndex >= 0 && allConsonantsBefore(word, quIndex)) {
            return word.substring(quIndex + 2) + word.substring(0, quIndex + 2) + "ay";
        }
        
        // Rule 4: Word starts with consonants followed by "y"
        int yIndex = word.indexOf('y');
        if (yIndex > 0 && allConsonantsBefore(word, yIndex)) {
            return word.substring(yIndex) + word.substring(0, yIndex) + "ay";
        }
        
        // Rule 2: Word begins with consonants
        int firstVowelIndex = findFirstVowelIndex(word);
        if (firstVowelIndex > 0) {
            return word.substring(firstVowelIndex) + word.substring(0, firstVowelIndex) + "ay";
        }
        
        // Default case (should not reach here if all rules are properly implemented)
        return word + "ay";
    }
    
    private boolean startsWithVowel(String word) {
        if (word.isEmpty()) return false;
        char firstChar = Character.toLowerCase(word.charAt(0));
        return firstChar == 'a' || firstChar == 'e' || firstChar == 'i' ||
               firstChar == 'o' || firstChar == 'u';
    }
    
    private boolean isVowel(char c) {
        c = Character.toLowerCase(c);
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
    }
    
    private int findFirstVowelIndex(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (isVowel(word.charAt(i))) {
                return i;
            }
        }
        return -1; // No vowels found
    }
    
    private boolean allConsonantsBefore(String word, int index) {
        for (int i = 0; i < index; i++) {
            if (isVowel(word.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}