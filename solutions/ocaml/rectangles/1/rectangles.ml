let character_at grid_rows row_index column_index =
  grid_rows.(row_index).[column_index]

let is_corner_cell grid_rows row_index column_index =
  character_at grid_rows row_index column_index = '+'

let rec is_horizontal_edge_range grid_rows row_index current_column last_column =
  if current_column > last_column
    then true
    else
      match character_at grid_rows row_index current_column with
      | '+' | '-' ->
          is_horizontal_edge_range
            grid_rows
            row_index
            (current_column + 1)
            last_column
      | _ -> false

let is_horizontal_edge grid_rows row_index left_column right_column =
  is_horizontal_edge_range
    grid_rows
    row_index
    left_column
    right_column

let rec is_vertical_edge_range grid_rows column_index current_row last_row =
  if current_row > last_row
    then true
    else
      match character_at grid_rows current_row column_index with
      | '+' | '|' ->
          is_vertical_edge_range
            grid_rows
            column_index
            (current_row + 1)
            last_row
      | _ -> false

let is_vertical_edge grid_rows column_index top_row bottom_row =
  is_vertical_edge_range
    grid_rows
    column_index
    top_row
    bottom_row

let corner_columns_for_row grid_rows column_count row_index =
  List.init column_count Fun.id
  |> List.filter (fun column_index ->
       is_corner_cell grid_rows row_index column_index)

let rows_with_corner_columns grid_rows row_count column_count =
  List.init row_count Fun.id
  |> List.map (fun row_index ->
       (row_index, corner_columns_for_row grid_rows column_count row_index))

let is_complete_rectangle
    grid_rows
    top_row
    bottom_row
    left_column
    right_column =
  is_horizontal_edge grid_rows top_row left_column right_column
  && is_horizontal_edge grid_rows bottom_row left_column right_column
  && is_vertical_edge grid_rows left_column top_row bottom_row
  && is_vertical_edge grid_rows right_column top_row bottom_row

let rec count_from_right
    grid_rows
    top_row
    bottom_row
    left_column
    remaining_columns
    accumulated_count =
  match remaining_columns with
  | [] -> accumulated_count
  | right_column :: tail ->
      let updated_count =
        if
          is_complete_rectangle
            grid_rows
            top_row
            bottom_row
            left_column
            right_column
          then accumulated_count + 1
          else accumulated_count
      in
      count_from_right
        grid_rows
        top_row
        bottom_row
        left_column
        tail
        updated_count

let count_rectangles_for_column_pairs
    grid_rows
    top_row
    bottom_row
    corner_columns =
  let rec count_from_left accumulated_count = function
    | [] -> accumulated_count
    | left_column :: remaining_columns ->
        let updated_count =
          count_from_right
            grid_rows
            top_row
            bottom_row
            left_column
            remaining_columns
            accumulated_count
        in
        count_from_left updated_count remaining_columns
  in
  count_from_left 0 corner_columns

let shared_corner_columns top_columns bottom_columns =
  List.filter
    (fun column_index -> List.mem column_index bottom_columns)
    top_columns

let count_rectangles_for_row_pair
    grid_rows
    top_row
    bottom_row
    top_columns
    bottom_columns =
  let shared_columns =
    shared_corner_columns top_columns bottom_columns
  in
  count_rectangles_for_column_pairs
    grid_rows
    top_row
    bottom_row
    shared_columns

let rec count_rectangles_across_rows grid_rows accumulated_count = function
  | [] -> accumulated_count
  | (top_row, top_columns) :: remaining_rows ->
      let updated_count =
        List.fold_left
          (fun running_total (bottom_row, bottom_columns) ->
             running_total
             + count_rectangles_for_row_pair
                 grid_rows
                 top_row
                 bottom_row
                 top_columns
                 bottom_columns)
          accumulated_count
          remaining_rows
      in
      count_rectangles_across_rows
        grid_rows
        updated_count
        remaining_rows

let count_all_rectangles grid_rows row_count column_count =
  let rows =
    rows_with_corner_columns
      grid_rows
      row_count
      column_count
  in
  count_rectangles_across_rows grid_rows 0 rows

let count_rectangles (grid_rows : string array) : int =
  let row_count = Array.length grid_rows in
  if row_count = 0
    then 0
    else
      let column_count = String.length grid_rows.(0) in
      if column_count = 0
        then 0
        else
          count_all_rectangles
            grid_rows
            row_count
            column_count
