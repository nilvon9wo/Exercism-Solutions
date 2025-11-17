using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;

public class PhoneNumber
{
    private static readonly Regex Letters = new Regex(@"[A-Za-z]+");

    public static string Clean(string phoneNumber)
    {
        if (Letters.Match(phoneNumber).Success)
        {
            throw new ArgumentException("Phone numbers must not accept letters.");
        }

        string cleanNumber = new string(
                phoneNumber.ToCharArray()
                .Where(char.IsNumber)
                .ToArray()
            );

        string normalizedNumber = (!'1'.Equals(cleanNumber[0]))
            ? $"1{cleanNumber}"
            : cleanNumber;

        return CheckDigits(normalizedNumber)
            ? normalizedNumber[1..]
            : throw new ArgumentException("Number is invalid");
    }

    private static bool CheckDigits(string normalizedNumber) => 
        !'0'.Equals(normalizedNumber[1])
            && !'1'.Equals(normalizedNumber[1])
            && !'0'.Equals(normalizedNumber[4])
            && !'1'.Equals(normalizedNumber[4])
            && normalizedNumber.Length == 11
            && !normalizedNumber.StartsWith("11");
}