import java.util.*;
import java.util.stream.Collectors;

class Poker {
    private final List<Hand> hands;

    Poker(List<String> handStrings) {
        this.hands = handStrings.stream()
                .map(Hand::new)
                .collect(Collectors.toList());
    }

    List<String> getBestHands() {
        if (hands.isEmpty()) {
            return Collections.emptyList();
        }

        Hand bestHand = Collections.max(hands);
        return hands.stream()
                .filter(hand -> hand.compareTo(bestHand) == 0)
                .map(hand -> hand.originalString)
                .collect(Collectors.toList());
    }

    private static class Hand implements Comparable<Hand> {
        private final String originalString;
        private final List<Card> cards;
        private final HandCategory category;
        private final List<Integer> tieBreakers;

        Hand(String handString) {
            this.originalString = handString;
            this.cards = parseCards(handString);
            this.category = categorizeHand();
            this.tieBreakers = calculateTieBreakers();
        }

        private List<Card> parseCards(String handString) {
            return Arrays.stream(handString.split(" "))
                    .map(Card::new)
                    .sorted(Comparator.comparing(Card::getValue).reversed())
                    .collect(Collectors.toList());
        }

        private HandCategory categorizeHand() {
            if (isStraightFlush()) return HandCategory.STRAIGHT_FLUSH;
            if (isFourOfAKind()) return HandCategory.FOUR_OF_A_KIND;
            if (isFullHouse()) return HandCategory.FULL_HOUSE;
            if (isFlush()) return HandCategory.FLUSH;
            if (isStraight()) return HandCategory.STRAIGHT;
            if (isThreeOfAKind()) return HandCategory.THREE_OF_A_KIND;
            if (isTwoPair()) return HandCategory.TWO_PAIR;
            if (isOnePair()) return HandCategory.ONE_PAIR;
            return HandCategory.HIGH_CARD;
        }

        private boolean isStraightFlush() {
            return isFlush() && isStraight();
        }

        private boolean isFourOfAKind() {
            Map<Integer, Long> valueCounts = getValueCounts();
            return valueCounts.values().stream().anyMatch(count -> count == 4);
        }

        private boolean isFullHouse() {
            Map<Integer, Long> valueCounts = getValueCounts();
            return valueCounts.values().stream().anyMatch(count -> count == 3) &&
                   valueCounts.values().stream().anyMatch(count -> count == 2);
        }

        private boolean isFlush() {
            return cards.stream().map(Card::getSuit).distinct().count() == 1;
        }

        private boolean isStraight() {
            // Sort cards by value
            List<Card> sortedCards = new ArrayList<>(cards);
            sortedCards.sort(Comparator.comparing(Card::getValue));

            // Check for A-5 straight
            if (hasAceLowStraight(sortedCards)) {
                return true;
            }

            // Check for regular straight
            for (int i = 0; i < sortedCards.size() - 1; i++) {
                if (sortedCards.get(i).getValue() + 1 != sortedCards.get(i + 1).getValue()) {
                    return false;
                }
            }
            return true;
        }

        private boolean hasAceLowStraight(List<Card> cards) {
            // Check for A-5 straight (A,2,3,4,5)
            // Since our cards are sorted in descending order (high to low)
            // We need to check if we have A,5,4,3,2
            boolean hasAce = false;
            boolean has5 = false;
            boolean has4 = false;
            boolean has3 = false;
            boolean has2 = false;
            
            for (Card card : cards) {
                int value = card.getValue();
                if (value == 14) hasAce = true;
                else if (value == 5) has5 = true;
                else if (value == 4) has4 = true;
                else if (value == 3) has3 = true;
                else if (value == 2) has2 = true;
            }
            
            return hasAce && has2 && has3 && has4 && has5;
        }

        private boolean isThreeOfAKind() {
            Map<Integer, Long> valueCounts = getValueCounts();
            return valueCounts.values().stream().anyMatch(count -> count == 3) &&
                   valueCounts.values().stream().noneMatch(count -> count == 2);
        }

        private boolean isTwoPair() {
            Map<Integer, Long> valueCounts = getValueCounts();
            return valueCounts.values().stream().filter(count -> count == 2).count() == 2;
        }

        private boolean isOnePair() {
            Map<Integer, Long> valueCounts = getValueCounts();
            return valueCounts.values().stream().filter(count -> count == 2).count() == 1 &&
                   valueCounts.values().stream().noneMatch(count -> count > 2);
        }

        private Map<Integer, Long> getValueCounts() {
            return cards.stream()
                    .collect(Collectors.groupingBy(Card::getValue, Collectors.counting()));
        }

        private List<Integer> calculateTieBreakers() {
            List<Integer> tieBreakers = new ArrayList<>();
            Map<Integer, Long> valueCounts = getValueCounts();
            
            switch (category) {
                case STRAIGHT_FLUSH:
                case STRAIGHT:
                    // For A-5 straight, it's the lowest straight (high card 5)
                    if (hasAceLowStraight(new ArrayList<>(cards))) {
                        // Use 5 as the value, but make it lower than any other straight
                        tieBreakers.add(1); // Lower than any other straight
                    } else {
                        tieBreakers.add(cards.get(0).getValue());
                    }
                    break;
                    
                case FOUR_OF_A_KIND:
                    // First the value of the four cards, then the kicker
                    int fourValue = valueCounts.entrySet().stream()
                            .filter(e -> e.getValue() == 4)
                            .map(Map.Entry::getKey)
                            .findFirst().orElse(0);
                    tieBreakers.add(fourValue);
                    
                    // Add the kicker
                    valueCounts.keySet().stream()
                            .filter(v -> v != fourValue)
                            .forEach(tieBreakers::add);
                    break;
                    
                case FULL_HOUSE:
                    // First the value of the three cards, then the pair
                    int threeValue = valueCounts.entrySet().stream()
                            .filter(e -> e.getValue() == 3)
                            .map(Map.Entry::getKey)
                            .findFirst().orElse(0);
                    tieBreakers.add(threeValue);
                    
                    int pairValue = valueCounts.entrySet().stream()
                            .filter(e -> e.getValue() == 2)
                            .map(Map.Entry::getKey)
                            .findFirst().orElse(0);
                    tieBreakers.add(pairValue);
                    break;
                    
                case FLUSH:
                case HIGH_CARD:
                    // All cards in descending order
                    cards.forEach(card -> tieBreakers.add(card.getValue()));
                    break;
                    
                case THREE_OF_A_KIND:
                    // First the value of the three cards, then the kickers
                    int tripValue = valueCounts.entrySet().stream()
                            .filter(e -> e.getValue() == 3)
                            .map(Map.Entry::getKey)
                            .findFirst().orElse(0);
                    tieBreakers.add(tripValue);
                    
                    // Add the kickers in descending order
                    cards.stream()
                            .map(Card::getValue)
                            .filter(v -> v != tripValue)
                            .sorted(Comparator.reverseOrder())
                            .forEach(tieBreakers::add);
                    break;
                    
                case TWO_PAIR:
                    // First the higher pair, then the lower pair, then the kicker
                    List<Integer> pairValues = valueCounts.entrySet().stream()
                            .filter(e -> e.getValue() == 2)
                            .map(Map.Entry::getKey)
                            .sorted(Comparator.reverseOrder())
                            .collect(Collectors.toList());
                    
                    tieBreakers.addAll(pairValues);
                    
                    // Add the kicker
                    cards.stream()
                            .map(Card::getValue)
                            .filter(v -> !pairValues.contains(v))
                            .forEach(tieBreakers::add);
                    break;
                    
                case ONE_PAIR:
                    // First the pair value, then the kickers
                    int pairVal = valueCounts.entrySet().stream()
                            .filter(e -> e.getValue() == 2)
                            .map(Map.Entry::getKey)
                            .findFirst().orElse(0);
                    tieBreakers.add(pairVal);
                    
                    // Add the kickers in descending order
                    cards.stream()
                            .map(Card::getValue)
                            .filter(v -> v != pairVal)
                            .sorted(Comparator.reverseOrder())
                            .forEach(tieBreakers::add);
                    break;
            }
            
            return tieBreakers;
        }

        @Override
        public int compareTo(Hand other) {
            // First compare by category
            int categoryComparison = category.compareTo(other.category);
            if (categoryComparison != 0) {
                return categoryComparison;
            }
            
            // If same category, compare tie breakers
            for (int i = 0; i < Math.min(tieBreakers.size(), other.tieBreakers.size()); i++) {
                int comparison = Integer.compare(tieBreakers.get(i), other.tieBreakers.get(i));
                if (comparison != 0) {
                    return comparison;
                }
            }
            
            return 0; // Hands are equal
        }
    }

    private static class Card {
        private final int value;
        private final char suit;

        Card(String card) {
            String valueStr = card.substring(0, card.length() - 1);
            this.value = parseValue(valueStr);
            this.suit = card.charAt(card.length() - 1);
        }

        private int parseValue(String valueStr) {
            switch (valueStr) {
                case "A": return 14;
                case "K": return 13;
                case "Q": return 12;
                case "J": return 11;
                case "10": return 10;
                default: return Integer.parseInt(valueStr);
            }
        }

        int getValue() {
            return value;
        }

        char getSuit() {
            return suit;
        }
    }

    private enum HandCategory {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        STRAIGHT,
        FLUSH,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        STRAIGHT_FLUSH
    }
}