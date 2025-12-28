open Zone  (* optional, or use Zone.t everywhere *)

(* Define all zones in increasing radius order *)
let scoring_zones = [
  { radius = 1.0;  score = 10; name = "inner" };
  { radius = 5.0;  score = 5;  name = "middle" };
  { radius = 10.0; score = 1;  name = "outer" };
]

(* Helper: compute Euclidean distance *)
let euclidean_distance x y = Float.sqrt (x *. x +. y *. y)

(* Check if a distance is inside a zone *)
let distance_in_zone distance zone =
  distance <= zone.radius

(* Helper: find the zone a distance falls into *)
let zone_for_distance distance =
  scoring_zones
  |> List.find_opt (distance_in_zone distance)

(* Map distance to score *)
let score_of_distance distance =
  match zone_for_distance distance with
  | Some zone -> zone.score
  | None -> 0  (* missed the board *)

(* Main function *)
let score x y =
  x
  |> euclidean_distance y
  |> score_of_distance
