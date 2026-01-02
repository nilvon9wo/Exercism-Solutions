open Grid
open Position

let rec is_horizontal_edge_range (grid : grid) row col_start col_end =
  if col_start > col_end then true
  else
    match char_at grid {row; column = col_start} with
    | '+' | '-' -> is_horizontal_edge_range grid row (col_start + 1) col_end
    | _ -> false

let is_horizontal_edge (grid : grid) row left_column right_column =
  is_horizontal_edge_range grid row left_column right_column

let rec is_vertical_edge_range (grid : grid) column row_start row_end =
  if row_start > row_end then true
  else
    match char_at grid {row = row_start; column} with
    | '+' | '|' -> is_vertical_edge_range grid column (row_start + 1) row_end
    | _ -> false

let is_vertical_edge (grid : grid) column top_row bottom_row =
  is_vertical_edge_range grid column top_row bottom_row

let corner_columns_for_row (grid : grid) row_index =
  List.init grid.column_count Fun.id
  |> List.filter (fun col_index -> is_corner grid {row = row_index; column = col_index})

let rows_with_corner_columns (grid : grid) =
  List.init grid.row_count Fun.id
  |> List.map (fun row_index -> (row_index, corner_columns_for_row grid row_index))

let is_complete_rectangle (grid : grid) top_row bottom_row left_col right_col =
  is_horizontal_edge grid top_row left_col right_col
  && is_horizontal_edge grid bottom_row left_col right_col
  && is_vertical_edge grid left_col top_row bottom_row
  && is_vertical_edge grid right_col top_row bottom_row

let rec count_from_right (grid : grid) top_row bottom_row left_col remaining_cols accumulated_count =
  match remaining_cols with
  | [] -> accumulated_count
  | right_col :: tail ->
      let updated_count =
        if is_complete_rectangle grid top_row bottom_row left_col right_col
        then accumulated_count + 1
        else accumulated_count
      in
      count_from_right grid top_row bottom_row left_col tail updated_count

let count_rectangles_for_column_pairs grid top_row bottom_row corner_columns =
  let rec count_from_left accumulated_count = function
    | [] -> accumulated_count
    | left_col :: remaining_cols ->
        let updated_count =
          count_from_right grid top_row bottom_row left_col remaining_cols accumulated_count
        in
        count_from_left updated_count remaining_cols
  in
  count_from_left 0 corner_columns

let shared_corner_columns top_columns bottom_columns =
  List.filter (fun col -> List.mem col bottom_columns) top_columns

let count_rectangles_for_row_pair grid top_row bottom_row top_columns bottom_columns =
  let shared_cols = shared_corner_columns top_columns bottom_columns in
  count_rectangles_for_column_pairs grid top_row bottom_row shared_cols

let rec count_rectangles_across_rows grid accumulated_count = function
  | [] -> accumulated_count
  | (top_row, top_cols) :: remaining_rows ->
      let updated_count =
        List.fold_left
          (fun running_total (bottom_row, bottom_cols) ->
             running_total + count_rectangles_for_row_pair grid top_row bottom_row top_cols bottom_cols)
          accumulated_count
          remaining_rows
      in
      count_rectangles_across_rows grid updated_count remaining_rows

let count_all_rectangles (grid : grid) =
  let rows = rows_with_corner_columns grid in
  count_rectangles_across_rows grid 0 rows

let count_rectangles (grid_rows : string array) : int =
  let grid = create grid_rows in
  if grid.row_count = 0 || grid.column_count = 0 then 0
  else count_all_rectangles grid
