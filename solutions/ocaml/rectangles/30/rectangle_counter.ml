open Count_context
open Counting_state
open Rectangle_candidate
open Row_pair
open Row_pair_with_columns
open Row_with_columns

(* --- Helpers --- *)
(* FIXME: too many parameters! *)
let compute_updated_count context left_position right_position_list accumulated_count =
    if context.validator left_position right_position_list
        then accumulated_count + 1
        else accumulated_count

let make_right_positions bottom_row remaining_columns =
    let create_position_for_column = Position.create bottom_row in
    List.map create_position_for_column remaining_columns

let filter_shared_columns top_columns bottom_columns =
    let is_shared_column column = List.mem column bottom_columns in
    List.filter is_shared_column top_columns

(* --- Core counting --- *)
let rec count_from_right state accumulated_count =
  match state.candidate.right_positions with
  | [] ->
      accumulated_count
  | right_position :: remaining_right_positions ->
      (* FIXME: too many parameters! *)
      let updated_count = compute_updated_count state.context state.candidate.left_position right_position accumulated_count in
      let new_state = {
            state with
            candidate = { state.candidate with right_positions = remaining_right_positions }
      } in
      count_from_right new_state updated_count

let rec count_from_left context row_pair accumulated_count = function
  | [] ->
      accumulated_count
  | left_column :: remaining_columns ->
      let left_position = Position.create row_pair.top_row left_column in
      let right_positions = make_right_positions row_pair.bottom_row remaining_columns in
      let counting_state = {
            context;
            row_pair;
            candidate = Rectangle_candidate.create left_position right_positions;
      } in
      let updated_count = count_from_right counting_state accumulated_count in
      count_from_left context row_pair updated_count remaining_columns

let build_counting_state context row_pair corner_columns left_column =
       let left_position = Position.create row_pair.top_row left_column in
       let right_positions = make_right_positions row_pair.bottom_row corner_columns in
       {
            context;
            row_pair;
            candidate = Rectangle_candidate.create left_position right_positions
       }

let count_rectangles_for_rectangle_candidates context row_pair corner_columns =
    let state_builder = build_counting_state context row_pair corner_columns in
    let counting_states = List.map state_builder corner_columns in
    let fold_function accumulated_count state = count_from_right state accumulated_count in
    List.fold_left fold_function 0 counting_states

let count_rectangles_for_row_pair context row_pair_with_columns =
    let top_row = row_pair_with_columns.top in
    let bottom_row = row_pair_with_columns.bottom in
    let row_pair = Row_pair.create top_row.row_index bottom_row.row_index in
    let shared_columns = filter_shared_columns top_row.columns bottom_row.columns in
    count_rectangles_for_rectangle_candidates context row_pair shared_columns

let accumulate_row_pair_counts context total row_pair_with_columns =
    let rectangle_count = count_rectangles_for_row_pair context row_pair_with_columns in
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
