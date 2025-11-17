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
        IEnumerable<char> transcribed = nucleotide.ToCharArray()
            .Select(letter => 
                RnaNucleotideByDnaNucleotides.GetValueOrDefault(letter, '-')
            );
        return string.Join("", transcribed);
    }
}