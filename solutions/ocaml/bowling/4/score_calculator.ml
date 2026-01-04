open Base

let maximum_number_of_frames : int = 10
let score_not_available_message : string = "Score cannot be taken until the end of the game"

type t = {
  frame_score_calculator : Frame_scorer.t;
}

let create () : t =
  { frame_score_calculator = Frame_scorer.create () }

let validate_game_has_all_frames (frames : Frame.frame list) : unit =
  let no_frames_available : bool = List.is_empty frames in
  let less_than_maximum_frames : bool = List.length frames < maximum_number_of_frames in
  if no_frames_available || less_than_maximum_frames then
    failwith score_not_available_message

let calculate_score (self : t) (frames : Frame.frame list) : int =
  validate_game_has_all_frames frames;
  List.init maximum_number_of_frames ~f:(fun frame_index : int ->
      Frame_scorer.score_frame self.frame_score_calculator (Frame_context.create frames frame_index)
    )
  |> List.sum (module Int) ~f:Fn.id
