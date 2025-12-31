open Best_product_state
open Palindrome_search_config

type search_context = {
  config : palindrome_search_config;
  state : best_product_state;
  current_left_factor : int;
  current_right_factor : int;
}

let create_search_context config state = {
                                             config;
                                             state;
                                             current_left_factor = config.min_factor;
                                             current_right_factor = config.min_factor;
                                           }

let replace_best_product context product =
  let state = context.state in
  let factor_pair = (context.current_left_factor, context.current_right_factor) in
  state.product <- Some product;
  state.factor_pairs <- [factor_pair]

let append_equal_product context =
  let state = context.state in
  let factor_pair = (context.current_left_factor, context.current_right_factor) in
  state.factor_pairs <- factor_pair :: state.factor_pairs

let update_best context product =
    let is_better_product = context.config.is_better_product in
    let state = context.state in
    match state.product with
    | None -> replace_best_product context product
    | Some existing_product ->
        if is_better_product product existing_product
              then replace_best_product context product
              else if product = existing_product
                  then append_equal_product context
                  else ()