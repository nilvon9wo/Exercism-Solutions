let new_definition_pattern (string : string) : string option =
  let is_long_enough = String.length string >= 2 in
  let starts_with_colon = string.[0] = ':' in
  let ends_with_semicolon = string.[String.length string - 1] = ';' in
  let is_valid_definition = is_long_enough && starts_with_colon && ends_with_semicolon in
  if is_valid_definition
    then Some (String.sub string 1 (String.length string - 2))
    else None

let default_handler_by_token = Default_handlers.create_default ()

let push_number_or_mark_operation (context : Context.t) (tokens : string list ref) next_operation =
    let token = List_helpers.shift tokens in
    match int_of_string_opt token with
    | Some value ->
        context.stack := !(context.stack) @ [value]
    | None ->
        next_operation := token

let should_keep_shifting_numbers (tokens : string list ref) next_operation =
    !tokens <> []
        && !next_operation = ""

let shift_tokens_to_stack (context : Context.t) (tokens : string list ref) : string =
  let next_operation = ref "" in
  let loop_body () = push_number_or_mark_operation context tokens next_operation in
  let continue_condition () = should_keep_shifting_numbers tokens next_operation in
  Control_flow_helpers.do_while loop_body continue_condition;
  !next_operation

let get_tokens (instruction : string) =
    instruction
    |> String.split_on_char ' '
    |> List.filter (fun token -> token <> "")
    |> ref

let rec push_numbers_until_next_operation_found (context : Context.t) (tokens : string list ref) : unit =
    if !tokens <> [] then begin
        let next_operation = shift_tokens_to_stack context tokens in
        ignore (do_instruction context next_operation);
        push_numbers_until_next_operation_found context tokens
    end

and follow_instruction (context : Context.t) (instruction : string) : Context.t =
    let tokens : string list ref = get_tokens instruction in
    push_numbers_until_next_operation_found context tokens;
    context

and do_instruction (context : Context.t) (operation : string) : Context.t =
  if operation = ""
        then context
        else execute_operation context operation

and execute_operation (context : Context.t) (operation : string) =
    match Context.get_instruction context operation with
    | Some instruction -> follow_instruction context instruction
    | None ->
        match Default_handlers.get default_handler_by_token operation with
        | Some f -> f context
        | None ->
            failwith (Printf.sprintf "Unknown operation: `%s`." operation)

let evaluate_instruction (context : Context.t) (instruction : string) : Context.t =
  match new_definition_pattern instruction with
  | Some definition -> Context.define_handler context { Definition_string.value = definition }
  | None -> follow_instruction context instruction

let evaluate (instructions : string list) : int list option =
  try
    let context = Context.create () in
    let final_context = List.fold_left evaluate_instruction context instructions in
    Some !(final_context.Context.stack)
  with _ ->
    None
