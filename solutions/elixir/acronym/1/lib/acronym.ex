defmodule Acronym do
  @start_of_ascii_uppercase_alphabet 65
  @end_of_ascii_uppercase_alphabet 90
  @start_of_ascii_lowercase_alphabet 97
  @end_of_ascii_lowercase_alphabet 122
  @space 32

  @doc """
  Generate an acronym from a string.
  "This is a string" => "TIAS"
  """
  @spec abbreviate(String.t()) :: String.t()
  def abbreviate(string),
      do:
        string
        |> reformat()
        |> to_charlist()
        |> find_initials()
        |> Enum.join("")

  defp reformat(string),
       do:
         string
         |> String.split(" ")
         |> Enum.map(&reformat_acronym(&1))
         |> Enum.map(&clean(&1))
         |> Enum.reject(&String.trim(&1) === "")
         |> Enum.join(" ")

  defp reformat_acronym(word) do
    if word === String.upcase(word),
       do: String.downcase(word),
       else: word
  end

  defp clean(word),
       do:
         Regex.replace(~r/[^a-zA-Z]/, word, "")
         |> String.trim()

  defp find_initials(list),
       do: find_initials([@space | list], _accumulated = [])

  defp find_initials(list, accumulated)
       when length(list) === 0,
       do: Enum.reverse(accumulated)

  defp find_initials([@space = _head | tail], accumulated) do
    head = take_next_head(tail)
    [_ | tail] = tail
    find_initials(tail, [head | accumulated])
  end

  defp find_initials([head | tail], accumulated)
       when head >= @start_of_ascii_uppercase_alphabet and
            head <= @end_of_ascii_uppercase_alphabet,
       do: find_initials(tail, [to_string([head]) | accumulated])

  defp find_initials([head | tail], accumulated),
       do: find_initials(tail, accumulated)

  defp take_next_head(tail),
       do: tail
           |> to_string()
           |> String.first()
           |> String.upcase()
end
