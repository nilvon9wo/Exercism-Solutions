defmodule Garden do
  @doc """
    Accepts a string representing the arrangement of cups on a windowsill and a
    list with names of students in the class. The student names list does not
    have to be in alphabetical order.

    It decodes that string into the various gardens for each student and returns
    that information in a map.
  """
  @plant_by_code %{
    "R" => :radishes,
    "C" => :clover,
    "G" => :grass,
    "V" => :violets
  }

  @default_students [
    :alice,
    :bob,
    :charlie,
    :david,
    :eve,
    :fred,
    :ginny,
    :harriet,
    :ileana,
    :joseph,
    :kincaid,
    :larry
  ]

  @spec info(String.t(), list) :: map
  def info(info_string, students \\ @default_students),
    do:
      info_string
      |> String.split("\n")
      |> Enum.map(&to_plant_rows/1)
      |> map_plants_to_students(students)

  defp map_plants_to_students(plant_rows, students),
    do:
      students
      |> Enum.map(&extract_plants(&1, students, plant_rows))
      |> Map.new()

  defp to_plant_rows(row_codes),
    do:
      row_codes
      |> String.graphemes()
      |> Enum.map(&Map.fetch!(@plant_by_code, &1))

  defp extract_plants(student, students, plant_rows) do
    student_plants =
      student
      |> position(students)
      |> extract_plants(plant_rows)

    {student, student_plants}
  end

  defp extract_plants(position, [row_1, _row_2] = plant_rows)
       when is_list(row_1),
       do:
         plant_rows
         |> Enum.map(&extract_plants(position, &1))
         |> List.flatten()
         |> to_tuple()

  defp to_tuple([nil, nil, nil, nil]),
    do: {}

  defp to_tuple(student_plants),
    do: List.to_tuple(student_plants)

  defp extract_plants(position, plant_row) do
    {plant_1, _} = List.pop_at(plant_row, position * 2)
    {plant_2, _} = List.pop_at(plant_row, position * 2 + 1)
    [plant_1, plant_2]
  end

  defp position(student_name, students),
    do:
      students
      |> Enum.sort()
      |> Enum.find_index(&(&1 === student_name))
end
