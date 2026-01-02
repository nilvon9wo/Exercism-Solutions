open Grid
open Row_pair
open Row_with_corners
open Position
open Rectangle

let generate_all_positions_in_row grid row_index =
    let column_indices = List.init grid.column_count Fun.id in
    let make_position column_index = { row = row_index; column = column_index } in
    List.map make_position column_indices

let corner_positions_for_row grid row_index : position list =
    let positions_in_row = generate_all_positions_in_row grid row_index in
    let is_corner_position position = is_corner grid position in
    List.filter is_corner_position positions_in_row

let generate_row_with_corners grid row_index =
    let corners_in_row = corner_positions_for_row grid row_index in
    { row_index; positions = corners_in_row }

let rows_with_corner_positions grid =
    let all_row_indices = List.init grid.row_count Fun.id in
    List.map (generate_row_with_corners grid) all_row_indices

let rec column_exists_in_bottom top_column bottom_positions =
    match bottom_positions with
    | [] -> false
    | bottom_position :: remaining_positions ->
        let is_match = bottom_position.column = top_column in
        if is_match
            then true
            else column_exists_in_bottom top_column remaining_positions

let rec shared_columns top_row_positions bottom_row_positions =
    match top_row_positions with
    | [] -> []
    | top_position :: remaining_top_positions ->
        let found_in_bottom =
            column_exists_in_bottom top_position.column bottom_row_positions
        in
        if found_in_bottom
            then top_position :: (shared_columns remaining_top_positions bottom_row_positions)
            else shared_columns remaining_top_positions bottom_row_positions

let rec right_candidates_for_left left_corner shared_positions =
    match shared_positions with
    | [] -> []
    | candidate_position :: remaining_positions ->
        let is_right_of_left = candidate_position.column > left_corner.column in
        if is_right_of_left
            then candidate_position :: (right_candidates_for_left left_corner remaining_positions)
            else right_candidates_for_left left_corner remaining_positions

let rec rectangles_for_left left_corner shared_positions =
    match right_candidates_for_left left_corner shared_positions with
    | [] -> []
    | right_corner :: remaining_right_corners ->
            let rectangle_for_pair = Rectangle.create left_corner right_corner in
            rectangle_for_pair :: rectangles_for_left left_corner remaining_right_corners

let rec build_rectangles accumulated_rectangles shared_positions =
    match shared_positions with
    | [] -> accumulated_rectangles
    | left_corner :: remaining_left_corners ->
        let new_rectangles_for_corner = rectangles_for_left left_corner shared_positions in
        let all_rectangles = accumulated_rectangles @ new_rectangles_for_corner in
        build_rectangles all_rectangles remaining_left_corners

let candidate_rectangles_between_rows row_pair =
    let shared_positions = shared_columns row_pair.top_row.positions row_pair.bottom_row.positions in
    build_rectangles [] shared_positions

let rec fold_rectangles_from_bottom_rows accumulated_rectangles top_row remaining_bottom_rows =
    match remaining_bottom_rows with
    | [] -> accumulated_rectangles
    | bottom_row :: remaining_rows ->
        let rectangles_for_pair = candidate_rectangles_between_rows { top_row; bottom_row } in
        let all_rectangles = accumulated_rectangles @ rectangles_for_pair in
        fold_rectangles_from_bottom_rows all_rectangles top_row remaining_rows

let rec enumerate_rows accumulated_rectangles rows_with_corners =
    match rows_with_corners with
    | [] -> accumulated_rectangles
    | top_row :: remaining_rows ->
        let rectangles_in_top_row = fold_rectangles_from_bottom_rows [] top_row remaining_rows in
        let all_rectangles = accumulated_rectangles @ rectangles_in_top_row in
        enumerate_rows all_rectangles remaining_rows

let all_candidate_rectangles grid =
    let rows_with_corners = rows_with_corner_positions grid in
    enumerate_rows [] rows_with_corners

(* Generate Row_with_corners for all rows *)
let rows_with_corner_positions grid =
  let all_row_indices = List.init grid.row_count Fun.id in
  List.map (fun row_index ->
      let positions_in_row =
        List.init grid.column_count Fun.id
        |> List.map (fun col -> { row = row_index; column = col })
        |> List.filter (fun pos -> is_corner grid pos)
      in
      { row_index; positions = positions_in_row }
    ) all_row_indices