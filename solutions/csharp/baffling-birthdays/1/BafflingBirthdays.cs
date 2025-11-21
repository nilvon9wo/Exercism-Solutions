#pragma warning disable IDE0079
#pragma warning disable IDE0130
#pragma warning disable CA1050
public static class BafflingBirthdays
{
    private const int DaysInYear = 365;

    public static DateOnly[] RandomBirthdates(int numberOfBirthdays)
    {
        Random random = new();
        return [.. 
            Enumerable.Range(0, numberOfBirthdays)
                .Select(_ => CreateDate(random))
        ];
    }

    private static DateOnly CreateDate(Random random)
    {
        int year = Enumerable.Range(1900, 200)
            .Where(y => !DateTime.IsLeapYear(y))
            .OrderBy(_ => random.Next())
            .First();
        int month = random.Next(1, 13);
        int day = random.Next(1, DateTime.DaysInMonth(year, month) + 1);
        return new(year, month, day);
    }

    public static bool SharedBirthday(DateOnly[] birthdays)
    {
        int uniqueDates = birthdays
            .Select(birthday => new BirthdateKey(birthday.Month, birthday.Day))
            .Distinct()
            .Count();

        return uniqueDates < birthdays.Length;
    }

    public static double EstimatedProbabilityOfSharedBirthday(int numberOfBirthdays)
    {
        if (numberOfBirthdays <= 1)
        {
            return 0.0;
        }


        double probabilityNoSharedBirthday = Enumerable.Range(0, numberOfBirthdays)
            .Aggregate(1.0, CalculateProbability);

        double probabilitySharedBirthday = 1.0 - probabilityNoSharedBirthday;
        return probabilitySharedBirthday * 100;

    }
    
    private static double CalculateProbability(double currentProbability, int personIndex)
        => currentProbability * (DaysInYear - personIndex) / DaysInYear;
}

public record struct BirthdateKey(int Month, int Day);
