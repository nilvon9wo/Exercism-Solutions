type item = {
  weight : int;
  value : int;
}

open Knapsack_context
open Knapsack_context_item

let to_context_item (item : item) : knapsack_context_item = {
  weight = item.weight;
  value = item.value;
}

let to_context_items items =
    items
    |> List.map to_context_item

let maximum_value items capacity =
  let knapsack_context_items = to_context_items items in
  let context = create knapsack_context_items capacity in
  let dp_table = create_dp_table context in
  dp_table.(context.num_items).(context.capacity)