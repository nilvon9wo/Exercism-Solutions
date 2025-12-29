open Base

type bst =
  | Empty
  | Node of {
      value : int;
      left  : bst;
      right : bst;
    }

let empty = Empty

let get_field tree ~extract =
  match tree with
  | Empty -> Error "empty tree"
  | Node { value; left; right } -> Ok (extract value left right)

let value tree = get_field tree ~extract:(fun value _ _ -> value)
let left  tree = get_field tree ~extract:(fun _ left _ -> left)
let right tree = get_field tree ~extract:(fun _ _ right -> right)

let make_node value left right = Node { value; left; right }

let rec insert x tree =
  match tree with
  | Empty -> make_node x Empty Empty
  | Node { value; left; right } ->
      if x <= value
      then make_node value (insert x left) right
      else make_node value left (insert x right)

let rec to_list = function
  | Empty -> []
  | Node { value; left; right } ->
      to_list left @ (value :: to_list right)
