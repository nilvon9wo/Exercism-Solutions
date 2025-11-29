import java.util.Random;

class CaptainsLog {
    private final Random random;
    CaptainsLog(Random random) {
        this.random = random;
    }

	private static final char[] PLANET_CLASSES = new char[]{
			'D', 'H', 'J', 'K', 'L', 'M', 'N', 'R', 'T', 'Y'
	};
	private static final int PLANET_CLASS_COUNT = PLANET_CLASSES.length;

	private static final String STARSHIP_PREFIX = "NCC-";
	private static final int STARSHIP_NUMBER_FIRST = 1000;
	private static final int STARSHIP_NUMBER_LAST = 9999;
	private static final int STARSHIP_NUMBERS_AVAILABLE = STARSHIP_NUMBER_LAST - STARSHIP_NUMBER_FIRST + 1;

	private static final double STARDATE_NUMBER_FIRST = 41000.0;
	private static final double STARDATE_NUMBER_LAST = 42000.0;
	private static final double STARDATE_NUMBERS_AVAILABLE = STARDATE_NUMBER_LAST - STARDATE_NUMBER_FIRST;

	char randomPlanetClass() {
		int value = this.random.nextInt(PLANET_CLASS_COUNT);
		return PLANET_CLASSES[value];
    }

    String randomShipRegistryNumber() {
		int value = this.random.nextInt(STARSHIP_NUMBERS_AVAILABLE);
	    int starshipNumber = STARSHIP_NUMBER_FIRST + value;
	    return STARSHIP_PREFIX + starshipNumber;
    }

    double randomStardate() {
	    double elapsedTime = this.random.nextDouble() * STARDATE_NUMBERS_AVAILABLE;
	    return STARDATE_NUMBER_FIRST + elapsedTime;
    }
}
