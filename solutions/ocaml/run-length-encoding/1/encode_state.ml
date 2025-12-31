open Base

type encode_state = {
  remaining_chars : char list;
  current_char    : char;
  run_length      : int;
  encoded_parts   : string list;
}

let create_encode_state string =
    match String.to_list string with
    | [] -> failwith "No string provided."
    | char :: rest ->
    {
         remaining_chars = rest;
         current_char = char;
         run_length = 1;
         encoded_parts = [];
    }

let char_to_string char =
  Char.to_string char

let string_of_count_and_char state =
  let char_as_string = char_to_string state.current_char in
  let count_as_string = Int.to_string state.run_length in
  count_as_string ^ char_as_string

let flush_encoded_run state =
  let count = state.run_length in
  let current_char = state.current_char in
  let encoded_parts = state.encoded_parts in
  if count = 1
      then char_to_string current_char :: encoded_parts
      else string_of_count_and_char state :: encoded_parts

let finalize_encoded_output state =
  flush_encoded_run state
  |> List.rev
  |> String.concat

let rec encode_loop (state : encode_state) =
  match state.remaining_chars with
  | [] ->
      finalize_encoded_output state
  | char :: rest when Char.equal char state.current_char ->
      encode_loop {
          state with
          remaining_chars = rest;
          run_length = state.run_length + 1;
      }
  | char :: rest ->
      encode_loop {
          remaining_chars = rest;
          current_char = char;
          run_length = 1;
          encoded_parts = flush_encoded_run state;
      }