type t = (string, Context.t -> Context.t) Hashtbl.t

let create () : t = Hashtbl.create 16

let add (handlers : t) key ~(f : Context.t -> Context.t) : unit =
  let lowercase_key = String.lowercase_ascii key in
  Hashtbl.replace handlers lowercase_key f

let get (handlers : t) (key : string) : (Context.t -> Context.t) option =
  let lowercase_key = String.lowercase_ascii key in
  Hashtbl.find_opt handlers lowercase_key

let create_default () : t =
  let handlers = create () in
  add handlers "+" ~f:Math_helpers.add;
  add handlers "-" ~f:Math_helpers.subtract;
  add handlers "*" ~f:Math_helpers.multiply;
  add handlers "/" ~f:Math_helpers.divide;
  add handlers "dup" ~f:Stack_helpers.duplicate_last;
  add handlers "drop" ~f:Stack_helpers.drop_last;
  add handlers "swap" ~f:Stack_helpers.swap_last;
  add handlers "over" ~f:Stack_helpers.penultimate_value_copy;
  handlers
