open Grid
open Position
open Rectangle

let is_complete_rectangle grid top_left bottom_right =
    let rectangle = Rectangle.create top_left bottom_right in
    is_complete rectangle grid

let rec count_from_right grid left_position remaining_right_positions accumulated_count =
    match remaining_right_positions with
    | [] -> accumulated_count
    | right_position :: remaining_tail ->
        let is_valid =
            is_complete_rectangle grid left_position right_position
        in
        let updated_count =
            if is_valid then accumulated_count + 1 else accumulated_count
        in
        count_from_right grid left_position remaining_tail updated_count

let count_rectangles_for_column_pairs grid top_row bottom_row corner_columns =
    let rec count_from_left accumulated_count remaining_columns =
        match remaining_columns with
        | [] -> accumulated_count
        | left_column :: tail_columns ->
            let left_position = { row = top_row; column = left_column } in
            let right_positions =
                List.map
                    (fun column_index -> { row = bottom_row; column = column_index })
                    tail_columns
            in
            let updated_count =
                count_from_right grid left_position right_positions accumulated_count
            in
            count_from_left updated_count tail_columns
    in
    count_from_left 0 corner_columns

let shared_corner_columns top_row_columns bottom_row_columns =
    List.filter
        (fun column_index -> List.mem column_index bottom_row_columns)
        top_row_columns

let count_rectangles_for_row_pair
    grid
    top_row
    bottom_row
    top_row_columns
    bottom_row_columns
    =
    let shared_columns =
        shared_corner_columns top_row_columns bottom_row_columns
    in
    count_rectangles_for_column_pairs grid top_row bottom_row shared_columns

let rec count_rectangles_across_rows grid accumulated_count remaining_rows =
    match remaining_rows with
    | [] -> accumulated_count
    | (top_row, top_row_columns) :: tail_rows ->
        let updated_count =
            List.fold_left
                (fun running_total (bottom_row, bottom_row_columns) ->
                    running_total
                    + count_rectangles_for_row_pair
                        grid
                        top_row
                        bottom_row
                        top_row_columns
                        bottom_row_columns)
                accumulated_count
                tail_rows
        in
        count_rectangles_across_rows grid updated_count tail_rows

let count_all_rectangles grid rows_with_columns =
    count_rectangles_across_rows grid 0 rows_with_columns
