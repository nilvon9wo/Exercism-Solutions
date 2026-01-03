open Grid
open Position
open Row_with_corners

let position_at_row_and_column row_index column_index = {
        row = row_index;
        column = column_index;
    }

let all_positions_for_row grid row_index =
    let column_indices = List.init grid.column_count Fun.id in
    let make_position = position_at_row_and_column row_index in
    List.map make_position column_indices

let corner_positions_for_row grid row_index =
    let positions = all_positions_for_row grid row_index in
    let is_corner_position position = is_corner grid position in
    List.filter is_corner_position positions

let row_with_corner_positions grid row_index = {
        row_index = row_index;
        positions = corner_positions_for_row grid row_index;
    }

let rows_with_corner_positions grid =
    let row_indices = List.init grid.row_count Fun.id in
    let build_row = row_with_corner_positions grid in
    List.map build_row row_indices
