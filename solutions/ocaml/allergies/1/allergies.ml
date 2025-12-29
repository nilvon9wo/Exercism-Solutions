open Base

type allergen =
  | Eggs
  | Peanuts
  | Shellfish
  | Strawberries
  | Tomatoes
  | Chocolate
  | Pollen
  | Cats

let allergen_scores =
  [ Eggs, 1
  ; Peanuts, 2
  ; Shellfish, 4
  ; Strawberries, 8
  ; Tomatoes, 16
  ; Chocolate, 32
  ; Pollen, 64
  ; Cats, 128
  ]

let get_bit_for_allergen allergen =
  allergen
  |> List.Assoc.find allergen_scores ~equal:Poly.equal

let is_bit_set_in_score score bit =
    let masked_bits = score land bit in
    masked_bits <> 0

(* Returns true if the score includes the given allergen *)
let allergic_to score allergen =
    allergen
    |> get_bit_for_allergen
    |> Option.value_map
        ~default:false
        ~f:(is_bit_set_in_score score)

(* Convert a (allergen, bit) pair into Some allergen if present in score *)
let allergen_if_present_in_score score (allergen, bit) =
  let is_present = is_bit_set_in_score score bit in
  Option.some_if is_present allergen

let allergies score =
  allergen_scores
  |> List.filter_map ~f:(allergen_if_present_in_score score)
