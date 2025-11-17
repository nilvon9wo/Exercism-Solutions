use std::cmp::Ordering;
use std::collections::HashMap;
use std::collections::HashSet;

/// Given a list of poker hands, return a list of those hands which win.
///
/// Note the type signature: this function should return _the same_ reference to
/// the winning hand(s) as were passed in, not reconstructed strings which happen to be equal.
pub fn winning_hands<'a>(hands: &[&'a str]) -> Vec<&'a str> {
	let mut best_hands: Vec<&'a str> = Vec::new();
	let mut best_value = PokerHandValue::HighCard;
	let mut best_rank_values: Vec<i32> = Vec::new();

	for &hand_str in hands {
		let mut hand_info = HandInfo::new(hand_str.to_string());
		let hand = hand_info.hand();
		let hand_value = hand.get_hand_value();
		let hand_rank_values: Vec<i32> = hand.cards.iter().map(|card| card.value()).collect();

		match compare_hands(&hand_value, &hand_rank_values, &best_value, &best_rank_values) {
			Ordering::Greater => {
				best_hands = vec![hand_str];
				best_value = hand_value;
				best_rank_values = hand_rank_values;
			}
			Ordering::Equal => {
				best_hands.push(hand_str);
			}
			_ => {}
		}
	}

	best_hands
}

fn compare_hands(hand_value: &PokerHandValue, hand_rank_values: &Vec<i32>, best_value: &PokerHandValue, best_rank_values: &Vec<i32>) -> Ordering {
	match hand_value.cmp(&best_value) {
		Ordering::Equal => {
			if matches!(best_value, PokerHandValue::FullHouse | PokerHandValue::FourOfAKind) {
				let hand_triplet_value = hand_rank_values.find_triplet_key(|&x| x);
				let best_triplet_value = best_rank_values.find_triplet_key(|&x| x);

				if let (Some(hand_triplet_value), Some(best_triplet_value)) = (hand_triplet_value, best_triplet_value) {
					let triplet_comparison = hand_triplet_value.cmp(&best_triplet_value);
					if triplet_comparison != Ordering::Equal {
						return triplet_comparison;
					}
				}
			}

			compare_card_values(hand_rank_values, best_rank_values)
		}
		other => other,
	}
}

fn compare_card_values(hand_rank_values: &Vec<i32>, best_rank_values: &Vec<i32>) -> Ordering {
	for (hand_rank_value, best_rank_value) in hand_rank_values.iter().zip(best_rank_values.iter()) {
		match hand_rank_value.cmp(best_rank_value) {
			Ordering::Equal => continue,
			other => return other,
		}
	}
	hand_rank_values.len().cmp(&best_rank_values.len())
}



//=======================================================================



pub struct HandInfo {
	hand_string: String,
	hand: Option<PokerHand>,
}

impl HandInfo {
	pub fn new(hand_string: String) -> Self {
		Self {
			hand_string,
			hand: None,
		}
	}

	pub fn hand(&mut self) -> &PokerHand {
		if self.hand.is_none() {
			self.hand = Some(self.evaluate_hand());
		}
		self.hand.as_ref().unwrap()
	}

	fn evaluate_hand(&mut self) -> PokerHand {
		let mut ordered_cards: Vec<PokerCard> = self.hand_string.split_whitespace().map(|card_string| PokerCard::from(card_string)).collect();
		ordered_cards.sort_by(|a, b| Self::compare(&a, b));
		PokerHand::new(ordered_cards)
	}

	fn compare(a: &&PokerCard, b: &PokerCard)
	           -> Ordering {
		b.value().cmp(&a.value())
	}
}



//=======================================================================





//=======================================================================



pub struct PokerHand {
	pub(crate) cards: Vec<PokerCard>,
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

	fn evaluate_hand_value(&self)
	                       -> PokerHandValue {
		let rank_groups = self.create_rank_groups();
		for (eval_fn, value) in Self::create_rank_group_evaluations() {
			if eval_fn(&rank_groups) {
				return value;
			}
		}

		for (eval_fn, value) in Self::create_self_evaluations() {
			if eval_fn(self) {
				return value;
			}
		}

		PokerHandValue::HighCard // Default case
	}

	fn create_self_evaluations()
		-> Vec<(&'static dyn Fn(&PokerHand)
			-> bool, PokerHandValue)> {
		vec![
			(&|self_ref| self_ref.is_flush() && self_ref.is_low_ace_straight(), PokerHandValue::LowAceStraightFlush),
			(&|self_ref| self_ref.is_flush() && self_ref.is_straight(), PokerHandValue::StraightFlush),
			(&|self_ref| self_ref.is_flush(), PokerHandValue::Flush),
			(&|self_ref| self_ref.is_low_ace_straight(), PokerHandValue::LowAceStraight),
			(&|self_ref| self_ref.is_straight(), PokerHandValue::Straight),
		]
	}

	fn create_rank_group_evaluations()
		-> Vec<(&'static dyn Fn(&HashMap<String, Vec<&PokerCard>>)
			-> bool, PokerHandValue)> {
		vec![
			(&|groups| groups.has_group_with_length(4), PokerHandValue::FourOfAKind),
			(&|groups| groups.has_group_with_length(3) && groups.has_group_with_length(2), PokerHandValue::FullHouse),
			(&|groups| groups.has_group_with_length(3), PokerHandValue::ThreeOfAKind),
			(&|groups| groups.has_two_pair(), PokerHandValue::TwoPair),
			(&|groups| groups.has_group_with_length(2), PokerHandValue::Pair),
		]
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



//=======================================================================


#[derive(PartialEq, Eq, PartialOrd, Ord)]
pub enum PokerHandValue {
	HighCard,
	Pair,
	TwoPair,
	ThreeOfAKind,
	LowAceStraight,
	Straight,
	Flush,
	FullHouse,
	FourOfAKind,
	LowAceStraightFlush,
	StraightFlush,
}



//=======================================================================





//=======================================================================



pub struct PokerCard {
	pub(crate) rank: String,
	pub(crate) suit: String,
}

impl PokerCard {
	pub fn new(rank: String, suit: String)
	           -> Self {
		Self {
			rank,
			suit,
		}
	}

	pub fn value(&self)
	             -> i32 {
		Self::value_by_rank().get(&self.rank).cloned().unwrap_or_else(|| panic!("Invalid card rank: {}", self.rank))
	}

	pub fn from(card_string: &str)
	            -> Self {
		let (rank, suit) = card_string.split_at(card_string.len() - 1);
		Self::new(
			rank.to_string(),
			suit.to_string(),
		)
	}

	fn value_by_rank()
		-> HashMap<String, i32> {
		HashMap::from([
			("2".to_string(), 2),
			("3".to_string(), 3),
			("4".to_string(), 4),
			("5".to_string(), 5),
			("6".to_string(), 6),
			("7".to_string(), 7),
			("8".to_string(), 8),
			("9".to_string(), 9),
			("10".to_string(), 10),
			("J".to_string(), 11),
			("Q".to_string(), 12),
			("K".to_string(), 13),
			("A".to_string(), 14),
		])
	}
}



//=======================================================================



pub trait EnumerableExtensions<T, TKey: Eq + std::hash::Hash + Clone> {
	fn find_triplet_key<F>(&self, group_key_selector: F)
	                       -> Option<TKey>
		where F: Fn(&T) -> TKey;

	fn find_group_key<F, G>(&self, group_key_selector: F, condition: G)
	                        -> Option<TKey>
		where F: Fn(&T) -> TKey,
		      G: Fn(usize) -> bool;
}

impl<T, TKey: Eq + std::hash::Hash + Clone> EnumerableExtensions<T, TKey> for Vec<T> {
	fn find_triplet_key<F>(&self, group_key_selector: F)
	                       -> Option<TKey>
		where F: Fn(&T)
			-> TKey, {
		self.find_group_key(group_key_selector, |count| count >= 3)
	}

	fn find_group_key<F, G>(&self, group_key_selector: F, condition: G)
	                        -> Option<TKey>
		where F: Fn(&T) -> TKey,
		      G: Fn(usize) -> bool, {
		let mut groups: HashMap<TKey, Vec<&T>> = HashMap::new();
		for item in self {
			groups.entry(group_key_selector(item)).or_insert_with(Vec::new).push(item);
		}

		for (key, group) in groups {
			if condition(group.len()) {
				return Some(key);
			}
		}

		None
	}
}



//=======================================================================



pub trait LengthCheck {
	fn has_group_with_length(&self, length: usize)
	                         -> bool;
	fn has_two_pair(&self)
	                -> bool;
}

impl LengthCheck for HashMap<String, Vec<&PokerCard>> {
	fn has_group_with_length(&self, length: usize)
	                         -> bool {
		self.values().any(|group| group.len() == length)
	}

	fn has_two_pair(&self)
	                -> bool {
		self.values().filter(|group| group.len() == 2).count() == 2
	}
}


//=======================================================================





//=======================================================================


