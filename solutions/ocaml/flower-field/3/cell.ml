open Base

type t =
    | Flower
    | Empty

let zero_char_code = Char.to_int '0'

let to_char_with_count (cell : t) ~(count : int) : char =
    match cell with
    | Flower -> '*'
    | Empty ->
        if count = 0
            then ' '
            else Char.of_int_exn (zero_char_code + count)
