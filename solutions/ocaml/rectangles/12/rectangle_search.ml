open Grid
open Position
open Row_with_corners

let is_corner_position grid position =
    is_corner grid position

let corner_positions_for_row grid row_index =
    let all_column_indices = List.init grid.column_count Fun.id in
    let positions_in_row = List.map
            (fun column_index -> { row = row_index; column = column_index })
            all_column_indices
    in
    List.filter (is_corner_position grid) positions_in_row

let row_with_corner_positions grid row_index =
    let positions =
        corner_positions_for_row grid row_index
    in
    {
        row_index = row_index;
        positions = positions;
    }

let rows_with_corner_positions grid =
    let all_row_indices =
        List.init grid.row_count Fun.id
    in
    List.map (row_with_corner_positions grid) all_row_indices
