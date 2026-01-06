open Base

let can_connect_right ~(open_end : int) (domino : Domino_value.t) : bool =
    domino.left = open_end
        || domino.right  = open_end

let oriented_to_match_right ~(open_end : int) (domino : Domino_value.t) : Domino_value.t option =
    if domino.left = open_end
        then Some domino
        else if domino.right  = open_end
            then Some (Domino_value.flipped domino)
            else None
