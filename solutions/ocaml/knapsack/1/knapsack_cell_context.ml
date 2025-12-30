open Knapsack_context_item

type knapsack_cell_context = {
  dp_table : int array array;
  items : knapsack_context_item array;
  item_index : int;
  capacity : int;
}

let previous_row_index (cell_context: knapsack_cell_context) =
  cell_context.item_index - 1

let value_if_skipped (cell_context: knapsack_cell_context) =
  cell_context.dp_table
    .(previous_row_index cell_context)
    .(cell_context.capacity)

let current_item (cell_context: knapsack_cell_context) =
  cell_context.items
    .(previous_row_index cell_context)

let remaining_value_in_previous_row (cell_context: knapsack_cell_context) =
  let item = current_item cell_context in
  let remaining_capacity = cell_context.capacity - item.weight in
  cell_context.dp_table.(previous_row_index cell_context).(remaining_capacity)

let value_if_taken (cell_context: knapsack_cell_context) =
  let item = current_item cell_context in
  let value_from_remaining_capacity = remaining_value_in_previous_row cell_context in
  item.value + value_from_remaining_capacity

let compute_cell (cell_context: knapsack_cell_context) =
  let skipped = value_if_skipped cell_context in
  let item = current_item cell_context in

  if item.weight > cell_context.capacity
  then skipped
  else Int.max skipped (value_if_taken cell_context)