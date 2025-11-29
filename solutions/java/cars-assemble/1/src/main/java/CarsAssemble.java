public class CarsAssemble {
	private static final int carsProducedPerHour = 221;
	private static final RangeMap<Integer, Double> successPercentageBySpeedRange
			= RangeMap.of(
				new Speed(0).to(0.0),
				new SpeedRange(1, 4).to(1.00),
				new SpeedRange(5, 8).to(0.90),
				new Speed(9).to(0.80),
				new Speed(10).to(0.77)
			);

    public double productionRatePerHour(int speed) {
	    return carsProducedPerHour * speed
			           * successPercentageBySpeedRange.get(speed);
    }

	public int workingItemsPerMinute(int speed) {
		return (int) (this.productionRatePerHour(speed) / 60);
	}
}
