open Base

(* --- Helpers --- *)

let check_chain_closed ~(chain : Domino_chain.t) : Domino_chain.t option =
  match Domino_chain.first_value chain, Domino_chain.last_value chain with
  | Some first_value, Some last_value ->
      if first_value = last_value
        then Some chain
        else None
  | _ -> None

let try_candidate_with_state ~(state : Chain_search_state.t)
                             ~(candidate : Domino_value.t)
                             ~(recurse : Chain_search_state.t -> Domino_chain.t option)
                             : Domino_chain.t option =
    match Chain_search_state.advance state ~next_domino:candidate with
    | None -> None
    | Some new_state -> recurse new_state

let rec try_candidates_list ~(state : Chain_search_state.t)
                            ~(candidates : Domino_value.t list)
                            ~(recurse : Chain_search_state.t -> Domino_chain.t option)
                            : Domino_chain.t option =
    match candidates with
    | [] -> None
    | candidate :: rest ->
        match try_candidate_with_state ~state ~candidate ~recurse with
        | None -> try_candidates_list ~state ~candidates:rest ~recurse
        | Some chain -> Some chain

(* --- Main function --- *)

let rec try_build_closed_chain (state : Chain_search_state.t) : Domino_chain.t option =
    let remaining = state.remaining_dominoes in
    if Remaining_dominoes.is_empty remaining
        then check_chain_closed ~chain:state.chain_so_far
        else try_next_candidates state remaining

and try_next_candidates state remaining =
    let open_end = Chain_search_state.open_end state in
    let candidates = Remaining_dominoes.candidates_matching_right open_end remaining in
    try_candidates_list ~state ~candidates:candidates ~recurse:try_build_closed_chain
