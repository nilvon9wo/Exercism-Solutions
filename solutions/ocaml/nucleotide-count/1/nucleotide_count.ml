open Base

let empty = Map.empty (module Char)

let is_valid_nucleotide = function
  | 'A' | 'C' | 'G' | 'T' -> true
  | _ -> false

let count_matching_nucleotide target_nucleotide =
  fun current_count nucleotide ->
    if not (is_valid_nucleotide nucleotide) then
      Error nucleotide
    else if Char.equal nucleotide target_nucleotide then
      Ok (current_count + 1)
    else
      Ok current_count

let count_nucleotide dna nucleotide =
  if not (is_valid_nucleotide nucleotide) then
    Error nucleotide
  else
    dna
    |> String.to_list
    |> List.fold_result
         ~init:0
         ~f:(count_matching_nucleotide nucleotide)

let increment_count = function
  | None -> 1
  | Some count -> count + 1

let update_nucleotide_counts =
  fun counts nucleotide ->
    if not (is_valid_nucleotide nucleotide) then
      Error nucleotide
    else
      Ok (Map.update counts nucleotide ~f:increment_count)

let count_nucleotides dna =
  dna
  |> String.to_list
  |> List.fold_result
       ~init:empty
       ~f:update_nucleotide_counts
