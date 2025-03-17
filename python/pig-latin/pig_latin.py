def translate(text):
    """
    Translate text from English to Pig Latin according to the following rules:
    1. If a word begins with a vowel, or starts with "xr" or "yt", add "ay" to the end of the word.
    2. If a word begins with one or more consonants, move those consonants to the end of the word and add "ay".
    3. If a word starts with zero or more consonants followed by "qu", move those consonants (if any) and the "qu" to the end of the word and add "ay".
    4. If a word starts with one or more consonants followed by "y", move the consonants preceding the "y" to the end of the word and add "ay".
    """
    words = text.split()
    translated_words = [translate_word(word) for word in words]
    return " ".join(translated_words)

def translate_word(word):
    # Rule 1: Word begins with a vowel or starts with "xr" or "yt"
    if word[0] in "aeiou" or word.startswith("xr") or word.startswith("yt"):
        return word + "ay"
    
    # Rule 3: Word starts with consonants followed by "qu"
    if "qu" in word:
        qu_index = word.index("qu")
        # Check if "qu" is preceded only by consonants
        if all(letter not in "aeiou" for letter in word[:qu_index]):
            return word[qu_index+2:] + word[:qu_index+2] + "ay"
    
    # Rule 4: Word starts with consonants followed by "y"
    if "y" in word[1:]:  # "y" not at the beginning
        y_index = word.find("y", 1)  # Find "y" starting from the second character
        # Check if "y" is preceded only by consonants
        if all(letter not in "aeiou" for letter in word[:y_index]):
            return word[y_index:] + word[:y_index] + "ay"
    
    # Rule 2: Word begins with one or more consonants
    # Find the index of the first vowel
    for i, letter in enumerate(word):
        if letter in "aeiou" or (letter == "y" and i > 0):
            return word[i:] + word[:i] + "ay"
    
    # If no vowels found, return the original word with "ay"
    return word + "ay"
