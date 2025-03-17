use std::cmp::Ordering;
use std::collections::HashMap;

/// Given a list of poker hands, return a list of those hands which win.
///
/// Note the type signature: this function should return _the same_ reference to
/// the winning hand(s) as were passed in, not reconstructed strings which happen to be equal.
pub fn winning_hands<'a>(hands: &[&'a str]) -> Vec<&'a str> {
    if hands.is_empty() {
        return Vec::new();
    }
    
    if hands.len() == 1 {
        return vec![hands[0]];
    }
    
    // Parse all hands
    let parsed_hands: Vec<_> = hands.iter()
        .map(|&hand_str| {
            let hand = parse_hand(hand_str);
            (hand_str, hand)
        })
        .collect();
    
    // Find the maximum hand(s)
    let mut max_hands = vec![parsed_hands[0].clone()];
    
    for hand in &parsed_hands[1..] {
        match compare_hands(&hand.1, &max_hands[0].1) {
            Ordering::Greater => {
                max_hands = vec![hand.clone()];
            }
            Ordering::Equal => {
                max_hands.push(hand.clone());
            }
            Ordering::Less => {}
        }
    }
    
    // Return the original string references
    max_hands.into_iter().map(|(hand_str, _)| hand_str).collect()
}

// Card representation
#[derive(Debug, Clone, Copy, PartialEq, Eq)]
struct Card {
    rank: Rank,
    suit: Suit,
}

#[derive(Debug, Clone, Copy, PartialEq, Eq, PartialOrd, Ord, Hash)]
enum Rank {
    Two = 2,
    Three = 3,
    Four = 4,
    Five = 5,
    Six = 6,
    Seven = 7,
    Eight = 8,
    Nine = 9,
    Ten = 10,
    Jack = 11,
    Queen = 12,
    King = 13,
    Ace = 14,
}

#[derive(Debug, Clone, Copy, PartialEq, Eq, PartialOrd, Ord)]
enum Suit {
    Clubs,
    Diamonds,
    Hearts,
    Spades,
}

// Hand representation
#[derive(Debug, Clone, PartialEq, Eq)]
struct Hand {
    cards: Vec<Card>,
    hand_type: HandType,
}

#[derive(Debug, Clone, PartialEq, Eq, PartialOrd, Ord)]
enum HandType {
    HighCard(Vec<Rank>),
    OnePair(Rank, Vec<Rank>),
    TwoPair(Rank, Rank, Rank),
    ThreeOfAKind(Rank, Vec<Rank>),
    Straight(Rank),
    Flush(Vec<Rank>),
    FullHouse(Rank, Rank),
    FourOfAKind(Rank, Rank),
    StraightFlush(Rank),
}

// Parse a card string like "AS" into a Card struct
fn parse_card(card_str: &str) -> Card {
    let mut chars = card_str.chars();
    
    // Parse rank
    let rank_char = chars.next().unwrap();
    let rank = match rank_char {
        '2' => Rank::Two,
        '3' => Rank::Three,
        '4' => Rank::Four,
        '5' => Rank::Five,
        '6' => Rank::Six,
        '7' => Rank::Seven,
        '8' => Rank::Eight,
        '9' => Rank::Nine,
        '1' => {
            // Must be "10"
            chars.next();
            Rank::Ten
        },
        'J' => Rank::Jack,
        'Q' => Rank::Queen,
        'K' => Rank::King,
        'A' => Rank::Ace,
        _ => panic!("Invalid rank: {}", rank_char),
    };
    
    // Parse suit
    let suit_char = chars.next().unwrap();
    let suit = match suit_char {
        'C' => Suit::Clubs,
        'D' => Suit::Diamonds,
        'H' => Suit::Hearts,
        'S' => Suit::Spades,
        _ => panic!("Invalid suit: {}", suit_char),
    };
    
    Card { rank, suit }
}

// Parse a hand string like "AS KH QC JD 10S" into a Hand struct
fn parse_hand(hand_str: &str) -> Hand {
    let cards: Vec<Card> = hand_str.split_whitespace()
        .map(parse_card)
        .collect();
    
    let hand_type = determine_hand_type(&cards);
    
    Hand { cards, hand_type }
}

// Determine the type of a hand
fn determine_hand_type(cards: &[Card]) -> HandType {
    // Count occurrences of each rank
    let mut rank_counts: HashMap<Rank, usize> = HashMap::new();
    for card in cards {
        *rank_counts.entry(card.rank).or_insert(0) += 1;
    }
    
    // Check if all cards have the same suit (flush)
    let is_flush = cards.windows(2).all(|w| w[0].suit == w[1].suit);
    
    // Check if the cards form a straight
    let mut ranks: Vec<Rank> = cards.iter().map(|card| card.rank).collect();
    ranks.sort_by(|a, b| b.cmp(a)); // Sort in descending order
    
    // Handle special case: A-5-4-3-2 straight
    let is_low_ace_straight = ranks == vec![Rank::Ace, Rank::Five, Rank::Four, Rank::Three, Rank::Two];
    
    // Regular straight check
    let is_straight = is_low_ace_straight || 
        ranks.windows(2).all(|w| w[0] as usize == w[1] as usize + 1);
    
    // Determine the highest rank in a straight
    let straight_high_rank = if is_low_ace_straight {
        Rank::Five
    } else if is_straight {
        ranks[0]
    } else {
        Rank::Two // Placeholder, not used
    };
    
    // Straight flush
    if is_straight && is_flush {
        return HandType::StraightFlush(straight_high_rank);
    }
    
    // Four of a kind
    if let Some(&rank) = rank_counts.iter().find_map(|(rank, &count)| if count == 4 { Some(rank) } else { None }) {
        let kicker = *rank_counts.iter()
            .find_map(|(r, &count)| if count == 1 { Some(r) } else { None })
            .unwrap();
        return HandType::FourOfAKind(rank, kicker);
    }
    
    // Full house
    if let Some(&three_rank) = rank_counts.iter().find_map(|(rank, &count)| if count == 3 { Some(rank) } else { None }) {
        if let Some(&pair_rank) = rank_counts.iter().find_map(|(rank, &count)| if count == 2 { Some(rank) } else { None }) {
            return HandType::FullHouse(three_rank, pair_rank);
        }
    }
    
    // Flush
    if is_flush {
        return HandType::Flush(ranks.clone());
    }
    
    // Straight
    if is_straight {
        return HandType::Straight(straight_high_rank);
    }
    
    // Three of a kind
    if let Some(&three_rank) = rank_counts.iter().find_map(|(rank, &count)| if count == 3 { Some(rank) } else { None }) {
        let mut kickers: Vec<Rank> = rank_counts.iter()
            .filter_map(|(rank, &count)| if count == 1 { Some(*rank) } else { None })
            .collect();
        kickers.sort_by(|a, b| b.cmp(a));
        return HandType::ThreeOfAKind(three_rank, kickers);
    }
    
    // Two pair
    let pairs: Vec<Rank> = rank_counts.iter()
        .filter_map(|(rank, &count)| if count == 2 { Some(*rank) } else { None })
        .collect();
    
    if pairs.len() == 2 {
        let mut sorted_pairs = pairs.clone();
        sorted_pairs.sort_by(|a, b| b.cmp(a));
        
        let kicker = rank_counts.iter()
            .find_map(|(rank, &count)| if count == 1 { Some(*rank) } else { None })
            .unwrap();
        
        return HandType::TwoPair(sorted_pairs[0], sorted_pairs[1], kicker);
    }
    
    // One pair
    if let Some(&pair_rank) = rank_counts.iter().find_map(|(rank, &count)| if count == 2 { Some(rank) } else { None }) {
        let mut kickers: Vec<Rank> = rank_counts.iter()
            .filter_map(|(rank, &count)| if count == 1 { Some(*rank) } else { None })
            .collect();
        kickers.sort_by(|a, b| b.cmp(a));
        return HandType::OnePair(pair_rank, kickers);
    }
    
    // High card
    HandType::HighCard(ranks)
}

// Compare two hands
fn compare_hands(hand1: &Hand, hand2: &Hand) -> Ordering {
    match (&hand1.hand_type, &hand2.hand_type) {
        // Different hand types
        (type1, type2) if std::mem::discriminant(type1) != std::mem::discriminant(type2) => {
            // Convert to ordinal value for comparison
            let type_value = |hand_type: &HandType| -> u8 {
                match hand_type {
                    HandType::HighCard(_) => 0,
                    HandType::OnePair(_, _) => 1,
                    HandType::TwoPair(_, _, _) => 2,
                    HandType::ThreeOfAKind(_, _) => 3,
                    HandType::Straight(_) => 4,
                    HandType::Flush(_) => 5,
                    HandType::FullHouse(_, _) => 6,
                    HandType::FourOfAKind(_, _) => 7,
                    HandType::StraightFlush(_) => 8,
                }
            };
            
            type_value(&hand1.hand_type).cmp(&type_value(&hand2.hand_type))
        },
        
        // Same hand types - compare based on specific type
        (HandType::HighCard(ranks1), HandType::HighCard(ranks2)) => {
            compare_rank_lists(ranks1, ranks2)
        },
        
        (HandType::OnePair(pair1, kickers1), HandType::OnePair(pair2, kickers2)) => {
            match pair1.cmp(pair2) {
                Ordering::Equal => compare_rank_lists(kickers1, kickers2),
                ordering => ordering,
            }
        },
        
        (HandType::TwoPair(high1, low1, kicker1), HandType::TwoPair(high2, low2, kicker2)) => {
            match high1.cmp(high2) {
                Ordering::Equal => match low1.cmp(low2) {
                    Ordering::Equal => kicker1.cmp(kicker2),
                    ordering => ordering,
                },
                ordering => ordering,
            }
        },
        
        (HandType::ThreeOfAKind(three1, kickers1), HandType::ThreeOfAKind(three2, kickers2)) => {
            match three1.cmp(three2) {
                Ordering::Equal => compare_rank_lists(kickers1, kickers2),
                ordering => ordering,
            }
        },
        
        (HandType::Straight(high1), HandType::Straight(high2)) => {
            high1.cmp(high2)
        },
        
        (HandType::Flush(ranks1), HandType::Flush(ranks2)) => {
            compare_rank_lists(ranks1, ranks2)
        },
        
        (HandType::FullHouse(three1, pair1), HandType::FullHouse(three2, pair2)) => {
            match three1.cmp(three2) {
                Ordering::Equal => pair1.cmp(pair2),
                ordering => ordering,
            }
        },
        
        (HandType::FourOfAKind(four1, kicker1), HandType::FourOfAKind(four2, kicker2)) => {
            match four1.cmp(four2) {
                Ordering::Equal => kicker1.cmp(kicker2),
                ordering => ordering,
            }
        },
        
        (HandType::StraightFlush(high1), HandType::StraightFlush(high2)) => {
            high1.cmp(high2)
        },
        
        // This should never happen due to the first match arm
        _ => unreachable!(),
    }
}

// Compare two lists of ranks (used for high card and kickers)
fn compare_rank_lists(ranks1: &[Rank], ranks2: &[Rank]) -> Ordering {
    for (r1, r2) in ranks1.iter().zip(ranks2.iter()) {
        match r1.cmp(r2) {
            Ordering::Equal => continue,
            ordering => return ordering,
        }
    }
    Ordering::Equal
}
