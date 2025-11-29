import java.util.Map;

@SuppressWarnings("unused")
public class GameMaster {
private static final Map<TravelMethod, String> PREPOSITION_BY_TRAVEL_METHODS = Map.of(
			TravelMethod.WALKING, "by",
			TravelMethod.HORSEBACK, "on"
	);

	public String describe(TravelMethod travelMethod) {
		String preposition = PREPOSITION_BY_TRAVEL_METHODS.getOrDefault(travelMethod, "by");
		String displayName = travelMethod.name()
				                     .toLowerCase();
		return "You're traveling to your destination " + preposition + " " + displayName + ".";
	}

	public String describe(Character character) {
		return "You're a level "
				       + character.getLevel() + " "
				       + character.getCharacterClass()
				       + " with " + character.getHitPoints() + " hit points.";
	}

	public String describe(Destination destination) {
		return "You've arrived at " + destination.getName()
				       + ", which has " + destination.getInhabitants() + " inhabitants.";
	}

	@SuppressWarnings("unused")
	public String describe(Character character, Destination destination) {
		return this.describe(character, destination, TravelMethod.WALKING);
	}

	public String describe(Character character, Destination destination, TravelMethod travelMethod) {
		return this.describe(character)
				       + " " + this.describe(travelMethod)
				       + " " + this.describe(destination);
	}
}
