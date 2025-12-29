open Base
open Raindrop_rule

let rules = [
  { factor = 3; sound = "Pling" };
  { factor = 5; sound = "Plang" };
  { factor = 7; sound = "Plong" };
]

let sounds_for_factor number rule =
  if number % rule.factor = 0
    then [rule.sound]
    else []

let format_raindrop_output number sounds =
    match sounds with
      | [] -> Int.to_string number
      | sounds -> String.concat sounds

let raindrop number =
  rules
  |> List.concat_map ~f:(sounds_for_factor number)
  |> format_raindrop_output number