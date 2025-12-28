type nucleotide = A | C | G | T

let is_different nucleotide1 nucleotide2 =
  if nucleotide1 = nucleotide2
    then 0
    else 1

let rec compute_distance strand1 strand2 accumulated_distance =
  match strand1, strand2 with
  | [], [] ->
    Ok accumulated_distance
  | [], _ | _, [] ->
    Error "strands must be of equal length"
  | nucleotide1::tail1, nucleotide2::tail2 ->
      let distance_increment = is_different nucleotide1 nucleotide2 in
      let updated_distance = accumulated_distance + distance_increment in
      compute_distance tail1 tail2 updated_distance

let hamming_distance strand1 strand2 =
  compute_distance strand1 strand2 0