let duplicate_last (context : Context.t) : Context.t =
  Context.with_stack context (fun stack ->
    let last =
      !stack
      |> List_helpers.require_at_least_one_value
      |> List_helpers.get_last_element
    in
    stack := !stack @ [last]
  )

let drop_last (context : Context.t) : Context.t =
  Context.with_stack context (fun stack ->
    let _ = List_helpers.require_at_least_one_value !stack in
    let _ = List_helpers.pop stack in
    ()
  )

let swap_last (context : Context.t) : Context.t =
  Context.with_stack context (fun stack ->
    let _ = List_helpers.require_at_least_two_values !stack in
    let last_value = List_helpers.pop stack in
    let penultimate_value = List_helpers.pop stack in
    stack := !stack @ [last_value; penultimate_value]
  )

let penultimate_value_copy (context : Context.t) : Context.t =
  Context.with_stack context (fun stack ->
    let penultimate_value =
      !stack
      |> List_helpers.require_at_least_two_values
      |> fun lst -> List.nth lst (List.length lst - 2)
    in
    stack := !stack @ [penultimate_value]
  )
