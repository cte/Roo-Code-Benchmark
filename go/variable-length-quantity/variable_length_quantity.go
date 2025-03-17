package variablelengthquantity

import (
	"errors"
)

// EncodeVarint encodes a slice of uint32 values into a VLQ byte slice
func EncodeVarint(input []uint32) []byte {
	var result []byte

	for _, num := range input {
		// Handle the special case of 0
		if num == 0 {
			result = append(result, 0)
			continue
		}

		// Encode the number in reverse order first
		var bytes []byte
		for num > 0 {
			// Take the 7 least significant bits
			b := byte(num & 0x7F)
			num >>= 7 // Shift right by 7 bits

			// Set the continuation bit for all but the first byte (which will be the last when reversed)
			if len(bytes) > 0 {
				b |= 0x80 // Set the MSB (continuation bit)
			}

			bytes = append(bytes, b)
		}

		// Reverse the bytes to get the correct order
		for i := len(bytes) - 1; i >= 0; i-- {
			result = append(result, bytes[i])
		}
	}

	return result
}

// DecodeVarint decodes a VLQ byte slice into a slice of uint32 values
func DecodeVarint(input []byte) ([]uint32, error) {
	var result []uint32
	var current uint32
	var complete bool

	for i := 0; i < len(input); i++ {
		b := input[i]
		
		// Check if this is a continuation byte (MSB set)
		isContinuation := (b & 0x80) != 0
		
		// Extract the 7 data bits
		value := uint32(b & 0x7F)
		
		// Shift the current value and add the new bits
		current = (current << 7) | value
		
		if !isContinuation {
			// This is the last byte of the current number
			result = append(result, current)
			current = 0
			complete = true
		} else {
			complete = false
		}
	}

	// If the last sequence was incomplete, return an error
	if !complete {
		return nil, errors.New("incomplete sequence")
	}

	return result, nil
}
