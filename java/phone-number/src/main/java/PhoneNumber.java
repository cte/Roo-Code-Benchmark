class PhoneNumber {
    private final String number;

    PhoneNumber(String numberString) {
        // Remove all non-digit characters
        String digitsOnly = numberString.replaceAll("[^0-9]", "");
        
        // Check for letters
        if (numberString.matches(".*[a-zA-Z].*")) {
            throw new IllegalArgumentException("letters not permitted");
        }
        
        // Check for punctuations (excluding spaces, parentheses, hyphens, dots, and plus sign)
        if (numberString.replaceAll("[0-9\\s()\\-\\.\\+]", "").length() > 0) {
            throw new IllegalArgumentException("punctuations not permitted");
        }
        
        // Validate the length
        if (digitsOnly.length() < 10) {
            throw new IllegalArgumentException("must not be fewer than 10 digits");
        } else if (digitsOnly.length() > 11) {
            throw new IllegalArgumentException("must not be greater than 11 digits");
        } else if (digitsOnly.length() == 11 && digitsOnly.charAt(0) != '1') {
            throw new IllegalArgumentException("11 digits must start with 1");
        }
        
        // Extract the 10-digit number (removing country code if present)
        String tenDigits = digitsOnly.length() == 11 ? digitsOnly.substring(1) : digitsOnly;
        
        // Validate area code
        if (tenDigits.charAt(0) == '0') {
            throw new IllegalArgumentException("area code cannot start with zero");
        } else if (tenDigits.charAt(0) == '1') {
            throw new IllegalArgumentException("area code cannot start with one");
        }
        
        // Validate exchange code
        if (tenDigits.charAt(3) == '0') {
            throw new IllegalArgumentException("exchange code cannot start with zero");
        } else if (tenDigits.charAt(3) == '1') {
            throw new IllegalArgumentException("exchange code cannot start with one");
        }
        
        this.number = tenDigits;
    }

    String getNumber() {
        return number;
    }
}