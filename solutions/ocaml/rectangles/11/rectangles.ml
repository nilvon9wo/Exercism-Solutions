open Grid
open Position
open Rectangle
open Rectangle_search
open Row_with_corners

(* Convert Row_with_corners to simple corner column lists for counting *)
let rows_with_corner_columns grid =
  let rows = rows_with_corner_positions grid in
  List.map (fun row -> (row.row_index, List.map (fun pos -> pos.column) row.positions)) rows

let is_complete_rectangle grid top_left bottom_right =
  let rectangle = Rectangle.create top_left bottom_right in
  is_complete rectangle grid

(* --- Counting logic remains completely unchanged --- *)
let rec count_from_right grid top_row bottom_row left_position remaining_right_positions accumulated_count =
  match remaining_right_positions with
  | [] -> accumulated_count
  | right_position :: remaining_tail ->
      let updated_count =
        if is_complete_rectangle grid left_position right_position
        then accumulated_count + 1
        else accumulated_count
      in
      count_from_right grid top_row bottom_row left_position remaining_tail updated_count

let count_rectangles_for_column_pairs grid top_row bottom_row corner_columns =
  let rec count_from_left accumulated_count = function
    | [] -> accumulated_count
    | left_column :: remaining_columns ->
        let left_position = { row = top_row; column = left_column } in
        let right_positions =
          List.map (fun column_index -> { row = bottom_row; column = column_index }) remaining_columns
        in
        let updated_count = count_from_right grid top_row bottom_row left_position right_positions accumulated_count in
        count_from_left updated_count remaining_columns
  in
  count_from_left 0 corner_columns

let shared_corner_columns top_row_columns bottom_row_columns =
  List.filter (fun column_index -> List.mem column_index bottom_row_columns) top_row_columns

let count_rectangles_for_row_pair grid top_row bottom_row top_row_columns bottom_row_columns =
  let shared_columns = shared_corner_columns top_row_columns bottom_row_columns in
  count_rectangles_for_column_pairs grid top_row bottom_row shared_columns

let rec count_rectangles_across_rows grid accumulated_count = function
  | [] -> accumulated_count
  | (top_row, top_row_columns) :: remaining_rows ->
      let updated_count =
        List.fold_left
          (fun running_total (bottom_row, bottom_row_columns) ->
            running_total + count_rectangles_for_row_pair grid top_row bottom_row top_row_columns bottom_row_columns)
          accumulated_count
          remaining_rows
      in
      count_rectangles_across_rows grid updated_count remaining_rows

let count_all_rectangles grid =
  let rows_with_columns = rows_with_corner_columns grid in
  count_rectangles_across_rows grid 0 rows_with_columns

let count_rectangles grid_rows =
  let grid = Grid.create grid_rows in
  if grid.row_count = 0 || grid.column_count = 0 then 0
  else count_all_rectangles grid
