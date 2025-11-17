use std::cmp::Ordering;

use crate::hands::poker_hand::PokerHand;
use crate::models::poker_card::PokerCard;

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
