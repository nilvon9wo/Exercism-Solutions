type counting_input = {
      context: Count_context.count_context;
      left_position: Position.position;
      right_position: Position.position;
}

let create ~context ~left_position ~right_position = {
    context;
    left_position;
    right_position
}
