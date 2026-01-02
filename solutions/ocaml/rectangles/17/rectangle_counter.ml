open Column_pair
open Count_context
open Position
open Row_pair

let rec count_from_right context row_pair column_pair accumulated_count =
  match column_pair.rights with
  | [] -> accumulated_count
  | right :: remaining ->
      let updated_count =
        if context.validator column_pair.left right
        then accumulated_count + 1
        else accumulated_count
      in
      let new_pair = { column_pair with rights = remaining } in
      count_from_right context row_pair new_pair updated_count

let count_rectangles_for_column_pairs context row_pair corner_columns =
  let rec count_from_left accumulated_count = function
    | [] -> accumulated_count
    | left_column :: remaining_columns ->
        let left_position = { row = row_pair.top_row; column = left_column } in
        let right_positions =
          List.map (fun col -> { row = row_pair.bottom_row; column = col }) remaining_columns
        in
        let pair = { left = left_position; rights = right_positions } in
        let updated_count = count_from_right context row_pair pair accumulated_count in
        count_from_left updated_count remaining_columns
  in
  count_from_left 0 corner_columns

let shared_corner_columns top bottom =
  List.filter (fun col -> List.mem col bottom) top

let count_rectangles_for_row_pair context top_row bottom_row top_cols bottom_cols =
  let shared = shared_corner_columns top_cols bottom_cols in
  let pair = { top_row; bottom_row } in
  count_rectangles_for_column_pairs context pair shared

let rec count_rectangles_across_rows context accumulated_rows = function
  | [] -> accumulated_rows
  | (top_row, top_cols) :: remaining ->
      let updated =
        List.fold_left
          (fun total (bottom_row, bottom_cols) ->
             total + count_rectangles_for_row_pair
                       context top_row bottom_row top_cols bottom_cols)
          accumulated_rows
          remaining
      in
      count_rectangles_across_rows context updated remaining

let count context rows =
  count_rectangles_across_rows context 0 rows
