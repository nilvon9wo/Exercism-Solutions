public class SalaryCalculator {
	private static final double BASE_SALARY = 1000.0;
	private static final double MAX_SALARY = 2000.0;

	private static final int BONUS_THRESHOLD = 20;
	private static final int BONUS_LOW = 10;
	private static final int BONUS_HIGH = 13;

	private static final int SKIP_DAY_THRESHOLD = 5;
	private static final double SALARY_FULL = 1.0;
	private static final double SALARY_REDUCED = 0.85;

	public double salaryMultiplier(int daysSkipped) {
		return daysSkipped >= SKIP_DAY_THRESHOLD
				       ? SALARY_REDUCED
				       : SALARY_FULL;
	}

	public int bonusMultiplier(int productsSold) {
		return productsSold >= BONUS_THRESHOLD
				       ? BONUS_HIGH
				       : BONUS_LOW;
	}

	public int bonusForProductsSold(int productsSold) {
		return productsSold * bonusMultiplier(productsSold);
	}

	public double finalSalary(int daysSkipped, int productsSold) {
		double salary = BASE_SALARY * salaryMultiplier(daysSkipped)
				                + bonusForProductsSold(productsSold);

		return Math.min(salary, MAX_SALARY);
	}
}
