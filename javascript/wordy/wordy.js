/**
 * Parses and evaluates mathematical expressions given in natural language.
 * @param {string} question - The mathematical question to evaluate
 * @returns {number} - The result of the evaluation
 */
export const answer = (question) => {
  // Check if the question follows the expected format
  if (!question.startsWith('What is') || !question.endsWith('?')) {
    throw new Error('Unknown operation');
  }

  // Extract the mathematical expression
  const expression = question.slice(8, -1).trim();
  
  // Handle the case where there's no expression
  if (expression === '') {
    throw new Error('Syntax error');
  }

  // Tokenize the expression
  const tokens = expression.split(' ');
  
  // Parse the expression
  const { result, error } = parseExpression(tokens);
  
  if (error) {
    throw new Error(error);
  }
  
  return result;
};

/**
 * Parses a mathematical expression given as tokens
 * @param {string[]} tokens - The tokens of the expression
 * @returns {Object} - The result and any error
 */
function parseExpression(tokens) {
  // Check if the first token is a number
  if (tokens.length === 0 || !isValidNumber(tokens[0])) {
    return { error: 'Syntax error' };
  }
  
  let result = parseInt(tokens[0]);
  let i = 1;
  
  // Expected pattern: number operation number operation number...
  // So we should alternate between operations and numbers
  let expectingOperation = true;
  
  // Special case for postfix notation: "What is 1 2 plus?"
  // Check if the second token is a number
  if (tokens.length > 1 && isValidNumber(tokens[1])) {
    return { error: 'Syntax error' };
  }
  
  while (i < tokens.length) {
    if (expectingOperation) {
      // We expect an operation here
      let operation;
      
      if (tokens[i] === 'plus') {
        operation = 'plus';
        i += 1;
      } else if (tokens[i] === 'minus') {
        operation = 'minus';
        i += 1;
      } else if (i + 1 < tokens.length && tokens[i] === 'multiplied' && tokens[i + 1] === 'by') {
        operation = 'multiplied by';
        i += 2;
      } else if (i + 1 < tokens.length && tokens[i] === 'divided' && tokens[i + 1] === 'by') {
        operation = 'divided by';
        i += 2;
      } else {
        // Unknown operation
        return { error: 'Unknown operation' };
      }
      
      expectingOperation = false;
    } else {
      // We expect a number here
      if (!isValidNumber(tokens[i])) {
        return { error: 'Syntax error' };
      }
      
      const operand = parseInt(tokens[i]);
      i += 1;
      
      // Check for two numbers in a row
      if (i < tokens.length && isValidNumber(tokens[i])) {
        return { error: 'Syntax error' };
      }
      
      // Apply the previous operation
      if (tokens[i - 2] === 'plus') {
        result += operand;
      } else if (tokens[i - 2] === 'minus') {
        result -= operand;
      } else if (i - 3 >= 0 && tokens[i - 3] === 'multiplied') {
        result *= operand;
      } else if (i - 3 >= 0 && tokens[i - 3] === 'divided') {
        result /= operand;
      }
      
      expectingOperation = true;
    }
  }
  
  // If we're still expecting a number, that means the expression ended with an operation
  if (!expectingOperation) {
    return { error: 'Syntax error' };
  }
  
  return { result };
}

/**
 * Checks if a token is a valid number
 * @param {string} token - The token to check
 * @returns {boolean} - Whether the token is a valid number
 */
function isValidNumber(token) {
  return !isNaN(parseInt(token)) && String(parseInt(token)) === token;
}
