open Count_context
open Grid
open Position
open Rectangle
open Rectangle_counter
open Rectangle_search
open Row_with_corners

let row_with_columns_from_row row =
    let extract_column position = position.column in
    row.positions
    |> List.map extract_column
    |> Row_with_columns.create row.row_index

let rows_with_corner_columns grid =
    grid
    |> rows_with_corner_positions
    |> List.map row_with_columns_from_row

let rectangle_is_complete grid left right =
    right
    |> Rectangle.create left
    |> is_complete grid

let count_all_rectangles grid =
    count {
            rows = rows_with_corner_columns grid;
            validator = rectangle_is_complete grid;
        }

let is_empty grid =
    grid.row_count = 0
    || grid.column_count = 0

let count_rectangles grid_rows =
    let grid = Grid.create grid_rows in
    if is_empty grid
        then 0
        else count_all_rectangles grid
