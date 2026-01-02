open Count_context
open Grid
open Position
open Rectangle
open Rectangle_counter
open Rectangle_search
open Row_with_corners

let row_to_row_with_columns row =
    let extract_column position = position.column in
    List.map extract_column row.positions
    |> Row_with_columns.create row.row_index

let rows_with_corner_columns grid =
    grid
    |> rows_with_corner_positions
    |> List.map row_to_row_with_columns

let foo2 grid left right =
    right
    |> Rectangle.create left
    |> is_complete grid

let count_all_rectangles grid =
    count {
            rows = rows_with_corner_columns grid;
            validator = foo2 grid
    }

let is_empty grid =
    grid.row_count = 0
        || grid.column_count = 0

let count_rectangles grid_rows =
  let grid = Grid.create grid_rows in
  if (is_empty grid)
        then 0
        else count_all_rectangles grid
