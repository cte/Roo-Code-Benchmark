pub struct Luhn {
    digits: Vec<u8>,
}

impl Luhn {
    pub fn is_valid(&self) -> bool {
        // A valid Luhn number must have at least 2 digits
        if self.digits.len() < 2 {
            return false;
        }

        // Apply the Luhn algorithm
        let sum: u32 = self.digits
            .iter()
            .rev()
            .enumerate()
            .map(|(i, &digit)| {
                if i % 2 == 1 {
                    // Double every second digit starting from the right
                    let doubled = digit as u32 * 2;
                    // If doubling results in a number > 9, subtract 9
                    if doubled > 9 {
                        doubled - 9
                    } else {
                        doubled
                    }
                } else {
                    digit as u32
                }
            })
            .sum();

        // If the sum is divisible by 10, the number is valid
        sum % 10 == 0
    }
}

/// Here is the example of how the From trait could be implemented
/// for the &str type. Naturally, you can implement this trait
/// by hand for every other type presented in the test suite,
/// but your solution will fail if a new type is presented.
/// Perhaps there exists a better solution for this problem?
impl From<&str> for Luhn {
    fn from(input: &str) -> Self {
        // Remove spaces and collect only digits
        let digits: Vec<u8> = input
            .chars()
            .filter(|c| !c.is_whitespace())
            .map(|c| {
                if c.is_digit(10) {
                    c.to_digit(10).unwrap() as u8
                } else {
                    // If there's a non-digit character, add an invalid digit
                    // This will make is_valid() return false
                    10
                }
            })
            .collect();

        Self { digits }
    }
}

// Implement From for String
impl From<String> for Luhn {
    fn from(input: String) -> Self {
        Self::from(input.as_str())
    }
}

// Implement From for unsigned integer types
macro_rules! impl_from_unsigned {
    ($($t:ty),*) => {
        $(
            impl From<$t> for Luhn {
                fn from(input: $t) -> Self {
                    // Convert the number to a string of digits
                    let digits: Vec<u8> = input
                        .to_string()
                        .chars()
                        .map(|c| c.to_digit(10).unwrap() as u8)
                        .collect();
                    
                    Self { digits }
                }
            }
        )*
    };
}

// Implement From for all the unsigned integer types used in the tests
impl_from_unsigned!(u8, u16, u32, u64, usize);
