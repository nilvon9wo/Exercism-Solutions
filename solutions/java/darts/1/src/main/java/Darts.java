class Darts {
    int score(double xOfDart, double yOfDart) {
		return LandingArea.from(xOfDart, yOfDart)
				       .getPoints();
    }
}
