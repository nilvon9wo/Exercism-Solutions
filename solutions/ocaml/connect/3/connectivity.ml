open Base

(* Check if a given coordinate belongs to the current player *)
let cell_belongs_to_player (context : Context.t) (coordinate : Coordinate.t) : bool =
  match Board.cell_at context.board coordinate with
  | Some that_player -> Player.equal that_player context.player
  | None -> false

(* Generate all starting coordinates for a player on the board *)
let start_edge_coordinates (context : Context.t) : Coordinate.t list =
  let board = context.board in
  match context.player with
  | Player.X ->
      (* X connects left to right: all coordinates in the leftmost column *)
      List.init (board.height) ~f:(fun y ->
          Board.coordinate_at_xy board ~y ~x:0)
      |> List.filter ~f:(cell_belongs_to_player context)
  | Player.O ->
      (* O connects top to bottom: all coordinates in the top row *)
      List.init (board.width) ~f:(fun x ->
          Board.coordinate_at_xy board ~y:0 ~x)
      |> List.filter ~f:(cell_belongs_to_player context)

(* Check if a coordinate is on the target edge *)
let is_target_edge (context : Context.t) (coordinate : Coordinate.t) : bool =
  let board = context.board in
  match context.player with
  | Player.X -> coordinate.x = board.width - 1
  | Player.O -> coordinate.y  = board.height - 1

(* Depth-first search along the player's pieces *)
let rec dfs (context : Context.t) ~(visited : Coordinate.t list) (coordinate : Coordinate.t) : bool =
  if List.exists visited ~f:(Coordinate.equal coordinate) then
    false
  else if is_target_edge context coordinate then
    true
  else
    let visited = coordinate :: visited in
    coordinate
    |> Coordinate.neighbors
    |> List.filter ~f:(Board.is_inside context.board)
    |> List.filter ~f:(cell_belongs_to_player context)
    |> List.exists ~f:(fun neighbor -> dfs context ~visited neighbor)

(* Check if a player has a winning connection *)
let has_connection ~(board : Board.t) ~(player : Player.t) : bool =
  let context = { Context.board; player } in
  start_edge_coordinates context
  |> List.exists ~f:(dfs context ~visited:[])