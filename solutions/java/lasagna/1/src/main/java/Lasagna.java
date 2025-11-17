public class Lasagna {
	private final int expectedCookingTime = 40;
	private final int preparationMinutesPerLayer = 2;
    public int expectedMinutesInOven() {
		return expectedCookingTime;
    }

    public int remainingMinutesInOven(int elapsedMinutes) {
		return expectedCookingTime - elapsedMinutes;
    }

	public int preparationTimeInMinutes(int layerCount) {
		return layerCount * preparationMinutesPerLayer;
	}

	public int totalTimeInMinutes(int layerCount, int elapsedMinutes) {
		return elapsedMinutes + preparationTimeInMinutes(layerCount);
	}
}
