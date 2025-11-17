public class Lasagna {
	private final static int expectedCookingTime = 40;
	private final static int preparationMinutesPerLayer = 2;
    public int expectedMinutesInOven() {
		return expectedCookingTime;
    }

    public int remainingMinutesInOven(int elapsedMinutes) {
		return expectedMinutesInOven() - elapsedMinutes;
    }

	public int preparationTimeInMinutes(int layerCount) {
		return layerCount * preparationMinutesPerLayer;
	}

	public int totalTimeInMinutes(int layerCount, int elapsedMinutes) {
		return elapsedMinutes + preparationTimeInMinutes(layerCount);
	}
}
