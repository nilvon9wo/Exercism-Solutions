defmodule Hamming do
  @doc """
  Returns number of differences between two strands of DNA, known as the Hamming Distance.

  ## Examples

  iex> Hamming.hamming_distance('AAGTCATA', 'TAGCGATC')
  {:ok, 4}
  """
  @spec hamming_distance([char], [char]) :: {:ok, non_neg_integer} | {:error, String.t()}
  def hamming_distance(strand1, strand2) do
    if (length(strand1) != length(strand2)),
      do: {:error, "Lists must be the same length"},
      else: {:ok, calculate(strand1, strand2)}
  end

  defp calculate([head | tail1], [head | tail2]),
       do: 0 + calculate(tail1, tail2)

  defp calculate([_ | tail1], [_ | tail2]),
       do: 1 + calculate(tail1, tail2)

  defp calculate([], []),
       do: 0
end
