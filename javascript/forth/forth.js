export class Forth {
  constructor() {
    this._stack = [];
    this._words = {};
    
    // Initialize built-in operations
    this._initializeBuiltIns();
  }

  _initializeBuiltIns() {
    // Arithmetic operations
    this._defineBuiltIn('+', () => {
      if (this._stack.length < 2) throw new Error('Stack empty');
      const b = this._stack.pop();
      const a = this._stack.pop();
      this._stack.push(a + b);
    });

    this._defineBuiltIn('-', () => {
      if (this._stack.length < 2) throw new Error('Stack empty');
      const b = this._stack.pop();
      const a = this._stack.pop();
      this._stack.push(a - b);
    });

    this._defineBuiltIn('*', () => {
      if (this._stack.length < 2) throw new Error('Stack empty');
      const b = this._stack.pop();
      const a = this._stack.pop();
      this._stack.push(a * b);
    });

    this._defineBuiltIn('/', () => {
      if (this._stack.length < 2) throw new Error('Stack empty');
      const b = this._stack.pop();
      if (b === 0) throw new Error('Division by zero');
      const a = this._stack.pop();
      this._stack.push(Math.floor(a / b)); // Integer division
    });

    // Stack manipulation operations
    this._defineBuiltIn('dup', () => {
      if (this._stack.length < 1) throw new Error('Stack empty');
      const a = this._stack[this._stack.length - 1];
      this._stack.push(a);
    });

    this._defineBuiltIn('drop', () => {
      if (this._stack.length < 1) throw new Error('Stack empty');
      this._stack.pop();
    });

    this._defineBuiltIn('swap', () => {
      if (this._stack.length < 2) throw new Error('Stack empty');
      const b = this._stack.pop();
      const a = this._stack.pop();
      this._stack.push(b);
      this._stack.push(a);
    });

    this._defineBuiltIn('over', () => {
      if (this._stack.length < 2) throw new Error('Stack empty');
      const a = this._stack[this._stack.length - 2];
      this._stack.push(a);
    });
  }

  _defineBuiltIn(name, fn) {
    this._words[name.toLowerCase()] = { isBuiltIn: true, fn };
  }

  evaluate(program) {
    // Split the program into tokens
    const tokens = this._tokenize(program);
    
    // Process tokens
    let isDefiningWord = false;
    let wordBeingDefined = null;
    let definition = [];
    
    for (let i = 0; i < tokens.length; i++) {
      const token = tokens[i].toLowerCase(); // Case-insensitive
      
      if (isDefiningWord) {
        if (token === ';') {
          // End of word definition
          const wordName = wordBeingDefined;
          
          // Create a compiled version of the definition
          // This resolves all word references at definition time
          const compiledDefinition = this._compileDefinition(definition);
          
          // Store the definition
          this._words[wordName] = {
            isBuiltIn: false,
            definition: compiledDefinition
          };
          
          isDefiningWord = false;
          wordBeingDefined = null;
          definition = [];
        } else {
          // Add token to the definition
          definition.push(token);
        }
      } else if (token === ':') {
        // Start of word definition
        isDefiningWord = true;
        // Next token is the word name
        const wordName = tokens[++i].toLowerCase();
        
        // Check if the word name is a number
        if (this._isNumber(wordName)) {
          throw new Error('Invalid definition');
        }
        
        wordBeingDefined = wordName;
      } else {
        // Execute the token
        this._executeToken(token);
      }
    }
  }

  _compileDefinition(definition) {
    const compiled = [];
    
    for (const token of definition) {
      if (this._isNumber(token)) {
        // If it's a number, add a function that pushes the number to the stack
        compiled.push(() => this._stack.push(parseInt(token, 10)));
      } else if (token in this._words) {
        const wordDef = this._words[token];
        
        if (wordDef.isBuiltIn) {
          // If it's a built-in word, add the function directly
          compiled.push(wordDef.fn);
        } else {
          // If it's a user-defined word, add a function that executes its compiled definition
          // Make a copy to capture the current state of the word
          const definitionCopy = [...wordDef.definition];
          compiled.push(() => {
            for (const fn of definitionCopy) {
              fn();
            }
          });
        }
      } else {
        // If it's an unknown word, it might be defined later
        // Add a function that looks up the word at execution time
        compiled.push(() => {
          if (token in this._words) {
            const wordDef = this._words[token];
            if (wordDef.isBuiltIn) {
              wordDef.fn();
            } else {
              for (const fn of wordDef.definition) {
                fn();
              }
            }
          } else {
            throw new Error('Unknown command');
          }
        });
      }
    }
    
    return compiled;
  }

  _tokenize(program) {
    // Split the program into tokens
    return program.match(/[^\s]+/g) || [];
  }

  _executeToken(token) {
    // Check if the token is a number
    if (this._isNumber(token)) {
      this._stack.push(parseInt(token, 10));
      return;
    }
    
    // Check if the token is a defined word
    const word = token.toLowerCase();
    if (word in this._words) {
      const wordDef = this._words[word];
      
      if (wordDef.isBuiltIn) {
        // Execute built-in function
        wordDef.fn();
      } else {
        // Execute user-defined word
        for (const fn of wordDef.definition) {
          fn();
        }
      }
      return;
    }
    
    // Unknown command
    throw new Error('Unknown command');
  }

  _isNumber(token) {
    return /^-?\d+$/.test(token);
  }

  get stack() {
    // Return a copy of the stack to prevent external modification
    return [...this._stack];
  }
}
