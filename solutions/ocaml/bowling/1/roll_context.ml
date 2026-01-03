open Base
open Frame

type t = {
  frame : Frame.frame;
  pins : int;
  roll_index : int;
}

let create (frame : Frame.frame) (pins : int) : t =
  { frame; pins; roll_index = Frame.roll_count frame }

let get_frame (context : t) : Frame.frame = context.frame
let get_pins (context : t) : int = context.pins
let get_roll_index (context : t) : int = context.roll_index
