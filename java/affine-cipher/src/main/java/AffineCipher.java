public class AffineCipher {
    
    private static final int ALPHABET_SIZE = 26;
    private static final int GROUP_SIZE = 5;
    
    public String encode(String text, int keyA, int keyB) {
        // Check if keyA and alphabet size are coprime
        if (!areCoprime(keyA, ALPHABET_SIZE)) {
            throw new IllegalArgumentException("Error: keyA and alphabet size must be coprime.");
        }
        
        // Normalize the text (lowercase, remove punctuation)
        String normalizedText = text.toLowerCase().replaceAll("[^a-z0-9]", "");
        
        StringBuilder encoded = new StringBuilder();
        int count = 0;
        
        for (char c : normalizedText.toCharArray()) {
            if (Character.isDigit(c)) {
                // Digits remain unchanged
                if (count > 0 && count % GROUP_SIZE == 0) {
                    encoded.append(" ");
                }
                encoded.append(c);
                count++;
            } else if (Character.isLetter(c)) {
                // Apply the encryption formula: E(x) = (ax + b) mod m
                int x = c - 'a'; // Convert to 0-25
                int encryptedValue = (keyA * x + keyB) % ALPHABET_SIZE;
                char encryptedChar = (char) ('a' + encryptedValue);
                
                // Add space every 5 characters
                if (count > 0 && count % GROUP_SIZE == 0) {
                    encoded.append(" ");
                }
                
                encoded.append(encryptedChar);
                count++;
            }
        }
        
        return encoded.toString();
    }
    
    public String decode(String text, int keyA, int keyB) {
        // Check if keyA and alphabet size are coprime
        if (!areCoprime(keyA, ALPHABET_SIZE)) {
            throw new IllegalArgumentException("Error: keyA and alphabet size must be coprime.");
        }
        
        // Find the modular multiplicative inverse of keyA
        int mmi = findModularMultiplicativeInverse(keyA, ALPHABET_SIZE);
        
        // Remove all spaces
        String normalizedText = text.replaceAll("\\s+", "");
        
        StringBuilder decoded = new StringBuilder();
        
        for (char c : normalizedText.toCharArray()) {
            if (Character.isDigit(c)) {
                // Digits remain unchanged
                decoded.append(c);
            } else if (Character.isLetter(c)) {
                // Apply the decryption formula: D(y) = a^-1 * (y - b) mod m
                int y = c - 'a'; // Convert to 0-25
                
                // Calculate (y - b) and ensure it's positive
                int yMinusB = (y - keyB) % ALPHABET_SIZE;
                if (yMinusB < 0) {
                    yMinusB += ALPHABET_SIZE;
                }
                
                int decryptedValue = (mmi * yMinusB) % ALPHABET_SIZE;
                char decryptedChar = (char) ('a' + decryptedValue);
                
                decoded.append(decryptedChar);
            }
        }
        
        return decoded.toString();
    }
    
    // Check if two numbers are coprime (their greatest common divisor is 1)
    private boolean areCoprime(int a, int b) {
        return gcd(a, b) == 1;
    }
    
    // Calculate the greatest common divisor using Euclidean algorithm
    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    
    // Find the modular multiplicative inverse using the extended Euclidean algorithm
    private int findModularMultiplicativeInverse(int a, int m) {
        // We need to find x such that (a * x) % m = 1
        
        int m0 = m;
        int y = 0, x = 1;
        
        if (m == 1) {
            return 0;
        }
        
        while (a > 1) {
            // q is quotient
            int q = a / m;
            int t = m;
            
            // m is remainder now, process same as Euclid's algorithm
            m = a % m;
            a = t;
            t = y;
            
            // Update y and x
            y = x - q * y;
            x = t;
        }
        
        // Make x positive
        if (x < 0) {
            x += m0;
        }
        
        return x;
    }
}