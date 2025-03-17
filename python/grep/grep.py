def grep(pattern, flags, files):
    # Parse flags
    flag_list = flags.split()
    show_line_numbers = "-n" in flag_list
    show_file_names_only = "-l" in flag_list
    case_insensitive = "-i" in flag_list
    invert_match = "-v" in flag_list
    match_entire_line = "-x" in flag_list
    
    result = []
    multiple_files = len(files) > 1
    
    for file_name in files:
        file_has_match = False
        with open(file_name, 'r') as file:
            for line_number, line in enumerate(file, 1):
                # Check if the line matches the pattern
                if case_insensitive:
                    pattern_lower = pattern.lower()
                    line_to_check = line.lower()
                else:
                    pattern_lower = pattern
                    line_to_check = line
                
                # Determine if there's a match based on flags
                if match_entire_line:
                    # Remove newline for comparison but keep it for output
                    matches = pattern_lower == line_to_check.rstrip('\n')
                else:
                    matches = pattern_lower in line_to_check
                
                # Invert the match if -v flag is present
                if invert_match:
                    matches = not matches
                
                if matches:
                    file_has_match = True
                    
                    # If -l flag is present, we only need to know if there's a match
                    if show_file_names_only:
                        break
                    
                    # Format the output line
                    output_line = line
                    
                    # Add file name prefix for multiple files
                    if multiple_files:
                        output_line = f"{file_name}:{output_line}"
                    
                    # Add line number if -n flag is present
                    if show_line_numbers:
                        # Insert line number after file name if present
                        if multiple_files:
                            # Remove the file name prefix to insert line number
                            parts = output_line.split(':', 1)
                            output_line = f"{parts[0]}:{line_number}:{parts[1]}"
                        else:
                            output_line = f"{line_number}:{output_line}"
                    
                    result.append(output_line)
        
        # If -l flag is present and file has a match, add the file name
        if show_file_names_only and file_has_match:
            result.append(f"{file_name}\n")
    
    return "".join(result)
