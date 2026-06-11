using System.Diagnostics.CodeAnalysis;

[SuppressMessage("Design", "CA1050:Declare types in namespaces", Justification = "<Pending>")]
public static class LineUp
{
    public static string Format(string name, int number)
    {
        var suffix = GetOrdinalSuffix(number);
        return $"{name}, you are the {number}{suffix} customer we serve today. Thank you!";
    }

    private static string GetOrdinalSuffix(int number) => (number % 10) switch
    {
        1 when number % 100 != 11 => "st",
        2 when number % 100 != 12 => "nd",
        3 when number % 100 != 13 => "rd",
        _ => "th"
    };
}
