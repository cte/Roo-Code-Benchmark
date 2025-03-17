package forth

import (
	"errors"
	"fmt"
	"strconv"
	"strings"
)

// Custom errors
var (
	ErrEmptyStack      = errors.New("empty stack")
	ErrOnlyOneValue    = errors.New("only one value on the stack")
	ErrDivideByZero    = errors.New("divide by zero")
	ErrIllegalOperation = errors.New("illegal operation")
	ErrUndefinedOperation = errors.New("undefined operation")
)

// Forth evaluates a series of Forth commands and returns the resulting stack
func Forth(input []string) ([]int, error) {
	interpreter := newInterpreter()
	
	for _, line := range input {
		if err := interpreter.evaluate(line); err != nil {
			return nil, err
		}
	}
	
	return interpreter.stack, nil
}

// interpreter represents a Forth interpreter
type interpreter struct {
	stack []int
	words map[string][]string
}

// newInterpreter creates a new Forth interpreter
func newInterpreter() *interpreter {
	return &interpreter{
		stack: []int{},
		words: make(map[string][]string),
	}
}

// evaluate processes a line of Forth code
func (i *interpreter) evaluate(line string) error {
	// Convert to lowercase for case-insensitivity
	line = strings.ToLower(line)
	
	// Check if this is a word definition
	if strings.HasPrefix(line, ":") && strings.HasSuffix(line, ";") {
		return i.defineWord(line)
	}
	
	// Process the tokens
	tokens := strings.Fields(line)
	return i.processTokens(tokens)
}

// defineWord handles the definition of a new word
func (i *interpreter) defineWord(line string) error {
	// Remove the : and ; from the definition
	definition := strings.TrimPrefix(line, ":")
	definition = strings.TrimSuffix(definition, ";")
	definition = strings.TrimSpace(definition)
	
	tokens := strings.Fields(definition)
	if len(tokens) < 2 {
		return ErrIllegalOperation
	}
	
	// The first token is the word name
	wordName := tokens[0]
	
	// Check if trying to redefine a number
	if _, err := strconv.Atoi(wordName); err == nil {
		return ErrIllegalOperation
	}
	
	// The rest of the tokens are the word definition
	wordDefinition := tokens[1:]
	
	// Expand any previously defined words in the definition
	expandedDefinition := []string{}
	for _, token := range wordDefinition {
		if def, exists := i.words[token]; exists {
			expandedDefinition = append(expandedDefinition, def...)
		} else {
			expandedDefinition = append(expandedDefinition, token)
		}
	}
	
	// Store the word definition
	i.words[wordName] = expandedDefinition
	
	return nil
}

// processTokens processes a sequence of Forth tokens
func (i *interpreter) processTokens(tokens []string) error {
	for j := 0; j < len(tokens); j++ {
		token := tokens[j]
		
		// Check if this is a defined word
		if def, exists := i.words[token]; exists {
			// Process the word definition
			if err := i.processTokens(def); err != nil {
				return err
			}
			continue
		}
		
		// Try to parse as a number
		if num, err := strconv.Atoi(token); err == nil {
			i.stack = append(i.stack, num)
			continue
		}
		
		// Handle built-in operations
		switch token {
		case "+":
			if len(i.stack) < 2 {
				if len(i.stack) == 0 {
					return ErrEmptyStack
				}
				return ErrOnlyOneValue
			}
			a, b := i.stack[len(i.stack)-2], i.stack[len(i.stack)-1]
			i.stack = i.stack[:len(i.stack)-2]
			i.stack = append(i.stack, a+b)
			
		case "-":
			if len(i.stack) < 2 {
				if len(i.stack) == 0 {
					return ErrEmptyStack
				}
				return ErrOnlyOneValue
			}
			a, b := i.stack[len(i.stack)-2], i.stack[len(i.stack)-1]
			i.stack = i.stack[:len(i.stack)-2]
			i.stack = append(i.stack, a-b)
			
		case "*":
			if len(i.stack) < 2 {
				if len(i.stack) == 0 {
					return ErrEmptyStack
				}
				return ErrOnlyOneValue
			}
			a, b := i.stack[len(i.stack)-2], i.stack[len(i.stack)-1]
			i.stack = i.stack[:len(i.stack)-2]
			i.stack = append(i.stack, a*b)
			
		case "/":
			if len(i.stack) < 2 {
				if len(i.stack) == 0 {
					return ErrEmptyStack
				}
				return ErrOnlyOneValue
			}
			a, b := i.stack[len(i.stack)-2], i.stack[len(i.stack)-1]
			if b == 0 {
				return ErrDivideByZero
			}
			i.stack = i.stack[:len(i.stack)-2]
			i.stack = append(i.stack, a/b)
			
		case "dup":
			if len(i.stack) == 0 {
				return ErrEmptyStack
			}
			i.stack = append(i.stack, i.stack[len(i.stack)-1])
			
		case "drop":
			if len(i.stack) == 0 {
				return ErrEmptyStack
			}
			i.stack = i.stack[:len(i.stack)-1]
			
		case "swap":
			if len(i.stack) < 2 {
				if len(i.stack) == 0 {
					return ErrEmptyStack
				}
				return ErrOnlyOneValue
			}
			i.stack[len(i.stack)-1], i.stack[len(i.stack)-2] = i.stack[len(i.stack)-2], i.stack[len(i.stack)-1]
			
		case "over":
			if len(i.stack) < 2 {
				if len(i.stack) == 0 {
					return ErrEmptyStack
				}
				return ErrOnlyOneValue
			}
			i.stack = append(i.stack, i.stack[len(i.stack)-2])
			
		default:
			return fmt.Errorf("%w: %s", ErrUndefinedOperation, token)
		}
	}
	
	return nil
}
