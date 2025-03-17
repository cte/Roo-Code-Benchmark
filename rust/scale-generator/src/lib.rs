// Define the Error type for handling potential errors
#[derive(Debug)]
pub struct Error;

pub struct Scale {
    notes: Vec<String>,
}

impl Scale {
    pub fn new(tonic: &str, intervals: &str) -> Result<Scale, Error> {
        // Normalize the tonic and determine whether to use sharps or flats
        let (normalized_tonic, use_sharps) = Self::normalize_tonic(tonic)?;
        
        // Generate the chromatic scale based on the tonic
        let chromatic = Self::generate_chromatic_scale(&normalized_tonic, use_sharps);
        
        // Apply the intervals to generate the scale
        let mut notes = Vec::new();
        let mut current_index = 0;
        
        // Add the first note (tonic)
        notes.push(chromatic[current_index].clone());
        
        // Apply each interval
        for interval in intervals.chars() {
            match interval {
                'M' => current_index += 2, // Major second (whole step)
                'm' => current_index += 1, // Minor second (half step)
                'A' => current_index += 3, // Augmented second (whole step + half step)
                _ => return Err(Error),    // Invalid interval
            }
            
            // Handle wrapping around the chromatic scale
            current_index %= 12;
            
            notes.push(chromatic[current_index].clone());
        }
        
        Ok(Scale { notes })
    }

    pub fn chromatic(tonic: &str) -> Result<Scale, Error> {
        // Normalize the tonic and determine whether to use sharps or flats
        let (normalized_tonic, use_sharps) = Self::normalize_tonic(tonic)?;
        
        // Generate the chromatic scale
        let mut notes = Self::generate_chromatic_scale(&normalized_tonic, use_sharps);
        
        // Add the tonic at the end to complete the scale
        notes.push(normalized_tonic.clone());
        
        Ok(Scale { notes })
    }

    pub fn enumerate(&self) -> Vec<String> {
        self.notes.clone()
    }
    
    // Helper function to normalize the tonic and determine whether to use sharps or flats
    fn normalize_tonic(tonic: &str) -> Result<(String, bool), Error> {
        if tonic.is_empty() {
            return Err(Error);
        }
        
        // Check if the tonic is for a minor scale (starts with lowercase)
        let is_minor = tonic.chars().next().unwrap().is_lowercase();
        
        // Convert the first character to uppercase and keep the rest as is
        let mut chars = tonic.chars();
        let first_char = chars.next().unwrap().to_uppercase().to_string();
        let rest: String = chars.collect();
        let normalized = format!("{}{}", first_char, rest);
        
        // Determine whether to use sharps or flats based on the tonic
        let use_sharps = if is_minor {
            // Minor scales
            match normalized.as_str() {
                // Minor scales that use sharps
                "E" | "B" | "F#" | "C#" | "G#" | "D#" => true,
                // Minor scales that use flats
                "D" | "G" | "C" | "F" | "Bb" | "Eb" => false,
                // A minor uses neither sharps nor flats, but we'll use sharps for consistency
                "A" => true,
                _ => return Err(Error), // Invalid tonic
            }
        } else {
            // Major scales
            match normalized.as_str() {
                // Major scales that use sharps
                "G" | "D" | "A" | "E" | "B" | "F#" => true,
                // Major scales that use flats
                "F" | "Bb" | "Eb" | "Ab" | "Db" | "Gb" => false,
                // C major uses neither sharps nor flats, but we'll use sharps for consistency
                "C" => true,
                _ => return Err(Error), // Invalid tonic
            }
        };
        
        Ok((normalized, use_sharps))
    }
    
    // Helper function to generate the chromatic scale
    fn generate_chromatic_scale(tonic: &str, use_sharps: bool) -> Vec<String> {
        // Define the chromatic scale with sharps and flats
        let sharp_notes = vec!["A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"];
        let flat_notes = vec!["A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"];
        
        // Choose the appropriate scale based on whether to use sharps or flats
        let notes = if use_sharps { sharp_notes } else { flat_notes };
        
        // Find the index of the tonic in the scale
        let tonic_index = notes.iter().position(|&note| note == tonic)
            .unwrap_or_else(|| {
                // Handle special cases for enharmonic equivalents
                match tonic {
                    "Cb" => notes.iter().position(|&note| note == "B").unwrap(),
                    "B#" => notes.iter().position(|&note| note == "C").unwrap(),
                    "E#" => notes.iter().position(|&note| note == "F").unwrap(),
                    "Fb" => notes.iter().position(|&note| note == "E").unwrap(),
                    _ => 0, // Default to A if not found (should not happen)
                }
            });
        
        // Rotate the scale to start with the tonic
        let mut result = Vec::with_capacity(12);
        for i in 0..12 {
            result.push(notes[(tonic_index + i) % 12].to_string());
        }
        
        result
    }
}
