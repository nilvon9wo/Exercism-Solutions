open Column_pair
open Count_context
open Row_pair

(* --- Helpers --- *)
(* FIXME: too many parameters! *)
let compute_updated_count context left_position right_position accumulated_count =
    if context.validator left_position right_position
        then accumulated_count + 1
        else accumulated_count

let make_right_positions bottom_row remaining_columns =
    let create_position_for_column = Position.create bottom_row in
    List.map create_position_for_column remaining_columns

let filter_shared_columns top_columns bottom_columns =
    let is_shared column = List.mem column bottom_columns in
    List.filter is_shared top_columns

(* --- Core counting --- *)
(* FIXME: too many parameters! *)
let rec count_from_right context row_pair column_pair accumulated_count =
    match column_pair.rights with
    | [] -> accumulated_count
    | right_column :: remaining_rights ->
        (* FIXME: too many parameters! *)
        let updated_count =
            compute_updated_count context column_pair.left right_column accumulated_count
        in
        let remaining_pair = { column_pair with rights = remaining_rights } in
        (* FIXME: too many parameters! *)
        count_from_right context row_pair remaining_pair updated_count

let rec count_from_left context row_pair accumulated_count = function
    | [] -> accumulated_count
    | left_column :: remaining_columns ->
        let left_position = Position.create row_pair.top_row left_column in
        let right_positions = make_right_positions row_pair.bottom_row remaining_columns in
        let column_pair = Column_pair.create left_position right_positions in
        (* FIXME: too many parameters! *)
        let updated_count = count_from_right context row_pair column_pair accumulated_count in
        (* FIXME: too many parameters! *)
        count_from_left context row_pair updated_count remaining_columns

let count_rectangles_for_column_pairs context row_pair corner_columns =
    (* FIXME: This is not actually too many parameters, but because of the way
        the function operations, the expression is equally unreadable.
        We should use some type to fix this. *)
    count_from_left context row_pair 0 corner_columns

(* FIXME: too many parameters! *)
let count_rectangles_for_row_pair context top_row bottom_row top_columns bottom_columns =
    let shared_columns = filter_shared_columns top_columns bottom_columns in
    let row_pair = { top_row; bottom_row } in
    count_rectangles_for_column_pairs context row_pair shared_columns

(* FIXME: too many parameters! *)
let accumulate_row_pair_counts context top_row top_columns total remaining_row_data =
    let (bottom_row, bottom_columns) = remaining_row_data in
    (* FIXME: too many parameters! *)
    let rectangle_count =
        count_rectangles_for_row_pair context top_row bottom_row top_columns bottom_columns
    in
    total + rectangle_count

let rec count_rectangles_across_rows context accumulated_count = function
    | [] -> accumulated_count
    | (top_row, top_columns) :: remaining_rows ->
        let fold_function = accumulate_row_pair_counts context top_row top_columns in
        let updated_count = List.fold_left fold_function accumulated_count remaining_rows in
        count_rectangles_across_rows context updated_count remaining_rows

let count context rows =
    count_rectangles_across_rows context 0 rows
