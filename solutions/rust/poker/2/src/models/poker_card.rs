use std::collections::HashMap;

use once_cell::sync::Lazy;

pub struct PokerCard {
	pub(crate) rank: String,
	pub(crate) suit: String,
}

static VALUE_BY_RANK: Lazy<HashMap<String, i32>> = Lazy::new(|| {
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
});

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
		VALUE_BY_RANK.get(&self.rank).cloned().unwrap_or_else(|| panic!("Invalid card rank: {}", self.rank))
	}

	pub fn from(card_string: &str)
	            -> Self {
		let (rank, suit) = card_string.split_at(card_string.len() - 1);
		Self::new(
			rank.to_string(),
			suit.to_string(),
		)
	}
}
