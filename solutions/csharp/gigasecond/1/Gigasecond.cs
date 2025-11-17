using System;

public static class Gigasecond
{
    const double secondsPerGigasecond = 1000000000;

    public static DateTime Add(DateTime moment) =>
        moment.AddSeconds(secondsPerGigasecond);
}