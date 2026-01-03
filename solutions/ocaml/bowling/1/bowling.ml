open Base

type t = Bowling_game.bowling_game

let new_game : t = Bowling_game.create_bowling_game ()

let roll (pins : int) (game : t) : (t, string) Result.t =
  try
    let updated_game = Bowling_game.roll game pins in
    Ok updated_game
  with
  | Failure msg -> Error msg

let score (game : t) : (int, string) Result.t =
  try
    Ok (Bowling_game.score game)
  with
  | Failure msg -> Error msg
