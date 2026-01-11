open Base

let determine_winner (board : Board.t) : Player.t option =
  if Connectivity.has_connection ~board ~player:Player.X
    then Some Player.X
    else if Connectivity.has_connection ~board ~player:Player.O
        then Some Player.O
        else None