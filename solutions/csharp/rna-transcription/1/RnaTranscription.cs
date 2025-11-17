using System;
using System.Collections.Generic;
using System.Linq;

public static class RnaTranscription
{
    private static readonly Dictionary<char, char> RnaNucleotideByDnaNucleotides = new Dictionary<char, char>{ 
            { 'G', 'C' },
            { 'C', 'G' },
            { 'T', 'A' },
            { 'A', 'U' },
        };

    public static string ToRna(string nucleotide)
    {
        char[] letters = nucleotide.ToCharArray();
        IEnumerable<char> transcribed = letters.Select(letter => 
            RnaNucleotideByDnaNucleotides.GetValueOrDefault(letter, '-')
        );
        return string.Join("", transcribed);
    }
}