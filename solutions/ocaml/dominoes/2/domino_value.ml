open Base

type t = {
    left : int;
    right : int;
}

let create ~(left : int) ~(right : int) : t =
    {
        left;
        right;
    }

let flipped (domino : t) : t =
    {
        left = domino.right;
        right = domino.left;
    }

let of_tuple ((left, right) : int * int) : t =
    create ~left ~right

let to_tuple (domino : t) : int * int =
    (domino.left, domino.right)

