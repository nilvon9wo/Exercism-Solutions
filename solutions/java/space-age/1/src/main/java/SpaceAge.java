class SpaceAge {

	private final double seconds;

	SpaceAge(double seconds) {
		this.seconds = seconds;
	}

	double onEarth() {
		return this.on(Planet.EARTH);
	}

	double onMercury() {
		return this.on(Planet.MERCURY);
	}

	double onVenus() {
		return this.on(Planet.VENUS);
	}

	double onMars() {
		return this.on(Planet.MARS);
	}

	double onJupiter() {
		return this.on(Planet.JUPITER);
	}

	double onSaturn() {
		return this.on(Planet.SATURN);
	}

	double onUranus() {
		return this.on(Planet.URANUS);
	}

	double onNeptune() {
		return this.on(Planet.NEPTUNE);
	}

	private double on(Planet planet) {
		return planet.toYears(seconds);
	}
}
