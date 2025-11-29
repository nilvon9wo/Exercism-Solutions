public enum TravelMethod {
	WALKING("by"),
	HORSEBACK("on");

	private final String preposition;

	TravelMethod(String preposition) {
		this.preposition = preposition;
	}

	public String getDisplayName() {
		return this.name()
				       .toLowerCase();
	}

	public String getPrepositionalPhrase() {
		return preposition + " " + this.getDisplayName();
	}
}
