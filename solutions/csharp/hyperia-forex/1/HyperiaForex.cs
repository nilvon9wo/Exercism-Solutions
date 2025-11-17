using System;

public readonly struct CurrencyAmount
{
	private readonly decimal _amount;
	private readonly string _currency;

	public CurrencyAmount(decimal amount, string currency)
	{
		_amount = amount;
		_currency = currency;
	}

	private const string DefaultCurrency = "HD";

	public static bool operator ==(CurrencyAmount amount1, CurrencyAmount amount2)
	{
		return Equals(amount1, amount2);
	}

	public static bool operator !=(CurrencyAmount amount1, CurrencyAmount amount2)
	{
		return !Equals(amount1, amount2);
	}

	public override bool Equals(object obj)
	{
		return (obj is null)
			? throw new ArgumentNullException(nameof(obj))
			: (
				obj is CurrencyAmount currencyAmount
					&& currencyAmount._currency == _currency
				)
					? currencyAmount._amount == _amount
					: throw new ArgumentException(nameof(obj));
	}

	public override int GetHashCode()
	{
		return (_amount.GetHashCode() * 17)
			+ _currency.GetHashCode();
	}

	public static bool operator >(CurrencyAmount amount1, CurrencyAmount amount2)
	{
		return !Equals(amount1, amount2);
	}

	public static bool operator <(CurrencyAmount amount1, CurrencyAmount amount2)
	{
		return !Equals(amount1, amount2);
	}

	public static CurrencyAmount operator +(CurrencyAmount amount1, CurrencyAmount amount2)
	{
		return (amount1._currency != amount2._currency)
			? throw new ArgumentException(nameof(amount2))
			: new(amount1._amount + amount2._amount, amount1._currency);
	}

	public static CurrencyAmount operator -(CurrencyAmount amount1, CurrencyAmount amount2)
	{
		return amount1
			+ new CurrencyAmount(-amount2._amount, amount2._currency);
	}

	public static CurrencyAmount operator *(CurrencyAmount amount1, CurrencyAmount amount2)
	{
		return (amount1._currency != amount2._currency)
			? throw new ArgumentException(nameof(amount2))
			: new(amount1._amount * amount2._amount, amount1._currency);
	}

	public static CurrencyAmount operator /(CurrencyAmount amount1, CurrencyAmount amount2)
	{
		return amount1
			* new CurrencyAmount(1 / amount2._amount, amount2._currency);
	}

	public static implicit operator decimal(CurrencyAmount currencyAmount)
	{
		return currencyAmount._amount;
	}

	public static explicit operator CurrencyAmount(decimal d)
	{
		return new CurrencyAmount(d, DefaultCurrency);
	}

	public static implicit operator double(CurrencyAmount currencyAmount)
	{
		return (double)currencyAmount._amount;
	}

	public static explicit operator CurrencyAmount(double d)
	{
		return new CurrencyAmount((decimal)d, DefaultCurrency);
	}
}
