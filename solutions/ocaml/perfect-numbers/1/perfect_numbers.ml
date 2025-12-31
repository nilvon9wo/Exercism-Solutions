open Base

(* ---- Helpers ---- *)

let integer_square_root number =
    number
    |> Int.to_float
    |> Float.sqrt
    |> Float.to_int

let is_divisor number candidate =
    Int.rem number candidate = 0

let get_divisors_up_to_limit number limit =
    let upper_bound = limit + 1 in
    upper_bound
    |> List.range 1
    |> List.filter ~f:(is_divisor number)

let expand_pair_for_divisor number divisor =
    let paired_divisor = number / divisor in
    if paired_divisor = divisor
        then [divisor]  (* perfect square *)
        else [divisor; paired_divisor]

let expand_divisor_pair number divisor =
    if divisor = 1
        then [1]
        else expand_pair_for_divisor number divisor

let is_not_self_divisor number divisor =
    divisor <> number

let get_proper_divisors number =
    number
    |> integer_square_root
    |> get_divisors_up_to_limit number
    |> List.concat_map ~f:(expand_divisor_pair number)
    |> List.filter ~f:(is_not_self_divisor number)

let proper_divisors number =
    if number <= 1
        then []
        else get_proper_divisors number

let sum_of_numbers numbers =
    List.fold numbers ~init:0 ~f:(+)

(* ---- Classification ---- *)

let classify_by_divisor_sum number =
    let divisors = proper_divisors number in
    let sum = sum_of_numbers divisors in
    if sum = number
        then Ok "perfect"
        else if sum > number
            then Ok "abundant"
            else Ok "deficient"

let classify number =
    if number <= 0
        then Error "Classification is only possible for positive integers."
        else classify_by_divisor_sum number
