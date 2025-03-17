def gcd(a, b):
    """Calculate the greatest common divisor of a and b."""
    while b:
        a, b = b, a % b
    return a

def is_coprime(a, m):
    """Check if a and m are coprime (gcd is 1)."""
    return gcd(a, m) == 1

def mmi(a, m):
    """Find the modular multiplicative inverse of a mod m."""
    for i in range(1, m):
        if (a * i) % m == 1:
            return i
    return None

def encode(plain_text, a, b):
    """
    Encode text using the affine cipher with key (a, b).
    
    E(x) = (a*i + b) mod m
    where i is the letter's index (0-25), m is the alphabet length (26)
    """
    m = 26  # Length of the alphabet
    
    # Check if a and m are coprime
    if not is_coprime(a, m):
        raise ValueError("a and m must be coprime.")
    
    # Normalize the input text
    plain_text = plain_text.lower()
    
    # Encode each character
    encoded_chars = []
    for char in plain_text:
        if char.isalpha():
            # Convert letter to index (0-25)
            i = ord(char) - ord('a')
            # Apply encryption formula
            encrypted_index = (a * i + b) % m
            # Convert back to letter
            encoded_chars.append(chr(encrypted_index + ord('a')))
        elif char.isdigit():
            # Keep digits as is
            encoded_chars.append(char)
        # Ignore spaces and punctuation
    
    # Join and group into chunks of 5
    encoded_text = ''.join(encoded_chars)
    grouped_text = ' '.join([encoded_text[i:i+5] for i in range(0, len(encoded_text), 5)])
    
    return grouped_text

def decode(ciphered_text, a, b):
    """
    Decode text using the affine cipher with key (a, b).
    
    D(y) = (a^-1)*(y - b) mod m
    where y is the encrypted letter's index, a^-1 is the modular multiplicative inverse
    """
    m = 26  # Length of the alphabet
    
    # Check if a and m are coprime
    if not is_coprime(a, m):
        raise ValueError("a and m must be coprime.")
    
    # Find the modular multiplicative inverse of a
    a_inv = mmi(a, m)
    
    # Remove spaces from ciphertext
    ciphered_text = ciphered_text.replace(" ", "")
    
    # Decode each character
    decoded_chars = []
    for char in ciphered_text:
        if char.isalpha():
            # Convert letter to index (0-25)
            y = ord(char) - ord('a')
            # Apply decryption formula
            decrypted_index = (a_inv * (y - b)) % m
            # Convert back to letter
            decoded_chars.append(chr(decrypted_index + ord('a')))
        elif char.isdigit():
            # Keep digits as is
            decoded_chars.append(char)
    
    return ''.join(decoded_chars)
