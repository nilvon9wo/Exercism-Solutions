type player = O | X

let connect (lines : string list) : player option =
  let board = Board.from_strings lines in
  match Connect_game.determine_winner board with
  | Some Player.X -> Some X
  | Some Player.O -> Some O
  | None -> None