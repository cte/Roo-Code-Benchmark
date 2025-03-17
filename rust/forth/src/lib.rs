use std::collections::HashMap;

pub type Value = i32;
pub type Result = std::result::Result<(), Error>;

#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    DivisionByZero,
    StackUnderflow,
    UnknownWord,
    InvalidWord,
}

#[derive(Clone)]
enum WordDefinition {
    Builtin(fn(&mut Forth) -> Result),
    UserDefined(Vec<Token>),
}

#[derive(Clone)]
enum Token {
    Number(Value),
    Word(String),
    Definition(Vec<Token>), // Store the actual definition, not just the word name
}

pub struct Forth {
    stack: Vec<Value>,
    definitions: HashMap<String, WordDefinition>,
}

impl Forth {
    pub fn new() -> Forth {
        let mut forth = Forth {
            stack: Vec::new(),
            definitions: HashMap::new(),
        };
        
        // Add built-in operations
        forth.definitions.insert("+".to_string(), WordDefinition::Builtin(Forth::add));
        forth.definitions.insert("-".to_string(), WordDefinition::Builtin(Forth::subtract));
        forth.definitions.insert("*".to_string(), WordDefinition::Builtin(Forth::multiply));
        forth.definitions.insert("/".to_string(), WordDefinition::Builtin(Forth::divide));
        forth.definitions.insert("dup".to_string(), WordDefinition::Builtin(Forth::dup));
        forth.definitions.insert("drop".to_string(), WordDefinition::Builtin(Forth::drop));
        forth.definitions.insert("swap".to_string(), WordDefinition::Builtin(Forth::swap));
        forth.definitions.insert("over".to_string(), WordDefinition::Builtin(Forth::over));
        
        forth
    }

    pub fn stack(&self) -> &[Value] {
        &self.stack
    }

    pub fn eval(&mut self, input: &str) -> Result {
        let mut in_definition = false;
        let mut current_definition = Vec::new();
        let mut word_name = String::new();

        let tokens: Vec<&str> = input
            .split_whitespace()
            .collect();

        let mut i = 0;
        while i < tokens.len() {
            let token = tokens[i].to_lowercase();

            if token == ":" && !in_definition {
                in_definition = true;
                i += 1;
                if i >= tokens.len() {
                    return Err(Error::InvalidWord);
                }
                
                word_name = tokens[i].to_lowercase();
                
                // Check if word_name is a number
                if word_name.parse::<Value>().is_ok() {
                    return Err(Error::InvalidWord);
                }
                
                current_definition.clear();
            } else if token == ";" && in_definition {
                in_definition = false;
                
                // Convert string tokens to our Token enum
                match self.compile_definition(&current_definition) {
                    Ok(compiled_definition) => {
                        self.definitions.insert(word_name.clone(), WordDefinition::UserDefined(compiled_definition));
                    },
                    Err(e) => return Err(e),
                }
            } else if in_definition {
                current_definition.push(token);
            } else {
                self.eval_token(&token)?;
            }
            
            i += 1;
        }

        if in_definition {
            return Err(Error::InvalidWord);
        }

        Ok(())
    }
    
    // Compile a definition into tokens that can be executed later
    fn compile_definition(&self, definition: &[String]) -> std::result::Result<Vec<Token>, Error> {
        let mut result = Vec::new();
        
        for token in definition {
            if let Ok(num) = token.parse::<Value>() {
                result.push(Token::Number(num));
            } else {
                let token_lower = token.to_lowercase();
                
                // If the word is already defined, store its current definition
                if let Some(def) = self.definitions.get(&token_lower) {
                    match def {
                        WordDefinition::Builtin(_) => {
                            // For built-ins, just store the word name
                            result.push(Token::Word(token_lower));
                        },
                        WordDefinition::UserDefined(tokens) => {
                            // For user-defined words, store the actual definition
                            result.push(Token::Definition(tokens.clone()));
                        }
                    }
                } else {
                    // If the word is not defined, just store the word name
                    result.push(Token::Word(token_lower));
                }
            }
        }
        
        Ok(result)
    }

    fn eval_token(&mut self, token: &str) -> Result {
        // Try to parse as a number
        if let Ok(num) = token.parse::<Value>() {
            self.stack.push(num);
            return Ok(());
        }

        // Check if it's a built-in operation or a user-defined word
        let token_lower = token.to_lowercase();
        
        // Clone the definition to avoid borrowing issues
        let definition = match self.definitions.get(&token_lower) {
            Some(def) => def.clone(),
            None => return Err(Error::UnknownWord),
        };
        
        match definition {
            WordDefinition::Builtin(func) => func(self),
            WordDefinition::UserDefined(tokens) => {
                for token in tokens {
                    match token {
                        Token::Number(num) => self.stack.push(num),
                        Token::Word(word) => self.eval_token(&word)?,
                        Token::Definition(inner_tokens) => {
                            // Execute the stored definition directly
                            for inner_token in inner_tokens {
                                match inner_token {
                                    Token::Number(num) => self.stack.push(num),
                                    Token::Word(word) => self.eval_token(&word)?,
                                    Token::Definition(more_tokens) => {
                                        // Handle nested definitions (should be rare)
                                        for more_token in more_tokens {
                                            match more_token {
                                                Token::Number(num) => self.stack.push(num),
                                                Token::Word(word) => self.eval_token(&word)?,
                                                // We limit nesting to avoid stack overflows
                                                _ => return Err(Error::InvalidWord),
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Ok(())
            }
        }
    }

    fn add(forth: &mut Forth) -> Result {
        if forth.stack.len() < 2 {
            return Err(Error::StackUnderflow);
        }
        
        let b = forth.stack.pop().unwrap();
        let a = forth.stack.pop().unwrap();
        forth.stack.push(a + b);
        
        Ok(())
    }

    fn subtract(forth: &mut Forth) -> Result {
        if forth.stack.len() < 2 {
            return Err(Error::StackUnderflow);
        }
        
        let b = forth.stack.pop().unwrap();
        let a = forth.stack.pop().unwrap();
        forth.stack.push(a - b);
        
        Ok(())
    }

    fn multiply(forth: &mut Forth) -> Result {
        if forth.stack.len() < 2 {
            return Err(Error::StackUnderflow);
        }
        
        let b = forth.stack.pop().unwrap();
        let a = forth.stack.pop().unwrap();
        forth.stack.push(a * b);
        
        Ok(())
    }

    fn divide(forth: &mut Forth) -> Result {
        if forth.stack.len() < 2 {
            return Err(Error::StackUnderflow);
        }
        
        let b = forth.stack.pop().unwrap();
        let a = forth.stack.pop().unwrap();
        
        if b == 0 {
            return Err(Error::DivisionByZero);
        }
        
        forth.stack.push(a / b);
        
        Ok(())
    }

    fn dup(forth: &mut Forth) -> Result {
        if forth.stack.is_empty() {
            return Err(Error::StackUnderflow);
        }
        
        let a = *forth.stack.last().unwrap();
        forth.stack.push(a);
        
        Ok(())
    }

    fn drop(forth: &mut Forth) -> Result {
        if forth.stack.is_empty() {
            return Err(Error::StackUnderflow);
        }
        
        forth.stack.pop();
        
        Ok(())
    }

    fn swap(forth: &mut Forth) -> Result {
        if forth.stack.len() < 2 {
            return Err(Error::StackUnderflow);
        }
        
        let len = forth.stack.len();
        forth.stack.swap(len - 1, len - 2);
        
        Ok(())
    }

    fn over(forth: &mut Forth) -> Result {
        if forth.stack.len() < 2 {
            return Err(Error::StackUnderflow);
        }
        
        let a = forth.stack[forth.stack.len() - 2];
        forth.stack.push(a);
        
        Ok(())
    }
}
