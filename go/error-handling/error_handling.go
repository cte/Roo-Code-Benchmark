package erratum

import "errors"

// Use opens a resource, calls Frob(input) on the resource,
// and then closes the resource. It properly handles errors and panics.
func Use(opener ResourceOpener, input string) (err error) {
	// Keep trying to open the resource until successful or a non-transient error occurs
	var resource Resource
	
	for {
		resource, err = opener()
		if err == nil {
			break
		}
		
		// Check if it's a transient error
		if _, ok := err.(TransientError); ok {
			continue // Retry opening the resource
		}
		
		// Non-transient error, return it
		return err
	}
	
	// Ensure the resource is closed exactly once if it was successfully opened
	defer func() {
		resource.Close()
	}()
	
	// Use defer and recover to handle panics during Frob
	defer func() {
		if r := recover(); r != nil {
			// Check if it's a FrobError
			if frobErr, ok := r.(FrobError); ok {
				// Call Defrob with the defrobTag before closing
				resource.Defrob(frobErr.defrobTag)
				// Set err to the FrobError so it can be returned
				err = frobErr
			} else if e, ok := r.(error); ok {
				// It's some other error, just set err
				err = e
			} else {
				// It's not an error type, convert it to an error
				err = errors.New("unknown panic occurred")
			}
		}
	}()
	
	// Call Frob with the input string
	resource.Frob(input)
	
	// The named return value 'err' will be returned automatically
	return
}
