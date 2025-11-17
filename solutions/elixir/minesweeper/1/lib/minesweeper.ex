defmodule Point do
  defstruct [:row_index, :column_index, :value, :annotation]

  def new({value, column_index}, row_index),
    do: %Point{
      row_index: row_index,
      column_index: column_index,
      value: value
    }

  def is_adjacent_point(
        %Point{row_index: row_a, column_index: column_a},
        %Point{row_index: row_b, column_index: column_b}
      ),
      do:
        (row_a !== row_b || column_a !== column_b) &&
          (row_a >= row_b - 1 && row_a <= row_b + 1) &&
          (column_a >= column_b - 1 && column_a <= column_b + 1)

  def set_annotation({%Point{value: "*"} = point, _adjacent_points}),
    do: %Point{point | annotation: "*"}

  def set_annotation({%Point{} = point, adjacent_points}) do
    mine_count = Enum.count(adjacent_points, &(&1.value === "*"))

    annotation =
      if mine_count > 0,
        do: to_string(mine_count),
        else: " "

    %Point{point | annotation: annotation}
  end
end

defmodule Matrix do
  alias Point

  defstruct [:point_list]

  def from_string_list(string_list),
    do:
      string_list
      |> Enum.with_index()
      |> Enum.map(&to_point_list/1)
      |> List.flatten()
      |> from_point_list()

  defp to_point_list({row_string, row_index}),
    do:
      row_string
      |> String.graphemes()
      |> Enum.with_index()
      |> Enum.map(&Point.new(&1, row_index))

  defp from_point_list(point_list),
    do: %Matrix{point_list: point_list}

  def annotate(%Matrix{point_list: point_list}),
    do: %Matrix{
      point_list:
        point_list
        |> map_to_surrounding_points(point_list)
        |> Map.to_list()
        |> Enum.map(&Point.set_annotation/1)
    }

  defp map_to_surrounding_points(_point, point_list),
    do:
      point_list
      |> Enum.map(&find_adjacent_points(point_list, &1))
      |> Map.new()

  defp find_adjacent_points(point_list, point),
    do: {point, filter_for_adjacent_points(point_list, point)}

  defp filter_for_adjacent_points(point_list, point),
    do: Enum.filter(point_list, &Point.is_adjacent_point(&1, point))

  def to_string(%Matrix{point_list: point_list}),
    do:
      point_list
      |> Enum.group_by(& &1.row_index)
      |> Enum.map(&convert_to_string/1)

  defp convert_to_string({_row_index, point_list}),
    do:
      point_list
      |> Enum.sort(&(&1.column_index < &2.column_index))
      |> Enum.map(& &1.annotation)
      |> Enum.join()
end

defmodule Minesweeper do
  alias Matrix

  @doc """
  Annotate empty spots next to mines with the number of mines next to them.
  """
  @spec annotate([String.t()]) :: [String.t()]
  def annotate([] = _board),
    do: []

  def annotate(board),
    do:
      board
      |> Matrix.from_string_list()
      |> Matrix.annotate()
      |> Matrix.to_string()
end
