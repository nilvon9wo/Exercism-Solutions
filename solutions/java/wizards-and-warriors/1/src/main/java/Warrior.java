public class Warrior extends Fighter {
	@Override
	boolean isVulnerable() {
		return false;
	}

	@Override
	int getDamagePoints(Fighter fighter) {
		return (fighter.isVulnerable())
				? 10
				: 6;
	}
}
