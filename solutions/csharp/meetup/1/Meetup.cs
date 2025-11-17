using System;

public enum Schedule
{
    First,
    Second,
    Third,
    Fourth,
    Teenth,
    Last
}

public class Meetup
{
    private readonly int Month;
    private readonly int Year;

    public Meetup(int month, int year)
    {
        Month = month;
        Year = year;
    }

    public DateTime Day(DayOfWeek targetWeekday, Schedule schedule) =>
        schedule switch
        {
            Schedule.Teenth => FindDay(targetWeekday, 13, x => x),
            Schedule.Last => FindLastDay(targetWeekday),
            _ => FindDay(targetWeekday, 1, x => FindByIndexedOrdinal(schedule, x))
        };

    private DateTime FindLastDay(DayOfWeek targetWeekday)
    {
        DateTime fourthTargetDay = Day(targetWeekday, Schedule.Fourth);
        DateTime candidateDate = fourthTargetDay.AddDays(7);
        return (Month == candidateDate.Month)
            ? candidateDate
            : fourthTargetDay;
    }

    private int FindByIndexedOrdinal(Schedule schedule, int dayDifference) =>
        7 * (int)schedule + dayDifference;

    private DateTime FindDay(DayOfWeek targetWeekday, int baseDay, Func<int, int> baseOffset)
    {
        DateTime nthOfMonth = new DateTime(Year, Month, baseDay);
        int nthDayNumber = (int)nthOfMonth.DayOfWeek;
        int targetDayNumber = (int)targetWeekday;
        int dayDifference = targetDayNumber - nthDayNumber;

        DateTime candidateDay = nthOfMonth.AddDays(baseOffset(dayDifference));
        return (nthDayNumber <= targetDayNumber)
            ? candidateDay
            : candidateDay.AddDays(7);
    }
}