let requires_count (values : 'a list) (is_valid : int -> bool) (error_message : string) : 'a list =
  let length = List.length values in
  let is_not_valid = not (is_valid length) in
  if is_not_valid
      then failwith error_message
      else values

let require_at_least_one_value (values : 'a list) : 'a list =
  requires_count values (fun count -> count >= 1) "Operation requires at least one value."

let require_at_least_two_values (values : 'a list) : 'a list =
  requires_count values (fun count -> count >= 2) "Operation requires at least two values."

let require_exactly_two_values (values : 'a list) : 'a list =
  requires_count values (fun count -> count = 2) "Operation requires exactly two values."

let rec remove_first_item item = function
  | [] -> []
  | x :: xs -> if x = item
        then xs
        else x :: remove_first_item item xs

let take_one (values : 'a list ref) (selector : 'a list -> 'a) : 'a =
      let item = selector !values in
      values := remove_first_item item !values;
      item

let get_first_element (values : 'a list) : 'a =
    match values with
       | [] -> failwith "List is empty"
       | first_element::_ -> first_element

let shift (values : 'a list ref) : 'a =
  take_one values get_first_element

let rec get_last_element (values : 'a list) : 'a =
    match values with
       | [] -> failwith "List is empty"
       | [x] -> x
       | _::rest_of_list -> get_last_element rest_of_list

let pop (values : 'a list ref) : 'a =
      take_one values get_last_element

let get_penultimate_element (values : 'a list) =
    let index_of_penultimate = List.length values - 2 in
    List.nth values index_of_penultimate