defmodule Phone do
  @doc """
  Remove formatting from a phone number.

  Returns "0000000000" if phone number is not valid
  (10 digits or "1" followed by 10 digits)

  ## Examples

  iex> Phone.number("212-555-0100")
  "2125550100"

  iex> Phone.number("+1 (212) 555-0100")
  "2125550100"

  iex> Phone.number("+1 (212) 055-0100")
  "0000000000"

  iex> Phone.number("(212) 555-0100")
  "2125550100"

  iex> Phone.number("867.5309")
  "0000000000"
  """
  @error_number "0000000000"

  @spec number(String.t()) :: String.t()
  def number(raw) do
    if has_letters?(raw),
      do: @error_number,
      else:
        raw
        |> String.replace(~r/[^\d]/, "")
        |> drop_leading_0s_and_1s()
        |> replace_invalid_with_all_0s()
  end

  defp has_letters?(phone),
    do: String.match?(phone, ~r/[[:lower:]|[:upper:]]/)

  defp drop_leading_0s_and_1s(nil),
    do: nil

  defp drop_leading_0s_and_1s([head | tail])
       when head === "0" or head === "1",
       do: drop_leading_0s_and_1s(tail)

  defp drop_leading_0s_and_1s(graphemes)
       when is_list(graphemes),
       do: Enum.join(graphemes, "")

  defp drop_leading_0s_and_1s(phone)
       when is_binary(phone),
       do: drop_leading_0s_and_1s(String.graphemes(phone))

  defp replace_invalid_with_all_0s(phone) do
    if bad_length?(phone) or bad_exchange?(phone),
      do: @error_number,
      else: phone
  end

  defp bad_length?(phone),
    do: String.length(phone) !== 10

  defp bad_exchange?(phone),
    do: Enum.member?(["0", "1"], String.at(phone, 3))

  @doc """
  Extract the area code from a phone number

  Returns the first three digits from a phone number,
  ignoring long distance indicator

  ## Examples

  iex> Phone.area_code("212-555-0100")
  "212"

  iex> Phone.area_code("+1 (212) 555-0100")
  "212"

  iex> Phone.area_code("+1 (012) 555-0100")
  "000"

  iex> Phone.area_code("867.5309")
  "000"
  """
  @spec area_code(String.t()) :: String.t()
  def area_code(raw),
    do:
      raw
      |> number()
      |> String.slice(0..2)

  @doc """
  Pretty print a phone number

  Wraps the area code in parentheses and separates
  exchange and subscriber number with a dash.

  ## Examples

  iex> Phone.pretty("212-555-0100")
  "(212) 555-0100"

  iex> Phone.pretty("212-155-0100")
  "(000) 000-0000"

  iex> Phone.pretty("+1 (303) 555-1212")
  "(303) 555-1212"

  iex> Phone.pretty("867.5309")
  "(000) 000-0000"
  """
  @spec pretty(String.t()) :: String.t()
  def pretty(raw) do
    number = number(raw)
    {area_code, local} = String.split_at(number, 3)
    {exchange_code, subscriber_number} = String.split_at(local, 3)
    "(#{area_code}) #{exchange_code}-#{subscriber_number}"
  end
end
