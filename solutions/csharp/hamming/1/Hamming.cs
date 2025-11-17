using System;

public static class Hamming
{
	public static int Distance(string firstStrand, string secondStrand)
	{
		if (firstStrand.Length != secondStrand.Length)
		{
			throw new ArgumentException("Strings must be equal length.", nameof(secondStrand));
		}
		else
		{
			int differenceCount = 0;
			for (int i = 0; i < firstStrand.Length; i++)
			{
				if (firstStrand[i] != secondStrand[i])
				{
					differenceCount++;
				}
			}

			return differenceCount;
		}
	}
}