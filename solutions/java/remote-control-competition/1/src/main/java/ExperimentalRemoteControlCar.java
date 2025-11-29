public record ExperimentalRemoteControlCar(Car car) implements RemoteControlCar {

	public ExperimentalRemoteControlCar() {
		this(new Car(20));
	}

	public void drive() {
		this.car.drive();
	}

	public int getDistanceTravelled() {
		return this.car.distanceDriven();
	}
}
