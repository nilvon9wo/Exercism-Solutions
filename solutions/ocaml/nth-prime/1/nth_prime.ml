open Base

let estimate_sieve_upper_bound nth_prime_index =
  let nth_prime_as_float = Float.of_int nth_prime_index in
  let natural_log_of_nth = Float.log nth_prime_as_float in
  let natural_log_of_log_of_nth = Float.log natural_log_of_nth in

  let log_sum = natural_log_of_nth +. natural_log_of_log_of_nth in
  let estimated_float = nth_prime_as_float *. log_sum in
  let estimated_int = Float.to_int estimated_float in
  estimated_int + 1

let estimate_sieve_limit nth_prime_index =
  if nth_prime_index < 6
      then 15
      else estimate_sieve_upper_bound nth_prime_index

let create_true_array upper_limit =
  let length = upper_limit + 1 in
  Array.create ~len:length true

let initialize_sieve_array upper_limit =
  let sieve_array = create_true_array upper_limit in
  sieve_array.(0) <- false;
  sieve_array.(1) <- false;
  sieve_array

let compute_max_candidate_to_check upper_limit =
  upper_limit
  |> Float.of_int
  |> Float.sqrt
  |> Int.of_float

let mark_multiples_as_non_prime upper_limit sieve_array candidate_number =
  let rec mark_multiple multiple =
    if multiple <= upper_limit
    then (
      sieve_array.(multiple) <- false;
      mark_multiple (multiple + candidate_number)
    )
  in
  mark_multiple (candidate_number * candidate_number)

let mark_candidate_multiples_if_prime upper_limit sieve_array candidate_number =
  if sieve_array.(candidate_number)
    then mark_multiples_as_non_prime upper_limit sieve_array candidate_number

let get_next_odd_candidate candidate =
    let increment = if candidate = 2
        then 1
        else 2
    in
    candidate + increment

let apply_sieve_to_candidates upper_limit sieve_array =
  let max_candidate = compute_max_candidate_to_check upper_limit in
  let rec loop candidate =
    if candidate <= max_candidate
    then (
      mark_candidate_multiples_if_prime upper_limit sieve_array candidate;
      loop (get_next_odd_candidate candidate)
    )
  in
  loop 2;
  sieve_array

let create_sieve_array upper_limit =
  upper_limit
  |> initialize_sieve_array
  |> apply_sieve_to_candidates upper_limit

let convert_to_prime_option candidate_number is_prime =
  if is_prime
    then Some candidate_number
    else None

let sieve_array_to_prime_list sieve_array =
  sieve_array
  |> Array.mapi ~f:convert_to_prime_option
  |> Array.to_list
  |> List.filter_map ~f:Fn.id

let generate_prime_list nth_prime_index =
  nth_prime_index
  |> estimate_sieve_limit
  |> create_sieve_array
  |> sieve_array_to_prime_list

let compute_nth_prime nth_prime_index =
  let prime_numbers = generate_prime_list nth_prime_index in
  let index = nth_prime_index - 1 in
  match List.nth prime_numbers index with
  | Some prime_number -> Ok prime_number
  | None -> Error "unable to find the nth prime"

let nth_prime nth_prime_index =
  if nth_prime_index <= 0
      then Error "there is no zeroth prime"
      else compute_nth_prime nth_prime_index
