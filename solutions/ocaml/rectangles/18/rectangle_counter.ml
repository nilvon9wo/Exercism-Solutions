open Column_pair
open Count_context
open Position
open Row_pair

(* --- Helpers --- *)

let compute_updated_count context left_position right_position accumulated =
  if context.validator left_position right_position
      then accumulated + 1
      else accumulated

let make_right_positions bottom_row remaining_columns =
  List.map (fun col -> { row = bottom_row; column = col }) remaining_columns

let make_column_pair left_position right_positions =
  { left = left_position; rights = right_positions }

let filter_shared_columns top bottom =
  List.filter (fun col -> List.mem col bottom) top

(* --- Core counting --- *)

let rec count_from_right context row_pair column_pair accumulated_count =
  match column_pair.rights with
  | [] -> accumulated_count
  | right :: remaining_rights ->
      let updated_count = compute_updated_count context column_pair.left right accumulated_count in
      let new_pair = { column_pair with rights = remaining_rights } in
      count_from_right context row_pair new_pair updated_count

let rec count_from_left context row_pair accumulated_count = function
  | [] -> accumulated_count
  | left_col :: remaining_cols ->
      let left_position = { row = row_pair.top_row; column = left_col } in
      let right_positions = make_right_positions row_pair.bottom_row remaining_cols in
      let pair = make_column_pair left_position right_positions in
      let updated_count = count_from_right context row_pair pair accumulated_count in
      count_from_left context row_pair updated_count remaining_cols

let count_rectangles_for_column_pairs context row_pair corner_columns =
  count_from_left context row_pair 0 corner_columns

let count_rectangles_for_row_pair context top_row bottom_row top_cols bottom_cols =
  let shared = filter_shared_columns top_cols bottom_cols in
  let pair = { top_row; bottom_row } in
  count_rectangles_for_column_pairs context pair shared

let rec count_rectangles_across_rows context accumulated = function
  | [] -> accumulated
  | (top_row, top_cols) :: remaining ->
      let updated =
        List.fold_left (fun total (bottom_row, bottom_cols) ->
             total + count_rectangles_for_row_pair
                       context top_row bottom_row top_cols bottom_cols) accumulated remaining
      in
      count_rectangles_across_rows context updated remaining

let count context rows =
  count_rectangles_across_rows context 0 rows
