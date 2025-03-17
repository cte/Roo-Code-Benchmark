def recite(start_verse, end_verse):
    animals = [
        "fly",
        "spider",
        "bird",
        "cat",
        "dog",
        "goat",
        "cow",
        "horse"
    ]
    
    second_lines = [
        "I don't know why she swallowed the fly. Perhaps she'll die.",
        "It wriggled and jiggled and tickled inside her.",
        "How absurd to swallow a bird!",
        "Imagine that, to swallow a cat!",
        "What a hog, to swallow a dog!",
        "Just opened her throat and swallowed a goat!",
        "I don't know how she swallowed a cow!",
        "She's dead, of course!"
    ]
    
    result = []
    
    for verse_num in range(start_verse, end_verse + 1):
        # Add an empty line between verses
        if verse_num > start_verse:
            result.append("")
        
        # Add the first line
        animal = animals[verse_num - 1]
        result.append(f"I know an old lady who swallowed a {animal}.")
        
        # Add the second line
        result.append(second_lines[verse_num - 1])
        
        # If it's the horse, we're done with this verse
        if verse_num == 8:
            continue
        
        # If it's the fly, we're done with this verse
        if verse_num == 1:
            continue
        
        # Add the cumulative "She swallowed X to catch Y" lines
        for i in range(verse_num - 1, 0, -1):
            current_animal = animals[i]
            previous_animal = animals[i - 1]
            
            if previous_animal == "spider":
                result.append(f"She swallowed the {current_animal} to catch the {previous_animal} that wriggled and jiggled and tickled inside her.")
            else:
                result.append(f"She swallowed the {current_animal} to catch the {previous_animal}.")
        
        # Add the last line for all verses except the horse
        result.append("I don't know why she swallowed the fly. Perhaps she'll die.")
    
    return result
