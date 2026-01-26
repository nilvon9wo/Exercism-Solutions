type 'a t = {
  focus: 'a Tree.t;
  parent: 'a t option;
  is_left: bool;
}

let rec equal (element_equal : 'a -> 'a -> bool) (zipper_left : 'a t) (zipper_right : 'a t) : bool =
  Tree.equal element_equal zipper_left.focus zipper_right.focus &&
  Option.equal (equal element_equal) zipper_left.parent zipper_right.parent &&
  zipper_left.is_left = zipper_right.is_left

let t_of_sexp (_ : Sexplib0.Sexp.t -> 'a) (_ : Sexplib0.Sexp.t) : 'a t =
  failwith "t_of_sexp not needed"

let sexp_of_t (_ : 'a -> Sexplib0.Sexp.t) (_ : 'a t) : Sexplib0.Sexp.t =
  Sexplib0.Sexp.Atom "<zipper>"

let of_tree (tree_root: 'a Tree.t) : 'a t =
  { focus = tree_root; parent = None; is_left = false }

let rec to_tree (current_zipper: 'a t) : 'a Tree.t =
  match current_zipper.parent with
  | None -> current_zipper.focus
  | Some parent_zipper ->
      let updated_parent_focus =
        if current_zipper.is_left then
          { parent_zipper.focus with left = Some current_zipper.focus }
        else
          { parent_zipper.focus with right = Some current_zipper.focus }
      in
      to_tree { parent_zipper with focus = updated_parent_focus }

let value (zipper: 'a t) : 'a =
  zipper.focus.value

let left (zipper: 'a t) : 'a t option =
  match zipper.focus.left with
  | None -> None
  | Some left_child -> Some { focus = left_child; parent = Some zipper; is_left = true }

let right (zipper: 'a t) : 'a t option =
  match zipper.focus.right with
  | None -> None
  | Some right_child -> Some { focus = right_child; parent = Some zipper; is_left = false }

let up (zipper: 'a t) : 'a t option =
  zipper.parent

let set_value (new_value: 'a) (zipper: 'a t) : 'a t =
  { zipper with focus = { zipper.focus with value = new_value } }

let set_left (new_left: 'a Tree.t option) (zipper: 'a t) : 'a t =
  { zipper with focus = { zipper.focus with left = new_left } }

let set_right (new_right: 'a Tree.t option) (zipper: 'a t) : 'a t =
  { zipper with focus = { zipper.focus with right = new_right } }
