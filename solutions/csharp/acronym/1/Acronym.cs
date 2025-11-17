using System.Collections.Generic;
using System.Linq;

public static class Acronym
{
    public static string Abbreviate(string phrase)
    {
        IEnumerable<char> initials = phrase.Replace("-", " ")
            .Replace("_", " ")
            .Split(" ")
            .Where(word => !string.IsNullOrEmpty(word))
            .Select(word => word[0]);

        return string.Join("", initials)
            .ToUpper();
    }

}