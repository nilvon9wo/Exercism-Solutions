use std::cmp::Ordering;

use hands::hand_info::HandInfo;

use crate::hands::poker_hand_value::PokerHandValue;
use crate::utilities::enumerable_extensions::EnumerableExtensions;

mod hands;
mod models;
mod utilities;

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
