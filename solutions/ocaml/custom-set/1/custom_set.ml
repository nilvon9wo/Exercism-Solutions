module type ELEMENT = sig
  type t
  val compare : t -> t -> int
end

module Make (Element : ELEMENT) = struct
  type el = Element.t
  type t = el list

  type status = [ `Both | `OnlyA | `OnlyB ]

  let el_equal a b =
    Element.compare a b = 0

  let is_empty set =
    set = []

  let is_member set element =
    let predicate x = el_equal x element in
    List.exists predicate set

  let is_subset left right =
    let predicate x = is_member right x in
    List.for_all predicate left

  let is_disjoint left right =
    let predicate x = not (is_member right x) in
    List.for_all predicate left

  let equal a b =
    is_subset a b
        && is_subset b a

  let add_unique accumulator element =
    if is_member accumulator element
        then accumulator
        else element :: accumulator

  let of_list elements =
    List.fold_left add_unique [] elements

  let add set element =
    if is_member set element
        then set
        else element :: set

  (* Compute the status of a single element *)
  let status_of_element element set_a set_b =
    let in_a = is_member set_a element in
    let in_b = is_member set_b element in
    match in_a, in_b with
    | true, true -> `Both
    | true, false -> `OnlyA
    | false, true -> `OnlyB
    | false, false -> failwith "Impossible: element in neither set"

  let predicate_applies_to_element predicate set_a set_b element =
    let element_status = status_of_element element set_a set_b in
    predicate element_status

  let diff_filter (predicate : status -> bool) (a : t) (b : t) : t =
    let foo19 = predicate_applies_to_element predicate a b in
    let foo7 = a @ b in
    List.filter foo19 foo7

  let only_in_a = function
    | `OnlyA -> true
    | _ -> false

  let difference set_a set_b =
    diff_filter only_in_a set_a set_b

  let both_sets = function
    | `Both -> true
    | _ -> false

  let intersect set_a set_b =
    diff_filter both_sets set_a set_b

  let add_to_accumulator accumulator element =
    add accumulator element

  let union set_a set_b =
    List.fold_left add_to_accumulator set_a set_b

end
