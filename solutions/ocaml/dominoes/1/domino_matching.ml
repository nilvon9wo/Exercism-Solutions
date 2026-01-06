open Base

let can_connect_right ~(open_end : int) (domino : Domino_value.t) : bool =
    Domino_value.left domino = open_end
     || Domino_value.right domino = open_end

let oriented_to_match_right ~(open_end : int) (domino : Domino_value.t) : Domino_value.t option =
    if Domino_value.left domino = open_end
        then Some domino
    else if Domino_value.right domino = open_end
        then Some (Domino_value.flipped domino)
        else None
