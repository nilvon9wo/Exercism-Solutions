open Grid
open Position

let rec is_horizontal_edge_range
    (grid : grid)
    row_index
    column_start
    column_end =
  if column_start > column_end then true
  else
    match char_at grid { row = row_index; column = column_start } with
    | '+' | '-' ->
        is_horizontal_edge_range
          grid
          row_index
          (column_start + 1)
          column_end
    | _ -> false

let is_horizontal_edge
    (grid : grid)
    row_index
    left_column
    right_column =
  is_horizontal_edge_range grid row_index left_column right_column

let rec is_vertical_edge_range
    (grid : grid)
    column_index
    row_start
    row_end =
  if row_start > row_end then true
  else
    match char_at grid { row = row_start; column = column_index } with
    | '+' | '|' ->
        is_vertical_edge_range
          grid
          column_index
          (row_start + 1)
          row_end
    | _ -> false

let is_vertical_edge
    (grid : grid)
    column_index
    top_row
    bottom_row =
  is_vertical_edge_range grid column_index top_row bottom_row

let corner_columns_for_row
    (grid : grid)
    row_index =
  List.init grid.column_count Fun.id
  |> List.filter (fun column_index ->
       is_corner grid { row = row_index; column = column_index })

let rows_with_corner_columns (grid : grid) =
  List.init grid.row_count Fun.id
  |> List.map (fun row_index ->
       (row_index, corner_columns_for_row grid row_index))

let is_complete_rectangle
    (grid : grid)
    top_left_position
    bottom_right_position =
  is_horizontal_edge
    grid
    top_left_position.row
    top_left_position.column
    bottom_right_position.column
  && is_horizontal_edge
       grid
       bottom_right_position.row
       top_left_position.column
       bottom_right_position.column
  && is_vertical_edge
       grid
       top_left_position.column
       top_left_position.row
       bottom_right_position.row
  && is_vertical_edge
       grid
       bottom_right_position.column
       top_left_position.row
       bottom_right_position.row

let rec count_from_right
    (grid : grid)
    top_row
    bottom_row
    left_position
    remaining_right_positions
    accumulated_count =
  match remaining_right_positions with
  | [] -> accumulated_count
  | right_position :: remaining_tail ->
      let updated_count =
        if is_complete_rectangle grid left_position right_position
        then accumulated_count + 1
        else accumulated_count
      in
      count_from_right
        grid
        top_row
        bottom_row
        left_position
        remaining_tail
        updated_count

let count_rectangles_for_column_pairs
    grid
    top_row
    bottom_row
    corner_columns =
  let rec count_from_left accumulated_count = function
    | [] -> accumulated_count
    | left_column :: remaining_columns ->
        let left_position =
          { row = top_row; column = left_column }
        in
        let right_positions =
          List.map
            (fun column_index ->
               { row = bottom_row; column = column_index })
            remaining_columns
        in
        let updated_count =
          count_from_right
            grid
            top_row
            bottom_row
            left_position
            right_positions
            accumulated_count
        in
        count_from_left updated_count remaining_columns
  in
  count_from_left 0 corner_columns

let shared_corner_columns
    top_row_columns
    bottom_row_columns =
  List.filter
    (fun column_index ->
       List.mem column_index bottom_row_columns)
    top_row_columns

let count_rectangles_for_row_pair
    grid
    top_row
    bottom_row
    top_row_columns
    bottom_row_columns =
  let shared_columns =
    shared_corner_columns
      top_row_columns
      bottom_row_columns
  in
  count_rectangles_for_column_pairs
    grid
    top_row
    bottom_row
    shared_columns

let rec count_rectangles_across_rows
    grid
    accumulated_count = function
  | [] -> accumulated_count
  | (top_row, top_row_columns) :: remaining_rows ->
      let updated_count =
        List.fold_left
          (fun running_total
               (bottom_row, bottom_row_columns) ->
             running_total
             + count_rectangles_for_row_pair
                 grid
                 top_row
                 bottom_row
                 top_row_columns
                 bottom_row_columns)
          accumulated_count
          remaining_rows
      in
      count_rectangles_across_rows
        grid
        updated_count
        remaining_rows

let count_all_rectangles (grid : grid) =
  let rows_with_columns =
    rows_with_corner_columns grid
  in
  count_rectangles_across_rows
    grid
    0
    rows_with_columns

let count_rectangles (grid_rows : string array) : int =
  let grid = create grid_rows in
  if grid.row_count = 0 || grid.column_count = 0 then 0
  else count_all_rectangles grid
