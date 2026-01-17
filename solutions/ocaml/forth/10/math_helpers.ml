let do_math (context : Context.t) (f : int -> int -> int) : Context.t =
  Context.with_stack context (fun stack ->
    let _ = List_helpers.require_exactly_two_values !stack in
    let x = List_helpers.shift stack in
    let y = List_helpers.shift stack in
    let result = f x y in
    stack := !stack @ [result]
  )

let add (context : Context.t) : Context.t =
     do_math context (fun x y -> x + y)

let subtract (context : Context.t) : Context.t =
     do_math context (fun x y -> x - y)

let multiply (context : Context.t) : Context.t =
     do_math context (fun x y -> x * y)

let divide (context : Context.t) : Context.t =
    do_math context (fun x y -> x / y)
