open Position

type rectangle_candidate = {
      left_position: position;
      right_positions: position list;
}

let create left_position right_positions = {
    left_position;
    right_positions;
}
