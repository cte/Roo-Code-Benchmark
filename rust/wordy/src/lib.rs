pub fn answer(command: &str) -> Option<i32> {
    // Check if the command starts with "What is " and ends with "?"
    if !command.starts_with("What is ") || !command.ends_with("?") {
        return None;
    }

    // Extract the expression between "What is " and "?"
    let expression = &command["What is ".len()..command.len() - 1].trim();
    
    // If the expression is empty, return None
    if expression.is_empty() {
        return None;
    }

    // Split the expression into tokens
    let tokens: Vec<&str> = expression.split_whitespace().collect();
    
    // Parse the first token as a number
    let mut result = match tokens[0].parse::<i32>() {
        Ok(num) => num,
        Err(_) => return None, // First token must be a number
    };

    // Process the remaining tokens
    let mut i = 1;
    while i < tokens.len() {
        // Check if we have enough tokens for an operation
        if i + 1 >= tokens.len() {
            return None; // Missing operand
        }

        // Get the operation
        let operation = match tokens[i] {
            "plus" => "+",
            "minus" => "-",
            "multiplied" => {
                // Check for "multiplied by"
                if i + 1 < tokens.len() && tokens[i + 1] == "by" {
                    i += 1; // Skip "by"
                    "*"
                } else {
                    return None; // Invalid syntax
                }
            },
            "divided" => {
                // Check for "divided by"
                if i + 1 < tokens.len() && tokens[i + 1] == "by" {
                    i += 1; // Skip "by"
                    "/"
                } else {
                    return None; // Invalid syntax
                }
            },
            _ => return None, // Unsupported operation
        };

        // Move to the operand
        i += 1;
        
        // Check if we have a valid operand
        if i >= tokens.len() {
            return None; // Missing operand
        }

        // Parse the operand
        let operand = match tokens[i].parse::<i32>() {
            Ok(num) => num,
            Err(_) => return None, // Invalid operand
        };

        // Perform the operation
        match operation {
            "+" => result += operand,
            "-" => result -= operand,
            "*" => result *= operand,
            "/" => {
                // Check for division by zero
                if operand == 0 {
                    return None;
                }
                result /= operand;
            },
            _ => unreachable!(), // We've already validated the operation
        }

        // Move to the next token
        i += 1;
    }

    Some(result)
}
