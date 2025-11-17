using System;
using System.Collections.Generic;

public static class NucleotideCount
{
	public static IDictionary<char, int> Count(string sequence)
	{
		Dictionary<char, int> dictionary = new()
		{
			['A'] = 0,
			['C'] = 0,
			['G'] = 0,
			['T'] = 0
		};

		foreach (char c in sequence)
		{
			if (dictionary.ContainsKey(c))
			{
				dictionary[c]++;
			}
			else
			{
				throw new ArgumentException("Bad sequence", nameof(sequence));
			}
		}

		return dictionary;
	}
}