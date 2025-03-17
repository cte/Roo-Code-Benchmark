import re

def answer(question):
    # Check if the question starts with "What is" and ends with "?"
    if not question.startswith("What is") or not question.endswith("?"):
        raise ValueError("unknown operation")
    
    # Extract the expression between "What is" and "?"
    expression = question[8:-1].strip()
    
    # If there's no expression, raise a syntax error
    if not expression:
        raise ValueError("syntax error")
    
    # Check for specific syntax error patterns
    
    # Pattern 1: Two numbers in a row (e.g., "2 2", "1 plus 2 1")
    # This regex looks for a number followed by another number
    if re.search(r'\b\d+\s+\d+\b', expression):
        raise ValueError("syntax error")
    
    # Pattern 2: Postfix notation (e.g., "1 2 plus")
    # This regex looks for a number followed by an operation at the end
    if re.search(r'\b\d+\s+(plus|minus|multiplied|divided)\s*$', expression):
        raise ValueError("syntax error")
    
    # Pattern 3: Prefix notation (e.g., "plus 1 2")
    # This regex looks for an operation at the beginning
    if re.search(r'^\s*(plus|minus|multiplied|divided)\s+\d+', expression):
        raise ValueError("syntax error")
    
    # Pattern 4: Missing operand (e.g., "1 plus")
    # This regex looks for an operation at the end
    if re.search(r'\b(plus|minus|multiplied|divided)(\s+by)?\s*$', expression):
        raise ValueError("syntax error")
    
    # Pattern 5: Two operations in a row (e.g., "1 plus plus 2")
    # This regex looks for an operation followed by another operation
    if re.search(r'\b(plus|minus|multiplied|divided)(\s+by)?\s+(plus|minus|multiplied|divided)\b', expression):
        raise ValueError("syntax error")
    
    # If there's only one token and it's a number, return it
    if re.match(r'^-?\d+$', expression):
        return int(expression)
    
    # Check for unknown operations
    valid_ops = ["plus", "minus", "multiplied by", "divided by"]
    tokens = expression.split()
    
    # If the first token is not a number, it's a syntax error
    try:
        result = int(tokens[0])
    except ValueError:
        raise ValueError("syntax error")
    
    i = 1
    while i < len(tokens):
        # Check for valid operations
        if tokens[i] == "plus":
            try:
                result += int(tokens[i + 1])
                i += 2
            except (IndexError, ValueError):
                raise ValueError("syntax error")
        elif tokens[i] == "minus":
            try:
                result -= int(tokens[i + 1])
                i += 2
            except (IndexError, ValueError):
                raise ValueError("syntax error")
        elif tokens[i] == "multiplied" and i + 2 < len(tokens) and tokens[i + 1] == "by":
            try:
                result *= int(tokens[i + 2])
                i += 3
            except (IndexError, ValueError):
                raise ValueError("syntax error")
        elif tokens[i] == "divided" and i + 2 < len(tokens) and tokens[i + 1] == "by":
            try:
                result //= int(tokens[i + 2])
                i += 3
            except (IndexError, ValueError):
                raise ValueError("syntax error")
        else:
            # If it's not a valid operation, it's an unknown operation
            raise ValueError("unknown operation")
    
    return result
