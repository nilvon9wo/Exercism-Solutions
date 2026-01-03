open Base

let strike_pins_count = 10

type frame = {
    rolls : int list;
    is_final_frame : bool;
}

let create_frame (is_last_frame : bool) : frame =
    { rolls = []; is_final_frame = is_last_frame }

let add_roll (existing_frame : frame) (pins_knocked_down : int) : frame =
    { existing_frame with rolls = existing_frame.rolls @ [pins_knocked_down] }

let get_roll_or_zero (frame : frame) (roll_index : int) : int =
    if roll_index < List.length frame.rolls
        then List.nth_exn frame.rolls roll_index
        else 0

let first_roll (frame : frame) : int =
    get_roll_or_zero frame 0

let second_roll (frame : frame) : int =
    get_roll_or_zero frame 1

let is_strike (frame : frame) : bool =
    first_roll frame = strike_pins_count

let is_spare (frame : frame) : bool =
    let total_pins_knocked_down = first_roll frame + second_roll frame in
    if not (is_strike frame) && total_pins_knocked_down = strike_pins_count
        then true
        else false

let roll_count (frame : frame) : int =
    List.length frame.rolls

let needs_bonus_roll (frame : frame) : bool =
    if roll_count frame < 2
        then true  (* always need at least two rolls *)
        else if roll_count frame = 2
            then is_strike frame || is_spare frame  (* bonus roll allowed *)
            else false  (* third roll has already been taken *)

let is_incomplete (frame : frame) : bool =
    if not frame.is_final_frame
        then roll_count frame < 2 && not (is_strike frame)
        else needs_bonus_roll frame

let is_complete (frame : frame) : bool =
    not (is_incomplete frame)

let is_last_frame (frame : frame) : bool =
    frame.is_final_frame

let identity (value : int) : int =
    value

let pins_total (frame : frame) : int =
    List.sum (module Int) frame.rolls ~f:identity

let can_accept_roll (frame : frame) : bool =
    is_incomplete frame
