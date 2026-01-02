open Position

let rec count_from_right
    validate_rectangle
    top_row
    bottom_row
    left_position
    remaining_right_positions
    accumulated_count =
  match remaining_right_positions with
  | [] ->
      accumulated_count
  | right_position :: remaining_tail ->
      let updated_count =
        if validate_rectangle left_position right_position
        then accumulated_count + 1
        else accumulated_count
      in
      count_from_right
        validate_rectangle
        top_row
        bottom_row
        left_position
        remaining_tail
        updated_count

let count_rectangles_for_column_pairs
    validate_rectangle
    top_row
    bottom_row
    corner_columns =
  let rec count_from_left accumulated_count remaining_columns =
    match remaining_columns with
    | [] ->
        accumulated_count
    | left_column :: remaining_tail ->
        let left_position =
          { row = top_row; column = left_column }
        in
        let right_positions =
          List.map
            (fun column ->
              { row = bottom_row; column = column })
            remaining_tail
        in
        let updated_count =
          count_from_right
            validate_rectangle
            top_row
            bottom_row
            left_position
            right_positions
            accumulated_count
        in
        count_from_left updated_count remaining_tail
  in
  count_from_left 0 corner_columns

let shared_corner_columns top_columns bottom_columns =
  List.filter
    (fun column -> List.mem column bottom_columns)
    top_columns

let count_rectangles_for_row_pair
    validate_rectangle
    top_row
    bottom_row
    top_columns
    bottom_columns =
  let shared_columns =
    shared_corner_columns top_columns bottom_columns
  in
  count_rectangles_for_column_pairs
    validate_rectangle
    top_row
    bottom_row
    shared_columns

let rec count_rectangles_across_rows
    validate_rectangle
    accumulated_count
    remaining_rows =
  match remaining_rows with
  | [] ->
      accumulated_count
  | (top_row, top_columns) :: remaining_tail ->
      let updated_count =
        List.fold_left
          (fun running_total (bottom_row, bottom_columns) ->
            running_total +
            count_rectangles_for_row_pair
              validate_rectangle
              top_row
              bottom_row
              top_columns
              bottom_columns)
          accumulated_count
          remaining_tail
      in
      count_rectangles_across_rows
        validate_rectangle
        updated_count
        remaining_tail

let count validate_rectangle rows =
  count_rectangles_across_rows
    validate_rectangle
    0
    rows
