open Base

type dominoe = (int * int)

let chain (input : dominoe list) : dominoe list option =
  let domino_values =
    List.map input ~f:(fun (l, r) -> Domino_value.create l r)
  in

  match domino_values with
  | [] ->
      (* Empty input = empty (trivially closed) chain *)
      Some []

  | _ ->
      let try_starting_domino (starting_domino : Domino_value.t) =
        let remaining =
          Remaining_dominoes.from_list domino_values
          |> fun r -> Remaining_dominoes.remove_one r starting_domino
        in
        match remaining with
        | None -> None
        | Some remaining_dominoes ->
            let initial_state =
              Chain_search_state.create_initial
                ~starting_domino
                ~remaining:remaining_dominoes
            in
            Domino_chain_builder.try_build_closed_chain initial_state
      in

      let rec try_all_starting_dominoes candidates =
        match candidates with
        | [] -> None
        | d :: rest ->
            match try_starting_domino d with
            | Some chain -> Some chain
            | None -> try_all_starting_dominoes rest
      in

      match try_all_starting_dominoes domino_values with
      | None -> None
      | Some chain ->
          Some
            (Domino_chain.to_list chain
             |> List.map ~f:(fun dv ->
                    (Domino_value.left dv, Domino_value.right dv)))
