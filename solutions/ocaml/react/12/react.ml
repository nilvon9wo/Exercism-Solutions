type callback_id = int

type 'value_type cell_base = {
  mutable current_value: 'value_type;
  equality_fn: 'value_type -> 'value_type -> bool;
  mutable registered_callbacks: (callback_id * ('value_type -> unit)) list;
  mutable next_callback_id: int;
  mutable dependent_cells: 'value_type cell list;
}
and 'value_type cell =
  | Input of 'value_type cell_base
  | Compute of {
      base: 'value_type cell_base;
      compute_fn: unit -> 'value_type;
    }

let get_base_cell = function
     | Input base -> base
     | Compute { base; _ } -> base

let value_of cell =
    let base = get_base_cell cell in
    base.current_value

let add_dependent_cell cell new_cell =
  let base = get_base_cell cell in
  base.dependent_cells <- new_cell :: base.dependent_cells

let create_base_cell initial_value eq = {
    current_value = initial_value;
    equality_fn = eq;
    registered_callbacks = [];
    next_callback_id = 0;
    dependent_cells = []
}

let create_input_cell ~value ~eq =
  let base = create_base_cell value eq in
  Input base

let add_dependency bfs_queue dependency =
    Queue.add dependency bfs_queue

let invoke_callbacks dependency_base new_value =
  let call_callback new_value (_, callback_fn) = callback_fn new_value in
  let call_all = call_callback new_value in
  List.iter call_all dependency_base.registered_callbacks

let enqueue_dependents bfs_queue dependency_base =
  let add_dependent dependency = Queue.add dependency bfs_queue in
  List.iter add_dependent dependency_base.dependent_cells

let update_cell_value bfs_queue dependency_base new_value =
  dependency_base.current_value <- new_value;
  invoke_callbacks dependency_base new_value;
  enqueue_dependents bfs_queue dependency_base

let recompute_cell bfs_queue dependency_base compute_fn =
  let new_value = compute_fn () in
  let value_unchanged = dependency_base.equality_fn new_value dependency_base.current_value in
  if not value_unchanged
        then update_cell_value bfs_queue dependency_base new_value

let process_current_cell bfs_queue visited_cells current_cell =
  Hashtbl.add visited_cells current_cell ();
  match current_cell with
  | Compute { base = dependency_base; compute_fn } ->
      recompute_cell bfs_queue dependency_base compute_fn
  | Input _ ->
      ()

let propagate_changes_from_queue bfs_queue visited_cells =
    while not (Queue.is_empty bfs_queue) do
            let current_cell = Queue.pop bfs_queue in
            let is_visited = Hashtbl.mem visited_cells current_cell in
            if not is_visited
                then process_current_cell bfs_queue visited_cells current_cell
    done

let set_value_on_input base new_value =
          base.current_value <- new_value;
          let bfs_queue = Queue.create () in
          let visited_cells = Hashtbl.create 16 in
          List.iter (add_dependency bfs_queue) base.dependent_cells;
          propagate_changes_from_queue bfs_queue visited_cells

let set_value input_cell new_value =
  match input_cell with
  | Input base ->
      if base.equality_fn new_value base.current_value
        then ()
        else set_value_on_input base new_value
  | Compute _ -> failwith "Cannot set value of compute cell"

let create_compute_cell_1 dependency_cell ~f ~eq =
  let initial_value = f (value_of dependency_cell) in
  let new_cell = Compute {
        base = create_base_cell initial_value eq;
        compute_fn = (fun () -> f (value_of dependency_cell))
  } in
  let _ = add_dependent_cell dependency_cell new_cell in
  new_cell

let create_compute_cell_2 dependency_cell_1 dependency_cell_2 ~f ~eq =
  let initial_value = f (value_of dependency_cell_1) (value_of dependency_cell_2) in
  let new_cell = Compute {
        base = create_base_cell initial_value eq;
        compute_fn = (fun () -> f (value_of dependency_cell_1) (value_of dependency_cell_2))
  } in
  let _ = add_dependent_cell dependency_cell_1 new_cell in
  let _ = add_dependent_cell dependency_cell_2 new_cell in
  new_cell

let add_callback cell ~k =
  let base = get_base_cell cell in
  let id = base.next_callback_id in
  base.next_callback_id <- id + 1;
  base.registered_callbacks <- (id, k) :: base.registered_callbacks;
  id

let remove_callback cell callback_id =
  let base = get_base_cell cell in
  let is_different_id callback_id (id, _) = id <> callback_id in
  let filter_fn = is_different_id callback_id in
  base.registered_callbacks <- List.filter filter_fn base.registered_callbacks