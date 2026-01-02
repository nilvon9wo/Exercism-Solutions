open Grid
open Position
open Row_with_corners

let all_column_indices grid =
    List.init grid.column_count Fun.id

let position_at_row_and_column row_index column_index =
    {
        row = row_index;
        column = column_index;
    }

let all_positions_for_row grid row_index =
    let column_indices =
        all_column_indices grid
    in
    List.map
        (position_at_row_and_column row_index)
        column_indices

let is_corner_in_grid grid position =
    is_corner grid position

let corner_positions_for_row grid row_index =
    let positions =
        all_positions_for_row grid row_index
    in
    List.filter
        (is_corner_in_grid grid)
        positions

let row_with_corner_positions grid row_index =
    let corner_positions =
        corner_positions_for_row grid row_index
    in
    {
        row_index = row_index;
        positions = corner_positions;
    }

let all_row_indices grid =
    List.init grid.row_count Fun.id

let rows_with_corner_positions grid =
    let row_indices =
        all_row_indices grid
    in
    List.map
        (row_with_corner_positions grid)
        row_indices
