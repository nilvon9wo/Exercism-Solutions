defmodule RnaTranscription do
  @rna_nucleotide_by_dna_nucleotide %{
    ?G => ?C,
    ?C => ?G,
    ?T => ?A,
    ?A => ?U
  }

  @doc """
  Transcribes a character list representing DNA nucleotides to RNA

  ## Examples

  iex> RnaTranscription.to_rna('ACTG')
  'UGAC'
  """
  @spec to_rna([char]) :: [char]
  def to_rna(dna),
    do: Enum.map(dna, &@rna_nucleotide_by_dna_nucleotide[&1])
end
