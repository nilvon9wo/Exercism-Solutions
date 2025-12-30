open Base

let is_letter character =
  Char.is_alpha character

let normalize character =
  Char.lowercase character

let extract_letters sentence =
  sentence
  |> String.to_list
  |> List.filter ~f:is_letter
  |> List.map ~f:normalize

let unique_letter_count characters =
  characters
  |> List.dedup_and_sort ~compare:Char.compare
  |> List.length

let is_pangram sentence =
  sentence
  |> extract_letters
  |> unique_letter_count
  |> Int.equal 26
