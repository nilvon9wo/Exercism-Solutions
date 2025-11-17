use std::collections::HashMap;
use std::collections::HashSet;

use lazy_static::lazy_static;

use crate::hands::poker_hand_value::PokerHandValue;
use crate::models::poker_card::PokerCard;
use crate::utilities::length_check::LengthCheck;

pub struct PokerHand {
	pub(crate) cards: Vec<PokerCard>,
}

lazy_static! {
    static ref RANK_GROUP_EVALUATIONS: [(fn(&HashMap<String, Vec<&PokerCard>>) -> bool, PokerHandValue); 5] = [
	((|groups: &HashMap<String, Vec<&PokerCard>>| groups.has_group_with_length(4)) as fn(&HashMap<String, Vec<&PokerCard>>) -> bool, PokerHandValue::FourOfAKind),
	((|groups: &HashMap<String, Vec<&PokerCard>>| groups.has_group_with_length(3) && groups.has_group_with_length(2)) as fn(&HashMap<String, Vec<&PokerCard>>) -> bool, PokerHandValue::FullHouse),
	((|groups: &HashMap<String, Vec<&PokerCard>>| groups.has_group_with_length(3)) as fn(&HashMap<String, Vec<&PokerCard>>) -> bool, PokerHandValue::ThreeOfAKind),
	((|groups: &HashMap<String, Vec<&PokerCard>>| groups.has_two_pair()) as fn(&HashMap<String, Vec<&PokerCard>>) -> bool, PokerHandValue::TwoPair),
	((|groups: &HashMap<String, Vec<&PokerCard>>| groups.has_group_with_length(2)) as fn(&HashMap<String, Vec<&PokerCard>>) -> bool, PokerHandValue::Pair),
];

static ref  HAND_CONDITION_EVALUATIONS: [(fn(&PokerHand) -> bool, PokerHandValue); 5] = [
	((|poker_hand: &PokerHand| poker_hand.is_flush() && poker_hand.is_low_ace_straight()) as fn(&PokerHand) -> bool, PokerHandValue::LowAceStraightFlush),
	((|poker_hand: &PokerHand| poker_hand.is_flush() && poker_hand.is_straight()) as fn(&PokerHand) -> bool, PokerHandValue::StraightFlush),
	((|poker_hand: &PokerHand| poker_hand.is_flush()) as fn(&PokerHand) -> bool, PokerHandValue::Flush),
	((|poker_hand: &PokerHand| poker_hand.is_low_ace_straight()) as fn(&PokerHand) -> bool, PokerHandValue::LowAceStraight),
	((|poker_hand: &PokerHand| poker_hand.is_straight()) as fn(&PokerHand) -> bool, PokerHandValue::Straight),
];
}

impl PokerHand {
	pub fn new(cards: Vec<PokerCard>)
	           -> Self {
		Self {
			cards
		}
	}


	pub fn get_hand_value(&self)
	                      -> PokerHandValue {
		Self::evaluate_hand_value(self)
	}

	fn evaluate_hand_value(&self) -> PokerHandValue {
		let rank_groups = self.create_rank_groups();

		for &(eval_fn, ref value) in RANK_GROUP_EVALUATIONS.iter() {
			if eval_fn(&rank_groups) {
				return *value; // Dereference value to get the actual PokerHandValue
			}
		}

		for &(eval_fn, ref value) in HAND_CONDITION_EVALUATIONS.iter() {
			if eval_fn(self) {
				return *value; // Dereference value to get the actual PokerHandValue
			}
		}

		PokerHandValue::HighCard
	}

	fn create_rank_groups(&self)
	                      -> HashMap<String, Vec<&PokerCard>> {
		let mut rank_groups: HashMap<String, Vec<&PokerCard>> = HashMap::new();
		for card in &self.cards {
			rank_groups.entry(card.rank.clone()).or_insert_with(Vec::new).push(&card);
		}
		rank_groups
	}

	fn is_straight(&self)
	               -> bool {
		let mut sorted_values: Vec<i32> = self.cards.iter().map(|card| card.value()).collect();
		sorted_values.sort_unstable();
		sorted_values.windows(2).all(|w| w[0] + 1 == w[1])
	}

	fn is_low_ace_straight(&self)
	                       -> bool {
		let mut sorted_ranks: Vec<String> = self.cards.iter().map(|card| card.rank.clone()).collect();
		sorted_ranks.sort_unstable();
		sorted_ranks == vec!["2", "3", "4", "5", "A"]
	}

	fn is_flush(&self)
	            -> bool {
		let suit_set: HashSet<String> = self.cards.iter().map(|card| card.suit.clone()).collect();
		suit_set.len() == 1
	}
}
