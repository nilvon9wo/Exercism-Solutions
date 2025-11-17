use std::collections::HashMap;

use crate::models::poker_card::PokerCard;

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