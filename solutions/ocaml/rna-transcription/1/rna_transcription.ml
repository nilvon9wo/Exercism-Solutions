open Base

type dna = [ `A | `C | `G | `T ]
type rna = [ `A | `C | `G | `U ]

let transcribe : dna -> rna = function
    | `A -> `U
    | `C -> `G
    | `G -> `C
    | `T -> `A

let to_rna (strand : dna list) : rna list =
  List.map strand ~f:transcribe