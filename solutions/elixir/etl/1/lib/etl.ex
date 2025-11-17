defmodule ETL do
  @doc """
  Transform an index into an inverted index.

  ## Examples

  iex> ETL.transform(%{"a" => ["ABILITY", "AARDVARK"], "b" => ["BALLAST", "BEAUTY"]})
  %{"ability" => "a", "aardvark" => "a", "ballast" => "b", "beauty" =>"b"}
  """
  @spec transform(map) :: map
  def transform(input),
    do:
      input
      |> invert_index()
      |> List.flatten()
      |> Map.new()

  def invert_index(input) do
    for({score, words} <- input) do
      Enum.map(words, &{String.downcase(&1), score})
    end
  end
end
