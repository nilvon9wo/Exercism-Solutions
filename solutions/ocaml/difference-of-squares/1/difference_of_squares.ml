(* Square of the sum of the first n natural numbers *)
let square_of_sum n =
  let sum = n * (n + 1) / 2 in
  sum * sum

(* Sum of the squares of the first n natural numbers *)
let sum_of_squares n =
  n * (n + 1) * (2 * n + 1) / 6

(* Difference between square_of_sum and sum_of_squares *)
let difference_of_squares n =
  square_of_sum n - sum_of_squares n
