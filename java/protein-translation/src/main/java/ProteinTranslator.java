import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ProteinTranslator {
    private static final Map<String, String> CODON_TO_PROTEIN = new HashMap<>();
    
    static {
        // Initialize the codon to protein mapping
        CODON_TO_PROTEIN.put("AUG", "Methionine");
        
        CODON_TO_PROTEIN.put("UUU", "Phenylalanine");
        CODON_TO_PROTEIN.put("UUC", "Phenylalanine");
        
        CODON_TO_PROTEIN.put("UUA", "Leucine");
        CODON_TO_PROTEIN.put("UUG", "Leucine");
        
        CODON_TO_PROTEIN.put("UCU", "Serine");
        CODON_TO_PROTEIN.put("UCC", "Serine");
        CODON_TO_PROTEIN.put("UCA", "Serine");
        CODON_TO_PROTEIN.put("UCG", "Serine");
        
        CODON_TO_PROTEIN.put("UAU", "Tyrosine");
        CODON_TO_PROTEIN.put("UAC", "Tyrosine");
        
        CODON_TO_PROTEIN.put("UGU", "Cysteine");
        CODON_TO_PROTEIN.put("UGC", "Cysteine");
        
        CODON_TO_PROTEIN.put("UGG", "Tryptophan");
        
        // Stop codons
        CODON_TO_PROTEIN.put("UAA", "STOP");
        CODON_TO_PROTEIN.put("UAG", "STOP");
        CODON_TO_PROTEIN.put("UGA", "STOP");
    }

    List<String> translate(String rnaSequence) {
        List<String> proteins = new ArrayList<>();
        
        // Handle empty sequence
        if (rnaSequence.isEmpty()) {
            return proteins;
        }
        
        // Process the RNA sequence in groups of 3 (codons)
        for (int i = 0; i < rnaSequence.length(); i += 3) {
            // Check if there are enough characters left for a complete codon
            if (i + 3 > rnaSequence.length()) {
                throw new IllegalArgumentException("Invalid codon");
            }
            
            String codon = rnaSequence.substring(i, i + 3);
            
            // Check if the codon is valid
            if (!CODON_TO_PROTEIN.containsKey(codon)) {
                throw new IllegalArgumentException("Invalid codon");
            }
            
            // Get the protein for this codon
            String protein = CODON_TO_PROTEIN.get(codon);
            
            // If it's a stop codon, stop translation
            if ("STOP".equals(protein)) {
                break;
            }
            
            // Add the protein to the list
            proteins.add(protein);
        }
        
        return proteins;
    }
}
