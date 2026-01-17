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
  List.iter (fun (k,f) -> add handlers k ~f) [
         ("+", Math_helpers.add);
         ("-", Math_helpers.subtract);
         ("*", Math_helpers.multiply);
         ("/", Math_helpers.divide);
         ("dup", Stack_helpers.duplicate_last);
         ("drop", Stack_helpers.drop_last);
         ("swap", Stack_helpers.swap_last);
         ("over", Stack_helpers.penultimate_value_copy);
  ];
  handlers
