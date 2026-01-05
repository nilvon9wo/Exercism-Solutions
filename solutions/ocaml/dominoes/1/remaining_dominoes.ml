open Base

type t = {
    dominoes : Domino_value.t list;
}

let from_list (dominoes : Domino_value.t list) : t = {
    dominoes
}

let is_empty (remaining : t) : bool =
    List.is_empty remaining.dominoes

let domino_values_are_equal (left_domino : Domino_value.t) (right_domino : Domino_value.t) : bool =
    let left_left : int = Domino_value.left left_domino in
    let left_right : int = Domino_value.right left_domino in
    let right_left : int = Domino_value.left right_domino in
    let right_right : int = Domino_value.right right_domino in
    (left_left = right_left && left_right = right_right)
        || (left_left = right_right && left_right = right_left)

let remove_first_matching_domino (dominoes : Domino_value.t list) (domino_to_remove : Domino_value.t) : Domino_value.t list option =
    let rec remove (remaining : Domino_value.t list) (accumulator : Domino_value.t list) : Domino_value.t list option =
      match remaining with
      | [] ->
          None
      | head :: tail ->
          if domino_values_are_equal head domino_to_remove
            then Some (List.rev_append accumulator tail)
            else remove tail (head :: accumulator)
    in
    remove dominoes []

let remove_one (remaining : t) (domino : Domino_value.t) : t option =
    match remove_first_matching_domino remaining.dominoes domino with
    | None -> None
    | Some updated_dominoes -> Some { dominoes = updated_dominoes }

let candidate_oriented_to_match_right (open_end : int) (domino : Domino_value.t) : Domino_value.t option =
    Domino_matching.oriented_to_match_right ~open_end domino

let candidates_matching_right (open_end : int) (remaining : t) : Domino_value.t list =
    let rec collect (dominoes : Domino_value.t list) (accumulator : Domino_value.t list) : Domino_value.t list =
      match dominoes with
      | [] ->
          List.rev accumulator
      | head :: tail ->
          match candidate_oriented_to_match_right open_end head with
          | None ->
              collect tail accumulator
          | Some oriented_domino ->
              collect tail (oriented_domino :: accumulator)
    in
    collect remaining.dominoes []
