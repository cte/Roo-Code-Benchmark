def recite(start, take=1):
    """
    Recite the lyrics to the "99 Bottles of Beer on the Wall" song.
    
    Args:
        start: The starting number of bottles
        take: The number of verses to recite (default: 1)
    
    Returns:
        A list of strings representing the lyrics
    """
    lyrics = []
    
    for bottles in range(start, start - take, -1):
        # First line
        if bottles == 0:
            lyrics.append("No more bottles of beer on the wall, no more bottles of beer.")
        elif bottles == 1:
            lyrics.append("1 bottle of beer on the wall, 1 bottle of beer.")
        else:
            lyrics.append(f"{bottles} bottles of beer on the wall, {bottles} bottles of beer.")
        
        # Second line
        if bottles == 0:
            lyrics.append("Go to the store and buy some more, 99 bottles of beer on the wall.")
        elif bottles == 1:
            lyrics.append("Take it down and pass it around, no more bottles of beer on the wall.")
        elif bottles == 2:
            lyrics.append("Take one down and pass it around, 1 bottle of beer on the wall.")
        else:
            lyrics.append(f"Take one down and pass it around, {bottles - 1} bottles of beer on the wall.")
        
        # Add empty line between verses, but not after the last verse
        if bottles > start - take + 1:
            lyrics.append("")
    
    return lyrics
