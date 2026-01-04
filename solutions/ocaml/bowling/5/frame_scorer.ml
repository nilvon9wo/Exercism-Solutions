open Base

type t = unit

let create () : t = ()

let strike_pins : int = 10

let get_roll_or_zero (rolls : int list) (index : int) : int =
  match List.nth rolls index with
  | Some roll_value -> roll_value
  | None -> 0

let sum_first_n_rolls (rolls : int list) (count : int) : int =
    count
    |> List.init ~f:(fun roll_index -> get_roll_or_zero rolls roll_index)
    |> List.sum (module Int) ~f:Fn.id

let sum_first_rolls (first_frame : Frame.frame) (second_frame : Frame.frame) : int =
  sum_first_n_rolls [Frame.first_roll first_frame; Frame.first_roll second_frame] 2

let sum_first_two_rolls (rolls : int list) : int =
  sum_first_n_rolls rolls 2

let get_next_frame context
    = Frame_context.get_next_frame context

let calculate_spare_bonus (context : Frame_context.t) : int =
  match get_next_frame context with
  | Some next_frame -> Frame.first_roll next_frame
  | None -> 0

let calculate_strike_bonus (context : Frame_context.t) : int =
  match get_next_frame context with
  | None -> 0
  | Some next_frame ->
      let next_frame_is_strike = Frame.is_strike next_frame in
      match Frame_context.get_following_frame context with
      | Some following_frame when next_frame_is_strike ->
          sum_first_rolls next_frame following_frame
      | _ ->
          sum_first_n_rolls next_frame.rolls 2

let get_roll_at_index_or_zero (rolls_list : int list) (roll_index : int) : int =
    let roll_option = List.nth rolls_list roll_index in
    match roll_option with
    | Some roll_value -> roll_value
    | None -> 0

let score_last_frame_spare (frame : Frame.frame) : int =
  (* The third roll is the bonus for spare in the last frame *)
  let rolls_list : int list = frame.rolls in
  let bonus_roll : int = get_roll_at_index_or_zero rolls_list 2 in
  strike_pins + bonus_roll

let score_last_frame_strike (frame : Frame.frame) : int =
  (* Last frame strike can have up to two bonus rolls *)
  let rolls_list : int list = frame.rolls in
  let second_roll : int = get_roll_at_index_or_zero rolls_list 1 in
  let third_roll : int = get_roll_at_index_or_zero rolls_list 2 in
  strike_pins + second_roll + third_roll

let score_strike (_ : Frame.frame) (context : Frame_context.t) : int =
  let bonus : int = calculate_strike_bonus context in
  strike_pins + bonus

let score_spare (_ : Frame.frame) (context : Frame_context.t) : int =
  let bonus : int = calculate_spare_bonus context in
  strike_pins + bonus

let score_frame (_self : t) (context : Frame_context.t) : int =
  let frame : Frame.frame = Frame_context.get_current_frame context in
  let is_last_frame : bool = Frame_context.is_last_frame context in
  if Frame.is_strike frame
      then if is_last_frame
          then score_last_frame_strike frame
          else score_strike frame context
      else if Frame.is_spare frame
          then if is_last_frame
              then score_last_frame_spare frame
              else score_spare frame context
          else Frame.pins_total frame
