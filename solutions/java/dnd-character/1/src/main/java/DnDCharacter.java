import java.util.List;

class DnDCharacter {
	private static final int HIT_POINT_BASE = 10;

	private final Dice dice;

	private final int strength;
	private final int dexterity;
	private final int constitution;
	private final int intelligence;
	private final int wisdom;
	private final int charisma;
	private final int hitPoints;

	private DnDCharacter(Dice dice) {
		this.dice = dice;

		this.strength = ability(dice.roll());
		this.dexterity = ability(dice.roll());
		this.constitution = ability(dice.roll());
		this.intelligence = ability(dice.roll());
		this.wisdom = ability(dice.roll());
		this.charisma = ability(dice.roll());

		this.hitPoints = HIT_POINT_BASE + modifier(this.constitution);
	}

	public DnDCharacter() {
		this(new Dice());
	}

	private static final int ABILITY_DICE_TO_KEEP = 3;
	private static final int MODIFIER_BASELINE = 10;
	private static final int MODIFIER_DIVISOR = 2;

	int ability(List<Integer> scores) {
		return scores.stream()
				       .sorted((a, b) -> b - a)  // descending
				       .limit(ABILITY_DICE_TO_KEEP)
				       .mapToInt(Integer::intValue)
				       .sum();
	}

	List<Integer> rollDice() {
		return dice.roll();
	}

	int modifier(int score) {
		return Math.floorDiv(score - MODIFIER_BASELINE, MODIFIER_DIVISOR);
	}

	int getStrength()     { return strength; }
	int getDexterity()    { return dexterity; }
	int getConstitution() { return constitution; }
	int getIntelligence() { return intelligence; }
	int getWisdom()       { return wisdom; }
	int getCharisma()     { return charisma; }
	int getHitpoints()    { return hitPoints; }
}
