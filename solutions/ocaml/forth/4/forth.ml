let new_definition_pattern (string : string) : string option =
  let is_long_enough = String.length string >= 2 in
  let starts_with_colon = string.[0] = ':' in
  let ends_with_semicolon = string.[String.length string - 1] = ';' in
  let is_valid_definition = is_long_enough && starts_with_colon && ends_with_semicolon in
  if is_valid_definition
    then Some (String.sub string 1 (String.length string - 2))
    else None


let default_handler_by_token : (string, Context.t -> Context.t) Hashtbl.t =
  let tbl = Hashtbl.create 16 in
  let add key fn =
    Hashtbl.replace tbl (String.lowercase_ascii key) fn
  in
  add "+" Math_helpers.add;
  add "-" Math_helpers.subtract;
  add "*" Math_helpers.multiply;
  add "/" Math_helpers.divide;
  add "dup" Stack_helpers.duplicate_last;
  add "drop" Stack_helpers.drop_last;
  add "swap" Stack_helpers.swap_last;
  add "over" Stack_helpers.penultimate_value_copy;
  tbl

let rec do_while (body : unit -> unit) (cond : unit -> bool) : unit =
  body ();
  if cond ()
    then do_while body cond

let shift_tokens_to_stack (context : Context.t) (tokens : string list ref) : string =
  let next_operation = ref "" in
  let body () =
    let token = List_helpers.shift tokens in
    match int_of_string_opt token with
    | Some value ->
        context.stack := !(context.stack) @ [value]
    | None ->
        next_operation := token
  in
  let cond () = !tokens <> [] && !next_operation = "" in
  do_while body cond;
  !next_operation

let rec follow_instruction (context : Context.t) (instruction : string) : Context.t =
  let tokens : string list ref =
    instruction
    |> String.split_on_char ' '
    |> List.filter (fun x -> x <> "")
    |> ref
  in

  let rec loop () =
    if !tokens <> [] then begin
      let next_operation = shift_tokens_to_stack context tokens in
      ignore (do_instruction context next_operation);
      loop ()
    end
  in

  loop ();
  context

and do_instruction (context : Context.t) (operation : string) : Context.t =
  if operation = "" then
    context
  else
    match Context.get_instruction context operation with
    | Some instruction -> follow_instruction context instruction
    | None ->
        match Hashtbl.find_opt default_handler_by_token (String.lowercase_ascii operation) with
        | Some f -> f context
        | None ->
            failwith (Printf.sprintf "Unknown operation: `%s`." operation)

let evaluate_instruction (context : Context.t) (instruction : string) : Context.t =
  match new_definition_pattern instruction with
  | Some definition -> Context.define_handler context { Definition_string.value = definition }
  | None -> follow_instruction context instruction

let evaluate (instructions : string list) : int list option =
  try
    let final_context = List.fold_left evaluate_instruction (Context.create ()) instructions in
    Some !(final_context.Context.stack)
  with _ ->
    None
