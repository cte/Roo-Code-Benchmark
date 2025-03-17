// The code below is a stub. Just enough to satisfy the compiler.
// In order to pass the tests you can add-to or change any of this code.

#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    InvalidRowCount(usize),
    InvalidColumnCount(usize),
}

pub fn convert(input: &str) -> Result<String, Error> {
    // Split the input into lines
    let lines: Vec<&str> = input.lines().collect();
    
    // Check if the number of lines is a multiple of 4
    if lines.len() % 4 != 0 {
        return Err(Error::InvalidRowCount(lines.len()));
    }
    
    // Check if all lines have the same length and it's a multiple of 3
    if let Some(first_line) = lines.first() {
        let line_length = first_line.len();
        
        // Check if all lines have the same length
        if lines.iter().any(|line| line.len() != line_length) {
            return Err(Error::InvalidColumnCount(0)); // Invalid line length
        }
        
        // Check if the line length is a multiple of 3
        if line_length % 3 != 0 {
            return Err(Error::InvalidColumnCount(line_length));
        }
    }
    
    // Process each group of 4 lines (representing a row of digits)
    let mut result = Vec::new();
    
    for chunk_index in 0..(lines.len() / 4) {
        let start_idx = chunk_index * 4;
        let chunk = &lines[start_idx..start_idx + 4];
        
        let row_result = process_row(chunk)?;
        result.push(row_result);
    }
    
    // Join the results with commas
    Ok(result.join(","))
}

fn process_row(lines: &[&str]) -> Result<String, Error> {
    let line_length = lines[0].len();
    let num_digits = line_length / 3;
    let mut result = String::new();
    
    for digit_idx in 0..num_digits {
        let start_col = digit_idx * 3;
        let digit_pattern = extract_digit_pattern(lines, start_col);
        let digit = recognize_digit(&digit_pattern);
        result.push(digit);
    }
    
    Ok(result)
}

fn extract_digit_pattern(lines: &[&str], start_col: usize) -> Vec<String> {
    let mut pattern = Vec::new();
    
    for line in lines.iter().take(3) {  // Only take the first 3 lines, 4th is always blank
        let end_col = (start_col + 3).min(line.len());
        let segment = if start_col < line.len() {
            &line[start_col..end_col]
        } else {
            ""
        };
        pattern.push(segment.to_string());
    }
    
    pattern
}

fn recognize_digit(pattern: &[String]) -> char {
    match pattern {
        // 0
        [top, middle, bottom] if top == " _ " && middle == "| |" && bottom == "|_|" => '0',
        // 1
        [top, middle, bottom] if top == "   " && middle == "  |" && bottom == "  |" => '1',
        // 2
        [top, middle, bottom] if top == " _ " && middle == " _|" && bottom == "|_ " => '2',
        // 3
        [top, middle, bottom] if top == " _ " && middle == " _|" && bottom == " _|" => '3',
        // 4
        [top, middle, bottom] if top == "   " && middle == "|_|" && bottom == "  |" => '4',
        // 5
        [top, middle, bottom] if top == " _ " && middle == "|_ " && bottom == " _|" => '5',
        // 6
        [top, middle, bottom] if top == " _ " && middle == "|_ " && bottom == "|_|" => '6',
        // 7
        [top, middle, bottom] if top == " _ " && middle == "  |" && bottom == "  |" => '7',
        // 8
        [top, middle, bottom] if top == " _ " && middle == "|_|" && bottom == "|_|" => '8',
        // 9
        [top, middle, bottom] if top == " _ " && middle == "|_|" && bottom == " _|" => '9',
        // Unrecognized
        _ => '?',
    }
}
