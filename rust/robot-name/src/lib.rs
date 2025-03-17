use once_cell::sync::Lazy;
use rand::Rng;
use std::collections::HashSet;
use std::sync::Mutex;

// Static set to track all used names and ensure uniqueness
static USED_NAMES: Lazy<Mutex<HashSet<String>>> = Lazy::new(|| Mutex::new(HashSet::new()));

pub struct Robot {
    name: String,
}

impl Robot {
    pub fn new() -> Self {
        let name = Self::generate_unique_name();
        Robot { name }
    }

    pub fn name(&self) -> &str {
        &self.name
    }

    pub fn reset_name(&mut self) {
        // Generate a new unique name
        let new_name = Self::generate_unique_name();
        
        // Remove the old name from the set of used names
        let mut used_names = USED_NAMES.lock().unwrap();
        used_names.remove(&self.name);
        
        // Update the robot's name
        self.name = new_name;
    }

    // Generate a random name that follows the pattern: 2 uppercase letters + 3 digits
    fn generate_random_name() -> String {
        let mut rng = rand::thread_rng();
        
        // Generate 2 random uppercase letters (ASCII 'A' to 'Z' is 65 to 90)
        let letter1 = (rng.gen_range(65..=90) as u8) as char;
        let letter2 = (rng.gen_range(65..=90) as u8) as char;
        
        // Generate 3 random digits
        let digit1 = rng.gen_range(0..=9);
        let digit2 = rng.gen_range(0..=9);
        let digit3 = rng.gen_range(0..=9);
        
        format!("{}{}{}{}{}", letter1, letter2, digit1, digit2, digit3)
    }

    // Generate a unique name by checking against the set of used names
    fn generate_unique_name() -> String {
        let mut used_names = USED_NAMES.lock().unwrap();
        
        loop {
            let name = Self::generate_random_name();
            
            // If the name is not already used, add it to the set and return it
            if !used_names.contains(&name) {
                used_names.insert(name.clone());
                return name;
            }
            
            // Otherwise, try again with a new random name
        }
    }
}
