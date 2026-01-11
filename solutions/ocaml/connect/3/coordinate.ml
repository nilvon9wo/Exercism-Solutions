open Base

(* Cube coordinates for hex grid *)
type t = {
  x : int;
  y : int;
  z : int;
}

let create ~x ~y ~z =
  { x; y; z }

(* All 6 directions in cube coordinates *)
let directions = [
  ( 1, -1,  0);
  ( 1,  0, -1);
  ( 0,  1, -1);
  (-1,  1,  0);
  (-1,  0,  1);
  ( 0, -1,  1);
]

let neighbors (coord : t) : t list =
  List.map directions ~f:(fun (dx, dy, dz) ->
    create ~x:(coord.x + dx) ~y:(coord.y + dy) ~z:(coord.z + dz)
  )

let equal (a : t) (b : t) : bool =
  a.x = b.x && a.y = b.y && a.z = b.z
