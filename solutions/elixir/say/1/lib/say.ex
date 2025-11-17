defmodule Say do
  @doc """
  Translate a positive integer into English.
  """
  @zero "zero"
  @small_numbers %{
    1 => "one",
    2 => "two",
    3 => "three",
    4 => "four",
    5 => "five",
    6 => "six",
    7 => "seven",
    8 => "eight",
    9 => "nine",
    10 => "ten",
    11 => "eleven",
    12 => "twelve",
    13 => "thirteen",
    14 => "fourteen",
    15 => "fifteen",
    16 => "sixteen",
    17 => "seventeen",
    18 => "eighteen",
    19 => "nineteen"
  }

  @multiples_of_ten %{
    20 => "twenty",
    30 => "thirty",
    40 => "forty",
    50 => "fifty",
    60 => "sixty",
    70 => "seventy",
    80 => "eighty",
    90 => "ninety"
  }

  @short_scale %{
    3 => "thousand",
    6 => "million",
    9 => "billion"
  }

  @spec in_english(integer) :: {atom, String.t()}
  def in_english(number)
      when number < 0 or
             number > 999_999_999_999,
      do: {:error, "number is out of range"}

  def in_english(0),
    do: {:ok, @zero}

  def in_english(number),
    do: {:ok, in_english!(number)}

  defp in_english!(number)
       when number < 20,
       do: @small_numbers[number]

  defp in_english!(number)
       when number < 100 do
    number_of_tens = div(number, 10)
    multiple_of_ten = number_of_tens * 10

    in_english!(
      _prefix = @multiples_of_ten[multiple_of_ten],
      _remainder = number - multiple_of_ten,
      _separator = "-"
    )
  end

  defp in_english!(number)
       when number < 1000 do
    number_of_hundreds = div(number, 100)
    multiple_of_hundred = number_of_hundreds * 100

    in_english!(
      _prefix = "#{in_english!(number_of_hundreds)} hundred",
      _remainder = number - multiple_of_hundred
    )
  end

  defp in_english!(number) do
    log10 = trunc(:math.log10(number) / 3) * 3
    scale_to_10_power = round(:math.pow(10, log10))
    quotient = div(number, scale_to_10_power)

    in_english!(
      _prefix = "#{in_english!(quotient)} #{@short_scale[log10]}",
      _remainder = number - quotient * scale_to_10_power
    )
  end

  defp in_english!(prefix, remainder, separator \\ " ") do
    if remainder === 0,
      do: prefix,
      else: "#{prefix}#{separator}#{in_english!(remainder)}"
  end
end
