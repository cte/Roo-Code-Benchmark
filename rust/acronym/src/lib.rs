pub fn abbreviate(phrase: &str) -> String {
    // Insert space before uppercase letters that follow lowercase letters (for camelCase)
    let mut processed = String::with_capacity(phrase.len() * 2);
    let chars: Vec<char> = phrase.chars().collect();
    
    for i in 0..chars.len() {
        let current = chars[i];
        
        // Insert space before uppercase letters that follow lowercase letters
        if i > 0 && current.is_uppercase() && chars[i-1].is_lowercase() {
            processed.push(' ');
        }
        
        // Replace hyphens with spaces
        if current == '-' {
            processed.push(' ');
        } else if current.is_alphanumeric() || current.is_whitespace() {
            // Keep alphanumeric and whitespace characters
            processed.push(current);
        }
        // Skip all other punctuation
    }
    
    // Split by whitespace, take first letter of each word, convert to uppercase
    processed
        .split_whitespace()
        .filter(|word| !word.is_empty())
        .map(|word| word.chars().next().unwrap().to_ascii_uppercase())
        .collect()
}
