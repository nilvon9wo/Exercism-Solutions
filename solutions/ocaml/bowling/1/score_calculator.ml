open Base

let max_frames : int = 10
let score_not_available_message : string = "Score cannot be taken until the end of the game"

type t = {
  frame_scorer : Frame_scorer.t;
}

let create () : t =
  { frame_scorer = Frame_scorer.create () }

let validate_game_is_complete (frames : Frame.frame list) : unit =
  let no_frames : bool = List.is_empty frames in
  let less_than_ten_frames : bool = List.length frames < max_frames in
  if no_frames || less_than_ten_frames then
    failwith score_not_available_message

let calculate_score (self : t) (frames : Frame.frame list) : int =
  validate_game_is_complete frames;
  List.init max_frames ~f:(fun index : int ->
      Frame_scorer.score_frame self.frame_scorer (Frame_context.create frames index)
    )
  |> List.sum (module Int) ~f:Fn.id
