defmodule Queens do
  @type t :: %Queens{black: {integer, integer}, white: {integer, integer}}
  defstruct [:white, :black]

  @empty_row "_ _ _ _ _ _ _ _"
  @empty_board 0..7
               |> Enum.map(&{&1, @empty_row})
               |> Map.new()

  @symbol_by_colour %{
    :black => "B",
    :white => "W"
  }

  @nonexistent -1

  @doc """
  Creates a new set of Queens
  """
  @spec new(Keyword.t()) :: Queens.t()
  def new(queen_coordinates) do
    Enum.each(queen_coordinates, &check_valid_positions/1)
    Enum.each(queen_coordinates, &check_colour/1)

    %Queens{}
    |> Kernel.struct(queen_coordinates)
    |> check_unique_positions()
  end

  defp check_valid_positions({_color, {x, y}})
       when x < 0 or x > 7 or y < 0 or y > 7,
       do: raise(ArgumentError, message: "queen must have valid location")

  defp check_valid_positions(queen_coordinate),
    do: queen_coordinate

  defp check_unique_positions(%{black: black, white: white})
       when black == white,
       do: raise(ArgumentError, message: "queens must have unique location")

  defp check_unique_positions(queens),
    do: queens

  defp check_colour({colour, _coordinates})
       when colour not in [:black, :white],
       do: raise(ArgumentError, message: "queen must have valid colour")

  defp check_colour(_queen_coordinate),
    do: :ok

  @doc """
  Gives a string representation of the board with
  white and black queen locations shown
  """
  @spec to_string(Queens.t()) :: String.t()
  def to_string(queens),
    do:
      queens
      |> Map.from_struct()
      |> Enum.group_by(&get_rows/1)
      |> Enum.filter(fn {key, _value} -> key != @nonexistent end)
      |> Enum.map(&make_occupied_row/1)
      |> Map.new()
      |> add_to_board()

  defp add_to_board(queens_by_rows),
    do:
      @empty_board
      |> Map.merge(queens_by_rows)
      |> Map.values()
      |> Enum.join("\n")

  defp get_rows({_color, nil}),
    do: @nonexistent

  defp get_rows({_color, {row, _column}}),
    do: row

  defp make_occupied_row({row, queens}) do
    queens_by_columns =
      queens
      |> Enum.group_by(&get_column/1)
      |> Enum.map(fn {key, [{colour, _coordinates}]} -> {key, colour} end)
      |> Map.new()

    {
      row,
      make_occupied_row(_next_cell = 0, queens_by_columns, _accumulator = [])
    }
  end

  defp make_occupied_row(8, _queens_by_columns, accumulator),
    do:
      accumulator
      |> Enum.reverse()
      |> Enum.join(" ")

  defp make_occupied_row(next_cell, queens_by_columns, accumulator),
    do:
      make_occupied_row(
        next_cell + 1,
        queens_by_columns,
        [make_cell(Map.get(queens_by_columns, next_cell)) | accumulator]
      )

  defp get_column({_colour, {_row, column}}),
    do: column

  defp make_cell(nil),
    do: "_"

  defp make_cell(colour),
    do: Map.get(@symbol_by_colour, colour)

  @doc """
  Checks if the queens can attack each other
  """
  @spec can_attack?(Queens.t()) :: boolean
  def can_attack?(%Queens{black: _black, white: nil}),
    do: false

  def can_attack?(%Queens{black: nil, white: _white}),
    do: false

  def can_attack?(%Queens{black: {black_queen_x, _}, white: {white_queen_x, _}})
      when white_queen_x == black_queen_x,
      do: true

  def can_attack?(%Queens{black: {_, black_queen_y}, white: {_, white_queen_y}})
      when white_queen_y == black_queen_y,
      do: true

  def can_attack?(%Queens{
        black: {black_queen_x, black_queen_y},
        white: {white_queen_x, white_queen_y}
      }),
      do: abs(white_queen_x - black_queen_x) === abs(white_queen_y - black_queen_y)
end
