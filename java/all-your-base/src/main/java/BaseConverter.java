class BaseConverter {
    private final int originalBase;
    private final int[] originalDigits;
    private final int decimalValue;

    BaseConverter(int originalBase, int[] originalDigits) {
        if (originalBase < 2) {
            throw new IllegalArgumentException("Bases must be at least 2.");
        }
        
        // Validate digits
        for (int digit : originalDigits) {
            if (digit < 0) {
                throw new IllegalArgumentException("Digits may not be negative.");
            }
            if (digit >= originalBase) {
                throw new IllegalArgumentException("All digits must be strictly less than the base.");
            }
        }
        
        this.originalBase = originalBase;
        this.originalDigits = originalDigits;
        
        // Convert to decimal value for later use
        this.decimalValue = toDecimal();
    }
    
    // Convert original digits to decimal (base 10) value
    private int toDecimal() {
        int result = 0;
        
        for (int digit : originalDigits) {
            result = result * originalBase + digit;
        }
        
        return result;
    }

    int[] convertToBase(int newBase) {
        if (newBase < 2) {
            throw new IllegalArgumentException("Bases must be at least 2.");
        }
        
        // Handle special cases
        if (decimalValue == 0) {
            return new int[]{0};
        }
        
        // Convert decimal value to the new base
        int value = decimalValue;
        java.util.ArrayList<Integer> digits = new java.util.ArrayList<>();
        
        while (value > 0) {
            digits.add(0, value % newBase);
            value /= newBase;
        }
        
        // Convert ArrayList to int array
        int[] result = new int[digits.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = digits.get(i);
        }
        
        return result;
    }
}