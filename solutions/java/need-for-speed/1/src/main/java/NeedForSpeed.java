class NeedForSpeed {
	private final int speed;
	private final int batteryDrain;
    NeedForSpeed(int speed, int batteryDrain) {
		this.speed = speed;
		this.batteryDrain = batteryDrain;
    }

	private int battery = 100;
	private int distanceDriven = 0;

    public boolean batteryDrained() {
        return this.battery < this.batteryDrain;
    }

    public int distanceDriven() {
        return this.distanceDriven;
    }

    public void drive() {
		if (!this.batteryDrained()) {
			this.distanceDriven += this.speed;
			this.drainBattery();
		}
    }

	private void drainBattery() {
		this.battery -= batteryDrain;
		if (this.battery < 0) {
			this.battery = 0;
		}
	}

    public static NeedForSpeed nitro() {
        return new NeedForSpeed(50, 4);
    }
}

class RaceTrack {
	private final int distance;
    RaceTrack(int distance) {
        this.distance = distance;
    }

    public boolean tryFinishTrack(NeedForSpeed car) {
		while (!car.batteryDrained() && car.distanceDriven() < this.distance) {
			car.drive();
		}

		return this.distance <= car.distanceDriven();
    }
}
