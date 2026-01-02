open Grid
open Position

(* Horizontal and vertical edge checks *)
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

(* Corners in a row as positions *)
let corner_positions_for_row (grid : grid) row_index : position list =
  List.init grid.column_count Fun.id
  |> List.filter_map (fun col_index ->
         let pos = {row = row_index; column = col_index} in
         if is_corner grid pos then Some pos else None)

let rows_with_corner_positions (grid : grid) : position list list =
  List.init grid.row_count Fun.id
  |> List.map (fun row_index -> corner_positions_for_row grid row_index)

(* Complete rectangle check using positions *)
let is_complete_rectangle (grid : grid) (top_left : position) (bottom_right : position) =
  is_horizontal_edge grid top_left.row top_left.column bottom_right.column
  && is_horizontal_edge grid bottom_right.row top_left.column bottom_right.column
  && is_vertical_edge grid top_left.column top_left.row bottom_right.row
  && is_vertical_edge grid bottom_right.column top_left.row bottom_right.row

(* Count rectangles for one left position across remaining rights *)
let rec count_from_right (grid : grid) top_row bottom_row left_pos remaining_rights accumulated_count =
  match remaining_rights with
  | [] -> accumulated_count
  | right_pos :: tail ->
      let updated_count =
        if is_complete_rectangle grid left_pos right_pos
        then accumulated_count + 1
        else accumulated_count
      in
      count_from_right grid top_row bottom_row left_pos tail updated_count

(* Count rectangles for all pairs in a row *)
let count_rectangles_for_row_positions (grid : grid) row_positions =
  let rec count_from_left accumulated_count = function
    | [] -> accumulated_count
    | left_pos :: remaining_rights ->
        let updated_count = count_from_right grid left_pos.row left_pos.row left_pos remaining_rights accumulated_count in
        count_from_left updated_count remaining_rights
  in
  count_from_left 0 row_positions

(* Shared columns (positions) between two rows *)
let shared_corner_positions top_positions bottom_positions =
  List.filter (fun pos ->
      List.exists (fun b_pos -> b_pos.column = pos.column) bottom_positions)
    top_positions

(* Count rectangles between two rows *)
let count_rectangles_for_row_pair grid top_positions bottom_positions =
  let shared_positions = shared_corner_positions top_positions bottom_positions in
  count_rectangles_for_row_positions grid shared_positions

(* Count rectangles across all row pairs *)
let rec count_rectangles_across_rows grid accumulated_count = function
  | [] -> accumulated_count
  | top_positions :: remaining_rows ->
      let updated_count =
        List.fold_left
          (fun running_total bottom_positions ->
             running_total + count_rectangles_for_row_pair grid top_positions bottom_positions)
          accumulated_count
          remaining_rows
      in
      count_rectangles_across_rows grid updated_count remaining_rows

(* All rectangles in the grid *)
let count_all_rectangles (grid : grid) =
  let row_positions = rows_with_corner_positions grid in
  count_rectangles_across_rows grid 0 row_positions

(* Public API *)
let count_rectangles (grid_rows : string array) : int =
  let grid = create grid_rows in
  if grid.row_count = 0 || grid.column_count = 0 then 0
  else count_all_rectangles grid
