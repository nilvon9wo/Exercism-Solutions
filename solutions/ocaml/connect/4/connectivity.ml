open Base

let does_cell_belongs_to_player (context : Context.t) (coordinate : Coordinate.t) : bool =
  match Board.cell_at context.board coordinate with
  | Some cell_player ->
        Player.equal cell_player context.player
  | None ->
        false

let filter_edge_coordinates context edge_size ~f =
      edge_size
      |> List.init ~f
      |> List.filter ~f:(does_cell_belongs_to_player context)

(* Generate all starting coordinates for a player on the board *)
let start_edge_coordinates (context : Context.t) : Coordinate.t list =
  let board = context.board in
  let edge_coordinates_for_player = filter_edge_coordinates context in
  match context.player with
  | Player.X ->
      edge_coordinates_for_player board.height ~f:(fun y -> Board.coordinate_at_xy board ~y ~x:0)
  | Player.O ->
      edge_coordinates_for_player board.width ~f:(fun x -> Board.coordinate_at_xy board ~y:0 ~x)

(* Check if a coordinate is on the target edge *)
let is_target_edge (context : Context.t) (coordinate : Coordinate.t) : bool =
  let board = context.board in
  match context.player with
  | Player.X -> coordinate.x = board.width - 1
  | Player.O -> coordinate.y  = board.height - 1

(* Depth-first search along the player's pieces *)
let rec dfs (context : Context.t) ~(visited : Coordinate.t list) (coordinate : Coordinate.t) : bool =
  if List.exists visited ~f:(Coordinate.equal coordinate)
        then false
        else if is_target_edge context coordinate
            then true
            else explore_neighbors context ~visited coordinate

and explore_neighbors (context : Context.t) ~(visited : Coordinate.t list) (coordinate : Coordinate.t) : bool =
    let visited = coordinate :: visited in
    coordinate
    |> Coordinate.neighbors
    |> List.filter ~f:(Board.is_inside context.board)
    |> List.filter ~f:(does_cell_belongs_to_player context)
    |> List.exists ~f:(fun neighbor -> dfs context ~visited neighbor)

(* Check if a player has a winning connection *)
let has_connection ~(board : Board.t) ~(player : Player.t) : bool =
  let context = { Context.board; player } in
  start_edge_coordinates context
  |> List.exists ~f:(dfs context ~visited:[])