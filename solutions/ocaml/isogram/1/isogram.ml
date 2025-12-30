open Base

let is_letter character =
  Char.is_alpha character

let normalize character =
  Char.lowercase character

let extract_letters word =
  word
  |> String.to_list
  |> List.filter ~f:is_letter
  |> List.map ~f:normalize

let has_duplicates characters =
  characters
  |> List.sort ~compare:Char.compare
  |> List.find_consecutive_duplicate ~equal:Char.equal
  |> Option.is_some

let is_isogram (word : string) : bool =
  word
  |> extract_letters
  |> has_duplicates
  |> not
