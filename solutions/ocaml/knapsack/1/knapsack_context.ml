open Knapsack_context_item
open Knapsack_cell_context

type knapsack_context = {
  items : knapsack_context_item array;
  num_items : int;
  capacity : int;
}

let create items capacity = {
    items = Array.of_list items;
    num_items = List.length items;
    capacity;
  }

let initialize_dp_table context =
  let rows = context.num_items + 1 in
  let columns = context.capacity + 1 in
  Array.make_matrix rows columns 0

let fill_dp_table context dp_table =
  for item_index = 1 to context.num_items do
    for capacity = 0 to context.capacity do
      dp_table.(item_index).(capacity) <- compute_cell {
                                             dp_table;
                                             items = context.items;
                                             item_index;
                                             capacity;
                                           }
    done
  done

let create_dp_table context =
  let dp_table = initialize_dp_table context in
  fill_dp_table context dp_table;
  dp_table
