def encode(numbers):
    """
    Encode a list of integers using Variable Length Quantity (VLQ) encoding.
    
    In VLQ encoding:
    - Only the first 7 bits of each byte are used for data (right-justified)
    - The 8th bit (MSB) is a continuation flag:
      - If MSB is 1, there are more bytes to follow for this number
      - If MSB is 0, this is the last byte for this number
    
    Args:
        numbers: A list of integers to encode
        
    Returns:
        A list of bytes representing the VLQ encoding of the input numbers
    """
    result = []
    
    for number in numbers:
        # Handle the special case of 0
        if number == 0:
            result.append(0)
            continue
            
        # Store the bytes for the current number
        bytes_for_number = []
        
        # Extract 7 bits at a time, starting from the least significant bits
        while number > 0:
            # Get the 7 least significant bits
            byte = number & 0x7F
            number >>= 7
            
            # Add the continuation bit (MSB) for all bytes except the first one we process
            # (which will be the last one in the final sequence)
            if bytes_for_number:
                byte |= 0x80
                
            bytes_for_number.append(byte)
        
        # Reverse the bytes since we processed them from least to most significant
        result.extend(reversed(bytes_for_number))
    
    return result


def decode(bytes_):
    """
    Decode a list of bytes encoded using Variable Length Quantity (VLQ) encoding.
    
    Args:
        bytes_: A list of bytes in VLQ encoding
        
    Returns:
        A list of integers decoded from the input bytes
        
    Raises:
        ValueError: If the sequence is incomplete (i.e., the last byte has MSB=1)
    """
    result = []
    current_number = 0
    
    i = 0
    while i < len(bytes_):
        # Extract the current byte
        byte = bytes_[i]
        
        # Extract the 7 data bits and add them to the current number
        current_number = (current_number << 7) | (byte & 0x7F)
        
        # Check if this is the last byte for the current number
        if not (byte & 0x80):
            # This is the last byte, so add the number to the result and reset
            result.append(current_number)
            current_number = 0
        
        i += 1
    
    # If we've processed all bytes but still have a number being decoded,
    # the sequence is incomplete
    if current_number != 0 or (bytes_ and bytes_[-1] & 0x80):
        raise ValueError("incomplete sequence")
    
    return result
