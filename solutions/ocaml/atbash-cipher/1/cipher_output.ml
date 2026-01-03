let default_output_block_size =
  5

let insert_output_block_separators block_size characters =
  let rec build remaining characters_in_current_block accumulator =
    match remaining with
    | [] ->
        List.rev accumulator
    | character :: rest ->
        if characters_in_current_block = block_size
            then build rest 1 (character :: ' ' :: accumulator)
            else build rest (characters_in_current_block + 1) (character :: accumulator)
  in
  build characters 0 []

let characters_to_string characters =
  characters
  |> List.to_seq
  |> String.of_seq
