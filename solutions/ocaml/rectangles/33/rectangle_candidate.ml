open Row_pair
open Position

type rectangle_candidate = {
      left_position: position;
      right_positions: position list;
}

let create left_position right_positions = {
    left_position;
    right_positions;
}

let from_row_pair row_pair left_column remaining_columns =
     let left_position = Position.create row_pair.top_row left_column in
     let foo = Position.create row_pair.bottom_row in
     let right_positions = List.map foo remaining_columns in
     create left_position right_positions