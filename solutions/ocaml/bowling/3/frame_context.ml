open Base

type t = {
  frames : Frame.frame list;
  current_frame_index : int;
}

let create (frames : Frame.frame list) (current_frame_index : int) : t =
  { frames; current_frame_index }

let get_current_frame (frame_context : t) : Frame.frame =
  match List.nth frame_context.frames frame_context.current_frame_index with
  | Some current_frame -> current_frame
  | None -> failwith "Invalid frame index"

let get_next_frame (frame_context : t) : Frame.frame option =
  List.nth frame_context.frames (frame_context.current_frame_index + 1)

let get_following_frame (frame_context : t) : Frame.frame option =
  List.nth frame_context.frames (frame_context.current_frame_index + 2)

let is_last_frame (frame_context : t) : bool =
  frame_context.current_frame_index = List.length frame_context.frames - 1
