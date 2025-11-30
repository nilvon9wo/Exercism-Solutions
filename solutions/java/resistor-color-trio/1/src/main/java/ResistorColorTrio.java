class ResistorColorTrio {
	String label(String[] colors) {
		int baseValue = value(colors);

		ResistorColor displayMultiplierColor = ResistorColor.from(colors[2]);
		int displayValue = (int)(baseValue * displayMultiplierColor.getDisplayMultiplier());
		String displayPrefix = displayMultiplierColor.getDisplayPrefix();

		return displayValue + " " + displayPrefix + "ohms";
	}

	int value(String[] colors) {
		int firstDigit = this.getValue(colors, 0);
		int secondDigit = this.getValue(colors, 1);
		return Integer.parseInt("" + firstDigit + secondDigit);
	}

	private int getValue(String[] colors, int index) {
		return ResistorColor.from(colors[index])
				       .getValue();
	}
}
