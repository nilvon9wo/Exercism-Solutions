open Base

type t = Bowling_game.bowling_game

let new_game : t = Bowling_game.create_bowling_game ()

let roll (pins_knocked_down : int) (current_game : t) : (t, string) Result.t =
  try
    let updated_game_state = Bowling_game.roll current_game pins_knocked_down in
    Ok updated_game_state
  with
  | Failure error_message -> Error error_message

let score (completed_game : t) : (int, string) Result.t =
  try
    Ok (Bowling_game.score completed_game)
  with
  | Failure error_message -> Error error_message
