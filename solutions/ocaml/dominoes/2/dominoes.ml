open Base

type dominoe = int * int

(* --- Conversion --- *)
let domino_values_of_input (input : dominoe list) : Domino_value.t list =
  List.map input ~f:(fun (left, right) -> Domino_value.create ~left ~right)

let dominoe_of_domino_value (domino_value : Domino_value.t) : dominoe =
  (domino_value.left, domino_value.right)

(* --- Starting attempts --- *)

let remaining_after_start ~(all_dominoes : Domino_value.t list) ~(starting_domino : Domino_value.t) : Remaining_dominoes.t option =
  let remaining_dominoes = Remaining_dominoes.from_list all_dominoes in
  Remaining_dominoes.remove_one remaining_dominoes starting_domino

let initial_state_for_start ~(starting_domino : Domino_value.t) ~(remaining_dominoes : Remaining_dominoes.t) : Chain_search_state.t =
  Chain_search_state.create_initial ~starting_domino ~remaining:remaining_dominoes

let build_chain_from_start ~starting_domino ~remaining_dominoes =
      let initial_state = initial_state_for_start ~starting_domino ~remaining_dominoes in
      Domino_chain_builder.try_build_closed_chain initial_state

let try_starting_domino ~(all_dominoes : Domino_value.t list) ~(starting_domino : Domino_value.t) : Domino_chain.t option =
  match remaining_after_start ~all_dominoes ~starting_domino with
  | None ->
      None
  | Some remaining_dominoes ->
      build_chain_from_start ~starting_domino ~remaining_dominoes


let rec try_all_starting_dominoes ~(all_dominoes : Domino_value.t list) ~(candidates : Domino_value.t list) : Domino_chain.t option =
  match candidates with
  | [] ->
      None
  | domino :: rest ->
      match try_starting_domino ~all_dominoes ~starting_domino:domino with
      | Some chain ->
          Some chain
      | None ->
          try_all_starting_dominoes ~all_dominoes ~candidates:rest

(* --- Public API --- *)

let chain (input : dominoe list) : dominoe list option =
  let domino_values = domino_values_of_input input in
  match domino_values with
  | [] ->
      Some []
  | _ ->
      match try_all_starting_dominoes ~all_dominoes:domino_values ~candidates:domino_values with
      | None ->
          None
      | Some chain ->
          Some (List.map chain ~f:dominoe_of_domino_value)
