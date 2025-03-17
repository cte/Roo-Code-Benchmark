use std::collections::HashMap;

pub struct CodonsInfo<'a> {
    codon_to_name: HashMap<&'a str, &'a str>,
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub struct Error;

impl<'a> CodonsInfo<'a> {
    pub fn name_for(&self, codon: &str) -> Result<&'a str, Error> {
        // Validate codon length
        if codon.len() != 3 {
            return Err(Error);
        }

        // Check if the codon is in our direct mapping
        if let Some(name) = self.codon_to_name.get(codon) {
            return Ok(name);
        }

        // If the codon contains shorthand, we need to expand it and check all possibilities
        if !codon.chars().all(|c| c == 'A' || c == 'C' || c == 'G' || c == 'T') {
            // Find all possible expansions of the shorthand codon
            let expanded_codons = expand_shorthand_codon(codon)?;
            
            // All expanded codons should map to the same amino acid
            // So we just need to find one that exists in our mapping
            for expanded in expanded_codons {
                if let Some(name) = self.codon_to_name.get(expanded.as_str()) {
                    return Ok(name);
                }
            }
        }

        // If we get here, the codon is invalid
        Err(Error)
    }

    pub fn of_rna(&self, rna: &str) -> Result<Vec<&'a str>, Error> {
        // RNA must be a multiple of 3 in length
        if rna.len() % 3 != 0 {
            return Err(Error);
        }

        let mut result = Vec::new();
        
        // Process each codon (3 nucleotides) in the RNA string
        for i in (0..rna.len()).step_by(3) {
            if i + 3 > rna.len() {
                return Err(Error);
            }
            
            let codon = &rna[i..i+3];
            let name = self.name_for(codon)?;
            
            // Stop translation if we encounter a stop codon
            if name == "stop codon" {
                break;
            }
            
            result.push(name);
        }
        
        Ok(result)
    }
}

pub fn parse<'a>(pairs: Vec<(&'a str, &'a str)>) -> CodonsInfo<'a> {
    let mut codon_to_name = HashMap::new();
    
    for (codon, name) in pairs {
        codon_to_name.insert(codon, name);
    }
    
    CodonsInfo { codon_to_name }
}

// Function to expand a shorthand codon into all possible standard codons
fn expand_shorthand_codon(codon: &str) -> Result<Vec<String>, Error> {
    if codon.len() != 3 {
        return Err(Error);
    }
    
    let mut result = vec![String::new()];
    
    for c in codon.chars() {
        let nucleotides = match c {
            'A' => vec!['A'],
            'C' => vec!['C'],
            'G' => vec!['G'],
            'T' => vec!['T'],
            'R' => vec!['A', 'G'],           // Purine (A or G)
            'Y' => vec!['C', 'T'],           // Pyrimidine (C or T)
            'M' => vec!['A', 'C'],           // Amino (A or C)
            'K' => vec!['G', 'T'],           // Keto (G or T)
            'S' => vec!['C', 'G'],           // Strong (C or G)
            'W' => vec!['A', 'T'],           // Weak (A or T)
            'H' => vec!['A', 'C', 'T'],      // Not G
            'B' => vec!['C', 'G', 'T'],      // Not A
            'V' => vec!['A', 'C', 'G'],      // Not T
            'D' => vec!['A', 'G', 'T'],      // Not C
            'N' => vec!['A', 'C', 'G', 'T'], // Any
            _ => return Err(Error),          // Invalid character
        };
        
        let mut new_result = Vec::new();
        for s in result {
            for &n in &nucleotides {
                let mut new_s = s.clone();
                new_s.push(n);
                new_result.push(new_s);
            }
        }
        
        result = new_result;
    }
    
    Ok(result)
}
