defmodule RomanNumerals do
  @doc """
  Convert the number to a roman number.
  """
  @spec numeral(pos_integer) :: String.t()
  def numeral(number),
    do: numeral(number, roman_number = "")

  def numeral(0, roman_number),
    do: roman_number

  def numeral(number, roman_number)
      when number >= 1000,
      do: numeral(number - 1000, "#{roman_number}M")

  def numeral(number, roman_number)
      when number >= 900,
      do: numeral(number - 900, "#{roman_number}CM")

  def numeral(number, roman_number)
      when number >= 500,
      do: numeral(number - 500, "#{roman_number}D")

  def numeral(number, roman_number)
      when number >= 400,
      do: numeral(number - 400, "#{roman_number}CD")

  def numeral(number, roman_number)
      when number >= 100,
      do: numeral(number - 100, "#{roman_number}C")

  def numeral(number, roman_number)
      when number >= 90,
      do: numeral(number - 90, "#{roman_number}XC")

  def numeral(number, roman_number)
      when number >= 50,
      do: numeral(number - 50, "#{roman_number}L")

  def numeral(number, roman_number)
      when number >= 40,
      do: numeral(number - 40, "#{roman_number}XL")

  def numeral(number, roman_number)
      when number >= 10,
      do: numeral(number - 10, "#{roman_number}X")

  def numeral(number, roman_number)
      when number >= 9,
      do: numeral(number - 9, "#{roman_number}IX")

  def numeral(number, roman_number)
      when number >= 5,
      do: numeral(number - 5, "#{roman_number}V")

  def numeral(number, roman_number)
      when number >= 4,
      do: numeral(number - 4, "#{roman_number}IV")

  def numeral(number, roman_number)
      when number >= 1,
      do: numeral(number - 1, "#{roman_number}I")
end
