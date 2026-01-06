open Base

type t = {
    chain_so_far : Domino_chain.t;
    remaining_dominoes : Remaining_dominoes.t;
}

let create_initial ~(starting_domino : Domino_value.t) ~(remaining : Remaining_dominoes.t) : t =
    let initial_chain = Domino_chain.append Domino_chain.empty starting_domino in
    let remaining_after_first =
      match Remaining_dominoes.remove_one remaining starting_domino with
      | Some r -> r
      | None -> remaining
    in
    {
        chain_so_far = initial_chain;
        remaining_dominoes = remaining_after_first
    }

let open_end (state : t) : int =
    match Domino_chain.last_value state.chain_so_far with
    | Some value -> value
    | None -> failwith "Chain is empty; cannot determine open end"

let remaining_dominoes (state : t) : Remaining_dominoes.t =
    state.remaining_dominoes

let chain_so_far (state : t) : Domino_chain.t =
    state.chain_so_far

let advance (state : t) ~(next_domino : Domino_value.t) : t option =
    let open_end_value : int = open_end state in
    match Domino_matching.oriented_to_match_right ~open_end:open_end_value next_domino with
    | None -> None
    | Some oriented_domino ->
        let updated_chain = Domino_chain.append state.chain_so_far oriented_domino in
        match Remaining_dominoes.remove_one state.remaining_dominoes next_domino with
        | None -> None
        | Some updated_remaining ->
            Some {
                chain_so_far = updated_chain;
                remaining_dominoes = updated_remaining
            }
