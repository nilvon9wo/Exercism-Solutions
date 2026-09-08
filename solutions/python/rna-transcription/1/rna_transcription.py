NUCLEOTIDE_COMPLEMENTS = {
    "G": "C",
    "C": "G",
    "T": "A",
    "A": "U",
}

def to_rna(dna):
    rna = ""
    for nucleotide in dna:
        rna += NUCLEOTIDE_COMPLEMENTS[nucleotide]

    return rna