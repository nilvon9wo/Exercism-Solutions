defmodule CryptoSquare do
  @doc """
  Encode string square methods
  ## Examples

    iex> CryptoSquare.encode("abcd")
    "ac bd"
  """
  @spec encode(String.t()) :: String.t()
  def encode("" = _plain_text),
    do: ""

  def encode(plain_text),
    do:
      plain_text
      |> normalize()
      |> to_rectangle()
      |> Enum.reverse()
      |> Enum.map(&String.graphemes/1)
      |> Enum.zip()
      |> Enum.map(&Tuple.to_list/1)
      |> Enum.map(&Enum.join/1)
      |> Enum.map(&String.trim/1)
      |> Enum.join(" ")

  defp normalize(plain_text),
    do:
      plain_text
      |> String.downcase()
      |> String.replace(~r/[^a-z0-9]/, "")

  defp to_rectangle(normalized_text),
    do:
      normalized_text
      |> calculate_dimensions()
      |> make_rows(normalized_text)

  defp calculate_dimensions(normalized_text) do
    text_length = String.length(normalized_text)
    rows = calculate_columns(text_length)

    if :math.pow(rows, 2) >= text_length,
      do: rows,
      else: rows + 1
  end

  defp make_rows(columns, text, accumulator \\ []) do
    if String.length(text) < columns,
      do: [String.pad_trailing(text, columns) | accumulator],
      else:
        make_rows(
          columns,
          String.slice(text, columns, String.length(text) - columns),
          [String.slice(text, 0, columns) | accumulator]
        )
  end

  defp calculate_columns(text_length) do
    square_root = :math.sqrt(text_length)
    truncated_root = trunc(square_root)

    if square_root == truncated_root,
      do: truncated_root,
      else: truncated_root + 1
  end
end
