defmodule RunLengthEncoder do
  @code_structure ~r/^(?<quantity>[\d]*)(?<character>[[:print:]])(?<remainder>[[:print:]]*)/

  @doc """
  Generates a string where consecutive elements are represented as a data value and count.
  "AABBBCCCC" => "2A3B4C"
  For this example, assume all input are strings, that are all uppercase letters.
  It should also be able to reconstruct the data into its original form.
  "2A3B4C" => "AABBBCCCC"
  """
  @spec encode(String.t()) :: String.t()
  def encode(string),
    do:
      string
      |> String.split("")
      |> Enum.reject(&(&1 === ""))
      |> encode(_accumulated = [])
      |> Enum.join("")

  def encode(strings, accumulated)
      when length(strings) === 0,
      do:
        Enum.reverse(accumulated)
        |> Enum.map(&tuple_to_string/1)

  def encode([head | tail], accumulated) do
    {count, tail} = take_head_from_tail(head, tail)
    encoding = {head, count}
    encode(tail, [encoding | accumulated])
  end

  defp tuple_to_string({head, 1 = _count}),
    do: "#{head}"

  defp tuple_to_string({head, count}),
    do: "#{count}#{head}"

  defp take_head_from_tail(head, tail),
    do: take_head_from_tail(head, tail, _count = 1)

  defp take_head_from_tail(_head, nil = _tail, count),
    do: {count, nil}

  defp take_head_from_tail(head_1, [head_2 | tail], count)
       when head_1 === head_2,
       do: take_head_from_tail(head_1, tail, count + 1)

  defp take_head_from_tail(_head, tail, count),
    do: {count, tail}

  @spec decode(String.t()) :: String.t()
  def decode(string),
    do: decode(string, _accumulated = [])

  def decode("" = _string, accumulated),
    do:
      Enum.reverse(accumulated)
      |> Enum.join("")

  def decode(string, accumulated) do
    captured = Regex.named_captures(@code_structure, string)
    decoded = explode(captured["quantity"], captured["character"])
    decode(captured["remainder"], [decoded | accumulated])
  end

  defp explode("" = _quantity, character),
    do: character

  defp explode(quantity, character),
    do:
      1..String.to_integer(quantity)
      |> Enum.map(&produce(&1, character))
      |> Enum.join("")

  defp produce(_count, character),
    do: character
end
