open Base

let create_prime_flags limit =
  let is_prime_array = Array.create ~len:(limit + 1) true in
  is_prime_array.(0) <- false;
  is_prime_array.(1) <- false;
  is_prime_array

let mark_multiples is_prime_array prime limit =
  let rec mark multiple =
    if multiple <= limit then (
      is_prime_array.(multiple) <- false;
      mark (multiple + prime)
    )
  in
  mark (prime * 2)

let sieve_upper_bound limit =
  limit
  |> Float.of_int
  |> Float.sqrt
  |> Float.to_int

let convert_to_prime_option index is_prime =
  if is_prime
  then Some index
  else None

let collect_primes_from_flags is_prime_array =
  Array.to_list is_prime_array
  |> List.mapi ~f:convert_to_prime_option
  |> List.filter_opt

let mark_all_multiples is_prime_array limit =
  let max_candidate = sieve_upper_bound limit in
  for candidate = 2 to max_candidate do
    if is_prime_array.(candidate)
    then mark_multiples is_prime_array candidate limit
  done

let primes limit =
  if limit < 2
  then []
  else
    let is_prime_array = create_prime_flags limit in
    mark_all_multiples is_prime_array limit;
    collect_primes_from_flags is_prime_array
