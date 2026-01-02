open Count_context
open Counting_state
open Rectangle_candidate
open Row_pair
open Row_pair_with_columns
open Row_with_columns

(* --- Helpers --- *)
(* FIXME: too many parameters! *)
let compute_updated_count context left_position right_positions accumulated_count =
    if context.validator left_position right_positions
        then accumulated_count + 1
        else accumulated_count

let make_right_positions bottom_row remaining_columns =
    let create_position_for_column = Position.create bottom_row in
    List.map create_position_for_column remaining_columns

let filter_shared_columns top_columns bottom_columns =
    let is_shared column = List.mem column bottom_columns in
    List.filter is_shared top_columns

(* --- Core counting --- *)
let rec count_from_right state accumulated_count =
  match state.candidate.right_positions with
  | [] ->
      accumulated_count
  | right_column :: remaining_rights ->
	(* FIXME: too many parameters! *)
      let updated_count = compute_updated_count state.context state.candidate.left_position right_column accumulated_count in
      let new_state = {
            state with candidate = { state.candidate with right_positions = remaining_rights
      } } in
      count_from_right new_state updated_count


let rec count_from_left context row_pair accumulated_count = function
  | [] ->
      accumulated_count
  | left_column :: remaining_columns ->
      let left_position = Position.create row_pair.top_row left_column in
      let right_positions = make_right_positions row_pair.bottom_row remaining_columns in
      let state = {
            context;
            row_pair;
            candidate = Rectangle_candidate.create left_position right_positions;
      } in
      let updated_count = count_from_right state accumulated_count in
      count_from_left context row_pair updated_count remaining_columns

let count_rectangles_for_rectangle_candidates context row_pair corner_columns =
    (* FIXME: This is not actually too many parameters, but because of the way
        the function operations, the expression is equally unreadable.
        We should use some type to fix this. *)
    count_from_left context row_pair 0 corner_columns

let count_rectangles_for_row_pair context row_pair_with_columns =
    let row_pair = Row_pair.create row_pair_with_columns.top.row_index row_pair_with_columns.bottom.row_index in
    let shared_columns = filter_shared_columns row_pair_with_columns.top.columns row_pair_with_columns.bottom.columns in
    count_rectangles_for_rectangle_candidates context row_pair shared_columns

let accumulate_row_pair_counts context total row_pair_with_columns =
    let rectangle_count =
        count_rectangles_for_row_pair context row_pair_with_columns
    in
    total + rectangle_count

let rec count_rectangles_across_rows context accumulated_count = function
    | [] ->
        accumulated_count
    | top_row_with_columns :: remaining_rows ->
        let make_row_pair = Row_pair_with_columns.create top_row_with_columns in
        let row_pairs_with_columns = List.map make_row_pair remaining_rows in
        let accumulate = accumulate_row_pair_counts context in
        let updated_count = List.fold_left accumulate accumulated_count row_pairs_with_columns in
        count_rectangles_across_rows context updated_count remaining_rows

let count context =
    count_rectangles_across_rows context 0 context.rows
