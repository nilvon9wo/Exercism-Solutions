open Cipher_input
open Cipher_mapping
open Cipher_output

let determine_block_size block_size =
    match block_size with
    | Some size -> size
    | None -> default_output_block_size

let get_encipher_characters plaintext output_block_size =
    plaintext
    |> extract_cipher_characters
    |> List.map substitute_letter_using_atbash_cipher
    |> insert_output_block_separators output_block_size

let encode ?block_size plaintext =
  let output_block_size = determine_block_size block_size in
  let encoded_characters = get_encipher_characters plaintext output_block_size in
  characters_to_string encoded_characters

let get_decipher_characters ciphertext =
        ciphertext
        |> String.to_seq
        |> Seq.filter (fun character -> character <> ' ')
        |> List.of_seq

let decode ciphertext =
  let cipher_characters = get_decipher_characters ciphertext in
  let decoded_characters = List.map substitute_letter_using_atbash_cipher cipher_characters in
  characters_to_string decoded_characters
