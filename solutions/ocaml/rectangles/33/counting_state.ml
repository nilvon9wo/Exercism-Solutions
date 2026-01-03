open Count_context
open Rectangle_candidate
open Row_pair

type counting_state = {
  context: count_context;
  row_pair: row_pair;
  candidate: rectangle_candidate;
}

let create context row_pair candidate = {
        context;
        row_pair;
        candidate
}

let from_row_pair_and_columns context row_pair left_column remaining_columns =
    let candidate = Rectangle_candidate.from_row_pair row_pair left_column remaining_columns in
    create context row_pair candidate

let make_right_positions bottom_row remaining_columns =
    let create_position_for_column = Position.create bottom_row in
    List.map create_position_for_column remaining_columns

let build_counting_state context row_pair corner_columns left_column =
       let left_position = Position.create row_pair.top_row left_column in
       let right_positions = make_right_positions row_pair.bottom_row corner_columns in
       {
            context;
            row_pair;
            candidate = Rectangle_candidate.create left_position right_positions
       }

let update_state_candidate_right_position state remaining_right_positions = {
      state with
      candidate = {
          state.candidate
          with right_positions = remaining_right_positions
      }
}