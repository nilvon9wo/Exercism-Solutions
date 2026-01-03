open Base

let maximum_pins_per_frame : int = 10
let minimum_pins_per_roll : int = 0

let negative_roll_error_message : string = "Negative roll is invalid"
let too_many_pins_error_message : string = "Pin count exceeds pins on the lane"
let cannot_roll_after_game_over_message : string = "Cannot roll after game is over"

type t = unit  (* no internal state, just like Java *)

let create () : t = ()

(* Helpers that need to be defined early due to forward references *)

let are_first_two_rolls_a_spare (frame : Frame.frame) : bool =
  let total_pins_in_first_two_rolls = Frame.first_roll frame + Frame.second_roll frame in
  total_pins_in_first_two_rolls = maximum_pins_per_frame

let was_second_roll_a_strike (frame : Frame.frame) : bool =
  Frame.second_roll frame = maximum_pins_per_frame

let validate_second_roll_for_standard_frame (context : Roll_context.t) : unit =
  let current_frame : Frame.frame = Roll_context.get_frame context in
  if Frame.is_strike current_frame then
    ()
  else
    let total_pins_after_second_roll : int = Frame.first_roll current_frame + Roll_context.get_pins context in
    if total_pins_after_second_roll > maximum_pins_per_frame then
      failwith too_many_pins_error_message

let validate_third_roll_for_last_frame (context : Roll_context.t) : unit =
  let current_frame : Frame.frame = Roll_context.get_frame context in
  let first_roll_was_strike : bool = Frame.is_strike current_frame in
  let first_two_rolls_were_a_spare : bool = not first_roll_was_strike && are_first_two_rolls_a_spare current_frame in
  if not first_roll_was_strike && not first_two_rolls_were_a_spare then
    failwith too_many_pins_error_message

let validate_strike_bonus_roll_for_last_frame (context : Roll_context.t) : unit =
  let current_frame : Frame.frame = Roll_context.get_frame context in
  let is_third_roll : bool = Roll_context.get_roll_index context = 2 in
  if not is_third_roll || not (Frame.is_strike current_frame) || was_second_roll_a_strike current_frame then
    ()
  else
    let total_bonus_pins : int = Frame.second_roll current_frame + Roll_context.get_pins context in
    if total_bonus_pins > maximum_pins_per_frame then
      failwith too_many_pins_error_message

let is_second_roll_exceeding_maximum_pins (context : Roll_context.t) : bool =
  let current_frame : Frame.frame = Roll_context.get_frame context in
  let total_pins_after_second_roll : int = Frame.first_roll current_frame + Roll_context.get_pins context in
  total_pins_after_second_roll > maximum_pins_per_frame

let validate_standard_frame_rules (context : Roll_context.t) : unit =
  if Roll_context.get_roll_index context = 0 then
    ()
  else if is_second_roll_exceeding_maximum_pins context then
    failwith too_many_pins_error_message

let validate_last_frame_rules (context : Roll_context.t) : unit =
  let roll_position_index : int = Roll_context.get_roll_index context in
  if roll_position_index = 1 then
    validate_second_roll_for_standard_frame context
  else if roll_position_index = 2 then begin
    validate_third_roll_for_last_frame context;
    validate_strike_bonus_roll_for_last_frame context
  end

let validate_frame_rules (context : Roll_context.t) : unit =
  let current_frame : Frame.frame = Roll_context.get_frame context in
  if Frame.is_last_frame current_frame then
    validate_last_frame_rules context
  else
    validate_standard_frame_rules context

let validate_game_not_over_before_roll (context : Roll_context.t) : unit =
  let current_frame : Frame.frame = Roll_context.get_frame context in
  let is_game_over : bool = Frame.is_last_frame current_frame && Frame.is_complete current_frame in
  if is_game_over then failwith cannot_roll_after_game_over_message

let validate_roll_is_non_negative (context : Roll_context.t) : unit =
  if Roll_context.get_pins context < minimum_pins_per_roll then
    failwith negative_roll_error_message

let validate_roll_does_not_exceed_maximum (context : Roll_context.t) : unit =
  if Roll_context.get_pins context > maximum_pins_per_frame then
    failwith too_many_pins_error_message

(* Public method *)
let validate_roll (_self : t) (context : Roll_context.t) : unit =
  validate_game_not_over_before_roll context;
  validate_roll_is_non_negative context;
  validate_roll_does_not_exceed_maximum context;
  validate_frame_rules context
