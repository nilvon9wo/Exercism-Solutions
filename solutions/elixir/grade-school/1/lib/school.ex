defmodule School do
  @moduledoc """
  Simulate students in a school.

  Each student is in a grade.
  """

  @doc """
  Add a student to a particular grade in school.
  """
  @spec add(map, String.t(), integer) :: map
  def add(database, name, grade),
    do: Map.update(database, grade, [name], &[name | &1])

  @doc """
  Return the names of the students in a particular grade.
  """
  @spec grade(map, integer) :: [String.t()]
  def grade(database, grade),
    do: Map.get(database, grade, [])

  @doc """
  Sorts the school by grade and name.
  """
  @spec sort(map) :: [{integer, [String.t()]}]
  def sort(database),
    do:
      database
      |> Enum.sort()
      |> Enum.map(&sort_names_for_grade/1)

  defp sort_names_for_grade({grade, names}),
    do: {grade, Enum.sort(names)}
end
