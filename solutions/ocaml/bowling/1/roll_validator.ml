open Base

let max_pins : int = 10
let min_pins : int = 0

let negative_roll_message : string = "Negative roll is invalid"
let too_many_pins_message : string = "Pin count exceeds pins on the lane"
let cant_roll_after_over_message : string = "Cannot roll after game is over"

type t = unit  (* no internal state, just like Java *)

let create () : t = ()

(* Helpers that need to be defined early due to forward references *)

let is_first_two_rolls_spare (frame : Frame.frame) : bool =
  let total_pins_in_first_two_rolls = Frame.first_roll frame + Frame.second_roll frame in
  total_pins_in_first_two_rolls = max_pins

let was_second_roll_strike (frame : Frame.frame) : bool =
  Frame.second_roll frame = max_pins

let validate_second_roll_if_needed (context : Roll_context.t) : unit =
  let frame : Frame.frame = Roll_context.get_frame context in
  if Frame.is_strike frame then
    ()
  else
    let total_pins_after_second_roll : int = Frame.first_roll frame + Roll_context.get_pins context in
    if total_pins_after_second_roll > max_pins then
      failwith too_many_pins_message

let validate_third_roll_if_needed (context : Roll_context.t) : unit =
  let frame : Frame.frame = Roll_context.get_frame context in
  let first_roll_was_strike : bool = Frame.is_strike frame in
  let first_two_rolls_were_spare : bool = not first_roll_was_strike && is_first_two_rolls_spare frame in
  if not first_roll_was_strike && not first_two_rolls_were_spare then
    failwith too_many_pins_message

let validate_strike_bonus_if_needed (context : Roll_context.t) : unit =
  let frame : Frame.frame = Roll_context.get_frame context in
  let is_third_roll : bool = Roll_context.get_roll_index context = 2 in
  if not is_third_roll || not (Frame.is_strike frame) || was_second_roll_strike frame then
    ()
  else
    let bonus_pins_total : int = Frame.second_roll frame + Roll_context.get_pins context in
    if bonus_pins_total > max_pins then
      failwith too_many_pins_message

let is_second_roll_exceeding_max_pins (context : Roll_context.t) : bool =
  let frame : Frame.frame = Roll_context.get_frame context in
  let total_pins_after_second_roll : int = Frame.first_roll frame + Roll_context.get_pins context in
  total_pins_after_second_roll > max_pins

let validate_standard_frame_rules (context : Roll_context.t) : unit =
  if Roll_context.get_roll_index context = 0 then
    ()
  else if is_second_roll_exceeding_max_pins context then
    failwith too_many_pins_message

let validate_last_frame_rules (context : Roll_context.t) : unit =
  let roll_index : int = Roll_context.get_roll_index context in
  if roll_index = 1 then
    validate_second_roll_if_needed context
  else if roll_index = 2 then begin
    validate_third_roll_if_needed context;
    validate_strike_bonus_if_needed context
  end

let validate_frame_rules (context : Roll_context.t) : unit =
  let frame : Frame.frame = Roll_context.get_frame context in
  if Frame.is_last_frame frame then
    validate_last_frame_rules context
  else
    validate_standard_frame_rules context

let validate_game_not_over (context : Roll_context.t) : unit =
  let frame : Frame.frame = Roll_context.get_frame context in
  let is_game_over : bool = Frame.is_last_frame frame && Frame.is_complete frame in
  if is_game_over then failwith cant_roll_after_over_message

let validate_pins_non_negative (context : Roll_context.t) : unit =
  if Roll_context.get_pins context < min_pins then
    failwith negative_roll_message

let validate_pins_within_maximum (context : Roll_context.t) : unit =
  if Roll_context.get_pins context > max_pins then
    failwith too_many_pins_message

(* Public method *)
let validate_roll (_self : t) (context : Roll_context.t) : unit =
  validate_game_not_over context;
  validate_pins_non_negative context;
  validate_pins_within_maximum context;
  validate_frame_rules context
