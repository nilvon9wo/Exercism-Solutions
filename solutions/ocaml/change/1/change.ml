open Base

let error_negative_target = "target can't be negative"
let error_unreachable_target = "can't make target with given coins"

let initialize_dp_table ~target : (int list option) array =
  let table_length = target + 1 in
  let dp_table = Array.create ~len:table_length None in
  dp_table.(0) <- Some [];
  dp_table

let compare_combinations_by_length a b =
  Int.compare (List.length a) (List.length b)

let select_shortest_combination combinations =
  List.min_elt combinations ~compare:compare_combinations_by_length

let is_coin_valid target_value coin =
  coin <= target_value

let prepend_coin_if_possible dp_table target_value coin =
  let remaining_value = target_value - coin in
  match dp_table.(remaining_value) with
  | None -> None
  | Some combination -> Some (coin :: combination)

let compute_combination dp_table coins target_value =
  let valid_coins = List.filter coins ~f:(is_coin_valid target_value) in
  let possible_combinations =
    List.filter_map valid_coins ~f:(prepend_coin_if_possible dp_table target_value)
  in
  select_shortest_combination possible_combinations

let fill_dp_table dp_table ~target ~coins =
  let rec loop value =
    if value > target
    then ()
    else
      let combination = compute_combination dp_table coins value in
      dp_table.(value) <- combination;
      loop (value + 1)
  in
  loop 1

let compute_minimal_change ~target ~coins =
  let dp_table = initialize_dp_table ~target in
  fill_dp_table dp_table ~target ~coins;
  match dp_table.(target) with
  | None -> Error error_unreachable_target
  | Some combination ->
      let sorted_combination = List.sort ~compare:Int.compare combination in
      Ok sorted_combination

let make_change ~target ~coins : (int list, string) Result.t =
  if target < 0
  then Error error_negative_target
  else compute_minimal_change ~target ~coins
