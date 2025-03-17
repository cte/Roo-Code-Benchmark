class BottleSong {

    String recite(int startBottles, int takeDown) {
        StringBuilder song = new StringBuilder();
        
        for (int i = 0; i < takeDown; i++) {
            int currentBottles = startBottles - i;
            int nextBottles = currentBottles - 1;
            
            // First line
            song.append(bottleText(currentBottles, true)).append(" hanging on the wall,\n");
            
            // Second line
            song.append(bottleText(currentBottles, true)).append(" hanging on the wall,\n");
            
            // Third line
            song.append("And if one green bottle should accidentally fall,\n");
            
            // Fourth line
            song.append("There'll be ").append(bottleText(nextBottles, false)).append(" hanging on the wall.\n");
            
            // Add blank line between verses, except for the last verse
            if (i < takeDown - 1) {
                song.append("\n");
            }
        }
        
        return song.toString();
    }
    
    private String bottleText(int count, boolean capitalized) {
        if (count == 0) {
            return "no green bottles";
        } else if (count == 1) {
            return capitalized ? "One green bottle" : "one green bottle";
        } else {
            String number = capitalized ? capitalize(numberToWord(count)) : numberToWord(count);
            return number + " green bottles";
        }
    }
    
    private String capitalize(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
    
    private String numberToWord(int number) {
        switch (number) {
            case 0: return "no";
            case 1: return "one";
            case 2: return "two";
            case 3: return "three";
            case 4: return "four";
            case 5: return "five";
            case 6: return "six";
            case 7: return "seven";
            case 8: return "eight";
            case 9: return "nine";
            case 10: return "ten";
            default: return String.valueOf(number);
        }
    }
}