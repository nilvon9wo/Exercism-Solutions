defmodule ProteinTranslation do
  @stop "STOP"
  @invalid_rna {:error, "invalid RNA"}

  @corresponding_protein_by_codon %{
    "AUG" => "Methionine",
    "UAA" => @stop,
    "UAC" => "Tyrosine",
    "UAG" => @stop,
    "UAU" => "Tyrosine",
    "UCA" => "Serine",
    "UCC" => "Serine",
    "UCG" => "Serine",
    "UCU" => "Serine",
    "UGA" => @stop,
    "UGC" => "Cysteine",
    "UGG" => "Tryptophan",
    "UGU" => "Cysteine",
    "UUA" => "Leucine",
    "UUC" => "Phenylalanine",
    "UUG" => "Leucine",
    "UUU" => "Phenylalanine"
  }

  @is_valid true
  @is_invalid false

  @doc """
  Given an RNA string, return a list of proteins specified by codons, in order.
  """
  @spec of_rna(String.t()) :: {atom, list(String.t())}
  def of_rna(rna),
    do:
      rna
      |> split_words()
      |> Enum.take_while(&(translate(&1) != @stop))
      |> Enum.map(&translate/1)
      |> return_rna()

  defp split_words(rna),
    do:
      rna
      |> to_charlist()
      |> Enum.chunk_every(3)
      |> Enum.map(&to_string/1)

  defp translate({:ok, codon}),
    do: codon

  defp translate({:error, _}),
    do: {:error, "invalid RNA"}

  defp translate(word),
    do: translate(of_codon(word))

  defp return_rna(rna),
    do: return_rna(rna, !Enum.member?(rna, @invalid_rna))

  defp return_rna(rna, @is_valid),
    do: {:ok, rna}

  defp return_rna(rna, @is_invalid),
    do: @invalid_rna

  @doc """
  Given a codon, return the corresponding protein

  UGU -> Cysteine
  UGC -> Cysteine
  UUA -> Leucine
  UUG -> Leucine
  AUG -> Methionine
  UUU -> Phenylalanine
  UUC -> Phenylalanine
  UCU -> Serine
  UCC -> Serine
  UCA -> Serine
  UCG -> Serine
  UGG -> Tryptophan
  UAU -> Tyrosine
  UAC -> Tyrosine
  UAA -> STOP
  UAG -> STOP
  UGA -> STOP
  """
  @spec of_codon(String.t()) :: {atom, String.t()}
  def of_codon(codon),
    do: find_codon(codon, Map.has_key?(@corresponding_protein_by_codon, codon))

  defp find_codon(codon, @is_valid),
    do: {:ok, @corresponding_protein_by_codon[codon]}

  defp find_codon(_codon, @is_invalid),
    do: {:error, "invalid codon"}
end
