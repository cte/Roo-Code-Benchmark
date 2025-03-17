class SgfTree:
    def __init__(self, properties=None, children=None):
        self.properties = properties or {}
        self.children = children or []

    def __eq__(self, other):
        if not isinstance(other, SgfTree):
            return False
        for key, value in self.properties.items():
            if key not in other.properties:
                return False
            if other.properties[key] != value:
                return False
        for key in other.properties.keys():
            if key not in self.properties:
                return False
        if len(self.children) != len(other.children):
            return False
        for child, other_child in zip(self.children, other.children):
            if child != other_child:
                return False
        return True

    def __ne__(self, other):
        return not self == other


def parse(input_string):
    if not input_string:
        raise ValueError("tree missing")
    
    if input_string == ";" or not input_string.startswith("("):
        raise ValueError("tree missing")
    
    if input_string == "()":
        raise ValueError("tree with no nodes")
    
    # Remove outer parentheses
    input_string = input_string[1:-1]
    
    # Parse the tree
    tree, _ = parse_node(input_string, 0)
    return tree

def parse_node(input_string, index):
    if index >= len(input_string) or input_string[index] != ';':
        raise ValueError("tree missing")
    
    index += 1  # Skip the semicolon
    properties = {}
    children = []
    
    # Parse properties
    while index < len(input_string) and input_string[index] not in '();':
        prop_name = ""
        # Get property name
        while index < len(input_string) and input_string[index].isalpha():
            prop_name += input_string[index]
            index += 1
        
        if not prop_name:
            index += 1
            continue
        
        # Validate property name
        if any(c.islower() for c in prop_name):
            raise ValueError("property must be in uppercase")
        
        # Check for property delimiter
        if index >= len(input_string) or input_string[index] != '[':
            raise ValueError("properties without delimiter")
        
        # Parse property values
        values = []
        while index < len(input_string) and input_string[index] == '[':
            index += 1  # Skip the opening bracket
            value, index = parse_property_value(input_string, index)
            values.append(value)
        
        properties[prop_name] = values
    
    # Parse children
    while index < len(input_string):
        if input_string[index] == ';':
            # Direct child
            child, new_index = parse_node(input_string, index)
            children.append(child)
            index = new_index
        elif input_string[index] == '(':
            # Subtree
            subtree_end = find_matching_parenthesis(input_string, index)
            if subtree_end == -1:
                raise ValueError("unmatched parenthesis")
            
            # Parse the subtree
            subtree_content = input_string[index+1:subtree_end]
            if subtree_content:
                child, _ = parse_node(subtree_content, 0)
                children.append(child)
            
            index = subtree_end + 1
        else:
            index += 1
    
    return SgfTree(properties, children), index

def parse_property_value(input_string, index):
    value = ""
    escape_next = False
    
    while index < len(input_string):
        char = input_string[index]
        
        if escape_next:
            if char == '\n':
                # Escaped newline is removed
                pass
            elif char.isspace() and char != '\n':
                # Other escaped whitespace becomes a space
                value += ' '
            else:
                # Any other escaped character is included as-is
                value += char
            escape_next = False
        elif char == '\\':
            escape_next = True
        elif char == ']':
            # End of property value
            return value, index + 1
        elif char.isspace() and char != '\n':
            # Non-newline whitespace becomes a space
            value += ' '
        else:
            value += char
        
        index += 1
    
    # If we get here, we didn't find a closing bracket
    raise ValueError("unclosed property value")

def find_matching_parenthesis(input_string, start_index):
    """Find the index of the matching closing parenthesis."""
    if input_string[start_index] != '(':
        return -1
    
    count = 1
    for i in range(start_index + 1, len(input_string)):
        if input_string[i] == '(':
            count += 1
        elif input_string[i] == ')':
            count -= 1
            if count == 0:
                return i
    
    return -1
