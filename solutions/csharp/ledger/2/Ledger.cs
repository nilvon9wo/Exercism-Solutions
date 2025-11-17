using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;

// ReSharper disable once CheckNamespace

public static class Ledger
{
	public static LedgerEntry CreateEntry(string date, string description, int change)
		=> LedgerEntry.From(date, description, change);

	public static string Format(string currency, string locale, LedgerEntry[] entries)
		=> LedgerFormatter.Format(currency, locale, entries);
}

//=======================================================================

// ReSharper disable once CheckNamespace
public record LedgerEntry(DateTime Date, string Description, decimal Change)
{
	public static LedgerEntry From(string date, string description, int change)
		=> new(
			DateTime.Parse(date, CultureInfo.InvariantCulture),
			description,
			change / 100.0m
		);

	public string ToString(IFormatProvider culture)
	{
		string date = Date.ToString("d", culture);
		string description = Description.Length <= 25
			? Description
			: $"{Description[..22]}...";
		string padding = Change >= 0.0m
			? " "
			: "";
		string change = $"{Change.ToString("C", culture)}{padding}";
		return $"{date} | {description,-25} | {change,13}";
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class LedgerFormatter
{
	public static string Format(string currency, string locale, LedgerEntry[] entries)
	{
		_ = entries ?? throw new ArgumentNullException(nameof(entries));
		string header = CreateHeader(locale);
		if (entries.Length == 0)
		{
			return header;
		}

		CultureInfo culture = LocaleLookup.CreateCulture(locale, currency);
		return entries
			.OrderBy(x => $"{x.Date}@{x.Description}@{x.Change}")
			.Select(entry => entry.ToString(culture))
			.Aggregate(
				new StringBuilder(header),
				(sb, entry) => sb.Append(CultureInfo.InvariantCulture, $"\n{entry}")
			)
			.ToString();
	}

	private static string CreateHeader(string locale)
		=> !LocaleLookup.TryGetHeaderTemplate(locale, out LedgerHeaderTemplate? template)
			? throw new ArgumentException("Invalid locale")
			: template!.ToString();
}

//=======================================================================

// ReSharper disable once CheckNamespace

public record CultureInformation
{
	public required CurrencyNegativePattern CurrencyNegativePattern { get; init; }
	public required string ShortDatePattern { get; init; }
}

//=======================================================================

// ReSharper disable once CheckNamespace

public enum CurrencyNegativePattern
{
	CurrencySymbolBeforeNumberWithMinusSign = 0,
	CurrencySymbolBeforeNumberWithParentheses = 1,
	CurrencySymbolAfterNumberWithMinusSign = 2,
	CurrencySymbolAfterNumberWithParentheses = 3,
	MinusSignBeforeCurrencySymbolAndNumber = 4,
	MinusSignAfterCurrencySymbolAndNumber = 5,
	ParenthesesBeforeAndAfterCurrencySymbolAndNumber = 6,
	ParenthesesAfterCurrencySymbolAndNumber = 7,
	CurrencySymbolBeforeNumberNoNegativeSign = 8,
	CurrencySymbolAfterNumberNoNegativeSign = 9,
	MinusSignBeforeCurrencySymbolNoNegativeSignBeforeNumber = 10,
	MinusSignAfterCurrencySymbolNoNegativeSignBeforeNumber = 11,
	ParenthesesBeforeAndAfterCurrencySymbolNoNegativeSignBeforeNumber = 12,
	ParenthesesAfterCurrencySymbolNoNegativeSignBeforeNumber = 13,
	NoCurrencySymbolMinusSignBeforeNumber = 14,
	NoCurrencySymbolMinusSignAfterNumber = 15
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal interface ILocale
{
	public CultureInformation Information { get; }
	public LedgerHeaderTemplate HeaderTemplate { get; }
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal sealed record LedgerHeaderTemplate
{
	public required string Date { get; init; }
	public required string Description { get; init; }
	public required string Change { get; init; }

	public override string ToString()
		=> $"{Date,-10} | {Description,-25} | {Change,-13}";
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class LocaleLookup
{
	private const string _unitedStatesDollar = "USD";
	private const string _europeanEuro = "EUR";

	private static readonly Dictionary<string, ILocale> _localeByIdentifiers
		= new()
		{
			{ UnitedStatesLocale.LocaleIdentifier, new UnitedStatesLocale() },
			{ NetherlandsLocale.LocaleIdentifier, new NetherlandsLocale() }
		};

	private static readonly Dictionary<string, char> _currencySymbolByCurrency = new()
	{
		{ _unitedStatesDollar, '$' }, { _europeanEuro, '€' }
	};

	internal static CultureInfo CreateCulture(string localeId, string currency)
	{
		CultureInformation information =
			_localeByIdentifiers.TryGetValue(localeId, out ILocale? locale)
				? locale.Information
				: throw new ArgumentException("Unsupported Local");

		CultureInfo cultureInfo = new(localeId);
		cultureInfo.NumberFormat.CurrencySymbol = _currencySymbolByCurrency.TryGetValue(currency, out char symbol)
			? symbol.ToString()
			: throw new ArgumentException("Invalid currency");
		cultureInfo.NumberFormat.CurrencyNegativePattern = (int)information.CurrencyNegativePattern;
		cultureInfo.DateTimeFormat.ShortDatePattern = information.ShortDatePattern;
		return cultureInfo;
	}

	internal static bool TryGetHeaderTemplate(string localeId, out LedgerHeaderTemplate? headers)
	{
		if (!_localeByIdentifiers.TryGetValue(localeId, out ILocale? locale))
		{
			throw new ArgumentException("Unsupported Local");
		}

		headers = locale.HeaderTemplate;
		return true;
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal sealed class NetherlandsLocale : ILocale
{
	public const string LocaleIdentifier = "nl-NL";

	public CultureInformation Information
		=> new()
		{
			CurrencyNegativePattern =
				CurrencyNegativePattern.ParenthesesBeforeAndAfterCurrencySymbolNoNegativeSignBeforeNumber,
			ShortDatePattern = "dd/MM/yyyy"
		};

	public LedgerHeaderTemplate HeaderTemplate
		=> new() { Date = "Datum", Description = "Omschrijving", Change = "Verandering" };
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal sealed class UnitedStatesLocale : ILocale
{
	public const string LocaleIdentifier = "en-US";

	public CultureInformation Information
		=> new()
		{
			CurrencyNegativePattern =
				CurrencyNegativePattern.CurrencySymbolBeforeNumberWithMinusSign,
			ShortDatePattern = "MM/dd/yyyy"
		};

	public LedgerHeaderTemplate HeaderTemplate
		=> new() { Date = "Date", Description = "Description", Change = "Change" };
}

//=======================================================================