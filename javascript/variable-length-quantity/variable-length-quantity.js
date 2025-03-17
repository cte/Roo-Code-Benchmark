/**
 * Variable Length Quantity (VLQ) encoding/decoding implementation
 * 
 * In VLQ encoding:
 * - Only the first 7 bits of each byte are used for the actual value
 * - The 8th bit (MSB) is used as a continuation flag:
 *   - If set to 1, it means there are more bytes to follow
 *   - If set to 0, it means this is the last byte in the sequence
 */

/**
 * Encodes an array of integers into VLQ format
 * @param {number[]} numbers - Array of integers to encode
 * @returns {number[]} - Array of bytes in VLQ format
 */
export const encode = (numbers) => {
  const result = [];
  
  for (const num of numbers) {
    // Handle the special case of 0
    if (num === 0) {
      result.push(0);
      continue;
    }
    
    // Convert the number to VLQ format
    const bytes = [];
    let value = num;
    
    // Extract 7 bits at a time, starting from the least significant bits
    while (value > 0) {
      // Take the 7 least significant bits
      bytes.unshift(value & 0x7F);
      // Shift right by 7 bits to process the next 7 bits
      value >>>= 7;
    }
    
    // Set the continuation bit (bit 7) for all bytes except the last one
    for (let i = 0; i < bytes.length - 1; i++) {
      bytes[i] |= 0x80; // Set the MSB to 1
    }
    
    // Add the bytes to the result
    result.push(...bytes);
  }
  
  return result;
};

/**
 * Decodes an array of bytes in VLQ format into an array of integers
 * @param {number[]} bytes - Array of bytes in VLQ format
 * @returns {number[]} - Array of decoded integers
 * @throws {Error} - If the sequence is incomplete
 */
export const decode = (bytes) => {
  const result = [];
  let currentValue = 0;
  let i = 0;
  
  while (i < bytes.length) {
    // Get the current byte
    const byte = bytes[i];
    
    // Extract the 7 least significant bits and add them to the current value
    // Use unsigned right shift to ensure we're treating numbers as unsigned
    currentValue = ((currentValue & 0xFFFFFFF) << 7) | (byte & 0x7F);
    
    // Check if this is the last byte in the sequence (bit 7 is 0)
    if ((byte & 0x80) === 0) {
      // End of sequence, add the value to the result
      result.push(currentValue >>> 0); // Ensure unsigned 32-bit integer
      currentValue = 0;
    } else if (i === bytes.length - 1) {
      // If this is the last byte in the array but the continuation bit is set,
      // then the sequence is incomplete
      throw new Error('Incomplete sequence');
    }
    
    i++;
  }
  
  return result;
};
