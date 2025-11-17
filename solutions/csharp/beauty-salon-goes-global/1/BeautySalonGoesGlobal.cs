using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;

public enum Location
{
	[TimeZone("Eastern Standard Time")]
	[Culture("en-US")]
	NewYork,

	[TimeZone("GMT Standard Time")]
	[Culture("en-GB")]
	London,

	[TimeZone("W. Europe Standard Time")]
	[Culture("fr-FR")]
	Paris
}

internal class CultureAttribute : ValueAttribute
{
	internal CultureAttribute(string culture) : base(culture)
	{
	}
}

internal class TimeZoneAttribute : ValueAttribute
{
	internal TimeZoneAttribute(string id) : base(id)
	{
	}
}

internal abstract class ValueAttribute : Attribute
{
	public ValueAttribute(string value) =>
	Value = value;

	public string Value { get; }
}

public static class LocationExtensions
{
	public static TimeZoneInfo ToTimeZoneInfo(this Location location) =>
		TimeZoneInfo.FindSystemTimeZoneById(location.ToTimeZoneId());

	private static string ToTimeZoneId(this Location location) =>
		ToValue<TimeZoneAttribute>(location);

	public static CultureInfo ToFormatProvider(this Location location) =>
		new(location.ToCulture());

	private static string ToCulture(this Location location) =>
		ToValue<CultureAttribute>(location);

	private static string ToValue<T>(this Location location)
		where T : ValueAttribute
	{
		T[] attributes = GetAttributes<T>(location);
		return attributes.Length > 0
			? attributes[0].Value.ToString()
			: "";
	}

	private static T[] GetAttributes<T>(this Location location)
		where T : class
	{
		T[] attributes = (T[])location
		   .GetType()
		   .GetField(location.ToString())
		   .GetCustomAttributes(typeof(T), false);

		return attributes;
	}
}

public enum AlertLevel
{
	Early,
	Standard,
	Late
}

public static class AlertLevelExtensions
{
	private static readonly Dictionary<AlertLevel, TimeSpan> offsetByAlertLevel = new()
	{
		{  AlertLevel.Early, TimeSpan.FromDays(1) },
		{  AlertLevel.Standard, TimeSpan.FromHours(1) + TimeSpan.FromMinutes(45) },
		{  AlertLevel.Late, TimeSpan.FromMinutes(30) },
	};

	public static TimeSpan ToAlertOffset(this AlertLevel alertLevel) =>
		offsetByAlertLevel.TryGetValue(alertLevel, out TimeSpan alertOffset)
			? alertOffset
			: throw new ArgumentException(nameof(alertLevel));
}

public static class Appointment
{
	public static DateTime ShowLocalTime(DateTime dateTimeUtc) =>
		dateTimeUtc + TimeZoneInfo.Local.GetUtcOffset(dateTimeUtc);

	public static DateTime Schedule(string appointmentDateDescription, Location location)
	{
		DateTime dateTime = DateTime.Parse(appointmentDateDescription);
		TimeZoneInfo timeZoneInfo = location.ToTimeZoneInfo();

		TimeSpan offset = timeZoneInfo.BaseUtcOffset;
		if (timeZoneInfo.IsDaylightSavingTime(dateTime))
		{
			offset += TimeSpan.FromHours(1);
		}

		return dateTime - offset;
	}

	public static DateTime GetAlertTime(DateTime appointment, AlertLevel alertLevel) =>
		appointment - alertLevel.ToAlertOffset();

	public static bool HasDaylightSavingChanged(DateTime datetime, Location location)
	{
		TimeZoneInfo timeZoneInfo = location.ToTimeZoneInfo();
		return timeZoneInfo.SupportsDaylightSavingTime
				&& timeZoneInfo.GetAdjustmentRules()
					.Any(x => IsLessThanOneWeekPast(x.DateStart, datetime));
	}

	private static bool IsLessThanOneWeekPast(DateTime changeDate, DateTime referenceDate)
	{
		DateTime changeDayThisYear = changeDate.AddYears(referenceDate.Year - changeDate.Year);
		if (changeDayThisYear < referenceDate)
		{
			changeDayThisYear = changeDayThisYear.AddYears(1);
		}

		return (changeDayThisYear - referenceDate).TotalDays > 7;
	}

	public static DateTime NormalizeDateTime(string datetimeString, Location location)
	{
		try
		{
			return DateTime.Parse(datetimeString, location.ToFormatProvider());
		}
		catch (FormatException)
		{
			return DateTime.MinValue;
		}
	}
}
