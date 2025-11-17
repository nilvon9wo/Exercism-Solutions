defmodule Digital do
  @digital_0 {
    " _ ",
    "| |",
    "|_|",
    "   "
  }
  @digital_1 {
    "   ",
    "  |",
    "  |",
    "   "
  }
  @digital_2 {
    " _ ",
    " _|",
    "|_ ",
    "   "
  }
  @digital_3 {
    " _ ",
    " _|",
    " _|",
    "   "
  }
  @digital_4 {
    "   ",
    "|_|",
    "  |",
    "   "
  }
  @digital_5 {
    " _ ",
    "|_ ",
    " _|",
    "   "
  }
  @digital_6 {
    " _ ",
    "|_ ",
    "|_|",
    "   "
  }
  @digital_7 {
    " _ ",
    "  |",
    "  |",
    "   "
  }
  @digital_8 {
    " _ ",
    "|_|",
    "|_|",
    "   "
  }
  @digital_9 {
    " _ ",
    "|_|",
    " _|",
    "   "
  }

  @numeral_by_digital %{
    @digital_0 => "0",
    @digital_1 => "1",
    @digital_2 => "2",
    @digital_3 => "3",
    @digital_4 => "4",
    @digital_5 => "5",
    @digital_6 => "6",
    @digital_7 => "7",
    @digital_8 => "8",
    @digital_9 => "9"
  }

  def to_numerals(digits),
    do: convert(digits, &to_period/1, separator: ",")

  defp to_period(digits),
    do: convert(digits, &to_numeral/1, separator: "")

  defp convert(digits, convert_function, separator) do
    numerals = Enum.map(digits, convert_function)

    if all_ok?(numerals),
      do: {:ok, join(numerals, separator)},
      else: Enum.find(numerals, &(elem(&1, 0) === :error))
  end

  defp to_numeral({top, middle, bottom, "   " = blank}) do
    if String.length(top) === 3 and
         String.length(middle) === 3 and
         String.length(bottom) === 3,
       do: {:ok, Map.get(@numeral_by_digital, {top, middle, bottom, blank}, "?")},
       else: {:error}
  end

  defp all_ok?(numerals),
    do: Enum.all?(numerals, &(elem(&1, 0) === :ok))

  defp join(numerals, separator: separator),
    do:
      numerals
      |> Enum.map(&elem(&1, 1))
      |> Enum.join(separator)
end

defmodule OcrNumbers do
  alias Digital

  @doc """
  Given a 3 x 4 grid of pipes, underscores, and spaces, determine which number is represented, or
  whether it is garbled.
  """
  @spec convert([String.t()]) :: String.t()
  def convert(input)
      when rem(length(input), 4) !== 0,
      do: {:error, 'invalid line count'}

  def convert(input) do
    valid_column_count? = Enum.all?(input, &(rem(String.length(&1), 3) === 0))

    if valid_column_count?,
      do: to_numerals(input),
      else: {:error, 'invalid column count'}
  end

  defp to_numerals(input),
    do:
      input
      |> Enum.chunk_every(4)
      |> Enum.map(&separate_digits/1)
      |> Digital.to_numerals()

  defp separate_digits(four_rows),
    do:
      four_rows
      |> Enum.map(&split_row/1)
      |> Enum.zip()

  defp split_row(row),
    do:
      row
      |> String.graphemes()
      |> Enum.chunk_every(3)
      |> Enum.map(&Enum.join(&1, ""))
end
