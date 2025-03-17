package protein

import (
	"errors"
)

// ErrStop indicates that a stop codon was encountered
var ErrStop = errors.New("stop codon")

// ErrInvalidBase indicates that an invalid codon was encountered
var ErrInvalidBase = errors.New("invalid codon")

// codonMap maps codons to their corresponding proteins
var codonMap = map[string]string{
	"AUG": "Methionine",
	"UUU": "Phenylalanine",
	"UUC": "Phenylalanine",
	"UUA": "Leucine",
	"UUG": "Leucine",
	"UCU": "Serine",
	"UCC": "Serine",
	"UCA": "Serine",
	"UCG": "Serine",
	"UAU": "Tyrosine",
	"UAC": "Tyrosine",
	"UGU": "Cysteine",
	"UGC": "Cysteine",
	"UGG": "Tryptophan",
}

// stopCodons is a set of codons that terminate translation
var stopCodons = map[string]bool{
	"UAA": true,
	"UAG": true,
	"UGA": true,
}

// FromCodon translates a codon to its corresponding protein
func FromCodon(codon string) (string, error) {
	// Check if it's a stop codon
	if stopCodons[codon] {
		return "", ErrStop
	}

	// Check if it's a valid codon
	protein, ok := codonMap[codon]
	if !ok {
		return "", ErrInvalidBase
	}

	return protein, nil
}

// FromRNA translates an RNA sequence to a list of proteins
func FromRNA(rna string) ([]string, error) {
	var proteins []string

	// Process the RNA sequence in groups of 3 (codons)
	for i := 0; i < len(rna); i += 3 {
		// Check if we have a complete codon
		if i+3 > len(rna) {
			return nil, ErrInvalidBase
		}

		codon := rna[i : i+3]
		protein, err := FromCodon(codon)

		// If we encounter a stop codon, stop translation
		if err == ErrStop {
			break
		}

		// If we encounter an invalid codon, return an error
		if err != nil {
			return nil, err
		}

		proteins = append(proteins, protein)
	}

	return proteins, nil
}
