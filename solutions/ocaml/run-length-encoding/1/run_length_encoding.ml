open Base
open Decode_state
open Encode_state

let encode string =
  match String.to_list string with
  | [] -> ""
  | _ ->
      let initial_state = create_encode_state string in
      encode_loop initial_state

let decode string =
  let initial_state = create_decode_state string in
  decode_loop initial_state
