import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ZebraPuzzle {
    // Enums for the different attributes
    enum Color { RED, GREEN, IVORY, YELLOW, BLUE }
    enum Nationality { ENGLISHMAN, SPANIARD, UKRAINIAN, NORWEGIAN, JAPANESE }
    enum Pet { DOG, SNAIL, FOX, HORSE, ZEBRA }
    enum Drink { COFFEE, TEA, MILK, ORANGE_JUICE, WATER }
    enum Hobby { DANCING, PAINTING, READING, FOOTBALL, CHESS }
    
    // Class to represent a house
    private static class House {
        Color color;
        Nationality nationality;
        Pet pet;
        Drink drink;
        Hobby hobby;
        
        public House(Color color, Nationality nationality, Pet pet, Drink drink, Hobby hobby) {
            this.color = color;
            this.nationality = nationality;
            this.pet = pet;
            this.drink = drink;
            this.hobby = hobby;
        }
    }
    
    private final List<House> houses;
    
    public ZebraPuzzle() {
        houses = solve();
    }
    
    private List<House> solve() {
        // Generate all possible permutations of colors, nationalities, pets, drinks, and hobbies
        List<List<Color>> colorPermutations = generatePermutations(Color.values());
        List<List<Nationality>> nationalityPermutations = generatePermutations(Nationality.values());
        List<List<Pet>> petPermutations = generatePermutations(Pet.values());
        List<List<Drink>> drinkPermutations = generatePermutations(Drink.values());
        List<List<Hobby>> hobbyPermutations = generatePermutations(Hobby.values());
        
        // Try all combinations until we find a valid solution
        for (List<Color> colors : colorPermutations) {
            for (List<Nationality> nationalities : nationalityPermutations) {
                // Apply constraint 10: The Norwegian lives in the first house
                if (nationalities.get(0) != Nationality.NORWEGIAN) continue;
                
                for (List<Pet> pets : petPermutations) {
                    for (List<Drink> drinks : drinkPermutations) {
                        // Apply constraint 9: The person in the middle house drinks milk
                        if (drinks.get(2) != Drink.MILK) continue;
                        
                        for (List<Hobby> hobbies : hobbyPermutations) {
                            List<House> candidateHouses = createHouses(colors, nationalities, pets, drinks, hobbies);
                            
                            if (isValidSolution(candidateHouses)) {
                                return candidateHouses;
                            }
                        }
                    }
                }
            }
        }
        
        throw new IllegalStateException("No solution found");
    }
    
    private <T> List<List<T>> generatePermutations(T[] values) {
        List<List<T>> result = new ArrayList<>();
        generatePermutationsHelper(Arrays.asList(values), 0, result);
        return result;
    }
    
    private <T> void generatePermutationsHelper(List<T> items, int start, List<List<T>> result) {
        if (start == items.size() - 1) {
            result.add(new ArrayList<>(items));
            return;
        }
        
        for (int i = start; i < items.size(); i++) {
            // Swap items[start] and items[i]
            T temp = items.get(start);
            items.set(start, items.get(i));
            items.set(i, temp);
            
            // Recurse
            generatePermutationsHelper(items, start + 1, result);
            
            // Swap back
            temp = items.get(start);
            items.set(start, items.get(i));
            items.set(i, temp);
        }
    }
    
    private List<House> createHouses(List<Color> colors, List<Nationality> nationalities, 
                                    List<Pet> pets, List<Drink> drinks, List<Hobby> hobbies) {
        List<House> houses = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            houses.add(new House(colors.get(i), nationalities.get(i), pets.get(i), drinks.get(i), hobbies.get(i)));
        }
        return houses;
    }
    
    private boolean isValidSolution(List<House> houses) {
        // 2. The Englishman lives in the red house
        if (!hasMatch(houses, h -> h.nationality == Nationality.ENGLISHMAN && h.color == Color.RED)) return false;
        
        // 3. The Spaniard owns the dog
        if (!hasMatch(houses, h -> h.nationality == Nationality.SPANIARD && h.pet == Pet.DOG)) return false;
        
        // 4. The person in the green house drinks coffee
        if (!hasMatch(houses, h -> h.color == Color.GREEN && h.drink == Drink.COFFEE)) return false;
        
        // 5. The Ukrainian drinks tea
        if (!hasMatch(houses, h -> h.nationality == Nationality.UKRAINIAN && h.drink == Drink.TEA)) return false;
        
        // 6. The green house is immediately to the right of the ivory house
        if (!hasAdjacentMatch(houses, 
            h1 -> h1.color == Color.IVORY, 
            h2 -> h2.color == Color.GREEN, 
            true)) return false;
        
        // 7. The snail owner likes to go dancing
        if (!hasMatch(houses, h -> h.pet == Pet.SNAIL && h.hobby == Hobby.DANCING)) return false;
        
        // 8. The person in the yellow house is a painter
        if (!hasMatch(houses, h -> h.color == Color.YELLOW && h.hobby == Hobby.PAINTING)) return false;
        
        // 11. The person who enjoys reading lives in the house next to the person with the fox
        if (!hasAdjacentMatch(houses, 
            h1 -> h1.hobby == Hobby.READING, 
            h2 -> h2.pet == Pet.FOX, 
            false)) return false;
        
        // 12. The painter's house is next to the house with the horse
        if (!hasAdjacentMatch(houses, 
            h1 -> h1.hobby == Hobby.PAINTING, 
            h2 -> h2.pet == Pet.HORSE, 
            false)) return false;
        
        // 13. The person who plays football drinks orange juice
        if (!hasMatch(houses, h -> h.hobby == Hobby.FOOTBALL && h.drink == Drink.ORANGE_JUICE)) return false;
        
        // 14. The Japanese person plays chess
        if (!hasMatch(houses, h -> h.nationality == Nationality.JAPANESE && h.hobby == Hobby.CHESS)) return false;
        
        // 15. The Norwegian lives next to the blue house
        if (!hasAdjacentMatch(houses, 
            h1 -> h1.nationality == Nationality.NORWEGIAN, 
            h2 -> h2.color == Color.BLUE, 
            false)) return false;
        
        return true;
    }
    
    private boolean hasMatch(List<House> houses, Predicate<House> predicate) {
        return houses.stream().anyMatch(predicate::test);
    }
    
    private boolean hasAdjacentMatch(List<House> houses, Predicate<House> pred1, Predicate<House> pred2, boolean directional) {
        for (int i = 0; i < houses.size() - 1; i++) {
            if (pred1.test(houses.get(i)) && pred2.test(houses.get(i + 1))) {
                return true;
            }
            if (!directional && pred2.test(houses.get(i)) && pred1.test(houses.get(i + 1))) {
                return true;
            }
        }
        return false;
    }
    
    // Functional interface for predicates
    private interface Predicate<T> {
        boolean test(T t);
    }
    
    String getWaterDrinker() {
        for (House house : houses) {
            if (house.drink == Drink.WATER) {
                return house.nationality.toString().charAt(0) + house.nationality.toString().substring(1).toLowerCase();
            }
        }
        throw new IllegalStateException("No water drinker found");
    }
    
    String getZebraOwner() {
        for (House house : houses) {
            if (house.pet == Pet.ZEBRA) {
                return house.nationality.toString().charAt(0) + house.nationality.toString().substring(1).toLowerCase();
            }
        }
        throw new IllegalStateException("No zebra owner found");
    }
}
