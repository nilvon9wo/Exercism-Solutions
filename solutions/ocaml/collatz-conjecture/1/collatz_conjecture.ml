open Base

let is_even number = number % 2 = 0

let next_number number =
  if is_even number
    then number / 2
    else 3 * number + 1

let rec step count number =
  if number = 1
  then count
  else step (count + 1) (next_number number)

let collatz_conjecture number =
  if number <= 0
  then Error "Only positive integers are allowed"
  else Ok (step 0 number)