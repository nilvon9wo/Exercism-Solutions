open Base

let letters_sorted_lowercase word =
  word
  |> String.lowercase
  |> String.to_list
  |> List.sort ~compare:Char.compare

let are_equal_case_insensitive word1 word2 =
  String.Caseless.equal word1 word2

let are_not_equal_case_insensitive word1 word2 =
  not (are_equal_case_insensitive word1 word2)

let are_letters_equal word1 word2 =
  let sorted1 = letters_sorted_lowercase word1 in
  let sorted2 = letters_sorted_lowercase word2 in
  Poly.equal sorted1 sorted2

let is_anagram subject candidate =
  let are_different_words = are_not_equal_case_insensitive subject candidate in
  let have_same_letters = are_letters_equal subject candidate in
  are_different_words && have_same_letters

let anagrams subject candidates =
  List.filter candidates ~f:(is_anagram subject)
