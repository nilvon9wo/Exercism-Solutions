open Search_context

let rec search context left right =
  if left > right then
    Error "value not in array"
  else
    let mid = (left + right) / 2 in
    let mid_value = context.array.(mid) in
    let target = context.target in

    if mid_value = target
    then Ok mid
    else if target < mid_value
        then search context left (mid - 1)
        else search context (mid + 1) right

let find array target =
  let context = { array; target } in
  let right_bound = Array.length array - 1 in
  search context 0 right_bound
