open Base

let create_score_letter_pair score = fun character ->
    let lowercase_letter = Char.lowercase character in
    lowercase_letter, score

let score_letters_to_pairs (score, letters) =
  List.map letters ~f:(create_score_letter_pair score)

let sort_alphabetically = fun (character1, _) (character2, _) ->
    Char.compare character1 character2

let transform input =
  input
  |> List.concat_map ~f:score_letters_to_pairs
  |> List.sort ~compare:sort_alphabetically