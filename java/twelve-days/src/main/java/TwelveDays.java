class TwelveDays {
    private static final String[] ORDINALS = {
        "first", "second", "third", "fourth", "fifth", "sixth",
        "seventh", "eighth", "ninth", "tenth", "eleventh", "twelfth"
    };
    
    private static final String[] GIFTS = {
        "twelve Drummers Drumming",
        "eleven Pipers Piping",
        "ten Lords-a-Leaping",
        "nine Ladies Dancing",
        "eight Maids-a-Milking",
        "seven Swans-a-Swimming",
        "six Geese-a-Laying",
        "five Gold Rings",
        "four Calling Birds",
        "three French Hens",
        "two Turtle Doves",
        "a Partridge in a Pear Tree"
    };
    
    String verse(int verseNumber) {
        StringBuilder verse = new StringBuilder();
        
        // Add the beginning of the verse
        verse.append(String.format("On the %s day of Christmas my true love gave to me: ", 
                ORDINALS[verseNumber - 1]));
        
        // Add the gifts
        for (int i = 12 - verseNumber; i < 11; i++) {
            verse.append(GIFTS[i]).append(", ");
        }
        
        // Add the last gift (with "and" if not the first verse)
        if (verseNumber > 1) {
            verse.append("and ");
        }
        verse.append(GIFTS[11]).append(".\n");
        
        return verse.toString();
    }
    
    String verses(int startVerse, int endVerse) {
        StringBuilder verses = new StringBuilder();
        
        for (int i = startVerse; i <= endVerse; i++) {
            verses.append(verse(i));
            
            // Add a blank line between verses, but not after the last verse
            if (i < endVerse) {
                verses.append("\n");
            }
        }
        
        return verses.toString();
    }
    
    String sing() {
        return verses(1, 12);
    }
}
