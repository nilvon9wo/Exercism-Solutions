open Base

type t = Domino_value.t list

let empty : t =
    []

let append (chain : t) (domino : Domino_value.t) : t =
    chain @ [domino]

let first_value (chain : t) : int option =
    match chain with
    | [] -> None
    | first_domino :: _ ->
        Some (Domino_value.left first_domino)

let last_value (chain : t) : int option =
    match List.last chain with
    | None -> None
    | Some last_domino ->
        Some (Domino_value.right last_domino)

let to_list (chain : t) : Domino_value.t list =
    chain
