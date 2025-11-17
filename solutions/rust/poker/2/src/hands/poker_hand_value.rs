#[derive(Clone, Copy, PartialEq, Eq, PartialOrd, Ord)]
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
