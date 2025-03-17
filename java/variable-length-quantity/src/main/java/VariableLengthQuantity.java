import java.util.ArrayList;
import java.util.List;

class VariableLengthQuantity {

    List<String> encode(List<Long> numbers) {
        List<String> result = new ArrayList<>();
        
        for (Long number : numbers) {
            // Handle the special case of 0
            if (number == 0) {
                result.add("0x0");
                continue;
            }
            
            List<String> encodedBytes = new ArrayList<>();
            boolean isFirstByte = true;
            
            // Process the number 7 bits at a time, starting from the least significant bits
            while (number > 0) {
                // Extract the 7 least significant bits
                long byteValue = number & 0x7F;
                
                // Set the continuation bit for all bytes except the last one
                if (!isFirstByte) {
                    byteValue |= 0x80; // Set the 8th bit (continuation bit)
                }
                
                // Convert to hex string and add to the result
                encodedBytes.add(0, String.format("0x%x", byteValue));
                
                // Shift right by 7 bits to process the next chunk
                number >>>= 7;
                isFirstByte = false;
            }
            
            // Add the encoded bytes to the result
            result.addAll(encodedBytes);
        }
        
        return result;
    }

    List<String> decode(List<Long> bytes) {
        List<String> result = new ArrayList<>();
        List<Long> currentSequence = new ArrayList<>();
        
        for (int i = 0; i < bytes.size(); i++) {
            long currentByte = bytes.get(i);
            
            // Add the current byte to the sequence
            currentSequence.add(currentByte);
            
            // Check if this is the last byte in the sequence (continuation bit not set)
            if ((currentByte & 0x80) == 0) {
                // Process the complete sequence
                result.add(decodeSequence(currentSequence));
                currentSequence.clear();
            }
        }
        
        // If there are still bytes in the current sequence, it means the sequence is incomplete
        if (!currentSequence.isEmpty()) {
            throw new IllegalArgumentException("Invalid variable-length quantity encoding");
        }
        
        return result;
    }
    
    private String decodeSequence(List<Long> sequence) {
        if (sequence.isEmpty()) {
            return "0x0";
        }
        
        long result = 0;
        
        for (int i = 0; i < sequence.size(); i++) {
            long currentByte = sequence.get(i);
            
            // For all bytes except the last one, we only care about the 7 least significant bits
            if (i < sequence.size() - 1) {
                // Check if the continuation bit is set
                if ((currentByte & 0x80) == 0) {
                    throw new IllegalArgumentException("Invalid variable-length quantity encoding");
                }
                
                // Extract the 7 least significant bits and add to the result
                result = (result << 7) | (currentByte & 0x7F);
            } else {
                // For the last byte, the continuation bit should not be set
                result = (result << 7) | (currentByte & 0x7F);
            }
        }
        
        return String.format("0x%x", result);
    }
}
