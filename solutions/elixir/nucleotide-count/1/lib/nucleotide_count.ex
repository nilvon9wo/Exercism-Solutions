defmodule NucleotideCount do
  @nucleotides [?A, ?C, ?G, ?T]

  @doc """
  Counts individual nucleotides in a DNA strand.

  ## Examples

  iex> NucleotideCount.count('AATAA', ?A)
  4

  iex> NucleotideCount.count('AATAA', ?T)
  1
  """
  @spec count(charlist(), char()) :: non_neg_integer()
  def count(strand, nucleotide),
    do:
      Enum.reduce(
        strand,
        _initial_accumulator = 0,
        &count(%{
          target_nucleotide: nucleotide,
          current_nucleotide: &1,
          accumulator: &2
        })
      )

  defp count(%{
         target_nucleotide: target_nucleotide,
         current_nucleotide: current_nucleotide,
         accumulator: accumulator
       }),
       do:
         if(current_nucleotide === target_nucleotide,
           do: 1 + accumulator,
           else: accumulator
         )

  @doc """
  Returns a summary of counts by nucleotide.

  ## Examples

  iex> NucleotideCount.histogram('AATAA')
  %{?A => 4, ?T => 1, ?C => 0, ?G => 0}
  """
  @spec histogram(charlist()) :: map()
  def histogram(strand),
    do:
      @nucleotides
      |> Enum.map(&{&1, count(strand, &1)})
      |> Map.new()
end
