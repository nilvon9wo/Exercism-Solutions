defmodule Cell do
  defstruct [:row, :column, :value]

  def new(value),
    do: %Cell{row: 1, column: value, value: value}

  def new({value, index}, row),
    do: %Cell{row: row, column: index + 1, value: value}
end

defmodule Grid do
  alias Cell

  defstruct [
    :cells,
    :dimension,
    :final_value,
    :last_populated_location,
    :next_direction,
    :next_value
  ]

  def new(dimension) do
    cells = create_cells(dimension)

    %Grid{
      cells: cells,
      dimension: dimension,
      final_value: :math.pow(dimension, 2),
      last_populated_location: %Cell{row: dimension, column: 1},
      next_direction: :up,
      next_value: find_max_value(cells) + 1
    }
  end

  defp create_cells(dimension),
    do:
      1..dimension
      |> Enum.map(&create_columns(dimension, &1))
      |> List.flatten()

  defp create_columns(dimension, 1),
    do: Enum.map(1..dimension, &Cell.new(&1))

  defp create_columns(dimension, dimension = row),
    do:
      (dimension + row - 1)..(2 * dimension + row - 2)
      |> Enum.map(& &1)
      |> Enum.reverse()
      |> Enum.with_index()
      |> Enum.map(&Cell.new(&1, row))

  defp create_columns(dimension, row),
    do:
      1..dimension
      |> Enum.map(&calculate_cell_value(dimension, row, &1))
      |> Enum.with_index()
      |> Enum.map(&Cell.new(&1, row))

  defp calculate_cell_value(dimension, row, index) do
    if index === dimension,
      do: dimension + row - 1,
      else: nil
  end

  defp find_max_value(cells),
    do:
      cells
      |> Enum.filter(&(&1.value != nil))
      |> Enum.map(& &1.value)
      |> Enum.max()

  def to_matrix(%Grid{cells: cells}),
    do:
      cells
      |> Enum.group_by(& &1.row)
      |> Enum.map(&cells_to_list/1)
      |> Enum.sort(&(elem(&1, 0) < elem(&2, 0)))
      |> Enum.map(&elem(&1, 1))

  defp cells_to_list({key, cells}),
    do: {
      key,
      cells
      |> Enum.sort(&(&1.column < &2.column))
      |> Enum.map(& &1.value)
    }

  def get_cell(%Grid{cells: cells}, %Cell{row: row, column: column} = _next_location),
    do:
      cells
      |> Enum.filter(&(&1.row == row and &1.column == column))
      |> hd()

  def update(%Grid{cells: cells} = grid, target_cell, next_value),
    do: %Grid{
      grid
      | cells: update(cells, target_cell, next_value),
        last_populated_location: target_cell,
        next_value: next_value + 1
    }

  def update(cells, %Cell{row: row, column: column} = target_cell, new_value) do
    [
      %Cell{target_cell | value: new_value}
      | Enum.reject(cells, &(&1.row == row and &1.column == column))
    ]
  end
end

defmodule Orientation do
  @next_direction_by_last_direction %{
    :up => :right,
    :right => :down,
    :down => :left,
    :left => :up
  }

  def get_next_cell(%Cell{row: last_row, column: last_column}, :up),
    do: %Cell{row: last_row - 1, column: last_column}

  def get_next_cell(%Cell{row: last_row, column: last_column}, :right),
    do: %Cell{row: last_row, column: last_column + 1}

  def get_next_cell(%Cell{row: last_row, column: last_column}, :down),
    do: %Cell{row: last_row + 1, column: last_column}

  def get_next_cell(%Cell{row: last_row, column: last_column}, :left),
    do: %Cell{row: last_row, column: last_column - 1}

  def get_next_direction(last_direction),
    do: @next_direction_by_last_direction[last_direction]
end

defmodule Spiral do
  @doc """
  Given the dimension, return a square matrix of numbers in clockwise spiral order.
  """
  alias Cell
  alias Orientation
  alias Grid

  @spec matrix(dimension :: integer) :: list(list(integer))
  def matrix(0 = _dimension),
    do: []

  def matrix(1 = _dimension),
    do: [[1]]

  def matrix(dimension),
    do:
      dimension
      |> Grid.new()
      |> fill()
      |> Grid.to_matrix()

  defp fill(%Grid{final_value: final_value, next_value: next_value} = grid)
       when next_value > final_value,
       do: grid

  defp fill(
         %Grid{
           last_populated_location: last_populated_location,
           next_direction: next_direction,
           next_value: next_value
         } = grid
       ) do
    target_cell =
      Grid.get_cell(grid, Orientation.get_next_cell(last_populated_location, next_direction))

    if target_cell.value != nil,
      do: fill(%Grid{grid | next_direction: Orientation.get_next_direction(next_direction)}),
      else: fill(Grid.update(grid, target_cell, next_value))
  end
end
