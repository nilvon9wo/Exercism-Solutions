class Car {
	private final int speed;
	Car(int speed) {
		this.speed = speed;
	}

	private int distanceDriven = 0;

	public int distanceDriven() {
		return this.distanceDriven;
	}

	public void drive() {
			this.distanceDriven += this.speed;
	}
}