using System.Text.RegularExpressions;
using System.Reflection;

#pragma warning disable IDE0079
#pragma warning disable IDE0130
#pragma warning disable CA1050
public static class SwiftScheduling
{
    private static readonly SwiftSchedulingLookup Lookup = new();

    public static DateTime DeliveryDate(DateTime meetingStart, string description)
        => Lookup.GetProvider(description)
            .Translate(meetingStart, description);
}

public static class SchedulingConstants
{
    // Business hours
    public const int MorningWorkdayStartHour = 8;
    public const int EndOfBusinessDayHour = 17;
    public const int AfternoonThresholdHour = 13;
    public const int EveningEndOfWeekHour = 20;

    // "NOW" delay
    public const int NowDelayHours = 2;

    // Calendar constants
    public const int DaysPerWeek = 7;
    public const int MonthsPerQuarter = 3;

    // Date calculation offsets
    public const int FirstDayOfMonth = 1;
    public const int NextDay = 1;

    // Day groupings
    public static readonly HashSet<DayOfWeek> EarlyWeekDays = [
        DayOfWeek.Monday,
        DayOfWeek.Tuesday,
        DayOfWeek.Wednesday
    ];
    public static readonly HashSet<DayOfWeek> WeekendDays = [
        DayOfWeek.Saturday,
        DayOfWeek.Sunday
    ];

    public static readonly HashSet<DayOfWeek> WorkDays = [
        .. Enum.GetValues<DayOfWeek>()
            .Except(WeekendDays)
    ];
}

public interface IDateTimeProvider<TSelf> : IDateTimeProvider
    where TSelf : class, IDateTimeProvider<TSelf>
{
    public static abstract Regex GetPattern();
}

public interface IDateTimeProvider
{
    public DateTime Translate(DateTime meetingStart, string description);
}

public class SwiftSchedulingLookup
{
    private readonly Dictionary<Func<Regex>, Type> _providerTypesByPatterns = DiscoverProviders();
    private readonly Dictionary<Type, IDateTimeProvider> _cachedProviders = [];

    private static Dictionary<Func<Regex>, Type> DiscoverProviders()
        => Assembly.GetExecutingAssembly()
            .GetTypes()
            .Where(t => t.IsClass && !t.IsAbstract)
            .Where(t => t.GetInterfaces().Any(i =>
                i.IsGenericType &&
                i.GetGenericTypeDefinition() == typeof(IDateTimeProvider<>) &&
                i.GenericTypeArguments[0] == t))
            .ToDictionary(
                CreatePatternDelegate,
                type => type
            );

    private static Func<Regex> CreatePatternDelegate(Type type) 
        => (Func<Regex>)typeof(SwiftSchedulingLookup)
            .GetMethod(nameof(GetPatternGeneric), BindingFlags.Static | BindingFlags.NonPublic)!
            .MakeGenericMethod(type)
            .Invoke(null, null)!;

    private static Func<Regex> GetPatternGeneric<T>()
        where T : class, IDateTimeProvider<T> =>
        T.GetPattern;

    public IDateTimeProvider GetProvider(string description)
    {
        Type providerType = _providerTypesByPatterns
                                .FirstOrDefault(mapping => IsMatch(mapping, description))
                                .Value
                            ?? throw new ArgumentException($"No provider found for description: {description}");

        if (!_cachedProviders.TryGetValue(providerType, out IDateTimeProvider? provider))
        {
            provider = (IDateTimeProvider)Activator.CreateInstance(providerType)!;
            _cachedProviders[providerType] = provider;
        }

        return provider;
    }

    private static bool IsMatch(KeyValuePair<Func<Regex>, Type> mapping, string description)
        => mapping.Key()
            .IsMatch(description);
}

public partial class NowProvider : IDateTimeProvider<NowProvider>
{
    [GeneratedRegex(@"^NOW$", RegexOptions.IgnoreCase)]
    private static partial Regex PatternRegex();

    public static Regex GetPattern() => PatternRegex();

    public DateTime Translate(DateTime meetingStart, string description)
        => meetingStart.AddHours(SchedulingConstants.NowDelayHours);
}

public partial class AsapProvider : IDateTimeProvider<AsapProvider>
{
    [GeneratedRegex(@"^ASAP$", RegexOptions.IgnoreCase)]
    private static partial Regex PatternRegex();

    public static Regex GetPattern() => PatternRegex();

    public DateTime Translate(DateTime meetingStart, string description)
        => meetingStart.Hour < SchedulingConstants.AfternoonThresholdHour
            ? EndOfBusinessDay(meetingStart)
            : NextAfternoon(meetingStart);

    private static DateTime EndOfBusinessDay(DateTime meetingStart)
        => new(
            meetingStart.Year,
            meetingStart.Month,
            meetingStart.Day,
            SchedulingConstants.EndOfBusinessDayHour,
            0,
            0
        );

    private static DateTime NextAfternoon(DateTime meetingStart)
        => meetingStart.AddDays(SchedulingConstants.NextDay)
            .Date.AddHours(SchedulingConstants.AfternoonThresholdHour);
}

public partial class EndOfWeekProvider : IDateTimeProvider<EndOfWeekProvider>
{
    [GeneratedRegex(@"^EOW$", RegexOptions.IgnoreCase)]
    private static partial Regex PatternRegex();

    public static Regex GetPattern() => PatternRegex();

    public DateTime Translate(DateTime meetingStart, string description)
    {
        DayOfWeek dayOfWeek = meetingStart.DayOfWeek;
        return SchedulingConstants.EarlyWeekDays.Contains(dayOfWeek)
            ? CalculateFridayEndOfWeek(meetingStart, dayOfWeek)
            : CalculateSundayEndOfWeek(meetingStart, dayOfWeek);
    }

    private static DateTime CalculateSundayEndOfWeek(DateTime meetingStart, DayOfWeek dayOfWeek)
    {
        int daysUntilSunday = DaysUntilTarget(dayOfWeek, DayOfWeek.Sunday);
        if (daysUntilSunday == 0)
        {
            daysUntilSunday = SchedulingConstants.DaysPerWeek;
        }

        return meetingStart.Date.AddDays(daysUntilSunday)
            .AddHours(SchedulingConstants.EveningEndOfWeekHour);
    }

    private static DateTime CalculateFridayEndOfWeek(DateTime meetingStart, DayOfWeek dayOfWeek)
    {
        int daysUntilFriday = DaysUntilTarget(dayOfWeek, DayOfWeek.Friday);
        return meetingStart.Date.AddDays(daysUntilFriday)
            .AddHours(SchedulingConstants.EndOfBusinessDayHour);
    }

    private static int DaysUntilTarget(DayOfWeek dayOfWeek, DayOfWeek targetDay)
        => ((int)targetDay - (int)dayOfWeek + SchedulingConstants.DaysPerWeek) % SchedulingConstants.DaysPerWeek;
}

public partial class MonthProvider : IDateTimeProvider<MonthProvider>
{
    [GeneratedRegex(@"^(\d+)M$", RegexOptions.IgnoreCase)]
    private static partial Regex PatternRegex();

    [GeneratedRegex(@"(\d+)", RegexOptions.IgnoreCase)]
    private static partial Regex NumberExtractionRegex();

    public static Regex GetPattern() 
        => PatternRegex();

    public DateTime Translate(DateTime meetingStart, string description)
    {
        Match match = NumberExtractionRegex().Match(description);
        int targetMonth = int.Parse(match.Groups[1].Value);

        int targetYear = meetingStart.Year;
        if (meetingStart.Month >= targetMonth)
        {
            targetYear++;
        }

        return GetFirstWorkday(targetYear, targetMonth)
            .AddHours(SchedulingConstants.MorningWorkdayStartHour);
    }

    private static DateTime GetFirstWorkday(int year, int month)
    {
        DateTime startDate = new(year, month, SchedulingConstants.FirstDayOfMonth);
        return Enumerable.Range(0, SchedulingConstants.DaysPerWeek)
            .Select(offset => startDate.AddDays(offset))
            .First(date => SchedulingConstants.WorkDays.Contains(date.DayOfWeek));
    }
}

public partial class QuarterProvider : IDateTimeProvider<QuarterProvider>
{
    [GeneratedRegex(@"^Q(\d+)$", RegexOptions.IgnoreCase)]
    private static partial Regex PatternRegex();

    [GeneratedRegex(@"(\d+)", RegexOptions.IgnoreCase)]
    private static partial Regex NumberExtractionRegex();

    public static Regex GetPattern() => PatternRegex();

    public DateTime Translate(DateTime meetingStart, string description)
    {
        Match match = NumberExtractionRegex().Match(description);
        int targetQuarter = int.Parse(match.Groups[1].Value);

        int currentQuarter = (meetingStart.Month - SchedulingConstants.FirstDayOfMonth) / SchedulingConstants.MonthsPerQuarter
                             + SchedulingConstants.FirstDayOfMonth;

        int targetYear = meetingStart.Year;
        if (currentQuarter > targetQuarter)
        {
            targetYear++;
        }

        return GetLastWorkdayOfQuarter(targetYear, targetQuarter)
            .AddHours(SchedulingConstants.MorningWorkdayStartHour);
    }

    private static DateTime GetLastWorkdayOfQuarter(int year, int quarter)
    {
        int lastMonthOfQuarter = quarter * SchedulingConstants.MonthsPerQuarter;
        int daysInMonth = DateTime.DaysInMonth(year, lastMonthOfQuarter);
        DateTime lastDayOfQuarter = new(year, lastMonthOfQuarter, daysInMonth);

        return Enumerable.Range(0, SchedulingConstants.DaysPerWeek)
            .Select(offset => lastDayOfQuarter.AddDays(-offset))
            .First(date => SchedulingConstants.WorkDays.Contains(date.DayOfWeek));
    }
}