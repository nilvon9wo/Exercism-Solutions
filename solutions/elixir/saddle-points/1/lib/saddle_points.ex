defmodule Point do
  defstruct [:row_index, :column_index, :value]
end

defmodule SaddlePoints do
  alias Point

  @doc """
  Parses a string representation of a matrix
  to a list of rows
  """
  @spec rows(String.t()) :: [[integer]]
  def rows(string),
    do: convert_to_matrix(string)

  defp convert_to_matrix(string),
    do:
      string
      |> String.split("\n")
      |> Enum.map(&String.split(&1))
      |> Enum.map(&convert_to_numbers/1)

  defp convert_to_numbers(number_string_list),
    do:
      number_string_list
      |> Enum.map(&Integer.parse(&1))
      |> Enum.map(&elem(&1, 0))

  @doc """
  Parses a string representation of a matrix
  to a list of columns
  """
  @spec columns(String.t()) :: [[integer]]
  def columns(string),
    do:
      string
      |> convert_to_matrix()
      |> Enum.zip()
      |> Enum.map(&Tuple.to_list(&1))

  @doc """
  Calculates all the saddle points from a string
  representation of a matrix
  """
  @spec saddle_points(String.t()) :: [{integer, integer}]
  def saddle_points(string) do
    points =
      string
      |> convert_to_matrix()
      |> create_points()

    points
    |> find_points_with_greatest_or_equal_value_in_own_row()
    |> filter_for_min_or_equal_value_in_column(points)
    |> Enum.sort(&(&1.row_index < &2.row_index))
    |> Enum.map(&convert_point_to_tuples/1)
  end

  defp create_points(matrix),
    do:
      matrix
      |> add_indexes()
      |> convert_to_points(_points_accumulator = [])

  defp add_indexes(matrix),
    do:
      matrix
      |> Enum.map(&add_inner_index/1)
      |> Enum.zip(0..(length(matrix) - 1))

  defp add_inner_index(inner_list),
    do: Enum.zip(inner_list, 0..(length(inner_list) - 1))

  defp convert_to_points(matrix_with_indexes, points_accumulator)
       when length(matrix_with_indexes) == 0,
       do: points_accumulator

  defp convert_to_points([matrix_head | matrix_tail] = _matrix_with_indexes, points_accumulator),
    do: convert_to_points(matrix_tail, convert_row_to_points(matrix_head, points_accumulator))

  defp convert_row_to_points({values_with_column_indexes, _row_index}, points_accumulator)
       when length(values_with_column_indexes) == 0,
       do: points_accumulator

  defp convert_row_to_points(
         {
           [{value, column_index} | remaining_values_with_column_indexes] =
             _values_with_column_indexes,
           row_index
         },
         points_accumulator
       ),
       do:
         convert_row_to_points({remaining_values_with_column_indexes, row_index}, [
           %Point{
             row_index: row_index,
             column_index: column_index,
             value: value
           }
           | points_accumulator
         ])

  defp find_points_with_greatest_or_equal_value_in_own_row(points) do
    max_value_for_each_row = find_max_value_for_each_row(points)
    Enum.filter(points, &filter_for_row_with_max_value(max_value_for_each_row, &1))
  end

  defp filter_for_row_with_max_value(max_value_for_each_row, %Point{
         row_index: row_index,
         value: value
       }),
       do: value >= Map.get(max_value_for_each_row, row_index)

  defp find_max_value_for_each_row(points),
    do:
      points
      |> Enum.group_by(& &1.row_index)
      |> Enum.map(&find_max_value_for/1)
      |> Map.new()

  defp find_max_value_for({row_index, points_on_row}),
    do: {row_index, find_max_value_for(points_on_row)}

  defp find_max_value_for(points_on_row),
    do:
      points_on_row
      |> Enum.map(& &1.value)
      |> Enum.max()

  defp filter_for_min_or_equal_value_in_column(
         points_with_greatest_or_equal_value_in_own_row,
         points
       ) do
    minimum_value_for_each_column = find_min_value_for_each_column(points)

    Enum.filter(
      points_with_greatest_or_equal_value_in_own_row,
      &filter_For_column_with_min_value(minimum_value_for_each_column, &1)
    )
  end

  defp filter_For_column_with_min_value(minimum_value_for_each_column, %Point{
         column_index: column_index,
         value: value
       }),
       do: value <= Map.get(minimum_value_for_each_column, column_index)

  defp find_min_value_for_each_column(points),
    do:
      points
      |> Enum.group_by(& &1.column_index)
      |> Enum.map(&find_min_value_for/1)
      |> Map.new()

  defp find_min_value_for({column_index, points_on_column}),
    do: {column_index, find_min_value_for(points_on_column)}

  defp find_min_value_for(points_on_column),
    do:
      points_on_column
      |> Enum.map(& &1.value)
      |> Enum.min()

  defp convert_point_to_tuples(%Point{row_index: row_index, column_index: column_index}),
    do: {row_index, column_index}
end
