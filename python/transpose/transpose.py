def transpose(text):
    # Handle empty string case
    if not text:
        return ""
    
    # Split the input text into lines
    lines = text.split('\n')
    
    # Find the maximum line length
    max_length = max(len(line) for line in lines)
    
    # Initialize the result
    result = []
    
    # Transpose the matrix
    for i in range(max_length):
        new_line = []
        for j in range(len(lines)):
            # If the current position is within the line's length, add the character
            # Otherwise, add a space if it's not the rightmost column that needs padding
            if i < len(lines[j]):
                new_line.append(lines[j][i])
            else:
                # Only add spaces for positions that have characters below them
                # This implements the "pad to the left with spaces" rule
                should_pad = False
                for k in range(j + 1, len(lines)):
                    if i < len(lines[k]):
                        should_pad = True
                        break
                
                if should_pad:
                    new_line.append(' ')
                else:
                    # Don't add anything if there are no characters below
                    # This implements the "don't pad to the right" rule
                    break
        
        result.append(''.join(new_line))
    
    # Join the transposed lines with newlines
    return '\n'.join(result)
