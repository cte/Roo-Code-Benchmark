package poker

import (
	"errors"
	"sort"
	"strings"
	"unicode/utf8"
)

// Card represents a playing card with a rank and suit
type Card struct {
	Rank int
	Suit rune
}

// Hand represents a poker hand with cards and its category
type Hand struct {
	Cards    []Card
	Original string
	Category int
	Ranks    []int // Used for comparing hands of the same category
}

// Categories of poker hands (in ascending order of value)
const (
	HighCard = iota
	OnePair
	TwoPair
	ThreeOfAKind
	Straight
	Flush
	FullHouse
	FourOfAKind
	StraightFlush
)

// BestHand returns the best poker hand(s) from a list of hands
func BestHand(hands []string) ([]string, error) {
	if len(hands) == 0 {
		return nil, errors.New("no hands provided")
	}

	parsedHands := make([]Hand, 0, len(hands))

	// Parse and validate each hand
	for _, handStr := range hands {
		hand, err := parseHand(handStr)
		if err != nil {
			return nil, err
		}
		parsedHands = append(parsedHands, hand)
	}

	// Find the best hand(s)
	bestHands := findBestHands(parsedHands)

	// Extract the original string representations
	result := make([]string, len(bestHands))
	for i, hand := range bestHands {
		result[i] = hand.Original
	}

	return result, nil
}

// parseHand parses a hand string into a structured Hand
func parseHand(handStr string) (Hand, error) {
	cardStrs := strings.Fields(handStr)
	
	// Check if the hand has exactly 5 cards
	if len(cardStrs) != 5 {
		if len(cardStrs) < 5 {
			return Hand{}, errors.New("too few cards")
		}
		return Hand{}, errors.New("too many cards")
	}

	cards := make([]Card, 5)
	
	// Parse each card
	for i, cardStr := range cardStrs {
		card, err := parseCard(cardStr)
		if err != nil {
			return Hand{}, err
		}
		cards[i] = card
	}

	// Sort cards by rank (descending)
	sort.Slice(cards, func(i, j int) bool {
		return cards[i].Rank > cards[j].Rank
	})

	// Create the hand
	hand := Hand{
		Cards:    cards,
		Original: handStr,
	}

	// Categorize the hand
	categorizeHand(&hand)

	return hand, nil
}

// parseCard parses a card string into a Card
func parseCard(cardStr string) (Card, error) {
	if len(cardStr) < 2 {
		return Card{}, errors.New("invalid card format")
	}

	// Extract the last rune as the suit
	lastRuneIndex := strings.LastIndexFunc(cardStr, func(r rune) bool { return true })
	if lastRuneIndex < 0 {
		return Card{}, errors.New("invalid card format")
	}

	suitRune, _ := utf8.DecodeLastRuneInString(cardStr)
	rankStr := cardStr[:lastRuneIndex]

	// Validate suit
	if !isValidSuit(suitRune) {
		return Card{}, errors.New("invalid suit")
	}

	// Parse rank
	rank, err := parseRank(rankStr)
	if err != nil {
		return Card{}, err
	}

	return Card{Rank: rank, Suit: suitRune}, nil
}

// isValidSuit checks if a rune is a valid card suit
func isValidSuit(r rune) bool {
	validSuits := []rune{'♤', '♡', '♢', '♧'}
	for _, suit := range validSuits {
		if r == suit {
			return true
		}
	}
	return false
}

// parseRank parses a rank string into an integer
func parseRank(rankStr string) (int, error) {
	switch rankStr {
	case "A":
		return 14, nil
	case "K":
		return 13, nil
	case "Q":
		return 12, nil
	case "J":
		return 11, nil
	case "10":
		return 10, nil
	case "9":
		return 9, nil
	case "8":
		return 8, nil
	case "7":
		return 7, nil
	case "6":
		return 6, nil
	case "5":
		return 5, nil
	case "4":
		return 4, nil
	case "3":
		return 3, nil
	case "2":
		return 2, nil
	default:
		return 0, errors.New("invalid rank")
	}
}

// categorizeHand determines the category of a hand and sets its ranks for comparison
func categorizeHand(hand *Hand) {
	// Check for flush (all cards of the same suit)
	isFlush := true
	for i := 1; i < 5; i++ {
		if hand.Cards[i].Suit != hand.Cards[0].Suit {
			isFlush = false
			break
		}
	}

	// Check for straight (consecutive ranks)
	isStraight := true
	// Special case for A-5 straight (where A is treated as 1)
	if hand.Cards[0].Rank == 14 && hand.Cards[1].Rank == 5 && hand.Cards[2].Rank == 4 &&
		hand.Cards[3].Rank == 3 && hand.Cards[4].Rank == 2 {
		// A-5 straight
		hand.Ranks = []int{5, 4, 3, 2, 1} // Use 5 as the highest rank for A-5 straight
	} else {
		// Normal straight check
		for i := 1; i < 5; i++ {
			if hand.Cards[i-1].Rank != hand.Cards[i].Rank+1 {
				isStraight = false
				break
			}
		}
		
		if isStraight {
			// Use the highest card for comparison
			hand.Ranks = []int{hand.Cards[0].Rank}
		}
	}

	// If it's both a straight and a flush, it's a straight flush
	if isStraight && isFlush {
		hand.Category = StraightFlush
		return
	}

	// Count occurrences of each rank
	rankCounts := make(map[int]int)
	for _, card := range hand.Cards {
		rankCounts[card.Rank]++
	}

	// Find groups of cards with the same rank
	pairs := make([]int, 0)
	threes := make([]int, 0)
	fours := make([]int, 0)

	for rank, count := range rankCounts {
		switch count {
		case 2:
			pairs = append(pairs, rank)
		case 3:
			threes = append(threes, rank)
		case 4:
			fours = append(fours, rank)
		}
	}

	// Sort groups by rank (descending)
	sort.Slice(pairs, func(i, j int) bool {
		return pairs[i] > pairs[j]
	})
	sort.Slice(threes, func(i, j int) bool {
		return threes[i] > threes[j]
	})
	sort.Slice(fours, func(i, j int) bool {
		return fours[i] > fours[j]
	})

	// Check for four of a kind
	if len(fours) == 1 {
		hand.Category = FourOfAKind
		// The rank of the four cards, followed by the rank of the kicker
		hand.Ranks = []int{fours[0]}
		for _, card := range hand.Cards {
			if card.Rank != fours[0] {
				hand.Ranks = append(hand.Ranks, card.Rank)
				break
			}
		}
		return
	}

	// Check for full house (three of a kind and a pair)
	if len(threes) == 1 && len(pairs) == 1 {
		hand.Category = FullHouse
		// The rank of the three cards, followed by the rank of the pair
		hand.Ranks = []int{threes[0], pairs[0]}
		return
	}

	// Check for flush
	if isFlush {
		hand.Category = Flush
		// All card ranks in descending order
		hand.Ranks = make([]int, 5)
		for i, card := range hand.Cards {
			hand.Ranks[i] = card.Rank
		}
		return
	}

	// Check for straight
	if isStraight {
		hand.Category = Straight
		// Already set hand.Ranks in the straight check
		return
	}

	// Check for three of a kind
	if len(threes) == 1 {
		hand.Category = ThreeOfAKind
		// The rank of the three cards, followed by the ranks of the kickers
		hand.Ranks = []int{threes[0]}
		for _, card := range hand.Cards {
			if card.Rank != threes[0] {
				hand.Ranks = append(hand.Ranks, card.Rank)
			}
		}
		return
	}

	// Check for two pair
	if len(pairs) == 2 {
		hand.Category = TwoPair
		// The ranks of the two pairs, followed by the rank of the kicker
		hand.Ranks = []int{pairs[0], pairs[1]}
		for _, card := range hand.Cards {
			if card.Rank != pairs[0] && card.Rank != pairs[1] {
				hand.Ranks = append(hand.Ranks, card.Rank)
				break
			}
		}
		return
	}

	// Check for one pair
	if len(pairs) == 1 {
		hand.Category = OnePair
		// The rank of the pair, followed by the ranks of the kickers
		hand.Ranks = []int{pairs[0]}
		for _, card := range hand.Cards {
			if card.Rank != pairs[0] {
				hand.Ranks = append(hand.Ranks, card.Rank)
			}
		}
		return
	}

	// High card
	hand.Category = HighCard
	// All card ranks in descending order
	hand.Ranks = make([]int, 5)
	for i, card := range hand.Cards {
		hand.Ranks[i] = card.Rank
	}
}

// findBestHands finds the best hand(s) from a list of hands
func findBestHands(hands []Hand) []Hand {
	if len(hands) == 0 {
		return nil
	}

	// Start with the first hand as the best
	bestHands := []Hand{hands[0]}
	bestCategory := hands[0].Category

	// Compare each hand to the current best
	for i := 1; i < len(hands); i++ {
		hand := hands[i]
		
		// If this hand has a higher category, it becomes the new best
		if hand.Category > bestCategory {
			bestHands = []Hand{hand}
			bestCategory = hand.Category
			continue
		}

		// If this hand has a lower category, skip it
		if hand.Category < bestCategory {
			continue
		}

		// If this hand has the same category, compare the ranks
		comparison := compareRanks(hand.Ranks, bestHands[0].Ranks)
		
		if comparison > 0 {
			// This hand is better
			bestHands = []Hand{hand}
		} else if comparison == 0 {
			// This hand is tied with the current best
			bestHands = append(bestHands, hand)
		}
	}

	return bestHands
}

// compareRanks compares two sets of ranks and returns:
// 1 if ranks1 is better than ranks2
// 0 if they are equal
// -1 if ranks2 is better than ranks1
func compareRanks(ranks1, ranks2 []int) int {
	for i := 0; i < len(ranks1) && i < len(ranks2); i++ {
		if ranks1[i] > ranks2[i] {
			return 1
		}
		if ranks1[i] < ranks2[i] {
			return -1
		}
	}
	return 0
}
