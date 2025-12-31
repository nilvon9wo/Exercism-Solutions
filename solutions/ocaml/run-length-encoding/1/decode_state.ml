open Base

type decode_state = {
  remaining_chars : char list;
  current_count   : int;
  decoded_parts   : string list;
}

let create_decode_state string = {
     remaining_chars = String.to_list string;
     current_count = 0;
     decoded_parts = [];
}

let is_digit char =
  Char.is_digit char

let digit_value char =
  Char.get_digit_exn char

let accumulate_count count digit =
  (count * 10) + digit

let repeat_char char count =
  String.make count char

let decode_repetition_count count =
  if count = 0
      then 1
      else count

let flush_decoded_run state char =
  let repetitions = decode_repetition_count state.current_count in
  let expanded_run = repeat_char char repetitions in
  expanded_run :: state.decoded_parts

let finalize_decoded_output decoded_parts =
  decoded_parts
  |> List.rev
  |> String.concat

let rec decode_loop (state : decode_state) =
  match state.remaining_chars with
  | [] ->
      finalize_decoded_output state.decoded_parts
  | char :: rest when is_digit char ->
      let digit = digit_value char in
      decode_loop {
          state with
          remaining_chars = rest;
          current_count = accumulate_count state.current_count digit;
      }
  | char :: rest ->
      decode_loop {
          remaining_chars = rest;
          current_count = 0;
          decoded_parts = flush_decoded_run state char;
      }