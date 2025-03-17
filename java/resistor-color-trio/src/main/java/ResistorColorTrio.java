class ResistorColorTrio {
    String label(String[] colors) {
        // Map colors to their numeric values
        int firstDigit = getColorValue(colors[0]);
        int secondDigit = getColorValue(colors[1]);
        int zeros = getColorValue(colors[2]);
        
        // Calculate the main value
        int mainValue = firstDigit * 10 + secondDigit;
        
        // Calculate the total value
        long totalValue = mainValue * (long)Math.pow(10, zeros);
        
        // Format the output with the appropriate metric prefix
        return formatOutput(totalValue);
    }
    
    private int getColorValue(String color) {
        switch (color.toLowerCase()) {
            case "black": return 0;
            case "brown": return 1;
            case "red": return 2;
            case "orange": return 3;
            case "yellow": return 4;
            case "green": return 5;
            case "blue": return 6;
            case "violet": return 7;
            case "grey": return 8;
            case "white": return 9;
            default: throw new IllegalArgumentException("Invalid color: " + color);
        }
    }
    
    private String formatOutput(long value) {
        if (value < 1000) {
            return value + " ohms";
        } else if (value < 1_000_000) {
            return (value / 1000) + " kiloohms";
        } else if (value < 1_000_000_000) {
            return (value / 1_000_000) + " megaohms";
        } else {
            return (value / 1_000_000_000) + " gigaohms";
        }
    }
}
