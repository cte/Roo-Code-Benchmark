use std::collections::HashMap;

/// Count occurrences of words.
pub fn word_count(words: &str) -> HashMap<String, u32> {
    let mut word_counts = HashMap::new();
    
    // Convert to lowercase
    let lowercase_words = words.to_lowercase();
    
    // Use a regular expression to match words
    // A word is:
    // - One or more alphanumeric characters
    // - Optionally followed by an apostrophe and more letters (for contractions)
    let mut current_word = String::new();
    let mut in_word = false;
    
    for c in lowercase_words.chars() {
        if c.is_alphanumeric() {
            // If it's a letter or number, add it to the current word
            current_word.push(c);
            in_word = true;
        } else if c == '\'' && in_word {
            // If it's an apostrophe and we're in a word, it might be a contraction
            // Add it to the current word
            current_word.push(c);
        } else if in_word {
            // If we were in a word and hit a non-alphanumeric character that's not an apostrophe,
            // the word is complete
            
            // Check if the word ends with an apostrophe (not a contraction)
            if current_word.ends_with('\'') && !current_word.ends_with("'s") {
                current_word.pop();
            }
            
            // Only add non-empty words
            if !current_word.is_empty() {
                *word_counts.entry(current_word.clone()).or_insert(0) += 1;
            }
            
            current_word.clear();
            in_word = false;
        }
    }
    
    // Don't forget to add the last word if we were in one
    if in_word && !current_word.is_empty() {
        // Check if the word ends with an apostrophe (not a contraction)
        if current_word.ends_with('\'') && !current_word.ends_with("'s") {
            current_word.pop();
        }
        
        *word_counts.entry(current_word).or_insert(0) += 1;
    }
    
    word_counts
}
