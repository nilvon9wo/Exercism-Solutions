open Base

let maximum_number_of_frames : int = 10
let score_not_available_message : string = Score_calculator.score_not_available_message

type bowling_game = {
    frames : Frame.frame list ref;  
}

let create_bowling_game () : bowling_game = { 
    frames = ref [] 
}

let is_creating_last_frame (current_game : bowling_game) : bool =
    let current_frame_count : int = List.length !(current_game.frames) in
    current_frame_count + 1 = maximum_number_of_frames

let create_frame (current_game : bowling_game) (is_last_frame : bool) : Frame.frame =
    let new_frame : Frame.frame = Frame.create_frame is_last_frame in
    current_game.frames := !(current_game.frames) @ [new_frame];
    new_frame

let should_create_new_frame (current_game : bowling_game) (last_frame : Frame.frame) : bool =
    let last_frame_is_full : bool = not (Frame.can_accept_roll last_frame) in   
    let there_are_less_than_max_frames : bool = List.length !(current_game.frames) < maximum_number_of_frames in 
    last_frame_is_full && there_are_less_than_max_frames

let create_next_frame_if_needed (current_game : bowling_game) : Frame.frame = 
    let is_last_frame_to_create : bool = is_creating_last_frame current_game in
    create_frame current_game is_last_frame_to_create

let choose_current_frame_or_create_new (current_game : bowling_game) : Frame.frame =         
    let last_frame : Frame.frame = List.last_exn !(current_game.frames) in
    if should_create_new_frame current_game last_frame
        then create_next_frame_if_needed current_game
        else last_frame

let get_or_create_current_frame (current_game : bowling_game) : Frame.frame =
    if List.is_empty !(current_game.frames) 
        then create_frame current_game false
        else choose_current_frame_or_create_new current_game

let validate_game_is_complete (current_game : bowling_game) : unit =
    if List.length !(current_game.frames) < maximum_number_of_frames 
        then failwith score_not_available_message;

    let all_frames : Frame.frame list = !(current_game.frames) in
    let last_frame_index : int = maximum_number_of_frames - 1 in
    let last_frame : Frame.frame = List.nth_exn all_frames last_frame_index in
    if Frame.is_incomplete last_frame 
          then failwith score_not_available_message

let rebuild_frames_with_updated_frame (current_game : bowling_game) (updated_frame : Frame.frame) : Frame.frame list =
    match !(current_game.frames) with
    | [] -> [updated_frame]
    | existing_frames ->
        let index_of_last_frame : int = List.length existing_frames - 1 in 
        let all_frames_except_last : Frame.frame list = List.take existing_frames index_of_last_frame in
        all_frames_except_last @ [updated_frame]

(* Public functions *)

let roll (current_game : bowling_game) (pins_knocked_down : int) : bowling_game =
    let current_frame : Frame.frame = get_or_create_current_frame current_game in
    Roll_validator.validate_roll (Roll_context.create current_frame pins_knocked_down);
    let updated_frame : Frame.frame = Frame.add_roll current_frame pins_knocked_down in
    { 
        frames = ref (rebuild_frames_with_updated_frame current_game updated_frame) 
    }

let score (current_game : bowling_game) : int =
    validate_game_is_complete current_game;
    Score_calculator.calculate_score !(current_game.frames)
