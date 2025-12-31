open Base
open Best_product_state
open Palindrome_search_config
open Search_context

type palindrome_products = {
  value : int option;
  factors : (int * int) list;
}

let is_palindrome (number : int) : bool =
  let number_as_string = Int.to_string number in
  let reversed_string = String.rev number_as_string in
  String.equal number_as_string reversed_string

let compare_factor_pairs (left_a, left_b) (right_a, right_b) : int =
  match Int.compare left_a right_a with
  | 0 -> Int.compare left_b right_b
  | primary_comparison -> primary_comparison

let sort_factor_pairs (pairs : (int * int) list) : (int * int) list =
  List.sort pairs ~compare:compare_factor_pairs

let state_to_palindrome_products state = {
        value = state.product;
        factors = sort_factor_pairs state.factor_pairs
}

let rec iterate_right_factor context =
  if context.current_right_factor > context.config.max_factor
      then ()
      else
        let product = context.current_left_factor * context.current_right_factor in
        if is_palindrome product
            then update_best context product;
        let next_context = { context with current_right_factor = context.current_right_factor + 1 } in
        iterate_right_factor next_context

let rec iterate_left_factor context =
  if context.current_left_factor > context.config.max_factor
      then ()
      else
        let right_context = { context with current_right_factor = context.current_left_factor } in
        let () = iterate_right_factor right_context in
        let next_context = { context with current_left_factor = context.current_left_factor + 1 } in
        iterate_left_factor next_context

let find_extreme_palindrome config =
  let state = create_new_state () in
  let initial_context = create_search_context config state in
  iterate_left_factor initial_context;
  state_to_palindrome_products state

let compute_palindrome config : (palindrome_products, string) Result.t =
  if config.min_factor > config.max_factor
    then Error "min must be <= max"
    else Ok (find_extreme_palindrome config)

let smallest ~min ~max =
  compute_palindrome {
                         min_factor = min;
                         max_factor = max;
                         is_better_product = (<)
                     }

let largest ~min ~max =
  compute_palindrome {
                         min_factor = min;
                         max_factor = max;
                         is_better_product = (>)
                     }

