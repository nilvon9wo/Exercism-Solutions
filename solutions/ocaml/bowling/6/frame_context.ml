open Base

type t = {
  frames : Frame.frame list;
  current_frame_index : int;
}

let create (frames : Frame.frame list) (current_frame_index : int) : t =
  { frames; current_frame_index }

let get_frame_at_offset (frame_context : t) (offset : int) : Frame.frame option =
    List.nth frame_context.frames (frame_context.current_frame_index + offset)

let get_current_frame (frame_context : t) : Frame.frame =
  let current_frame_option = get_frame_at_offset frame_context 0 in
  match current_frame_option with
  | Some current_frame -> current_frame
  | None -> failwith "Invalid frame index"

let get_next_frame (frame_context : t) : Frame.frame option =
  get_frame_at_offset frame_context 1

let get_following_frame (frame_context : t) : Frame.frame option =
  get_frame_at_offset frame_context 2

let is_last_frame (frame_context : t) : bool =
  frame_context.current_frame_index = List.length frame_context.frames - 1
