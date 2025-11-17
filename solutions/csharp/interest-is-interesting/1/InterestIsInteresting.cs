using System;

internal static class SavingsAccount
{
	private const float _negativeRate = 3.213f;
	private const float _lowRate = 0.5f;
	private const float _mediumRate = 1.621f;
	private const float _highRate = 2.475f;

	public static float InterestRate(decimal balance) =>
		balance switch
		{
			< 0 => _negativeRate,
			< 1000 => _lowRate,
			< 5000 => _mediumRate,
			_ => _highRate,
		};

	public static decimal Interest(decimal balance) =>
		balance * (decimal)InterestRate(balance) / 100;

	public static decimal AnnualBalanceUpdate(decimal balance) =>
		balance + Interest(balance);

	public static int YearsBeforeDesiredBalance(decimal balance, decimal targetBalance)
	{
		if (balance <= 0)
		{
			throw new ArgumentOutOfRangeException(nameof(balance), "Balance cannot be less than or equal to zero.");
		}

		if (targetBalance < 0)
		{
			throw new ArgumentOutOfRangeException(nameof(targetBalance), "Target Balance cannot be less than to zero.");
		}

		int years = 0;
		while (balance < targetBalance)
		{
			balance = AnnualBalanceUpdate(balance);
			years++;
		}

		return years;
	}
}
