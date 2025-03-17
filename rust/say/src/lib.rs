pub fn encode(n: u64) -> String {
    if n == 0 {
        return "zero".to_string();
    }

    let mut result = String::new();
    let mut remaining = n;

    // Define the scale words and their corresponding values
    let scales = [
        (1_000_000_000_000_000_000, "quintillion"),
        (1_000_000_000_000_000, "quadrillion"),
        (1_000_000_000_000, "trillion"),
        (1_000_000_000, "billion"),
        (1_000_000, "million"),
        (1_000, "thousand"),
        (100, "hundred"),
    ];

    // Process each scale
    for &(scale_value, scale_name) in &scales {
        if remaining >= scale_value {
            let count = remaining / scale_value;
            remaining %= scale_value;

            // For hundreds, we don't need to process the count separately
            if scale_value == 100 {
                result.push_str(&format!("{} {}", encode_less_than_100(count), scale_name));
            } else {
                result.push_str(&format!("{} {}", encode_less_than_1000(count), scale_name));
            }

            // Add a space if there are more digits to process
            if remaining > 0 {
                result.push(' ');
            }
        }
    }

    // Process the remaining digits (less than 100)
    if remaining > 0 {
        result.push_str(&encode_less_than_100(remaining));
    }

    result
}

// Helper function to convert numbers less than 1000 to words
fn encode_less_than_1000(n: u64) -> String {
    if n < 100 {
        return encode_less_than_100(n);
    }

    let hundreds = n / 100;
    let remainder = n % 100;

    if remainder == 0 {
        format!("{} hundred", encode_less_than_100(hundreds))
    } else {
        format!("{} hundred {}", encode_less_than_100(hundreds), encode_less_than_100(remainder))
    }
}

// Helper function to convert numbers less than 100 to words
fn encode_less_than_100(n: u64) -> String {
    let ones = [
        "", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
        "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen",
    ];
    
    let tens = [
        "", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety",
    ];

    if n < 20 {
        ones[n as usize].to_string()
    } else {
        let ten = n / 10;
        let one = n % 10;

        if one == 0 {
            tens[ten as usize].to_string()
        } else {
            format!("{}-{}", tens[ten as usize], ones[one as usize])
        }
    }
}
