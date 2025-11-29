@SuppressWarnings("unused")
public class GameMaster {
	public String describe(Character character) {
		return "You're a level "
					  + character.getLevel() + " "
					  + character.getCharacterClass()
					  + " with " + character.getHitPoints() + " hit points.";
	}

	public String describe(Destination description) {
		return "You've arrived at " + description.getName()
				       + ", which has " + description.getInhabitants() + " inhabitants.";
	}

	public String describe(TravelMethod travelMethod) {
		return "You're traveling to your destination "
				       + travelMethod.getPrepositionalPhrase() + ".";
	}

	@SuppressWarnings("unused")
	public String describe(Character character, Destination description) {
		return this.describe(character, description, TravelMethod.WALKING);
	}

	public String describe(Character character, Destination description, TravelMethod travelMethod) {
		return this.describe(character)
				       + " " + this.describe(travelMethod)
				       + " " + this.describe(description);
	}
}
