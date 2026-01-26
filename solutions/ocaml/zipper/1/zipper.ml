type 'a t = {
  focus: 'a Tree.t;        (* the focused tree node *)
  parent: 'a t option;     (* parent zipper *)
  is_left: bool;           (* whether focus is left child of parent *)
}

let rec equal (value_eq : 'a -> 'a -> bool) (z1 : 'a t) (z2 : 'a t) : bool =
  Tree.equal value_eq z1.focus z2.focus &&
  Option.equal (equal value_eq) z1.parent z2.parent &&
  z1.is_left = z2.is_left

let t_of_sexp (_ : Sexplib0.Sexp.t -> 'a) (_ : Sexplib0.Sexp.t) : 'a t =
  failwith "t_of_sexp not needed"

let sexp_of_t (_ : 'a -> Sexplib0.Sexp.t) (_ : 'a t) : Sexplib0.Sexp.t =
  Sexplib0.Sexp.Atom "<zipper>"

(* Create zipper focused on root *)
let of_tree (tree: 'a Tree.t) : 'a t =
  { focus = tree; parent = None; is_left = false }

(* Get complete tree from zipper *)
let rec to_tree (zipper: 'a t) : 'a Tree.t =
  match zipper.parent with
  | None -> zipper.focus
  | Some p ->
      let updated_parent =
        if zipper.is_left then
          { p.focus with left = Some zipper.focus }
        else
          { p.focus with right = Some zipper.focus }
      in
      to_tree { p with focus = updated_parent }

(* Get value of focus *)
let value (zipper: 'a t) : 'a =
  zipper.focus.value

(* Get left child zipper *)
let left (zipper: 'a t) : 'a t option =
  match zipper.focus.left with
  | None -> None
  | Some l -> Some { focus = l; parent = Some zipper; is_left = true }

(* Get right child zipper *)
let right (zipper: 'a t) : 'a t option =
  match zipper.focus.right with
  | None -> None
  | Some r -> Some { focus = r; parent = Some zipper; is_left = false }

(* Get parent zipper *)
let up (zipper: 'a t) : 'a t option =
  zipper.parent

(* Set value of focus *)
let set_value (new_value: 'a) (zipper: 'a t) : 'a t =
  { zipper with focus = { zipper.focus with value = new_value } }

(* Set left child of focus *)
let set_left (tree_opt: 'a Tree.t option) (zipper: 'a t) : 'a t =
  { zipper with focus = { zipper.focus with left = tree_opt } }

(* Set right child of focus *)
let set_right (tree_opt: 'a Tree.t option) (zipper: 'a t) : 'a t =
  { zipper with focus = { zipper.focus with right = tree_opt } }
