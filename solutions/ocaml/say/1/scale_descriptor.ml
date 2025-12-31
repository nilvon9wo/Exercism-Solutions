open Base
open Int64
open Say_joiner
open Scale_result
open Spell_chunk
open Spelling_state

type scale_descriptor = {
  scale: Int64.t;
  label: string;
}

let large_scales = [
  { scale = 1_000_000_000L; label = "billion" };
  { scale = 1_000_000L;     label = "million" };
  { scale = 1_000L;         label = "thousand" };
]

let split_scale descriptor number : scale_result =
  let count = number / descriptor.scale in
  let remaining = number % descriptor.scale in
  match spell_chunk count with
  | None ->
    {
        part = None;
        remaining
    }
  | Some chunk ->
      {
        part = Some (join_with_space chunk descriptor.label);
        remaining
      }

let spell_remainder remainder =
  if remainder = 0L
      then None
      else Some (under_thousand remainder)

let apply_scale state descriptor =
  let result = split_scale descriptor state.remaining in
  {
        parts = result.part :: state.parts;
        remaining = result.remaining
  }

let collect_parts_with_remainder number =
  let initial_spelling_state = initial_state number in
  let final_spelling_state = List.fold large_scales ~init:initial_spelling_state ~f:apply_scale in
  spell_remainder final_spelling_state.remaining :: final_spelling_state.parts

let collect_spelled_parts parts =
  parts
  |> List.filter_opt
  |> List.rev

let spell_large_number number =
  let raw_parts = collect_parts_with_remainder number in
  let ordered_parts = collect_spelled_parts raw_parts in
  Ok (join_words ordered_parts)
