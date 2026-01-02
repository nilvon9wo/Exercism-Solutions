open Column_pair
open Count_context
open Position
open Row_pair
open Row_with_columns

(* --- Helpers --- *)

let compute_updated_count context left_position right_position accumulated_count =
  if context.validator left_position right_position
  then accumulated_count + 1
  else accumulated_count

let make_right_positions bottom_row remaining_columns =
  let rec build_right_positions acc = function
    | [] -> List.rev acc
    | column_index :: tail ->
        let position = { row = bottom_row; column = column_index } in
        build_right_positions (position :: acc) tail
  in
  build_right_positions [] remaining_columns

let make_column_pair left_position right_positions =
  { left = left_position; rights = right_positions }

let filter_shared_columns top_columns bottom_columns =
  let rec filter_columns acc = function
    | [] -> List.rev acc
    | column_index :: tail ->
        let acc' =
          if List.mem column_index bottom_columns
          then column_index :: acc
          else acc
        in
        filter_columns acc' tail
  in
  filter_columns [] top_columns

(* --- Core counting --- *)

let rec count_from_right context _row_pair column_pair accumulated_count =
  match column_pair.rights with
  | [] -> accumulated_count
  | right :: remaining_rights ->
      let updated_count =
        compute_updated_count context column_pair.left right accumulated_count
      in
      let new_pair = { column_pair with rights = remaining_rights } in
      count_from_right context _row_pair new_pair updated_count

let rec count_from_left context row_pair accumulated_count remaining_left_columns =
  match remaining_left_columns with
  | [] -> accumulated_count
  | left_column :: remaining_columns ->
      let left_position = { row = row_pair.top_row; column = left_column } in
      let right_positions = make_right_positions row_pair.bottom_row remaining_columns in
      let pair = make_column_pair left_position right_positions in
      let updated_count = count_from_right context row_pair pair accumulated_count in
      count_from_left context row_pair updated_count remaining_columns

let count_rectangles_for_column_pairs context row_pair corner_columns =
  count_from_left context row_pair 0 corner_columns

let count_rectangles_for_row_pair context top_row bottom_row =
  let shared_columns = filter_shared_columns top_row.columns bottom_row.columns in
  let row_pair = { top_row = top_row.row_index; bottom_row = bottom_row.row_index } in
  count_rectangles_for_column_pairs context row_pair shared_columns

let rec count_rectangles_across_rows context accumulated_rows = function
  | [] -> accumulated_rows
  | top_row :: remaining_rows ->
      let rec count_bottom_rows total = function
        | [] -> total
        | bottom_row :: bottom_tail ->
            let total' = total + count_rectangles_for_row_pair context top_row bottom_row in
            count_bottom_rows total' bottom_tail
      in
      let updated_rows = count_bottom_rows accumulated_rows remaining_rows in
      count_rectangles_across_rows context updated_rows remaining_rows

let count context rows =
  count_rectangles_across_rows context 0 rows
