type t = {
  instructions_by_word : (string, string) Hashtbl.t;
  stack : int list ref;
}

let create () =
  {
    instructions_by_word = Hashtbl.create 16;
    stack = ref [];
  }

let normalize_word (string : string) : string =
  String.lowercase_ascii string

let to_string (context : t) : string =
  !(context.stack)
  |> List.map string_of_int
  |> String.concat " "

let get_instruction (context : t) (word : string) : string option =
    word
    |> normalize_word
    |> Hashtbl.find_opt context.instructions_by_word

let find_instruction (context : t) word =
  match get_instruction context word with
  | Some v -> v
  | None -> word

let add_word_to_builder (context : t) builder word =
    let instruction = find_instruction context word in
    Buffer.add_string builder (instruction ^ " ")

let build_buffer_for_definition (context : t) (definition : string list) =
    let builder = Buffer.create 16 in
    let add_word = add_word_to_builder context builder in
    List.iter add_word definition;
    builder

let create_instruction (context : t) (definition : string list) : string =
    definition
    |> build_buffer_for_definition context
    |> Buffer.contents
    |> String.trim

let split_and_ref (definition_string : Definition_string.t) =
      definition_string.value
      |> String.split_on_char ' '
      |> List.filter (fun token -> token <> "")
      |> List_helpers.require_at_least_two_values
      |> ref

let ensure_not_number word =
        match int_of_string_opt word with
        | Some _ -> failwith "Redefining numbers not allowed."
        | None -> ()

let find_key definition =
    let word = List_helpers.shift definition in
    let _ = ensure_not_number word in
    normalize_word word

let define_handler (context : t) (definition_string : Definition_string.t) : t =
    let definition = split_and_ref definition_string in
    let key = find_key definition in
    let instruction = create_instruction context !definition in
    Hashtbl.replace context.instructions_by_word key instruction;
    context

let with_stack (context : t) (f : int list ref -> unit) : t =
  f context.stack;
  context
