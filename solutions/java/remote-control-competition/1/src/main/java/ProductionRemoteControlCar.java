public class ProductionRemoteControlCar implements RemoteControlCar, Comparable<ProductionRemoteControlCar> {
	private final Car car;

	private ProductionRemoteControlCar (Car car) {
		this.car = car;
	}

	public ProductionRemoteControlCar () {
		this(new Car(10));
	}

    public void drive() {
	    this.car.drive();
	}

    public int getDistanceTravelled() {
        return this.car.distanceDriven();
    }

	private int numberOfVictories = 0;

    public int getNumberOfVictories() {
        return this.numberOfVictories;
    }

    public void setNumberOfVictories(int numberOfVictories) {
        this.numberOfVictories = numberOfVictories;
    }

	@Override
	public int compareTo(ProductionRemoteControlCar other) {
		return -Integer.compare(this.numberOfVictories, other.numberOfVictories);
	}
}
