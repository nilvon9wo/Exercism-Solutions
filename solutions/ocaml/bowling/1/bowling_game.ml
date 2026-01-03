open Base

let max_frames = 10

type bowling_game = {
  roll_validator : Roll_validator.t;
  score_calculator : Score_calculator.t;
  frames : Frame.frame list ref;  (* already correct *)
}

let create_bowling_game () : bowling_game =
  {
    roll_validator = Roll_validator.create ();
    score_calculator = Score_calculator.create ();
    frames = ref [];
  }

let score_not_available_message = Score_calculator.score_not_available_message

(* Helper functions defined first so no forward references occur *)

let is_creating_last_frame (game : bowling_game) : bool =
  (List.length !(game.frames)) + 1 = max_frames

let create_frame (game : bowling_game) (is_last_frame : bool) : Frame.frame =
  let frame : Frame.frame = Frame.create_frame is_last_frame in
  game.frames := !(game.frames) @ [frame];
  frame

let get_or_create_current_frame (game : bowling_game) : Frame.frame =
  if List.is_empty !(game.frames) then
    create_frame game false
  else
    let last_frame : Frame.frame = List.last_exn !(game.frames) in
    if not (Frame.can_accept_roll last_frame) && List.length !(game.frames) < max_frames then
      let is_last = is_creating_last_frame game in
      create_frame game is_last
    else
      last_frame

let validate_game_is_complete (game : bowling_game) : unit =
  if List.length !(game.frames) < max_frames then
    failwith score_not_available_message;

  let last_frame : Frame.frame = List.nth_exn !(game.frames) (max_frames - 1) in
  if Frame.is_incomplete last_frame then
    failwith score_not_available_message

(* Main public functions *)
let roll (game : bowling_game) (pins : int) : bowling_game =
  let current_frame : Frame.frame = get_or_create_current_frame game in
  Roll_validator.validate_roll game.roll_validator (Roll_context.create current_frame pins);
  let updated_frame : Frame.frame = Frame.add_roll current_frame pins in
  let updated_frames : Frame.frame list =
    match !(game.frames) with
    | [] -> [updated_frame]
    | frames ->
        let init_frames = List.take frames (List.length frames - 1) in
        init_frames @ [updated_frame]
  in
  { game with frames = ref updated_frames }


let score (game : bowling_game) : int =
  validate_game_is_complete game;
  Score_calculator.calculate_score game.score_calculator !(game.frames)
