open Base

let maximum_pins_per_frame : int = 10
let minimum_pins_per_roll : int = 0

let negative_roll_error_message : string = "Negative roll is invalid"
let too_many_pins_error_message : string = "Pin count exceeds pins on the lane"
let cannot_roll_after_game_over_message : string = "Cannot roll after game is over"

let are_first_two_rolls_a_spare (frame : Frame.frame) : bool =
  let total_pins_in_first_two_rolls = Frame.first_roll frame + Frame.second_roll frame in
  total_pins_in_first_two_rolls = maximum_pins_per_frame

let was_second_roll_a_strike (frame : Frame.frame) : bool =
  Frame.second_roll frame = maximum_pins_per_frame

let validate_second_roll_for_standard_frame (context : Roll_context.t) : unit =
  let current_frame : Frame.frame = context.current_frame in
  if Frame.is_strike current_frame
      then ()
      else
        let total_pins_in_first_roll : int = Frame.first_roll current_frame in
        let total_pins_after_second_roll : int = total_pins_in_first_roll + context.pins_knocked_down in
        if total_pins_after_second_roll > maximum_pins_per_frame
            then failwith too_many_pins_error_message

let validate_third_roll_for_last_frame (context : Roll_context.t) : unit =
  let current_frame : Frame.frame = context.current_frame in
  let first_roll_was_strike : bool = Frame.is_strike current_frame in
  let first_roll_was_not_strike : bool = not first_roll_was_strike in
  let had_spare_in_first_two_rolls : bool = Frame.is_spare current_frame in
  let first_two_rolls_were_a_spare : bool = first_roll_was_not_strike && had_spare_in_first_two_rolls in
  if not first_roll_was_strike && not first_two_rolls_were_a_spare
      then failwith too_many_pins_error_message

let validate_strike_bonus_roll_for_last_frame (context : Roll_context.t) : unit =
  let current_frame : Frame.frame = context.current_frame in
  let is_third_roll : bool = context.roll_position_index = 2 in
  let is_strike_bonus_roll : bool = Frame.is_strike current_frame in
  let was_second_roll_a_strike : bool = was_second_roll_a_strike current_frame in
  if not is_third_roll || not is_strike_bonus_roll || was_second_roll_a_strike
      then ()
      else
        let total_bonus_pins : int = Frame.second_roll current_frame + context.pins_knocked_down in
        if total_bonus_pins > maximum_pins_per_frame
            then failwith too_many_pins_error_message

let is_second_roll_exceeding_maximum_pins (context : Roll_context.t) : bool =
  let current_frame : Frame.frame = context.current_frame in
  let total_pins_in_first_roll : int = Frame.first_roll current_frame in
  let total_pins_after_second_roll : int = total_pins_in_first_roll + context.pins_knocked_down in
  total_pins_after_second_roll > maximum_pins_per_frame

let validate_standard_frame_rules (context : Roll_context.t) : unit =
  if context.roll_position_index = 0
      then ()
      else if is_second_roll_exceeding_maximum_pins context
          then failwith too_many_pins_error_message

let validate_last_frame_rules (context : Roll_context.t) : unit =
  let roll_position_index : int = context.roll_position_index in
  if roll_position_index = 1
      then validate_second_roll_for_standard_frame context
      else if roll_position_index = 2
          then begin
            validate_third_roll_for_last_frame context;
            validate_strike_bonus_roll_for_last_frame context
          end

let validate_frame_rules (context : Roll_context.t) : unit =
  let current_frame : Frame.frame = context.current_frame in
  if Frame.is_last_frame current_frame
      then validate_last_frame_rules context
      else validate_standard_frame_rules context

let validate_game_not_over_before_roll (context : Roll_context.t) : unit =
  let current_frame : Frame.frame = context.current_frame in
  let is_last_frame : bool = Frame.is_last_frame current_frame in
  let is_complete : bool = Frame.is_complete current_frame in
  let is_game_over : bool = is_last_frame && is_complete in
  if is_game_over
      then failwith cannot_roll_after_game_over_message

let validate_roll_is_non_negative (context : Roll_context.t) : unit =
  if context.pins_knocked_down < minimum_pins_per_roll
      then failwith negative_roll_error_message

let validate_roll_does_not_exceed_maximum (context : Roll_context.t) : unit =
  if context.pins_knocked_down > maximum_pins_per_frame
      then failwith too_many_pins_error_message

(* Public method *)
let validate_roll (context : Roll_context.t) : unit =
  validate_game_not_over_before_roll context;
  validate_roll_is_non_negative context;
  validate_roll_does_not_exceed_maximum context;
  validate_frame_rules context
