using System;

public static class ReverseString
{
    public static string Reverse(string input)
    {
        if ("".Equals(input))
        {
            return input;
        }

        string reversed = "";
        char[] characters = input.ToCharArray();
        for (int i = 1; i <= characters.Length; i++)
        {
            reversed += characters[^i];
        }

        return reversed;        
    }
}