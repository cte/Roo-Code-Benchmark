use rand::distributions::{Distribution, Uniform};
use std::iter;

/// Validates if a key is valid (non-empty and contains only lowercase letters)
fn is_valid_key(key: &str) -> bool {
    !key.is_empty() && key.chars().all(|c| c.is_ascii_lowercase())
}

pub fn encode(key: &str, s: &str) -> Option<String> {
    // Validate the key
    if !is_valid_key(key) {
        return None;
    }

    // Convert the key and input string to characters
    let key_chars: Vec<char> = key.chars().collect();
    let key_len = key_chars.len();
    
    // Encode each character in the input string
    let encoded: String = s
        .chars()
        .enumerate()
        .map(|(i, c)| {
            // Only encode lowercase ASCII letters
            if c.is_ascii_lowercase() {
                // Get the corresponding key character (cycling through the key if needed)
                let key_char = key_chars[i % key_len];
                
                // Calculate the shift amount (0-25 for a-z)
                let key_shift = (key_char as u8) - b'a';
                
                // Apply the shift to the current character
                let mut new_char_code = (c as u8) + key_shift;
                
                // Handle wrapping around the alphabet
                if new_char_code > b'z' {
                    new_char_code = b'a' + (new_char_code - b'z' - 1);
                }
                
                new_char_code as char
            } else {
                // Return non-lowercase letters unchanged
                c
            }
        })
        .collect();
    
    Some(encoded)
}

pub fn decode(key: &str, s: &str) -> Option<String> {
    // Validate the key
    if !is_valid_key(key) {
        return None;
    }

    // Convert the key and input string to characters
    let key_chars: Vec<char> = key.chars().collect();
    let key_len = key_chars.len();
    
    // Decode each character in the input string
    let decoded: String = s
        .chars()
        .enumerate()
        .map(|(i, c)| {
            // Only decode lowercase ASCII letters
            if c.is_ascii_lowercase() {
                // Get the corresponding key character (cycling through the key if needed)
                let key_char = key_chars[i % key_len];
                
                // Calculate the shift amount (0-25 for a-z)
                let key_shift = (key_char as u8) - b'a';
                
                // Apply the reverse shift to the current character
                let mut new_char_code = (c as u8).wrapping_sub(key_shift);
                
                // Handle wrapping around the alphabet
                if new_char_code < b'a' {
                    new_char_code = b'z' - (b'a' - new_char_code - 1);
                }
                
                new_char_code as char
            } else {
                // Return non-lowercase letters unchanged
                c
            }
        })
        .collect();
    
    Some(decoded)
}

pub fn encode_random(s: &str) -> (String, String) {
    // Generate a random key of at least 100 lowercase characters
    let mut rng = rand::thread_rng();
    let char_range = Uniform::from(0..26);
    
    // Determine the key length (at least 100, but can be longer if the input is longer)
    let key_length = std::cmp::max(100, s.len());
    
    // Generate the random key
    let key: String = iter::repeat_with(|| {
        let char_code = char_range.sample(&mut rng) as u8 + b'a';
        char_code as char
    })
    .take(key_length)
    .collect();
    
    // Encode the input string with the random key
    let encoded = encode(&key, s).unwrap();
    
    (key, encoded)
}
