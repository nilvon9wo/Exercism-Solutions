open Base

type t = {
  x : int;
  y : int;
  z : int;
}

let create ~x ~y ~z =
    {
        x;
        y;
        z
    }

let directions = [
  ( 1, -1,  0);
  ( 1,  0, -1);
  ( 0,  1, -1);
  (-1,  1,  0);
  (-1,  0,  1);
  ( 0, -1,  1);
]

let apply_cube_offset coordinate (dx, dy, dz) =
    let x = coordinate.x + dx in
    let y = coordinate.y + dy in
    let z = coordinate.z + dz in
    create ~x ~y ~z

let neighbors (coordinate : t) : t list =
  List.map directions ~f:(apply_cube_offset coordinate)

let equal (a : t) (b : t) : bool =
  a.x = b.x
    && a.y = b.y
    && a.z = b.z
