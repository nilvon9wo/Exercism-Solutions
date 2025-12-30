open Base

(* ---- Helpers ---- *)

let openers_by_closers =
  Map.of_alist_exn (module Char)
    [
      (')', '(');
      (']', '[');
      ('}', '{')
    ]

let openers =
  Set.of_list (module Char) (Map.data openers_by_closers)

let is_opener character =
    Set.mem openers character

let find_expected_opener_for_closer character =
    Map.find openers_by_closers character

(* ---- Stack manipulation ---- *)

let pop_if_matches_closer stack opener =
  match stack with
  | top :: tail when Char.equal top opener ->
    Some tail
  | _ ->
    None

(* ---- Core recursive logic ---- *)

let handle_closing_or_ignore stack character =
    match find_expected_opener_for_closer character with
    | Some opener ->
        pop_if_matches_closer stack opener
    | None ->
        Some stack

let push_or_match_closer stack character =
  if is_opener character
  then Some (character :: stack)
  else handle_closing_or_ignore stack character

let rec check_balanced stack chars =
  match chars with
  | [] -> List.is_empty stack
  | character :: rest ->
      match push_or_match_closer stack character with
      | Some new_stack -> check_balanced new_stack rest
      | None -> false

(* ---- Main entry ---- *)

let are_balanced string =
    string
    |> String.to_list
    |> check_balanced []
