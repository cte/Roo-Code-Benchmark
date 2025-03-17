class PhoneNumber:
    def __init__(self, number):
        # Check for letters first
        if any(char.isalpha() for char in number):
            raise ValueError("letters not permitted")
        
        # Check for punctuations (any non-alphanumeric and non-space character)
        if any(char for char in number if not char.isalnum() and not char.isspace() and char not in '()-.+'):
            raise ValueError("punctuations not permitted")
        
        # Clean the input by removing all non-digit characters
        digits_only = ''.join(char for char in number if char.isdigit())
        
        # Check length
        if len(digits_only) < 10:
            raise ValueError("must not be fewer than 10 digits")
        elif len(digits_only) > 11:
            raise ValueError("must not be greater than 11 digits")
        
        # Handle 11-digit numbers
        if len(digits_only) == 11:
            if digits_only[0] != '1':
                raise ValueError("11 digits must start with 1")
            # Remove the country code
            digits_only = digits_only[1:]
        
        # Validate area code
        if digits_only[0] == '0':
            raise ValueError("area code cannot start with zero")
        if digits_only[0] == '1':
            raise ValueError("area code cannot start with one")
        
        # Validate exchange code
        if digits_only[3] == '0':
            raise ValueError("exchange code cannot start with zero")
        if digits_only[3] == '1':
            raise ValueError("exchange code cannot start with one")
        
        # Store the cleaned number
        self._number = digits_only
    
    @property
    def number(self):
        return self._number
    
    @property
    def area_code(self):
        return self._number[:3]
    
    def pretty(self):
        return f"({self.area_code})-{self._number[3:6]}-{self._number[6:]}"
