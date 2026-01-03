open Base

type t = {
  current_frame : Frame.frame;
  pins_knocked_down : int;
  roll_position_index : int;
}

let create (current_frame : Frame.frame) (pins_knocked_down : int) : t = {
    current_frame;
    pins_knocked_down;
    roll_position_index = Frame.roll_count current_frame
}
