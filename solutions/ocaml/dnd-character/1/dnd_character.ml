open Base

let ability () =
  3 + Random.int 16

let modifier ~score =
  let delta = score - 10 in
  if delta >= 0
    then delta / 2
    else (delta - 1) / 2

type character = {
  charisma : int;
  constitution : int;
  dexterity : int;
  hitpoints : int;
  intelligence : int;
  strength : int;
  wisdom : int;
}

let generate_character () =
  let constitution = ability () in
  {
    charisma = ability ();
    constitution = constitution;
    dexterity = ability ();
    hitpoints = 10 + modifier ~score:constitution;
    intelligence = ability ();
    strength = ability ();
    wisdom = ability ()
  }
