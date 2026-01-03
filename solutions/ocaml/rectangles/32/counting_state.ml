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