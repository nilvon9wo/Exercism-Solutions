open Base

type planet =
  | Mercury
  | Venus
  | Earth
  | Mars
  | Jupiter
  | Saturn
  | Neptune
  | Uranus

let earth_year_in_seconds =
  31_557_600.0

let orbital_period_in_earth_years = function
  | Mercury -> 0.2408467
  | Venus   -> 0.61519726
  | Earth   -> 1.0
  | Mars    -> 1.8808158
  | Jupiter -> 11.862615
  | Saturn  -> 29.447498
  | Neptune -> 164.79132
  | Uranus  -> 84.016846

let seconds_to_earth_years seconds =
  Float.of_int seconds /. earth_year_in_seconds

let age_on planet seconds =
  let earth_years = seconds_to_earth_years seconds in
  let orbital_period = orbital_period_in_earth_years planet in
  earth_years /. orbital_period
