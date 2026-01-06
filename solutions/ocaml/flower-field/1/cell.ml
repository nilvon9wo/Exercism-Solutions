open Base

type t =
    | Flower
    | Empty

let of_char (character : char) : t =
    if Char.equal character '*'
        then Flower
        else Empty

let to_char_with_count (cell : t) ~(count : int) : char =
    match cell with
    | Flower -> '*'
    | Empty ->
        if count = 0
            then ' '
            else Char.of_int_exn (Char.to_int '0' + count)
