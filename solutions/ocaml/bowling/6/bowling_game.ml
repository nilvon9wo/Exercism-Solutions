open Base

let maximum_number_of_frames = 10

type bowling_game = {
  frames : Frame.frame list ref;  (* already correct *)
}

let create_bowling_game () : bowling_game =
  {
    frames = ref [];
  }

let score_not_available_message = Score_calculator.score_not_available_message

(* Helper functions defined first so no forward references occur *)

let is_creating_last_frame (bowling_game_state : bowling_game) : bool =
  (List.length !(bowling_game_state.frames)) + 1 = maximum_number_of_frames

let create_frame (bowling_game_state : bowling_game) (is_last_frame : bool) : Frame.frame =
  let created_frame : Frame.frame = Frame.create_frame is_last_frame in
  bowling_game_state.frames := !(bowling_game_state.frames) @ [created_frame];
  created_frame

let get_or_create_current_frame (bowling_game_state : bowling_game) : Frame.frame =
  if List.is_empty !(bowling_game_state.frames) then
    create_frame bowling_game_state false
  else
    let last_frame : Frame.frame = List.last_exn !(bowling_game_state.frames) in
    if not (Frame.can_accept_roll last_frame) && List.length !(bowling_game_state.frames) < maximum_number_of_frames then
      let is_last_frame_to_create = is_creating_last_frame bowling_game_state in
      create_frame bowling_game_state is_last_frame_to_create
    else
      last_frame

let validate_game_is_complete (bowling_game_state : bowling_game) : unit =
  if List.length !(bowling_game_state.frames) < maximum_number_of_frames then
    failwith score_not_available_message;

  let last_frame : Frame.frame = List.nth_exn !(bowling_game_state.frames) (maximum_number_of_frames - 1) in
  if Frame.is_incomplete last_frame then
    failwith score_not_available_message

(* Main public functions *)
let roll (bowling_game_state : bowling_game) (pins_knocked_down : int) : bowling_game =
  let current_frame : Frame.frame = get_or_create_current_frame bowling_game_state in
  Roll_validator.validate_roll (Roll_context.create current_frame pins_knocked_down);
  let updated_frame : Frame.frame = Frame.add_roll current_frame pins_knocked_down in
  let updated_frames : Frame.frame list =
    match !(bowling_game_state.frames) with
    | [] -> [updated_frame]
    | existing_frames ->
        let initial_frames_except_last = List.take existing_frames (List.length existing_frames - 1) in
        initial_frames_except_last @ [updated_frame]
  in
  { frames = ref updated_frames }


let score (bowling_game_state : bowling_game) : int =
  validate_game_is_complete bowling_game_state;
  Score_calculator.calculate_score !(bowling_game_state.frames)
