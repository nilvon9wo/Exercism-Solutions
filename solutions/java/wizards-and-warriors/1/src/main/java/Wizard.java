public class Wizard extends Fighter  {
	private boolean hasPreparedSpell = false;

	@SuppressWarnings("unused")
	public void prepareSpell() {
		this.hasPreparedSpell = true;
	}

	@Override
	boolean isVulnerable() {
		if (this.hasPreparedSpell) {
			this.hasPreparedSpell = false;
			return false;
		}

		return true;
	}

	@Override
	int getDamagePoints(Fighter fighter) {
		int damagePoints = (this.hasPreparedSpell)
				        ? 12
				        : 3;

		this.hasPreparedSpell = false;
		return damagePoints;
	}
}
