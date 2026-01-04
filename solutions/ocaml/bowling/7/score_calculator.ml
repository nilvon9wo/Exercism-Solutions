open Base

let maximum_number_of_frames : int = 10
let score_not_available_message : string = "Score cannot be taken until the end of the game"

let validate_game_has_all_frames (frames : Frame.frame list) : unit =
    let no_frames_available : bool = List.is_empty frames in
    let frame_count : int = List.length frames in
    let less_than_maximum_frames : bool = frame_count < maximum_number_of_frames in
    if no_frames_available || less_than_maximum_frames
        then failwith score_not_available_message

let score_frame_at_index  frames frame_index : int = 
    Frame_scorer.score_frame (Frame_context.create frames frame_index)

let calculate_score (frames : Frame.frame list) : int =
    validate_game_has_all_frames frames;
    maximum_number_of_frames
    |> List.init ~f:(score_frame_at_index frames)
    |> List.sum (module Int) ~f:Fn.id