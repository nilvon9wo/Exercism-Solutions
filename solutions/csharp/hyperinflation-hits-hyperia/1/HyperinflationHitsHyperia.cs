using System;

public static class CentralBank
{
	private const string _tooBigError = "*** Too Big ***";
	private const string _overflowError = "*** Much Too Big ***";

	public static string DisplayDenomination(long @base, long multiplier)
	{
		long value = @base * multiplier;
		return (value > 0)
			? value.ToString()
			: _tooBigError;
	}

	public static string DisplayGDP(float @base, float multiplier) =>
		DisplayDenomination((long)@base, (long)multiplier);

	public static string DisplayChiefEconomistSalary(decimal salaryBase, decimal multiplier)
	{
		try
		{
			return DisplayDenomination((long)salaryBase, (long)multiplier);
		}
		catch (OverflowException)
		{
			return _overflowError;
		}
	}
}
