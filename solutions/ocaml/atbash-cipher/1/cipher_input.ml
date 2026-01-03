open Ascii_character

let extract_cipher_characters input =
  input
  |> String.to_seq
  |> Seq.map to_lowercase
  |> Seq.filter is_alphanumeric
  |> List.of_seq
