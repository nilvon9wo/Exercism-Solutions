defmodule Cell do
  defstruct [:x, :y, :z, :stone]

  @stone_by_characters %{
    "O" => :white,
    "X" => :black,
    "." => :none
  }

  def new({stone, column_index}, row_index),
    do: %Cell{
      x: row_index,
      y: column_index,
      z: -row_index - column_index,
      stone: stone
    }

  def from_strings(board_strings),
    # This list must be reversed for hex grid math to work!
    do:
      board_strings
      |> Enum.reverse()
      |> convert_to_stones()
      |> Enum.map(&Enum.with_index/1)
      |> Enum.with_index()
      |> Enum.map(&to_cells/1)
      |> List.flatten()

  defp convert_to_stones(board),
    do: convert_to_stones(board, _accumulator = [])

  defp convert_to_stones([], accumulator),
    do: accumulator

  defp convert_to_stones([head_row | tail_rows], accumulator) do
    atom_row =
      head_row
      |> String.graphemes()
      |> Enum.map(&Map.get(@stone_by_characters, &1))

    convert_to_stones(tail_rows, [atom_row | accumulator])
  end

  defp to_cells({stones_with_column_index, row_index}),
    do: Enum.map(stones_with_column_index, &Cell.new(&1, row_index))
end

# @see: https://www.redblobgames.com/grids/hexagons/
defmodule HexGrid do
  defstruct [:row_max, :column_max, :cells]

  def from_strings(board_strings),
    do: from_strings(board_strings, Cell.from_strings(board_strings))

  defp from_strings(board_strings, cells),
    do: %HexGrid{
      cells: cells,
      row_max: length(board_strings) - 1,
      column_max: find_max_column(cells)
    }

  defp find_max_column(cells),
    do:
      cells
      |> Enum.map(& &1.y)
      |> Enum.max()

  def is_connected(%HexGrid{cells: cells} = hex_grid, stone, index_to_connect) do
    occupied_cells = Enum.filter(cells, &(&1.stone === stone))

    connected_start_cells =
      hex_grid
      |> get_start_cells(stone, index_to_connect)
      |> find_matched_cells(occupied_cells)

    connected_end_cells =
      hex_grid
      |> get_end_cells(stone, index_to_connect)
      |> find_matched_cells(occupied_cells)

    length(connected_start_cells) > 0 &&
      length(connected_end_cells) > 0 &&
      (is_start_and_end_overlapping(connected_start_cells, connected_end_cells) ||
         is_connected(
           connected_start_cells,
           connected_end_cells,
           filter_disconnected_cells(occupied_cells, connected_start_cells, connected_end_cells)
         ))
  end

  def is_connected(_connected_start_cells, _connected_end_cells, []),
    do: false

  def is_connected(connected_start_cells, connected_end_cells, disconnected_cells) do
    connected_start_cells =
      find_neighbors(disconnected_cells, connected_start_cells) ++ connected_start_cells

    connected_end_cells =
      find_neighbors(disconnected_cells, connected_end_cells) ++ connected_end_cells

    remaining_disconnected_cells =
      filter_disconnected_cells(disconnected_cells, connected_start_cells, connected_end_cells)

    is_start_and_end_overlapping(connected_start_cells, connected_end_cells) ||
      (length(remaining_disconnected_cells) !== length(disconnected_cells) &&
         is_connected(
           connected_start_cells,
           connected_end_cells,
           remaining_disconnected_cells
         ))
  end

  defp is_start_and_end_overlapping(connected_start_cells, connected_end_cells),
    do: length(find_matched_cells(connected_start_cells, connected_end_cells)) > 0

  defp get_start_cells(%HexGrid{column_max: column_max}, stone, :x = _index_to_connect),
    do: Enum.map(0..column_max, &make_required_cell(&1, 0, stone))

  defp get_start_cells(%HexGrid{row_max: row_max}, stone, :y = _index_to_connect),
    do: Enum.map(0..row_max, &make_required_cell(0, &1, stone))

  defp get_end_cells(%HexGrid{column_max: column_max}, stone, :x = _index_to_connect),
    do: Enum.map(0..column_max, &make_required_cell(&1, column_max, stone))

  defp get_end_cells(%HexGrid{row_max: row_max}, stone, :y = _index_to_connect),
    do: Enum.map(0..row_max, &make_required_cell(row_max, &1, stone))

  defp make_required_cell(x, y, stone),
    do: %Cell{
      x: x,
      y: y,
      z: -x - y,
      stone: stone
    }

  defp find_matched_cells(required_cells, occupied_cells),
    do:
      occupied_cells
      |> MapSet.new()
      |> MapSet.intersection(MapSet.new(required_cells))
      |> MapSet.to_list()

  defp find_neighbors(disconnected_cells, connected_cells),
    do: Enum.filter(disconnected_cells, &is_neighbor(&1, connected_cells))

  defp is_neighbor(disconnected_cell, connected_cells)
       when is_list(connected_cells),
       do: Enum.any?(connected_cells, &is_neighbor(disconnected_cell, &1))

  defp is_neighbor(disconnected_cell, connected_cell),
    do:
      is_neighbor(disconnected_cell, connected_cell, :x, :y, :z) ||
        is_neighbor(disconnected_cell, connected_cell, :x, :z, :y) ||
        is_neighbor(disconnected_cell, connected_cell, :y, :x, :z) ||
        is_neighbor(disconnected_cell, connected_cell, :y, :z, :x) ||
        is_neighbor(disconnected_cell, connected_cell, :z, :x, :y) ||
        is_neighbor(disconnected_cell, connected_cell, :z, :y, :x)

  defp is_neighbor(disconnected_cell, connected_cell, same, one_down, one_up),
    do:
      Map.get(disconnected_cell, same) === Map.get(connected_cell, same) &&
        Map.get(disconnected_cell, one_down) === Map.get(connected_cell, one_down) - 1 &&
        Map.get(disconnected_cell, one_up) === Map.get(connected_cell, one_up) + 1

  defp filter_disconnected_cells(
         previously_disconnected,
         connected_start_cells,
         connected_end_cells
       ),
       do:
         Enum.filter(
           previously_disconnected,
           &(!Enum.member?(connected_start_cells ++ connected_end_cells, &1))
         )
end

defmodule Connect do
  @doc """
  Calculates the winner (if any) of a board
  using "O" as the white player
  and "X" as the black player
  """
  @spec result_for([String.t()]) :: :none | :black | :white
  def result_for(board) do
    hex_grid = HexGrid.from_strings(board)

    cond do
      # white wins from top to bottom
      HexGrid.is_connected(hex_grid, :white, :y) ->
        :white

      # black wins from left to right
      HexGrid.is_connected(hex_grid, :black, :x) ->
        :black

      true ->
        :none
    end
  end
end
