defmodule Acronym do
  @doc """
  Generate an acronym from a string.
  "This is a string" => "TIAS"
  """
  @spec abbreviate(String.t()) :: String.t()
  def abbreviate(string),
    do:
      string
      |> String.replace(~r/[-|_]/, " ")
      |> String.split(" ")
      |> Enum.map(&separate_inconsistent_casing/1)
      |> List.flatten()
      |> Enum.reject(&(&1 === " "))
      |> Enum.map(&String.trim(&1))
      |> Enum.map(&String.first(&1))
      |> Enum.join("")
      |> String.upcase()

  def separate_inconsistent_casing(string) do
    if String.trim(string) == String.trim(String.upcase(string)),
      do: string,
      else:
        string
        |> insert_spaces_before_capitals()
        |> String.split(" ")
  end

  def insert_spaces_before_capitals(string),
    do:
      string
      |> String.graphemes()
      |> Enum.map(&insert_spaces_before_capital/1)
      |> Enum.join("")

  def insert_spaces_before_capital(character)
      when character >= "A" and
             character <= "Z",
      do: " " <> character

  def insert_spaces_before_capital(character),
    do: character
end
