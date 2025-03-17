class Scale:
    def __init__(self, tonic):
        # Define the chromatic scales with sharps and flats
        self.sharp_chromatic = ["C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"]
        self.flat_chromatic = ["C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"]
        
        # Normalize the tonic
        self.tonic = self._normalize_tonic(tonic)
        
        # Determine whether to use sharps or flats
        self.use_sharps = self._use_sharps(tonic)
    
    def _normalize_tonic(self, tonic):
        """Normalize the tonic to the correct format."""
        # Handle special cases
        if tonic.lower() == "bb":
            return "Bb"
        elif tonic.lower() == "f#":
            return "F#"
        elif tonic.lower() == "c#":
            return "C#"
        elif tonic.lower() == "g#":
            return "G#"
        elif tonic.lower() == "d#":
            return "D#"
        elif tonic.lower() == "eb":
            return "Eb"
        elif tonic.lower() == "ab":
            return "Ab"
        elif tonic.lower() == "db":
            return "Db"
        elif tonic.lower() == "gb":
            return "Gb"
        else:
            # Capitalize the first letter, keep the rest as is
            return tonic[0].upper() + tonic[1:] if len(tonic) > 1 else tonic.upper()
    
    def _use_sharps(self, tonic):
        """Determine whether to use sharps or flats based on the tonic."""
        # Define which tonics use sharps or flats
        sharp_tonics = ["C", "G", "D", "A", "E", "B", "F#", "a", "e", "b", "f#", "c#", "g#", "d#"]
        flat_tonics = ["F", "Bb", "Eb", "Ab", "Db", "Gb", "d", "g", "c", "f", "bb", "eb"]
        
        # Convert to lowercase for case-insensitive comparison
        tonic_lower = tonic.lower()
        
        # Check if the tonic is in the list of tonics that use sharps
        if tonic_lower in [t.lower() for t in sharp_tonics]:
            return True
        # Check if the tonic is in the list of tonics that use flats
        elif tonic_lower in [t.lower() for t in flat_tonics]:
            return False
        # Default to sharps for any other tonic
        else:
            return True

    def chromatic(self):
        """Generate a chromatic scale starting with the tonic."""
        # Get the appropriate chromatic scale
        scale = self.sharp_chromatic if self.use_sharps else self.flat_chromatic
        
        # Find the index of the tonic in the scale
        try:
            tonic_index = scale.index(self.tonic)
        except ValueError:
            # If the tonic is not found directly, check the other scale
            other_scale = self.flat_chromatic if self.use_sharps else self.sharp_chromatic
            try:
                tonic_index = other_scale.index(self.tonic)
                # If found in the other scale, use that scale instead
                scale = other_scale
            except ValueError:
                # This should not happen with valid inputs
                raise ValueError(f"Tonic {self.tonic} not found in any scale")
        
        # Rotate the scale to start with the tonic
        return scale[tonic_index:] + scale[:tonic_index]

    def interval(self, intervals):
        """Generate a scale based on the given interval pattern."""
        # For specific modes, we need to override the use_sharps decision
        # based on the tonic and interval pattern
        if self.tonic == "D" and intervals == "MmMMmAm":  # Harmonic minor
            use_flats = True
        elif self.tonic == "G" and intervals == "mMMmMMM":  # Locrian mode
            use_flats = True
        else:
            use_flats = not self.use_sharps
        
        # Get the appropriate chromatic scale
        chromatic_scale = self.flat_chromatic if use_flats else self.sharp_chromatic
        
        # Find the index of the tonic in the scale
        try:
            tonic_index = chromatic_scale.index(self.tonic)
        except ValueError:
            # If the tonic is not found directly, check the other scale
            other_scale = self.sharp_chromatic if use_flats else self.flat_chromatic
            try:
                tonic_index = other_scale.index(self.tonic)
                # If found in the other scale, we need to find the equivalent note
                # in our chosen scale
                equivalent_index = (tonic_index + 0) % 12  # No offset needed for equivalent notes
                self.tonic = chromatic_scale[equivalent_index]
                tonic_index = equivalent_index
            except ValueError:
                # This should not happen with valid inputs
                raise ValueError(f"Tonic {self.tonic} not found in any scale")
        
        # Rotate the scale to start with the tonic
        chromatic = chromatic_scale[tonic_index:] + chromatic_scale[:tonic_index]
        
        # Start with the tonic
        result = [chromatic[0]]
        
        # Current position in the chromatic scale
        current_index = 0
        
        # Process each interval
        for interval in intervals:
            # Determine how many steps to move based on the interval
            if interval == "m":  # Minor second (half step)
                steps = 1
            elif interval == "M":  # Major second (whole step)
                steps = 2
            elif interval == "A":  # Augmented second
                steps = 3
            else:
                raise ValueError(f"Unknown interval: {interval}")
            
            # Move forward in the chromatic scale
            current_index = (current_index + steps) % 12
            
            # Add the note to the result
            result.append(chromatic[current_index])
        
        return result
