defmodule Hamming do
  @doc """
  Returns number of differences between two strands of DNA, known as the Hamming Distance.

  ## Examples

  iex> Hamming.hamming_distance('AAGTCATA', 'TAGCGATC')
  {:ok, 4}
  """
  @spec hamming_distance([char], [char]) :: {:ok, non_neg_integer} | {:error, String.t()}
  def hamming_distance(strand1, strand2) do
    if length(strand1) !== length(strand2),
      do: {:error, "Lists must be the same length"},
      else: {:ok, count_differences(to_list(strand1), to_list(strand2))}
  end

  defp to_list(strand),
    do: String.graphemes(to_string(strand))

  defp count_differences(strand1, strand2)
       when is_list(strand1) and is_list(strand2),
       do:
         Enum.zip(strand1, strand2)
         |> Enum.reduce(_accumulator = 0, &count_differences/2)

  defp count_differences({strand1, strand2}, accumulator) do
    if strand1 === strand2,
      do: accumulator,
      else: accumulator + 1
  end
end
