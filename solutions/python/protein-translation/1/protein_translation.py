CODON_LENGTH = 3
STOP = "STOP"

PROTEIN_BY_CODON = {
    "AUG": "Methionine",
    "UUU": "Phenylalanine",
    "UUC": "Phenylalanine",
    "UUA": "Leucine",
    "UUG": "Leucine",
    "UCU": "Serine",
    "UCC": "Serine",
    "UCA": "Serine",
    "UCG": "Serine",
    "UAU": "Tyrosine",
    "UAC": "Tyrosine",
    "UGU": "Cysteine",
    "UGC": "Cysteine",
    "UGG": "Tryptophan",
    "UAA": STOP,
    "UAG": STOP,
    "UGA": STOP,
}

def proteins(rna):
    translated_proteins = []
    for codon in _get_codons(rna):
        protein = PROTEIN_BY_CODON[codon]
        if protein == STOP:
            break

        translated_proteins.append(protein)

    return translated_proteins

def _get_codons(rna):
    for start_index in range(0, len(rna), CODON_LENGTH):
        yield rna[start_index:start_index + CODON_LENGTH]