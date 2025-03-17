#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    IncompleteNumber,
}

/// Convert a list of numbers to a stream of bytes encoded with variable length encoding.
pub fn to_bytes(values: &[u32]) -> Vec<u8> {
    let mut result = Vec::new();
    
    for &value in values {
        if value == 0 {
            // Special case for zero
            result.push(0);
            continue;
        }
        
        let mut bytes = Vec::new();
        let mut val = value;
        
        // Break the number into 7-bit chunks
        while val > 0 {
            // Take the 7 least significant bits
            let mut byte = (val & 0x7F) as u8;
            val >>= 7;
            
            // Set the continuation bit for all bytes except the first one we process
            // (which will be the last one in the output)
            if !bytes.is_empty() {
                byte |= 0x80; // Set the 8th bit
            }
            
            bytes.push(byte);
        }
        
        // Reverse the bytes (since we processed from least to most significant)
        bytes.reverse();
        
        // Add the bytes to the result
        result.extend(bytes);
    }
    
    result
}

/// Given a stream of bytes, extract all numbers which are encoded in there.
pub fn from_bytes(bytes: &[u8]) -> Result<Vec<u32>, Error> {
    let mut result = Vec::new();
    let mut i = 0;
    
    while i < bytes.len() {
        let mut value: u32 = 0;
        let mut continuation = true;
        
        // Process bytes until we find one without the continuation bit set
        while continuation && i < bytes.len() {
            let byte = bytes[i];
            i += 1;
            
            // Check if the continuation bit is set
            continuation = (byte & 0x80) != 0;
            
            // Add the 7 bits of data to our value
            value = (value << 7) | (byte & 0x7F) as u32;
            
            // If this is the last byte in the sequence (no continuation bit)
            // or we've reached the end of the input, add the value to the result
            if !continuation {
                result.push(value);
            }
        }
        
        // If we've processed all bytes but the last one had a continuation bit set,
        // we have an incomplete number
        if continuation {
            return Err(Error::IncompleteNumber);
        }
    }
    
    Ok(result)
}
