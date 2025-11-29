class Fighter {
    boolean isVulnerable() {
        return true;
    }

    @SuppressWarnings("unused")
    int getDamagePoints(Fighter fighter) {
        return 1;
    }

	@Override
	public String toString() {
		return "Fighter is a " + getClass().getSimpleName();
	}
}
