pub fn translate(input: &str) -> String {
    input
        .split_whitespace()
        .map(translate_word)
        .collect::<Vec<String>>()
        .join(" ")
}

fn translate_word(word: &str) -> String {
    // Special cases: words beginning with "yt" or "xr" are treated as starting with vowels
    if word.starts_with("yt") || word.starts_with("xr") {
        return format!("{}ay", word);
    }

    let chars: Vec<char> = word.chars().collect();
    
    // Check if the word starts with a vowel
    if is_vowel(chars[0]) {
        return format!("{}ay", word);
    }
    
    // Find the index of the first vowel
    let mut vowel_idx = 0;
    for (i, &c) in chars.iter().enumerate() {
        // Special case: 'qu' is treated as a single consonant sound
        if i > 0 && c == 'u' && chars[i - 1] == 'q' {
            continue;
        }
        
        // 'y' is treated as a vowel if it's not the first letter
        if c == 'y' && i > 0 {
            vowel_idx = i;
            break;
        }
        
        if is_vowel(c) {
            vowel_idx = i;
            break;
        }
    }
    
    // If no vowel is found, return the original word with "ay"
    if vowel_idx == 0 {
        return format!("{}ay", word);
    }
    
    // Special case: if 'qu' appears after the first consonant and before the first vowel
    if vowel_idx > 0 && vowel_idx < chars.len() - 1 {
        if chars[vowel_idx - 1] == 'q' && chars[vowel_idx] == 'u' {
            vowel_idx += 1;
        }
    }
    
    // Move consonants before the first vowel to the end and add "ay"
    let (first, last) = word.split_at(vowel_idx);
    format!("{}{}ay", last, first)
}

fn is_vowel(c: char) -> bool {
    matches!(c.to_ascii_lowercase(), 'a' | 'e' | 'i' | 'o' | 'u')
}
