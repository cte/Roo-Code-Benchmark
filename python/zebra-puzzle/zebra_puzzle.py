def solve_zebra_puzzle():
    """
    Solve the Zebra Puzzle and return the solution.
    
    The puzzle has 5 houses, each with:
    - A color (red, green, ivory, yellow, blue)
    - A nationality (Englishman, Spaniard, Ukrainian, Norwegian, Japanese)
    - A pet (dog, snail, fox, horse, zebra)
    - A drink (coffee, tea, milk, orange juice, water)
    - A hobby (dancing, painting, reading, football, chess)
    """
    # We'll represent the solution as a list of houses
    # Each house is a dictionary with attributes
    houses = [
        {"position": i, "color": None, "nationality": None, "pet": None, "drink": None, "hobby": None}
        for i in range(1, 6)  # Positions 1-5
    ]
    
    # All possible values for each attribute
    colors = ["red", "green", "ivory", "yellow", "blue"]
    nationalities = ["Englishman", "Spaniard", "Ukrainian", "Norwegian", "Japanese"]
    pets = ["dog", "snail", "fox", "horse", "zebra"]
    drinks = ["coffee", "tea", "milk", "orange juice", "water"]
    hobbies = ["dancing", "painting", "reading", "football", "chess"]
    
    # Generate all possible combinations of houses
    from itertools import permutations
    
    # Apply constraints to find the solution
    for color_perm in permutations(colors):
        # 6. The green house is immediately to the right of the ivory house.
        if not is_right_of(color_perm.index("green") + 1, color_perm.index("ivory") + 1):
            continue
            
        for nationality_perm in permutations(nationalities):
            # 10. The Norwegian lives in the first house.
            if nationality_perm[0] != "Norwegian":
                continue
                
            # 2. The Englishman lives in the red house.
            if nationality_perm[color_perm.index("red")] != "Englishman":
                continue
                
            # 15. The Norwegian lives next to the blue house.
            norwegian_pos = nationality_perm.index("Norwegian") + 1
            blue_pos = color_perm.index("blue") + 1
            if not is_next_to(norwegian_pos, blue_pos):
                continue
                
            for pet_perm in permutations(pets):
                # 3. The Spaniard owns the dog.
                if pet_perm[nationality_perm.index("Spaniard")] != "dog":
                    continue
                    
                for drink_perm in permutations(drinks):
                    # 4. The person in the green house drinks coffee.
                    if drink_perm[color_perm.index("green")] != "coffee":
                        continue
                        
                    # 5. The Ukrainian drinks tea.
                    if drink_perm[nationality_perm.index("Ukrainian")] != "tea":
                        continue
                        
                    # 9. The person in the middle house drinks milk.
                    if drink_perm[2] != "milk":  # Middle house is at index 2 (position 3)
                        continue
                        
                    # 13. The person who plays football drinks orange juice.
                    football_pos = None  # We'll check this later
                    
                    for hobby_perm in permutations(hobbies):
                        # 7. The snail owner likes to go dancing.
                        if hobby_perm[pet_perm.index("snail")] != "dancing":
                            continue
                            
                        # 8. The person in the yellow house is a painter.
                        if hobby_perm[color_perm.index("yellow")] != "painting":
                            continue
                            
                        # 13. The person who plays football drinks orange juice.
                        football_pos = hobby_perm.index("football") + 1
                        orange_juice_pos = drink_perm.index("orange juice") + 1
                        if football_pos != orange_juice_pos:
                            continue
                            
                        # 14. The Japanese person plays chess.
                        if hobby_perm[nationality_perm.index("Japanese")] != "chess":
                            continue
                            
                        # 11. The person who enjoys reading lives in the house next to the person with the fox.
                        reading_pos = hobby_perm.index("reading") + 1
                        fox_pos = pet_perm.index("fox") + 1
                        if not is_next_to(reading_pos, fox_pos):
                            continue
                            
                        # 12. The painter's house is next to the house with the horse.
                        painter_pos = hobby_perm.index("painting") + 1
                        horse_pos = pet_perm.index("horse") + 1
                        if not is_next_to(painter_pos, horse_pos):
                            continue
                            
                        # If we've reached here, we have a valid solution
                        for i in range(5):
                            houses[i]["color"] = color_perm[i]
                            houses[i]["nationality"] = nationality_perm[i]
                            houses[i]["pet"] = pet_perm[i]
                            houses[i]["drink"] = drink_perm[i]
                            houses[i]["hobby"] = hobby_perm[i]
                            
                        return houses
    
    return None  # No solution found

def is_right_of(pos1, pos2):
    """Check if position 1 is immediately to the right of position 2."""
    return pos1 == pos2 + 1

def is_next_to(pos1, pos2):
    """Check if position 1 is next to position 2."""
    return abs(pos1 - pos2) == 1

# Cache the solution to avoid recomputing it
_solution = None

def get_solution():
    """Get the solution to the Zebra Puzzle, computing it if necessary."""
    global _solution
    if _solution is None:
        _solution = solve_zebra_puzzle()
    return _solution

def drinks_water():
    """Return the nationality of the person who drinks water."""
    solution = get_solution()
    for house in solution:
        if house["drink"] == "water":
            return house["nationality"]
    return None

def owns_zebra():
    """Return the nationality of the person who owns the zebra."""
    solution = get_solution()
    for house in solution:
        if house["pet"] == "zebra":
            return house["nationality"]
    return None
