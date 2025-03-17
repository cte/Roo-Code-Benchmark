def best_hands(hands):
    """
    Determine the best poker hand(s) from a list of hands.
    
    Args:
        hands: A list of strings representing poker hands.
              Each hand is a string of 5 cards separated by spaces.
              Each card is represented by a value and a suit.
              Values are: 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K, A
              Suits are: S (Spades), H (Hearts), D (Diamonds), C (Clubs)
    
    Returns:
        A list of the best hand(s) from the input.
    """
    if not hands:
        return []
    
    if len(hands) == 1:
        return hands
    
    # Parse and evaluate each hand
    evaluated_hands = [(hand, evaluate_hand(hand)) for hand in hands]
    
    # Sort hands by their rank (higher rank = better hand)
    evaluated_hands.sort(key=lambda x: x[1], reverse=True)
    
    # Get the highest rank
    best_rank = evaluated_hands[0][1]
    
    # Return all hands that have the highest rank
    return [hand for hand, rank in evaluated_hands if rank == best_rank]


def evaluate_hand(hand):
    """
    Evaluate a poker hand and return its rank as a tuple for comparison.
    The tuple is structured to allow for direct comparison with other hands.
    
    Args:
        hand: A string representing a poker hand.
    
    Returns:
        A tuple representing the hand's rank, where higher values indicate better hands.
    """
    cards = parse_hand(hand)
    
    # Check for each hand type from highest to lowest
    if is_straight_flush(cards):
        return (8, high_card_values(cards, straight_high_card(cards)))
    
    if is_four_of_a_kind(cards):
        return (7, four_of_a_kind_values(cards))
    
    if is_full_house(cards):
        return (6, full_house_values(cards))
    
    if is_flush(cards):
        return (5, high_card_values(cards))
    
    if is_straight(cards):
        return (4, high_card_values(cards, straight_high_card(cards)))
    
    if is_three_of_a_kind(cards):
        return (3, three_of_a_kind_values(cards))
    
    if is_two_pair(cards):
        return (2, two_pair_values(cards))
    
    if is_one_pair(cards):
        return (1, one_pair_values(cards))
    
    # High card
    return (0, high_card_values(cards))


def parse_hand(hand):
    """
    Parse a hand string into a list of (value, suit) tuples.
    
    Args:
        hand: A string representing a poker hand.
    
    Returns:
        A list of (value, suit) tuples.
    """
    cards = hand.split()
    parsed_cards = []
    
    for card in cards:
        if card[0] == '1' and len(card) > 2:  # Handle '10'
            value, suit = card[:2], card[2]
        else:
            value, suit = card[0], card[1]
        
        parsed_cards.append((value, suit))
    
    return parsed_cards


def card_value(value):
    """
    Convert a card value to a numeric value for comparison.
    
    Args:
        value: A string representing a card value.
    
    Returns:
        An integer representing the card's value.
    """
    values = {
        '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9,
        '10': 10, 'J': 11, 'Q': 12, 'K': 13, 'A': 14
    }
    return values[value]


def is_straight_flush(cards):
    """
    Check if a hand is a straight flush.
    
    Args:
        cards: A list of (value, suit) tuples.
    
    Returns:
        True if the hand is a straight flush, False otherwise.
    """
    return is_straight(cards) and is_flush(cards)


def is_four_of_a_kind(cards):
    """
    Check if a hand is four of a kind.
    
    Args:
        cards: A list of (value, suit) tuples.
    
    Returns:
        True if the hand is four of a kind, False otherwise.
    """
    values = [card[0] for card in cards]
    for value in set(values):
        if values.count(value) == 4:
            return True
    return False


def is_full_house(cards):
    """
    Check if a hand is a full house.
    
    Args:
        cards: A list of (value, suit) tuples.
    
    Returns:
        True if the hand is a full house, False otherwise.
    """
    values = [card[0] for card in cards]
    value_counts = {value: values.count(value) for value in set(values)}
    return 3 in value_counts.values() and 2 in value_counts.values()


def is_flush(cards):
    """
    Check if a hand is a flush.
    
    Args:
        cards: A list of (value, suit) tuples.
    
    Returns:
        True if the hand is a flush, False otherwise.
    """
    suits = [card[1] for card in cards]
    return len(set(suits)) == 1


def is_straight(cards):
    """
    Check if a hand is a straight.
    
    Args:
        cards: A list of (value, suit) tuples.
    
    Returns:
        True if the hand is a straight, False otherwise.
    """
    values = sorted([card_value(card[0]) for card in cards])
    
    # Check for A-5 straight
    if values == [2, 3, 4, 5, 14]:
        return True
    
    # Check for regular straight
    return values == list(range(min(values), max(values) + 1))


def is_three_of_a_kind(cards):
    """
    Check if a hand is three of a kind.
    
    Args:
        cards: A list of (value, suit) tuples.
    
    Returns:
        True if the hand is three of a kind, False otherwise.
    """
    values = [card[0] for card in cards]
    for value in set(values):
        if values.count(value) == 3:
            return True
    return False


def is_two_pair(cards):
    """
    Check if a hand is two pair.
    
    Args:
        cards: A list of (value, suit) tuples.
    
    Returns:
        True if the hand is two pair, False otherwise.
    """
    values = [card[0] for card in cards]
    pairs = [value for value in set(values) if values.count(value) == 2]
    return len(pairs) == 2


def is_one_pair(cards):
    """
    Check if a hand is one pair.
    
    Args:
        cards: A list of (value, suit) tuples.
    
    Returns:
        True if the hand is one pair, False otherwise.
    """
    values = [card[0] for card in cards]
    for value in set(values):
        if values.count(value) == 2:
            return True
    return False


def straight_high_card(cards):
    """
    Get the high card of a straight.
    
    Args:
        cards: A list of (value, suit) tuples.
    
    Returns:
        The high card value of the straight.
    """
    values = [card_value(card[0]) for card in cards]
    
    # Handle A-5 straight (where A is low)
    if sorted(values) == [2, 3, 4, 5, 14]:
        return 5
    
    return max(values)


def high_card_values(cards, high_value=None):
    """
    Get the values of the cards in a hand, sorted by value.
    
    Args:
        cards: A list of (value, suit) tuples.
        high_value: Optional high card value for straights.
    
    Returns:
        A tuple of card values, sorted in descending order.
    """
    values = [card_value(card[0]) for card in cards]
    
    # Handle A-5 straight (where A is low)
    if high_value == 5 and 14 in values:
        values.remove(14)
        values.append(1)
    
    return tuple(sorted(values, reverse=True))


def four_of_a_kind_values(cards):
    """
    Get the values for a four of a kind hand.
    
    Args:
        cards: A list of (value, suit) tuples.
    
    Returns:
        A tuple of (four of a kind value, kicker value).
    """
    values = [card[0] for card in cards]
    for value in set(values):
        if values.count(value) == 4:
            four_value = card_value(value)
            kicker = [card_value(v) for v in values if v != value][0]
            return (four_value, kicker)


def full_house_values(cards):
    """
    Get the values for a full house hand.
    
    Args:
        cards: A list of (value, suit) tuples.
    
    Returns:
        A tuple of (three of a kind value, pair value).
    """
    values = [card[0] for card in cards]
    value_counts = {value: values.count(value) for value in set(values)}
    
    three_value = [card_value(v) for v, count in value_counts.items() if count == 3][0]
    pair_value = [card_value(v) for v, count in value_counts.items() if count == 2][0]
    
    return (three_value, pair_value)


def three_of_a_kind_values(cards):
    """
    Get the values for a three of a kind hand.
    
    Args:
        cards: A list of (value, suit) tuples.
    
    Returns:
        A tuple of (three of a kind value, high kicker, low kicker).
    """
    values = [card[0] for card in cards]
    for value in set(values):
        if values.count(value) == 3:
            three_value = card_value(value)
            kickers = sorted([card_value(v) for v in values if v != value], reverse=True)
            return (three_value, kickers[0], kickers[1])


def two_pair_values(cards):
    """
    Get the values for a two pair hand.
    
    Args:
        cards: A list of (value, suit) tuples.
    
    Returns:
        A tuple of (high pair value, low pair value, kicker value).
    """
    values = [card[0] for card in cards]
    pairs = [value for value in set(values) if values.count(value) == 2]
    pair_values = sorted([card_value(p) for p in pairs], reverse=True)
    
    kicker = [card_value(v) for v in values if values.count(v) == 1][0]
    
    return (pair_values[0], pair_values[1], kicker)


def one_pair_values(cards):
    """
    Get the values for a one pair hand.
    
    Args:
        cards: A list of (value, suit) tuples.
    
    Returns:
        A tuple of (pair value, high kicker, middle kicker, low kicker).
    """
    values = [card[0] for card in cards]
    for value in set(values):
        if values.count(value) == 2:
            pair_value = card_value(value)
            kickers = sorted([card_value(v) for v in values if v != value], reverse=True)
            return (pair_value, kickers[0], kickers[1], kickers[2])
