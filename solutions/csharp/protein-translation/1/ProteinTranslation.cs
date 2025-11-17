using System;
using System.Collections.Generic;
using System.Linq;

public static class ProteinTranslation
{
	private static readonly Dictionary<string, string> polypeptideByCodons = new()
	{
		{ "AUG", "Methionine" },
		{ "UAC", "Tyrosine" },
		{ "UAU", "Tyrosine" },
		{ "UCA", "Serine" },
		{ "UCC", "Serine" },
		{ "UCG", "Serine" },
		{ "UCU", "Serine" },
		{ "UGC", "Cysteine" },
		{ "UGG", "Tryptophan" },
		{ "UGU", "Cysteine" },
		{ "UUA", "Leucine" },
		{ "UUC", "Phenylalanine" },
		{ "UUG", "Leucine" },
		{ "UUU", "Phenylalanine" },
	};

	private static readonly HashSet<string> stoppers = new()
	{
		"UAA", "UAG", "UGA"
	};

	public static string[] Proteins(string strand)
	{
		if (string.IsNullOrEmpty(strand))
		{
			return Array.Empty<string>();
		}

		List<string> polypeptides = new();

		List<string> codons = strand.Split(3)
			.ToList();
		bool wasStopperFound = false;
		while (codons.Any() && !wasStopperFound)
		{
			string codon = codons.Shift();
			wasStopperFound = stoppers.Contains(codon);

			if (!wasStopperFound)
			{
				if (polypeptideByCodons.TryGetValue(codon, out string polypeptide))
				{
					polypeptides.Add(polypeptide);
				}
				else
				{
					throw new ArgumentException("Invalid sequence.", nameof(strand));
				}
			}
		}

		return polypeptides.ToArray();
	}
}

public static class StringExtensions
{
	public static IEnumerable<string> Split(this string str, int chunkSize) =>
		Enumerable.Range(0, str.Length / chunkSize)
			.Select(i => str.Substring(i * chunkSize, chunkSize));
}

public static class EnumerableExtensions
{
	public static T Shift<T>(this ICollection<T> haystack)
	{
		T value = haystack.FirstOrDefault();
		_ = haystack.Remove(value);
		return value;
	}
}