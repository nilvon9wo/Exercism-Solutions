open Base

type t = {
  frames : Frame.frame list;
  frame_index : int;
}

let create (frames : Frame.frame list) (frame_index : int) : t =
  { frames; frame_index }

let get_current_frame (context : t) : Frame.frame =
  match List.nth context.frames context.frame_index with
  | Some frame -> frame
  | None -> failwith "Invalid frame index"

let get_next_frame (context : t) : Frame.frame option =
  List.nth context.frames (context.frame_index + 1)

let get_following_frame (context : t) : Frame.frame option =
  List.nth context.frames (context.frame_index + 2)

let is_last_frame (context : t) : bool =
  context.frame_index = List.length context.frames - 1
