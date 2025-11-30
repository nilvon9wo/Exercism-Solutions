import java.util.function.IntUnaryOperator;

class ArmstrongNumbers {
	boolean isArmstrongNumber(int numberToCheck) {
		String numStr = String.valueOf(numberToCheck);
		int numberOfDigits = numStr.length();

		int sum = numStr.chars()
				          .map(Character::getNumericValue)
				          .map(this.raiseToPower(numberOfDigits))
				          .sum();

		return sum == numberToCheck;
	}

	private IntUnaryOperator raiseToPower(int numberOfDigits) {
		return digit -> (int) Math.pow(digit, numberOfDigits);
	}
}
