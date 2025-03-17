use std::collections::{HashMap, HashSet};

pub fn solve(input: &str) -> Option<HashMap<char, u8>> {
    // Parse the equation
    let parts: Vec<&str> = input.split("==").collect();
    if parts.len() != 2 {
        return None;
    }

    let left_side = parts[0].trim();
    let right_side = parts[1].trim();

    // Extract all terms (addends and result)
    let addends: Vec<&str> = left_side.split('+').map(|s| s.trim()).collect();
    let result = right_side;

    // Collect all unique letters
    let mut all_letters = HashSet::new();
    let mut first_letters = HashSet::new();

    // Add letters from addends
    for term in &addends {
        if !term.is_empty() {
            all_letters.extend(term.chars());
            first_letters.insert(term.chars().next().unwrap());
        }
    }

    // Add letters from result
    all_letters.extend(result.chars());
    first_letters.insert(result.chars().next().unwrap());

    // Convert to a vector for indexing
    let letters: Vec<char> = all_letters.into_iter().collect();
    
    // If we have more than 10 unique letters, no solution is possible
    if letters.len() > 10 {
        return None;
    }

    // Try all possible digit assignments
    let mut solution = HashMap::new();
    if backtrack(&letters, &first_letters, &addends, result, 0, &mut solution, &mut HashSet::new()) {
        return Some(solution);
    }

    None
}

fn backtrack(
    letters: &[char],
    first_letters: &HashSet<char>,
    addends: &[&str],
    result: &str,
    index: usize,
    solution: &mut HashMap<char, u8>,
    used_digits: &mut HashSet<u8>,
) -> bool {
    // If all letters have been assigned, check if the equation is valid
    if index == letters.len() {
        return is_valid_solution(addends, result, solution);
    }

    let current_letter = letters[index];
    let is_first_letter = first_letters.contains(&current_letter);

    // Try each possible digit for the current letter
    let start_digit = if is_first_letter { 1 } else { 0 };
    
    for digit in start_digit..=9 {
        if !used_digits.contains(&digit) {
            // Assign the digit to the letter
            solution.insert(current_letter, digit);
            used_digits.insert(digit);

            // Recursively try to assign digits to the remaining letters
            if backtrack(letters, first_letters, addends, result, index + 1, solution, used_digits) {
                return true;
            }

            // Backtrack
            solution.remove(&current_letter);
            used_digits.remove(&digit);
        }
    }

    false
}

fn is_valid_solution(
    addends: &[&str],
    result: &str,
    solution: &HashMap<char, u8>,
) -> bool {
    // Convert addends to numbers
    let addend_values: Vec<u64> = addends
        .iter()
        .map(|&term| {
            let mut value = 0;
            for c in term.chars() {
                value = value * 10 + *solution.get(&c).unwrap() as u64;
            }
            value
        })
        .collect();

    // Convert result to number
    let mut result_value = 0;
    for c in result.chars() {
        result_value = result_value * 10 + *solution.get(&c).unwrap() as u64;
    }

    // Check if the sum of addends equals the result
    addend_values.iter().sum::<u64>() == result_value
}
