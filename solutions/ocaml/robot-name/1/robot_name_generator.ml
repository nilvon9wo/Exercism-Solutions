open Base

let empty_name_set () =
    Set.empty (module String)

let assigned_names =
    ref (empty_name_set ())

let letter_at index =
    Char.of_int_exn (Char.to_int 'A' + index)

let digit_at index =
    Char.of_int_exn (Char.to_int '0' + index)

let letters =
    Array.init 26 ~f:letter_at

let digits =
    Array.init 10 ~f:digit_at

let get_random_index array =
        array
        |> Array.length
        |> Random.int

let random_element array =
    let index = get_random_index array in
    array.(index)

let random_letter () =
    random_element letters

let random_digit () =
    random_element digits

let character_for_position position =
    match position with
    | 0 -> random_letter ()
    | 1 -> random_letter ()
    | 2 -> random_digit ()
    | 3 -> random_digit ()
    | 4 -> random_digit ()
    | _ -> failwith "unreachable"

let build_name () =
    String.init 5 ~f:character_for_position

let name_is_assigned name =
    Set.mem !assigned_names name

let assign_name name =
    assigned_names := Set.add !assigned_names name

let rec generate () =
    let name = build_name () in
    if name_is_assigned name
        then generate ()
        else (
            assign_name name;
            name
        )

let release name =
    assigned_names := Set.remove !assigned_names name

let reset_global_names () =
    assigned_names := empty_name_set ()
