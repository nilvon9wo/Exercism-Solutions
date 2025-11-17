defmodule Separators do
  def cell_separator,
    do: " "

  def row_separator,
    do: "\n"
end

defmodule Point do
  @enforce_keys [:row_index, :column_index]
  defstruct [:row_index, :column_index]

  defp new(row_index, column_index),
    do: %Point{
      row_index: row_index,
      column_index: column_index
    }

  def to_points_from_string(string),
    do:
      string
      |> String.split(Separators.row_separator())
      |> Enum.map(&String.split(&1, Separators.cell_separator()))
      |> convert_to_points()

  defp convert_to_points(values_in_rows),
    do:
      values_in_rows
      |> Enum.with_index()
      |> Enum.map(&convert_row_to_points/1)
      |> List.flatten()
      |> Map.new()

  defp convert_row_to_points({row, row_number}),
    do:
      row
      |> Enum.with_index()
      |> Enum.reduce(_accumulator = [], &add_point_value_pair(row_number, &1, &2))

  defp add_point_value_pair(row_number, {value, column_number}, accumulator),
    do: [{new(row_number, column_number), value} | accumulator]
end

defmodule Matrix do
  @enforce_keys [:points]
  defstruct [:points]

  alias __MODULE__

  @doc """
  Convert an `input` string, with rows separated by newlines and values
  separated by single spaces, into a `Matrix` struct.
  """
  @spec from_string(input :: String.t()) :: %Matrix{}
  def from_string(input),
    do:
      input
      |> Point.to_points_from_string()
      |> from_point_map()

  defp from_point_map(%{} = points),
    do: %Matrix{points: points}

  @doc """
  Write the `matrix` out as a string, with rows separated by newlines and
  values separated by single spaces.
  """
  @spec to_string(matrix :: %Matrix{}) :: String.t()
  def to_string(matrix),
    do:
      matrix
      |> rows()
      |> Enum.map(&Enum.join(&1, Separators.cell_separator()))
      |> Enum.join(Separators.row_separator())

  @doc """
  Given a `matrix`, return its rows as a list of lists of integers.
  """
  @spec rows(matrix :: %Matrix{}) :: list(list(integer))
  def rows(matrix),
    do: to_vectors(matrix, &to_row_list/1)

  @doc """
  Given a `matrix` and `index`, return the row at `index`.
  """
  @spec row(matrix :: %Matrix{}, index :: integer) :: list(integer)
  def row(matrix, index),
    do: to_vector(matrix, index, &filter_by_row/2)

  @doc """
  Given a `matrix`, return its columns as a list of lists of integers.
  """
  @spec columns(matrix :: %Matrix{}) :: list(list(integer))
  def columns(matrix),
    do: to_vectors(matrix, &to_column_list/1)

  @doc """
  Given a `matrix` and `index`, return the column at `index`.
  """
  @spec column(matrix :: %Matrix{}, index :: integer) :: list(integer)
  def column(matrix, index),
    do: to_vector(matrix, index, &filter_by_column/2)

  defp sort(
         {%Point{column_index: column_1, row_index: row_1}, _},
         {%Point{column_index: column_2, row_index: row_2}, _}
       ),
       do: row_1 <= row_2 or column_1 <= column_2

  defp filter_by_row({%Point{row_index: row_index}, _}, target),
    do: row_index === target

  defp filter_by_column({%Point{column_index: column_index}, _}, target),
    do: column_index === target

  defp to_vector(matrix, index, filter_function),
    do:
      matrix.points
      |> Enum.filter(&filter_function.(&1, index))
      |> Enum.sort(&sort/2)
      |> to_row_list()
      |> List.flatten()

  defp to_vectors(matrix, list_function),
    do:
      matrix.points
      |> Enum.sort(&sort/2)
      |> list_function.()

  defp to_row_list(matrix),
    do:
      matrix
      |> Enum.group_by(&to_row_index/1, &to_value/1)
      |> to_values()

  defp to_column_list(matrix),
    do:
      matrix
      |> Enum.group_by(&to_column_index/1, &to_value/1)
      |> to_values()

  defp to_row_index({%Point{row_index: row_index}, _}),
    do: row_index

  defp to_column_index({%Point{column_index: column_index,}, _}),
    do: column_index

  defp to_value({%Point{}, value}),
    do: value

  defp to_values(matrix),
    do:
      matrix
      |> Map.values()
      |> Enum.map(&to_integers/1)

  defp to_integers(row),
    do: Enum.map(row, &String.to_integer/1)
end
