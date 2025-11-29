class Leap {
	boolean isLeapYear(int year) {
		return this.isEvenlyDivisible(year, 400)
				       || this.isTypicalLeapYear(year);
	}

	private boolean isTypicalLeapYear(int year) {
		return this.isEvenlyDivisible(year, 4)
				       && !this.isEvenlyDivisible(year, 100);
	}

	boolean isEvenlyDivisible(int year, int divisor) {
		return year % divisor == 0;
	}
}
