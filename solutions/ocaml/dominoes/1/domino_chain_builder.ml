open Base

let rec try_build_closed_chain (state : Chain_search_state.t) : Domino_chain.t option =
    let remaining = Chain_search_state.remaining_dominoes state in
    if Remaining_dominoes.is_empty remaining
    then
          (* All dominoes placed, check if chain is closed *)
          match Domino_chain.first_value (Chain_search_state.chain_so_far state),
                Domino_chain.last_value (Chain_search_state.chain_so_far state) with
          | Some first_value, Some last_value ->
              if first_value = last_value
                    then Some (Chain_search_state.chain_so_far state)
                    else None
          | _ -> None
    else
      let current_open_end = Chain_search_state.open_end state in
      let candidates = Remaining_dominoes.candidates_matching_right current_open_end remaining in

      let rec try_candidates candidates_list =
        match candidates_list with
        | [] -> None
        | candidate :: rest ->
            match Chain_search_state.advance state ~next_domino:candidate with
            | None -> try_candidates rest
            | Some new_state ->
                match try_build_closed_chain new_state with
                | Some completed_chain -> Some completed_chain
                | None -> try_candidates rest
      in
      try_candidates candidates
